package com.bytecubed.commons.models.movement;

import com.bytecubed.commons.models.Placement;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomRouteTest {

    @Test
    public void shouldFlipRouteAlongXAxis(){

        CustomRoute route = new CustomRoute(
                asList(
                        new CustomMoveDescriptor(new Placement(81d, 0), new Placement( 91d, 20 )),
                        new CustomMoveDescriptor(new Placement(91d, 20), new Placement( 95d, 20 ))
                ),
                "Foo");

        CustomRoute flippedRoute = route.flipAlongXAxis(81d);
        CustomMoveDescriptor flippedMove = (CustomMoveDescriptor) flippedRoute.getMoveDescriptors().get(0);
        assertThat(flippedMove.getStart().getRelativeX()).isEqualTo(81d);
        assertThat(flippedMove.getStart().getRelativeY()).isEqualTo(0d);

        assertThat(flippedMove.getEnd().getRelativeX()).isEqualTo(71d);
        assertThat(flippedMove.getEnd().getRelativeY()).isEqualTo(20d);
    }

}