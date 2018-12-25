package com.bytecubed;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.sl.usermodel.TextShape;
import org.apache.poi.xdgf.usermodel.XDGFShape;
import org.apache.poi.xdgf.usermodel.XmlVisioDocument;
import org.apache.poi.xdgf.usermodel.section.geometry.Ellipse;
import org.apache.poi.xslf.usermodel.*;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationTest {

    static int maxX = 1000;
    static int maxY = 600;
    static int lineOfScrimage = 400;
    static int yard5 = 500;
    static int yard0 = 600;

    private static boolean isOnCanvas(XSLFShape s) {
        return s.getAnchor().getX() < maxX && s.getAnchor().getY() < maxY
                && s.getAnchor().getX() >= 0 && s.getAnchor().getY() >= 0;

    }

    @Test
    public void should() throws IOException, OpenXML4JException {
        XmlVisioDocument doc = new XmlVisioDocument(new FileInputStream("/Users/carlyledavis/Downloads/MIXEDRIVER.vsdx"));
        System.out.println(doc.getPages().size());

        doc.getPages().iterator().next().getContent().getShapes()
                .stream()
                .filter(s -> isACircle(s))
//                .forEach(s->System.out.println(s.getType()));
//                .filter(s->s.getTextAsString().trim().equals("7"))
                .forEach(s -> {
                    System.out.println("Found");
                    System.out.println(s.getTextAsString());
                    System.out.println("X: " + s.getPinX());
                    System.out.println("Y: " + s.getPinY());
                });
        System.out.println(doc.getPages().iterator().next().getContent().getShapes().size());
    }

    private boolean isACircle(XDGFShape s) {

        return s.getGeometrySections().iterator().hasNext() && s.getGeometrySections().iterator().next().getCombinedRows().iterator().next() instanceof Ellipse;
    }

    @Test
    public void shouldOpenUpPowerpoint() throws IOException {
        File file = new File("/Users/carlyledavis/Desktop/test.pptx");
        FileInputStream inputstream = new FileInputStream(file);
        XMLSlideShow ppt = new XMLSlideShow(inputstream);

        ppt.getSlides().stream().forEach(this::extractPlayersOnSlide);
    }

    private void extractPlayersOnSlide(XSLFSlide slide) {
        List<Placement> placements = new ArrayList<>();

        slide.getShapes().forEach(x-> System.out.println( "Shape:  " + x.getShapeName()));

        slide.getShapes().stream()
                .filter(ApplicationTest::isOnCanvas)
                .forEach(s -> {
                    Placement placement = playerExtractor(s);
                    if (placement != null)
                        placements.add(placement);

                    placements.addAll(nestedPlayerExtractor(s));
                });

        placements.forEach(f -> {
            String pos = f.isCenter() ? "center" : "wr";
            System.out.println("{ placement: { relativeX: " + f.getRelativeX() + ", relativeY: "
                    + f.getRelativeY() + "}, pos: \"" + pos + "\", tag: \"" +f.getTag() + "\" },");
        });
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

//                        System.out.println("{ placement: { relativeX: " + placement.getRelativeX() + ", relativeY: " + placement.getRelativeY() + "}, pos: \"wr\", tag: \"19\" }");
//                        System.out.println( "Where the group is:  " + group.getAnchor().getY());
//                        System.out.println(placement);

//                            System.out.println("------>>>> " + f.getShapeName());
//                            System.out.println( f.getAnchor().getWidth());
//                            System.out.println( f.getAnchor().getHeight());
                            System.out.println( "X: " + f.getAnchor().getX());
                            System.out.println( "Y: " + f.getAnchor().getY());


//                            System.out.println( "Bounds Y:  " + f.getAnchor().getMaxY());
//                        System.out.println( "Group:  " + s.getShapeId());
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

            getNearestRoute(shape.getAnchor().getX(), shape.getAnchor().getY(), shape.getSheet());
            translatedY = (int) Math.round(((adjustedY / 200) * 30));
            return new Placement(translatedX, translatedY, "wr", ((TextShape) shape).getText(), isCenter);
        }

        return null;
    }

    private void getNearestRoute(double x, double y, XSLFSheet sheet) {

        Point2D.Double point = new Point2D.Double(x,y);

        sheet.getShapes()
                .stream()
                .filter(s->s.getShapeName().contains("Freeform"))
                .forEach(f->{
                    XSLFFreeformShape freeformShape = (XSLFFreeformShape) f;
                    System.out.println(freeformShape.getPath().getPathIterator(new AffineTransform()).getClass());
                    System.out.println( "path:  " + freeformShape.getPath());



                });
    }

    private class Placement {

        private final int relativeX;
        private final int relativeY;
        private final String pos;
        private final String tag;
        private boolean isCenter;

        public Placement(int relativeX, int relativeY, String pos, String tag) {
            this(relativeX,relativeY,pos,tag,false);
        }

        public boolean isCenter() {
            return isCenter;
        }

        public Placement(int relativeX, int relativeY, String pos, String tag, boolean isCenter) {
            this.relativeX = relativeX;
            this.relativeY = relativeY;
            this.pos = pos;
            this.tag = tag;
            this.isCenter = isCenter;
        }

        public int getRelativeX() {
            return relativeX;
        }

        @Override
        public String toString() {
            return "Placement{" +
                    "relativeX=" + relativeX +
                    ", relativeY=" + relativeY +
                    ", pos='" + pos + '\'' +
                    ", tag='" + tag + '\'' +
                    '}';
        }

        public int getRelativeY() {
            return relativeY;
        }

        public String getPos() {
            return pos;
        }

        public String getTag() {
            return tag;
        }

    }
}