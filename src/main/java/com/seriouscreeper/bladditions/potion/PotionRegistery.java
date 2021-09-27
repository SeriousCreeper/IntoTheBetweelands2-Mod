package com.seriouscreeper.bladditions.potion;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class PotionRegistery {
    public static PotionThaumcraftResearch PotionResearchAlchemy;
    public static PotionThaumcraftResearch PotionResearchArtifice;
    public static PotionThaumcraftResearch PotionResearchAuromancy;
    public static PotionThaumcraftResearch PotionResearchGolemancy;
    public static PotionThaumcraftResearch PotionResearchEldritch;
    public static PotionThaumcraftResearch PotionResearchArcane;

    public static void RegisterPotions() {
        PotionResearchAlchemy = new PotionThaumcraftResearch(PotionThaumcraftResearch.RESEARCH_CATEGORY.ALCHEMY, "tc_alchemy");
        PotionResearchArtifice = new PotionThaumcraftResearch(PotionThaumcraftResearch.RESEARCH_CATEGORY.ARTIFICE, "tc_artifice");
        PotionResearchAuromancy = new PotionThaumcraftResearch(PotionThaumcraftResearch.RESEARCH_CATEGORY.AUROMANCY, "tc_auromancy");
        PotionResearchGolemancy = new PotionThaumcraftResearch(PotionThaumcraftResearch.RESEARCH_CATEGORY.GOLEMANCY, "tc_golemancy");
        PotionResearchEldritch = new PotionThaumcraftResearch(PotionThaumcraftResearch.RESEARCH_CATEGORY.ELDRITCH, "tc_eldritch");
        PotionResearchArcane = new PotionThaumcraftResearch(PotionThaumcraftResearch.RESEARCH_CATEGORY.ARCANE, "tc_arcane");

        register(PotionResearchAlchemy);
        register(PotionResearchArtifice);
        register(PotionResearchAuromancy);
        register(PotionResearchGolemancy);
        register(PotionResearchEldritch);
        register(PotionResearchArcane);
    }

    private static void register(Potion potion) {
        ForgeRegistries.POTIONS.register(potion);
    }
}
