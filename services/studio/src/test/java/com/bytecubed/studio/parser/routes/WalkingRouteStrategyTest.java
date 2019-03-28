package com.bytecubed.studio.parser.routes;

import com.bytecubed.commons.models.movement.CustomRoute;
import com.bytecubed.parser.RavensPowerPointParserTest;
import com.bytecubed.studio.parser.ShapeToEntityRegistry;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class WalkingRouteStrategyTest {


    @Test
    @Ignore
    public void shouldIdentifyARouteWithWeirdDirections() throws IOException {
        XMLSlideShow show = new RavensPowerPointParserTest().get("89-route-only.pptx");
        RouteStrategy strategy = new WalkingRouteStrategy();

        ShapeToEntityRegistry entityRegistry = new ShapeToEntityRegistry();
        XSLFSlide slide = show.getSlides().get(0);

        List<CustomRoute> routes = strategy.buildRoutes(slide, entityRegistry);

        assertThat(routes).hasSize(1);

    }


}