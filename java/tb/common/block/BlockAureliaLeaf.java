package tb.common.block;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import tb.init.TBItems;


public class BlockAureliaLeaf extends Block {
	
	public BlockAureliaLeaf() {
		super(Material.plants);
		setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.25F, 0.75F);
		setLightLevel(0.3F);
	}


	public int damageDropped(int meta) {
		return 5;
	}


	public Item getItemDropped(int meta, Random rnd, int fort) {
		return TBItems.resource;
	}


	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		return null;
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {

		if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemShears) {
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			ItemStack itemstack = this.createStackedBlock(meta);

			if (itemstack != null) {
				items.add(itemstack);
			}

			ForgeEventFactory.fireBlockHarvesting(items, world, this, x, y, z, meta, 0, 1.0f, true, player);
			for (ItemStack is : items) {
				this.dropBlockAsItem(world, x, y, z, is);
			}
		}
		else {
			super.harvestBlock(world, player, x, y, z, meta);
		}

	}

	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
		return true;
	}


	public boolean isOpaqueCube() {
		return false;
	}


	public boolean renderAsNormalBlock() {
		return false;
	}


	public int getRenderType() {
		return 1;
	}
}


