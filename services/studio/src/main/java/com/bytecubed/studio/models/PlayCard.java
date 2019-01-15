package com.bytecubed.studio.models;

import com.bytecubed.commons.models.PlayerMarker;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.UUID;

public class PlayCard {

    @Id
    private UUID id;
    private List<PlayerMarker> playerMarkers;
    private PlayCardType playCardType;
    private UUID teamId;

    protected PlayCard(){}

    public PlayCard(UUID teamId, UUID id, List<PlayerMarker> playerMarkers) {
        this( teamId, id, playerMarkers, PlayCardType.Offense);
    }

    public PlayCard(UUID teamId, UUID id, List<PlayerMarker> playerMarkers, PlayCardType playCardType) {
        this.teamId = teamId;
        this.id = id;
        this.playerMarkers = playerMarkers;
        this.playCardType = playCardType;
    }

    public PlayCardType getPlayCardType() {
        return playCardType;
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

    public enum PlayCardType {
        Offense,
        Defense,
        SpecialTeams
    }
}
