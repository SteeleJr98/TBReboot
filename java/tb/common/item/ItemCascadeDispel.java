package tb.common.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import tb.init.TBBlocks;
import tb.utils.TBConfig;

public class ItemCascadeDispel extends Item implements IBauble {
	
	public ItemCascadeDispel() {
		setMaxStackSize(1);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int p_77648_7_, float hitX, float hitY, float hitZ) {
		
		Block targetBlock = world.getBlock(x, y, z);
		
		if (targetBlock == TBBlocks.cascadeBlock) {
			if (player.dimension != TBConfig.cascadeDimID) {
				if (!world.isRemote) {
					world.playSoundEffect(x, y, z, "thaumcraft:jar4", 1.0F, 1.0F);
					world.setBlockToAir(x, y, z);
				}
				return true;
			}
			else {
				if (!world.isRemote) {
					world.playSoundEffect(x, y, z, "thaumcraft:craftfail", 1.0F, 1.0F);
				}
			}
		}
		
		return false;
	}

	@Override
	public boolean canEquip(ItemStack stack, EntityLivingBase entity) {
		return entity instanceof EntityPlayer;
	}

	@Override
	public boolean canUnequip(ItemStack stack, EntityLivingBase entity) {
		return true;
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.AMULET;
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase entity) {
		if (!entity.worldObj.isRemote) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "" + StatCollector.translateToLocal("tb.txt.pendant.equip")));
			}
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase entity) {
		if (!entity.worldObj.isRemote) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "" + StatCollector.translateToLocal("tb.txt.pendant.unequip")));
			}
		}
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase entity) {}
	
	
}
