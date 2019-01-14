package com.bytecubed.commons;

import com.bytecubed.commons.models.Placement;
import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.nlp.exceptions.InvalidPlayerException;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;

public class Formation {

    public static final int PLAYER_HORIZONTAL_SPACING = 8;
    private String name;
    private List<PlayerMarker> players;

    @Id
    private UUID id;

    public Formation(){
        this(null, "",new PlayerMarker[0]);
    }
    public Formation(PlayerMarker... players) {
        this(randomUUID(), "", players);
    }

    public Formation(UUID id, String name, PlayerMarker... players ){
        this.id = id;
        this.name = name;
        this.players = new ArrayList(asList(players));
    }

    public Formation(UUID id, Formation formation) {
        this(id, formation.getName(),
                formation.getPlayerMarkers().toArray(new PlayerMarker[0]));
    }

    private String getName() {
        return name;
    }

    public PlayerMarker getPlayerMarkerAt(String tag) {
        return players.stream()
                .filter(f->f.getTag() != null && f.getTag().equalsIgnoreCase(tag))
                .findFirst()
                .orElseThrow(() -> new InvalidPlayerException( "No such player:  " + tag));
    }

    public Formation andQbUnderCenter() {
        PlayerMarker center = this.getCenter();
        players.add(newMarkerBehind(center, "QB", "QB"));

        return this;
    }

    private PlayerMarker newMarkerBehind(PlayerMarker marker, String pos, String tag) {
        return new PlayerMarker(new Placement(marker.getPlacement().getRelativeX(),
                marker.getPlacement().getRelativeY() + PLAYER_HORIZONTAL_SPACING), pos, tag );
    }

    private PlayerMarker getCenter() {
        return players.stream().filter(PlayerMarker::isCenter)
                .findFirst()
                .get();
    }

    public Formation andXIsOnLeftOffTheBallOutsideTheNumbers() {
        this.players.add(new PlayerMarker(new Placement(20, 2 ), "wr", "X"));
        return this;
    }

    public Formation andYIsOnTheRightLinedUpWithQBOutSideTheNumbers() {
        this.players.add(new PlayerMarker(new Placement(145, 8 ), "wr", "Y"));
        return this;
    }

    public Formation andFullBackBehindQB() {
        PlayerMarker qb = getPlayerMarkerAt("QB" );
        this.players.add( newMarkerBehind(qb, "FB", "FB"));

        return this;
    }

    public Formation andHalfBackBehindFullBack() {
        PlayerMarker fb = getPlayerMarkerAt("FB" );
        this.players.add( newMarkerBehind(fb, "HB", "HB"));

        return this;
    }

    public List<PlayerMarker> getPlayerMarkers() {
        return players;
    }

    public Formation addTightEndOnTheBallOnTheRight() {
        this.players.add(new PlayerMarker(new Placement(104, 2 ), "te", "T"));
        return this;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "name='" + name + '\'' +
                ", players=" + players +
                ", id=" + id +
                '}';
    }
}
