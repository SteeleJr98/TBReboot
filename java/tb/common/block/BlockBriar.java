package tb.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import tb.init.TBItems;

public class BlockBriar extends BlockBush implements IGrowable {
	
	private IIcon[] doublePlantBottomIcons;
	private IIcon[] doublePlantTopIcons;
	public int growthStages;
	public int growthDelay;

	public BlockBriar(int stages, int g) {
		super(Material.plants);
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		this.growthStages = stages;
		this.growthDelay = g;
	}


	public int getRenderType() {
		return 6;
	}


	public void updateTick(World w, int x, int y, int z, Random rnd) {
		super.updateTick(w, x, y, z, rnd);

		if (w.getBlock(x, y, z) == this) {

			int meta = w.getBlockMetadata(x, y, z);
			if (isTopBlock(meta)) {

				if (meta - 8 < this.growthStages - 1 && w.rand.nextInt(this.growthDelay) == 0) {
					w.setBlockMetadataWithNotify(x, y, z, meta + 1, 3);
					w.setBlockMetadataWithNotify(x, y - 1, z, meta - 7, 3);
				}

			}
			else if (meta < this.growthStages - 1 && w.rand.nextInt(this.growthDelay) == 0) {

				w.setBlockMetadataWithNotify(x, y, z, meta + 1, 3);
				w.setBlockMetadataWithNotify(x, y + 1, z, meta + 9, 3);
			} 
		} 
	}



	public void setBlockBoundsBasedOnState(IBlockAccess w, int x, int y, int z) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}


	public boolean canPlaceBlockAt(World w, int x, int y, int z) {
		return (super.canPlaceBlockAt(w, x, y, z) && w.isAirBlock(x, y + 1, z));
	}


	protected void checkAndDropBlock(World w, int x, int y, int z) {
		int meta = w.getBlockMetadata(x, y, z);
		if (isTopBlock(meta)) {

			if (w.getBlock(x, y - 1, z) != this) {

				w.setBlockToAir(x, y, z);
				dropBlockAsItem(w, x, y, z, meta, 0);
			} 
		}
		else {

			if (w.getBlock(x, y + 1, z) != this) {

				dropBlockAsItem(w, x, y, z, meta, 0);
				w.setBlockToAir(x, y, z);
				return;
			} 
			if (!canBlockStay(w, x, y, z)) {

				dropBlockAsItem(w, x, y + 1, z, meta, 0);
				w.setBlockToAir(x, y + 1, z);
				dropBlockAsItem(w, x, y, z, meta, 0);
				w.setBlockToAir(x, y, z);
			} 
		} 
	}



	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		if (!isTopBlock(metadata)) {

			ret.add(new ItemStack((Block)this, 1, 0));
			return ret;
		} 

		metadata -= 8;
		if (metadata >= this.growthStages - 1) {

			ret.add(new ItemStack(TBItems.resource, 1 + world.rand.nextInt(4), 6));
			return ret;
		} 

		return ret;
	}





	public boolean canBlockStay(World w, int x, int y, int z) {
		return (!w.isAirBlock(x, y - 1, z) && (w.getBlock(x, y - 1, z).isReplaceableOreGen(w, x, y - 1, z, (Block)Blocks.grass) || w.getBlock(x, y - 1, z).isReplaceableOreGen(w, x, y, z, Blocks.dirt) || w.getBlock(x, y - 1, z).canSustainPlant((IBlockAccess)w, x, y - 1, z, ForgeDirection.UP, (IPlantable)this)));
	}


	public boolean isTopBlock(int meta) {
		return (meta > 7);
	}


	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
		w.setBlock(x, y + 1, z, (Block)this, 8, 3);
	}


	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		this.doublePlantBottomIcons = new IIcon[this.growthStages];
		this.doublePlantTopIcons = new IIcon[this.growthStages];

		for (int i = 0; i < this.growthStages; i++) {

			this.doublePlantBottomIcons[i] = reg.registerIcon(this.textureName + "stage_" + i + "_bot");
			this.doublePlantTopIcons[i] = reg.registerIcon(this.textureName + "stage_" + i + "_top");
		} 
	}



	public IIcon getIcon(int side, int meta) {
		return isTopBlock(meta) ? this.doublePlantTopIcons[meta - 8] : this.doublePlantBottomIcons[meta];
	}




	public boolean func_149851_a(World w, int x, int y, int z, boolean remote) {
		return (w.getBlockMetadata(x, y, z) % 8 < this.growthStages - 1);
	}



	public boolean func_149852_a(World w, Random r, int x, int y, int z) {
		return (w.getBlockLightValue(x, y + 1, z) >= 9);
	}




	public void func_149853_b(World w, Random r, int x, int y, int z) {
		if (w.getBlock(x, y, z) == this) {

			int meta = w.getBlockMetadata(x, y, z);
			if (isTopBlock(meta)) {

				if (meta - 8 < this.growthStages - 1 && w.rand.nextInt(this.growthDelay) == 0)
				{
					w.setBlockMetadataWithNotify(x, y, z, meta + 1, 3);
					w.setBlockMetadataWithNotify(x, y - 1, z, meta - 7, 3);
				}

			}
			else if (meta < this.growthStages - 1 && w.rand.nextInt(this.growthDelay) == 0) {

				w.setBlockMetadataWithNotify(x, y, z, meta + 1, 3);
				w.setBlockMetadataWithNotify(x, y + 1, z, meta + 9, 3);
			} 
		} 
	}
}


