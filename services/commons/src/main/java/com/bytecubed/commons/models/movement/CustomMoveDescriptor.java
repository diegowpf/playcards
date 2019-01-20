package com.bytecubed.commons.models.movement;

import com.bytecubed.commons.models.Placement;

public class CustomMoveDescriptor extends MoveDescriptor{
    Placement start;
    Placement end;

    public CustomMoveDescriptor(Move move) {
        super(move);
    }
}
