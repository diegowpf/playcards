package com.bytecubed.models;

import com.bytecubed.parser.Player;

import java.util.UUID;

public class PlayCard {

    private final UUID id;
    private UUID teamId;
    private Iterable<Player> players;

    public PlayCard(UUID teamId, Iterable<Player> players){
        this.teamId = teamId;
        this.players = players;
        this.id = UUID.randomUUID();
    }

    public Iterable<Player> getPlayers() {
        return players;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public UUID getId() {
        return id;
    }
}
