package com.seriouscreeper.bladditions.rituals;

import epicsquid.roots.ritual.RitualFrostLands;

public class PatchedRitualFrostLands extends RitualFrostLands {
    public PatchedRitualFrostLands(String name, boolean disabled) {
        super(name, disabled);
        setEntityClass(PatchedEntityRitualFrostLands.class);
    }
}
