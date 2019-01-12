package com.bytecubed.nlp.design;

import com.bytecubed.commons.Formation;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class FormationFactoryTest {


    @Test
    public void shouldProvideStandardIFormation(){
        Formation formation = new FormationFactory()
                .withStandardTemplateInTheCenter()
                .andQbUnderCenter()
                .andXIsOnLeftOffTheBallOutsideTheNumbers()
                .andYIsOnTheRightLinedUpWithQBOutSideTheNumbers()
                .andFullBackBehindQB()
                .andHalfBackBehindFullBack()
                .addTightEndOnTheBallOnTheRight();

        assertThat(formation.getPLayerMarkers().size()).isEqualTo(11);

    }

}