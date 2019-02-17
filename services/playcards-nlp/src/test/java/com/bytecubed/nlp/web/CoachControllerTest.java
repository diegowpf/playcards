package com.bytecubed.nlp.web;

import com.bytecubed.commons.PlayCard;
import com.bytecubed.nlp.models.PlayCardInstruction;
import com.bytecubed.nlp.parsing.InstructionParser;
import com.bytecubed.nlp.repository.FormationRepository;
import com.bytecubed.nlp.repository.PlayRepository;
import com.bytecubed.nlp.repository.RouteRepository;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class CoachControllerTest {

    private PlayRepository playRepository;
    private InstructionParser instructionParser;
    private FormationRepository formationRepository;

    @Before
    public void setup(){
        playRepository = mock(PlayRepository.class);
        instructionParser = mock(InstructionParser.class);
        formationRepository = mock(FormationRepository.class);
    }

    @Test
    public void shouldReturnNewPlayCardBasedOnOnePlayerOneRoute(){
        CoachController controller = new CoachController(
                playRepository,
                instructionParser,
                formationRepository,
                mock(RouteRepository.class));
//        PlayCardInstruction instruction = new PlayCardInstruction();
//        PlayCard card = controller.postScript(instruction).getBody();
    }
}