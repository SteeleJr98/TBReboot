package tb.common.item.foci;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.*;

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
import net.minecraft.util.StatCollector;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import tb.core.TBCore;
import tb.init.TBFociUpgrades;
import tb.utils.DummySteele.MystColour;
import tb.utils.TBConfig;
import tb.utils.TBUtils;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.blocks.BlockJar;
import thaumcraft.common.blocks.BlockJarItem;
import thaumcraft.common.blocks.ItemJarFilled;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

public class FociDecompose extends ItemFocusBasic {


	private float aspectLoss = TBConfig.decompGain;

	public int getFocusColor(ItemStack focusstack) {
		return (new MystColour(117, 135, 153)).asInt();
		//return (new MystColour(9, 14, 75)).asInt();
		//return (new MystColour(0, 0, 0)).asInt();
	}

	public String getSortingHelper(ItemStack focusstack) {
		String out = "DP";
		for (short id : getAppliedUpgrades(focusstack)) {
			out = out + id;
		}
		return out;
	}
	
	public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack stack, int rank) {
		if (rank == 3 || rank == 4) {
			return new FocusUpgradeType[] {FocusUpgradeType.frugal, TBFociUpgrades.spillage};
		}
		else {
			return new FocusUpgradeType[] {FocusUpgradeType.frugal};
		}
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
				Double blockRange = 1.4D;
				//player.addChatComponentMessage(new ChatComponentText("Block: " + world.getBlock(mov.blockX, mov.blockY, mov.blockZ).getLocalizedName()));
				AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(mov.blockX - blockRange, mov.blockY - blockRange, mov.blockZ - blockRange, mov.blockX + blockRange, mov.blockY + 2 + blockRange, mov.blockZ + blockRange);
				List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, aabb);
				List<EntityItem> goodStackSize = new ArrayList() {};

				if (itemList.size() > 0) {
					for (EntityItem ei : itemList) {
						if (ei.getEntityItem().stackSize <= 5) {
							goodStackSize.add(ei);
						}
					}
					if (goodStackSize.size() > 0) {
						moveAspectsToInvJars(player, goodStackSize, wandstack);
					}
					else {
						player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal(itemList.size() > 1 ? "tb.txt.stackTooLarge0" : "tb.txt.stackTooLarge1")));
					}
				}
				else {

					player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("tb.txt.noItems")));
				}
			}
		}
		return wandstack;
	}

	public void moveAspectsToInvJars(EntityPlayer player, List<EntityItem> items, ItemStack wandStack) {

		AspectList aList = new AspectList();
		for (EntityItem ei : items) {

			AspectList tempList = new AspectList(ei.getEntityItem());

			for (Aspect tempAspect : tempList.aspects.keySet()) {
				if (tempAspect != null) {
					aList.add(tempAspect, tempList.getAmount(tempAspect) * ei.getEntityItem().stackSize);
				}
			}

			//aList.add(new AspectList(ei.getEntityItem()));


			player.worldObj.playSoundAtEntity(player, "random.fizz", 1, 1);
			ei.setDead();
		}
		List<Integer> jarFillableSlots = getSlots(player, false);
		List<Integer> jarEmptySlots = getSlots(player, true);
		List<Aspect> toRemove = new ArrayList<Aspect>();
		
		ItemWandCasting wand = (ItemWandCasting)wandStack.getItem();
		
		int bonusAmount = 0;
		if (isUpgradedWith(wand.getFocusItem(wandStack), TBFociUpgrades.spillage)) {
			int upgradeLevel = getUpgradeLevel(wand.getFocusItem(wandStack), TBFociUpgrades.spillage);
			bonusAmount = upgradeLevel == 1 ? 5 : 15;
		}

		//		for (Aspect a : aList.aspects.keySet()) {
		//			if (a != null) {
		//				TBUtils.sendChatToSender(player, "Aspect: " + a.getName() + " Amount: " + aList.getAmount(a));
		//			}
		//		}
		if (jarFillableSlots.size() > 0) {
			for (Aspect aspect : aList.aspects.keySet()) {
				for (int i = 0; i < jarFillableSlots.size(); i++) {
					ItemStack jarStack = player.inventory.getStackInSlot(jarFillableSlots.get(i));
					ItemJarFilled jarItem = (ItemJarFilled) jarStack.getItem();
					AspectList jarAspect = jarItem.getAspects(jarStack);
					int aspectAmount = jarAspect.getAmount(aspect);
					if (jarAspect.getAspects()[0] == aspect) {
						if (aspectAmount < 64) {
							aspectAmount += Math.round(aList.getAmount(aspect) * ((this.aspectLoss + bonusAmount)/100));
							if (aspectAmount > 64) {
								aspectAmount = 64;
							}
							AspectList newList = new AspectList();
							newList.add(aspect, aspectAmount);
							jarItem.setAspects(jarStack, newList);
							toRemove.add(aspect);
							break;
						}
					}
				}
			}

			for (Aspect a : toRemove) {
				aList.remove(a);
				//TBUtils.sendChatToSender(player, "removed " + a.getName() + " from aList");
			}

		}
		else {
			//TBUtils.sendChatToSender(player, "no 'fillable' jars");
		}
		if (jarEmptySlots.size() > 0) {
			int jarSlot = jarEmptySlots.get(0);
			for (Aspect aspect : aList.aspects.keySet()) {
				if (player.inventory.getStackInSlot(jarEmptySlots.get(0)) != null) {

					ItemStack jarStack = new ItemStack(ConfigItems.itemJarFilled);
					ItemJarFilled newJarFilled = (ItemJarFilled) jarStack.getItem();

					
					int aspectAmount = Math.round(aList.getAmount(aspect) * ((this.aspectLoss + bonusAmount)/100));
					
					
					if (aspectAmount > 64) {
						aspectAmount = 64;
					}
					AspectList newList = new AspectList();
					newList.add(aspect, aspectAmount);
					newJarFilled.setAspects(jarStack, newList);
					int emptySlot = player.inventory.getFirstEmptyStack();
					if (emptySlot != -1) {
						player.inventory.setInventorySlotContents(emptySlot, jarStack);
					}
					else {
						EntityItem itemEntity = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ);
						itemEntity.setEntityItemStack(jarStack);
						if (!player.worldObj.isRemote) {
							player.worldObj.spawnEntityInWorld(itemEntity);
						}
					}
					toRemove.add(aspect);
					if (player.inventory.getStackInSlot(jarSlot).stackSize > 1) {
						player.inventory.getStackInSlot(jarSlot).stackSize--;
					}
					else {
						player.inventory.setInventorySlotContents(jarSlot, null);
					}

				}
			}
		}
		else {
			player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("tb.txt.wasteItems")));
		}
	}

	private static List<Integer> getSlots(EntityPlayer player, boolean jarEmpty) {

		List<Integer> returnSlots = new ArrayList<Integer>();
		Class jarClass = jarEmpty ? BlockJarItem.class : ItemJarFilled.class;
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			if (player.inventory.getStackInSlot(i) != null) {
				if (jarClass.isInstance(player.inventory.getStackInSlot(i).getItem())) {
					returnSlots.add(i);
				}
			}
		}

		return returnSlots;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		super.registerIcons(reg);
		this.icon = reg.registerIcon(getIconString());
	}

}
