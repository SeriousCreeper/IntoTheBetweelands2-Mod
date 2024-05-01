package com.seriouscreeper.bladditions.mixins.modsupport.thaumcraft;

import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.oredict.OreIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.internal.WeightedRandomLoot;
import thaumcraft.api.items.ItemGenericEssentiaContainer;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.blocks.basic.BlockStonePorous;
import thaumcraft.common.config.ModConfig;
import thebetweenlands.common.item.misc.ItemMisc;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.ItemRegistry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Mixin(value = BlockStonePorous.class, remap = false)
public class MixinBlockStonePorous {
    @Shadow
    static ArrayList<WeightedRandomLoot> pdrops = null;
    @Shadow
    static Random r = new Random(System.currentTimeMillis());


    /**
     * @author SC
     */
    @Overwrite
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> ret = new ArrayList();
        int rr = r.nextInt(15) + fortune;
        if (rr > 11) {
            if (pdrops == null || pdrops.size() <= 0) {
                this.createDrops();
            }

            ItemStack s = ((WeightedRandomLoot) WeightedRandom.getRandomItem(r, pdrops)).item.copy();
            ret.add(s);
        } else {
            ret.add(ItemStack.EMPTY);
        }

        return ret;
    }


    /**
     * @author SC
     */
    @Overwrite
    private void createDrops() {
        pdrops = new ArrayList();
        Iterator var1 = Aspect.getCompoundAspects().iterator();

        pdrops.add(new WeightedRandomLoot(new ItemStack(BlockRegistry.SILT), 40));
        pdrops.add(new WeightedRandomLoot(ItemMisc.EnumItemMisc.BETWEENSTONE_PEBBLE.create(1), 30));
        pdrops.add(new WeightedRandomLoot(ItemMisc.EnumItemMisc.WEEDWOOD_STICK.create(1), 30));
        pdrops.add(new WeightedRandomLoot(new ItemStack(ItemRegistry.DENTROTHYST_SHARD_GREEN), 20));
        pdrops.add(new WeightedRandomLoot(new ItemStack(ItemRegistry.DENTROTHYST_SHARD_ORANGE), 15));

        while(var1.hasNext()) {
            Aspect aspect = (Aspect)var1.next();
            ItemStack is = new ItemStack(ItemsTC.crystalEssence);
            ((ItemGenericEssentiaContainer)ItemsTC.crystalEssence).setAspects(is, (new AspectList()).add(aspect, aspect == Aspect.FLUX ? 100 : (aspect.isPrimal() ? 20 : 1)));
            pdrops.add(new WeightedRandomLoot(is, 1));
        }

        pdrops.add(new WeightedRandomLoot(new ItemStack(ItemsTC.amber), 20));
        pdrops.add(new WeightedRandomLoot(CommonProxy.CreateItemStackFromOreDictionary("clusterSyrmorite"), 20));
        pdrops.add(new WeightedRandomLoot(CommonProxy.CreateItemStackFromOreDictionary("clusterOctine"), 14));
        //pdrops.add(new WeightedRandomLoot(CommonProxy.CreateItemStackFromOreDictionary("clusterNickel"), 10));
        //pdrops.add(new WeightedRandomLoot(CommonProxy.CreateItemStackFromOreDictionary("clusterAluminium"), 10));
        //pdrops.add(new WeightedRandomLoot(new ItemStack(ItemsTC.clusters, 1, 2), 10)); // copper
        pdrops.add(new WeightedRandomLoot(new ItemStack(ItemsTC.clusters, 1, 5), 10)); // lead
        pdrops.add(new WeightedRandomLoot(new ItemStack(ItemsTC.clusters, 1, 4), 10)); // silver
        pdrops.add(new WeightedRandomLoot(new ItemStack(ItemsTC.clusters, 1, 6), 8)); // cinnabar


        /*
        pdrops.add(new WeightedRandomLoot(new ItemStack(Items.DIAMOND), 2));
        pdrops.add(new WeightedRandomLoot(new ItemStack(Items.EMERALD), 4));
        pdrops.add(new WeightedRandomLoot(new ItemStack(Items.REDSTONE), 8));
        pdrops.add(new WeightedRandomLoot(new ItemStack(Items.PRISMARINE_CRYSTALS), 3));
        pdrops.add(new WeightedRandomLoot(new ItemStack(Items.PRISMARINE_SHARD), 3));
        pdrops.add(new WeightedRandomLoot(new ItemStack(Items.CLAY_BALL), 30));
        pdrops.add(new WeightedRandomLoot(new ItemStack(Items.QUARTZ), 15));
         */
    }
}
