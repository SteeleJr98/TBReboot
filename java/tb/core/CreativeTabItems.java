package tb.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import tb.init.TBItems;

public class CreativeTabItems {
	public static final CreativeTabs tbTabItems = new CreativeTabs(TBCore.modid.toLowerCase() + ".items.name") {

		@Override
		public Item getTabIconItem() {
			// TODO Auto-generated method stub
			return TBItems.tobaccoSeeds;
		}
	};

}
