package com.bytecubed.nlp.repository;

import com.bytecubed.commons.models.movement.CustomRoute;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RouteRepository extends MongoRepository<CustomRoute, UUID> {
    Optional<CustomRoute> findByName(String routeName);
}
