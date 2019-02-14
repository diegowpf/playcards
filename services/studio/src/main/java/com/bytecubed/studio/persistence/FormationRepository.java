package com.bytecubed.studio.persistence;

import com.bytecubed.commons.Formation;
import com.bytecubed.commons.PlayCard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FormationRepository extends MongoRepository<Formation, UUID> {
}
