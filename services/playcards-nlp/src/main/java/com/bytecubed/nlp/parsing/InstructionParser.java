package com.bytecubed.nlp.parsing;

import com.bytecubed.commons.models.movement.CustomRoute;
import com.bytecubed.commons.models.movement.Move;
import com.bytecubed.commons.models.movement.StandardMoveDescriptor;
import com.bytecubed.nlp.parsing.models.RouteCommand;
import com.google.api.client.util.ArrayMap;
import org.bots4j.wit.WitClient;
import org.bots4j.wit.beans.GetIntentViaTextResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Component
public class InstructionParser {

    private Logger logger = LoggerFactory.getLogger(InstructionParser.class);
    private WitClient client;

    @Autowired
    public InstructionParser(WitClient client){
        this.client = client;
    }

    public CustomRoute parse(String routeDescription) {
        GetIntentViaTextResponse response = client.getIntentViaText(routeDescription,null,null,null,null);
        ArrayMap distanceMap = (ArrayMap) ((List)response.getOutcomes().get(0).getEntities().get("distance")).get(0);
        double yards = Double.valueOf( distanceMap.get("value").toString());

        String playerTag = response.getOutcomes().get(0).getEntities().firstEntityValue("player_position");
        String route_type = response.getOutcomes().get(0).getEntities().firstEntityValue("route_type");

        logger.debug("Found:   " + playerTag );
        logger.debug("Route Type: " + route_type );

        return new CustomRoute( asList(new StandardMoveDescriptor(yards, Move.valueOf(route_type))), playerTag );
//        return new CustomRoute(yards, Move.valueOf(route_type), playerTag);
    }

    public List<RouteCommand> getRouteCommands(String text ){
        List<RouteCommand> commands = new ArrayList();
        GetIntentViaTextResponse response = client.getIntentViaText(text,null,null,null,null);
        logger.debug( "Wit Repsonse:  " + response.getText() );

        List<String> playerPositions = response.getOutcomes().get(0).getEntities().entityValues("player_position");
        List<String> routeType = response.getOutcomes().get(0).getEntities().entityValues("route_type");

        logger.debug( "player positions:  " + playerPositions );
        logger.debug( "route type:  " + routeType );

        if(playerPositions != null && routeType != null ) {

            for (int i = 0; i < playerPositions.size(); i++) {
                commands.add(new RouteCommand(playerPositions.get(i), routeType.get(i)));
            }
        }

        return commands;
    }

}
