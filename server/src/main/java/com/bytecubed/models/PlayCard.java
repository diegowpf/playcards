package com.bytecubed.models;

import com.bytecubed.parser.Player;

import java.util.UUID;

public class PlayCard {

    private final UUID id;
    private Iterable<Player> players;

    public Iterable<Player> getPlayers() {
        return players;
    }

    public PlayCard(Iterable<Player> players ){
        this.players = players;
        this.id = UUID.randomUUID();
    }
    public UUID getId() {
        return id;
    }
}
