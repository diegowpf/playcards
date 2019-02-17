package com.bytecubed.commons.models;

import com.bytecubed.commons.models.movement.CustomMoveDescriptor;
import com.bytecubed.commons.models.movement.CustomRoute;
import com.bytecubed.commons.models.movement.MoveDescriptor;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class PlayerMarkerTest {

    @Test
    public void shouldApplyRouteGivenAPlayersCurrentPositionbyOffSettingBasedonCurrentPosition() {
        Placement playerStart = new Placement(10d, 10d);
        PlayerMarker marker = new PlayerMarker(playerStart, "wr", "X");
        CustomRoute route = new CustomRoute(
                asList(new CustomMoveDescriptor(
                        new Placement(0d, 0d),
                        new Placement(0d, 5d))), "X");

        marker.applyRoute(route);

        List<CustomRoute> routes = marker.getRoutes();
        List<MoveDescriptor> moveDescriptors = routes.get(0).getMoveDescriptors();
        CustomMoveDescriptor moveDescriptor = (CustomMoveDescriptor) moveDescriptors.get(0);

        assertThat(routes).hasSize(1);
        assertThat(moveDescriptors).hasSize(1);
        assertThat(moveDescriptor.getStart()).isEqualTo(playerStart);
    }

}