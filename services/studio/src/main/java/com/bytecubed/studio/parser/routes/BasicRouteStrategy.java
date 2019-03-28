package com.bytecubed.studio.parser.routes;

import com.bytecubed.commons.models.Placement;
import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.commons.models.movement.CustomMoveDescriptor;
import com.bytecubed.commons.models.movement.CustomRoute;
import com.bytecubed.commons.models.movement.MoveDescriptor;
import com.bytecubed.studio.parser.RavensPowerPointParser;
import com.bytecubed.studio.parser.ShapeToEntityRegistry;
import org.apache.poi.xslf.usermodel.XSLFFreeformShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSimpleShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.bytecubed.studio.parser.RavensPowerPointParser.lineOfScrimage;
import static com.bytecubed.studio.parser.RavensPowerPointParser.maxX;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

public class BasicRouteStrategy implements RouteStrategy {

    Logger logger = LoggerFactory.getLogger(BasicRouteStrategy.class);

    @Override
    //Todo: This should functionality of a playcard at this point but it is easier to build here.
    public List<CustomRoute> buildRoutes(XSLFSlide slide, ShapeToEntityRegistry entityRegistry) {

        List<CustomRoute> routes = new ArrayList(extractStraightRoutes(slide, entityRegistry));
        routes.addAll(slide
                .getShapes()
                .stream()
                .filter(RavensPowerPointParser::isOnCanvas)
                .filter(f -> f.getShapeName().contains("Free"))
                .map(f -> extractCurvedRoutes(f, entityRegistry))
                .collect(toList()));

        logger.debug("About to build routes.");
        mergeRoutes(routes);
        return correctAlignment(routes);
    }

    private List<CustomRoute> correctAlignment(List<CustomRoute> routes) {
        return routes;
    }

    private void mergeRoutes(List<CustomRoute> routes) {
        logger.debug("Routes: ");

        routes.stream().forEach(s -> logger.debug("Merging routes:  " + s.getPlayer()));

        Map<Boolean, List<CustomRoute>> partitionedRoutes = routes.stream()
                .collect(partitioningBy(s -> s.getPlayer() != null));

        logger.debug(partitionedRoutes.keySet().toString());

        List<CustomRoute> associatedWithPlayers = partitionedRoutes.get(true);
        List<CustomRoute> notAssociatedWithPlayers = partitionedRoutes.get(false);

        logger.debug("Number of associated routes:  " + associatedWithPlayers.size());
        logger.debug("Number of un-associated routes:  " + notAssociatedWithPlayers.size());


        associatedWithPlayers.forEach(r -> notAssociatedWithPlayers.forEach(n -> extendRouteIfPossible(r, n)));

        routes.forEach(r -> {


            r.getMoveDescriptors().forEach(d -> {
                Placement start = ((CustomMoveDescriptor) d).getStart();
                Placement end = ((CustomMoveDescriptor) d).getEnd();
                logger.debug("[" + start.getRelativeX() + "," + start.getRelativeY() + "]->["
                        + end.getRelativeX() + "," + end.getRelativeY() + "]");
            });

        });
    }

    private List<CustomRoute> extractStraightRoutes(XSLFSlide slide, ShapeToEntityRegistry entityRegistry) {
        List<CustomRoute> routes = new ArrayList();

        slide.getShapes().stream()
                .filter(RavensPowerPointParser::isOnCanvas)
                .filter(f -> f.getShapeName().contains("Straight"))
                .forEach(f -> {
                    XSLFSimpleShape shape = (XSLFSimpleShape) f;

                    PlayerMarker player = entityRegistry.getNearestPlayer(f);
                    String nearestPlayerMarker = player == null ? null : player.getTag();
                    logger.debug("Nearest Player:  " + nearestPlayerMarker);
                    logger.debug("Rotation:  " + ((XSLFSimpleShape) f).getRotation());
                    logger.debug("Horizontal Flip:  " + ((XSLFSimpleShape) f).getFlipHorizontal());
                    logger.debug("Vertical Flip:  " + ((XSLFSimpleShape) f).getFlipVertical());


                    Line2D.Double line = extractAsLine(shape, true);
                    Line2D.Double convertedLine;

                    if (shape.getFlipVertical()) {
                        logger.debug("Flip Vertical");
                        convertedLine = new Line2D.Double(line.x2, line.y2, line.x1, line.y1);
                        if (!shape.getFlipHorizontal()) {
                            convertedLine = new Line2D.Double(line.x1, line.y2, line.x2, line.y1);
                        }
                    } else {
                        logger.debug("No Flip");
                        convertedLine = new Line2D.Double(line.x2, line.y2, line.x1, line.y1);
                        //This does not make sense.
                        if (shape.getFlipHorizontal()) {
                            convertedLine = new Line2D.Double(line.x2, line.y1, line.x1, line.y2);
                        }
                    }

                    List<MoveDescriptor> moveDescriptors = new ArrayList();
                    moveDescriptors.add(new CustomMoveDescriptor(
                            new Placement(convertedLine.x1, convertedLine.y1),
                            new Placement(convertedLine.x2, convertedLine.y2)));

                    CustomRoute e = new CustomRoute(moveDescriptors, nearestPlayerMarker);

                    if (player != null) {
                        player.addRoute(e);
                    } else {
                        logger.debug("Not adding route.");
                        logger.debug("Nearest player:  " + nearestPlayerMarker);
                    }

                    routes.add(e);
                });

        return routes;
    }

    private Line2D.Double extractAsLine(XSLFShape shape, boolean b) {
        Rectangle2D bounds = shape.getAnchor().getBounds2D();
        logger.debug("Line is represented as:  " + shape.getClass().getName());
        logger.debug(shape.getXmlObject().toString());

        return new Line2D.Double(x(bounds.getMinX()),
                y(bounds.getMinY()),
                x(bounds.getMaxX()),
                y(bounds.getMaxY()));
    }


    private CustomRoute extractCurvedRoutes(XSLFShape f, ShapeToEntityRegistry entityRegistry) {
        XSLFFreeformShape connector = (XSLFFreeformShape) f;
        PathIterator pathIterator = connector.getPath().getPathIterator(new AffineTransform());

        PlayerMarker nearestPlayer = entityRegistry.getNearestPlayer(f);
        if (nearestPlayer != null)
            logger.debug("This is the nearest player:  " + nearestPlayer.getTag());

        while (!pathIterator.isDone()) {
            pathIterator.next();
        }

        ArrayList<double[]> areaPoints = new ArrayList<>();
        ArrayList<Line2D.Double> areaSegments = new ArrayList<>();
        double[] coords = new double[6];

        for (PathIterator pi = new FlatteningPathIterator(connector.getPath().getPathIterator(null), 1.0); !pi.isDone(); pi.next()) {
            // The type will be SEG_LINETO, SEG_MOVETO, or SEG_CLOSE
            // Because the Area is composed of straight lines
            int type = pi.currentSegment(coords);
            // We record a double array of {segment type, x coord, y coord}
            double[] pathIteratorCoords = {type, coords[0], coords[1]};
            areaPoints.add(pathIteratorCoords);
        }

        double[] start = new double[3]; // To record where each polygon starts

        for (int i = 0; i < areaPoints.size(); i++) {
            // If we're not on the last point, return a line from this point to the next
            double[] currentElement = areaPoints.get(i);

            // We need a default value in case we've reached the end of the ArrayList
            double[] nextElement = {-1, -1, -1};
            if (i < areaPoints.size() - 1) {
                nextElement = areaPoints.get(i + 1);
            }

            // Make the lines
            if (currentElement[0] == PathIterator.SEG_MOVETO) {
                start = currentElement; // Record where the polygon started to close it later
                System.out.println("This is the start:  " + start[0] + "| " + start[1]);
            }

            if (nextElement[0] == PathIterator.SEG_LINETO) {
                areaSegments.add(
                        new Line2D.Double(
                                currentElement[1], currentElement[2],
                                nextElement[1], nextElement[2]
                        )
                );
            } else if (nextElement[0] == PathIterator.SEG_CLOSE) {
                areaSegments.add(
                        new Line2D.Double(
                                currentElement[1], currentElement[2],
                                start[1], start[2]
                        )
                );
            }
        }

        String playerTag = "";
        if (nearestPlayer != null) {
            playerTag = nearestPlayer.getTag();
        }

        CustomRoute route = new CustomRoute(areaSegments.stream()
                .map(b -> new CustomMoveDescriptor(
                        new Placement(x(b.x1), y(b.y1)),
                        new Placement(x(b.x2), y(b.y2))))
                .collect(toList()), playerTag);

        if (nearestPlayer != null)
            nearestPlayer.addRoute(route);

        return route;
    }



    private void extendRouteIfPossible(CustomRoute associatedRoute, CustomRoute nonAssociatedRoute) {
        boolean notChanged = false;

        logger.debug("Number of associated routes - extended:  " + associatedRoute.getMoveDescriptors().size());
        logger.debug("Number of un-associated routes - extended:  " + nonAssociatedRoute.getMoveDescriptors().size());

        while (!notChanged) {
            List<MoveDescriptor> extension = new ArrayList();

            associatedRoute.getMoveDescriptors().forEach(associatedMovement -> {
                nonAssociatedRoute.getMoveDescriptors().forEach(nonAssociatedMovement -> {
                    if (((CustomMoveDescriptor) associatedMovement).collidesWith((CustomMoveDescriptor) nonAssociatedMovement)
                            && !associatedRoute.getMoveDescriptors().contains(nonAssociatedMovement)) {
                        extension.add(nonAssociatedMovement);
                        logger.debug("Incorporating route");
                    }
                });
            });

            associatedRoute.getMoveDescriptors().addAll(extension);
            notChanged = extension.isEmpty();
        }
    }






    private double y(double y) {
        double adjustedY = y - lineOfScrimage;
        return ((adjustedY / 200) * 30);
    }

    private double x(double x) {
        return (x / maxX) * 160;
    }
}
