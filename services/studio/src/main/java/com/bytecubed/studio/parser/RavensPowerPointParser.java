package com.bytecubed.studio.parser;

import com.bytecubed.commons.PlayCard;
import com.bytecubed.commons.models.Placement;
import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.commons.models.Route;
import org.apache.poi.sl.usermodel.TextShape;
import org.apache.poi.xslf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class RavensPowerPointParser implements PlayCardParser {
    private XMLSlideShow ppt;
    static int maxX = 1000;
    static int maxY = 600;
    static int lineOfScrimage = 400;

    private Logger logger = LoggerFactory.getLogger(RavensPowerPointParser.class);

    public RavensPowerPointParser(XMLSlideShow ppt) {
        this.ppt = ppt;
    }

    private PlayCard extractPlayersOnSlide(XSLFSlide slide) {

        slide.getShapes().forEach(x -> logger.debug("Shape:  " + x.getShapeName()));

        return slide.getShapes().stream()
                .filter(this::isOnCanvas)
                .map(s -> {
                    List<PlayerMarker> markers = new ArrayList();
                    PlayerMarker placement = playerExtractor(s);
                    if (placement != null)
                        markers.add(placement);

                    markers.addAll(nestedPlayerExtractor(s));
                    return new PlayCard(LocalDateTime.now(), UUID.randomUUID(), UUID.randomUUID(), markers, "foo");
                }).findFirst().get();

        /*playerMarkers.forEach(f -> {
            String pos = f.isCenter() ? "center" : "wr";
            logger.debug("{ placement: { relativeX: " + f.getPlacement().getRelativeX() + ", relativeY: "
                    + f.getPlacement().getRelativeY() + "}, pos: \"" + pos + "\", tag: \"" + f.getTag() + "\" },");
        });*/
    }

    private List<PlayerMarker> nestedPlayerExtractor(XSLFShape s) {
        List<PlayerMarker> placements = new ArrayList<>();

        if (s.getShapeName().contains("Group")) {
            XSLFGroupShape shape = (XSLFGroupShape) s;

            List<XSLFShape> groupShapes = new ArrayList(shape.getShapes());
            groupShapes.stream()
                    .filter(f -> f.getShapeName().contains("Oval") || f.getShapeName().contains("Rectangle"))
                    .forEach(f -> {
                        PlayerMarker playerMarker = playerExtractor(f);
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
            return new PlayerMarker(new Placement(translatedX, translatedY), "wr", ((TextShape) shape).getText(), isCenter);
        }

        return null;
    }

    @Override
    public List<PlayCard> extractPlayCards() {
        return ppt.getSlides()
                .stream()
                .map(this::extractPlayersOnSlide)
                .collect(toList());
    }

    private boolean isOnCanvas(XSLFShape s) {
        return s.getAnchor().getX() < maxX && s.getAnchor().getY() < maxY
                && s.getAnchor().getX() >= 0 && s.getAnchor().getY() >= 0;

    }

    public List<Route> getRoutes() {

        ppt.getSlides().get(0).getShapes().stream()
                .forEach(f -> logger.debug("Shape:  " + f.getClass().getName() + " name:  " + f.getShapeName()));

        ppt.getSlides().get(0).getShapes().stream()
                .filter(this::isOnCanvas)
                .filter(f -> f.getShapeName().contains("Straight"))
                .forEach(f -> {
                    logger.debug((f.getClass().getName()));
                    logger.debug(f.getXmlObject().xmlText());
                    Rectangle2D bounds = f.getAnchor().getBounds2D();

                    XSLFAutoShape shape = (XSLFAutoShape) f;

                    System.out.println("Is flip:  " + shape.getFlipVertical());
//                    System.out.println("Rotation:  " + shape.getRotation());
//                    System.out.println("Start:  " + f.getAnchor().getBounds2D().getX());
//                    System.out.println("Center:  " + f.getAnchor().getBounds2D().getCenterX());

                    if (shape.getFlipVertical()) {

                        System.out.println(newX(bounds.getMaxX()) + " " + newY(bounds.getMinY()) + " " +
                                newX(bounds.getMinX()) + " " + newY(bounds.getMaxY()));
                    } else {

                        System.out.println(newX(bounds.getMinX()) + " " + newY(bounds.getMaxY()) + " " +
                                newX(bounds.getMaxX()) + " " + newY(bounds.getMinY()));
                    }

                    System.out.println();
//                    print(extract(f));
                });

        ppt.getSlides().get(0)
                .getShapes()
                .stream()
                .filter(this::isOnCanvas)
                .filter(f -> f.getShapeName().contains("Free"))
                .map(this::extract)
                .collect(toList())
                .forEach(this::print);
        return null;
    }

    private void print(List<Line2D.Double> p) {
        for (Line2D.Double line : p) {
            System.out.println(newX(line.x1) + " " + newY(line.y1) + " " + newX(line.x2) + " " + newY(line.y2));
        }
        System.out.println("done");
    }

    private ArrayList<Line2D.Double> extract(XSLFShape f) {
        logger.debug((f.getClass().getName()));

        logger.debug(f.getXmlObject().xmlText());
        XSLFFreeformShape connector = (XSLFFreeformShape) f;
        PathIterator pathIterator = connector.getPath().getPathIterator(new AffineTransform());
        logger.debug("Path Iterator:  " + pathIterator.getClass().getName());
        while (!pathIterator.isDone()) {
            pathIterator.next();
        }

        ArrayList<double[]> areaPoints = new ArrayList<double[]>();
        ArrayList<Line2D.Double> areaSegments = new ArrayList<Line2D.Double>();
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


        return areaSegments;
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
}
