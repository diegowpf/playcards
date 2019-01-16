package com.bytecubed.nlp;

import com.bytecubed.commons.Formation;
import com.bytecubed.commons.FormationFactory;
import com.bytecubed.commons.IFormation;
import com.bytecubed.commons.PlayCard;
import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.commons.models.Route;
import com.bytecubed.nlp.parsing.InstructionParser;
import org.bots4j.wit.WitClient;
import org.junit.Test;

import java.util.UUID;

import static com.bytecubed.commons.models.Move.comeback;
import static com.bytecubed.commons.models.Move.go;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InstructionParserTest {

    @Test
    public void shouldReturnGoRouteForPlayerX(){
        Formation formation = mock(Formation.class);
        PlayerMarker playerMarker = new PlayerMarker(null, "wr", "x", false);
        when(formation.getPlayerMarkerAt("X")).thenReturn(playerMarker);

        Route route = new InstructionParser(getClient()).parse("X has a 5 yard go route", formation);
        assertThat(route.getMove()).isEqualTo(go);
        assertThat(route.getDistance()).isEqualTo(5);
        assertThat(route.getPlayer()).isEqualTo(playerMarker);
    }

    @Test
    public void shouldCreateXWithA5StepComebackFromStandardFormation(){
        Formation formation = new IFormation();
        PlayCard card = new PlayCard(UUID.randomUUID(), formation, "Fake card");

        Route route = new InstructionParser(getClient()).parse("X has a 5 yard go route", formation);
//        formation.apply(route);
    }

    @Test
    public void shouldReturnComebackRouteForPlayerX(){
        Formation formation = mock(Formation.class);
        PlayerMarker playerMarker = new PlayerMarker(null, "wr", "x", false);
        when(formation.getPlayerMarkerAt("X")).thenReturn(playerMarker);

        Route route = new InstructionParser(getClient()).parse("X has a 5 yard comeback", formation);
        assertThat(route.getMove()).isEqualTo(comeback);
        assertThat(route.getDistance()).isEqualTo(5);
        assertThat(route.getPlayer()).isEqualTo(playerMarker);
    }

    private WitClient getClient() {
        return new WitClient("GATGS2KRUABA3DSF75GTUNQO4BPQOADV");
    }
}