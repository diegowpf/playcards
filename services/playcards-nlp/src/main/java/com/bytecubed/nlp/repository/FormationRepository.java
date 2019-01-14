package com.bytecubed.nlp.repository;

import com.bytecubed.commons.Formation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FormationRepository extends MongoRepository<Formation, UUID> {
}
