package tb.dimension;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import tb.handlers.DimesnionTickHandler;
import tb.utils.TBConfig;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.lib.world.biomes.BiomeGenEldritch;

public class WorldProviderCascade extends WorldProvider {
	
	private ChunkProviderCascade chunkProvider;

	public void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerHell(TBDimGeneration.cascadeBiome, 2F);
		this.dimensionId = TBConfig.cascadeDimID;
		this.isHellWorld = false;
		this.hasNoSky = false;
		
	}
	
	@Override
	public IChunkProvider createChunkGenerator() {
		return this.getChunkGenerator();
	}
	
	public IChunkProvider getChunkGenerator() {
		
		if (chunkProvider == null) {
			chunkProvider = new ChunkProviderCascade(this.worldObj, System.currentTimeMillis(), false);
		}
		
		return chunkProvider;
	}
	
	
	@Override
	public double getMovementFactor() {
		//max = 16.2:1
		return 1.0D + ((DimesnionTickHandler.getDimAge() / 20F) * 0.09F * (TBConfig.cascadeDimMovementScale / 100F));
	}
	
	
	
	@Override
	public boolean isSurfaceWorld() {
        return false;
    }
	
	@Override
	public boolean canRespawnHere() {
        return false;
    }
	
	@Override
	public boolean canBlockFreeze(int x, int y, int z, boolean byWater) {
        return false;
    }
	
	@Override
	public boolean canSnowAt(int x, int y, int z, boolean checkLight) {
        return false;
    }
	
	@Override
	public void updateWeather() {
		this.worldObj.getWorldInfo().setThundering(true);
	}
	
	@Override
	public void calculateInitialWeather() {
		this.worldObj.getWorldInfo().setThundering(true);
	}
	
	@Override
	public String getDimensionName() {
		return StatCollector.translateToLocal("tb.dimCascade.name");
	}
	
	@Override
	public String getWelcomeMessage() {
		return StatCollector.translateToLocal("tb.dimCascade.welcome");
	}
	
	@Override
	public String getDepartMessage() {
		return StatCollector.translateToLocal("tb.dimCascade.depart");
	}
	
	@Override
	public boolean canDoLightning(Chunk chunk) {
		return true;
	}
	
	
	
	@Override
	public long getWorldTime() {
		return 18000;
	}
	
	

}
