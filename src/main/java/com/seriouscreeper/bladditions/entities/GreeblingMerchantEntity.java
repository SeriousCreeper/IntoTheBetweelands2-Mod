package com.seriouscreeper.bladditions.entities;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.delivery.DeliveryClient;
import party.lemons.deliverymechants.MerchantType;
import thebetweenlands.api.entity.IEntityBL;
import thebetweenlands.client.render.particle.BLParticles;
import thebetweenlands.client.render.particle.ParticleFactory;
import thebetweenlands.common.entity.ai.EntityAvoidEntityFlatPath;
import thebetweenlands.common.entity.mobs.EntityGreebling;
import thebetweenlands.common.entity.mobs.EntityGreeblingCoracle;
import thebetweenlands.common.entity.movement.PathNavigateAboveWater;
import thebetweenlands.common.registries.LootTableRegistry;
import thebetweenlands.common.registries.SoundRegistry;
import thebetweenlands.util.NonNullDelegateList;

import javax.annotation.Nullable;
import java.util.List;

public class GreeblingMerchantEntity extends EntityCreature implements IEntityBL {
    protected static final byte EVENT_DISAPPEAR = 41;
    protected static final byte EVENT_SPOUT = 42;
    public static final DataParameter<Integer> TYPE;
    private static final DataParameter<Integer> SINKING_TICKS;
    private static final DataParameter<Integer> LOOT_CLICKS;
    private EntityAvoidEntityFlatPath<EntityPlayer> avoidPlayer;
    private GreeblingMerchantEntity.AIWaterWander waterWander;
    private EntityAILookIdle lookIdle;
    private boolean hasSetAIForEmptyBoat = false;
    private boolean looted = false;
    private NonNullList<ItemStack> loot = NonNullList.create();
    private int shutUpFFSTime;
    public int rowTicks;
    public float rowSpeed = 0.5F;

    public GreeblingMerchantEntity(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
        this.setPathPriority(PathNodeType.WALKABLE, -100.0F);
        this.setPathPriority(PathNodeType.BLOCKED, -100.0F);
        this.setPathPriority(PathNodeType.LAVA, -100.0F);
        this.setPathPriority(PathNodeType.WATER, 16.0F);
        this.stepHeight = 0.0F;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SINKING_TICKS, 0);
        this.dataManager.register(LOOT_CLICKS, 0);
        this.dataManager.register(TYPE, 0);
    }

    protected void initEntityAI() {
        this.lookIdle = new EntityAILookIdle(this);
        this.tasks.addTask(2, this.lookIdle);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        MerchantType type = MerchantType.getRandomType(this.world.rand);
        this.dataManager.set(TYPE, MerchantType.TYPES.indexOf(type));
        super.onInitialSpawn(difficulty, livingdata);
        return livingdata;
    }

    protected PathNavigate createNavigator(World world) {
        return new PathNavigateAboveWater(this, world);
    }

    public float getBlockPathWeight(BlockPos pos) {
        return this.world.getBlockState(pos).getMaterial() == Material.WATER ? 10.0F + this.world.getLightBrightness(pos) - 0.5F : super.getBlockPathWeight(pos);
    }

    public boolean getCanSpawnHere() {
        int y = MathHelper.floor(this.getEntityBoundingBox().minY);
        if (y <= 120 && y > 110) {
            return this.getEntityWorld().checkNoEntityCollision(this.getEntityBoundingBox()) && this.getEntityWorld().getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && this.getEntityWorld().isMaterialInBB(this.getEntityBoundingBox(), Material.WATER);
        } else {
            return false;
        }
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote && this.getSinkingTicks() <= 0) {
            ++this.rowTicks;
            if (!this.isSilent() && this.posX != this.lastTickPosX && this.posZ != this.lastTickPosZ) {
                float rowAngle1 = MathHelper.cos((float)this.rowTicks * this.rowSpeed);
                float rowAngle2 = MathHelper.cos((float)(this.rowTicks + 1) * this.rowSpeed);
                if (rowAngle1 <= 0.8F && rowAngle2 > 0.8F) {
                    this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_SWIM, this.getSoundCategory(), 0.2F, 0.8F + 0.4F * this.rand.nextFloat(), false);
                }
            }
        }

        if (this.shutUpFFSTime > 0) {
            --this.shutUpFFSTime;
            this.livingSoundTime = -this.getTalkInterval();
        }

        if (this.getEntityWorld().containsAnyLiquid(this.getEntityBoundingBox()) && this.getSinkingTicks() <= 200) {
            this.motionY += 0.06D;
        }

        if (this.isGreeblingAboveWater() && this.getSinkingTicks() <= 200) {
            if (this.motionY < 0.0D) {
                this.motionY = 0.0D;
            }

            this.fallDistance = 0.0F;
        } else {
            this.motionY = -0.0075D;
        }

        this.motionX = 0;
        this.motionZ = 0;

        this.limbSwing = (float)((double)this.limbSwing + 0.5D);
        if (this.posX != this.lastTickPosX && this.posZ != this.lastTickPosZ) {
            this.limbSwingAmount = (float)((double)this.limbSwingAmount + 0.5D);
        }
    }

    public boolean isGreeblingAboveWater() {
        AxisAlignedBB floatingBox = new AxisAlignedBB(this.getEntityBoundingBox().minX + 0.25D, this.getEntityBoundingBox().minY + 0.11999999731779099D, this.getEntityBoundingBox().minZ + 0.25D, this.getEntityBoundingBox().maxX - 0.25D, this.getEntityBoundingBox().minY + 0.0625D, this.getEntityBoundingBox().maxZ - 0.25D);
        return this.getEntityWorld().containsAnyLiquid(floatingBox);
    }

    public void setSinkingTicks(int count) {
        this.dataManager.set(SINKING_TICKS, count);
    }

    public int getSinkingTicks() {
        return (Integer)this.dataManager.get(SINKING_TICKS);
    }

    public void setLootClicks(int count) {
        this.dataManager.set(LOOT_CLICKS, count);
    }

    public int getLootClicks() {
        return (Integer)this.dataManager.get(LOOT_CLICKS);
    }

    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("_merch", (Integer)this.dataManager.get(TYPE));
        nbt.setInteger("sinkingTicks", this.getSinkingTicks());
        nbt.setBoolean("Looted", this.looted);
        nbt.setInteger("LootCount", this.loot.size());
        nbt.setInteger("LootClicks", this.getLootClicks());
        nbt.setTag("Loot", ItemStackHelper.saveAllItems(new NBTTagCompound(), this.loot, false));
    }

    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.dataManager.set(TYPE, nbt.getInteger("_merch"));
        this.setSinkingTicks(nbt.getInteger("sinkingTicks"));
        this.looted = nbt.getBoolean("Looted");
        this.loot = NonNullList.withSize(nbt.getInteger("LootCount"), ItemStack.EMPTY);
        this.setLootClicks(nbt.getInteger("lootClicks"));
        ItemStackHelper.loadAllItems(nbt.getCompoundTag("Loot"), this.loot);
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if (id == 41) {
            this.doLeafEffects();
        }

        if (id == 42) {
            this.doSpoutEffects();
        }

    }

    @SideOnly(Side.CLIENT)
    private void doSpoutEffects() {
        if (this.getEntityWorld().isRemote) {
            int count = this.getSinkingTicks() <= 240 ? 40 : 10;
            float x = (float)this.posX;
            float y = (float)(this.posY + 0.25D);
            float z = (float)this.posZ;

            while(count-- > 0) {
                float dx = this.getEntityWorld().rand.nextFloat() * 0.25F - 0.1255F;
                float dy = this.getEntityWorld().rand.nextFloat() * 0.25F - 0.1255F;
                float dz = this.getEntityWorld().rand.nextFloat() * 0.25F - 0.1255F;
                float mag = 0.08F + this.getEntityWorld().rand.nextFloat() * 0.07F;
                int waterColor = BiomeColorHelper.getWaterColorAtPos(this.world, new BlockPos(this));
                float r = (float)(waterColor >> 16 & 255) / 255.0F;
                float g = (float)(waterColor >> 8 & 255) / 255.0F;
                float b = (float)(waterColor & 255) / 255.0F;
                if (this.getSinkingTicks() <= 240) {
                    BLParticles.RAIN.spawn(this.getEntityWorld(), (double)x, (double)y, (double)z, ParticleFactory.ParticleArgs.get().withMotion((double)(dx * mag), (double)(dy * mag), (double)(dz * mag)).withColor(r, g, b + 0.075F, 1.0F));
                } else if (this.getSinkingTicks() > 240 && this.getSinkingTicks() <= 400 && this.getSinkingTicks() % 5 == 0) {
                    BLParticles.BUBBLE_WATER.spawn(this.getEntityWorld(), (double)x, (double)y, (double)z, ParticleFactory.ParticleArgs.get().withMotion((double)(dx * mag), (double)(dy * mag), (double)(dz * mag)).withColor(r + 0.05F, g + 0.15F, b + 0.05F, 1.0F));
                }
            }
        }

    }

    @SideOnly(Side.CLIENT)
    private void doLeafEffects() {
        if (this.getEntityWorld().isRemote) {
            int leafCount = 40;
            float x = (float)this.posX;
            float y = (float)(this.posY + 0.75D);
            float z = (float)this.posZ;

            while(leafCount-- > 0) {
                float dx = this.getEntityWorld().rand.nextFloat() * 1.0F - 0.5F;
                float dy = this.getEntityWorld().rand.nextFloat() * 1.0F - 0.1F;
                float dz = this.getEntityWorld().rand.nextFloat() * 1.0F - 0.5F;
                float mag = 0.08F + this.getEntityWorld().rand.nextFloat() * 0.07F;
                BLParticles.WEEDWOOD_LEAF.spawn(this.getEntityWorld(), (double)x, (double)y, (double)z, ParticleFactory.ParticleArgs.get().withMotion((double)(dx * mag), (double)(dy * mag), (double)(dz * mag)));
            }
        }

    }

    protected float getSoundVolume() {
        return 0.75F;
    }

    protected SoundEvent getAmbientSound() {
        if (this.getSinkingTicks() <= 0) {
            if (this.rand.nextInt(4) == 0 && this.shutUpFFSTime <= 0) {
                this.shutUpFFSTime = 120;
                return SoundRegistry.GREEBLING_HUM;
            } else {
                return SoundRegistry.GREEBLING_GIGGLE;
            }
        } else {
            return null;
        }
    }

    public int getMaxSpawnedInChunk() {
        return 1;
    }

    public boolean attackEntityFrom(DamageSource source, float damage) {
        return false;
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        MerchantType type = (MerchantType)MerchantType.TYPES.get((Integer)this.dataManager.get(TYPE));

        if (type.isEnabled() && this.world.isRemote) {
            DeliveryClient.sendStoreMessage(type.getName(), false);
        }

        return super.processInteract(player, hand);
    }

    public void dropLoot(EntityPlayer player) {
        if (!this.getEntityWorld().isRemote) {
            if (!this.looted) {
                this.looted = true;
                LootTable lootTable = this.getEntityWorld().getLootTableManager().getLootTableFromLocation(LootTableRegistry.GREEBLING_CORACLE);
                LootContext.Builder builder = (new LootContext.Builder((WorldServer)this.getEntityWorld())).withLootedEntity(this).withPlayer(player).withLuck(player.getLuck());
                this.loot = new NonNullDelegateList(lootTable.generateLootForPools(this.rand, builder.build()), ItemStack.EMPTY);
            }

            ItemStack stack = (ItemStack)this.loot.get(this.getLootClicks());
            if (!stack.isEmpty()) {
                this.entityDropItem(stack, 0.0F);
                this.loot.set(this.getLootClicks(), ItemStack.EMPTY);
            }
        }
    }

    static {
        TYPE = EntityDataManager.createKey(GreeblingMerchantEntity.class, DataSerializers.VARINT);
        SINKING_TICKS = EntityDataManager.createKey(GreeblingMerchantEntity.class, DataSerializers.VARINT);
        LOOT_CLICKS = EntityDataManager.createKey(GreeblingMerchantEntity.class, DataSerializers.VARINT);
    }

    public class AIWaterWander extends EntityAIWander {
        private final GreeblingMerchantEntity coracle;

        public AIWaterWander(GreeblingMerchantEntity coracleIn, double speedIn, int chance) {
            super(coracleIn, speedIn, chance);
            this.setMutexBits(1);
            this.coracle = coracleIn;
        }

        @Nullable
        protected Vec3d getPosition() {
            return RandomPositionGenerator.findRandomTarget(this.coracle, 16, 0);
        }
    }
}
