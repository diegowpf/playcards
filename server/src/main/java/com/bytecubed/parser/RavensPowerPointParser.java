package com.bytecubed.parser;

import com.bytecubed.models.Placement;
import org.apache.poi.sl.usermodel.TextShape;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFGroupShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RavensPowerPointParser {
    private XMLSlideShow ppt;
    static int maxX = 1000;
    static int maxY = 600;
    static int lineOfScrimage = 400;
    static int yard5 = 500;
    static int yard0 = 600;

    private Logger logger = LoggerFactory.getLogger(RavensPowerPointParser.class);

    public RavensPowerPointParser(XMLSlideShow ppt) {
        this.ppt = ppt;
    }

    private List<Placement> extractPlayersOnSlide(XSLFSlide slide) {
        List<Placement> placements = new ArrayList<>();

        slide.getShapes().forEach(x-> logger.debug( "Shape:  " + x.getShapeName()));

        slide.getShapes().stream()
                .filter(this::isOnCanvas)
                .forEach(s -> {
                    Placement placement = playerExtractor(s);
                    if (placement != null)
                        placements.add(placement);

                    placements.addAll(nestedPlayerExtractor(s));
                });

        placements.forEach(f -> {
            String pos = f.isCenter() ? "center" : "wr";
            logger.debug("{ placement: { relativeX: " + f.getRelativeX() + ", relativeY: "
                    + f.getRelativeY() + "}, pos: \"" + pos + "\", tag: \"" +f.getTag() + "\" },");
        });

        return placements;
    }

    private List<Placement> nestedPlayerExtractor(XSLFShape s) {

        List<Placement> placements = new ArrayList<>();

        if (s.getShapeName().contains("Group")) {
            XSLFGroupShape shape = (XSLFGroupShape) s;

            List<XSLFShape> groupShapes = new ArrayList(shape.getShapes());
            XSLFGroupShape group = (XSLFGroupShape) s;



            groupShapes.stream()
                    .filter(f -> f.getShapeName().contains("Oval") || f.getShapeName().contains("Rectangle"))
                    .forEach(f -> {

                        Placement placement = playerExtractor(f);
                        logger.debug( "X: " + f.getAnchor().getX());
                        logger.debug( "Y: " + f.getAnchor().getY());
                        placements.add(placement);
                    });


        }

        return placements;
    }

    private Placement playerExtractor(XSLFShape shape) {

        if (shape.getShapeName().contains("Oval") || shape.getShapeName().contains("Rect")) {
            boolean isCenter = shape.getShapeName().contains("Rect" );
            int translatedX = 0;
            int translatedY = 0;

            translatedX = (int) Math.round((shape.getAnchor().getX() / maxX) * 160);

            double adjustedY = shape.getAnchor().getY() - lineOfScrimage;

//            getNearestRoute(shape.getAnchor().getX(), shape.getAnchor().getY(), shape.getSheet());
            translatedY = (int) Math.round(((adjustedY / 200) * 30));
            return new Placement(translatedX, translatedY, "wr", ((TextShape) shape).getText(), isCenter);
        }

        return null;
    }

    public List<Placement> extractPlayerPlacements() {
        return extractPlayersOnSlide(ppt.getSlides().get(0));
    }

    private boolean isOnCanvas(XSLFShape s) {
        return s.getAnchor().getX() < maxX && s.getAnchor().getY() < maxY
                && s.getAnchor().getX() >= 0 && s.getAnchor().getY() >= 0;

    }
}
