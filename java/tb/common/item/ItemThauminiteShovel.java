package tb.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import thaumcraft.api.IRepairable;

public class ItemThauminiteShovel extends ItemSpade implements IRepairable {
	
	public ItemThauminiteShovel(Item.ToolMaterial mat) {
		super(mat);
	}
}


