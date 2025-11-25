package tb.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import tb.common.tile.TileAutoDeconstructor;
import tb.common.tile.TileCascadeCollector;
import tb.core.TBCore;
import tb.utils.DummySteele;

public class BlockCascadeCollector extends BlockContainer {

	public IIcon topSide;
	public IIcon faceSide;
	
	public BlockCascadeCollector() {
		super(Material.iron);
		setHardness(1.0F);
		setResistance(1.0F);
		setStepSound(soundTypeMetal);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return (TileEntity) new TileCascadeCollector();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			player.openGui(TBCore.instance, 4331824, world, x, y, z);
		}
		return true;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int i) {
		DummySteele.dropItemsOnBlockBreak(world, x, y, z, block, i);
		super.breakBlock(world, x, y, z, block, i);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return side < 2 ? this.topSide : this.faceSide;
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		this.topSide = reg.registerIcon(getTextureName() + "cascadeCollector_top");
		this.faceSide = reg.registerIcon(getTextureName() + "cascadeCollector_side");
	}

}
