package com.seriouscreeper.bladditions.mixins.modsupport;

import com.rcx.mystgears.block.BlockTurret;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Iterator;

@Pseudo
@Mixin(BlockTurret.class)
public class MixinMystgears {
    @Shadow
    public static HashMap<Ingredient, String> metalTextures;


    /**
     * @author SC
     */
    @Overwrite(remap = false)
    public static String getMetalTexture(ItemStack metal) {
        Iterator var1 = metalTextures.keySet().iterator();
        int numsToSkip = 0;

        Ingredient ingredient = new OreIngredient("ingotSyrmorite");
        do {
            if (!var1.hasNext()) {
                return "syrmorite";
            }

            if(numsToSkip >= 2) {
                ingredient = (Ingredient) var1.next();
            }

            numsToSkip++;
        } while(!ingredient.apply(metal));

        return (String)metalTextures.get(ingredient);
    }
}
