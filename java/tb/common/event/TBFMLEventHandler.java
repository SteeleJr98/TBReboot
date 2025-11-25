package tb.common.event;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import tb.common.item.ItemAttunedCascadePendant;
import tb.init.TBItems;
import tb.utils.DummySteele;

public class TBFMLEventHandler {

	@SubscribeEvent
	public void pendantAssignDim(cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent event) {
		//DummySteele.sendMessageFromServer("craft of " + event.crafting.getItem().getUnlocalizedName() + " done");
		if (event.crafting.getItem() == TBItems.attunedCascadePendant) {
			ItemAttunedCascadePendant.setDimension(event.crafting, event.player.dimension);
		}
	}
}
