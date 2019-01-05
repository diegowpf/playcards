package com.bytecubed.commons.models;

public class Route {
    private double distance;
    private Move move;
    private PlayerMarker marker;

    public Route(double distance, Move move, PlayerMarker marker) {
        this.distance = distance;
        this.move = move;
        this.marker = marker;
    }

    public Move getMove() {
        return move;
    }

    public PlayerMarker getPlayer() {
        return marker;
    }

    public double getDistance() {
        return distance;
    }
}
