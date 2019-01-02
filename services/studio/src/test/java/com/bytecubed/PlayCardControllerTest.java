package com.bytecubed;

import com.bytecubed.studio.models.PlayCard;
import com.bytecubed.studio.persistence.PlayCardRepository;
import com.bytecubed.studio.web.PlayCardController;
import org.junit.Ignore;
import org.junit.Test;

import java.util.UUID;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayCardControllerTest {

    private UUID teamId;

    @Test
    @Ignore
    public void shouldReturnAllPlayersBasedOnPlayCardId(){
        PlayCardRepository repository = mock(PlayCardRepository.class);
        teamId = UUID.randomUUID();
        PlayCard playCard = null;//new PlayCardController.PlayCard(teamId, null);
        when(repository.findAll()).thenReturn( asList(playCard));

        PlayCardController controller = new PlayCardController(repository);
        assertThat(controller.getPlayCards(teamId).getBody().iterator().next()).isEqualTo(playCard);
    }
}