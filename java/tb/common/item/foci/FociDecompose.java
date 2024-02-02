package tb.common.item.foci;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import tb.core.TBCore;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.blocks.BlockJar;
import thaumcraft.common.blocks.BlockJarItem;
import thaumcraft.common.blocks.ItemJarFilled;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

public class FociDecompose extends ItemFocusBasic {
	

	public int getFocusColor(ItemStack focusstack) {
	     return 16318428;
	   }
	   
	   public String getSortingHelper(ItemStack focusstack) {
	     String out = "DP";
	     for (short id : getAppliedUpgrades(focusstack)) {
	       out = out + id;
	     }
	     return out;
	   }
	   
	   public boolean isVisCostPerTick(ItemStack focusstack) {
		   return false;
	   }
	   
	   
	   
	   public AspectList getVisCost(ItemStack focusstack) {
		   return (new AspectList()).add(Aspect.ORDER, 500).add(Aspect.ENTROPY, 500);
	   }
		 
		   
	   public ItemFocusBasic.WandFocusAnimation getAnimation(ItemStack focusstack) {
		   return ItemFocusBasic.WandFocusAnimation.CHARGE;
	   }
	   
	   @Override
	   public ItemStack onFocusRightClick(ItemStack wandstack, World world, EntityPlayer player, MovingObjectPosition mov) {
		   if (!world.isRemote) {
			   if (mov != null) {
				   //player.addChatComponentMessage(new ChatComponentText("Block: " + world.getBlock(mov.blockX, mov.blockY, mov.blockZ).getLocalizedName()));
				   AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(mov.blockX - 2, mov.blockY - 2, mov.blockZ - 2, mov.blockX + 2, mov.blockY + 2, mov.blockZ + 2);
				   List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, aabb);
				   AspectList tempAspectList = new AspectList();
				   AspectList itemAspectList = new AspectList();
				   if (itemList.size() > 0) {
					   for (int j = 0; j < itemList.size(); j++) {
						   
						   
						   player.addChatComponentMessage(new ChatComponentText("Item " + j + " : " + ((EntityItem) itemList.get(j)).getEntityItem().getDisplayName()));
						   
						   itemAspectList = ThaumcraftCraftingManager.getObjectTags(((EntityItem) itemList.get(j)).getEntityItem());
						   if (itemAspectList != null && !dumbAspectChecker(itemAspectList.getAspects())) {
							   Aspect[] aspects = itemAspectList.getAspects();
							   player.addChatComponentMessage(new ChatComponentText("Aspect list: " + aspects.length));
							   tempAspectList.add(itemAspectList);
							   Aspect[] itemAspects = tempAspectList.getAspects();
							   for (int i = 0; i < itemAspects.length; i++) {
								   int tempAmount = tempAspectList.getAmount(itemAspects[i]);
								   tempAmount *= ((EntityItem) itemList.get(j)).getEntityItem().stackSize;
								   player.addChatComponentMessage(new ChatComponentText("Aspect: " + itemAspects[i].getName() + "||||Amount: " + tempAmount));
							   }
							   
						   }
						   else {
							   player.addChatComponentMessage(new ChatComponentText("No aspects found in item"));
						   }
						   
						   
						   
					   }
					   
					   moveAspectsToInvJars(player, itemList, tempAspectList);
				   }
				   
			   }
		   }
		   return wandstack;
	   }
	
	   private boolean dumbAspectChecker(Aspect[] aspects) {
		   
		   for (Aspect aspect : aspects) {
			   if (aspect == null) {
				   return true;
			   }
		   }
		   
		   return false;
	   }
	   
	   public void moveAspectsToInvJars(EntityPlayer player, List<EntityItem> items, AspectList aspects) {
		   player.worldObj.playSoundAtEntity(player, "random.fizz", 1, 1);
		   for (EntityItem ei : items) {
			   //ei.setDead();
		   }
		   
		   ArrayList<Integer> emptyJarSlots = new ArrayList<Integer>();
		   ArrayList<Integer> filledJarSlots = new ArrayList<Integer>();
		   InventoryPlayer pInv = player.inventory;
		   for (int i = 0; i < pInv.getSizeInventory(); i++) {
			   ItemStack stack = pInv.getStackInSlot(i);
			   if (stack != null) {
				   if (stack.getItem() instanceof BlockJarItem) {
					   player.addChatComponentMessage(new ChatComponentText("Empty jar found in slot " + i + " with class " + stack.getItem().getClass().toString()));
					   emptyJarSlots.add(i);
				   }
				   if (stack.getItem() instanceof ItemJarFilled) {
					   ItemJarFilled tempJar = (ItemJarFilled) stack.getItem();
					   player.addChatComponentMessage(new ChatComponentText("Jar with aspect " + tempJar.getAspects(stack).getAspects()[0].getName() + " found in slot " + i + " with class " + stack.getItem().getClass().toString()));
					   filledJarSlots.add(i);
				   }
			   }
			   
		   }
		   
		   Aspect[] allAspects = aspects.getAspects();
		   for (Aspect a : allAspects) {
			   for (int i : filledJarSlots) {
				   ItemJarFilled tempJarFilled = (ItemJarFilled) pInv.getStackInSlot(i).getItem();
				   ItemStack stack = pInv.getStackInSlot(i);
				   if ((tempJarFilled.getAspects(stack).getAspects()[0]) == a) {
					   player.addChatComponentMessage(new ChatComponentText("found matching jar with aspect: " + a.getName()));
					   int aspectAmount = aspects.getAmount(a);
					   int jarAmount = tempJarFilled.getAspects(stack).getAmount(a);
					   //int jarMax = 64
					   AspectList tempList = new AspectList();
					   
					   if (aspectAmount + jarAmount > 64) {
						   
						   tempList.add(a, 64);
						   player.addChatComponentMessage(new ChatComponentText("setting aspect " + a.getName() + " to 64"));
						   
					   }
					   else {
						   tempList.add(a, aspectAmount + jarAmount);
						   player.addChatComponentMessage(new ChatComponentText("setting aspect " + a.getName() + " to " + (aspectAmount + jarAmount)));
						   
					   }					   
					   
					   
					   tempJarFilled.setAspects(stack, tempList);
					   pInv.setInventorySlotContents(i, new ItemStack(tempJarFilled));
					   aspects.remove(a);
				   }
			   }
		   }
		   
	   }
	   
	   @SideOnly(Side.CLIENT)
	   public void registerIcons(IIconRegister reg) {
	     super.registerIcons(reg);
	     this.icon = reg.registerIcon(getIconString());
	   }

}
