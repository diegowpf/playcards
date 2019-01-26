package com.bytecubed.studio.parser;

import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PositionDetectorTest {

    @Test
    @Ignore
    public void shouldReturnPositionGuessForWR(){
        assertThat(PositionDetector.getPosition(generatePlayer("81"))).isEqualTo("wr");
        assertThat(PositionDetector.getPosition(generatePlayer("82"))).isEqualTo("wr");
        assertThat(PositionDetector.getPosition(generatePlayer("83"))).isEqualTo("wr");
        assertThat(PositionDetector.getPosition(generatePlayer("84"))).isEqualTo("wr");
        assertThat(PositionDetector.getPosition(generatePlayer("85"))).isEqualTo("wr");
    }

    private XSLFAutoShape generatePlayer(String numberAsStr ) {
        XSLFAutoShape shape = mock(XSLFAutoShape.class);
        when(shape.getText()).thenReturn(numberAsStr);
        return shape;
    }

    @Test
    @Ignore
    public void shouldReturnPositionGuessForFB(){
        assertThat(PositionDetector.getPosition(generatePlayer("24"))).isEqualTo("fb");
        assertThat(PositionDetector.getPosition(generatePlayer("25"))).isEqualTo("fb");
        assertThat(PositionDetector.getPosition(generatePlayer("26"))).isEqualTo("fb");
    }

    @Test
    @Ignore
    public void shouldReturnPositionGuessForQB(){
        assertThat(PositionDetector.getPosition(generatePlayer("1"))).isEqualTo("fb");
        assertThat(PositionDetector.getPosition(generatePlayer("2"))).isEqualTo("fb");
        assertThat(PositionDetector.getPosition(generatePlayer("3"))).isEqualTo("fb");
    }

}