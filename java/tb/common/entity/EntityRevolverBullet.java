 package tb.common.entity;
 
 import java.util.ArrayList;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.entity.projectile.EntityThrowable;
 import net.minecraft.item.ItemStack;
 import net.minecraft.nbt.NBTBase;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.util.DamageSource;
 import net.minecraft.util.MovingObjectPosition;
 import net.minecraft.world.World;
import scala.reflect.internal.Trees.This;
import tb.api.RevolverUpgrade;
 import tb.common.item.ItemRevolver;
import tb.core.TBCore;
import tb.utils.DummySteele.Pair;
import thaumcraft.common.Thaumcraft;
 
 
 public class EntityRevolverBullet
   extends EntityThrowable
 {
   public Entity shooter;
   public ItemStack revolver;
   ArrayList<Pair<RevolverUpgrade, Integer>> upgrades;
   boolean isPrimal;
   
   public EntityRevolverBullet(World w) {
     super(w);
   }
 
   
   public EntityRevolverBullet(World w, EntityLivingBase shooter) {
     super(w, shooter);
     this.revolver = shooter.getHeldItem();
     this.shooter = (Entity)shooter;
     if (this.revolver != null && this.revolver.getItem() instanceof ItemRevolver) {
       
       this.upgrades = ItemRevolver.getAllUpgradesFor(this.revolver);
       boolean allowNoclip = false;
       float speedIndex = 1.0F;
       for (Pair<RevolverUpgrade, Integer> p : this.upgrades) {
         
         if (!allowNoclip) {
           allowNoclip = ((RevolverUpgrade)p.getFirst()).bulletNoclip((EntityPlayer)shooter, this.revolver, ((Integer)p.getSecond()).intValue());
         }
         speedIndex = (float)((RevolverUpgrade)p.getFirst()).modifySpeed((EntityPlayer)shooter, this.revolver, speedIndex, ((Integer)p.getSecond()).intValue());
         
         if (p.getFirst() == RevolverUpgrade.primal)
           this.isPrimal = true; 
       } 
       if (allowNoclip) {
         this.noClip = true;
       }
       this.motionX *= 3.0D;
       this.motionY *= 3.0D;
       this.motionZ *= 3.0D;
       
       this.motionX *= speedIndex;
       this.motionY *= speedIndex;
       this.motionZ *= speedIndex;
     } 
   }
 
   @Override
   public void onUpdate() {
     if (this.worldObj.isRemote) {
       Thaumcraft.proxy.sparkle((float)this.posX, (float)this.posY, (float)this.posZ, 4);
     }
     if (this.worldObj.isRemote) {
       Thaumcraft.proxy.sparkle((float)(this.posX - this.motionX / 20.0D), (float)(this.posY - this.motionY / 20.0D), (float)(this.posZ - this.motionZ / 20.0D), 4);
     }
     if (this.ticksExisted >= 200) {
       setDead();
     }
     super.onUpdate();
     
     if (this.isPrimal && !this.isDead) {
       
       this.ticksExisted++;
       onUpdate(); //instant loop for hitscan
     } 
   }
 
   @Override
   protected float getGravityVelocity() {
     return this.isPrimal ? 0.0F : 0.01F;
   }
 
 
   @Override
   protected void onImpact(MovingObjectPosition object) {
     if (this.isDead) {
       return;
     }
     if (!this.worldObj.isRemote && object.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
       
       if (this.noClip)
         return; 
       if (this.worldObj.isBlockNormalCubeDefault(object.blockX, object.blockY, object.blockZ, true)) {
    	   Block b = this.worldObj.getBlock(object.blockX, object.blockY, object.blockZ);
    	   this.worldObj.playSoundEffect(object.blockX + 0.5D, object.blockY + 0.5D, object.blockZ + 0.5D, b.stepSound.getBreakSound(), 1.0F, 1.0F);
    	   //TBCore.TBLogger.log(Level.DEBUG, b.stepSound.getBreakSound());
    	   //System.out.println(b.stepSound.getBreakSound());
         setDead();
       } 
       else {
//         
         Block b = this.worldObj.getBlock(object.blockX, object.blockY, object.blockZ);
         //int meta = this.worldObj.getBlockMetadata(object.blockX, object.blockY, object.blockZ);
//         
//         for (int i = 0; i < 100; i++) {
//           this.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(b) + "_" + meta, object.blockX + this.worldObj.rand.nextDouble(), object.blockY + this.worldObj.rand.nextDouble(), object.blockZ + this.worldObj.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
//         }
//         if (this.worldObj.isRemote) {
//        	 //this.worldObj.playSound(object.blockX + 0.5D, object.blockY + 0.5D, object.blockZ + 0.5D, b.stepSound.getBreakSound(), 1.0F, 1.0F, false);
//        	 
//         }
         this.worldObj.playSoundEffect(object.blockX + 0.5D, object.blockY + 0.5D, object.blockZ + 0.5D, b.stepSound.getBreakSound(), 1.0F, 1.0F);
         //TBCore.TBLogger.log(Level.DEBUG, b.stepSound.getBreakSound());
         //System.out.println(b.stepSound.getBreakSound());
         setDead();
         
//         
//         //this.worldObj.setBlockToAir(object.blockX, object.blockY, object.blockZ);
       } 
     } 
     if (object.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
       
       Entity e = object.entityHit;
       if (e instanceof EntityLivingBase && e != this.shooter && !(e instanceof EntityRevolverBullet)) {
         
         EntityLivingBase elb = (EntityLivingBase)e;
         float initialDamage = 14.0F;
         for (Pair<RevolverUpgrade, Integer> p : this.upgrades) {
           initialDamage = ((RevolverUpgrade)p.getFirst()).modifyDamage_start(elb, this.revolver, initialDamage, ((Integer)p.getSecond()).intValue());
         }
         for (Pair<RevolverUpgrade, Integer> p : this.upgrades) {
           initialDamage = ((RevolverUpgrade)p.getFirst()).modifyDamage_end(elb, this.revolver, initialDamage, ((Integer)p.getSecond()).intValue());
         }
         elb.attackEntityFrom(new RevolverDamage("revolver"), initialDamage);
         
         boolean destroy = true;
         
         for (Pair<RevolverUpgrade, Integer> p : this.upgrades) {
           
           if (destroy) {
             destroy = ((RevolverUpgrade)p.getFirst()).afterhit(elb, (EntityPlayer)this.shooter, this.revolver, initialDamage, ((Integer)p.getSecond()).intValue()); continue;
           } 
           ((RevolverUpgrade)p.getFirst()).afterhit(elb, (EntityPlayer)this.shooter, this.revolver, initialDamage, ((Integer)p.getSecond()).intValue());
         } 
         
         if (destroy)
           setDead(); 
       } 
     } 
   }
   
   public static class RevolverDamage
     extends DamageSource
   {
     public RevolverDamage(String damage) {
       super(damage);
     }
   }
 
 
   
   public void writeEntityToNBT(NBTTagCompound tag) {
     super.writeEntityToNBT(tag);
     if (this.revolver != null) {
       
       NBTTagCompound revolverTag = new NBTTagCompound();
       this.revolver.writeToNBT(revolverTag);
       tag.setTag("revolverTag", (NBTBase)revolverTag);
     } 
     tag.setBoolean("noclip", this.noClip);
     tag.setBoolean("primal", this.isPrimal);
   }
 
   
   public void readEntityFromNBT(NBTTagCompound tag) {
     super.readEntityFromNBT(tag);
     
     if (tag.hasKey("revolverTag")) {
       
       this.revolver = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("revolverTag"));
       this.upgrades = ItemRevolver.getAllUpgradesFor(this.revolver);
     } 
     this.noClip = tag.getBoolean("noclip");
     this.isPrimal = tag.getBoolean("primal");
   }
 }


