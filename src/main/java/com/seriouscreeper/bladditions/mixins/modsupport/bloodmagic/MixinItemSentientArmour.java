package com.seriouscreeper.bladditions.mixins.modsupport.bloodmagic;

import WayofTime.bloodmagic.item.armour.ItemSentientArmour;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ItemSentientArmour.class, remap = false)
public class MixinItemSentientArmour {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public double getArmourModifier(EnumDemonWillType type, int willBracket) {
        switch (type) {
            case STEADFAST:
                return ConfigBLAdditions.configBloodMagic.configSentientArmor.steadfastProtectionLevel[willBracket];
            default:
                return ConfigBLAdditions.configBloodMagic.configSentientArmor.extraProtectionLevel[willBracket];
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public double getKnockbackModifier(EnumDemonWillType type, int willBracket) {
        switch (type) {
            case STEADFAST:
                return ConfigBLAdditions.configBloodMagic.configSentientArmor.knockbackBonus[willBracket];
            default:
                return 0.0;
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public double getSpeedModifier(EnumDemonWillType type, int willBracket) {
        switch (type) {
            case VENGEFUL:
                return ConfigBLAdditions.configBloodMagic.configSentientArmor.speedBonus[willBracket];
            default:
                return 0.0;
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public double getDamageModifier(EnumDemonWillType type, int willBracket) {
        switch (type) {
            case DESTRUCTIVE:
                return ConfigBLAdditions.configBloodMagic.configSentientArmor.damageBoost[willBracket];
            default:
                return 0.0;
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public double getAttackSpeedModifier(EnumDemonWillType type, int willBracket) {
        switch (type) {
            case DESTRUCTIVE:
                return ConfigBLAdditions.configBloodMagic.configSentientArmor.attackSpeed[willBracket];
            default:
                return 0.0;
        }
    }
}
