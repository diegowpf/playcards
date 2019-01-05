package com.bytecubed.team.web;

import com.bytecubed.commons.models.Player;
import com.bytecubed.team.provisioning.TeamRosterLoader;
import com.bytecubed.team.repository.PlayerRepository;
import com.bytecubed.team.repository.TeamRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping( "/data")
public class DataController {

    private TeamRosterLoader teamRosterLoader;
    private PlayerRepository playerRepository;

    @Autowired
    public DataController(TeamRosterLoader teamRosterLoader, PlayerRepository playerRepository){
        this.teamRosterLoader = teamRosterLoader;
        this.playerRepository = playerRepository;
    }

    @GetMapping("/team")
    public ResponseEntity loadRosterData(){
        teamRosterLoader.load().forEach(playerRepository::save);
        return ok().build();
    }

}
