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
                nfl("Baltimore Ravens"),
                nfl("Arizona Cardinals"),
                nfl("Atlanta Falcons"),
                nfl("Buffalo Bills"),
                nfl( "Carolina Panthers" ),
                nfl( "Chicago Bears" ),
                nfl( "Cincinnati Bengals" ),
                nfl( "Cleveland Browns" ),
                nfl( "Dallas Cowboys" ),
                nfl( "Denver Broncos" ),
                nfl( "Detroit Lions" ),
                nfl( "Green Bay Packers" ),
                nfl( "Houston Texans" ),
                nfl( "Indianapolis Colts" ),
                nfl( "Jacksonville Jaguars" ),
                nfl( "Kansas City Chiefs" ),
                nfl( "Los Angeles Chargers" ),
                nfl( "Los Angeles Rams" ),
                nfl( "Miami Dolphins"),
                nfl( "Minnesota Vikings" ),
                nfl( "New England Patriots" ),
                nfl( "New Orleans Saints" ),
                nfl( "New York Giants" ),
                nfl( "New York Jets" ),
                nfl( "Oakland Raiders" ),
                nfl( "Philadelphia Eagles" ),
                nfl( "Pittsburg Steelers" ),
                nfl( "San Francisco 49ers" ),
                nfl( "Seattle Seahawks" ),
                nfl( "Tampa Bay Buccaneers" ),
                nfl( "Tennesse Titans" ),
                nfl( "Washington Redskins" )));
    }

    public List<Team> getTeamsBy( League league ){
        List<Team> teams = this.teams.get(league);

        return teams == null ? asList() : teams;
    }

    static Team nfl(String name){
        return new Team(League.nfl, name, UUID.nameUUIDFromBytes(name.getBytes()));
    }
}
