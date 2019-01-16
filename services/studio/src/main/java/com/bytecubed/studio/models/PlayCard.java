package com.bytecubed.studio.models;

import com.bytecubed.commons.models.PlayerMarker;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PlayCard {

    @Id
    private UUID id;
    private List<PlayerMarker> playerMarkers;
    private PlayCardType playCardType;
    private LocalDateTime createTime;
    private UUID teamId;

    protected PlayCard(){}

    public PlayCard(LocalDateTime createTime, UUID id, UUID teamId, List<PlayerMarker> playerMarkers) {
        this( createTime, id, teamId, playerMarkers, PlayCardType.Offense);
    }

    public PlayCard(LocalDateTime createTime, UUID id, UUID teamId, List<PlayerMarker> playerMarkers, PlayCardType playCardType) {
        this.createTime = createTime;
        this.teamId = teamId;
        this.id = id;
        this.playerMarkers = playerMarkers;
        this.playCardType = playCardType;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
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
