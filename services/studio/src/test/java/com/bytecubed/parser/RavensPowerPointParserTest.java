package com.bytecubed.parser;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class RavensPowerPointParserTest {

    @Test
    public void shouldOpenUpPowerpoint() throws IOException {
        assertThat(new RavensPowerPointParser(getPowerPoint()).extractPlayerPlacements()).hasSize(11);
    }

    private XMLSlideShow getPowerPoint() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("test.pptx").getFile());

        return new XMLSlideShow(new FileInputStream(file));
    }

    @Test
    @Ignore
    public void shouldReturnAllRoutesWithinCanvas() throws IOException {
        assertThat(new RavensPowerPointParser(new XMLSlideShow(new FileInputStream(
                new File("/Users/carlyledavis/Desktop/test-simple.pptx")))).getRoutes()).hasSize(4);

//        assertThat(new RavensPowerPointParser(getPowerPoint()).getRoutes()).hasSize(4);
    }



}