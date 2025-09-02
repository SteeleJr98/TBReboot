package tb.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tb.utils.DummySteele;


public class BlockCryingObelisk extends Block {
	
	public IIcon topbotIcon;
	public IIcon genericIcon;
	public IIcon topSide;
	public IIcon botSide;

	public BlockCryingObelisk() {
		super(Material.rock);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess w, int x, int y, int z, int side) {
		if (w.getBlock(x, y + 1, z) == this && w.getBlock(x, y + 2, z) != this && w.getBlock(x, y - 1, z) != this) {
			return (side == 0 || side == 1) ? this.topbotIcon : this.botSide;
		}
		if (w.getBlock(x, y - 1, z) == this && w.getBlock(x, y - 2, z) != this && w.getBlock(x, y + 1, z) != this) {
			return (side == 0 || side == 1) ? this.topbotIcon : this.topSide;
		}
		return this.genericIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		return this.genericIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World w, int x, int y, int z, Random rnd) {
		if ((w.getBlock(x, y + 1, z) == this && w.getBlock(x, y + 2, z) != this && w.getBlock(x, y - 1, z) != this) || (w.getBlock(x, y - 1, z) == this && w.getBlock(x, y - 2, z) != this && w.getBlock(x, y + 1, z) != this)) {
			for (int i = 0; i < 10; i++) {

				double rndY = rnd.nextDouble() * 3.0D;
				w.spawnParticle("portal", x + 0.5D + DummySteele.randomDouble(rnd), (y - 3) + rndY, z + 0.5D + DummySteele.randomDouble(rnd), 0.0D, rndY, 0.0D);
			} 
		}
	}

	@Override
	public boolean isBed(IBlockAccess world, int x, int y, int z, EntityLivingBase player) {
		return world.getBlockMetadata(x, y, z) != 0;
	}

	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int side, float vecX, float vecY, float vecZ) {

		if (isValidStruc(w, x, y, z)) {

			boolean isBase = w.getBlock(x, y + 1, z) == this;

			p.setSpawnChunk(new ChunkCoordinates(x, y - (isBase ? 0 : 1), z), false, w.provider.dimensionId);



			if (!p.worldObj.isRemote) {
				p.addChatMessage((new ChatComponentText(StatCollector.translateToLocal("tb.txt.spawnSet"))).setChatStyle((new ChatStyle()).setItalic(Boolean.valueOf(true)).setColor(EnumChatFormatting.AQUA))); 
			}
			w.playSound(x + 0.5D, y + 0.5D, z + 0.5D, "portal.travel", 0.4F, 2.0F, false);
			return true;
		} 
		return false;
	}


	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hX, float hY, float hZ, int meta) {

		updateMeta(world, x, y, z, true);

		return meta;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int metaQ) {
		updateMeta(world, x, y, z, false);
	}


	//   @Override
	//   public ChunkCoordinates getBedSpawnPosition(IBlockAccess world, int x, int y, int z, EntityPlayer player) {
	//     for (int dy = 0; dy > -3; dy--) {
	//       for (int dx = -2; dx <= 2; dx++) {
	//         
	//         for (int dz = -2; dz <= 2; dz++) {
	//           
	//           if (World.doesBlockHaveSolidTopSurface(world, x + dx, y + dy, z + dz) && !world.getBlock(x + dx, y + dy, z + dz).getMaterial().isOpaque())
	//           {
	//             return new ChunkCoordinates(x + dx, y + dy + 1, z + dz); } 
	//         } 
	//       } 
	//     } 
	//     return BlockBed.func_149977_a((World)world, x, y, z, 0);
	//   }


	private boolean isValidStruc(World world, int x, int y, int z) {
		return (world.getBlock(x, y + 1, z) == this && world.getBlock(x, y + 2, z) != this && world.getBlock(x, y - 1, z) != this) || (world.getBlock(x, y - 1, z) == this && world.getBlock(x, y - 2, z) != this && world.getBlock(x, y + 1, z) != this);
		//return true;
	}

	private void updateMeta(World world, int x, int y, int z, boolean add) {
		if (world.getBlock(x, y - 1, z) == this) {
			world.setBlockMetadataWithNotify(x, y - 1, z, add ? 1 : 0, 3);
		}
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		this.genericIcon = reg.registerIcon(getTextureName() + "cryingObsidian");
		this.topbotIcon = reg.registerIcon(getTextureName() + "cryingObelisk_TB");
		this.topSide = reg.registerIcon(getTextureName() + "cryingObelisk_sideT");
		this.botSide = reg.registerIcon(getTextureName() + "cryingObelisk_sideB");
	}
}


