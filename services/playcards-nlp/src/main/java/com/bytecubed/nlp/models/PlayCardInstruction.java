package com.bytecubed.nlp.models;

import java.util.List;
import java.util.UUID;

public class PlayCardInstruction {
    private UUID formationId;
    private List<RouteInstruction> routes;

    public PlayCardInstruction() { }

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
}
