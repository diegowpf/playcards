package com.bytecubed.team.web;

import com.bytecubed.commons.models.League;
import com.bytecubed.commons.models.Player;
import com.bytecubed.team.repository.TeamRegistry;

public class NFLResponsePlayerObj {

    private String position;
    private String name;
    private String teamAbbr;

    public NFLResponsePlayerObj(){}

    public Player toPlayer(TeamRegistry registry ){
        return new Player( name, position, registry.getTeamByAbbr(League.nfl, teamAbbr ));
    }

    public String getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getTeamAbbr() {
        return teamAbbr;
    }
}
