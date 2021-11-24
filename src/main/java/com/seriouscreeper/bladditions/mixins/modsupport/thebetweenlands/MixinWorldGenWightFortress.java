package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import scala.tools.asm.Opcodes;
import thebetweenlands.common.world.gen.feature.structure.WorldGenWightFortress;

import java.util.Random;

@Mixin(value = WorldGenWightFortress.class, remap = false)
public abstract class MixinWorldGenWightFortress {
    @Shadow private int direction;

    @Redirect(method = "generateStructure", at = @At(value = "FIELD", target = "Lthebetweenlands/common/world/gen/feature/structure/WorldGenWightFortress;direction:I", opcode = Opcodes.PUTFIELD))
    public void generateStructure(WorldGenWightFortress wightFortress, int direction) {
        this.direction = 0;
    }
}
