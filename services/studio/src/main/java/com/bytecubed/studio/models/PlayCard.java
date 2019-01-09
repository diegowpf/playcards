package com.bytecubed.studio.models;

import com.bytecubed.commons.models.PlayerMarker;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.UUID;

public class PlayCard {

    @Id
    private final UUID id;
    private final List<PlayerMarker> playerMarkers;
    private UUID teamId;
    public PlayCard(UUID id, List<PlayerMarker> playerMarkers) {
        this.id = id;
        this.playerMarkers = playerMarkers;
    }

    public UUID getId() {
        return id;
    }

    public List<PlayerMarker> getPlayerMarkers() {
        return playerMarkers;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }
}
