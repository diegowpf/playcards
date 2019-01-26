package com.bytecubed.studio.parser;

import com.bytecubed.commons.models.PlayerMarker;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class ShapeToEntityRegistry {


    private Logger logger = LoggerFactory.getLogger(ShapeToEntityRegistry.class);
    private Map<XSLFShape, PlayerMarker> shapeRegistry;

    public ShapeToEntityRegistry() {
        shapeRegistry = new HashMap<>();
    }

    public void register(PlayerMarker marker, XSLFShape shape) {
        shapeRegistry.put(shape, marker);
    }

    public PlayerMarker getNearestPlayer(Point2D point){
        return null;
    }

    public PlayerMarker getNearestPlayer(XSLFShape shape) {
        PlayerMarker nearestPlayer = null;

        logger.debug("Bounds:  " + shape.getAnchor().getBounds2D());

        for (XSLFShape playerShape : shapeRegistry.keySet()) {
            printBoundingBox(playerShape);
            double nearestPoints = distance(playerShape, shape);
            logger.debug("Nearest Distance:  " + nearestPoints);

            if (nearestPoints < 20) {
                logger.debug("Found intersection....");
                nearestPlayer = shapeRegistry.get(playerShape);
                logger.debug("Found a nearest player:  " + playerShape);
            }

        }

        return nearestPlayer;
    }

    private void printBoundingBox(XSLFShape shape) {
        logger.debug("Bounding Box:  " + shape.getAnchor().getBounds2D());
    }

    private double distance(XSLFShape shape1, XSLFShape shape2) {
        List<Point2D> shape1Points = getBoundsAsPoints(shape1.getAnchor().getBounds2D());
        List<Point2D> shape2Points = getBoundsAsPoints(shape2.getAnchor().getBounds2D());


        return getMinimumDistance(shape1Points, shape2Points);

    }

    private double getMinimumDistance(List<Point2D> shape1Points, List<Point2D> shape2Points) {
        double distance = Double.MAX_VALUE;
        for (Point2D p : shape1Points) {
            for (Point2D q : shape2Points) {
                double latestDistance = Math.abs(p.distance(q));
                if (distance > latestDistance) {
                    distance = latestDistance;
                }
            }
        }

        return distance;
    }

    private List<Point2D> getBoundsAsPoints(Rectangle2D shape1Bounds) {
        return asList(new Point2D.Double(shape1Bounds.getMinX(), shape1Bounds.getMinY()),
                new Point2D.Double(shape1Bounds.getMaxX(), shape1Bounds.getMinY()),
                new Point2D.Double(shape1Bounds.getMaxX(), shape1Bounds.getMaxY()),
                new Point2D.Double(shape1Bounds.getMinX(), shape1Bounds.getMaxY()));
    }

    private Point2D.Double getAsPoint(XSLFShape shape) {


        return new Point2D.Double(shape.getAnchor().getCenterX(),
                shape.getAnchor().getCenterY());
    }


}
