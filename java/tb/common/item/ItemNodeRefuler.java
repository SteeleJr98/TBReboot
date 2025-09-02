package tb.common.item;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import tb.utils.DummySteele;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
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
			if (player.isSneaking()) {
				int typeInt = player.inventory.getStackInSlot(0).stackSize;
				
				if (typeInt == 1) {
					node.setNodeType(NodeType.NORMAL);
				}
				if (typeInt == 2) {
					node.setNodeType(NodeType.UNSTABLE);
				}
				if (typeInt == 3) {
					node.setNodeType(NodeType.DARK);
				}
				if (typeInt == 4) {
					node.setNodeType(NodeType.TAINTED);
				}
				if (typeInt == 5) {
					node.setNodeType(NodeType.HUNGRY);
				}
				if (typeInt == 6) {
					node.setNodeType(NodeType.PURE);
				}
				
				int modInt = player.inventory.getStackInSlot(1).stackSize;
				
				
				if (modInt == 1) {
					node.setNodeModifier(NodeModifier.BRIGHT);
				}
				if (modInt == 2) {
					node.setNodeModifier(NodeModifier.PALE);
				}
				if (modInt == 3) {
					node.setNodeModifier(NodeModifier.FADING);
				}
				
			}
			else {
				AspectList refillList = new AspectList();
				refillList.add(Aspect.AIR, 64*2).add(Aspect.EARTH, 128).add(Aspect.ORDER, 128).add(Aspect.FIRE, 128).add(Aspect.ENTROPY, 128).add(Aspect.WATER, 128);
				
				
				
				refillList.add(Aspect.DARKNESS, 128).add(Aspect.VOID, 128);
				node.setAspects(refillList);
			}
			//player.inventory.setInventorySlotContents(0, new ItemStack(Items.apple, 64));
			//player.inventory.setInventorySlotContents(1, new ItemStack(Items.apple, 64));
			TileEntity testTile = (TileEntity) node;
			player.worldObj.markBlockForUpdate(coords.posX, coords.posY, coords.posZ);
		}
	}



}
