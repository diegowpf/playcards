package com.bytecubed.commons.models;

public class Player {
    private String name;
    private String position;
    private Team team;

    public Player(){}

    public Player(String name, String position, Team team) {
        this.name = name;
        this.position = position;
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }
}
