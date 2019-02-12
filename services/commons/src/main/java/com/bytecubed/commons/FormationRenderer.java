package com.bytecubed.commons;

import com.bytecubed.commons.models.PlayerMarker;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.IOException;


public class FormationRenderer {

    private Logger logger = LoggerFactory.getLogger(FormationRenderer.class);

    public static final int WIDTH = 1443;
    public static final int HEIGHT = 767;
    public static final int RADIUS = 50;
    private static final double LINE_OF_SCRIMAGE = 510;
    private static final double TEN_YARD_IN_PIXELS = HEIGHT - LINE_OF_SCRIMAGE;
    public static final int FIELD_WIDTH = 148;
    public static final int TEN_YARDS_IN_FT = 30;

    public String render(Formation formation ) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        Image image = null;
        try {
            image = ImageIO.read(classloader.getResourceAsStream("images/ravens-30-no-logo.png"));
        } catch (IOException e) {
            logger.error("Can not find base background image.",e );
        }

        SVGGraphics2D graphics = new SVGGraphics2D(WIDTH, HEIGHT);

        graphics.drawImage(image, 0,0,null);
        formation.getPlayerMarkers().forEach(p->drawPlayer(graphics, p ));

        return graphics.getSVGDocument();
    }

    private void drawPlayer(SVGGraphics2D graphics, PlayerMarker p) {
        Shape circle = new Ellipse2D.Double(x(p), y(p), RADIUS, RADIUS);
        graphics.setColor(Color.red);
        graphics.fill(circle);

        graphics.setColor(Color.black);
        graphics.setStroke(new BasicStroke(3));
        graphics.draw(circle);

        graphics.setColor(Color.black);
//        graphics.drawString(p.getTag(), (float)x(p) + (RADIUS/2), (float)y(p) + (RADIUS/2));
    }

    private double x(PlayerMarker p) {
        return ((p.getPlacement().getRelativeX()/ FIELD_WIDTH) * WIDTH);
    }

    private double y(PlayerMarker p) {
        return LINE_OF_SCRIMAGE + ((p.getPlacement().getRelativeY()/ TEN_YARDS_IN_FT) * TEN_YARD_IN_PIXELS);
    }
}
