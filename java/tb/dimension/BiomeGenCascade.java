package tb.dimension;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import tb.utils.DummySteele.MystColour;
import tb.utils.TBUtils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

public class BiomeGenCascade extends BiomeGenBase {
	

	public BiomeGenCascade(int id) {
		super(id);
		
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCaveCreatureList.clear();
		this.topBlock = Blocks.grass;
		this.fillerBlock = Blocks.dirt;
//		this.theBiomeDecorator.grassPerChunk = 0;
//		this.theBiomeDecorator.flowersPerChunk = 0;
//		this.theBiomeDecorator.deadBushPerChunk = 0;
//		this.theBiomeDecorator.mushroomsPerChunk = 0;
//		this.theBiomeDecorator.reedsPerChunk = 0;
//		this.theBiomeDecorator.cactiPerChunk = 0;
//		this.theBiomeDecorator.sandPerChunk = 0;
		this.theBiomeDecorator.treesPerChunk = -999;
		
		
		setHeight(new BiomeGenBase.Height(-1.999F, 0F));
		setTemperatureRainfall(1.33F, 0.0F);
		setDisableRain();
		setBiomeName(StatCollector.translateToLocal("tb.biomeCascade.name"));
		setColor(99999999);
	}
	
	@Override
	public BiomeDecorator createBiomeDecorator() {
		return new TBBiomeDecorator();
	}
	
	@Override
	public void decorate(World world, Random random, int chunkX, int chunkZ) {
		
		this.theBiomeDecorator.decorateChunk(world, random, this, chunkX, chunkZ);
		
		
		
		
		int l = chunkX + random.nextInt(16) + 8;
        int i1 = chunkZ + random.nextInt(16) + 8;
        int j1 = random.nextInt(world.getHeightValue(l, i1) + 32);
        //genTallFlowers.generate(world, random, l, j1, i1);
        doDoubleGrass(world, random, l, j1, i1);
		
        l = chunkX + random.nextInt(16) + 8;
        i1 = chunkZ + random.nextInt(16) + 8;
        j1 = random.nextInt(world.getHeightValue(l, i1) + 32);
        //genTallFlowers.generate(world, random, l, j1, i1);
        doSingleGrass(world, random, l, j1, i1);
        
        l = chunkX + random.nextInt(16) + 8;
        i1 = chunkZ + random.nextInt(16) + 8;
        j1 = random.nextInt(world.getHeightValue(l, i1) + 32);
        doNodePlacement(world, l, i1, j1, random);
        
        
	}
	
	private void doDoubleGrass(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {

        for (int l = 0; l < 3; ++l) {
            int i1 = p_76484_3_ + p_76484_2_.nextInt(8) - p_76484_2_.nextInt(8);
            int j1 = p_76484_4_ + p_76484_2_.nextInt(4) - p_76484_2_.nextInt(4);
            int k1 = p_76484_5_ + p_76484_2_.nextInt(8) - p_76484_2_.nextInt(8);

            if (p_76484_1_.isAirBlock(i1, j1, k1) && (!p_76484_1_.provider.hasNoSky || j1 < 254) && Blocks.double_plant.canPlaceBlockAt(p_76484_1_, i1, j1, k1)) {
                Blocks.double_plant.func_149889_c(p_76484_1_, i1, j1, k1, 2, 2);
            }
        }
    }
	
	public void doSingleGrass(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {


        

        for (int l = 0; l < 4; ++l) {
            int i1 = p_76484_3_ + p_76484_2_.nextInt(8) - p_76484_2_.nextInt(8);
            int j1 = p_76484_4_ + p_76484_2_.nextInt(4) - p_76484_2_.nextInt(4);
            int k1 = p_76484_5_ + p_76484_2_.nextInt(8) - p_76484_2_.nextInt(8);

            if (p_76484_1_.isAirBlock(i1, j1, k1) && Blocks.tallgrass.canBlockStay(p_76484_1_, i1, j1, k1)) {
                p_76484_1_.setBlock(i1, j1, k1, Blocks.tallgrass, 1, 2);
                //TBUtils.placeRandomNode(p_76484_1_, i1 + 5, j1 + 5, k1 + 5);
            }
        }
    }
	
	public void doNodePlacement(World world, int x, int y, int z, Random rand) {
		
		for (int l = 0; l < 1; ++l) {
			int i1 = x + rand.nextInt(8) - rand.nextInt(8);
	        int j1 = y + rand.nextInt(4) - rand.nextInt(4);
	        int k1 = z + rand.nextInt(8) - rand.nextInt(8);
			//ThaumcraftWorldGenerator.createRandomNodeAt(world, i1, j1, k1, rand, false, false, false);
			TBUtils.placeRandomNode(world, i1, j1, k1);
		}
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getBiomeGrassColor(int x, int y, int z) {
		MystColour colour = new MystColour(0.9F, 0.1F, 0.9F);
        return getModdedBiomeGrassColor(colour.asInt());
    }
	
//	@Override
//	@SideOnly(Side.CLIENT)
//	public int getSkyColorByTemp(float temp) {
//		MystColour colour = new MystColour(0.8F, 0.1F, 0.1F);
//		return colour.asInt();
//	}
	
	
	@Override
	public void genTerrainBlocks(World p_150560_1_, Random p_150560_2_, Block[] p_150560_3_, byte[] p_150560_4_, int p_150560_5_, int p_150560_6_, double p_150560_7_) {
        boolean flag = true;
        Block block = this.topBlock;
        byte b0 = (byte)(this.field_150604_aj & 255);
        Block block1 = this.fillerBlock;
        int k = -1;
        int l = (int)(p_150560_7_ / 3.0D + 3.0D + p_150560_2_.nextDouble() * 0.25D);
        int i1 = p_150560_5_ & 15;
        int j1 = p_150560_6_ & 15;
        int k1 = p_150560_3_.length / 256;

        for (int l1 = 255; l1 >= 0; --l1)
        {
            int i2 = (j1 * 16 + i1) * k1 + l1;

            if (l1 <= 0 + p_150560_2_.nextInt(5))
            {
                p_150560_3_[i2] = Blocks.bedrock;
            }
            else
            {
                Block block2 = p_150560_3_[i2];

                if (block2 != null && block2.getMaterial() != Material.air)
                {
                    if (block2 == Blocks.stone)
                    {
                        if (k == -1)
                        {
                            if (l <= 0)
                            {
                                block = null;
                                b0 = 0;
                                block1 = Blocks.stone;
                            }
                            else if (l1 >= 59 && l1 <= 64)
                            {
                                block = this.topBlock;
                                b0 = (byte)(this.field_150604_aj & 255);
                                block1 = this.fillerBlock;
                            }

                            if (l1 < 63 && (block == null || block.getMaterial() == Material.air))
                            {
                                if (this.getFloatTemperature(p_150560_5_, l1, p_150560_6_) < 0.15F)
                                {
                                    block = Blocks.ice;
                                    b0 = 0;
                                }
                                else
                                {
                                    block = Blocks.water;
                                    b0 = 0;
                                }
                            }

                            k = l;

                            if (l1 >= 62)
                            {
                                p_150560_3_[i2] = block;
                                p_150560_4_[i2] = b0;
                            }
                            else if (l1 < 56 - l)
                            {
                                block = null;
                                block1 = Blocks.stone;
                                p_150560_3_[i2] = this.topBlock;
                            }
                            else
                            {
                                p_150560_3_[i2] = block1;
                            }
                        }
                        else if (k > 0)
                        {
                            --k;
                            p_150560_3_[i2] = block1;

                            if (k == 0 && block1 == Blocks.sand)
                            {
                                k = p_150560_2_.nextInt(4) + Math.max(0, l1 - 63);
                                block1 = Blocks.sandstone;
                            }
                        }
                    }
                }
                else
                {
                    k = -1;
                }
            }
        }
    }
	
	

}
