package tb.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tb.common.tile.TileAutoDeconstructor;
import tb.core.TBCore;
import tb.utils.DummySteele;

public class BlockAutoDeconstructor extends BlockContainer {

	public IIcon topSide;
	public IIcon faceSide;

	public BlockAutoDeconstructor() {
		super(Material.wood);
		setHardness(1.0F);
		setResistance(1.0F);
		setStepSound(soundTypeWood);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return (TileEntity) new TileAutoDeconstructor();
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase playerM, ItemStack itemStack) {

		TileAutoDeconstructor tile = (TileAutoDeconstructor)world.getTileEntity(x, y, z);

		if (playerM instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) playerM;
			makeNBTCompound(tile, player);   
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {

			TileAutoDeconstructor tile = (TileAutoDeconstructor) world.getTileEntity(x, y, z);

			if (tile.getNames().contains(player.getDisplayName())) {
				player.openGui(TBCore.instance, 4331813, world, x, y, z);
				return true;
			}
			else {
				player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("tb.txt.noUseBlock")));
				return true;
			}


		}
		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int i) {
		DummySteele.dropItemsOnBlockBreak(world, x, y, z, block, i);
		super.breakBlock(world, x, y, z, block, i);
	}

	private void makeNBTCompound(TileAutoDeconstructor tile, EntityPlayer player) {
		NBTTagCompound pName = new NBTTagCompound();
		tile.playerNames.add(player.getDisplayName());
		tile.writeToNBT(pName);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return side < 2 ? this.topSide : this.faceSide;
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		this.topSide = reg.registerIcon(getTextureName() + "autoConst_top");
		this.faceSide = reg.registerIcon(getTextureName() + "autoConst_side");
	}
}
