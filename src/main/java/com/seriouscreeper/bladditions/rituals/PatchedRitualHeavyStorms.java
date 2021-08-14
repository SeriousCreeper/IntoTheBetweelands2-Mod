package com.seriouscreeper.bladditions.rituals;

import epicsquid.roots.ritual.RitualHeavyStorms;

public class PatchedRitualHeavyStorms extends RitualHeavyStorms {
    public PatchedRitualHeavyStorms(String name, boolean disabled) {
        super(name, disabled);
        setEntityClass(PatchedEntityRitualHeavyStorms.class);
    }
}
