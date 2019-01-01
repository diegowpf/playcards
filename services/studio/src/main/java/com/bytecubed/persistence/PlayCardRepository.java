package com.bytecubed.persistence;

import com.bytecubed.models.PlayCard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Arrays.asList;

@Repository
public interface PlayCardRepository extends MongoRepository<PlayCard, UUID> {

    List<PlayCard> findAllByTeamId(UUID teamId);
}
