package tb.dimension;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.GRASS;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

public class TBBiomeDecorator extends BiomeDecorator {
	
	public TBBiomeDecorator() {
		this.sandGen = null;
        this.gravelAsSandGen = null;
        this.dirtGen = new WorldGenMinable(Blocks.dirt, 8);
        this.gravelGen = null;
        this.coalGen = null;
        this.ironGen = null;
        this.goldGen = null;
        this.redstoneGen = null;
        this.diamondGen = null;
        this.lapisGen = null;
        this.yellowFlowerGen = null;
        this.mushroomBrownGen = null;
        this.mushroomRedGen = null;
        this.bigMushroomGen = null;
        this.reedGen = null;
        this.cactusGen = null;
        this.waterlilyGen = null;
        this.flowersPerChunk = -999;
        this.grassPerChunk = 1;
        this.sandPerChunk = -999;
        this.sandPerChunk2 = -999;
        this.clayPerChunk = -999;
        this.generateLakes = false;
	}
	
	@Override
	public void decorateChunk(World world, Random rand, BiomeGenBase biome, int chunkX, int chunkZ) {
		if (this.currentWorld != null) {
			throw new RuntimeException("what????");
		}
		else {
			
			this.currentWorld = world;
			this.randomGenerator = rand;
			this.chunk_X = chunkX;
			this.chunk_Z = chunkZ;
			
			this.genGrass(biome);
			this.genDecorations(biome);
			
			this.currentWorld = null;
			this.randomGenerator = null;
		}
	}
	
	protected void genGrass(BiomeGenBase base) {
		
		int i;
		int j;
		int k;
		int l;
        int i1;
		
		boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, GRASS);
//        for (j = 0; doGen && j < this.grassPerChunk; ++j)
//        {
//            k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
//            l = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
//            i1 = nextInt(this.currentWorld.getHeightValue(k, l) * 2);
//            WorldGenerator worldgenerator = base.getRandomWorldGenForGrass(this.randomGenerator);
//            worldgenerator.generate(this.currentWorld, this.randomGenerator, k, i1, l);
//        }
	}
	
	@Override
	protected void generateOres() {
		
	}
	
	@Override
	protected void genDecorations(BiomeGenBase biome) {
		
	}
	
	private int nextInt(int i) {
        if (i <= 1)
            return 0;
        return this.randomGenerator.nextInt(i);
	}
	
}
