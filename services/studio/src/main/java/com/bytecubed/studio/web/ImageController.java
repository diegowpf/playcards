package com.bytecubed.studio.web;

import com.bytecubed.commons.FormationRenderer;
import com.bytecubed.commons.PlayCard;
import com.bytecubed.studio.persistence.PlayCardRepository;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImageController {

    private ServletContext servletContext;
    private PlayCardRepository repository;
    private Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    public ImageController(ServletContext servletContext, PlayCardRepository repository ){
        this.servletContext = servletContext;
        this.repository = repository;
    }

    @GetMapping("/playcards/{id}")
    public void getImageAsByteArray(HttpServletResponse response, @PathVariable UUID id ) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//        InputStream in = classloader.getResourceAsStream("images/ravens-30-no-logo.png");
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
//        IOUtils.copy(in, response.getOutputStream());

        PlayCard card = repository.findById(id).get();

        FormationRenderer renderer = new FormationRenderer();
        String svg = renderer.render(card.getFormation());

        try {
//            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(svg);
            TranscoderInput input = new TranscoderInput(new StringReader(svg));
            TranscoderOutput output_png_image = new TranscoderOutput(response.getOutputStream());
            // Step-3: Create PNGTranscoder and define hints if required
            PNGTranscoder my_converter = new PNGTranscoder();
            // Step-4: Convert and Write output
            my_converter.transcode(input, output_png_image);
        } catch (Exception e) {
            logger.error("Error creating document", e );
        }
    }
}
