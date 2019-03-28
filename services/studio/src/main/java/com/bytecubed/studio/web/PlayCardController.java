package com.bytecubed.studio.web;

import com.bytecubed.commons.PlayCard;
import com.bytecubed.studio.parser.RavensPowerPointParser;
import com.bytecubed.studio.parser.routes.BasicRouteStrategy;
import com.bytecubed.studio.persistence.PlayCardRepository;
import com.bytecubed.studio.persistence.RouteRepository;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/playcards")
public class PlayCardController {
    private PlayCardRepository repository;
    private RouteRepository routeRepository;
    private Logger logger = LoggerFactory.getLogger(PlayCardController.class);

    @Autowired
    public PlayCardController(PlayCardRepository repository, RouteRepository routeRepository) {
        this.repository = repository;
        this.routeRepository = routeRepository;
    }

    @PostMapping("/team/import/{id}")
    public HttpEntity<Iterable<PlayCard>> importCard(@RequestParam("file") MultipartFile file,
                                                     RedirectAttributes redirectAttributes,
                                                     @PathVariable UUID id) throws IOException {
        return importOffensiveCard(file, redirectAttributes, id);
    }

    @PostMapping("/team/{id}")
    public HttpEntity<PlayCard> add(@PathVariable UUID id, @RequestBody PlayCard playCard) {
        playCard.setTeamId(id);
        playCard.setId(randomUUID());

        logger.debug("PlayCard:  " + playCard.toString());

        repository.save(playCard);
        return ok(playCard);
    }

    @PostMapping("/team/import/offense/{teamId}")
    public HttpEntity<Iterable<PlayCard>> importOffensiveCard(@RequestParam("file") MultipartFile file,
                                                              RedirectAttributes redirectAttributes,
                                                              @PathVariable UUID teamId) throws IOException {
        XMLSlideShow ppt = new XMLSlideShow(file.getInputStream());
        RavensPowerPointParser ravensPowerPointParser = new RavensPowerPointParser(ppt, new BasicRouteStrategy());
        List<PlayCard> playCards = ravensPowerPointParser.extractPlayCards();

        playCards.forEach(repository::save);
        playCards.forEach(p -> p.extractRoutes().forEach(
                r -> logger.debug("Saving route:  " + r)
        ));
        playCards.forEach(p -> p.extractRoutes().stream()
                .filter(Objects::nonNull)
                .forEach(routeRepository::save));

        return ok(playCards);
    }

    @GetMapping("/team/{id}")
    public HttpEntity<Iterable<PlayCard>> getPlayCards(@PathVariable UUID id) {
        return ok(repository.findAllByTeamId(id));
    }

    @GetMapping("/{id}")
    public HttpEntity<PlayCard> getPlayCard(@PathVariable UUID id) {
        return ok(repository.findById(id).get());
    }

    @GetMapping("/{id}/svg")
    public HttpEntity getPlayCardAsSvg(@PathVariable UUID id) {

        return null;
    }

    @GetMapping()
    public HttpEntity<Iterable<PlayCard>> getAll() {
        return ok(repository.findAll());
    }

    @GetMapping("/data/lastmodified")
    public HttpEntity<LocalDateTime> getLastModified() {
        LocalDateTime dateTime = LocalDateTime.now();
        for (PlayCard playCard : repository.findAll()) {
            if (dateTime.isAfter(playCard.getCreateTime())) {
                dateTime = playCard.getCreateTime();
            }
        }

        return ok(dateTime);
    }

    @DeleteMapping()
    public HttpEntity reset() {
        repository.deleteAll();

        return ok().build();
    }

}
