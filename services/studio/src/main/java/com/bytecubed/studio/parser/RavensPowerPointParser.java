package com.bytecubed.studio.parser;

import com.bytecubed.commons.models.Placement;
import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.commons.models.Route;
import org.apache.poi.sl.usermodel.TextShape;
import org.apache.poi.xslf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

public class RavensPowerPointParser implements PlayCardParser {
    private XMLSlideShow ppt;
    static int maxX = 1000;
    static int maxY = 600;
    static int lineOfScrimage = 400;

    private Logger logger = LoggerFactory.getLogger(RavensPowerPointParser.class);

    public RavensPowerPointParser(XMLSlideShow ppt) {
        this.ppt = ppt;
    }

    private List<PlayerMarker> extractPlayersOnSlide(XSLFSlide slide) {
        List<PlayerMarker> playerMarkers = new ArrayList<>();

        slide.getShapes().forEach(x-> logger.debug( "Shape:  " + x.getShapeName()));

        slide.getShapes().stream()
                .filter(this::isOnCanvas)
                .forEach(s -> {
                    PlayerMarker placement = playerExtractor(s);
                    if (placement != null)
                        playerMarkers.add(placement);

                    playerMarkers.addAll(nestedPlayerExtractor(s));
                });

        playerMarkers.forEach(f -> {
            String pos = f.isCenter() ? "center" : "wr";
            logger.debug("{ placement: { relativeX: " + f.getPlacement().getRelativeX() + ", relativeY: "
                    + f.getPlacement().getRelativeY() + "}, pos: \"" + pos + "\", tag: \"" +f.getTag() + "\" },");
        });

        return playerMarkers;
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
                        logger.debug( "X: " + f.getAnchor().getX());
                        logger.debug( "Y: " + f.getAnchor().getY());
                        placements.add(playerMarker);
                    });
        }

        return placements;
    }

    private PlayerMarker playerExtractor(XSLFShape shape) {
        if (shape.getShapeName().contains("Oval") || shape.getShapeName().contains("Rect")) {
            boolean isCenter = shape.getShapeName().contains("Rect" );
            int translatedX = 0;
            int translatedY = 0;

            translatedX = (int) Math.round((shape.getAnchor().getX() / maxX) * 160);

            double adjustedY = shape.getAnchor().getY() - lineOfScrimage;

            translatedY = (int) Math.round(((adjustedY / 200) * 30));
            return new PlayerMarker( new Placement(translatedX, translatedY), "wr", ((TextShape) shape).getText(), isCenter);
        }

        return null;
    }

    @Override
    public List<PlayerMarker> extractPlayerPlacements() {
        return extractPlayersOnSlide(ppt.getSlides().get(0));
    }

    private boolean isOnCanvas(XSLFShape s) {
        return s.getAnchor().getX() < maxX && s.getAnchor().getY() < maxY
                && s.getAnchor().getX() >= 0 && s.getAnchor().getY() >= 0;

    }

    public List<Route> getRoutes() {

        ppt.getSlides().get(0).getShapes().stream()
                .forEach(f->logger.debug( "Shape:  " + f.getClass().getName()  + " name:  " + f.getShapeName()));

        ppt.getSlides().get(0).getShapes().stream()
                .filter(this::isOnCanvas)
                .filter(f->f.getShapeName().contains( "Straight" ))
                .forEach(f->{
                    logger.debug((f.getClass().getName()));

                    logger.debug(f.getXmlObject().xmlText());
                    XSLFAutoShape connector = (XSLFAutoShape)f;
                    logger.debug( "Shape type:  " + connector.getShapeType().nativeName);

//
//                    Iterator iterate = connector.getGeometry().iterator();
//                    while( iterate.hasNext() ){
//                        logger.debug( "Shape geometery:  "+ f.getShapeId() + " : " + ((Path)iterate.next()).getH());
//                    }

//                    logger.debug(connector.getAnchor().)
                });

        ppt.getSlides().get(0).getShapes().stream()
                .filter(this::isOnCanvas)
                .filter(f->f.getShapeName().contains( "Free" ))
                .forEach(f->{
                    logger.debug((f.getClass().getName()));

                    logger.debug(f.getXmlObject().xmlText());
                    XSLFFreeformShape connector = (XSLFFreeformShape)f;
                    PathIterator pathIterator = connector.getPath().getPathIterator(new AffineTransform());
                    logger.debug("Path Iterator:  " + pathIterator.getClass().getName());
                    while(!pathIterator.isDone()) {
                        pathIterator.next();
//                        logger.debug("path:  " + pathIterator.);
                    }

//
//                    Iterator iterate = connector.getGeometry().iterator();
//                    while( iterate.hasNext() ){
//                        logger.debug( "Shape geometery:  "+ f.getShapeId() + " : " + ((Path)iterate.next()).getH());
//                    }

//                    logger.debug(connector.getAnchor().)
                });
        return null;
    }
}
