package com.bytecubed.nlp.models;

import java.util.UUID;

public class PlayCardCommand {

    String voiceCommands;
    UUID formationId;

    public PlayCardCommand() {
    }

    public PlayCardCommand( UUID formationId, String voiceCommands) {
        this.voiceCommands = voiceCommands;
        this.formationId = formationId;
    }

    public String getVoiceCommands() {
        return voiceCommands;
    }

    public UUID getFormationId() {
        return formationId;
    }
}
