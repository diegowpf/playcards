package com.bytecubed;

import com.bytecubed.commons.PlayCard;
import com.bytecubed.studio.persistence.PlayCardRepository;
import com.bytecubed.studio.web.PlayCardController;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayCardControllerTest {

    private UUID teamId;

    @Test
    @Ignore
    public void shouldReturnAllPlayersBasedOnPlayCardId(){
        PlayCardRepository repository = mock(PlayCardRepository.class);
        teamId = UUID.randomUUID();
        PlayCard playCard = null;//new PlayCardController.PlayCard(teamId, null);
        when(repository.findAll()).thenReturn( asList(playCard));

        PlayCardController controller = new PlayCardController(repository);
        assertThat(controller.getPlayCards(teamId).getBody().iterator().next()).isEqualTo(playCard);
    }

    @Test
    @Ignore
    public void shouldLoadAllRecords(){

    }


    @Test
    public void shouldGenerateAnSVGOfAGivenImage() throws IOException {

        Image image = ImageIO.read(new File("/Users/carlyledavis/Projects/mr-platform/playcards/client/public/images/ravens-30-no-logo.png"));
        SVGGraphics2D graphics = new SVGGraphics2D(1443,767);
        graphics.drawImage(image, 0,0,null);
//        graphics.drawImage( new BufferedImage(), 0,0);
        Shape circle = new Ellipse2D.Double(100, 100, 100, 100);


        PrintWriter fos = new PrintWriter("/Users/carlyledavis/Desktop/image.svg");

        graphics.setColor(Color.red);
        graphics.fill(circle);
        System.out.println( graphics.getSVGDocument() );
        fos.println(graphics.getSVGDocument());
        fos.flush();
    }
}