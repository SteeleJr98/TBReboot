package tb.common.tile;

import net.minecraft.item.ItemStack;
import thaumcraft.common.tiles.TileArcaneWorkbench;

public class FakeWorkbenchTile extends TileArcaneWorkbench {
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.stackList[slot] = stack;
		this.eventHandler.onCraftMatrixChanged(this);
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int size) {
		if(this.stackList[slot] != null) {
			if(this.stackList[slot].stackSize < size) {
				this.stackList[slot] = null;
				this.eventHandler.onCraftMatrixChanged(this);
				return this.stackList[slot];
			}
		}
		
		ItemStack tempStack = this.stackList[slot].splitStack(size);
		if(this.stackList[slot].stackSize == 0) {
			this.stackList[slot] = null;
		}
		
		this.eventHandler.onCraftMatrixChanged(this);
		return tempStack;
	}

}
