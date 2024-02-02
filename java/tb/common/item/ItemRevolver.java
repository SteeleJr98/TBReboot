 package tb.common.item;
 
 import com.google.common.collect.HashMultimap;
 import com.google.common.collect.Multimap;
 import java.util.ArrayList;
 import java.util.List;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.SharedMonsterAttributes;
 import net.minecraft.entity.ai.attributes.AttributeModifier;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.EnumRarity;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 import net.minecraft.nbt.NBTBase;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.nbt.NBTTagList;
 import net.minecraft.util.StatCollector;
 import net.minecraft.world.World;
 import tb.api.RevolverUpgrade;
 import tb.common.entity.EntityRevolverBullet;
 import tb.core.TBCore;
 import tb.network.proxy.TBNetworkManager;
 import tb.utils.DummySteele.Pair;
 import thaumcraft.api.IRepairable;
 import thaumcraft.api.IWarpingGear;
 import thaumcraft.api.aspects.Aspect;
 import thaumcraft.api.aspects.AspectList;
 import thaumcraft.codechicken.lib.math.MathHelper;
 import thaumcraft.common.Thaumcraft;
 import thaumcraft.common.blocks.ItemJarFilled;
 import thaumcraft.common.config.ConfigBlocks;
 
 
 public class ItemRevolver
   extends Item
   implements IRepairable, IWarpingGear
 {
   public static void addUpgrade(ItemStack stk, RevolverUpgrade upgrade, int level) {
     if (stk.hasTagCompound() && stk.getTagCompound().hasKey("tb.upgrades")) {
       
       NBTTagList upgrades = stk.getTagCompound().getTagList("tb.upgrades", 10);
       for (int i = 0; i < upgrades.tagCount(); i++) {
         
         NBTTagCompound nBTTagCompound = upgrades.getCompoundTagAt(i);
         int id = nBTTagCompound.getInteger("id");
         int llevel = nBTTagCompound.getInteger("level");
         if (upgrade.id == id) {
           
           llevel = level;
           nBTTagCompound.setInteger("level", llevel);
           return;
         } 
       } 
       NBTTagCompound tag = new NBTTagCompound();
       tag.setInteger("id", upgrade.id);
       tag.setInteger("level", level);
       upgrades.appendTag((NBTBase)tag);
     }
     else {
       
       if (!stk.hasTagCompound()) {
         stk.setTagCompound(new NBTTagCompound());
       }
       if (!stk.getTagCompound().hasKey("tb.upgrades")) {
         stk.getTagCompound().setTag("tb.upgrades", (NBTBase)new NBTTagList());
       }
       addUpgrade(stk, upgrade, level);
     } 
   }
 
   
   public static int getUpgradeLevel(ItemStack stk, RevolverUpgrade upgrade) {
     if (stk.hasTagCompound() && stk.getTagCompound().hasKey("tb.upgrades")) {
       
       NBTTagList upgrades = stk.getTagCompound().getTagList("tb.upgrades", 10);
       for (int i = 0; i < upgrades.tagCount(); i++) {
         
         NBTTagCompound tag = upgrades.getCompoundTagAt(i);
         int id = tag.getInteger("id");
         int level = tag.getInteger("level");
         if (id < RevolverUpgrade.allUpgrades.length && RevolverUpgrade.allUpgrades[id] != null && RevolverUpgrade.allUpgrades[id].equals(upgrade)) {
           return level;
         }
       } 
     } 
     return 0;
   }
 
   
   public static ArrayList<Pair<RevolverUpgrade, Integer>> getAllUpgradesFor(ItemStack stk) {
     ArrayList<Pair<RevolverUpgrade, Integer>> retLst = new ArrayList<Pair<RevolverUpgrade, Integer>>();
     
     if (stk.hasTagCompound() && stk.getTagCompound().hasKey("tb.upgrades")) {
       
       NBTTagList upgrades = stk.getTagCompound().getTagList("tb.upgrades", 10);
       for (int i = 0; i < upgrades.tagCount(); i++) {
         
         NBTTagCompound tag = upgrades.getCompoundTagAt(i);
         int id = tag.getInteger("id");
         int level = tag.getInteger("level");
         if (id < RevolverUpgrade.allUpgrades.length && RevolverUpgrade.allUpgrades[id] != null) {
           retLst.add(new Pair(RevolverUpgrade.allUpgrades[id], Integer.valueOf(level)));
         }
       } 
     } 
     return retLst;
   }
 
   
   public boolean hitEntity(ItemStack stk, EntityLivingBase attacker, EntityLivingBase attacked) {
     stk.damageItem(2, attacked);
     return true;
   }
 
   
   public ItemStack onItemRightClick(ItemStack stk, World w, EntityPlayer user) {
     if (w.isRemote) {
       return stk;
     }
     if (!stk.hasTagCompound()) {
       stk.setTagCompound(new NBTTagCompound());
     }
     if (stk.getTagCompound().getInteger("shots") > 0) {
       
       EntityRevolverBullet b = new EntityRevolverBullet(w, (EntityLivingBase)user);
       if (!w.isRemote) {
         w.spawnEntityInWorld((Entity)b);
       }
       stk.damageItem(1, (EntityLivingBase)user);
       if (stk == null || stk.getItemDamage() > stk.getMaxDamage()) {
         return null;
       }
       TBNetworkManager.playSoundOnServer(w, "thaumicbases:revolver.shot", user.posX, user.posY, user.posZ, 3.0F, 1.0F);
       
       stk.getTagCompound().setDouble("barrelRotation", stk.getTagCompound().getDouble("barrelRotation") + 45.0D);
       
       stk.getTagCompound().setInteger("shots", stk.getTagCompound().getInteger("shots") - 1);
 
     
     }
     else if (stk.hasTagCompound() && stk.getTagCompound().hasKey("jar")) {
       
       ItemStack jar = ItemStack.loadItemStackFromNBT(stk.stackTagCompound.getCompoundTag("jar"));
       
       if (jar == null) {
         
         TBNetworkManager.playSoundOnServer(w, "thaumicbases:revolver.click", user.posX, user.posY, user.posZ, 3.0F, 2.0F);
         stk.getTagCompound().setDouble("barrelRotation", stk.getTagCompound().getDouble("barrelRotation") + 45.0D);
         
         return super.onItemRightClick(stk, w, user);
       } 
       
       ArrayList<Pair<RevolverUpgrade, Integer>> upgrades = getAllUpgradesFor(stk);
       boolean hasPrimal = false;
       for (Pair<RevolverUpgrade, Integer> p : upgrades) {
         
         if (p.getFirst() == RevolverUpgrade.primal) {
           hasPrimal = true;
         }
         if (hasPrimal) {
           break;
         }
       } 
       int addedShots = hasPrimal ? 1 : 2;
       for (Pair<RevolverUpgrade, Integer> p : upgrades)
       {
         addedShots = ((RevolverUpgrade)p.getFirst()).modifyShots(user, stk, ((Integer)p.getSecond()).intValue(), addedShots, hasPrimal);
       }
       
       boolean addShots = false;
       
       if (!(jar.getItem() instanceof ItemJarFilled)) {
         return super.onItemRightClick(stk, w, user);
       }
       AspectList aspects = ((ItemJarFilled)jar.getItem()).getAspects(jar);
       if (aspects != null && aspects.size() > 0)
       {
         for (Aspect tag : aspects.getAspectsSorted()) {
           
           if (tag == Aspect.WEAPON) {
             
             int required = (addedShots < 0) ? 2 : 1;
             if (aspects.getAmount(tag) >= required) {
               
               aspects.reduce(tag, required);
               addShots = true;
               
               break;
             } 
           } 
         } 
       }
       if (addShots) {
         
         stk.getTagCompound().setInteger("shots", (addedShots < 0) ? 1 : addedShots);
         TBNetworkManager.playSoundOnServer(w, "thaumicbases:revolver.reload", user.posX, user.posY, user.posZ, 3.0F, 2.0F);
       } 
       
       if (aspects.visSize() > 0) {
         
         ((ItemJarFilled)jar.getItem()).setAspects(jar, aspects);
         NBTTagCompound tag = new NBTTagCompound();
         jar.writeToNBT(tag);
         stk.setTagInfo("jar", (NBTBase)tag);
       } else {
         
         ItemStack emptyJar = new ItemStack(ConfigBlocks.blockJar, 1, 0);
         NBTTagCompound tag = new NBTTagCompound();
         emptyJar.writeToNBT(tag);
         stk.setTagInfo("jar", (NBTBase)tag);
       } 
     } else {
       
       TBNetworkManager.playSoundOnServer(w, "thaumicbases:revolver.click", user.posX, user.posY, user.posZ, 3.0F, 2.0F);
     } 
     
     return super.onItemRightClick(stk, w, user);
   }
 
   
   public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
     if (!entityLiving.worldObj.isRemote)
     {
       if (entityLiving instanceof EntityPlayer)
       {
         if (((EntityPlayer)EntityPlayer.class.cast(entityLiving)).openContainer instanceof net.minecraft.inventory.ContainerPlayer && entityLiving.isSneaking())
         {
           ((EntityPlayer)EntityPlayer.class.cast(entityLiving)).openGui(TBCore.instance, 4331801, entityLiving.worldObj, MathHelper.floor_double(entityLiving.posX), MathHelper.floor_double(entityLiving.posY), MathHelper.floor_double(entityLiving.posZ));
         }
       }
     }
     return false;
   }
 
 
   @Override
   public void addInformation(ItemStack stk, EntityPlayer p, List lst, boolean isCurrentItem) {
     super.addInformation(stk, p, lst, isCurrentItem);
     if (stk.hasTagCompound() && stk.getTagCompound().hasKey("jar")) {
       
       ItemStack jar = ItemStack.loadItemStackFromNBT(stk.stackTagCompound.getCompoundTag("jar"));
 
       
       try {
         if (jar.getItem() instanceof ItemJarFilled) {
 
           
           AspectList aspects = ((ItemJarFilled)jar.getItem()).getAspects(jar);
           
           if (aspects != null && aspects.size() > 0)
             for (Aspect tag : aspects.getAspectsSorted())
             { if (Thaumcraft.proxy.playerKnowledge.hasDiscoveredAspect(p.getCommandSenderName(), tag))
               { lst.add(tag.getName() + " x " + aspects.getAmount(tag)); }
               else
               { lst.add(StatCollector.translateToLocal("tc.aspect.unknown")); }  }  
         } 
       } catch (Exception exception) {}
     } 
 
 
     
     if (stk.hasTagCompound() && stk.getTagCompound().hasKey("tb.upgrades")) {
       
       ArrayList<Pair<RevolverUpgrade, Integer>> upgrades = getAllUpgradesFor(stk);
       for (Pair<RevolverUpgrade, Integer> pr : upgrades)
       {
         lst.add(((RevolverUpgrade)pr.getFirst()).getName() + " " + StatCollector.translateToLocal("enchantment.level." + pr.getSecond()));
       }
     } 
   }
 
   
   public EnumRarity getRarity(ItemStack stk) {
     return EnumRarity.rare;
   }
 
 
 
   
   public Multimap getAttributeModifiers(ItemStack stack) {
     HashMultimap retMap = HashMultimap.create();
     
     retMap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (5.0F + (getUpgradeLevel(stack, RevolverUpgrade.heavy) * 2)), 0));
     
     return (Multimap)retMap;
   }
 
   
   public void onUpdate(ItemStack stk, World w, Entity entity, int slot, boolean held) {
     super.onUpdate(stk, w, entity, slot, held);
     int u = getUpgradeLevel(stk, RevolverUpgrade.uvoid);
     if (u > 0 && 
       stk.isItemDamaged() && entity != null && entity.ticksExisted % 100 / u == 0 && entity instanceof EntityLivingBase) {
       stk.damageItem(-1, (EntityLivingBase)entity);
     }
     if (stk.getTagCompound() != null) {
       
       double rotation = stk.getTagCompound().getDouble("barrelRotation");
       double renderedRotation = stk.getTagCompound().getDouble("renderedRotation");
       
       if (rotation >= 3600000.0D) {
         rotation -= 3600000.0D;
       }
       if (renderedRotation >= 3600000.0D) {
         renderedRotation -= 3600000.0D;
       }
       if (renderedRotation < rotation) {
         renderedRotation += 10.0D;
       }
       renderedRotation = Math.min(rotation, renderedRotation);
       
       stk.getTagCompound().setDouble("barrelRotation", rotation);
       stk.getTagCompound().setDouble("renderedRotation", renderedRotation);
     } 
   }
 
 
 
   
   public int getWarp(ItemStack stk, EntityPlayer player) {
     return getUpgradeLevel(stk, RevolverUpgrade.uvoid);
   }
 }


