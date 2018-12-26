package com.bytecubed;

import com.bytecubed.models.PlayCard;
import com.bytecubed.persistence.PlayCardRepository;
import com.bytecubed.web.PlayCardController;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayCardControllerTest {

    @Test
    @Ignore
    public void shouldReturnAllPlayersBasedOnPlayCardId(){
        PlayCardRepository repository = mock(PlayCardRepository.class);
        PlayCard playCard = new PlayCard(new ArrayList<>());
        when(repository.getPlayCards()).thenReturn( asList(playCard));

        PlayCardController controller = new PlayCardController(repository);
        assertThat(controller.getPlayCards().getBody().iterator().next()).isEqualTo(playCard);
    }
}