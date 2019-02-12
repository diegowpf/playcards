package com.bytecubed.nlp.parsing;

import com.bytecubed.commons.Formation;
import com.bytecubed.commons.FormationRenderer;
import com.bytecubed.commons.models.FormationGroup;
import org.apache.poi.xdgf.usermodel.XmlVisioDocument;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FormationParserTest {

    @Test
    @Ignore
    public void shouldLoadACardFromMichigan() throws IOException {
        XmlVisioDocument doc = new XmlVisioDocument(
                new FileInputStream("/Users/carlyledavis/Desktop/michigan.vsdx"));


        FormationParser parser = new FormationParser(doc);
        List<FormationGroup> formationGroup = parser.extractFormations();

        formationGroup.forEach(g->g.getFormations().forEach(this::print));

    }

    private void print(Formation formation) {

            new FormationRenderer().render(formation);
//        RestTemplate template = new RestTemplate();
//        ResponseEntity<String> response = template.postForEntity("http://nlp.immersivesports.ai/coach/formation", formation, String.class);
    }


    @Test
    public void shouldDrawSimpleSVG(){
    }


}