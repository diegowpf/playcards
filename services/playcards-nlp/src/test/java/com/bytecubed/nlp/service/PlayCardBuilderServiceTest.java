package com.bytecubed.nlp.service;

import com.bytecubed.commons.Formation;
import com.bytecubed.commons.PlayCard;
import com.bytecubed.commons.models.Placement;
import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.commons.models.movement.CustomMoveDescriptor;
import com.bytecubed.commons.models.movement.CustomRoute;
import com.bytecubed.nlp.models.PlayCardInstruction;
import com.bytecubed.nlp.models.RouteInstruction;
import com.bytecubed.nlp.repository.FormationRepository;
import com.bytecubed.nlp.repository.RouteRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayCardBuilderServiceTest {

    private RouteRepository routeRepository;

    @Before
    public void setUp(){
        routeRepository = mock(RouteRepository.class);
    }

    @Test
    public void shouldGenerateNewPlayCardWithSinglePlayerOnPlayCard(){
        UUID formationId = randomUUID();
        UUID routeId = randomUUID();

        FormationRepository formationRepository = mock(FormationRepository.class);
        Formation formation = new Formation(formationId, "fakeName", asList(new PlayerMarker(new Placement(30d, 0), "wr", "X")));
        when(formationRepository.findById(any(UUID.class))).thenReturn(
                Optional.of(formation));

        RouteRepository routeRepository = mock(RouteRepository.class);
        when(routeRepository.findById(routeId)).thenReturn(Optional.of(new CustomRoute(
                asList(new CustomMoveDescriptor(
                        new Placement(0d, 0d),
                        new Placement(0d, 5d))), "X")));

        PlayCard playCard = new PlayCardBuilderService(formationRepository, routeRepository)
                .buildFrom(new PlayCardInstruction(formationId, asList(new RouteInstruction("X", routeId))));

        assertThat(playCard.getFormation()).isEqualTo(formation);
        assertThat(formation.getPlayerMarkers().get(0).getRoutes()).hasSize(1);
    }

}