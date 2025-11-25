package tb.common.block;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import tb.core.TBCore;
import tb.init.TBBlocks;
import tb.network.proxy.TBNetworkManager;
import tb.utils.DummySteele;
import tb.utils.TBUtils;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.BlockAiry;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.research.PlayerKnowledge;
import thaumcraft.common.tiles.TileJarNode;

public class BlockVoidAnvil extends BlockAnvil implements IInfusionStabiliser {
	
	public static final String[] anvilDamageNames = new String[] { "intact", "slightlyDamaged", "veryDamaged" };
	private static final String[] anvilIconNames = new String[] { "top_damaged_0", "top_damaged_1", "top_damaged_2" };
	//private static final ChatComponentText warpText1 = new ChatComponentText("Add warp message ");
	//private static final ChatComponentText warpText2 = new ChatComponentText(EnumChatFormatting.DARK_BLUE + "here");


	@SideOnly(Side.CLIENT)
	private IIcon[] anvilIcons;


	public BlockVoidAnvil() {
		setHardness(12.0F);
		setResistance(Float.MAX_VALUE);
		setTickRandomly(true);
		setStepSound(Block.soundTypeAnvil);
		setHarvestLevel("pickaxe", 3);
	}

	public void updateHealth(World w, int x, int y, int z, boolean forceUpdate ) {
		int meta = w.getBlockMetadata(x, y, z);
		long worldTime = w.getTotalWorldTime();
		AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x - 5, y - 5, z - 5, x + 5, y + 5, z + 5);
		List playerList = w.getEntitiesWithinAABB(EntityPlayer.class, box);
		//System.out.println("Meta: " + meta + " World Time: " + worldTime + " Should Repair: " + (worldTime % 100 == 0 ? "True" : "False") + " Players: " + playerList.toString());


		if (worldTime % 5 == 0 || forceUpdate) {
			if (meta > 4) {
				w.setBlockMetadataWithNotify(x, y, z, meta - 4, 3);
				warpPlayers(w, playerList);
				
			}
		}
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.updateTick(world, x, y, z, world.rand);
	}

	@Override
	public void updateTick(World w, int x, int y, int z, Random rnd) {
		super.updateTick(w, x, y, z, rnd);
		if (!w.isRemote) {
			updateHealth(w, x, y, z, false);
			
			if (rnd.nextInt(100) <= 5) {
				
				int aCount = 0;
				boolean stoneBlock = false;
				
				PositionImpl firstBlock = TBUtils.getCornerOfCube(w, x, y, z);
				int firstX =  (int) firstBlock.getX();
				int firstY =  (int) firstBlock.getY();
				int firstZ =  (int) firstBlock.getZ();
				
				tempLabel:
				for (int xx = firstX; xx < firstX + 3; xx++) {
					for (int yy = firstY; yy < firstY + 3; yy++) {
						for (int zz = firstZ; zz < firstZ + 3; zz++) {
							Block tempBlock = w.getBlock(xx, yy, zz);
							if (tempBlock == this) {
								if (w.getBlockMetadata(xx, yy, zz) >= 4) {
									aCount++;
								}
							}
							else if (tempBlock == Blocks.stone) {
								stoneBlock = true;
							}
							else {
								//DummySteele.sendMessageFromServer("Structure fail");
								break tempLabel;
							}
						}
					}
				}
				if (aCount == 26 && stoneBlock) {
					//DummySteele.sendMessageFromServer("Structure complete");
					TBNetworkManager.playSoundOnServer(w, "ambient.weather.thunder", firstX + 1, firstY + 1, firstZ + 1, 1, 1);
					w.setBlock(firstX + 1, firstY + 1, firstZ + 1, TBBlocks.cascadeBlock);
				}
			}
		}
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return 100;
	}

	@Override
	public void randomDisplayTick(World w, int x, int y, int z, Random r) {
		Thaumcraft.proxy.sparkle(x + r.nextFloat(), y + r.nextFloat(), z + r.nextFloat(), 2.0F, 5, -0.1F);
		//updateHealth(w, x, y, z, false);
		//System.out.print(w.getBlockMetadata(x, y, z));
	}


	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		if (this.anvilRenderSide == 3 && p_149691_1_ == 1) {

			int k = (p_149691_2_ >> 2) % this.anvilIcons.length;
			return this.anvilIcons[k];
		} 


		return this.blockIcon;
	}



	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		this.blockIcon = p_149651_1_.registerIcon(getTextureName() + "base");
		this.anvilIcons = new IIcon[anvilIconNames.length];

		for (int i = 0; i < this.anvilIcons.length; i++) {
			this.anvilIcons[i] = p_149651_1_.registerIcon(getTextureName() + anvilIconNames[i]);
		}
	}


	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int side, float vecX, float vecY, float vecZ) {
		if (w.isRemote) {
			return true;
		}
		
		ItemStack pItem = p.getCurrentEquippedItem();
		if (pItem != null) {
			if (pItem.getItem() == ConfigItems.itemPrimalCrusher && ThaumcraftApiHelper.isResearchComplete(p.getCommandSenderName(), "TB.CascadeDim")) {
				
				int blockMeta = w.getBlockMetadata(x, y, z);
				if (blockMeta <= 7) {
					w.setBlockMetadataWithNotify(x, y, z, blockMeta + 4, 3);
					return true;
				}
				else {
					TileEntity tempTile = w.getTileEntity(x, y + 1, z);
					if (tempTile != null) {
						if (tempTile instanceof TileJarNode) {
							TileJarNode tempNode = (TileJarNode) tempTile;
							if (tempNode.getNodeType() == NodeType.DARK) {
								AspectList aList = tempNode.getAspects();
								Aspect[] amountCheck = {
										Aspect.DARKNESS,
										Aspect.VOID,
										Aspect.EARTH
								};
								if (aList.getAmount(amountCheck[0]) >= 6 && aList.getAmount(amountCheck[1]) >= 6 && aList.getAmount(amountCheck[2]) >= 6) {
									//DummySteele.sendMessageFromServer("node has aspects");
									if (p.dimension == 1) {
										TBNetworkManager.playSoundOnServer(w, "thaumcraft:craftfail", x, y + 1, z, 1, 1);
										TBNetworkManager.playSoundOnServer(w, "thaumcraft:urnbreak", x, y + 1, z, 1, 1);
										//BlockAiry.explodify(w, x, y + 1, z);
										w.setBlock(x, y + 1, z, Blocks.air, 0, 3);
										p.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("tb.txt.noEndPortal")));
										return true;
									}
									TBNetworkManager.playSoundOnServer(w, "ambient.weather.thunder", x, y + 1, z, 1, 1);
									TBNetworkManager.playSoundOnServer(w, "thaumcraft:urnbreak", x, y + 1, z, 1, 1);
									w.setBlock(x, y + 1, z, TBBlocks.cascadeBlock, 0, 3);
									return true;
								}
								else {
									//DummySteele.sendMessageFromServer("node doesn not have aspects");
									TBNetworkManager.playSoundOnServer(w, "thaumcraft:jar", x, y + 1, z, 1, 1);
									return true;
								}
							}
							else {
								//DummySteele.sendMessageFromServer("node is not dark it is: " + tempNode.getNodeType());
								TBNetworkManager.playSoundOnServer(w, "thaumcraft:jar", x, y + 1, z, 1, 1);
								return true;
							}
						}
					}
					return true;
				}
			}
			
		}
		p.openGui(TBCore.instance, 4331808, w, x, y, z);
		
		

		return true;
	}



	public boolean canStabaliseInfusion(World w, int x, int y, int z) {
		return true;
	}

	public void warpPlayers(World w, List playerList) {

		if (!playerList.isEmpty()) {
			for (Object playerObject : playerList) {
				EntityPlayer player = (EntityPlayer) playerObject;
				//player.addChatMessage(new ChatComponentText("test"));
				//Thaumcraft.addWarpToPlayer(player, 1, true); //how much warp to add?
				TBUtils.addWarpToPlayer(player, 10, 0);

			}
		}
	}
}


