package com.bytecubed.nlp.parsing;

import com.bytecubed.commons.Formation;
import com.bytecubed.commons.models.FormationGroup;
import com.bytecubed.commons.models.Placement;
import com.bytecubed.commons.models.PlayerMarker;
import org.apache.poi.xdgf.usermodel.XDGFPage;
import org.apache.poi.xdgf.usermodel.XDGFShape;
import org.apache.poi.xdgf.usermodel.XmlVisioDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class FormationParser {

    private static final double MICHIGAN_FIELD_WIDTH = 3.0390625d;
    private static final double MICHIGAN_10_YARD_LENGTH = 0.90456d;
    private static final double STANDARD_FIELD_WIDTH_FT = 110d;
    private static final double STANDARD_10_YARD_FT = 30d;


    private Logger logger = LoggerFactory.getLogger(FormationParser.class);
    private XmlVisioDocument doc;

    public FormationParser(XmlVisioDocument doc) {
        this.doc = doc;
    }

    public List<FormationGroup> extractFormations() {

        List<XDGFPage> page = doc.getPages().stream()
                .filter(p->p.getName().toLowerCase().contains("formation"))
                .collect(toList());

        return  page.stream().map(this::extractFormationFromPage).collect(toList());

    }

    private FormationGroup extractFormationFromPage(XDGFPage page) {

        List<XDGFShape> listOfGroups = page.getContent().getShapes().stream()
                .filter(s -> s.getShapes() != null && !s.getShapes().isEmpty())
                .filter(s -> s.getShapes().size() == 11)
                .collect(toList());

        return new FormationGroup(listOfGroups.stream().map(this::extractFormation).collect(toList()));
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
        double leftMost = getFurthestToTheLeft(group);

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


            PlayerMarker marker = convertToPlayer(shouldFlipX, line, leftMost, group.getBounds().getWidth(), f);
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

        return new Formation(UUID.nameUUIDFromBytes(formationName.getBytes()),
                formationName,
                placements.toArray(new PlayerMarker[0]));
    }

    private PlayerMarker convertToPlayer(boolean shouldFlipX, double line, double leftMost, double height, XDGFShape player) {
        double naturalX = player.getPinX();
        double naturalY = player.getPinY();

        if( leftMost < 0 )
            naturalX += Math.abs(leftMost);
        else
            naturalX -= Math.abs(leftMost);

        double yScalar = naturalY / MICHIGAN_10_YARD_LENGTH;
        double xScalar = naturalX / MICHIGAN_FIELD_WIDTH;

        double xPos = xScalar * STANDARD_FIELD_WIDTH_FT;
        double yPos = Math.abs((yScalar *STANDARD_10_YARD_FT) - 27);

        if( shouldFlipX ) xPos = Math.abs(xPos - STANDARD_FIELD_WIDTH_FT);


        String name = player.getTextAsString().trim();
        return new PlayerMarker(
                new Placement(15 + xPos, yPos ), name, name );
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

    private double getFurthestToTheLeft(XDGFShape group ){
        double furthest = 10;
        for (XDGFShape shape : group.getShapes()) {
            if (furthest > shape.getPinX()) {
                furthest = shape.getPinX();
            }
        }

        return furthest;
    }
}
