package com.bytecubed.commons.models;

import java.awt.geom.Point2D;

public class Placement {

    private double relativeX;
    private double relativeY;

    public Placement(){
        this(0,0);
    }

    public Placement(double relativeX, double relativeY ) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
    }

    public double getRelativeX() {
        return relativeX;
    }

    @Override
    public String toString() {
        return "Placement{" +
                "relativeX=" + relativeX +
                ", relativeY=" + relativeY +
                '}';
    }

    public double getRelativeY() {
        return relativeY;
    }

    public Point2D.Double toPoint() {
        return new Point2D.Double(relativeX, relativeY );
    }
}
