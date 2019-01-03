package com.bytecubed.team.repository;

import com.bytecubed.commons.models.League;
import com.bytecubed.commons.models.Team;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.Arrays.asList;

@Component
public class TeamRegistry {

    private Map<League, List<Team>> teams = new HashMap();

    public TeamRegistry(){
        teams.put(League.nfl, asList(
                nfl("Baltimore Ravens", "BAL"),
                nfl("Arizona Cardinals", "ARI"),
                nfl("Atlanta Falcons", "ATL"),
                nfl("Buffalo Bills", "BUF"),
                nfl( "Carolina Panthers", "CAR" ),
                nfl( "Chicago Bears", "CHI" ),
                nfl( "Cincinnati Bengals", "CIN" ),
                nfl( "Cleveland Browns", "CLE" ),
                nfl( "Dallas Cowboys", "DAL" ),
                nfl( "Denver Broncos", "DEN" ),
                nfl( "Detroit Lions", "DET" ),
                nfl( "Green Bay Packers","GB" ),
                nfl( "Houston Texans", "Hou" ),
                nfl( "Indianapolis Colts", "IND" ),
                nfl( "Jacksonville Jaguars", "JAX" ),
                nfl( "Kansas City Chiefs", "KC" ),
                nfl( "Los Angeles Chargers", "LAC" ),
                nfl( "Los Angeles Rams", "LAR" ),
                nfl( "Miami Dolphins", "MIA"),
                nfl( "Minnesota Vikings", "MIN" ),
                nfl( "New England Patriots", "NE" ),
                nfl( "New Orleans Saints", "NO" ),
                nfl( "New York Giants", "NYG" ),
                nfl( "New York Jets", "NYJ" ),
                nfl( "Oakland Raiders", "OAK" ),
                nfl( "Philadelphia Eagles", "PHL" ),
                nfl( "Pittsburg Steelers", "PIT" ),
                nfl( "San Francisco 49ers", "SF" ),
                nfl( "Seattle Seahawks", "SEA" ),
                nfl( "Tampa Bay Buccaneers", "TB" ),
                nfl( "Tennesse Titans", "TEN" ),
                nfl( "Washington Redskins", "WAS" )));
    }

    public List<Team> getTeamsBy( League league ){
        List<Team> teams = this.teams.get(league);

        return teams == null ? asList() : teams;
    }

    static Team nfl(String name, String abbr){
        return new Team(League.nfl, name, abbr, UUID.nameUUIDFromBytes(name.getBytes()));
    }

    public Team getTeamByAbbr(League league, String teamAbbr) {
        return teams.get(league).stream()
                .filter(f->f.getAbbr().equals(teamAbbr))
                .findFirst()
                .orElse( new Team(League.nfl, "Fake"));
    }
}
