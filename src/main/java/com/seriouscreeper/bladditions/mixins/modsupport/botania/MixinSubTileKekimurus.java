package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import com.codetaylor.mc.pyrotech.modules.tech.basic.tile.TileCampfire;
import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import com.tiviacz.pizzacraft.blocks.BlockPizza;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.block.subtile.generating.SubTileKekimurus;

import java.lang.reflect.Field;

@Mixin(value = SubTileKekimurus.class, remap = false)
public class MixinSubTileKekimurus extends SubTileGenerating {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onUpdate() {
        super.onUpdate();
        if (!this.supertile.getWorld().isRemote) {
            if (!this.supertile.getWorld().isRemote && this.ticksExisted % 80 == 0) {
                for(int i = 0; i < 11; ++i) {
                    for(int j = 0; j < 11; ++j) {
                        for(int k = 0; k < 11; ++k) {
                            BlockPos pos = this.supertile.getPos().add(i - 5, j - 5, k - 5);
                            IBlockState state = this.supertile.getWorld().getBlockState(pos);
                            Block block = state.getBlock();
                            if (block instanceof BlockPizza) {
                                Field fieldFoodstats;

                                try {
                                    fieldFoodstats = BlockPizza.class.getDeclaredField("foodstats");
                                    fieldFoodstats.setAccessible(true);
                                } catch (NoSuchFieldException e) {
                                    throw new RuntimeException(e);
                                }

                                int foodstats = 0;

                                try {
                                    foodstats = fieldFoodstats.getInt(block);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }

                                int mana = foodstats * ConfigBLAdditions.configBotania.ManaKekimurus;

                                if(this.getMaxMana() - this.mana < mana) {
                                    return;
                                }

                                int nextSlicesEaten = state.getValue(BlockPizza.BITES) + 1;
                                if (nextSlicesEaten >= 6) {
                                    this.supertile.getWorld().setBlockToAir(pos);
                                } else {
                                    this.supertile.getWorld().setBlockState(pos, state.withProperty(BlockPizza.BITES, nextSlicesEaten), 3);
                                }

                                this.supertile.getWorld().playEvent(2001, pos, Block.getStateId(state));
                                this.supertile.getWorld().playSound((EntityPlayer)null, this.supertile.getPos(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 1.0F, 0.5F + (float)Math.random() * 0.5F);
                                this.mana += mana;
                                this.sync();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getMaxMana() {
        return 20001;
    }
}
