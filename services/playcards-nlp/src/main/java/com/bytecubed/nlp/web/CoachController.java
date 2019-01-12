package com.bytecubed.nlp.web;

import com.bytecubed.commons.Play;
import com.bytecubed.commons.models.PlayDescription;
import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.nlp.design.FormationFactory;
import com.bytecubed.nlp.models.PlayerMovementDescription;
import com.bytecubed.nlp.parsing.InstructionParser;
import com.bytecubed.nlp.repository.PlayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping( "/coach")
public class CoachController {

    private PlayRepository repository;
    private InstructionParser parser;

    @Autowired
    public CoachController(PlayRepository repository, InstructionParser parser ){
        this.repository = repository;
        this.parser = parser;
    }

    @PostMapping("/play" )
    public HttpEntity<UUID> createPlay(@RequestBody PlayDescription playDescription){
        Play play = new Play(randomUUID(), playDescription);
        repository.save(play);
        return ok(play.getId());
    }

    @GetMapping("/play/{id}")
    public HttpEntity<Play> getPlay(@PathVariable UUID id ){
        return ok(repository.findById(id).orElseThrow(NoSuchElementException::new));
    }

    @PostMapping("/play/{id}")
    public HttpEntity addMove(@RequestBody String commandAsString, @PathVariable UUID id){
        Play play = repository.findById(id).get();
        parser.parse(commandAsString, play.getFormation() );

        return null;
    }

    @GetMapping("/formation/{name}")
    public HttpEntity<Iterable<PlayerMarker>> getFormationByName(@PathVariable String name ){
        return ok(new FormationFactory()
                .withStandardTemplateInTheCenter()
                .andQbUnderCenter()
                .andXIsOnLeftOffTheBallOutsideTheNumbers()
                .andYIsOnTheRightLinedUpWithQBOutSideTheNumbers()
                .andFullBackBehindQB()
                .andHalfBackBehindFullBack()
                .addTightEndOnTheBallOnTheRight().getPLayerMarkers());
    }

}
