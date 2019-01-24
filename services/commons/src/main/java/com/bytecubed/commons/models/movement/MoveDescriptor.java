package com.bytecubed.commons.models.movement;

public abstract class MoveDescriptor {
    private Move move;

    public Move getMove() {
        return move;
    }

    public MoveDescriptor(Move move) {
        this.move = move;
    }
}


