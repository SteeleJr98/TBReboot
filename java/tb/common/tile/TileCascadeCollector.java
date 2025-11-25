package tb.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import tb.utils.TBConfig;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.blocks.BlockJarItem;
import thaumcraft.common.blocks.ItemJarFilled;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

public class TileCascadeCollector extends TileEntity implements IInventory {
	
	private static final int jarSlot[] = {0};
	
	private ItemStack[] inventory = new ItemStack[1];
	
	@Override
	public void updateEntity() {
		
	}
	
	public void tryFillJar() {
		if (this.inventory[0] != null) {
			if (this.inventory[0].getItem() instanceof ItemJarFilled) {
				
				
				
				
				
				ItemJarFilled jar = (ItemJarFilled) this.inventory[0].getItem();
				int aspectAmount = jar.getAspects(this.inventory[0]).getAmount(Aspect.WEATHER);
				if (aspectAmount < 64) {
					aspectAmount += TBConfig.collectorAmount;
					if (aspectAmount > 64) {
						aspectAmount = 64;
					}
				}
				AspectList newList = new AspectList();
				newList.add(Aspect.WEATHER, aspectAmount);
				jar.setAspects(this.inventory[0], newList);
			}
			if (this.inventory[0].getItem() instanceof BlockJarItem) {
				ItemStack jarStack = new ItemStack(ConfigItems.itemJarFilled);
				ItemJarFilled newJarFilled = (ItemJarFilled) jarStack.getItem();
				int aspectAmount = TBConfig.collectorAmount;
				AspectList newList = new AspectList();
				newList.add(Aspect.WEATHER, aspectAmount);
				newJarFilled.setAspects(jarStack, newList);
				this.inventory[0] = jarStack;
			}
			this.markDirty();
		}
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int num) {
		if (this.inventory[slot] != null) {



			if (this.inventory[slot].stackSize <= num) {

				ItemStack itemStack = this.inventory[slot];
				this.inventory[slot] = null;
				markDirty();
				return itemStack;
			} 


			ItemStack itemstack = this.inventory[slot].splitStack(num);

			if (this.inventory[slot].stackSize == 0) {
				this.inventory[slot] = null;
			}

			markDirty();
			return itemstack;
		} 



		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return this.inventory[slot];
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.inventory[slot] = stack;

	}

	@Override
	public String getInventoryName() {
		return "tb.cascadeCollector";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return (player.dimension == this.worldObj.provider.dimensionId && this.worldObj.blockExists(this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		boolean fillCheck = false;
		if (stack.getItem() instanceof ItemJarFilled) {
			ItemJarFilled jar = (ItemJarFilled) stack.getItem();
			if (jar.getAspects(stack).getAmount(Aspect.WEATHER) > 0) {
				fillCheck = true;
			}
		}
		return (stack.getItem() instanceof BlockJarItem || fillCheck);
	}

}
