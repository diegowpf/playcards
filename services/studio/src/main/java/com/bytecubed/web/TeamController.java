package com.bytecubed.web;

import com.bytecubed.models.Team;
import com.bytecubed.persistence.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/team")
public class TeamController {

    private TeamRepository repository;

    @Autowired
    public TeamController(TeamRepository repository ) {
        this.repository = repository;
    }

    @RequestMapping( method = RequestMethod.PUT )
    public HttpEntity create( Team team ){
        repository.save(team);
        return created(null).build();
    }
}
