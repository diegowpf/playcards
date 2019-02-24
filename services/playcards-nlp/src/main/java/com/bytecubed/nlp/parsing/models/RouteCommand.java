package com.bytecubed.nlp.parsing.models;

public class RouteCommand {
    private final String playerTag;
    private final String routeName;

    public RouteCommand(String playerTag, String routeName) {
        this.playerTag = playerTag;
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }

    public String getPlayerTag() {
        return playerTag;
    }
}
