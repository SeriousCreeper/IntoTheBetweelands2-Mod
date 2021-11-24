package com.seriouscreeper.bladditions;

import com.seriouscreeper.bladditions.commands.BLAdditionsCommands;
import com.seriouscreeper.bladditions.events.BLAdditionsEventHandler;
import com.seriouscreeper.bladditions.items.FairyLightsRecipes;
import com.seriouscreeper.bladditions.proxy.CommonProxy;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import thebetweenlands.common.block.farming.BlockGenericDugSoil;
import thebetweenlands.common.entity.projectiles.EntityAngryPebble;
import thebetweenlands.common.entity.projectiles.EntityBLArrow;
import thebetweenlands.common.item.misc.ItemOctineIngot;
import thebetweenlands.common.item.tools.bow.EnumArrowType;
import thebetweenlands.common.registries.ItemRegistry;
import thebetweenlands.common.registries.SoundRegistry;
import thebetweenlands.common.tile.TileEntityDugSoil;

@Mod(modid = BLAdditions.MODID, name = BLAdditions.NAME, version = BLAdditions.VERSION, dependencies = "required-after:mysticalmechanics;required-after:embers;required-after:fairylights;required-after:pyrotech;required-after:pizzacraft;required-after:growthcraft;required-after:crafttweaker;required-after:deliverymerchants;required-after:thebetweenlands;required-after:roots;required-after:thaumcraft;required-after:thaumicperiphery")
public class BLAdditions
{
    public static final String MODID = "bladditions";
    public static final String NAME = "BL Additions";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.Instance
    public static BLAdditions instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);

        MinecraftForge.EVENT_BUS.register(new BLAdditionsEventHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new BLAdditionsCommands());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemRegistry.OCTINE_INGOT, new BehaviorDefaultDispenseItem()
        {
            @Override
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                World world = source.getWorld();

                if(!world.isRemote) {
                    BlockPos blockPos = (source.getBlockState().getValue(BlockDispenser.FACING) == EnumFacing.UP) ? source.getBlockPos().up() : source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING));

                    boolean hasTinder = false;
                    boolean isBlockTinder = false;

                    IBlockState blockState = world.getBlockState(blockPos);
                    if(((ItemOctineIngot)stack.getItem()).isTinder(stack, ItemStack.EMPTY, blockState)) {
                        hasTinder = true;
                        isBlockTinder = true;
                    }

                    if(hasTinder && isBlockTinder) {
                        IBlockState moss = world.getBlockState(blockPos);
                        world.setBlockState(blockPos, Blocks.FIRE.getDefaultState());
                        world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1, 1);
                    }

                    return stack;
                }

                return super.dispenseStack(source, stack);
            }
        });

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemRegistry.ITEMS_MISC, new BehaviorDefaultDispenseItem()
        {
            @Override
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                World world=source.getWorld();
                if(!world.isRemote&&stack.getItemDamage()==2)
                {
                    BlockPos blockPos=(source.getBlockState().getValue(BlockDispenser.FACING)==EnumFacing.UP) ? source.getBlockPos().up():source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING)).down();

                    if(world.getBlockState(blockPos).getBlock() instanceof BlockGenericDugSoil)
                    {
                        TileEntityDugSoil te=BlockGenericDugSoil.getTile(world, blockPos);
                        if(te!=null&&te.getCompost()==0)
                        {
                            world.playSound((EntityPlayer)null, blockPos.getX()+0.5, blockPos.getY()+1, blockPos.getZ()+0.5, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.PLAYERS, 1.0F, 0.5F+world.rand.nextFloat()*0.5F);
                            te.setCompost(30);
                            stack.shrink(1);
                        }
                    }

                    return stack;
                }

                return super.dispenseStack(source, stack);
            }
        });

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemRegistry.BL_BUCKET, new BehaviorDefaultDispenseItem()
        {
            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                World world = source.getWorld();

                if(!world.isRemote)
                {
                    BlockPos blockPos = (source.getBlockState().getValue(BlockDispenser.FACING) == EnumFacing.UP) ? source.getBlockPos().up() : source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING)).down();

                    IFluidHandler fluidHandler = FluidUtil.getFluidHandler(world, blockPos, null);

                    if(fluidHandler != null) {
                        ItemStack singleStack = stack.copy();
                        singleStack.setCount(1);
                        IFluidHandlerItem bucket = FluidUtil.getFluidHandler(singleStack);
                        FluidStack fluidStack = bucket.drain(Fluid.BUCKET_VOLUME, false);
                        fluidHandler.fill(fluidStack, false);

                        FluidActionResult result = fluidStack != null ? FluidUtil.tryEmptyContainer(singleStack, fluidHandler, 1000, null, false) : FluidActionResult.FAILURE;

                        if(result.isSuccess()) {
                            ItemStack drainedStack = result.getResult();

                            FluidUtil.tryEmptyContainer(singleStack, fluidHandler, 1000, null, true);

                            if (drainedStack.getCount() == 1)
                            {
                                return drainedStack;
                            }
                            else if (!drainedStack.isEmpty() && ((TileEntityDispenser)source.getBlockTileEntity()).addItemStack(drainedStack) < 0)
                            {
                                this.dispense(source, drainedStack);
                            }

                            ItemStack stackCopy = drainedStack.copy();
                            stackCopy.shrink(1);
                            return stackCopy;
                        }
                    }

                    return stack;
                }

                return super.dispenseStack(source, stack);
            }
        });

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemRegistry.ANGLER_TOOTH_ARROW, new BehaviorProjectileDispense()
        {
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
            {
                EntityBLArrow entitytippedarrow = new EntityBLArrow(worldIn);
                entitytippedarrow.setType(EnumArrowType.DEFAULT);
                entitytippedarrow.setPosition(position.getX(), position.getY(), position.getZ());
                entitytippedarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        });

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemRegistry.BASILISK_ARROW, new BehaviorProjectileDispense()
        {
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
            {
                EntityBLArrow entitytippedarrow = new EntityBLArrow(worldIn);
                entitytippedarrow.setType(EnumArrowType.BASILISK);
                entitytippedarrow.setPosition(position.getX(), position.getY(), position.getZ());
                entitytippedarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        });

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemRegistry.OCTINE_ARROW, new BehaviorProjectileDispense()
        {
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
            {
                EntityBLArrow entitytippedarrow = new EntityBLArrow(worldIn);
                entitytippedarrow.setType(EnumArrowType.OCTINE);
                entitytippedarrow.setPosition(position.getX(), position.getY(), position.getZ());
                entitytippedarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        });

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemRegistry.POISONED_ANGLER_TOOTH_ARROW, new BehaviorProjectileDispense()
        {
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
            {
                EntityBLArrow entitytippedarrow = new EntityBLArrow(worldIn);
                entitytippedarrow.setType(EnumArrowType.ANGLER_POISON);
                entitytippedarrow.setPosition(position.getX(), position.getY(), position.getZ());
                entitytippedarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        });

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemRegistry.SHOCK_ARROW, new BehaviorProjectileDispense()
        {
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
            {
                EntityBLArrow entitytippedarrow = new EntityBLArrow(worldIn);
                entitytippedarrow.setType(EnumArrowType.SHOCK);
                entitytippedarrow.setPosition(position.getX(), position.getY(), position.getZ());
                entitytippedarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        });

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemRegistry.SLUDGE_WORM_ARROW, new BehaviorProjectileDispense()
        {
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
            {
                EntityBLArrow entitytippedarrow = new EntityBLArrow(worldIn);
                entitytippedarrow.setType(EnumArrowType.WORM);
                entitytippedarrow.setPosition(position.getX(), position.getY(), position.getZ());
                entitytippedarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        });

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemRegistry.ANGRY_PEBBLE, new BehaviorProjectileDispense()
        {
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
            {
                BlockPos blockPos = new BlockPos(position.getX(), position.getY(), position.getZ());

                EntityAngryPebble pebble = new EntityAngryPebble(worldIn);
                pebble.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());

                return pebble;
            }
            protected float getProjectileInaccuracy()
            {
                return super.getProjectileInaccuracy() * 0.5F;
            }
            protected float getProjectileVelocity()
            {
                return super.getProjectileVelocity() * 1.2F;
            }
            protected void playDispenseSound(IBlockSource source)
            {
                source.getWorld().playSound(null, source.getBlockPos().getX(), source.getBlockPos().getY(), source.getBlockPos().getZ(), SoundRegistry.SORRY, SoundCategory.PLAYERS, 0.7F, 0.8F);
            }
        });
    }
}
