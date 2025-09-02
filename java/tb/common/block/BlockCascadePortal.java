package tb.common.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import tb.dimension.TBTeleporter;
import tb.utils.TBConfig;
import thaumcraft.common.Thaumcraft;

public class BlockCascadePortal extends Block {
	
	IIcon iIcon;

	public BlockCascadePortal() {
		super(Material.portal);
		setTickRandomly(true);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		return null;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		
        this.setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
	}
	
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if (entity.ridingEntity == null && entity.riddenByEntity == null && !world.isRemote)
        {
			if (entity instanceof EntityPlayerMP) {
				//entity.travelToDimension(TBConfig.cascadeDimID);
				EntityPlayerMP tempPlayer = (EntityPlayerMP) entity;
				MinecraftServer mServer = FMLCommonHandler.instance().getMinecraftServerInstance();
				int dimToTravelTo = tempPlayer.dimension == TBConfig.cascadeDimID ? 0 : TBConfig.cascadeDimID;
				tempPlayer.mcServer.getConfigurationManager().transferPlayerToDimension(tempPlayer, dimToTravelTo, new TBTeleporter(mServer.worldServerForDimension(dimToTravelTo)));
			}
			
        }
	}
	
	@Override
	public int getRenderType() {
        return 0;
    }
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	 */
	@Override
	public boolean renderAsNormalBlock() {
		return true;
	}
	
	@Override
	public int getBlockColor() {
		return 0x3333ff;
	}
	
	@Override
	public int getRenderBlockPass() {
		return 1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        this.iIcon = reg.registerIcon("thaumicbases:cascade");
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		return this.iIcon;
	}
	
	@Override
	public void randomDisplayTick(World w, int x, int y, int z, Random r) {
		Thaumcraft.proxy.sparkle(x + r.nextFloat(), y + r.nextFloat(), z + r.nextFloat(), 2.0F, 5, -0.1F);
	}

}
