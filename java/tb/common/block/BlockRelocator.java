package tb.common.block;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tb.common.tile.TileRelocator;
import tb.core.TBCore;
import tb.utils.DummySteele;

public class BlockRelocator extends BlockContainer {
	
	public IIcon[] icons = new IIcon[5];


	public BlockRelocator() {
		super(Material.wood);
		setHardness(2.5F);
		setResistance(15.0F);
		setStepSound(soundTypeWood);
	}


	public IIcon getIcon(int side, int meta) {
		meta %= 6;
		if (meta == 0) {
			return this.icons[(side == 0) ? 0 : ((side == 1) ? 1 : 2)];
		}
		if (meta == 1) {
			return this.icons[(side == 1) ? 0 : ((side == 0) ? 1 : 2)];
		}
		if (meta == 2) {
			return this.icons[(side == 2) ? 0 : ((side == 3) ? 1 : ((side == 0 || side == 1) ? 2 : 4))];
		}
		if (meta == 3) {
			return this.icons[(side == 3) ? 0 : ((side == 2) ? 1 : ((side == 0 || side == 1) ? 2 : 4))];
		}
		if (meta == 4) {
			return this.icons[(side == 4) ? 0 : ((side == 5) ? 1 : 4)];
		}
		if (meta == 5) {
			return this.icons[(side == 5) ? 0 : ((side == 4) ? 1 : 4)];
		}
		return this.icons[0];
	}



	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister r) {
		this.icons[0] = r.registerIcon("thaumcraft:liftertop");
		this.icons[1] = r.registerIcon("thaumcraft:arcaneearbottom");
		this.icons[2] = r.registerIcon("thaumcraft:lifterside");
		this.icons[3] = r.registerIcon("thaumcraft:animatedglow");
		this.icons[4] = r.registerIcon("thaumicbases:relocator/rotatedside");
	}


	public boolean renderAsNormalBlock() {
		return false;
	}


	public int getRenderType() {
		return 1196799;
	}


	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return (world.getBlockMetadata(x, y, z) % 6 != side.ordinal());
	}




	public TileEntity createNewTileEntity(World w, int meta) {
		return (TileEntity)new TileRelocator();
	}



	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 6));
	}


	public int damageDropped(int meta) {
		return (meta < 6) ? 0 : 6;
	}


	public void randomDisplayTick(World w, int x, int y, int z, Random r) {
		TileRelocator tile = (TileRelocator)w.getTileEntity(x, y, z);
		int meta = w.getBlockMetadata(x, y, z) % 6;
		boolean doColor = (w.getBlockMetadata(x, y, z) >= 6);
		if (!tile.isBlockPowered()) {

			if (meta == 0) {


				double dx = x + 0.5D + DummySteele.randomDouble(r) / 2.0D;
				double dy = y;
				double dz = z + 0.5D + DummySteele.randomDouble(r) / 2.0D;
				TBCore.proxy.sparkle(w, dx, doColor ? (dy - 3.0D) : dy, dz, dx, doColor ? dy : (dy - 3.0D), dz, doColor ? 2 : 1, 1.0F);
			} 
			if (meta == 1) {

				double dx = x + 0.5D + DummySteele.randomDouble(r) / 2.0D;
				double dy = (y + 1);
				double dz = z + 0.5D + DummySteele.randomDouble(r) / 2.0D;
				TBCore.proxy.sparkle(w, dx, doColor ? (dy + 3.0D) : dy, dz, dx, doColor ? dy : (dy + 3.0D), dz, doColor ? 2 : 1, 1.0F);
			} 
			if (meta == 2) {

				double dx = x + 0.5D + DummySteele.randomDouble(r) / 2.0D;
				double dy = y + 0.5D + DummySteele.randomDouble(r) / 2.0D;
				double dz = z;
				TBCore.proxy.sparkle(w, dx, dy, doColor ? (dz - 3.0D) : dz, dx, dy, doColor ? dz : (dz - 3.0D), doColor ? 2 : 1, 1.0F);
			} 
			if (meta == 3) {

				double dx = x + 0.5D + DummySteele.randomDouble(r) / 2.0D;
				double dy = y + 0.5D + DummySteele.randomDouble(r) / 2.0D;
				double dz = (z + 1);
				TBCore.proxy.sparkle(w, dx, dy, doColor ? (dz + 3.0D) : dz, dx, dy, doColor ? dz : (dz + 3.0D), doColor ? 2 : 1, 1.0F);
			} 
			if (meta == 4) {

				double dx = x;
				double dy = y + 0.5D + DummySteele.randomDouble(r) / 2.0D;
				double dz = z + 0.5D + DummySteele.randomDouble(r) / 2.0D;
				TBCore.proxy.sparkle(w, doColor ? (dx - 3.0D) : dx, dy, dz, doColor ? dx : (dx - 3.0D), dy, dz, doColor ? 2 : 1, 1.0F);
			} 
			if (meta == 5) {

				double dx = (x + 1);
				double dy = y + 0.5D + DummySteele.randomDouble(r) / 2.0D;
				double dz = z + 0.5D + DummySteele.randomDouble(r) / 2.0D;
				TBCore.proxy.sparkle(w, doColor ? (dx + 3.0D) : dx, dy, dz, doColor ? dx : (dx + 3.0D), dy, dz, doColor ? 2 : 1, 1.0F);
			} 
		} 
	}


	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		int l = determineOrientation(w, x, y, z, p_149689_5_);
		w.setBlockMetadataWithNotify(x, y, z, w.getBlockMetadata(x, y, z) + l, 3);
	}


	public static int determineOrientation(World p_150071_0_, int p_150071_1_, int p_150071_2_, int p_150071_3_, EntityLivingBase p_150071_4_) {
		if (MathHelper.abs((float)p_150071_4_.posX - p_150071_1_) < 2.0F && MathHelper.abs((float)p_150071_4_.posZ - p_150071_3_) < 2.0F) {

			double d0 = p_150071_4_.posY + 1.82D - p_150071_4_.yOffset;

			if (d0 - p_150071_2_ > 2.0D) {
				return 1;
			}

			if (p_150071_2_ - d0 > 0.0D) {
				return 0;
			}
		} 

		int l = MathHelper.floor_double((p_150071_4_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;
		return (l == 0) ? 2 : ((l == 1) ? 5 : ((l == 2) ? 3 : ((l == 3) ? 4 : 0)));
	}
}


