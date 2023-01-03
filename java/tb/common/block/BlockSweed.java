 package tb.common.block;
 
 import java.util.ArrayList;
 import java.util.Random;
 import net.minecraft.block.Block;
 import net.minecraft.init.Blocks;
 import net.minecraft.init.Items;
 import net.minecraft.item.ItemStack;
 import net.minecraft.world.World;
 import net.minecraftforge.common.util.ForgeDirection;
 
 
 
 public class BlockSweed
   extends BlockTBPlant
 {
   public BlockSweed(int stages, int delay, boolean isCrop) {
     super(stages, delay, isCrop);
   }
 
   
   protected boolean canPlaceBlockOn(Block b) {
     return (b != null && (b == Blocks.grass || b == Blocks.dirt || b instanceof net.minecraft.block.BlockGrass || b instanceof net.minecraft.block.BlockDirt));
   }
 
   
   public void updateTick(World w, int x, int y, int z, Random rnd) {
     super.updateTick(w, x, y, z, rnd);
     if (w.getBlockMetadata(x, y, z) == this.growthStages - 1 && !w.isRemote) {
       
       ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[2 + w.rand.nextInt(4)];
       int newX = x + dir.offsetX;
       int newZ = z + dir.offsetZ;
       int newY = findSutableY(w, newX, y, newZ);
       if (canPlaceBlockOn(w.getBlock(newX, newY - 1, newZ)) && w.isAirBlock(newX, newY, newZ)) {
         w.setBlock(newX, newY, newZ, (Block)this, 0, 3);
       }
     } 
   }
   
   public int findSutableY(World w, int x, int y, int z) {
     int bY = y;
     y++;
     while (!canPlaceBlockOn(w.getBlock(x, y, z)) && y > bY - 2) {
       y--;
     }
     return y + 1;
   }
 
 
   
   public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
     ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
     
     if (metadata >= this.growthStages - 1) {
       int i;
       for (i = 0; i < 1 + fortune; i++) {
         if (world.rand.nextInt(this.growthStages) <= metadata && 
           this.dropSeed != null)
           ret.add(this.dropSeed.copy()); 
       } 
       for (i = 0; i < 3 + fortune; i++) {
         if (world.rand.nextBoolean())
           ret.add(new ItemStack(Items.sugar)); 
       } 
       if (world.rand.nextBoolean()) {
         ret.add(new ItemStack(Items.reeds));
       }
     }
     else if (this.dropSeed != null) {
       ret.add(this.dropSeed.copy());
     } 
     return ret;
   }
 }


