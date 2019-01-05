package com.bytecubed.team.web;

import com.bytecubed.commons.models.League;
import com.bytecubed.commons.models.Player;
import com.bytecubed.commons.models.Roster;
import com.bytecubed.commons.models.Team;
import com.bytecubed.team.provisioning.TeamRosterLoader;
import com.bytecubed.team.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/team")
public class TeamController {

    private TeamRepository repository;
    private RosterRepository rosters;
    private Logger logger = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    public TeamController(TeamRepository repository, RosterRepository rosters ) {
        this.repository = repository;
        this.rosters = rosters;
    }

    @GetMapping("/{league}/{teamId}")
    public ResponseEntity<Roster> getTeamRoster(@PathVariable League league, @PathVariable UUID teamId){
        List<Player> players = new TeamRosterLoader(this).load();
        logger.debug("Players:  " + players );

        return ok(new Roster(players));
//        return ok(rosters.findByTeamIdAndActiveTrue(teamId).stream().findFirst().get());
    }

    @RequestMapping( method = RequestMethod.PUT )
    public HttpEntity create( Team team ){
        repository.save(team);
        return created(null).build();
    }

}
