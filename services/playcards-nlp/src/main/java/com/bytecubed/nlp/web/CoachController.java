package com.bytecubed.nlp.web;

import com.bytecubed.commons.Formation;
import com.bytecubed.commons.Play;
import com.bytecubed.commons.PlayCard;
import com.bytecubed.commons.models.PlayDescription;
import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.commons.FormationFactory;
import com.bytecubed.commons.models.movement.CustomRoute;
import com.bytecubed.commons.models.movement.Route;
import com.bytecubed.nlp.models.PlayCardCommand;
import com.bytecubed.nlp.models.PlayCardInstruction;
import com.bytecubed.nlp.parsing.InstructionParser;
import com.bytecubed.nlp.parsing.models.RouteCommand;
import com.bytecubed.nlp.repository.FormationRepository;
import com.bytecubed.nlp.repository.PlayRepository;
import com.bytecubed.nlp.repository.RouteRepository;
import com.bytecubed.nlp.service.PlayCardBuilderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/coach")
public class CoachController {

    private PlayRepository repository;
    private InstructionParser parser;
    private FormationRepository formationRepository;
    private RouteRepository routeRepository;
    private Logger logger = LoggerFactory.getLogger(CoachController.class);

    @Autowired
    public CoachController(PlayRepository playRepository,
                           InstructionParser parser,
                           FormationRepository formationRepository,
                           RouteRepository routeRepository) {
        this.repository = playRepository;
        this.parser = parser;
        this.formationRepository = formationRepository;
        this.routeRepository = routeRepository;
    }

    @PostMapping("/play")
    public HttpEntity<UUID> createPlay(@RequestBody PlayDescription playDescription) {
        Play play = new Play(randomUUID(), playDescription);
        repository.save(play);
        return ok(play.getId());
    }

    @PostMapping("/route/{id}/name")
    public HttpEntity updateName(@PathVariable UUID id, @RequestBody String name) {
        CustomRoute route = routeRepository.findById(id).get();
        route.setName(name);
        routeRepository.save(route);

        logger.debug("About to save route:  " + id.toString());
        return ok().build();
    }

    @GetMapping("/play/{id}")
    public HttpEntity<Play> getPlay(@PathVariable UUID id) {
        return ok(repository.findById(id).orElseThrow(NoSuchElementException::new));
    }

    @PostMapping("/play/{id}")
    public HttpEntity addMove(@RequestBody String commandAsString, @PathVariable UUID id) {
        Play play = repository.findById(id).get();
        parser.parse(commandAsString);

        return null;
    }

    @GetMapping("/formation/{name}")
    public HttpEntity<Iterable<PlayerMarker>> getFormationByName(@PathVariable String name) {
        return ok(new FormationFactory()
                .withStandardTemplateInTheCenter()
                .andQbUnderCenter()
                .andXIsOnLeftOffTheBallOutsideTheNumbers()
                .andYIsOnTheRightLinedUpWithQBOutSideTheNumbers()
                .andFullBackBehindQB()
                .andHalfBackBehindFullBack()
                .addTightEndOnTheBallOnTheRight().getPlayerMarkers());
    }

    @GetMapping("/formations")
    public HttpEntity<List<Formation>> getAllFormations() {
        return ok(formationRepository.findAll());
    }

    @PostMapping("/formation")
    public HttpEntity addFormation(@RequestBody Formation formation) {
        logger.debug("Formation added: " + formation.getName());
        Formation target = new Formation(randomUUID(), formation);
        formationRepository.save(target);

        logger.debug("saving fomration:  " + target.toString());
        return ok().build();
    }

    @PostMapping("/playcards/team/{id}/formation/{name}")
    public HttpEntity<PlayCard> add(@PathVariable UUID id, @PathVariable String name) {

        return null;
    }

    @PostMapping("/playcards/script")
    public HttpEntity<PlayCard> postScript(@RequestBody PlayCardInstruction instruction) {
        logger.debug("Formation ID:  " + instruction.getFormationId().toString());
        logger.debug("Route Count:  " + instruction.getRoutes().size());

        PlayCardBuilderService service = new PlayCardBuilderService(formationRepository, routeRepository);
        return ok(service.buildFrom(instruction));
    }

    @PostMapping("/playcards/text")
    public HttpEntity<PlayCard> postCommands(@RequestBody PlayCardCommand command) {
        List<RouteCommand> routeCommands = parser.getRouteCommands(command.getVoiceCommands());
        PlayCardInstruction instruction = new PlayCardInstruction(command.getFormationId());

        routeCommands.forEach(c -> {
            Optional<CustomRoute> routeSearchResult = routeRepository.findByName(c.getRouteName());
            routeSearchResult.ifPresent(route -> {
                instruction.addRoute(c.getPlayerTag(), route);
            });

        });

        return postScript(instruction);
    }

    @PostMapping("/routes")
    public HttpEntity<Route> addRoute(@RequestBody CustomRoute route) {
        UUID routeId = randomUUID();
        CustomRoute generatedRoute = new CustomRoute(routeId, "straight", route);
        routeRepository.save(generatedRoute);

        return ok(generatedRoute);
    }

    @GetMapping("/routes")
    public HttpEntity<List<CustomRoute>> getRoutes(@RequestParam(value = "named", defaultValue = "true" ) boolean named) {
        if (!named) {
            return ok(routeRepository.findAll());
        } else {
            return ok(routeRepository.findAll().stream()
                    .filter(f -> f.getName() != null)
                    .collect(toList()));
        }
    }


}
