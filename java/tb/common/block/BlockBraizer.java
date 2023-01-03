 package tb.common.block;
 
 import DummyCore.Utils.MathUtils;
 import java.util.Random;
 import net.minecraft.block.BlockContainer;
 import net.minecraft.block.material.Material;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.world.World;
 import tb.common.tile.TileBraizer;
 
 
 
 public class BlockBraizer
   extends BlockContainer
 {
   public BlockBraizer() {
     super(Material.rock);
     setHardness(1.0F);
     setResistance(1.0F);
   }
 
 
   
   public TileEntity createNewTileEntity(World w, int meta) {
     return (TileEntity)new TileBraizer();
   }
 
   
   public boolean isOpaqueCube() {
     return false;
   }
 
   
   public boolean renderAsNormalBlock() {
     return false;
   }
 
   
   public int getRenderType() {
     return 1196797;
   }
 
   
   public void randomDisplayTick(World w, int x, int y, int z, Random r) {
     if (w.getBlockMetadata(x, y, z) > 0) {
       
       w.spawnParticle("flame", x + 0.5D + MathUtils.randomDouble(r) / 4.0D, y + 0.6D, z + 0.5D + MathUtils.randomDouble(r) / 4.0D, 0.0D, 0.04D, 0.0D);
       for (int i = 0; i < 10; i++) {
         w.spawnParticle("smoke", x + 0.5D + MathUtils.randomDouble(r) / 4.0D, y + 0.7D, z + 0.5D + MathUtils.randomDouble(r) / 4.0D, 0.0D, r.nextDouble() / 20.0D, 0.0D);
       }
       w.playSound(x + 0.5D, y + 0.5D, z + 0.5D, "thaumicbases:fire.loop", 0.1F, 0.1F, false);
     } 
   }
 }


