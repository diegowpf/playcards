package com.bytecubed.parser;

import com.bytecubed.commons.PlayCard;
import com.bytecubed.commons.models.movement.CustomMoveDescriptor;
import com.bytecubed.commons.models.movement.CustomRoute;
import com.bytecubed.commons.models.movement.MoveDescriptor;
import com.bytecubed.studio.parser.RavensPowerPointParser;
import com.bytecubed.studio.parser.ShapeToEntityRegistry;
import com.bytecubed.studio.parser.routes.BasicRouteStrategy;
import com.bytecubed.studio.parser.routes.RouteStrategy;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.websocket.RemoteEndpoint;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RavensPowerPointParserTest {

    private RouteStrategy routeStrategy = new BasicRouteStrategy();

    @Before
    public void setup(){
        routeStrategy = new BasicRouteStrategy();
    }

    @Test
    public void shouldOpenUpPowerpoint() throws IOException {
        assertThat(new RavensPowerPointParser(getDefaultPowerPoint(), routeStrategy)
                .extractPlayCards()
                .get(0)
                .getFormation()
                .getPlayerMarkers()).hasSize(11);
    }

    @Test
    public void shouldOpenUpPowerPointWithMultipleSlides() throws IOException {
        assertThat(new RavensPowerPointParser(getDefaultPowerPoint(), routeStrategy)
                .extractPlayCards()
                .get(0).getName()).isEqualTo("CIN-32 3-6 +30");

        assertThat(new RavensPowerPointParser(getDefaultPowerPoint(), new BasicRouteStrategy())
                .extractPlayCards()
                .get(1).getName()).isEqualTo("BLT-44 3-6 +25");
    }

    @Test
    public void shouldExtractName() throws IOException {
        PlayCard playCard = new RavensPowerPointParser(getDefaultPowerPoint(), new BasicRouteStrategy()).extractPlayCards().get(0);
        assertThat(playCard.getName()).isEqualTo("CIN-32 3-6 +30");
    }

    @Test
    public void shouldExtractRoutesThatAreSegmentedAndOrientThemCorrectly() throws IOException {
        PlayCard playCard = new RavensPowerPointParser(get("89-route-only.pptx"), new BasicRouteStrategy()).extractPlayCards().get(0);
        playCard.getFormation().getPlayerMarkers().stream()
                .forEach(p->{
                    p.getRoutes().forEach(r->{
                        r.getMoveDescriptors().forEach(m->{
                            System.out.println(m);
                        });
                    });

                    System.out.println("Route Count:  " + p.getRoutes().size());
                });

        List<MoveDescriptor> moves = playCard.getFormation().getPlayerMarkers()
                .get(0).getRoutes().get(0).getMoveDescriptors();
//        assertThat(moves).hasSize(5);
    }

    @Test
    public void shouldCombineRoutesIfCurvedOrStraight() throws IOException {
        PlayCard playCard = new RavensPowerPointParser(getDefaultPowerPoint(), new BasicRouteStrategy()).extractPlayCards().get(0);
        CustomRoute route = playCard.getFormation().getPlayerMarkerAt("84").getRoutes().get(0);

        assertThat(route.getMoveDescriptors()).hasSize(10);

    }

    private XMLSlideShow getDefaultPowerPoint() throws IOException {
        return get("test.pptx");
    }

    public XMLSlideShow get(String fileName) throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        return new XMLSlideShow(new FileInputStream(file));
    }

    @Test
    public void shouldOpenSlidesWith2Plays() throws IOException {
        assertThat(new RavensPowerPointParser(get("test-2-play.pptx"), new BasicRouteStrategy())
                .extractPlayCards()).hasSize(2);
    }

    @Test
    public void shouldReturnSingleStraightRouteForPlayerOnCard() throws IOException {
        RavensPowerPointParser parser = new RavensPowerPointParser(get("1-player-1-route.pptx"), new BasicRouteStrategy());
        PlayCard playCard = parser.extractPlayCards().get(0);

        assertThat(playCard.getFormation().getPlayerMarkerAt("11").getRoutes()).hasSize(1);
    }


    @Test
    public void shouldReturnAllOfTheRoutesOnCard() throws IOException {
        RavensPowerPointParser parser = new RavensPowerPointParser(getDefaultPowerPoint(), new BasicRouteStrategy());
        PlayCard playCard = parser.extractPlayCards().get(0);

        assertThat(playCard.getFormation().getPlayerMarkerAt("11").getRoutes()).hasSize(1);
        assertThat(playCard.getFormation().getPlayerMarkerAt("84").getRoutes()).hasSize(1);
        assertThat(playCard.getFormation().getPlayerMarkerAt("19").getRoutes()).hasSize(1);
    }

    @Test
    public void shouldExtract2MovesFromPlayer89() throws IOException {
        RavensPowerPointParser parser = new RavensPowerPointParser(get("89-crazy-route.pptx"), new BasicRouteStrategy());
        PlayCard playCard = parser.extractPlayCards().get(0);

        assertThat(playCard.getFormation().getPlayerMarkerAt("89").getRoutes()).hasSize(3);
    }

    @Test
    public void shouldDiscoverExtensionsPaths() throws IOException {
        RavensPowerPointParser parser = new RavensPowerPointParser(get("CIN-32-84-only.pptx"), new BasicRouteStrategy());
        PlayCard playCard = parser.extractPlayCards().get(0);

    }


    @Test
    @Ignore("This test picks up routes that have not been collapsed.")
    public void shouldReturnAllRoutesWithinCanvas() throws IOException {
        XMLSlideShow show = get("test-scrubbed.pptx");
        XSLFSlide firstSlide = show.getSlides().get(0);

        assertThat(new RavensPowerPointParser(show, new BasicRouteStrategy()).buildRoutes(firstSlide, new ShapeToEntityRegistry())).hasSize(4);
        //This would work but this is wrong since this is a user error on how the routes are drawn.
        assertThat(new RavensPowerPointParser(show, new BasicRouteStrategy()).buildRoutes(firstSlide, new ShapeToEntityRegistry())).hasSize(10);
    }

    @Test
    @Ignore
    public void shouldExtractStandardTemplateOnRightHash() throws IOException {
        RavensPowerPointParser parser = new RavensPowerPointParser(get("center-hash.pptx"), new BasicRouteStrategy());
        PlayCard playCard = parser.extractPlayCards().get(0);

        assertThat(playCard.getFormation().getPlayerMarkers()).hasSize(5);

    }

}