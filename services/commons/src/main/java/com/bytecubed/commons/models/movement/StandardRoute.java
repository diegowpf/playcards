package com.bytecubed.commons.models.movement;

import com.bytecubed.commons.models.PlayerMarker;

import java.util.List;

public class StandardRoute implements Route {

    private PlayerMarker playerMarker;

    public StandardRoute(PlayerMarker playerMarker){
        this.playerMarker = playerMarker;
    }

    @Override
    public String getPlayer() {
        return null;
    }

    @Override
    public List<MoveDescriptor> getMoveDescriptors() {
        return null;
    }
}
