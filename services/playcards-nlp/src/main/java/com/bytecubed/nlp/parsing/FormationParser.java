package com.bytecubed.nlp.parsing;

import com.bytecubed.commons.Formation;
import com.bytecubed.commons.models.Placement;
import com.bytecubed.commons.models.Player;
import com.bytecubed.commons.models.PlayerMarker;
import org.apache.poi.xdgf.usermodel.XDGFPage;
import org.apache.poi.xdgf.usermodel.XDGFShape;
import org.apache.poi.xdgf.usermodel.XmlVisioDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FormationParser {

    private static final double MICHIGAN_FIELD_WIDTH = 3.0390625d;
    private static final double MICHIGAN_10_YARD_LENGTH = 0.90456d;
    private static final double STANDARD_FIELD_WIDTH_FT = 140d;
    private static final double STANDARD_10_YARD_FT = 30d;

    private Logger logger = LoggerFactory.getLogger(FormationParser.class);
    private XmlVisioDocument doc;

    public FormationParser(XmlVisioDocument doc) {
        this.doc = doc;
    }

    public List<Formation> extractFormations() {

        XDGFPage page = doc.getPages().stream().findFirst().get();
        List<XDGFShape> listOfGroups = page.getContent().getShapes().stream()
                .filter(s -> s.getShapes() != null && !s.getShapes().isEmpty())
                .filter(s -> s.getShapes().size() == 11)
                .collect(toList());

        return listOfGroups.stream().map(this::extractFormation).collect(toList());
    }

    private Formation extractFormation(XDGFShape group) {

        List<PlayerMarker> placements = new ArrayList();

        String formationName;

        boolean shouldFlipX = group.getFlipX() || group.getParentShape().getFlipX();
        boolean shouldFlipY = group.getFlipY() || group.getParentShape().getFlipY();

        logger.debug("I am in a group with this size:  " + group.getParentShape().getShapes().size());
        formationName = group.getParentShape().getShapes().stream().filter(s -> s != group).findFirst().get().getTextAsString();

        logger.debug("I am the name:  " + formationName);
        logger.debug("this is the X flip: " + group.getFlipX());
        logger.debug("this is the Y flip: " + group.getFlipY());

        logger.debug("this parent flip:  " + group.getParentShape().getFlipX());
        logger.debug("Should flip X:  " + shouldFlipX);
        logger.debug("Should flip Y:  " + shouldFlipY);

        double line = getLineWithinGroup(group);
        logger.debug("The line for " + formationName.trim() + " is located at:  " + line);

        group.getShapes().forEach(f -> {
            logger.debug("*************************************************");
            logger.debug("Shape Start");
            logger.debug(f.getName());
            logger.debug(f.getSymbolName());
            logger.debug(f.getTextAsString());
            logger.debug("Width:  " + group.getBounds().getWidth());
            logger.debug("Height:  " + group.getBounds().getHeight());
            logger.debug("(" + f.getPinX() + ", " + f.getPinY() + ")");


            PlayerMarker marker = convertShape(shouldFlipX, line, group.getBounds().getWidth(), f);
            placements.add(marker);
            logger.debug( "{\"placement\":{\"relativeX\":" + marker.getPlacement().getRelativeX()
                    + ",\"relativeY\":" + marker.getPlacement().getRelativeY() + "},\"tag\":\"" +
                    marker.getTag() + "\",\"center\":false}");
//                graphics.fillOval((int) (f.getPinX() * 100) + 100, (int) (f.getPinY() * -100) + 100, 20, 20);
        });

        System.out.println( "SDFFFFFFFFFFFFFFFFFFFFFFFFSDFSDFSDFDS");
        System.out.println(formationName);
        for( PlayerMarker marker : placements ){
            System.out.println( "{\"placement\":{\"relativeX\":" + marker.getPlacement().getRelativeX()
                    + ",\"relativeY\":" + marker.getPlacement().getRelativeY() + "},\"tag\":\"" +
                    "\",\"center\":false},");
        }
        System.out.println( "SDFFFFFFFFFFFFFFFFFFFFFFFFSDFSDFSDFDS");

        return null;
    }

    private PlayerMarker convertShape(boolean shouldFlipX, double line, double height, XDGFShape player) {
        double yScalar = player.getPinY() / MICHIGAN_10_YARD_LENGTH;
        double xScalar = player.getPinX() / MICHIGAN_FIELD_WIDTH;

        double xPos = xScalar * STANDARD_FIELD_WIDTH_FT;
        double yPos = Math.abs((yScalar *STANDARD_10_YARD_FT) - 27);

        if( shouldFlipX ) xPos = Math.abs(xPos - STANDARD_FIELD_WIDTH_FT);


        return new PlayerMarker(
                new Placement(10 + xPos, yPos ),
                player.getTextAsString(),
                player.getTextAsString());
    }

    private double getLineWithinGroup(XDGFShape group) {
        double highestElement = -1;
        for (XDGFShape shape : group.getShapes()) {
            if (highestElement < shape.getPinY()) {
                highestElement = shape.getPinY();
            }
        }

        return highestElement;
    }
}
