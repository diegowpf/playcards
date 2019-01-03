package com.bytecubed.team.repository;

import com.bytecubed.commons.models.League;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class TeamRegistryTest {

    @Test
    public void shouldReturnAll32NFLTeams(){
        assertThat( new TeamRegistry().getTeamsBy(League.nfl)).hasSize(32);
    }

    @Test
    public void shouldReturnNoTeams(){
        assertThat( new TeamRegistry().getTeamsBy(League.ncaaf)).hasSize(0);
    }

}