package com.bytecubed.commons.models.movement;

import com.bytecubed.commons.models.Placement;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import static com.bytecubed.commons.models.movement.Move.custom;

public class CustomMoveDescriptor extends MoveDescriptor{

    public static final int MAXIMUM_POINT_DISTANCE = 10;
    private Placement start;
    private Placement end;


    public CustomMoveDescriptor(Placement start, Placement end) {
        super(custom);
        this.start = start;
        this.end = end;
    }

    public Placement getEnd() {
        return end;
    }

    public Placement getStart() {
        return start;
    }

    public boolean collidesWith(CustomMoveDescriptor danglingRoute) {

        return doesCross(danglingRoute) || hasMinorGap(danglingRoute);
    }

    private boolean hasMinorGap(CustomMoveDescriptor danglingRoute) {

        Point2D startAsPoint = start.toPoint();
        Point2D endAsPoint = end.toPoint();

        return pointGapInThreshold(startAsPoint, danglingRoute) || pointGapInThreshold(endAsPoint, danglingRoute);
    }

    private boolean pointGapInThreshold(Point2D point, CustomMoveDescriptor danglingRoute) {
        return point.distance(danglingRoute.start.toPoint()) < MAXIMUM_POINT_DISTANCE
                || point.distance(danglingRoute.getEnd().toPoint()) < MAXIMUM_POINT_DISTANCE;
    }

    private boolean doesCross(CustomMoveDescriptor danglingRoute) {
        return Line2D.linesIntersect(this.start.getRelativeX(), this.start.getRelativeY(),
                this.end.getRelativeX(), this.end.getRelativeY(),
                danglingRoute.getStart().getRelativeX(), danglingRoute.getStart().getRelativeY(),
                danglingRoute.getEnd().getRelativeX(), danglingRoute.getEnd().getRelativeY());
    }


}
