package com.bytecubed.studio.web;

import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.studio.models.PlayCard;
import com.bytecubed.studio.parser.RavensPowerPointParser;
import com.bytecubed.studio.persistence.PlayCardRepository;
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
    private Logger logger = LoggerFactory.getLogger(PlayCardController.class);

    @Autowired
    public PlayCardController(PlayCardRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/team/{id}")
    public HttpEntity<Iterable<PlayerMarker>> importCard(@RequestParam("file") MultipartFile file,
                                                         RedirectAttributes redirectAttributes,
                                                         @PathVariable UUID id) throws IOException {
        XMLSlideShow ppt = new XMLSlideShow(file.getInputStream());
        PlayCard card = new PlayCard(id, new RavensPowerPointParser(ppt).extractPlayerPlacements());
        repository.save(card);

        return ok(card.getPlayerMarkers());
    }

    @GetMapping("/team/{id}")
    public HttpEntity<Iterable<PlayCard>> getPlayCards(@PathVariable UUID id) {
        return ok(repository.findAllByTeamId(id));
    }

    @GetMapping("/{id}")
    public HttpEntity<PlayCard> getPlayCard(@PathVariable UUID id) {
        return ok(repository.findById(id).get());
    }

    @GetMapping()
    public HttpEntity<Iterable<PlayCard>> getAll(){
        return ok(repository.findAll());
    }

}
