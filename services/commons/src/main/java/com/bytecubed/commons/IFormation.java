package com.bytecubed.commons;

import com.bytecubed.commons.models.PlayerMarker;

public class IFormation extends Formation{

    public IFormation(){
        super(new FormationFactory()
                .withStandardTemplateInTheCenter()
                .getPlayerMarkers().toArray(new PlayerMarker[0]));

        this.andQbUnderCenter()
                .andXIsOnLeftOffTheBallOutsideTheNumbers()
                .andYIsOnTheRightLinedUpWithQBOutSideTheNumbers()
                .andFullBackBehindQB()
                .andHalfBackBehindFullBack()
                .addTightEndOnTheBallOnTheRight();
    }

}
