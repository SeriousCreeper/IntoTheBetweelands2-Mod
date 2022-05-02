package com.seriouscreeper.bladditions.mixins.modsupport.waterstrainer;

import mods.waterstrainer.util.APIUtils;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

@Mixin(value = APIUtils.class, remap = false)
public class MixinAPIUtils {
    private static final String[] VALID_WATER_BLOCKS = new String[]{"minecraft:water", "minecraft:flowing_water", "tfc:fluid/fresh_water", "tfc:fluid/hot_water", "tfc:fluid/salt_water", "thebetweenlands:swamp_water"};

    /**
     * @author SC
     */
    @Overwrite
    public static boolean isWaterBlock(Block block) {
        boolean var1;
        if (block != null && block.getRegistryName() != null) {
            Stream var10000 = Arrays.stream(VALID_WATER_BLOCKS);
            String var10001 = block.getRegistryName().toString();
            Objects.requireNonNull(var10001);
            if (var10000.anyMatch(var10001::equals)) {
                var1 = true;
                return var1;
            }
        }

        var1 = false;
        return var1;
    }
}
