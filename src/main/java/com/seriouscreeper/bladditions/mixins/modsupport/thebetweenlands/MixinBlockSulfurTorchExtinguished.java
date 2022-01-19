package com.seriouscreeper.bladditions.mixins.modsupport.thebetweenlands;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thebetweenlands.common.block.misc.BlockSulfurTorch;
import thebetweenlands.common.block.misc.BlockSulfurTorchExtinguished;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.ItemRegistry;

@Mixin(value = BlockSulfurTorchExtinguished.class, remap = false)
public class MixinBlockSulfurTorchExtinguished extends BlockSulfurTorch {
    /**
     * @author SC
     */
    @Overwrite
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack held = playerIn.getHeldItem(hand);
        if (!held.isEmpty() && (held.getItem() == ItemRegistry.OCTINE_INGOT || held.getItem() == Item.getItemFromBlock(BlockRegistry.SULFUR_TORCH))) {
            if (!worldIn.isRemote) {
                worldIn.setBlockState(pos, BlockRegistry.SULFUR_TORCH.getDefaultState().withProperty(FACING, state.getValue(FACING)));
                worldIn.playSound((EntityPlayer)null, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), held.getItem() == ItemRegistry.OCTINE_INGOT ? SoundEvents.ITEM_FLINTANDSTEEL_USE : SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }

            return true;
        } else {
            return false;
        }
    }
}
