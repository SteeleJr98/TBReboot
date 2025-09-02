package tb.common.block;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.entities.EntityAspectOrb;

public class BlockAshroom extends BlockTBPlant {
	
	public BlockAshroom(int stages, int delay, boolean isCrop) {
		super(stages, delay, isCrop);
	}



	public void func_149853_b(World w, Random r, int x, int y, int z) {
		int meta = w.getBlockMetadata(x, y, z);
		w.setBlockMetadataWithNotify(x, y, z, Math.min(this.growthStages, meta + 1), 3);
	}


	protected boolean func_150109_e(World p_150109_1_, int p_150109_2_, int p_150109_3_, int p_150109_4_) {
		if (!canPlaceBlockAt(p_150109_1_, p_150109_2_, p_150109_3_, p_150109_4_)) {

			if (p_150109_1_.getBlock(p_150109_2_, p_150109_3_, p_150109_4_) == this) {

				dropBlockAsItem(p_150109_1_, p_150109_2_, p_150109_3_, p_150109_4_, p_150109_1_.getBlockMetadata(p_150109_2_, p_150109_3_, p_150109_4_), 0);
				p_150109_1_.setBlockToAir(p_150109_2_, p_150109_3_, p_150109_4_);
			} 

			return false;
		} 


		return true;
	}




	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		if (metadata >= this.growthStages - 1)
		{
			for (int i = 0; i < 8 + world.rand.nextInt(32); i++) {

				Aspect primal = Aspect.getPrimalAspects().get(world.rand.nextInt(Aspect.getPrimalAspects().size()));
				EntityAspectOrb orb = new EntityAspectOrb(world, x, y, z, primal, 1);
				if (!world.isRemote)
					world.spawnEntityInWorld((Entity)orb); 
			} 
		}
		return super.getDrops(world, x, y, z, metadata, fortune);
	}
}


