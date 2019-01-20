package com.bytecubed.parser;

import com.bytecubed.studio.parser.RavensPowerPointParser;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class RavensPowerPointParserTest {

    @Test
    public void shouldOpenUpPowerpoint() throws IOException {
        assertThat(new RavensPowerPointParser(getPowerPoint())
                .extractPlayCards()
                .get(0)
                .getFormation()
                .getPlayerMarkers()).hasSize(11);
    }

    @Test
    public void shouldExtractName() throws IOException {
        assertThat(new RavensPowerPointParser(getPowerPoint()).getName()).isEqualTo("CIN-32 3-6 +30");
    }

    private XMLSlideShow getPowerPoint() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("test.pptx").getFile());

        return new XMLSlideShow(new FileInputStream(file));
    }

    @Test
    public void shouldOpenSlidesWith2Plays() throws IOException {
        assertThat(new RavensPowerPointParser(new XMLSlideShow(new FileInputStream(
                new File("/Users/carlyledavis/Desktop/test-2-play.pptx")))).extractPlayCards()).hasSize(2);
    }

    @Test
    @Ignore
    public void shouldReturnAllRoutesWithinCanvas() throws IOException {
        XMLSlideShow show = new XMLSlideShow(new FileInputStream(
                new File("/Users/carlyledavis/Desktop/test-scrubbed.pptx")));
        XSLFSlide firstSlide = show.getSlides().get(0);

        assertThat(new RavensPowerPointParser(show).getRoutes(firstSlide)).hasSize(4);
    }



}