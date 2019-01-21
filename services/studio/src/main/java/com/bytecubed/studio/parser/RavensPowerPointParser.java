package com.bytecubed.studio.parser;

import com.bytecubed.commons.Formation;
import com.bytecubed.commons.PlayCard;
import com.bytecubed.commons.models.Placement;
import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.commons.models.movement.CustomMoveDescriptor;
import com.bytecubed.commons.models.movement.Move;
import com.bytecubed.commons.models.movement.MoveDescriptor;
import com.bytecubed.commons.models.movement.Route;
import org.apache.poi.xslf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.*;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class RavensPowerPointParser implements PlayCardParser {
    static int maxX = 1000;
    static int maxY = 600;
    static int lineOfScrimage = 400;
    private XMLSlideShow ppt;
    private ShapeToEntityRegistry entityRegistry;

    private Logger logger = LoggerFactory.getLogger(RavensPowerPointParser.class);

    public RavensPowerPointParser(XMLSlideShow ppt) {
        this.ppt = ppt;
        this.entityRegistry = new ShapeToEntityRegistry();
    }

    private PlayCard extractPlayersOnSlide(XSLFSlide slide) {
        List<PlayerMarker> playerMarkers = new ArrayList<>();
        slide.getShapes().stream()
                .filter(this::isOnCanvas)
                .forEach(s -> {
                    PlayerMarker placement = playerExtractor(s);
                    if (placement != null) {
                        entityRegistry.register(placement, s);
                        playerMarkers.add(placement);
                    }

                    playerMarkers.addAll(nestedPlayerExtractor(entityRegistry, s));
                });

        PlayCard playCard = new PlayCard(UUID.randomUUID(), new Formation(playerMarkers), getName());
        getRoutes(slide);

        playerMarkers.forEach(f -> {
            String pos = f.isCenter() ? "center" : "wr";
            logger.debug("{ placement: { relativeX: " + f.getPlacement().getRelativeX() + ", relativeY: "
                    + f.getPlacement().getRelativeY() + "}, pos: \"" + pos + "\", tag: \"" + f.getTag() + "\" },");
        });

        return playCard;
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

                        logger.debug("X: " + f.getAnchor().getX());
                        logger.debug("Y: " + f.getAnchor().getY());
                        placements.add(playerMarker);
                    });
        }

        return placements;
    }

    private PlayerMarker playerExtractor(XSLFShape shape) {
        if (shape.getShapeName().contains("Oval") || shape.getShapeName().contains("Rect")) {
            boolean isCenter = shape.getShapeName().contains("Rect");
            int translatedX = 0;
            int translatedY = 0;

            translatedX = (int) Math.round((shape.getAnchor().getX() / maxX) * 160);

            double adjustedY = shape.getAnchor().getY() - lineOfScrimage;

            translatedY = (int) Math.round(((adjustedY / 200) * 30));
            return new PlayerMarker(new Placement(translatedX, translatedY), "wr", getText(shape), isCenter);
        }

        return null;
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

    public List<Route> getRoutes(XSLFSlide slide) {

        List<Route> routes = new ArrayList(extractStraightRoutes(slide));
        routes.addAll( ppt.getSlides().get(0)
                .getShapes()
                .stream()
                .filter(this::isOnCanvas)
                .filter(f -> f.getShapeName().contains("Free"))
                .map(this::extract)
                .collect(toList()));


        return routes;
    }

    private Route convertToRoute(ArrayList<Line2D.Double> doubles) {
        return new Route(doubles.stream()
                .map(f -> new CustomMoveDescriptor(Move.custom,
                        new Placement(f.x1, f.y1),
                        new Placement(f.x2, f.y2)))
                .collect(toList()), "foo");
    }

    private List<Route> extractStraightRoutes(XSLFSlide slide) {
        List<Route> routes = new ArrayList();

        slide.getShapes().stream()
                .filter(this::isOnCanvas)
                .filter(f -> f.getShapeName().contains("Straight"))
                .forEach(f -> {
                    Rectangle2D bounds = f.getAnchor().getBounds2D();
                    XSLFSimpleShape shape = (XSLFSimpleShape) f;

                    PlayerMarker player = entityRegistry.getNearestPlayer(f);
                    String nearestPlayerMarker = player == null ? "" : player.getTag();
                    logger.debug("Nearest Player:  " + nearestPlayerMarker);

                    double x1, y1, x2, y2;

                    if (shape.getFlipVertical()) {

                        Line2D.Double line = extractAsLine(shape, true);

                        x1 = line.x1;
                        y1 = line.y1;
                        x2 = line.x2;
                        y2 = line.y2;

                        logger.debug(x1 + " " + y1 + " " + x2 + " " + y2);
                    } else {
                        x1 = newX(bounds.getMinX());
                        y1 = newY(bounds.getMaxY());
                        x2 = newX(bounds.getMaxX());
                        y2 = newY(bounds.getMinY());

                        logger.debug(x1 + " " + y1 + " " + x2 + " " + y2);
                    }

                    List<MoveDescriptor> moveDescriptors = new ArrayList();
                    moveDescriptors.add(new CustomMoveDescriptor(Move.custom,
                            new Placement(x1, y1),
                            new Placement(x2, y2)));

                    Route e = new Route(moveDescriptors, nearestPlayerMarker);

                    if (player != null) {
                        player.addRoute(e);
                    }

                    routes.add(e);
                });

        return routes;
    }

    private Line2D.Double extractAsLine(XSLFShape shape, boolean b) {
        Rectangle2D bounds = shape.getAnchor().getBounds2D();
        return new Line2D.Double(newX(bounds.getMaxX()),
                newY(bounds.getMinY()),
                newX(bounds.getMinX()),
                newY(bounds.getMaxY()));
    }

    private void print(List<Line2D.Double> p) {
        for (Line2D.Double line : p) {
            System.out.println(newX(line.x1) + " " + newY(line.y1) + " " + newX(line.x2) + " " + newY(line.y2));
        }
        System.out.println("done");
    }

    private Route extract(XSLFShape f) {
        logger.debug((f.getClass().getName()));

        logger.debug(f.getXmlObject().xmlText());
        XSLFFreeformShape connector = (XSLFFreeformShape) f;
        PathIterator pathIterator = connector.getPath().getPathIterator(new AffineTransform());
        logger.debug("Path Iterator:  " + pathIterator.getClass().getName());

        PlayerMarker nearestPlayer = entityRegistry.getNearestPlayer(f);
        if( nearestPlayer != null )
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

        System.out.println("Size of points:  " + areaPoints.size());

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
        if( nearestPlayer != null ){
            playerTag = nearestPlayer.getTag();
        }

        Route route = new Route( areaSegments.stream()
                .map(b->new CustomMoveDescriptor(Move.custom, new Placement(b.x1, b.y1), new Placement(b.x2, b.y2)) )
                .collect(toList()), playerTag );

        if( nearestPlayer != null )
            nearestPlayer.addRoute(route);

        return route;
    }

    private double newY(double y) {
        double adjustedY = y - lineOfScrimage;
        if (true) return (((adjustedY / 200) * 30) * 8.4) + 510;

        if (true) return (y * 8.4) + 510;
        if (false) return y;
        return y + 80;
    }

    private double newX(double x) {
        if (false) return x;
        return (x / maxX) * 160 * 8.4;
    }

    public String getName() {

        int longest = 0;
        String longestText = "";

        List<XSLFTextBox> textBoxes = asList(this.ppt.getSlides().get(0).getShapes()
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

    public class ShapeToEntityRegistry {

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
            Point2D points = getAsPoint(shape);
            PlayerMarker nearestPlayer = null;
            double nearestDistance = Double.MIN_VALUE;

            logger.debug("Bounds:  " + shape.getAnchor().getBounds2D());

            for (XSLFShape playerShape : shapeRegistry.keySet()) {
                printBoundingBox(playerShape);
//                Point2D playerShapePoint = getAsPoint(playerShape);
//                double distance = Math.abs(playerShapePoint.distance(points));

//                logger.debug("Distance:  " + distance);
                double nearestPoints = distance(playerShape, shape);
                logger.debug("Nearest Distance:  " + nearestPoints);

                if (nearestPoints < 20) {
                    logger.debug("Found intersection....");
//                    nearestDistance = distance;
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
}
