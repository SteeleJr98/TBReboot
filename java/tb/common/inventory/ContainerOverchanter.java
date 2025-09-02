package tb.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerOverchanter extends Container {
	
	public ContainerOverchanter(InventoryPlayer playerInv, TileEntity blockInv) {
		addSlotToContainer(new Slot((IInventory)blockInv, 0, 80, 20));

		int i;
		for (i = 0; i < 3; i++) {

			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot((IInventory)playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		} 

		for (i = 0; i < 9; i++) {
			addSlotToContainer(new Slot((IInventory)playerInv, i, 8 + i * 18, 142));
		}
	}


	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}


	public ItemStack transferStackInSlot(EntityPlayer player, int slotClicked) {

		ItemStack stack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotClicked);

		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			if (slotClicked == 0) {
				if (!this.mergeItemStack(stack1, 1, 37, true)) {
					return null;
				}
				slot.onSlotChange(stack1, stack);
			}

			else if (slotClicked != 0) {
				if (!this.mergeItemStack(stack1, 0, 1, false)) {
					return null;
				}

			}
			else if (!this.mergeItemStack(stack1, 1, 37, false)) {
				return null;
			}


			if (stack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			}
			else {
				slot.onSlotChanged();
			}

			//			if (stack1.stackSize == stack.stackSize) {
			//				return null;
			//			}
			//			
			//			slot.onPickupFromSlot(player, stack1);
		}


		return stack;
	}
}


