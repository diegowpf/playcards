package com.bytecubed.commons.models.movement;

import com.bytecubed.commons.models.Placement;

import java.util.ArrayList;
import java.util.List;

public class CustomRoute implements Route {
    private List<MoveDescriptor> moveDescriptors;
    private String marker;

    public CustomRoute(List<MoveDescriptor> moveDescriptors, String marker ){
        this.moveDescriptors = moveDescriptors;
        this.marker = marker;
    }

    public CustomRoute() {
    }

    @Override
    public String getPlayer() {
        return marker;
    }

    @Override
    public List<MoveDescriptor> getMoveDescriptors() {
        return moveDescriptors;
    }

    public CustomRoute transform(double xOffset, double yOffSet) {
        List<MoveDescriptor> moves =  new ArrayList();
        moveDescriptors.forEach( m->{
            Placement start  = ((CustomMoveDescriptor) m).getStart().adjust(xOffset, yOffSet );
            Placement end  = ((CustomMoveDescriptor) m).getEnd().adjust(xOffset, yOffSet );

            moves.add(new CustomMoveDescriptor(start,end));
        });

        return new CustomRoute(moves,this.marker);
    }
}
