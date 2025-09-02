package tb.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import tb.init.TBBlocks;

public class CreativeTabBlocks {
	public static final CreativeTabs tbTabBlocks = new CreativeTabs(TBCore.modid.toLowerCase() + ".blocks.name") {

		@Override
		public Item getTabIconItem() {
			// TODO Auto-generated method stub
			return Item.getItemFromBlock(TBBlocks.voidAnvil);
		}
	};

}
