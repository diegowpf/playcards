package com.bytecubed.commons;

import com.bytecubed.commons.models.Placement;
import com.bytecubed.commons.models.PlayerMarker;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static java.util.Arrays.asList;

public class FormationRendererTest {

    @Test
    @Ignore
    public void shouldRenderFormationAsImage() throws IOException {
        FormationRenderer renderer = new FormationRenderer();

        Formation formation = new Formation(asList(
                new PlayerMarker(new Placement(80, 0 ), "C", "C", true ),
                new PlayerMarker(new Placement(20, 5 ), "wr", "wr" )
        ));

        renderer.render(formation);
    }
}