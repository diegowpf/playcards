package com.bytecubed.studio.web;

import com.bytecubed.commons.FormationRenderer;
import com.bytecubed.commons.PlayCard;
import com.bytecubed.studio.persistence.FormationRepository;
import com.bytecubed.studio.persistence.PlayCardRepository;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImageController {

    private PlayCardRepository playCardRepository;
    private FormationRepository formationRepository;
    private FormationRenderer renderer;
    private Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    public ImageController(PlayCardRepository playCardRepository,
                           FormationRepository formationRepository,
                           FormationRenderer renderer ){
        this.playCardRepository = playCardRepository;
        this.formationRepository = formationRepository;
        this.renderer = renderer;
    }

    @GetMapping("/formations/{id}")
    public void getFormationAsByArray(HttpServletResponse response, @PathVariable UUID id ){

        String svg = renderer.render(formationRepository.findById(id).get());
        writeImageToResponse(response, svg);
    }

    private void writeImageToResponse(HttpServletResponse response, String svg) {
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        try {
            TranscoderInput input = new TranscoderInput(new StringReader(svg));
            TranscoderOutput output_png_image = new TranscoderOutput(response.getOutputStream());

            JPEGTranscoder my_converter = new JPEGTranscoder();
            my_converter.transcode(input, output_png_image);
        } catch (Exception e) {
            logger.error("Error creating document", e );
        }
    }

    @GetMapping("/playcards/{id}")
    public void getImageAsByteArray(HttpServletResponse response, @PathVariable UUID id ) {

        PlayCard card = playCardRepository.findById(id).get();
        String svg = renderer.render(card.getFormation());

        writeImageToResponse(response, svg);
    }

    @GetMapping("/playcards/{id}/placements")
    public void getImageAsSvg(HttpServletResponse response, @PathVariable UUID id ) throws IOException {
        PlayCard card = playCardRepository.findById(id).get();
        String svg = renderer.render(card.getFormation(), false);

        writeImageToResponse(response, svg);
    }
}
