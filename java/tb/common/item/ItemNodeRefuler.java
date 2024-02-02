package tb.common.item;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import tb.utils.DummySteele;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileNode;

public class ItemNodeRefuler extends Item {
	
	private int cooldown = 0;
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World w, EntityPlayer player) {
	    if (!w.isRemote) {
			if (this.cooldown == 0) {
		    	this.cooldown = 60;
		    	refillNodes(player);
		    }
	    }
		
		
	     return stack;
	}
	
	@Override
	public void onUpdate(ItemStack p_77663_1_, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
		if (this.cooldown > 0) {
			this.cooldown--;
		}
	}
	
	public void refillNodes(EntityPlayer player) {
		int x = (int) player.posX;
		int y = (int) player.posY;
		int z = (int) player.posZ;
		ArrayList<ChunkCoordinates> testBlocks = DummySteele.findBlocks(player.worldObj, x, y, z, 10, ConfigBlocks.blockAiry);
		if (testBlocks.size() > 0) {
			ChunkCoordinates coords = testBlocks.get(0);
			TileNode node = (TileNode) player.worldObj.getTileEntity(coords.posX, coords.posY, coords.posZ);
			AspectList refillList = new AspectList();
			refillList.add(Aspect.AIR, 64);
			node.setAspects(refillList);
		}
	}
	
	
	
}
