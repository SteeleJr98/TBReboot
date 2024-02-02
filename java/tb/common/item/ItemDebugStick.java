package tb.common.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import tb.utils.TBUtils;

public class ItemDebugStick extends Item {
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
	
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int p_77648_7_, float hitX, float hitY, float hitZ) {
		
		Block block = world.getBlock(x, y, z);
		if (!world.isRemote) {
			
			if (!(world.getBlock(x, y, z) == Blocks.end_stone)) {
				TBUtils.sendChatToSender(player, "Block Class: " + block.getClass().toString());
				TBUtils.sendChatToSender(player, Item.getItemFromBlock(block).getClass().toString());
				TBUtils.sendChatToSender(player, "Block Meta: " + world.getBlockMetadata(x, y, z));
			}
			else {
				AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(x - 5, y - 5, z - 5, x + 5, y + 5, z + 5);
				List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, aabb);
				if (!(list.size() == 0)) {
					for (EntityItem e : list) {
						int damage = e.getEntityItem().getItemDamage();
						TBUtils.sendChatToSender(player, "Item class: " + TBUtils.getClassLastString(e.getEntityItem().getItem().getClass().toString()));
					}
				}
				else {
					TBUtils.sendChatToSender(player, "No Entity Items found");
				}
			}
			
			
		}
		
		
		return true;
	}

}
