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

public class RavensPowerPointParser implements PlayCardParser {
    private XMLSlideShow ppt;
    static int maxX = 1000;
    static int maxY = 600;
    static int lineOfScrimage = 400;

    private Logger logger = LoggerFactory.getLogger(RavensPowerPointParser.class);

    public RavensPowerPointParser(XMLSlideShow ppt) {
        this.ppt = ppt;
    }

    private List<Player> extractPlayersOnSlide(XSLFSlide slide) {
        List<Player> players = new ArrayList<>();

        slide.getShapes().forEach(x-> logger.debug( "Shape:  " + x.getShapeName()));

        slide.getShapes().stream()
                .filter(this::isOnCanvas)
                .forEach(s -> {
                    Player placement = playerExtractor(s);
                    if (placement != null)
                        players.add(placement);

                    players.addAll(nestedPlayerExtractor(s));
                });

        players.forEach(f -> {
            String pos = f.isCenter() ? "center" : "wr";
            logger.debug("{ placement: { relativeX: " + f.getPlacement().getRelativeX() + ", relativeY: "
                    + f.getPlacement().getRelativeY() + "}, pos: \"" + pos + "\", tag: \"" +f.getTag() + "\" },");
        });

        return players;
    }

    private List<Player> nestedPlayerExtractor(XSLFShape s) {

        List<Player> placements = new ArrayList<>();

        if (s.getShapeName().contains("Group")) {
            XSLFGroupShape shape = (XSLFGroupShape) s;

            List<XSLFShape> groupShapes = new ArrayList(shape.getShapes());
            groupShapes.stream()
                    .filter(f -> f.getShapeName().contains("Oval") || f.getShapeName().contains("Rectangle"))
                    .forEach(f -> {

                        Player player = playerExtractor(f);
                        logger.debug( "X: " + f.getAnchor().getX());
                        logger.debug( "Y: " + f.getAnchor().getY());
                        placements.add(player);
                    });
        }

        return placements;
    }

    private Player playerExtractor(XSLFShape shape) {
        if (shape.getShapeName().contains("Oval") || shape.getShapeName().contains("Rect")) {
            boolean isCenter = shape.getShapeName().contains("Rect" );
            int translatedX = 0;
            int translatedY = 0;

            translatedX = (int) Math.round((shape.getAnchor().getX() / maxX) * 160);

            double adjustedY = shape.getAnchor().getY() - lineOfScrimage;

            translatedY = (int) Math.round(((adjustedY / 200) * 30));
            return new Player( new Placement(translatedX, translatedY), "wr", ((TextShape) shape).getText(), isCenter);
        }

        return null;
    }

    @Override
    public List<Player> extractPlayerPlacements() {
        return extractPlayersOnSlide(ppt.getSlides().get(0));
    }

    private boolean isOnCanvas(XSLFShape s) {
        return s.getAnchor().getX() < maxX && s.getAnchor().getY() < maxY
                && s.getAnchor().getX() >= 0 && s.getAnchor().getY() >= 0;

    }
}
