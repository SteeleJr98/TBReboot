package tb.dimension;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.event.terraingen.TerrainGen;

public class ChunkProviderCascadeFake implements IChunkProvider {

	private World worldObj;
	private WorldType worldType;
	private Random rand;
	private BiomeGenBase[] biomesForGeneration;
//	private final boolean mapFeaturesEnabled;
//	private MapGenBase caveGenerator = new MapGenCaves();
//	private MapGenBase ravineGenerator = new MapGenRavine();
//	private MapGenStronghold strongholdGenerator = new MapGenStronghold();
//	private MapGenVillage villageGenerator = new MapGenVillage();
//	private MapGenScatteredFeature scatterGenerator = new MapGenScatteredFeature();
	
	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	private NoiseGeneratorOctaves noiseGen4;
	public NoiseGeneratorOctaves noiseGen5;
	public NoiseGeneratorOctaves noiseGen6;
	public NoiseGeneratorOctaves mobSpawnerNoise;
	
	public double[] noise1;
	public double[] noise2;
	public double[] noise3;
	public double[] noise5;
	public double[] noise6;
	
	public float[] parabolicField;
	public int[][] field = new int[32][32];
	
	private double[] noiseArray;
	private double[] stoneNoise = new double[256];
	
	
//	{
//		//Initialize generators
//	}
	
	
	public ChunkProviderCascadeFake(World world, long l1, boolean genFeatures) {
		
		this.worldObj = world;
		this.worldType = world.getWorldInfo().getTerrainType();
		this.rand = new Random(l1);
		//this.mapFeaturesEnabled = genFeatures;
		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
		this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
		this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 0);
		
		NoiseGenerator[] noiseGenerators = {noiseGen1, noiseGen2, noiseGen3, noiseGen4, noiseGen5, noiseGen6, mobSpawnerNoise};
		noiseGenerators = TerrainGen.getModdedNoiseGenerators(world, rand, noiseGenerators);
		
		this.noiseGen1 = (NoiseGeneratorOctaves) noiseGenerators[0];
		this.noiseGen2 = (NoiseGeneratorOctaves) noiseGenerators[1];
		this.noiseGen3 = (NoiseGeneratorOctaves) noiseGenerators[2];
		this.noiseGen4 = (NoiseGeneratorOctaves) noiseGenerators[3];
		this.noiseGen5 = (NoiseGeneratorOctaves) noiseGenerators[4];
		this.noiseGen6 = (NoiseGeneratorOctaves) noiseGenerators[5];
		this.mobSpawnerNoise = (NoiseGeneratorOctaves) noiseGenerators[6];
		
	}


	@Override
	public boolean chunkExists(int x, int z) {
		return true;
	}


	@Override
	public Chunk provideChunk(int x, int z) {
		//TODO make the chunk lol
		
		this.rand.setSeed(x * 341873128712L + z * 132897987541L);
		Block[] blockArray = new Block[65536];
		byte[] metaArray = new byte[65536];
		
		this.generateTerrain(x, z, blockArray);
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, x * 16, z * 16, 16, 16);
		this.replaceBlockForBiome(x, z, blockArray, metaArray, this.biomesForGeneration);
		
		this.generateColumnData(x, z, blockArray);
		
		//caveGenerator
		//ravineGenerator
		
		Chunk newChunk = new Chunk(worldObj, blockArray, x, z);
		
		byte[] byteArray = newChunk.getBiomeArray();
		
		for (int i = 0; i < blockArray.length; i++) {
			byteArray[i] = (byte)this.biomesForGeneration[i].biomeID;
		}
		
		newChunk.generateSkylightMap();
		
		return newChunk;
	}



	private void generateColumnData(int x, int z, Block[] blockArray) {
		byte b0 = 63;
	}


	public void generateTerrain(int x, int z, Block[] blockArray) {
	}
	
	public void replaceBlockForBiome(int x, int z, Block[] blockArray, byte[] metaArray, BiomeGenBase[] biomes) {
		
		byte b = 63;
//		double d = 0.03125D;
		double d = 0.0013D;
		this.stoneNoise = this.noiseGen4.generateNoiseOctaves(stoneNoise, x * 16, z * 16, 0, 16, 1, 16, d * 2D, d * 2D, d * 2D);
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				BiomeGenBase biome = biomes[j + i * 16];
				biome.genTerrainBlocks(this.worldObj, this.rand, blockArray, metaArray, x * 16 + i, z * 16 + j, this.stoneNoise[j + i * 16]);
			}
		}
	}


	@Override
	public Chunk loadChunk(int x, int z) {
		return this.provideChunk(x, z);
	}


	@Override
	public void populate(IChunkProvider provider, int x, int z) {
		
		//Dont bother with adding features (at least not yet...)
		
//		BlockFalling.fallInstantly = true;
//		
//		int i = x * 16;
//		int j = z * 16;
//		BiomeGenBase biome = this.worldObj.getBiomeGenForCoords(i + 16, j + 16);
//		//this.rand.setSeed(this.worldObj.getSeed());
//		long i1 = this.rand.nextLong() / 2L * 2L + 1L;
//        long j1 = this.rand.nextLong() / 2L * 2L + 1L;
//		
//		BlockFalling.fallInstantly = false;
		
	}


	@Override
	public boolean saveChunks(boolean saveAll, IProgressUpdate progress) {
		return true;
	}


	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}


	@Override
	public boolean canSave() {
		return true;
	}


	@Override
	public String makeString() {
		return "RandomLevelSource... TWO";
	}


	@Override
	public List getPossibleCreatures(EnumCreatureType type, int x, int y, int z) {
		BiomeGenBase biome = this.worldObj.getBiomeGenForCoords(x, z);
		return biome.getSpawnableList(type);
	}


	@Override
	public ChunkPosition func_147416_a(World world, String structureType, int x, int y, int z) {
		return null; //Find closest structure; No structures to find (at least not yet...)
	}


	@Override
	public int getLoadedChunkCount() {
		return 0;
	}


	@Override
	public void recreateStructures(int x, int z) {}


	@Override
	public void saveExtraData() {}
	
	

}
