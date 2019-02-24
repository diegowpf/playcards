package com.bytecubed.nlp.models;

import com.bytecubed.commons.models.movement.CustomRoute;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayCardInstruction {
    private UUID formationId;
    private List<RouteInstruction> routes;

    public PlayCardInstruction() { }

    public PlayCardInstruction(UUID formationId ){
        this(formationId, new ArrayList());
    }

    public PlayCardInstruction(UUID formationId, List<RouteInstruction> routes ) {
        this.formationId = formationId;
        this.routes = routes;
    }

    public List<RouteInstruction> getRoutes() {
        return routes;
    }

    public UUID getFormationId() {
        return formationId;
    }

    public void addRoute(String playerTag, CustomRoute route ){
        routes.add(new RouteInstruction(playerTag, route.getId()));
    }
}
