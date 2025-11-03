package tb.handlers;


import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.WorldEvent.Save;
import tb.core.TBCore;
import tb.init.TBBlocks;
import tb.utils.DummySteele;
import tb.utils.TBConfig;
import tb.utils.TBUtils;
import thaumcraft.common.config.ConfigBlocks;

public class DimesnionTickHandler {
	
	private static int dimAge = 0;
	private static int maxAge = 20 * 60 * 60 * TBConfig.cascadeDimReset;
	private static int halfMax = maxAge >> 1;
	private static boolean readyForReset = false;
	private static boolean firstRun = true;
	
	
	@SubscribeEvent
	public void worldTick(TickEvent.WorldTickEvent event) {
		//TBCore.TBLogger.log(Level.INFO, "WorldTick");
		
		
		
		if (firstRun) {
			if (event.world.playerEntities.size() == 0 && event.side == Side.SERVER && event.world.provider.dimensionId == TBConfig.cascadeDimID) {
				TBCore.TBLogger.log(Level.INFO, "Doing first run unload");
				DimensionManager.unloadWorld(TBConfig.cascadeDimID);
				firstRun = false;
				return;
			}
		}
		if (event.type == Type.WORLD && event.side == Side.SERVER && event.phase == Phase.END) {
			if (event.world.provider.dimensionId == TBConfig.cascadeDimID) {
				
				if (dimAge >= (maxAge)) {
					
					TBUtils.primeForReset(event.world);
					readyForReset = true;
					
					dimAge = 0;
					//event.setCanceled(true);
					return;
					
				}
				dimAge++;
				if (!event.world.isRemote) {
					int ageDiff = maxAge - dimAge;
					
					int caseInt = (ageDiff < 1200) ? 1 : (ageDiff < halfMax ? 2 : 3);
					
					switch (caseInt) {
					case 1:
						fastStrike(event.world);
						chunkDeletor(event.world, true);
						break;
						
					case 2:
						scalingStrike(event.world);
						chunkDeletor(event.world, false);
						break;
						
					case 3:
						slowStrike(event.world);
						break;

					default:
						break;
					}
				}
			}
		}
		
		
		
//		WorldServer[] dimList = MinecraftServer.getServer().worldServers;
//		for (WorldServer w : dimList) {
//			TBCore.TBLogger.log(Level.INFO, w.provider.dimensionId);
//			if (w.provider.dimensionId == TBConfig.cascadeDimID) {
//				dimAge++;
//			}
//		}
	}
	
	private void chunkDeletor(World world, boolean near) {
		
	}
	
	private void slowStrike(World world) {
		if (world.rand.nextInt(20 * 60 * TBConfig.cascadeDimSPM) == 0) {//average 1 every x minutes
			TBUtils.strikeNearAllPlayers(world, true);
			DummySteele.sendMessageFromServer("did slow strike roll");
		}
	}
	
	private void scalingStrike(World world) {
		if (dimAge % 40 == 0) {
			float ageScale = (float) dimAge / (float) maxAge;
			if (world.rand.nextFloat() < ageScale) {
				TBUtils.strikeNearAllPlayers(world, world.rand.nextBoolean());
				DummySteele.sendMessageFromServer("did scaling strike roll");
			}
		}
	}
	
	private void fastStrike(World world) {
		if (world.rand.nextInt(20) == 0) {
			TBUtils.strikeNearAllPlayers(world, false);
			DummySteele.sendMessageFromServer("did fast strike roll");
		}
	}
	
	@SubscribeEvent
	public void gameTick(ServerTickEvent event) {
		if (readyForReset) {
			if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
				if (!TBUtils.isTBDimLoaded()) {
					TBUtils.TBDimensionReset();
					readyForReset = false;
				}
			}
		}
	}
	
	
	
	public static int getDimAge() {
		return dimAge;
	}
	
	public static void setDimAge(int newAge) {
		dimAge = newAge;
	}
	
	public static boolean checkReadyForReset() {
		return readyForReset;
	}
	
	public static void resetReadyFlag() {
		readyForReset = false;
	}
}
