package com.bytecubed.nlp.repository;

import com.bytecubed.commons.Play;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlayRepository extends MongoRepository<Play, UUID> {}
