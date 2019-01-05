package com.bytecubed.commons;

import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.nlp.exceptions.InvalidPlayerException;

import java.util.Arrays;

public class Formation {

    private PlayerMarker[] players;

    public Formation(PlayerMarker... players) {
        this.players = players;
    }

    public PlayerMarker getPlayerMarkerAt(String tag) {
        return Arrays.stream(players)
                .filter(f->f.getTag().equals(tag))
                .findFirst()
                .orElseThrow(() -> new InvalidPlayerException( "No such player:  " + tag));
    }
}
