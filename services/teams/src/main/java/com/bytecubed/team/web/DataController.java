package com.bytecubed.team.web;

import com.bytecubed.team.provisioning.TeamRosterLoader;
import com.bytecubed.team.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
