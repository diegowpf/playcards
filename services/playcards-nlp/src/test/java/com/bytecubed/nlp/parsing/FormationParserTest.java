package com.bytecubed.nlp.parsing;

import com.bytecubed.commons.Formation;
import org.apache.poi.xdgf.usermodel.XmlVisioDocument;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FormationParserTest {

    @Test
    public void shouldLoadACardFromMichigan() throws IOException {
        XmlVisioDocument doc = new XmlVisioDocument(
                new FileInputStream("/Users/carlyledavis/Desktop/michigan.vsdx"));


        FormationParser parser = new FormationParser(doc);
        List<Formation> formations = parser.extractFormations();

        formations.forEach(this::print);

    }

    private void print(Formation formation) {

    }


    @Test
    public void shouldDrawSimpleSVG(){
    }


}