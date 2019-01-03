package com.bytecubed.studio.parser;

import com.bytecubed.commons.models.PlayerMarker;

import java.util.List;

public interface PlayCardParser {
    List<PlayerMarker> extractPlayerPlacements();
}
