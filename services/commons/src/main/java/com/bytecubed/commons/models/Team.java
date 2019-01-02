package com.bytecubed.commons.models;


import org.springframework.data.annotation.Id;

import java.util.UUID;

public class Team {

    @Id
    private UUID Id;
    private String name;
    private League league;

    public Team( String name, League league ){
        this.name = name;
        this.league = league;
    }

    public UUID getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public League getLeague() {
        return league;
    }
}
