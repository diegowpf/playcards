package com.bytecubed;

import com.bytecubed.models.Placement;
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
import java.util.ArrayList;
import java.util.List;

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
    private List<Player> players;
    private Logger logger = LoggerFactory.getLogger(PlayCardController.class);

    public PlayCardController(){
        players = new ArrayList<>();
    }

    @PostMapping()
    public HttpEntity<List<Player>> importCard(@RequestParam("file") MultipartFile file,
                                               RedirectAttributes redirectAttributes) throws IOException {
        XMLSlideShow ppt = new XMLSlideShow(file.getInputStream());
        logger.debug( "PowerPoint with slidecount:  "+ ppt.getSlides().size());

        this.players = new RavensPowerPointParser(ppt).extractPlayerPlacements();

        return ok(players);
    }

    @GetMapping()
    public HttpEntity<List<Player>> getPlayCards(){
        return ok(players);
    }

}
