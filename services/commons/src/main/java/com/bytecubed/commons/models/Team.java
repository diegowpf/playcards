package com.bytecubed.commons.models;


import org.springframework.data.annotation.Id;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public class Team {

    private String name;
    private League league;
    private String abbr;

    @Id
    private UUID id;

    public Team(){}

    public Team(League league, String name){
        this(league, name, "", randomUUID());
    }

    public String getAbbr() {
        return abbr;
    }

    public Team(League league, String name, String abbr, UUID id) {
        this.abbr = abbr;
        this.id = id;
        this.name = name;
        this.league = league;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public League getLeague() {
        return league;
    }
}
