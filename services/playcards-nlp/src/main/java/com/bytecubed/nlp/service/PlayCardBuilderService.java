package com.bytecubed.nlp.service;

import com.bytecubed.commons.Formation;
import com.bytecubed.commons.PlayCard;
import com.bytecubed.nlp.models.PlayCardInstruction;
import com.bytecubed.nlp.repository.FormationRepository;
import com.bytecubed.nlp.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlayCardBuilderService {
    private FormationRepository formationRepository;
    private RouteRepository routeRepository;

    @Autowired
    public PlayCardBuilderService(FormationRepository formationRepository,
                                  RouteRepository routeRepository) {
        this.formationRepository = formationRepository;
        this.routeRepository = routeRepository;
    }

    public PlayCard buildFrom(PlayCardInstruction instruction) {

        Formation formation = formationRepository.findById(instruction.getFormationId()).get();

        instruction.getRoutes().forEach(r->{
            formation.getPlayerMarkerAt(r.getTag())
                    .applyRoute(routeRepository.findById(r.getRouteId()).get());
        });

        return new PlayCard(UUID.randomUUID(), formation, "fakeName" );
    }
}
