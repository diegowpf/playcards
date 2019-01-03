package com.bytecubed.team.web;

import com.bytecubed.commons.models.Player;

import java.util.List;

public class NFLResponse {
    public List<Player> getPlayers() {
        return players;
    }

    private List<Player> players;
}
