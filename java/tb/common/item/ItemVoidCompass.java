package tb.common.item;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import tb.dimension.WorldProviderCascade;
import tb.init.TBItems;
import tb.utils.DummySteele;
import tb.utils.TBConfig;
import tb.utils.TBUtils;

public class ItemVoidCompass extends Item {
	
	public ItemVoidCompass() {
		setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		
		if (!world.isRemote) {
			if (player.dimension == TBConfig.cascadeDimID) {
				
				double movementFactor = WorldProviderCascade.getProviderForDimension(TBConfig.cascadeDimID).getMovementFactor();
				int newPx = (int) (player.posX * movementFactor);
				int newPz = (int) (player.posZ * movementFactor);
				
				IInventory bInventory = BaublesApi.getBaubles(player);
				boolean hasBaublePendant = false;
				for (int i = 0; i < bInventory.getSizeInventory(); i++) {
					if (bInventory.getStackInSlot(i) != null) {
						if (bInventory.getStackInSlot(i).getItem() instanceof ItemAttunedCascadePendant) {
							hasBaublePendant = true;
							break;
							//dim = ((ItemAttunedCascadePendant) bInventory.getStackInSlot(i).getItem()).getDimension(bInventory.getStackInSlot(i));
							//DummySteele.sendMessageFromServer("found baubles slot pendant");
						}
					}
				}
				
				String attunedString = (TBUtils.getFirstSlotWithItem(TBItems.attunedCascadePendant, player.inventory.mainInventory) >= 0 || hasBaublePendant) ? (" " + StatCollector.translateToLocal("tb.txt.compassUsePendant")) : "";
				
				player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("tb.txt.compassUse1") + " " + newPx + " " + newPz + attunedString));
			}
		}
		
        return stack;
    }

}
