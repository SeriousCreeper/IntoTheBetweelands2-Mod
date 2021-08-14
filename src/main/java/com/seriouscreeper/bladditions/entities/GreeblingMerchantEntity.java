package com.seriouscreeper.bladditions.entities;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import party.lemons.delivery.DeliveryClient;
import party.lemons.deliverymechants.MerchantType;
import thebetweenlands.common.entity.mobs.EntityGreebling;

import javax.annotation.Nullable;

public class GreeblingMerchantEntity extends EntityGreebling {
    public static final DataParameter<Integer> TYPE;

    public GreeblingMerchantEntity(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("_merch", (Integer)this.dataManager.get(TYPE));
        return super.writeToNBT(compound);
    }

    public void readFromNBT(NBTTagCompound compound) {
        this.dataManager.set(TYPE, compound.getInteger("_merch"));
        super.readFromNBT(compound);
    }

    protected void initEntityAI() {
        //this.tasks.addTask(0, new EntityAISwimming(this));
        //this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        //this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityEvoker.class, 12.0F, 0.8D, 0.8D));
        //this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVindicator.class, 8.0F, 0.8D, 0.8D));
        //this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVex.class, 8.0F, 0.6D, 0.6D));
        //this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        //this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        //this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        //this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
        //this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        //this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, 0.6D));
        //this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(TYPE, 0);
    }


    @Override
    public void onUpdate() {
        this.disappearTimer = -1;
        super.onUpdate();
    }


    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        MerchantType type = MerchantType.getRandomType(this.world.rand);
        this.dataManager.set(TYPE, MerchantType.TYPES.indexOf(type));

        super.onInitialSpawn(difficulty, livingdata);

        return livingdata;
    }

    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        MerchantType type = (MerchantType)MerchantType.TYPES.get((Integer)this.dataManager.get(TYPE));
        if (type.isEnabled() && this.world.isRemote) {
            DeliveryClient.sendStoreMessage(type.getName(), false);
        }

        return super.processInteract(player, hand);
    }

    static {
        TYPE = EntityDataManager.createKey(GreeblingMerchantEntity.class, DataSerializers.VARINT);
    }
}
