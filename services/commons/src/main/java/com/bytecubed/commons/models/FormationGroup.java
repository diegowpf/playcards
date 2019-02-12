package com.bytecubed.commons.models;

import com.bytecubed.commons.Formation;

import java.util.List;

public class FormationGroup {
    public FormationGroup(List<Formation> formations) {
        this.formations = formations;
    }

    List<Formation> formations;

    public List<Formation> getFormations() {
        return formations;
    }
}
