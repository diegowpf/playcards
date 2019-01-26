package com.bytecubed.studio.parser;

import com.bytecubed.commons.Formation;
import com.bytecubed.commons.PlayCard;
import com.bytecubed.commons.models.Placement;
import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.commons.models.movement.CustomMoveDescriptor;
import com.bytecubed.commons.models.movement.MoveDescriptor;
import com.bytecubed.commons.models.movement.Route;
import org.apache.poi.xslf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.*;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

public class RavensPowerPointParser implements PlayCardParser {
    private static int maxX = 1000;
    private static int maxY = 600;
    private static int lineOfScrimage = 400;
    private XMLSlideShow ppt;

    private Logger logger = LoggerFactory.getLogger(RavensPowerPointParser.class);

    public RavensPowerPointParser(XMLSlideShow ppt) {
        this.ppt = ppt;
    }

    private PlayCard extractPlayersOnSlide(XSLFSlide slide) {
        ShapeToEntityRegistry entityRegistry = new ShapeToEntityRegistry();
        List<XSLFGroupShape> groupedShapes = slide.getShapes().stream()

                .filter(f->f.getClass().getName().equals(XSLFGroupShape.class.getName()))
                .filter(f->((XSLFGroupShape)f).getShapes()
                        .stream()
                        .anyMatch(x->x.getShapeName().contains("Rect")))
                .map(f->(XSLFGroupShape) f)
                .collect(toList());

        logger.debug("Only this many grouped shapes:  "  + groupedShapes.size());
        logger.debug("Shape count first:  " + slide.getShapes().size());
//        unGroupShapes(slide, groupedShapes);
        logger.debug("Shape count after:  " + slide.getShapes().size());

        List<PlayerMarker> playerMarkers = new ArrayList<>();
        slide.getShapes().stream()
                .filter(this::isOnCanvas)
                .filter(f -> !(f instanceof XSLFGroupShape))
                .forEach(s -> {
                    PlayerMarker placement = playerExtractor(s);
                    if (placement != null) {
                        entityRegistry.register(placement, s);
                        playerMarkers.add(placement);
                    }
                });

        slide.getShapes().stream()
                .filter(this::isOnCanvas)
                .forEach(s -> playerMarkers.addAll(nestedPlayerExtractor(entityRegistry, s)));

        PlayCard playCard = new PlayCard(UUID.randomUUID(), new Formation(playerMarkers), getName(slide));
        buildRoutes(slide, entityRegistry);

        return playCard;
    }

    private void unGroupShapes(XSLFSlide slide, List<XSLFGroupShape> groupedShapes) {

        groupedShapes.forEach( shape -> shape.getShapes().forEach(f->{

                XSLFAutoShape autoShape = slide.createAutoShape();
                autoShape.setShapeType(((XSLFAutoShape)f).getShapeType());
                autoShape.setAnchor(f.getAnchor());


                logger.debug("New X: " + autoShape.getAnchor().getX());
                logger.debug("New Y: " + autoShape.getAnchor().getY());

        }));

        groupedShapes.forEach(XSLFGroupShape::clear);
    }

    private List<PlayerMarker> nestedPlayerExtractor(ShapeToEntityRegistry registry, XSLFShape s) {
        List<PlayerMarker> placements = new ArrayList<>();

        if (s.getShapeName().contains("Group")) {
            XSLFGroupShape shape = (XSLFGroupShape) s;




            List<XSLFShape> groupShapes = new ArrayList(shape.getShapes());


            groupShapes.stream()
                    .filter(f -> f.getShapeName().contains("Oval") || f.getShapeName().contains("Rectangle"))
                    .forEach(f -> {




                        PlayerMarker playerMarker = playerExtractor(f);
                        registry.register(playerMarker, f);


                        logger.debug("Group X: " + f.getAnchor().getX());
                        logger.debug("Group Y: " + f.getAnchor().getY());

                        placements.add(playerMarker);
                    });
        }

        return placements;
    }

    private PlayerMarker playerExtractor(XSLFShape shape) {

        PlayerMarker marker = null;

        if (shape.getShapeName().contains("Oval") || shape.getShapeName().contains("Rect")) {
            logger.debug(shape.getClass().getName());
            boolean isCenter = shape.getShapeName().contains("Rect");
            int translatedX = (int) Math.round((shape.getAnchor().getX() / maxX) * 160);
            double adjustedY = shape.getAnchor().getY() - lineOfScrimage;
            int translatedY = (int) Math.round(((adjustedY / 200) * 30));
            marker = new PlayerMarker(new Placement(translatedX, translatedY),
                    PositionDetector.getPosition((XSLFAutoShape) shape), getText(shape), isCenter);
        }

        return marker;
    }

    //Todo: Such a hack
    private String getText(XSLFShape shape) {
        logger.debug("Shape:  " + shape.getClass().getName());
        return shape.getClass() == XSLFAutoShape.class ? ((XSLFAutoShape) shape).getText() : "";
    }

    @Override
    public List<PlayCard> extractPlayCards() {
        return ppt.getSlides().stream()
                .map(this::extractPlayersOnSlide)
                .collect(toList());
    }

    private boolean isOnCanvas(XSLFShape s) {
        return s.getAnchor().getX() < maxX && s.getAnchor().getY() < maxY
                && s.getAnchor().getX() >= 0 && s.getAnchor().getY() >= 0;

    }

    //Todo: This should functionality of a playcard at this point but it is easier to build here.
    public List<Route> buildRoutes(XSLFSlide slide, ShapeToEntityRegistry entityRegistry) {

        List<Route> routes = new ArrayList(extractStraightRoutes(slide, entityRegistry));
        routes.addAll(ppt.getSlides().get(0)
                .getShapes()
                .stream()
                .filter(this::isOnCanvas)
                .filter(f -> f.getShapeName().contains("Free"))
                .map(f -> extractCurvedRoutes(f, entityRegistry))
                .collect(toList()));

        logger.debug("About to build routes.");
        mergeRoutes(routes);

        return routes;
    }

    private void mergeRoutes(List<Route> routes) {
        logger.debug("Routes: ");

        routes.stream().forEach(s -> logger.debug("Merging routes:  " + s.getPlayer()));

        Map<Boolean, List<Route>> partitionedRoutes = routes.stream()
                .collect(partitioningBy(s -> s.getPlayer() != null));

        logger.debug(partitionedRoutes.keySet().toString());

        List<Route> associatedWithPlayers = partitionedRoutes.get(true);
        List<Route> notAssociatedWithPlayers = partitionedRoutes.get(false);

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

    private void extendRouteIfPossible(Route associatedRoute, Route nonAssociatedRoute) {
        boolean notChanged = false;

        logger.debug("Number of associated routes:  " + associatedRoute.getMoveDescriptors().size());
        logger.debug("Number of un-associated routes:  " + nonAssociatedRoute.getMoveDescriptors().size());

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


    private List<Route> extractStraightRoutes(XSLFSlide slide, ShapeToEntityRegistry entityRegistry) {
        List<Route> routes = new ArrayList();

        slide.getShapes().stream()
                .filter(this::isOnCanvas)
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

                    Route e = new Route(moveDescriptors, nearestPlayerMarker);

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


    private Route extractCurvedRoutes(XSLFShape f, ShapeToEntityRegistry entityRegistry) {
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

        Route route = new Route(areaSegments.stream()
                .map(b -> new CustomMoveDescriptor(
                        new Placement(x(b.x1), y(b.y1)),
                        new Placement(x(b.x2), y(b.y2))))
                .collect(toList()), playerTag);

        if (nearestPlayer != null)
            nearestPlayer.addRoute(route);

        return route;
    }

    private double y(double y) {
        double adjustedY = y - lineOfScrimage;
        return ((adjustedY / 200) * 30);
    }

    private double x(double x) {
        return (x / maxX) * 160;
    }

    public String getName(XSLFSlide slide) {

        int longest = 0;
        String longestText = "";

        List<XSLFTextBox> textBoxes = asList(slide.getShapes()
                .stream()
                .filter(s -> s.getShapeName().contains("Text"))
                .collect(toList()).toArray(new XSLFTextBox[0]));

        for (XSLFTextBox textBox : textBoxes) {
            if (longest < textBox.getText().length()) {
                longest = textBox.getText().length();
                longestText = textBox.getText();
            }
        }

        List<String> strings = Arrays.asList(longestText.split(" "))
                .stream()
                .filter(f -> !f.trim().equals(""))
                .collect(toList());

        logger.debug("Title Text: " + strings.toString());
        return strings.get(0).replaceAll("\t", " ").trim();
    }

}
