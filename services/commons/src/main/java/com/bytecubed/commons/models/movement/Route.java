package com.bytecubed.commons.models.movement;

import java.util.List;

public class Route {
    private List<MoveDescriptor> moveDescriptors;
    private String marker;

    public Route(List<MoveDescriptor> moveDescriptors, String marker ){
        this.moveDescriptors = moveDescriptors;
        this.marker = marker;
    }

    public Route() {
    }

    public String getPlayer() {
        return marker;
    }

    public List<MoveDescriptor> getMoveDescriptors() {
        return moveDescriptors;
    }
}
