package tb.dimension;

import java.util.Random;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeManager;
import tb.utils.TBConfig;

public class TBDimGeneration implements IWorldGenerator {

	
	public void initialize() {
		
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
	}
	
	
	
	public static BiomeGenBase cascadeBiome = new BiomeGenCascade(TBConfig.cascadeBiomeID);
	
	

}
