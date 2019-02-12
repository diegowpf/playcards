package com.bytecubed.studio.web;

import com.bytecubed.commons.FormationRenderer;
import com.bytecubed.commons.PlayCard;
import com.bytecubed.studio.persistence.PlayCardRepository;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.StringReader;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImageController {

    private PlayCardRepository repository;
    private FormationRenderer renderer;
    private Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    public ImageController(PlayCardRepository repository,
                           FormationRenderer renderer ){
        this.repository = repository;
        this.renderer = renderer;
    }

    @GetMapping("/playcards/{id}")
    public void getImageAsByteArray(HttpServletResponse response, @PathVariable UUID id ) {
        response.setContentType(MediaType.IMAGE_PNG_VALUE);

        PlayCard card = repository.findById(id).get();
        String svg = renderer.render(card.getFormation());

        try {
            TranscoderInput input = new TranscoderInput(new StringReader(svg));
            TranscoderOutput output_png_image = new TranscoderOutput(response.getOutputStream());
            PNGTranscoder my_converter = new PNGTranscoder();
            my_converter.transcode(input, output_png_image);
        } catch (Exception e) {
            logger.error("Error creating document", e );
        }
    }
}
