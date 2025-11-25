package tb.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import tb.common.tile.TileCascadeCollector;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.blocks.BlockJarItem;
import thaumcraft.common.blocks.ItemJarFilled;

public class ContainerCascadeCollector extends Container {
	
	private TileCascadeCollector tile;
	private IInventory input;
	
	public ContainerCascadeCollector(InventoryPlayer playerInv, TileEntity tileInv) {
		this.tile = (TileCascadeCollector) tileInv;
		this.input = (IInventory) tileInv;
		
		addSlotToContainer(new JarSlot((IInventory)tileInv, 0, 80, 24));
		
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

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer p, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) this.inventorySlots.get(slot);

		if (slotObject != null && slotObject.getHasStack()) {

			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			if (slot == 0) {

				if (!this.input.isItemValidForSlot(slot, stackInSlot) || !mergeItemStack(stackInSlot, 1, this.inventorySlots.size(), true)) {
					return null;
				}
			}
			else if (!this.input.isItemValidForSlot(slot, stackInSlot) || !mergeItemStack(stackInSlot, 0, 1, false)) {
				return null;
			} 
			if (stackInSlot.stackSize == 0) {
				slotObject.putStack(null);
			}
			else {
				slotObject.onSlotChanged();
			} 
		} 
		return stack;
	}
	
	class JarSlot extends Slot {

		public JarSlot(IInventory tileInventory, int slotNum, int x, int y) {
			super(tileInventory, slotNum, x, y);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			
			if (stack.getItem() instanceof BlockJarItem) {
				return true;
			}
			if (stack.getItem() instanceof ItemJarFilled) {
				ItemJarFilled jar = (ItemJarFilled) stack.getItem();
				if (jar.getAspects(stack).getAspects()[0] == Aspect.WEATHER) {
					return true;
				}
			}
			
			return false;
		}
		
	}

}
