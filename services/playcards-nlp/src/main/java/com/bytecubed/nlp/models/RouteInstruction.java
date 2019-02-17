package com.bytecubed.nlp.models;

import java.util.UUID;

public class RouteInstruction {
    private final String tag;
    private final UUID routeId;

    public RouteInstruction(String tag, UUID routeId) {
        this.tag = tag;
        this.routeId = routeId;
    }

    public String getTag() {
        return tag;
    }

    public UUID getRouteId() {
        return routeId;
    }
}
