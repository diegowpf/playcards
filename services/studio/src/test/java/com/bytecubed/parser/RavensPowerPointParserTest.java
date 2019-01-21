package com.bytecubed.parser;

import com.bytecubed.commons.PlayCard;
import com.bytecubed.studio.parser.RavensPowerPointParser;
import com.bytecubed.studio.parser.ShapeToEntityRegistry;
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
        assertThat(new RavensPowerPointParser(getDefaultPowerPoint())
                .extractPlayCards()
                .get(0)
                .getFormation()
                .getPlayerMarkers()).hasSize(11);
    }

    @Test
    public void shouldOpenUpPowerPointWithMultipleSlides() throws IOException {
        assertThat(new RavensPowerPointParser(getDefaultPowerPoint())
                .extractPlayCards()
                .get(0).getName()).isEqualTo("CIN-32 3-6 +30");

        assertThat(new RavensPowerPointParser(getDefaultPowerPoint())
                .extractPlayCards()
                .get(1).getName()).isEqualTo("BLT-44 3-6 +25");
    }

    @Test
    public void shouldExtractName() throws IOException {
        PlayCard playCard = new RavensPowerPointParser(getDefaultPowerPoint()).extractPlayCards().get(0);
        assertThat(playCard.getName()).isEqualTo("CIN-32 3-6 +30");
    }

    private XMLSlideShow getDefaultPowerPoint() throws IOException {
        return getPowerPoint("test.pptx");
    }

    private XMLSlideShow getPowerPoint(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        return new XMLSlideShow(new FileInputStream(file));
    }

    @Test
    public void shouldOpenSlidesWith2Plays() throws IOException {
        assertThat(new RavensPowerPointParser(getPowerPoint("test-2-play.pptx"))
                .extractPlayCards()).hasSize(2);
    }

    @Test
    public void shouldReturnSingleStraightRouteForPlayerOnCard() throws IOException {
        RavensPowerPointParser parser = new RavensPowerPointParser(getPowerPoint("1-player-1-route.pptx"));
        PlayCard playCard = parser.extractPlayCards().get(0);

        assertThat(playCard.getFormation().getPlayerMarkerAt("11").getRoutes()).hasSize(1);
    }


    @Test
    public void shouldReturnAllOfTheRoutesOnCard() throws IOException {
        RavensPowerPointParser parser = new RavensPowerPointParser(getDefaultPowerPoint());
        PlayCard playCard = parser.extractPlayCards().get(0);

        assertThat(playCard.getFormation().getPlayerMarkerAt("11").getRoutes()).hasSize(1);
        assertThat(playCard.getFormation().getPlayerMarkerAt("84").getRoutes()).hasSize(1);
        assertThat(playCard.getFormation().getPlayerMarkerAt("19").getRoutes()).hasSize(1);
    }

    @Test
    public void shouldExtract2MovesFromPlayer89() throws IOException {
        RavensPowerPointParser parser = new RavensPowerPointParser(getPowerPoint("89-crazy-route.pptx"));
        PlayCard playCard = parser.extractPlayCards().get(0);

        assertThat(playCard.getFormation().getPlayerMarkerAt("89").getRoutes()).hasSize(3);
    }

    @Test
    @Ignore("This test picks up routes that have not been collapsed.")
    public void shouldReturnAllRoutesWithinCanvas() throws IOException {
        XMLSlideShow show = getPowerPoint("test-scrubbed.pptx");
        XSLFSlide firstSlide = show.getSlides().get(0);

        assertThat(new RavensPowerPointParser(show).getRoutes(firstSlide, new ShapeToEntityRegistry())).hasSize(4);
        //This would work but this is wrong since this is a user error on how the routes are drawn.
        assertThat(new RavensPowerPointParser(show).getRoutes(firstSlide, new ShapeToEntityRegistry())).hasSize(10);
    }


}