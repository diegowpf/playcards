package com.bytecubed.persistence;

import com.bytecubed.models.PlayCard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayCardRepository {
    Map<UUID, PlayCard> playCards;

    public PlayCardRepository() {
        playCards = new HashMap();
    }

    public Iterable<PlayCard> getPlayCards() {
        return playCards.values();
    }

    public void save(PlayCard card) {
        playCards.put(card.getId(), card);
    }
}
