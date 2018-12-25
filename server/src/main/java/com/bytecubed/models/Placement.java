package com.bytecubed.models;

public class Placement {

    private final int relativeX;
    private final int relativeY;



    public Placement(int relativeX, int relativeY ) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
    }

    public int getRelativeX() {
        return relativeX;
    }

    @Override
    public String toString() {
        return "Placement{" +
                "relativeX=" + relativeX +
                ", relativeY=" + relativeY +
                '}';
    }

    public int getRelativeY() {
        return relativeY;
    }

}
