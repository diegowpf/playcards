package com.bytecubed.team.web;

import com.bytecubed.commons.models.Roster;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
public interface RosterRepository extends MongoRepository<Roster, UUID> {
    List<Roster> findByActiveTrue();
    List<Roster> findByTeamIdAndActiveTrue(UUID teamId);
}
