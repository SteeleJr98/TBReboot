package tb.common.inventory;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSneakyGrab extends Container {

	private EntityPlayer targetPlayer;

	public ContainerSneakyGrab(InventoryPlayer player, EntityPlayer target) {
		this.targetPlayer = target;

		int i;
		byte b0 = 51;



		for (i = 0; i < 5; i++) {
			this.addSlotToContainer(new Slot((IInventory)target, i, 44 + i * 18, 20));
		}



		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, i * 18 + b0));
			}
		}

		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 58 + b0));
		}

	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return true;
	}



}
