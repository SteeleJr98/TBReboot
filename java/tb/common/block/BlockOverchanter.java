package tb.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import tb.common.tile.TileOverchanter;
import tb.core.TBCore;
import tb.utils.DummySteele;

public class BlockOverchanter extends BlockContainer {
	
	public IIcon topIcon;
	public IIcon botIcon;
	public IIcon sideIcon;

	public BlockOverchanter() {
		super(Material.rock);
		setHarvestLevel("pickaxe", 3);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
		setLightOpacity(0);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}


	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		return (side == 0) ? this.botIcon : ((side == 1) ? this.topIcon : this.sideIcon);
	}


	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		this.topIcon = reg.registerIcon("thaumicbases:overchanter/top");
		this.botIcon = reg.registerIcon("thaumicbases:overchanter/bottom");
		this.sideIcon = reg.registerIcon("thaumicbases:overchanter/side");
	}

	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		return (TileEntity)new TileOverchanter();
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int i) {
		DummySteele.dropItemsOnBlockBreak(world, x, y, z, block, i);
		super.breakBlock(world, x, y, z, block, i);
	}

	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int side, float vecX, float vecY, float vecZ) {
		if (!p.isSneaking()) {

			if (!w.isRemote) {

				p.openGui(TBCore.instance, 4331810, w, x, y, z);
				return true;
			} 

			return true;
		} 

		return false;
	}
}


