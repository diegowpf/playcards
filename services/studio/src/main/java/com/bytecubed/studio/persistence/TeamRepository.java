package com.bytecubed.studio.persistence;

import com.bytecubed.commons.models.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeamRepository extends MongoRepository<Team, UUID> {
}
