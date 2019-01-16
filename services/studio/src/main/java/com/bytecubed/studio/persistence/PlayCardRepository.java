package com.bytecubed.studio.persistence;

import com.bytecubed.commons.PlayCard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlayCardRepository extends MongoRepository<PlayCard, UUID> {

    List<PlayCard> findAllByTeamId(UUID teamId);
}
