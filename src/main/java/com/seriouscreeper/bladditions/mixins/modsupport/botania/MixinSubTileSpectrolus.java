package com.seriouscreeper.bladditions.mixins.modsupport.botania;

import com.seriouscreeper.bladditions.config.ConfigBLAdditions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thebetweenlands.common.registries.BlockRegistry;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.block.subtile.generating.SubTileSpectrolus;

import java.util.Iterator;
import java.util.List;

@Mixin(value = SubTileSpectrolus.class, remap = false)
public class MixinSubTileSpectrolus extends SubTileGenerating {
    @Shadow
    int nextColor;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onUpdate() {
        super.onUpdate();
        if (!this.supertile.getWorld().isRemote) {
            Item wool = Item.getItemFromBlock(BlockRegistry.SAMITE);
            List<EntityItem> items = this.supertile.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(this.supertile.getPos().add(-1, -1, -1), this.supertile.getPos().add(2, 2, 2)));
            int slowdown = this.getSlowdownFactor();
            Iterator var4 = items.iterator();

            while(var4.hasNext()) {
                EntityItem item = (EntityItem)var4.next();
                ItemStack stack = item.getItem();
                if (!stack.isEmpty() && stack.getItem() == wool && !item.isDead && item.age >= slowdown) {
                    int meta = stack.getItemDamage();
                    if (meta == this.nextColor) {
                        this.mana = Math.min(this.getMaxMana(), this.mana + ConfigBLAdditions.configBotania.ManaSpectrolus);
                        this.nextColor = this.nextColor == 15 ? 0 : this.nextColor + 1;
                        this.sync();
                        ((WorldServer)this.supertile.getWorld()).spawnParticle(EnumParticleTypes.ITEM_CRACK, false, item.posX, item.posY, item.posZ, 20, 0.1, 0.1, 0.1, 0.05, new int[]{Item.getIdFromItem(stack.getItem()), stack.getItemDamage()});
                    }

                    item.setDead();
                }
            }

        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public void renderHUD(Minecraft mc, ScaledResolution res) {
        super.renderHUD(mc, res);
        ItemStack stack = new ItemStack(BlockRegistry.SAMITE, 1, this.nextColor);
        int color = this.getColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        if (!stack.isEmpty()) {
            String stackName = stack.getDisplayName();
            int width = 16 + mc.fontRenderer.getStringWidth(stackName) / 2;
            int x = res.getScaledWidth() / 2 - width;
            int y = res.getScaledHeight() / 2 + 30;
            mc.fontRenderer.drawStringWithShadow(stackName, (float)(x + 20), (float)(y + 5), color);
            RenderHelper.enableGUIStandardItemLighting();
            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
            RenderHelper.disableStandardItemLighting();
        }

        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
    }
}
