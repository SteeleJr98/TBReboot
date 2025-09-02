package tb.common.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import tb.common.tile.TileAutoDeconstructor;

public class ContainerAutoDeconstructor extends Container {
	
	private TileAutoDeconstructor tile;
	private int timer;
	
	
	public ContainerAutoDeconstructor(InventoryPlayer playerInv, TileEntity tileInv) {
		this.tile = (TileAutoDeconstructor) tileInv;
		
		addSlotToContainer(new Slot((IInventory)tileInv, 0, 53, 30));
		addSlotToContainer(new ContainerAutoDeconstructor.FinishedSlot((IInventory)tileInv, 1, 105, 30));
	
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
	
	public void addCraftingToCrafters(ICrafting p_75132_1_) {
        super.addCraftingToCrafters(p_75132_1_);
        p_75132_1_.sendProgressBarUpdate(this, 0, this.tile.getTimer());
    }
	
	@Override
	public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if (this.timer != this.tile.getTimer()) {
                icrafting.sendProgressBarUpdate(this, 0, this.tile.getTimer());
            }
        }

        this.timer = this.tile.getTimer();
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			this.tile.setTimer(par2);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer p, int qSlot) {
		ItemStack stack = null;
		Slot slot = (Slot) this.inventorySlots.get(qSlot);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack stack2 = slot.getStack();
			stack = stack2.copy();
			
			
			if (qSlot == 1) {
                if (!this.mergeItemStack(stack2, 2, 38, true)) {
                    return null;
                }
                slot.onSlotChange(stack2, stack);
            }
			
            else if (qSlot != 0 && qSlot != 1) {
            	if (!this.mergeItemStack(stack2, 0, 1, false)) {
            		return null;
            	}
            }
			
            else if (!this.mergeItemStack(stack2, 2, 38, false)) {
            	return null;
            }

			
		
			if (stack2.stackSize == 0) {
				slot.putStack((ItemStack)null);
			}
			else {
				slot.onSlotChanged();
			}
		}
		
		
		return stack;
	}
	
	class FinishedSlot extends Slot {

		public FinishedSlot(IInventory tileInventory, int slotNum, int x, int y) {
			super(tileInventory, slotNum, x, y);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return false;
		}
		
	}

}
