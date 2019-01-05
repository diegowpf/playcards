package com.bytecubed.nlp.design;

import com.bytecubed.commons.Formation;
import com.bytecubed.commons.models.Placement;
import com.bytecubed.commons.models.PlayerMarker;
import org.springframework.stereotype.Component;

@Component
public class FormationFactory {

    public Formation standardFormationCenter(){
        return null;
    }

    public Formation standardFormationLeftHash(){
        return new Formation(new PlayerMarker(new Placement(70,0), "center", "", true ));
    }

    public Formation standardFormationRightHash(){
        return null;
    }

    public Formation buildFormation(FormationType formationType ){
        switch(formationType){
            case StandardLeft:
                return standardFormationLeftHash();
            case StandardRight:
                return standardFormationRightHash();
            case StandardCenter:
                return standardFormationCenter();
        }

        return standardFormationCenter();
    }
}
