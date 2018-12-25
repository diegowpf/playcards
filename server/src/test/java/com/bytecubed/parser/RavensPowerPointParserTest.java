package com.bytecubed.parser;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class RavensPowerPointParserTest {

    @Test
    public void shouldOpenUpPowerpoint() throws IOException {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("test.pptx").getFile());
        FileInputStream inputstream = new FileInputStream(file);
        XMLSlideShow ppt = new XMLSlideShow(inputstream);

        assertThat(new RavensPowerPointParser(ppt).extractPlayerPlacements()).hasSize(11);
    }



}