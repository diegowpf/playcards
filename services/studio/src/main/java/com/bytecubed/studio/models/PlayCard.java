package com.bytecubed.studio.models;

import com.bytecubed.commons.models.PlayerMarker;

import java.util.List;
import java.util.UUID;

public class PlayCard {

    private final UUID id;
    private final List<PlayerMarker> playerMarkers;
    private UUID teamId;

    public PlayCard(UUID id, List<PlayerMarker> playerMarkers) {
        this.id = id;
        this.playerMarkers = playerMarkers;
    }

    public List<PlayerMarker> getPlayers() {
        return null;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }
}
