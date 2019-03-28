package com.bytecubed.nlp.web;

public class NFLGameResponse {
    String gameId;
    String season;
    String seasonType;
    String gameKey;
    String homeTeamAbbr;
    String visitorTeamAbbr;

    public String getHomeTeamAbbr() {
        return homeTeamAbbr;
    }

    public String getVisitorTeamAbbr() {
        return visitorTeamAbbr;
    }

    public String getGameId() {
        return gameId;
    }

    public String getSeason() {
        return season;
    }

    public String getSeasonType() {
        return seasonType;
    }

    public String getGameKey() {
        return gameKey;
    }
}
