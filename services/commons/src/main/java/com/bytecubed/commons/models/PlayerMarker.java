package com.bytecubed.commons.models;

public class PlayerMarker {
    private Placement placement;
    private String pos;
    private String tag;
    private boolean isCenter;

    public PlayerMarker(){
    }

    public PlayerMarker(Placement placement, String pos, String tag) {
        this( placement, pos, tag, false );
    }

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
