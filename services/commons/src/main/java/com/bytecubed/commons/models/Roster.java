package com.bytecubed.commons.models;


import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;

public class Roster {

    @Id
    private UUID id;
    private List<Player> players;
    private UUID teamId;
    private boolean active;

    public Roster(){
        this(asList());
    }

    public Roster(List<Player> players) {
        this.players = players;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public boolean isActive() {
        return active;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
