package com.bytecubed.commons;

import com.bytecubed.commons.models.PlayDescription;
import org.springframework.data.annotation.Id;

import java.util.UUID;

public class Play {

    @Id
    private UUID id;
    private PlayDescription playDescription;

    public Play(UUID id, PlayDescription playDescription) {
        this.id = id;
        this.playDescription = playDescription;
    }

    public Play() {
    }

    public PlayDescription getPlayDescription() {
        return playDescription;
    }

    public UUID getId() {
        return id;
    }

    public Formation getFormation() {
        return null;
    }
}
