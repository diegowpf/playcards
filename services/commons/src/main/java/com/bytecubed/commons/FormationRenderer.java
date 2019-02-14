package com.bytecubed.commons;

import com.bytecubed.commons.models.Placement;
import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.commons.models.movement.CustomMoveDescriptor;
import com.bytecubed.commons.models.movement.Route;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;


public class FormationRenderer {

    private Logger logger = LoggerFactory.getLogger(FormationRenderer.class);

    public static final int WIDTH = 1443;
    public static final int HEIGHT = 767;
    public static final int RADIUS = 50;
    private static final double LINE_OF_SCRIMAGE = 510;
    private static final double TEN_YARD_IN_PIXELS = HEIGHT - LINE_OF_SCRIMAGE;
    public static final int FIELD_WIDTH = 160;
    public static final int TEN_YARDS_IN_FT = 30;

    public String render(Formation formation) {
        return render(formation, true);
    }

    public String render(Formation formation, boolean includeBackground) {

        SVGGraphics2D graphics = new SVGGraphics2D(WIDTH, HEIGHT);
        if (includeBackground) {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            Image image = null;
            try {
                image = ImageIO.read(classloader.getResourceAsStream("images/ravens-30-no-logo.png"));
            } catch (IOException e) {
                logger.error("Can not find base background image.", e);
            }
            graphics.drawImage(image, 0, 0, null);
        }

        formation.getPlayerMarkers().forEach(p -> drawPlayer(graphics, p));

        return graphics.getSVGDocument();
    }


    private void drawPlayer(SVGGraphics2D graphics, PlayerMarker p) {
        Shape shape = new Ellipse2D.Double(x(p), y(p), RADIUS, RADIUS);
        graphics.setColor(Color.red);

        if( p.isCenter() ){
            shape = new Rectangle2D.Double(x(p), y(p), RADIUS, RADIUS );
            graphics.setColor(Color.black);
        }

        if( p.getTag() == null && p.getTag().isEmpty() ) {
            graphics.setColor(Color.lightGray);
        }

        graphics.fill(shape);
        graphics.setColor(Color.black);
        graphics.setStroke(new BasicStroke(3));
        graphics.draw(shape);

        graphics.setColor(Color.black);
        p.getRoutes().forEach(r->renderRoute(r, graphics));
    }

    private void renderRoute(Route route, SVGGraphics2D graphics) {

        route.getMoveDescriptors().forEach(m->{
            Placement start = ((CustomMoveDescriptor) m).getStart();
            Placement end = ((CustomMoveDescriptor) m).getEnd();

            graphics.drawLine(
                    (int)x(start.getRelativeX()),
                    (int)y(start.getRelativeY()),
                    (int)x(end.getRelativeX()),
                    (int)y(end.getRelativeY()));
        });
    }

    private double x(PlayerMarker p) {
        return x(p.getPlacement().getRelativeX());
    }

    private double x(double relativeX) {
        return (relativeX / FIELD_WIDTH) * WIDTH;
    }

    private double y(PlayerMarker p) {
        return y(p.getPlacement().getRelativeY());
    }

    private double y(double relativeY) {
        return LINE_OF_SCRIMAGE + ((relativeY / TEN_YARDS_IN_FT) * TEN_YARD_IN_PIXELS);
    }
}
