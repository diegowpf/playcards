package com.bytecubed.commons.models;

import java.awt.geom.Point2D;
import java.util.Objects;

public class Placement {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Placement placement = (Placement) o;
        return Double.compare(placement.relativeX, relativeX) == 0 &&
                Double.compare(placement.relativeY, relativeY) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(relativeX, relativeY);
    }

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

    public double getXOffSet(Placement placement) {
        return placement.getRelativeX() - this.relativeX;
    }

    public double getYOffSet(Placement placement) {
        return placement.getRelativeY() - this.relativeY;
    }

    public Placement adjust(double xOffset, double yOffSet) {
        return new Placement(relativeX + xOffset,relativeY + yOffSet);
    }
}
