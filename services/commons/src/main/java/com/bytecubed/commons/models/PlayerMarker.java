package com.bytecubed.commons.models;

import com.bytecubed.commons.models.movement.CustomMoveDescriptor;
import com.bytecubed.commons.models.movement.CustomRoute;
import com.bytecubed.commons.models.movement.MoveDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PlayerMarker {
    private Placement placement;
    private String pos;
    private String tag;
    private boolean isCenter;
    private List<CustomRoute> routes;

    public PlayerMarker(){
        this.routes = new ArrayList();
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

    public List<CustomRoute> getRoutes() {
        return routes;
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

    public void addRoute(CustomRoute route) {
        routes.add(route);
    }

    public void applyRoute(CustomRoute customRoute) {
        CustomMoveDescriptor moveDescriptor = ((CustomMoveDescriptor) customRoute.getMoveDescriptors().get(0));
        double xOffset = moveDescriptor.getStart().getXOffSet(placement);
        double yOffSet = moveDescriptor.getStart().getYOffSet(placement);

        CustomRoute transform = customRoute.transform(xOffset, yOffSet);
        CustomRoute tentativeRoute = transform;

        if( this.getPlacement().getRelativeX() > 80 ){
            tentativeRoute = customRoute.flipAlongXAxis(this.getPlacement().getRelativeX());
        }

        this.addRoute(tentativeRoute);
    }
}
