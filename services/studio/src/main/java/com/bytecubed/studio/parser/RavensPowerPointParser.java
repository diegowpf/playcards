package com.bytecubed.studio.parser;

import com.bytecubed.commons.Formation;
import com.bytecubed.commons.PlayCard;
import com.bytecubed.commons.models.Placement;
import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.commons.models.movement.CustomMoveDescriptor;
import com.bytecubed.commons.models.movement.CustomRoute;
import com.bytecubed.commons.models.movement.MoveDescriptor;
import com.bytecubed.studio.parser.routes.BasicRouteStrategy;
import com.bytecubed.studio.parser.routes.RouteStrategy;
import org.apache.poi.xslf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.*;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

public class RavensPowerPointParser implements PlayCardParser {
    public static int maxX = 1000;
    public static int maxY = 600;
    public static int lineOfScrimage = 400;
    private XMLSlideShow ppt;
    private RouteStrategy routeStrategy;

    private Logger logger = LoggerFactory.getLogger(RavensPowerPointParser.class);

    public RavensPowerPointParser(XMLSlideShow ppt, RouteStrategy routeStrategy) {
        this.ppt = ppt;
        this.routeStrategy = routeStrategy;
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
        logger.debug("Shape count after:  " + slide.getShapes().size());

        List<PlayerMarker> playerMarkers = new ArrayList<>();
        slide.getShapes().stream()
                .filter(RavensPowerPointParser::isOnCanvas)
                .filter(f -> !(f instanceof XSLFGroupShape))
                .forEach(s -> {
                    PlayerMarker placement = playerExtractor(s);
                    if (placement != null) {
                        entityRegistry.register(placement, s);
                        playerMarkers.add(placement);
                    }
                });

        slide.getShapes().stream()
                .filter(RavensPowerPointParser::isOnCanvas)
                .forEach(s -> playerMarkers.addAll(nestedPlayerExtractor(entityRegistry, s)));

        PlayCard playCard = new PlayCard(UUID.randomUUID(), new Formation(playerMarkers), getName(slide));
        routeStrategy.buildRoutes(slide, entityRegistry);

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

    public static boolean isOnCanvas(XSLFShape s) {
        return s.getAnchor().getX() < maxX && s.getAnchor().getY() < maxY
                && s.getAnchor().getX() >= 0 && s.getAnchor().getY() >= 0;

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

    public List<CustomRoute> buildRoutes(XSLFSlide slide, ShapeToEntityRegistry shapeToEntityRegistry) {
        return routeStrategy.buildRoutes(slide,shapeToEntityRegistry);
    }
}
