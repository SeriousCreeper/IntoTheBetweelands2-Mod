package com.seriouscreeper.bladditions.mixins.modsupport.pyrotech;

import com.codetaylor.mc.athenaeum.spi.BlockPartialBase;
import com.codetaylor.mc.pyrotech.modules.tech.basic.block.BlockKilnPit;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.registries.BlockRegistry;

@Mixin(value = BlockKilnPit.class, remap = false)
public class MixinBlockKilnPit extends BlockPartialBase {
    @Shadow
    public static final IProperty<BlockKilnPit.EnumType> VARIANT = PropertyEnum.create("variant", BlockKilnPit.EnumType.class);

    public MixinBlockKilnPit(Material material) {
        super(material);
    }


    /**
     * @author SC
     */
    @Overwrite
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        BlockKilnPit.EnumType type = (BlockKilnPit.EnumType)state.getValue(VARIANT);
        if (type == BlockKilnPit.EnumType.WOOD || type == BlockKilnPit.EnumType.THATCH) {
            drops.add(new ItemStack(BlockRegistry.THATCH, 1, 0));
        }

        super.getDrops(drops, world, pos, state, fortune);
    }
}
