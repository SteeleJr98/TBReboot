package tb.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandSender;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import tb.core.TBCore;
import tb.dimension.TBTeleporter;
import tb.dimension.WorldProviderCascade;
import tb.init.TBBlocks;
import tb.utils.DummySteele.SimpleNodeData;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketAspectPool;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileNode;

public class TBUtils
{
	public static void addAspectToKnowledgePool(EntityPlayer addedTo, Aspect added, short amount) {
		Thaumcraft.proxy.playerKnowledge.addAspectPool(addedTo.getCommandSenderName(), added, amount);
		ResearchManager.scheduleSave(addedTo);
		if (addedTo instanceof EntityPlayerMP) {
			PacketHandler.INSTANCE.sendTo((IMessage)new PacketAspectPool(added.getTag(), Short.valueOf(amount), Short.valueOf(Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(addedTo.getCommandSenderName(), added))), (EntityPlayerMP)addedTo);
		}
	}




	public static void addWarpToPlayer(EntityPlayer addTo, int amount, int type) {
		switch (type) {


		case 2:
			Thaumcraft.addWarpToPlayer(addTo, amount, false);
			return;


		case 1:
			Thaumcraft.addStickyWarpToPlayer(addTo, amount);
			return;


		case 0:
			Thaumcraft.addWarpToPlayer(addTo, amount, true);
			return;
		} 


		Thaumcraft.addWarpToPlayer(addTo, amount, false);
	}

	public static double SimpleDist(ChunkCoordinates c1, ChunkCoordinates c2) {
		int deltaX = c1.posX - c2.posX;
		int deltaY = c1.posY - c2.posY;
		int deltaZ = c1.posZ - c2.posZ;
		return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
	}

	public static void sendChatToSender(ICommandSender sender, Object str) {
		sender.addChatMessage(new ChatComponentText(str.toString()));
	}

	public static boolean checkMobBehindPlayer(World world, EntityPlayer player, double range) {
		if (!world.isRemote) {
			double pQuad = (((player.rotationYawHead + 360) % 360) / 45);
			//player.addChatComponentMessage(new ChatComponentText("rot quad: " + pQuad));
			boolean q1 = pQuad >= 1 && pQuad < 3;
			boolean q2 = pQuad >= 3 && pQuad < 5;
			boolean q3 = pQuad >= 5 && pQuad < 7;
			boolean q4 = !q1 && !q2 && !q3;
			//2- 4+ Z
			//1- 3+ X
			//player.addChatComponentMessage(new ChatComponentText("rot quad(1 2 3 4): " + String.valueOf(q1) + " " + String.valueOf(q2) + " " + String.valueOf(q3) + " " + String.valueOf(q4)));

			AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(player.posX - ((q1 || q3) ? (q3 ? range : -1) : range), player.posY, player.posZ - ((q2 || q4) ? (q4 ? range : -1) : range),/*min<->max*/ player.posX + ((q1 || q3) ? (q3 ? -1 : range) : range), player.posY + 2, player.posZ + ((q2 || q4) ? (q4 ? -1 : range) : range));
			List<Object> list = world.getEntitiesWithinAABB(EntityMob.class, aabb);
			for (Object e : list) {
				if (((EntityMob)e).getAttackTarget() == player) {
					return true;
				}
				//			   EntityMob mob = (EntityMob) e;
				//			   player.addChatComponentMessage(new ChatComponentText("Entity behind target: " + mob.getAttackTarget()));
			}
		}
		return false;
	}

	public static String getClassLastString(String s) {
		String last = s.substring(s.lastIndexOf(".") + 1).trim();
		return last;
	}

	public static Object getTaggedEntityLookingAway(EntityPlayer player, int radius) {
		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(player.posX - radius, player.posY - radius, player.posZ - radius, player.posX + radius, player.posY + radius, player.posZ + radius);
		List<Object> list = player.worldObj.getEntitiesWithinAABB(Entity.class, aabb);
		for (Object e : list) {
			//player.addChatComponentMessage(new ChatComponentText("Checking ent in list: " + e.toString()));
			if (e instanceof EntityPlayer) {
				EntityPlayer testPlayer = (EntityPlayer) e;
				float tAng = testPlayer.getRotationYawHead() + 180;
				float pAng = player.getRotationYawHead() + 180;
				if (testPlayer.getDistanceToEntity(player) < 1 && Math.min(Math.abs(tAng - pAng), 360 - (tAng - pAng)) < 10) {
					return testPlayer;
				}
				//player.addChatComponentMessage(new ChatComponentText("got entity"));

			}
		}
		return null;
	}

	public static int getTopBlock(World world, int x, int z) {
		for (int y = 256; y > 0; y--) {
			Material tempMaterial = world.getBlock(x, y, z).getMaterial();
			if (tempMaterial != Material.air) {
				return y;
			}
		}

		return -1;
	}

	public static int getTopBlockOfType(World world, int x, int z, Block blockType) {
		for (int y = 256; y > 0; y--) {
			if (world.getBlock(x, y, z) == blockType) {
				return y;
			}
		}

		return -1;
	}

	public static PositionImpl getCornerOfCube(World world, int x, int y, int z) {

		Block tempBlock = world.getBlock(x, y, z);

		int newX = x;
		int newY = y;
		int newZ = z;

		boolean atEnd = false;

		while (!atEnd) {

			if (world.getBlock(newX - 1, newY, newZ) == tempBlock) {
				newX--;
				continue;
			}
			if (world.getBlock(newX, newY - 1, newZ) == tempBlock) {
				newY--;
				continue;
			}
			if (world.getBlock(newX, newY, newZ - 1) == tempBlock) {
				newZ--;
				continue;
			}
			atEnd = true;



			//		   if (!(world.getBlock(newX - 1, newY, newZ) != tempBlock || world.getBlock(newX, newY - 1, newZ) != tempBlock || world.getBlock(newX, newY, newZ - 1) != tempBlock)) {
			//			   return new PositionImpl(newX, newY, newZ);
			//		   }
		}


		return new PositionImpl(newX, newY, newZ);
	}
	
	public static boolean isTBDimLoaded() {
		for (int i : DimensionManager.getIDs()) {
			if (i == TBConfig.cascadeDimID) {
				return true;
			}
		}
		return false;
	}

	private static boolean currentlyResetting = false;

	public static void primeForReset(World world) {
		List playerList = new ArrayList<Object>(world.playerEntities);
		if (!playerList.isEmpty()) {
			TBCore.TBLogger.log(Level.INFO, "Attempting to eject players from dim");
			List tempList = playerList;
			for (Object o : tempList) {
				EntityPlayerMP player = (EntityPlayerMP) o;
				MinecraftServer mServer = FMLCommonHandler.instance().getMinecraftServerInstance();
				player.mcServer.getConfigurationManager().transferPlayerToDimension(player, 0, new TBTeleporter(mServer.worldServerForDimension(0)));
			}
		}
//		if (world instanceof WorldServer) {
//			TBCore.TBLogger.log(Level.INFO, "flushing world caches (i think idrk)");
//			((WorldServer)world).flush();
//			//DimensionManager.unloadWorld(TBConfig.cascadeDimID);
//			
//		}
	}
	
	public static void TBDimensionReset() {
		try {
			if (currentlyResetting) {
				TBCore.TBLogger.log(Level.INFO, "currentlyResetting is set to true");
				return;
			}
			currentlyResetting = true;
			

			TBCore.TBLogger.log(Level.INFO, "attempting to force unload the dimension");
			DimensionManager.setWorld(TBConfig.cascadeDimID, null);

			File path = DimensionManager.getCurrentSaveRootDirectory();
			File dim = new File(path, "DIM"+TBConfig.cascadeDimID);
			if (dim.exists() && dim.isDirectory()) {
				TBCore.TBLogger.log(Level.INFO, "attempting to delete dimension folder");
				
				int i = 0;
				
				try {
					for (i = 0; i < TBConfig.resetTries; i++) {
						FileUtils.forceDelete(dim);
					}
				}
				catch (Exception e) {
					if (e instanceof FileNotFoundException && i > 0 && i != TBConfig.resetTries) {
						TBCore.TBLogger.log(Level.INFO, "io exception within retry bounds, world deleted || i: " + i);
					}
					else {
						TBCore.TBLogger.log(Level.INFO, "could not delete dimension");
						e.printStackTrace();
					}
				}
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		currentlyResetting = false;
	}
	
	public static void strikeNearAllPlayers(World world, boolean strikeNodes) {
		if (world.playerEntities.size() > 0) {
			List pList = world.playerEntities;
			for (Object o : pList) {
				EntityPlayer player = (EntityPlayer) o;
				
				int strikeX = 0;
				int strikeY = -1;
				int strikeZ = 0;
				
				if (strikeNodes) {
					ArrayList<ChunkCoordinates> cList = DummySteele.findBlocks(world, (int) player.posX, (int) player.posY, (int) player.posZ, 20, ConfigBlocks.blockAiry);
					if (cList.size() > 0) {
						ChunkCoordinates randNode = cList.get(world.rand.nextInt(cList.size()));
						strikeX = randNode.posX;
						strikeY = randNode.posY;
						strikeZ = randNode.posZ;
						
					}
					else {
						return;
					}
				}
				else {
					strikeX = (int) player.posX + (20 - world.rand.nextInt(41));
					strikeZ = (int) player.posZ + (20 - world.rand.nextInt(41));
				}
				
				int topY = strikeY != -1 ? strikeY : TBUtils.getTopBlock(world, strikeX, strikeZ);
				EntityLightningBolt bolt = new EntityLightningBolt(world, strikeX, topY, strikeZ);
				world.addWeatherEffect(bolt);
				world.spawnEntityInWorld(bolt);
				world.setBlock(strikeX, topY + (strikeY != -1 ? 0 : 1), strikeZ, TBBlocks.cascadeBlock);
			}
		}
	}
	
	
	public static void placeRandomNode(World world, int x, int y, int z) {
		
		if (world.isAirBlock(x, y, z)) {
			world.setBlock(x, y, z, ConfigBlocks.blockAiry, 0, 0);
			TileNode node = (TileNode) world.getTileEntity(x, y, z);
			
			if (node != null && node instanceof TileNode) {
				SimpleNodeData nodeData = new SimpleNodeData(world);
				
				node.setNodeType(nodeData.type);
				node.setNodeModifier(nodeData.modifier);
				node.setAspects(nodeData.aspects);
			}
			
			world.markBlockForUpdate(x, y, z);
		}
	}
	
	
	
	
	
}


