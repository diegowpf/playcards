package com.bytecubed.nlp.design;

import com.bytecubed.commons.Formation;
import com.bytecubed.commons.models.Placement;
import com.bytecubed.commons.models.PlayerMarker;
import org.springframework.stereotype.Component;

@Component
public class FormationFactory {

    public Formation withStandardTemplateInTheCenter(){
        return new Formation(
                new PlayerMarker(new Placement(72,0 ), "lt", "lt"),
                new PlayerMarker(new Placement(64,0 ), "lg", "lg"),
                new PlayerMarker(new Placement(88,0 ), "rt", "rt"),
                new PlayerMarker(new Placement(96,0 ), "rg", "rg"),
                new PlayerMarker(new Placement(80,0 ), "C", "C", true)
        );
    }

    public Formation standardTemplateFromLeftHash(){
        return new Formation(new PlayerMarker(new Placement(70,0), "center", "", true ));
    }

    public Formation standardTemplateFromRightHash(){
        return null;
    }

    public Formation buildFormation(FormationType formationType ){
        switch(formationType){
            case StandardLeft:
                return standardTemplateFromLeftHash();
            case StandardRight:
                return standardTemplateFromRightHash();
            case StandardCenter:
                return withStandardTemplateInTheCenter();
        }

        return withStandardTemplateInTheCenter();
    }
}
