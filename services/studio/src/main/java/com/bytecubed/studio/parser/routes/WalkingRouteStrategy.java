package com.bytecubed.studio.parser.routes;

import com.bytecubed.commons.models.movement.CustomRoute;
import com.bytecubed.studio.parser.ShapeToEntityRegistry;
import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFConnectorShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.stream.Collectors;

public class WalkingRouteStrategy implements RouteStrategy{

    Logger logger = LoggerFactory.getLogger(WalkingRouteStrategy.class);

    @Override
    public List<CustomRoute> buildRoutes(XSLFSlide slide, ShapeToEntityRegistry entityRegistry) {
        List<XSLFConnectorShape> routeSegments = slide.getShapes().stream()
                .filter(this::isNavigableShape)
                .map(f->((XSLFConnectorShape) f ))
                .collect(Collectors.toList());

        List<XSLFAutoShape> players = slide.getShapes().stream()
                .filter(this::isPlayerIcon)
                .map(f->((XSLFAutoShape) f ))
                .collect(Collectors.toList());

        List<XSLFConnectorShape> initialMoves = extractInitialMoves(routeSegments, players);
        return null;
    }

    private List<XSLFConnectorShape> extractInitialMoves(List<XSLFConnectorShape> routeSegments, List<XSLFAutoShape> players) {

        logger.debug( "Extracting initial moves" );

        for( XSLFAutoShape player : players ){
            XSLFConnectorShape closestConnector = routeSegments.get(0);
            double distance = getDistance(player, closestConnector);
            for(XSLFConnectorShape connector : routeSegments ){
                logger.debug("Calculated Distance:  " + distance );
                if( getDistance(player, connector) < distance ){
                    closestConnector = connector;
                    distance = getDistance(player,connector);
                }
            }

        }
        return null;
    }

    private double getDistance(XSLFAutoShape player, XSLFConnectorShape closestConnector) {
        double abs = Math.abs(Point2D.distance(
                player.getAnchor().getCenterX(),
                player.getAnchor().getCenterY(),
                LineUtility.normalLine(closestConnector).x1,
                LineUtility.normalLine(closestConnector).y1));
        logger.debug("Distance calculated:  " + abs);
        return abs;
    }


    private boolean isPlayerIcon(XSLFShape xslfShape) {
        logger.debug("This is the resolved shape name:  " +xslfShape.getShapeName() + ":  " + xslfShape.getClass().getName());
        return xslfShape.getShapeName().contains("Oval");
    }

    private boolean isNavigableShape(XSLFShape xslfShape) {
        logger.debug( "This is the shape: " + xslfShape.getClass().getName()  );
        return xslfShape.getClass() == XSLFConnectorShape.class;
    }

    public static class LineUtility{
        public static Line2D.Double normalLine(XSLFConnectorShape shape ){
            Rectangle2D anchor = shape.getAnchor();
            return new Line2D.Double(anchor.getMinX(), anchor.getMinY(), anchor.getMaxX(), anchor.getMaxY());
        }

        public static Line2D.Double flipAlongXAxis(Line2D.Double line ){
            return null;
        }
    }
}
