package com.seriouscreeper.bladditions.entities;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import party.lemons.delivery.DeliveryClient;
import party.lemons.deliverymechants.MerchantType;
import thebetweenlands.api.entity.IEntityBL;
import thebetweenlands.common.entity.ai.EntityAvoidEntityFlatPath;
import thebetweenlands.common.entity.movement.PathNavigateAboveWater;
import thebetweenlands.common.registries.SoundRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class GreeblingMerchantEntity extends EntityCreature implements IEntityBL {
    public static final DataParameter<Integer> TYPE = EntityDataManager.createKey(GreeblingMerchantEntity.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> LAST_SHOP_CHANGE = EntityDataManager.createKey(GreeblingMerchantEntity.class, DataSerializers.VARINT);
    private EntityAvoidEntityFlatPath<EntityPlayer> avoidPlayer;
    private GreeblingMerchantEntity.AIWaterWander waterWander;
    private EntityAILookIdle lookIdle;
    private final NonNullList<ItemStack> loot = NonNullList.create();
    private int shutUpFFSTime;
    private final boolean readData = false;
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

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(TYPE, 0);
        dataManager.register(LAST_SHOP_CHANGE, 0);
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
        this.dataManager.set(LAST_SHOP_CHANGE, (int)(this.world.getTotalWorldTime() / 24000));
        super.onInitialSpawn(difficulty, livingdata);
        return livingdata;
    }

    protected PathNavigate createNavigator(World world) {
        return new PathNavigateAboveWater(this, world);
    }

    public float getBlockPathWeight(BlockPos pos) {
        return this.world.getBlockState(pos).getMaterial() == Material.WATER ? 10.0F + this.world.getLightBrightness(pos) - 0.5F : super.getBlockPathWeight(pos);
    }

    @Override
    public boolean getCanSpawnHere() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote) {
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

        if (this.getEntityWorld().containsAnyLiquid(this.getEntityBoundingBox())) {
            this.motionY += 0.06D;
        }

        if (this.isGreeblingAboveWater()) {
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

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeLeashedTo(EntityPlayer player) {
        return false;
    }

    public boolean isGreeblingAboveWater() {
        AxisAlignedBB floatingBox = new AxisAlignedBB(this.getEntityBoundingBox().minX + 0.25D, this.getEntityBoundingBox().minY + 0.11999999731779099D, this.getEntityBoundingBox().minZ + 0.25D, this.getEntityBoundingBox().maxX - 0.25D, this.getEntityBoundingBox().minY + 0.0625D, this.getEntityBoundingBox().maxZ - 0.25D);
        return this.getEntityWorld().containsAnyLiquid(floatingBox);
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("_merch", this.dataManager.get(TYPE));
        nbt.setInteger("last_shop_change", this.dataManager.get(LAST_SHOP_CHANGE));
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.dataManager.set(TYPE, nbt.getInteger("_merch"));
        this.dataManager.set(LAST_SHOP_CHANGE, nbt.getInteger("last_shop_change"));
        super.readFromNBT(nbt);
    }

    protected float getSoundVolume() {
        return 0.75F;
    }

    protected SoundEvent getAmbientSound() {
        if (this.rand.nextInt(4) == 0 && this.shutUpFFSTime <= 0) {
            this.shutUpFFSTime = 120;
            return SoundRegistry.GREEBLING_HUM;
        } else {
            return SoundRegistry.GREEBLING_GIGGLE;
        }
    }

    public int getMaxSpawnedInChunk() {
        return 1;
    }

    public boolean attackEntityFrom(DamageSource source, float damage) {
        if(source == DamageSource.OUT_OF_WORLD) {
            return super.attackEntityFrom(source, damage);
        }
        
        return false;
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        MerchantType type;
        int currentDay = (int)(this.world.getWorldTime() / 24000);

        if(currentDay != LastDaySinceChange()) {
            List<MerchantType> filteredMerchants = MerchantType.TYPES.stream().filter(MerchantType::isEnabled).collect(Collectors.toList());
            type = filteredMerchants.get(currentDay % filteredMerchants.size());

            this.dataManager.set(TYPE, MerchantType.TYPES.indexOf(type));
            this.dataManager.set(LAST_SHOP_CHANGE, (int)(this.world.getWorldTime() / 24000));
        } else {
            type = MerchantType.TYPES.get(this.dataManager.get(TYPE));
        }

        if (type.isEnabled() && this.world.isRemote) {
            DeliveryClient.sendStoreMessage(type.getName(), false);
        }

        player.stopActiveHand();
        return true;
    }

    public int LastDaySinceChange() {
        return dataManager.get(LAST_SHOP_CHANGE);
    }

    public int GetMerchantType() {
        return dataManager.get(TYPE);
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
