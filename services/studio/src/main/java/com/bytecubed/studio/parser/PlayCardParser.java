package com.bytecubed.studio.parser;

import com.bytecubed.commons.models.Player;

import java.util.List;

public interface PlayCardParser {
    List<Player> extractPlayerPlacements();
}
