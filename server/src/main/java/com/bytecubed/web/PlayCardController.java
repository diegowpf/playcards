package com.bytecubed.web;

import com.bytecubed.models.PlayCard;
import com.bytecubed.parser.Player;
import com.bytecubed.parser.RavensPowerPointParser;
import com.bytecubed.persistence.PlayCardRepository;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/playcards")
public class PlayCardController {
    private PlayCardRepository repository;

    @Autowired
    public PlayCardController(PlayCardRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/card")
    public HttpEntity getPlayCard(){
        return ok("Test");
    }
    private Logger logger = LoggerFactory.getLogger(PlayCardController.class);


    @PostMapping()
    public HttpEntity<Iterable<Player>> importCard(@RequestParam("file") MultipartFile file,
                                               RedirectAttributes redirectAttributes) throws IOException {
        XMLSlideShow ppt = new XMLSlideShow(file.getInputStream());
        logger.debug( "PowerPoint with slidecount:  "+ ppt.getSlides().size());

        PlayCard card = new PlayCard(new RavensPowerPointParser(ppt).extractPlayerPlacements());
        repository.save(card);

        return ok(card.getPlayers());
    }

    @GetMapping()
    public HttpEntity<Iterable<PlayCard>> getPlayCards(){
        return ok(repository.getPlayCards());
    }

    @GetMapping("/{id}")
    public HttpEntity<Iterable<Player>> getPlayCard(@PathVariable  UUID id){
        return ok(repository.getPlayCards().iterator().next().getPlayers());
    }

}
