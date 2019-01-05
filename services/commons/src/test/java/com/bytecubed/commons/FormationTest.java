package com.bytecubed.commons;

import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.nlp.exceptions.InvalidPlayerException;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FormationTest {

    @Test
    public void shouldReturnPlayerWithTheSpecifiedTag(){
        PlayerMarker x = mock(PlayerMarker.class);
        when(x.getTag()).thenReturn("X");
        PlayerMarker y = mock(PlayerMarker.class);
        when(y.getTag()).thenReturn("Y");
        Formation formation = new Formation(x, y);

        assertThat(formation.getPlayerMarkerAt("X")).isEqualTo(x);
    }

    @Test(expected= InvalidPlayerException.class)
    public void shouldReturnPlayerWithTheSpecifiedTagThatDoesNotExist(){
        PlayerMarker x = mock(PlayerMarker.class);
        when(x.getTag()).thenReturn("X");
        PlayerMarker y = mock(PlayerMarker.class);
        when(y.getTag()).thenReturn("Y");
        Formation formation = new Formation(x, y);

        assertThat(formation.getPlayerMarkerAt("Z")).isNull(

        );
    }
}