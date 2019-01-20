package com.bytecubed.commons.models.movement;

public class StandardMoveDescriptor extends MoveDescriptor{
    private String marker;
    private double distance;

    public StandardMoveDescriptor(double distance, Move move) {
        super(move);
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }
}
