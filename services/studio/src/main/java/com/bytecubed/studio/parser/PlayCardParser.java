package com.bytecubed.studio.parser;

import com.bytecubed.commons.PlayCard;
import com.bytecubed.commons.models.PlayerMarker;

import java.util.List;

public interface PlayCardParser {
    List<PlayCard> extractPlayCards();
}
