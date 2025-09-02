package tb.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import tb.utils.DummySteele;
import tb.utils.TBConfig;
import tb.utils.TBUtils;

public class TBTeleporter extends Teleporter {

	private final WorldServer worldServerInstance;
	
	public TBTeleporter(WorldServer server) {
		super(server);
		this.worldServerInstance = server;
	}
	
	@Override
	public boolean placeInExistingPortal(Entity entity, double x, double y, double z, float f) {
		
		int topBedrock = TBUtils.getTopBlockOfType(worldServerInstance, 0, 0, Blocks.bedrock);
		
		if (topBedrock <= 5) {
			DummySteele.sendMessageFromServer("top bedrock less 5 or lower. topBedrock: " + topBedrock);
			return false;
		}
		
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				if (this.worldServerInstance.getBlock(i, topBedrock, j) != Blocks.bedrock) {
					DummySteele.sendMessageFromServer("bedrock check failed at: " + i + " " + topBedrock + " " + j);
					return false;
				}
			}
		}
		
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				this.worldServerInstance.setBlock(i, topBedrock + 1, j, Blocks.air);
				this.worldServerInstance.setBlock(i, topBedrock + 2, j, Blocks.air);
				this.worldServerInstance.setBlock(i, topBedrock + 3, j, Blocks.air);
			}
		}
		
		return true;
	}
	
	@Override
	public void placeInPortal(Entity entity, double x, double y, double z, float f) {
		if (this.worldServerInstance.provider.dimensionId == TBConfig.cascadeDimID) {
			int topY = TBUtils.getTopBlock(this.worldServerInstance, 0, 0);
			if (!placeInExistingPortal(entity, x, y, z, f)) {
				
				int newX = (int)x;
				int newZ = (int)z;
				
				for (int i = -2; i <= 2; i++) {
					for(int j = -2; j <= 2; j++) {
						this.worldServerInstance.setBlock(i, topY != -1 ? topY : 5, j, Blocks.bedrock);
					}
				}
				
				
			}
			entity.setPosition(0.5D, topY != -1 ? topY + 1D : 6D, 0.5D);
			
		}
		else {
			
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				
				int yTop = 1;
				for (int i = -2; i <= 2; i++) {
					for (int j = -2; j <= 2; j++) {
						int tempY = TBUtils.getTopBlock(this.worldServerInstance, (int) player.posX + i, (int) player.posX + j);
						if (tempY > yTop) {
							yTop = tempY;
						}
					}
				}
				DummySteele.sendMessageFromServer("top y found: " + yTop);
				player.setPosition(player.posX, (yTop != -1 ? yTop : savePlayerFromTP(this.worldServerInstance, player)) + 1, player.posZ);
//				ChunkCoordinates pCoords = player.getBedLocation(0);
//				
//				
//				if (pCoords != null) {
//					ChunkCoordinates sCoords = player.verifyRespawnCoordinates(worldServerInstance, pCoords, true);
//					player.setPosition(sCoords.posX, sCoords.posY + 1.1D, sCoords.posZ);
//				}
//				else {
//					ChunkCoordinates worldCoords = this.worldServerInstance.provider.getRandomizedSpawnPoint();
//					player.setPosition(worldCoords.posX, worldCoords.posY + 1.2D, worldCoords.posZ);
//				}
				
			}
			
		}
		
		
	}
	
	private int savePlayerFromTP(WorldServer world, EntityPlayer player) {
		DummySteele.sendMessageFromServer("Used alt tp method");
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				world.setBlock(i, (int) (player.posY - 1), j, Blocks.dirt);
				world.setBlockToAir(i, (int) (player.posY), j);
				world.setBlockToAir(i, (int) (player.posY + 1), j);
				world.setBlockToAir(i, (int) (player.posY + 2), j);
			}
		}
		return (int) (player.posY + 1);
	}

}
