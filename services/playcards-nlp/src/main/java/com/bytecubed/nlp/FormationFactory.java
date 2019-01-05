package com.bytecubed.nlp;

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

        /*
        00:03:42.130 [main] DEBUG com.bytecubed.studio.parser.RavensPowerPointParser - { placement: { relativeX: 70, relativeY: 0}, pos: "center", tag: "" },
00:03:42.130 [main] DEBUG com.bytecubed.studio.parser.RavensPowerPointParser - { placement: { relativeX: 86, relativeY: 0}, pos: "wr", tag: "" },
00:03:42.130 [main] DEBUG com.bytecubed.studio.parser.RavensPowerPointParser - { placement: { relativeX: 62, relativeY: 0}, pos: "wr", tag: "" },
00:03:42.130 [main] DEBUG com.bytecubed.studio.parser.RavensPowerPointParser - { placement: { relativeX: 78, relativeY: 0}, pos: "wr", tag: "" },
00:03:42.130 [main] DEBUG com.bytecubed.studio.parser.RavensPowerPointParser - { placement: { relativeX: 54, relativeY: 0}, pos: "wr", tag: "" },
00:03:42.130 [main] DEBUG com.bytecubed.studio.parser.RavensPowerPointParser - { placement: { relativeX: 11, relativeY: 0}, pos: "wr", tag: "84" },
         */

        new Formation(new PlayerMarker(new Placement(70,0), "center", "", true ));
        return null;
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
