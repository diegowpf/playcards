package com.bytecubed;

import com.bytecubed.parser.RavensPowerPointParser;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.xdgf.usermodel.XDGFShape;
import org.apache.poi.xdgf.usermodel.XmlVisioDocument;
import org.apache.poi.xdgf.usermodel.section.geometry.Ellipse;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFFreeformShape;
import org.apache.poi.xslf.usermodel.XSLFSheet;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationTest {

    @Test
    @Ignore
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

}