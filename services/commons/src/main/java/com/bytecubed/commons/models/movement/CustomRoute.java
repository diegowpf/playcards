package com.bytecubed.commons.models.movement;

import com.bytecubed.commons.models.Placement;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class CustomRoute implements Route {
    private String name;
    private List<MoveDescriptor> moveDescriptors;
    private String marker;
    @Id
    private UUID id;

    public CustomRoute(List<MoveDescriptor> moveDescriptors, String marker ){
        id = randomUUID();
        this.moveDescriptors = moveDescriptors;
        this.marker = marker;
    }

    public CustomRoute() {
    }

    public CustomRoute(UUID id, String name, CustomRoute route) {
        this.id = id;
        this.name = name;
        this.moveDescriptors = route.getMoveDescriptors();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
