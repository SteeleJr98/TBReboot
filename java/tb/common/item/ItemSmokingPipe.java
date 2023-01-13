 package tb.common.item;
 
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.EnumAction;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.Vec3;
 import net.minecraft.world.World;

import tb.api.ITobacco;
 
 
 public class ItemSmokingPipe
   extends Item
 {
   public boolean isSilverwood;
   
   public ItemSmokingPipe(boolean silverwood) {
     this.isSilverwood = silverwood;
     setFull3D();
     setMaxStackSize(1);
   }
 
   
   public ItemStack getTobacco(EntityPlayer p) {
     for (int i = 0; i < p.inventory.getSizeInventory(); i++) {
       
       ItemStack stk = p.inventory.getStackInSlot(i);
       if (stk != null && stk.getItem() != null && stk.getItem() instanceof ITobacco)
         return stk; 
     } 
     return null;
   }
 
   
   @Override
   public ItemStack onItemRightClick(ItemStack stack, World w, EntityPlayer player) {
     if (getTobacco(player) != null)
       player.setItemInUse(stack, getMaxItemUseDuration(stack)); 
     return stack;
   }
 
   
   @Override
   public EnumAction getItemUseAction(ItemStack stack) {
     return EnumAction.bow;
   }
 
   
   @Override
   public int getMaxItemUseDuration(ItemStack stack) {
     return 64;
   }
 
   
   @Override
   public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
     Vec3 look = player.getLookVec();
     double x = player.posX + look.xCoord / 5.0D;
     double y = player.posY + player.getEyeHeight() + look.yCoord / 5.0D;
     double z = player.posZ + look.zCoord / 5.0D;
     if (count < 32) {
       player.worldObj.spawnParticle(this.isSilverwood ? "explode" : "smoke", x, y, z, look.xCoord / 10.0D, look.yCoord / 10.0D, look.zCoord / 10.0D);
     }
   }
   
   @Override
   public ItemStack onEaten(ItemStack stack, World w, EntityPlayer player) {
     ItemStack tobacco = getTobacco(player);
     ITobacco t = ITobacco.class.cast(tobacco.getItem());
     //MessageLogging.sendFromServer("Item Cast to ITobacco");
     t.performTobaccoEffect(player, tobacco.getItemDamage(), this.isSilverwood);
     for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
       
       ItemStack stk = player.inventory.getStackInSlot(i);
       if (stk != null && stk.getItem() != null && stk.getItem() instanceof ITobacco) {
         
         player.inventory.decrStackSize(i, 1);
         break;
       } 
     } 
     Vec3 look = player.getLookVec();
     for (int j = 0; j < 100; j++) {
       
       double x = player.posX + look.xCoord / 5.0D;
       double y = player.posY + player.getEyeHeight() + look.yCoord / 5.0D;
       double z = player.posZ + look.zCoord / 5.0D;
       
       player.worldObj.spawnParticle(this.isSilverwood ? "explode" : "smoke", x, y, z, look.xCoord / 10.0D, look.yCoord / 10.0D, look.zCoord / 10.0D);
     } 
     
     return stack;
   }
 }


