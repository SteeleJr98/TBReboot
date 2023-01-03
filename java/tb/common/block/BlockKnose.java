 package tb.common.block;
 
 import java.util.ArrayList;
 import net.minecraft.block.Block;
 import net.minecraft.item.ItemStack;
 import net.minecraft.world.World;
 import tb.init.TBBlocks;
 import tb.init.TBItems;
 
 public class BlockKnose
   extends BlockTBPlant
 {
   public BlockKnose(int stages, int delay, boolean isCrop) {
     super(stages, delay, isCrop);
   }
 
   
   public boolean canBlockStay(World w, int x, int y, int z) {
     return (!w.isAirBlock(x, y - 1, z) && canPlaceBlockOn(w.getBlock(x, y - 1, z)));
   }
 
   
   protected boolean canPlaceBlockOn(Block b) {
     return (b == TBBlocks.crystalBlock);
   }
 
 
   
   public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
     ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
     
     if (metadata >= this.growthStages - 1) {
       
       if (this.dropSeed != null)
         ret.add(this.dropSeed.copy()); 
       Block b = world.getBlock(x, y - 1, z);
       if (b == TBBlocks.crystalBlock)
       {
         int md = world.getBlockMetadata(x, y - 1, z);
         ret.add(new ItemStack(TBItems.knoseFragment, 1, md));
       }
     
     }
     else if (this.dropSeed != null) {
       ret.add(this.dropSeed.copy());
     } 
     
     return ret;
   }
 }


