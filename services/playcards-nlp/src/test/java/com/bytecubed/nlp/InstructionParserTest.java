package com.bytecubed.nlp;

import com.bytecubed.commons.Formation;
import com.bytecubed.commons.IFormation;
import com.bytecubed.commons.PlayCard;
import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.commons.models.movement.CustomRoute;
import com.bytecubed.commons.models.movement.StandardMoveDescriptor;
import com.bytecubed.nlp.parsing.InstructionParser;
import com.bytecubed.nlp.parsing.models.RouteCommand;
import org.bots4j.wit.WitClient;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static com.bytecubed.commons.models.movement.Move.comeback;
import static com.bytecubed.commons.models.movement.Move.go;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InstructionParserTest {

    @Test
    public void shouldReturnGoRouteForPlayerX(){
        Formation formation = mock(Formation.class);
        PlayerMarker playerMarker = new PlayerMarker(null, "wr", "x", false);
        when(formation.getPlayerMarkerAt("X")).thenReturn(playerMarker);

        CustomRoute route = new InstructionParser(getClient()).parse("X has a 5 yard go route");
        StandardMoveDescriptor moveDescriptor = (StandardMoveDescriptor) route.getMoveDescriptors().get(0);

        assertThat(moveDescriptor.getMove()).isEqualTo(go);
        assertThat(moveDescriptor.getDistance()).isEqualTo(5);
        assertThat(route.getPlayer()).isEqualToIgnoringCase(playerMarker.getTag());
    }

    @Test
    @Ignore
    public void shouldCreateXWithA5StepComebackFromStandardFormation(){
        Formation formation = new IFormation();
        PlayCard card = new PlayCard(UUID.randomUUID(), formation, "Fake card");

        CustomRoute route = new InstructionParser(getClient()).parse("X has a 5 yard go route");
        card.apply(route);

        System.out.println(card);

        RestTemplate template = new RestTemplate();
        String response = template.postForEntity( "http://server.immersivesports.ai/playcards/team/c679919f-d524-3f75-ad2a-5161706e12a5", card, String.class).getBody();
        System.out.println( response );

    }

    @Test
    public void shouldExtractRouteCommands(){
        List<RouteCommand> commands = new InstructionParser(getClient())
                .getRouteCommands("x has a straight y has a slant z has a comeback");

        assertThat(commands).hasSize(3);
        assertThat(commands.get(0).getPlayerTag()).isEqualTo("X");
        assertThat(commands.get(0).getRouteName()).isEqualTo("straight");
        assertThat(commands.get(1).getPlayerTag()).isEqualTo("Y");
        assertThat(commands.get(1).getRouteName()).isEqualTo("slant");
        assertThat(commands.get(2).getPlayerTag()).isEqualTo("Z");
        assertThat(commands.get(2).getRouteName()).isEqualTo("comeback");
    }

    @Test
    public void shouldReturnComebackRouteForPlayerX(){
        Formation formation = mock(Formation.class);
        PlayerMarker playerMarker = new PlayerMarker(null, "wr", "x", false);
        when(formation.getPlayerMarkerAt("X")).thenReturn(playerMarker);

        CustomRoute route = new InstructionParser(getClient()).parse("X has a 5 yard comeback");
        StandardMoveDescriptor moveDescriptor = (StandardMoveDescriptor) route.getMoveDescriptors().get(0);

        assertThat(moveDescriptor.getMove()).isEqualTo(comeback);
        assertThat(moveDescriptor.getDistance()).isEqualTo(5);
        assertThat(route.getPlayer()).isEqualToIgnoringCase(playerMarker.getTag());
    }

    @Test
    @Ignore
    public void shouldParseCommandOfKnownRoute(){
        Formation formation = mock(Formation.class);
        PlayerMarker playerMarker = new PlayerMarker(null, "wr", "x", false);
        when(formation.getPlayerMarkerAt("X")).thenReturn(playerMarker);

        CustomRoute route = new InstructionParser(getClient()).parse("x has a straight y has a slant z has a comeback");
        StandardMoveDescriptor moveDescriptor = (StandardMoveDescriptor) route.getMoveDescriptors().get(0);

        assertThat(moveDescriptor.getMove()).isEqualTo(comeback);
        assertThat(moveDescriptor.getDistance()).isEqualTo(5);
        assertThat(route.getPlayer()).isEqualToIgnoringCase(playerMarker.getTag());

    }

    private WitClient getClient() {
        return new WitClient("GATGS2KRUABA3DSF75GTUNQO4BPQOADV");
    }
}