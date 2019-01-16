package com.bytecubed.commons.models;

public class Route {
    private double distance;
    private Move move;
    private String marker;

    @Override
    public String toString() {
        return "Route{" +
                "distance=" + distance +
                ", move=" + move +
                '}';
    }

    public Route(double distance, Move move, String marker) {
        this.distance = distance;
        this.move = move;
        this.marker = marker;
    }

    public Route() {
    }

    public Move getMove() {
        return move;
    }

    public String getPlayer() {
        return marker;
    }

    public double getDistance() {
        return distance;
    }
}
