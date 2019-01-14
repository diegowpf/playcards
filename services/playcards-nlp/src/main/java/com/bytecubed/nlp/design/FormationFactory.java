package com.bytecubed.nlp.design;

import com.bytecubed.commons.Formation;
import com.bytecubed.commons.models.Placement;
import com.bytecubed.commons.models.PlayerMarker;
import org.springframework.stereotype.Component;

@Component
public class FormationFactory {

    private static final int HASH_SPACING = 10;

    public Formation withStandardTemplateInTheCenter(){
        return getStandardTemplate(HashMarking.center);
    }

    public Formation getStandardTemplate(HashMarking hash ){

        switch(hash) {
            case left:
                return new Formation(
                        new PlayerMarker(new Placement(64 - HASH_SPACING, 0), "lg", null),
                        new PlayerMarker(new Placement(72 - HASH_SPACING, 0), "lt", null),
                        new PlayerMarker(new Placement(80 - HASH_SPACING, 0), "C", null, true),
                        new PlayerMarker(new Placement(88 - HASH_SPACING, 0), "rt", null),
                        new PlayerMarker(new Placement(96 - HASH_SPACING, 0), "rg", null)
                );
            case right:
                return new Formation(
                        new PlayerMarker(new Placement(64 + HASH_SPACING, 0), "lg", null),
                        new PlayerMarker(new Placement(72 + HASH_SPACING, 0), "lt", null),
                        new PlayerMarker(new Placement(80 + HASH_SPACING, 0), "C", null, true),
                        new PlayerMarker(new Placement(88 + HASH_SPACING, 0), "rt", null),
                        new PlayerMarker(new Placement(96 + HASH_SPACING, 0), "rg", null)
                );
        }

        return new Formation(
                new PlayerMarker(new Placement(64, 0), "lg", null),
                new PlayerMarker(new Placement(72, 0), "lt", null),
                new PlayerMarker(new Placement(80, 0), "C", null, true),
                new PlayerMarker(new Placement(88, 0), "rt", null),
                new PlayerMarker(new Placement(96, 0), "rg", null)
        );
    }

    public Formation standardTemplateFromLeftHash(){
        return getStandardTemplate(HashMarking.left);
    }

    public Formation standardTemplateFromRightHash(){
        return getStandardTemplate(HashMarking.right);
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

    public enum HashMarking{
        center,
        left,
        right
    }
}
