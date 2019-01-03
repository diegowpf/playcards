package com.bytecubed.commons.models;

public class PlayerMarker {
    private final Placement placement;
    private final String pos;
    private final String tag;
    private final boolean isCenter;

    public Placement getPlacement() {
        return placement;
    }

    public String getPos() {
        return pos;
    }

    public String getTag() {
        return tag;
    }

    public boolean isCenter() {
        return isCenter;
    }

    public PlayerMarker(Placement placement, String pos, String tag, boolean isCenter) {

        this.placement = placement;
        this.pos = pos;
        this.tag = tag;
        this.isCenter = isCenter;
    }
}
