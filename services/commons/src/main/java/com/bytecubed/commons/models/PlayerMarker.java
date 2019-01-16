package com.bytecubed.commons.models;

import java.util.ArrayList;
import java.util.List;

public class PlayerMarker {
    private Placement placement;
    private String pos;
    private String tag;
    private boolean isCenter;
    private List<Route> routes;

    public PlayerMarker(){
    }

    public PlayerMarker(Placement placement, String pos, String tag) {
        this( placement, pos, tag, false );
    }

    public PlayerMarker(Placement placement, String pos, String tag, boolean isCenter) {

        this.placement = placement;
        this.pos = pos;
        this.tag = tag;
        this.isCenter = isCenter;
        this.routes = new ArrayList<>();
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

    @Override
    public String toString() {
        return "PlayerMarker{" +
                "placement=" + placement +
                ", pos='" + pos + '\'' +
                ", tag='" + tag + '\'' +
                ", isCenter=" + isCenter +
                ", routes=" + routes +
                '}';
    }

    public void addRoute(Route route) {
        routes.add(route);
    }
}
