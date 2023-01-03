 package tb.common.item;
 
 import com.google.common.collect.HashMultimap;
 import com.google.common.collect.Multimap;
 import cpw.mods.fml.common.eventhandler.Event;
 import cpw.mods.fml.relauncher.Side;
 import cpw.mods.fml.relauncher.SideOnly;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Random;
 import java.util.UUID;
 import net.minecraft.enchantment.EnchantmentHelper;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.IEntityMultiPart;
 import net.minecraft.entity.SharedMonsterAttributes;
 import net.minecraft.entity.ai.attributes.AttributeModifier;
 import net.minecraft.entity.boss.EntityDragonPart;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.EnumRarity;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 import net.minecraft.item.ItemSword;
 import net.minecraft.potion.Potion;
 import net.minecraft.stats.AchievementList;
 import net.minecraft.stats.StatBase;
 import net.minecraft.stats.StatList;
 import net.minecraft.util.AxisAlignedBB;
 import net.minecraft.util.DamageSource;
 import net.minecraft.util.EnumChatFormatting;
 import net.minecraft.util.MathHelper;
 import net.minecraft.world.World;
 import net.minecraftforge.common.MinecraftForge;
 import net.minecraftforge.event.entity.player.AttackEntityEvent;
 import tb.core.TBCore;
 import thaumcraft.api.IRepairable;
 import thaumcraft.api.IWarpingGear;
 
 public class ItemHerobrinesScythe
   extends ItemSword
   implements IRepairable, IWarpingGear
 {
   public ItemHerobrinesScythe() {
     super(Item.ToolMaterial.EMERALD);
   }
 
   
   public EnumRarity getRarity(ItemStack itemstack) {
     return EnumRarity.uncommon;
   }
 
 
   
   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List lst, boolean held) {
     lst.add(EnumChatFormatting.ITALIC + "Well, they’re nothing...");
   }
 
   
   public void onUpdate(ItemStack stk, World w, Entity entity, int slot, boolean held) {
     super.onUpdate(stk, w, entity, slot, held);
     if (stk.isItemDamaged() && entity != null && entity.ticksExisted % 20 == 0 && entity instanceof EntityLivingBase) {
       stk.damageItem(-1, (EntityLivingBase)entity);
     }
   }
 
   
   public static void attack(EntityPlayer attacker, List<EntityLivingBase> doNotAttack, EntityLivingBase attacked) {
     AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(attacked.posX - 1.0D, attacked.posY - 1.0D, attacked.posZ - 1.0D, attacked.posX + 1.0D, attacked.posY + 1.0D, attacked.posZ + 1.0D).expand(6.0D, 6.0D, 6.0D);
     
     List<EntityLivingBase> mobs = attacked.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
     
     Random rnd = attacker.worldObj.rand;
     
     mobs.removeAll(doNotAttack);
     
     if (!mobs.isEmpty())
     {
       while (!mobs.isEmpty()) {
         
         int index = rnd.nextInt(mobs.size());
         if (mobs.get(index) != null && ((EntityLivingBase)mobs.get(index)).isEntityAlive() && mobs.get(index) instanceof net.minecraft.entity.monster.IMob && !(mobs.get(index) instanceof EntityPlayer)) {
           
           performPlayerAttackAt(attacker, (Entity)mobs.get(index));
           
           TBCore.proxy.lightning(attacker.worldObj, attacked.posX, attacked.posY + rnd.nextDouble() * attacked.getEyeHeight(), attacked.posZ, ((EntityLivingBase)mobs.get(index)).posX, ((EntityLivingBase)mobs.get(index)).posY + rnd.nextDouble() * ((EntityLivingBase)mobs.get(index)).getEyeHeight(), ((EntityLivingBase)mobs.get(index)).posZ, 20, 2.0F, 10, 0);
           attacker.worldObj.playSoundAtEntity((Entity)mobs.get(index), "thaumcraft:zap", 1.0F, 0.8F);
           
           doNotAttack.add(mobs.get(index));
           
           attack(attacker, doNotAttack, mobs.get(index));
 
           
           break;
         } 
         
         mobs.remove(index);
       } 
     }
   }
 
 
 
 
 
   
   public boolean onLeftClickEntity(ItemStack stk, EntityPlayer attacker, Entity attacked) {
     if (attacked.isEntityAlive() && attacked instanceof net.minecraft.entity.monster.IMob)
     {
       attack(attacker, new ArrayList<EntityLivingBase>(), (EntityLivingBase)attacked);
     }
     return super.onLeftClickEntity(stk, attacker, attacked);
   }
 
 
 
 
 
 
 
   
   public static void performPlayerAttackAt(EntityPlayer p, Entity p_71059_1_) {
     if (MinecraftForge.EVENT_BUS.post((Event)new AttackEntityEvent(p, p_71059_1_))) {
       return;
     }
 
     
     if (p_71059_1_.canAttackWithItem())
     {
       if (!p_71059_1_.hitByEntity((Entity)p)) {
         
         float f = (float)p.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
         int i = 0;
         float f1 = 0.0F;
         
         if (p_71059_1_ instanceof EntityLivingBase) {
           
           f1 = EnchantmentHelper.getEnchantmentModifierLiving((EntityLivingBase)p, (EntityLivingBase)p_71059_1_);
           i += EnchantmentHelper.getKnockbackModifier((EntityLivingBase)p, (EntityLivingBase)p_71059_1_);
         } 
         
         if (p.isSprinting())
         {
           i++;
         }
         
         if (f > 0.0F || f1 > 0.0F) {
           
           boolean flag = (p.fallDistance > 0.0F && !p.onGround && !p.isOnLadder() && !p.isInWater() && !p.isPotionActive(Potion.blindness) && p.ridingEntity == null && p_71059_1_ instanceof EntityLivingBase);
           
           if (flag && f > 0.0F)
           {
             f *= 1.5F;
           }
           
           f += f1;
           boolean flag1 = false;
           int j = EnchantmentHelper.getFireAspectModifier((EntityLivingBase)p);
           
           if (p_71059_1_ instanceof EntityLivingBase && j > 0 && !p_71059_1_.isBurning()) {
             
             flag1 = true;
             p_71059_1_.setFire(1);
           } 
           
           boolean flag2 = p_71059_1_.attackEntityFrom(DamageSource.causePlayerDamage(p), f);
           
           if (flag2) {
             
             if (i > 0) {
               
               p_71059_1_.addVelocity((-MathHelper.sin(p.rotationYaw * 3.1415927F / 180.0F) * i * 0.5F), 0.1D, (MathHelper.cos(p.rotationYaw * 3.1415927F / 180.0F) * i * 0.5F));
               p.motionX *= 0.6D;
               p.motionZ *= 0.6D;
               p.setSprinting(false);
             } 
             
             if (flag)
             {
               p.onCriticalHit(p_71059_1_);
             }
             
             if (f1 > 0.0F)
             {
               p.onEnchantmentCritical(p_71059_1_);
             }
             
             if (f >= 18.0F)
             {
               p.triggerAchievement((StatBase)AchievementList.overkill);
             }
             
             p.setLastAttacker(p_71059_1_);
             
             if (p_71059_1_ instanceof EntityLivingBase)
             {
               EnchantmentHelper.func_151384_a((EntityLivingBase)p_71059_1_, (Entity)p);
             }
             
             EnchantmentHelper.func_151385_b((EntityLivingBase)p, p_71059_1_);
             ItemStack itemstack = p.getCurrentEquippedItem();
             Object object = p_71059_1_;
             
             if (p_71059_1_ instanceof EntityDragonPart) {
               
               IEntityMultiPart ientitymultipart = ((EntityDragonPart)p_71059_1_).entityDragonObj;
               
               if (ientitymultipart != null && ientitymultipart instanceof EntityLivingBase)
               {
                 object = ientitymultipart;
               }
             } 
             
             if (itemstack != null && object instanceof EntityLivingBase) {
               
               itemstack.hitEntity((EntityLivingBase)object, p);
               
               if (itemstack.stackSize <= 0)
               {
                 p.destroyCurrentEquippedItem();
               }
             } 
             
             if (p_71059_1_ instanceof EntityLivingBase) {
               
               p.addStat(StatList.damageDealtStat, Math.round(f * 10.0F));
               
               if (j > 0)
               {
                 p_71059_1_.setFire(j * 4);
               }
             } 
             
             p.addExhaustion(0.3F);
           }
           else if (flag1) {
             
             p_71059_1_.extinguish();
           } 
         } 
       } 
     }
   }
 
   
   public int getWarp(ItemStack itemstack, EntityPlayer player) {
     return 3;
   }
 
 
   
   public Multimap getAttributeModifiers(ItemStack stack) {
     HashMultimap hashMultimap = HashMultimap.create();
     
     hashMultimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF"), "Weapon modifier", 14.5D, 0));
     hashMultimap.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), new AttributeModifier(UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CE"), "Speed modifier", 0.5D, 2));
     
     return (Multimap)hashMultimap;
   }
 
   
   public boolean isItemTool(ItemStack stk) {
     return true;
   }
 }


