package com.bytecubed.commons.models.movement;

import java.util.List;

public interface Route {
    String getPlayer();
    List<MoveDescriptor> getMoveDescriptors();
}
