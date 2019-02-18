package com.bytecubed.commons.models.movement;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = CustomMoveDescriptor.class)
public abstract class MoveDescriptor {
    private Move move;

    public Move getMove() {
        return move;
    }

    public MoveDescriptor(Move move) {
        this.move = move;
    }
}


