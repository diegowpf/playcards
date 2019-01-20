package com.bytecubed.commons.models.movement;

import com.bytecubed.commons.models.Placement;

public class CustomMoveDescriptor extends MoveDescriptor{
    Placement start;
    Placement end;

    public CustomMoveDescriptor(Move move, Placement start, Placement end) {
        super(move);
        this.start = start;
        this.end = end;
    }

    public Placement getEnd() {
        return end;
    }

    public Placement getStart() {
        return start;
    }
}
