package com.bytecubed.commons.models;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public class Player {
    private String name;
    private String position;
    private Team team;

    @Id
    private UUID id;

    public Player(){}

    public Player(String name, String position, Team team) {
        this.name = name;
        this.position = position;
        this.team = team;

        id = UUID.nameUUIDFromBytes((name+position+team.getName()).getBytes());
    }

    public UUID getId() {
        return id;
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
