package tb.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import tb.common.tile.FakeWorkbenchTile;
import thaumcraft.common.container.ContainerArcaneWorkbench;
import thaumcraft.common.tiles.TileArcaneWorkbench;

public class FociCrafterContainer extends ContainerArcaneWorkbench {
	
	private FakeWorkbenchTile tile;

	public FociCrafterContainer(InventoryPlayer pInv, FakeWorkbenchTile te) {
		super(pInv, (TileArcaneWorkbench) te);
		this.tile = te;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		for(int i = 0; i < this.tile.getSizeInventory(); i++) {
			
		}
	}
	
	public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer player) {
		if (par3 == 4) {
			par2 = 1;
			return super.slotClick(par1, par2, par3, player);
		} 
		if ((par1 == 0 || par1 == 1) && par2 > 0) par2 = 0; 
		
		
		ItemStack stack = this.tile.stackList[0];
		player.addChatComponentMessage(new ChatComponentText(stack != null ? stack.getUnlocalizedName() : "No item"));
		
		
		
		
		
		
     return super.slotClick(par1, par2, par3, player);
   }

}
