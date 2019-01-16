package com.bytecubed.commons;

import com.bytecubed.commons.models.PlayerMarker;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PlayCard {
    @Id
    private UUID id;
    private Formation formation;
    private PlayCardType playCardType;
    private String name;
    private LocalDateTime createTime;
    private UUID teamId;
    protected PlayCard(){}

    public PlayCard(LocalDateTime createTime,
                    UUID id,
                    UUID teamId,
                    List<PlayerMarker> playerMarkers,
                    String name) {
        this( createTime, id, teamId, playerMarkers, PlayCardType.Offense, name );
    }

    public PlayCard(LocalDateTime createTime,
                    UUID id,
                    UUID teamId,
                    List<PlayerMarker> playerMarkers,
                    PlayCardType playCardType,
                    String name) {
        this.createTime = createTime;
        this.teamId = teamId;
        this.id = id;
        this.formation = new Formation(playerMarkers.toArray(new PlayerMarker[0]));
        this.playCardType = playCardType;
        this.name = name;
    }

    public PlayCard(UUID teamId, Formation formation, String name) {
        this(LocalDateTime.now(), UUID.randomUUID(), teamId, formation.getPlayerMarkers(), name );
    }

    public Formation getFormation() {
        return formation;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public PlayCardType getPlayCardType() {
        return playCardType;
    }

    public void setPlayCardType(PlayCardType playCardType) {
        this.playCardType = playCardType;
    }

    public UUID getId() {
        return id;
    }

    public List<PlayerMarker> getPlayerMarkers() {
        return formation.getPlayerMarkers();
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
