package com.seriouscreeper.bladditions.items;

import com.blamejared.ctgui.reference.Reference;
import com.seriouscreeper.bladditions.BLAdditions;
import com.seriouscreeper.bladditions.compat.arcaneworld.TeleporterDungeonCustom;
import com.seriouscreeper.bladditions.libs.AdminExecute;
import com.seriouscreeper.bladditions.libs.CustomTeleporter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.arcaneworld.config.ArcaneWorldConfig;
import party.lemons.arcaneworld.gen.dungeon.dimension.TeleporterDungeon;
import party.lemons.arcaneworld.util.capabilities.IRitualCoordinate;
import party.lemons.arcaneworld.util.capabilities.RitualCoordinateProvider;
import thebetweenlands.client.handler.ItemTooltipHandler;
import thebetweenlands.common.TheBetweenlands;
import thebetweenlands.common.config.BetweenlandsConfig;
import thebetweenlands.common.handler.PlayerRespawnHandler;
import thebetweenlands.common.item.misc.ItemBoneWayfinder;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.world.teleporter.TeleporterHandler;
import thebetweenlands.util.PlayerUtil;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ItemCorruptedBoneWayfinder extends ItemBoneWayfinder {
    public ItemCorruptedBoneWayfinder() {
        setRegistryName("corrupted_bone_wayfinder");
        setTranslationKey(BLAdditions.MODID + ".corrupted_bone_wayfinder");
        this.setMaxDamage(1);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.isSneaking()) {
            player.setActiveHand(hand);
            return new ActionResult(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult(EnumActionResult.PASS, stack);
    }

    public int getDimension(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            assert nbt != null;
            if (nbt.hasKey("dimension")) {
                return nbt.getInteger("dimension");
            }
        }

        return 20;
    }


    public boolean isDungeon(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound() != null && stack.getTagCompound().hasKey("dungeon_id")) {
            return true;
        }

        return false;
    }


    public String getDungeonId(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound() != null && stack.getTagCompound().hasKey("dungeon_id")) {
            return stack.getTagCompound().getString("dungeon_id");
        }

        return "";
    }

    public BlockPos getTeleportPos(ItemStack stack) {
        int x = 0, y = 0, z = 0;

        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            assert nbt != null;
            if (nbt.hasKey("x")) {
                x = nbt.getInteger("x");
            }
            if (nbt.hasKey("y")) {
                y = nbt.getInteger("y");
            }
            if (nbt.hasKey("z")) {
                z = nbt.getInteger("z");
            }
        }

        return new BlockPos(x, y, z);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entity) {
        if (!worldIn.isRemote && stack.getItemDamage() < stack.getMaxDamage()) {
            //BlockPos waystone = this.getTeleportPos(stack);

            if(entity.isRiding())
                entity.dismountRidingEntity();

            this.playThunderSounds(worldIn, entity.posX, entity.posY, entity.posZ);

            if(isDungeon(stack)) {
                WorldServer ws = (WorldServer)worldIn;
                TeleporterDungeon teleporter = new TeleporterDungeonCustom(ws, getDungeonId(stack));

                ((IRitualCoordinate)entity.getCapability(RitualCoordinateProvider.RITUAL_COORDINATE_CAPABILITY, (EnumFacing)null)).setPos(new BlockPos(entity.posX, entity.posY, entity.posZ));
                ((IRitualCoordinate)entity.getCapability(RitualCoordinateProvider.RITUAL_COORDINATE_CAPABILITY, (EnumFacing)null)).setDim(entity.dimension);
                entity.changeDimension(ArcaneWorldConfig.DUNGEONS.DIM_ID, teleporter);
            } else {
                MinecraftServer server = worldIn.getMinecraftServer();
                ICommandSender sender = new AdminExecute((EntityPlayer) entity, entity.getPosition());

                String command = "tpj " + getDimension(stack);

                FunctionObject func = FunctionObject.create(server.getFunctionManager(), Arrays.asList(command));

                server.getFunctionManager().execute(func, sender);
            }

            this.playThunderSounds(worldIn, entity.posX, entity.posY, entity.posZ);

            stack.shrink(1);
        }

        return stack;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    }

    @Override
    public void setBoundWaystone(ItemStack stack, @Nullable BlockPos pos) {
    }

    @Override
    public boolean isRepairableByAnimator(ItemStack stack) {
        return false;
    }
}
