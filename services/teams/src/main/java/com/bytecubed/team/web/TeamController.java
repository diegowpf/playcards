package com.bytecubed.team.web;

import com.bytecubed.commons.models.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
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

    @Component
    public class TeamRepository {
        public void save(Team team) {

        }
    }
}
