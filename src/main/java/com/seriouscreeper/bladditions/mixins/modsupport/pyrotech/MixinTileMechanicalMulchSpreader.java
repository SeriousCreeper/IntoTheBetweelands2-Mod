package com.seriouscreeper.bladditions.mixins.modsupport.pyrotech;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.RandomHelper;
import com.codetaylor.mc.athenaeum.util.SoundHelper;
import com.codetaylor.mc.pyrotech.modules.core.ModuleCore;
import com.codetaylor.mc.pyrotech.modules.core.item.ItemMulch;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachine;
import com.codetaylor.mc.pyrotech.modules.tech.machine.ModuleTechMachineConfig;
import com.codetaylor.mc.pyrotech.modules.tech.machine.tile.TileMechanicalMulchSpreader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.block.farming.BlockGenericDugSoil;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.ItemRegistry;
import thebetweenlands.common.tile.TileEntityDugSoil;
import thecodex6824.thaumicaugmentation.api.ward.tile.IWardedInventory;

@Mixin(value = TileMechanicalMulchSpreader.class, remap = false)
public class MixinTileMechanicalMulchSpreader extends TileEntity {
    @Shadow
    private TileMechanicalMulchSpreader.MulchStackHandler mulchStackHandler;


    /**
     * @author SC
     */
    @Overwrite
    private boolean isValidMulch(ItemStack itemStack) {
        return itemStack.getItem() == ItemRegistry.ITEMS_MISC && itemStack.getItemDamage() == 2;
    }


    /**
     * @author SC
     */
    @Overwrite
    protected int doWork(ItemStack cog) {
        ItemStack mulchStack = this.mulchStackHandler.getStackInSlot(0);
        if (mulchStack.isEmpty()) {
            return 0;
        } else {
            SoundHelper.playSoundServer(this.world, this.pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, RandomHelper.random().nextFloat() * 0.2F + 0.8F);
            int[] cogData = this.getCogData(cog);
            int cogRange = cogData[0];
            int[] cogAttempts = new int[]{Math.min(cogData[1], mulchStack.getCount())};
            int[] placedMulch = new int[]{0};
            IBlockState blockState = this.world.getBlockState(this.pos);
            EnumFacing facing = ModuleTechMachine.Blocks.MECHANICAL_MULCH_SPREADER.getFacing(blockState);
            BlockPos origin = this.pos.offset(facing, cogRange + 1).down();
            BlockHelper.forBlocksInCubeShuffled(this.world, origin, cogRange, 0, cogRange, (w, p, bs) -> {
                int var10002;

                if (bs.getBlock() instanceof BlockGenericDugSoil) {
                    TileEntityDugSoil te = BlockGenericDugSoil.getTile(w, p);

                    if (te != null && !te.isComposted()) {
                        te.setCompost(30);

                        this.mulchStackHandler.extractItem(0, 1, false);

                        SoundHelper.playSoundServer(this.world, this.pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS);
                        var10002 = placedMulch[0]++;
                    }
                }

                var10002 = cogAttempts[0]--;
                return cogAttempts[0] > 0;
            });
            return ModuleTechMachineConfig.MECHANICAL_MULCH_SPREADER.COG_DAMAGE_TYPE == ModuleTechMachineConfig.MechanicalMulchSpreader.EnumCogDamageType.PerItem ? Math.max(1, placedMulch[0]) : 1;
        }
    }

    @Shadow
    private int[] getCogData(ItemStack cog) {
        return new int[0];
    }
}
