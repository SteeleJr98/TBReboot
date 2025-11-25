package tb.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import tb.core.TBCore;
import tb.utils.DummySteele;
import tb.utils.TBConfig;
import tb.utils.TBUtils;

public class ItemAttunedCascadePendant extends ItemCascadeDispel {
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
	
	
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (TBCore.isDev) {
			if (!world.isRemote) {
				//TBUtils.sendChatToSender(player, stack.hasTagCompound());
				if (stack.hasTagCompound()) {
					if (stack.getTagCompound().hasKey("dimension")) {
						int stackInt = stack.getTagCompound().getInteger("dimension");
						boolean notBlacklisted = true;
						//DummySteele.sendMessageFromServer("len: " + TBConfig.pendantDimBlacklist.length + " || 0: " + TBConfig.pendantDimBlacklist[0]);

						for (int i = 0; i < TBConfig.pendantDimBlacklist.length; i++) {
							if (player.dimension == TBConfig.pendantDimBlacklist[i]) {
								notBlacklisted = false;
								player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tb.txt.pendantBlacklist")));
								break;
							}
						}
						if (stackInt != player.dimension && player.dimension != TBConfig.cascadeDimID && notBlacklisted) {
							//DummySteele.sendMessageFromServer("Got pendant id of: " + stack.getTagCompound().getInteger("dimension"));
							stack.getTagCompound().setInteger("dimension", player.dimension);
							//DummySteele.sendMessageFromServer("attempted to set to: " + player.dimension);
						}
					}
				}
				else {
					//TBUtils.sendChatToSender(player, "in make tag code");
					NBTTagCompound newTag = new NBTTagCompound();
					newTag.setInteger("dimension", 0);
					stack.writeToNBT(newTag);
				}
			}
		}
		return stack;
	}
	
	public int getDimension(ItemStack stack) {
		if (stack.hasTagCompound()) {
			if (stack.getTagCompound().hasKey("dimension")) {
				return stack.getTagCompound().getInteger("dimension");
			}
			else {
				setDimension(stack, 0);
			}
		}
		else {
			setDimension(stack, 0);
		}
		return 0;
	}
	
	public static void setDimension(ItemStack stack, int dim) {
		NBTTagCompound newTag = new NBTTagCompound();
		newTag.setInteger("dimension", dim);
		stack.setTagCompound(newTag);
	}

}
