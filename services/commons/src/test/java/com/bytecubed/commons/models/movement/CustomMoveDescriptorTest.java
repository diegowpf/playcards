package com.bytecubed.commons.models.movement;

import com.bytecubed.commons.models.Placement;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CustomMoveDescriptorTest {

    @Test
    public void shouldRespondWithCollisionDetected(){
        CustomMoveDescriptor danglingRoute= new CustomMoveDescriptor(new Placement(3,3),
                new Placement(5,5));

        CustomMoveDescriptor associatedWithPlayer = new CustomMoveDescriptor( new Placement(1,1), new Placement(3,3));
        assertThat(associatedWithPlayer.collidesWith(danglingRoute)).isTrue();
    }

    @Test
    public void shouldRespondWithCollisionsNotDetected(){
        CustomMoveDescriptor danglingRoute= new CustomMoveDescriptor(new Placement(3,3),
                new Placement(5,5));

        CustomMoveDescriptor associatedWithPlayer = new CustomMoveDescriptor( new Placement(30,30), new Placement(300,300));
        assertThat(associatedWithPlayer.collidesWith(danglingRoute)).isFalse();
    }

    @Test
    public void shouldRespondWithRouteCollisionWhenRoutesAreNotConnectedButClose(){
        CustomMoveDescriptor danglingRoute= new CustomMoveDescriptor(new Placement(3,3),
                new Placement(5,5));

        CustomMoveDescriptor associatedWithPlayer = new CustomMoveDescriptor( new Placement(0,0), new Placement(2,2));
        assertThat(associatedWithPlayer.collidesWith(danglingRoute)).isTrue();
    }

    //Todo: Do a test that has many route segements where the segements are not in the order of the appereance.
}