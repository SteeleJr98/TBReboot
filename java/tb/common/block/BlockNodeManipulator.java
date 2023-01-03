 package tb.common.block;
 
 import net.minecraft.block.Block;
 import net.minecraft.block.BlockContainer;
 import net.minecraft.block.material.Material;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.item.EntityItem;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.ItemStack;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.world.World;
 import tb.common.tile.TileNodeManipulator;
 import tb.init.TBItems;
 
 public class BlockNodeManipulator
   extends BlockContainer
 {
   public BlockNodeManipulator() {
     super(Material.rock);
   }
 
   
   public TileEntity createNewTileEntity(World w, int meta) {
     return (TileEntity)new TileNodeManipulator();
   }
 
   
   public boolean isOpaqueCube() {
     return false;
   }
 
   
   public boolean renderAsNormalBlock() {
     return false;
   }
 
   
   public int getRenderType() {
     return 4331810;
   }
 
   
   public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int side, float vecX, float vecY, float vecZ) {
     if (p.getCurrentEquippedItem() != null) {
       
       ItemStack current = p.getCurrentEquippedItem();
       if (current.getItem() instanceof tb.common.item.ItemNodeFoci) {
         
         if (w.getBlockMetadata(x, y, z) != 0) {
           
           int meta = w.getBlockMetadata(x, y, z);
           ItemStack stk = new ItemStack(TBItems.nodeFoci, 1, meta - 1);
           EntityItem itm = new EntityItem(w, x + 0.5D, y, z + 0.5D, stk);
           if (!w.isRemote)
             w.spawnEntityInWorld((Entity)itm); 
         } 
         w.setBlockMetadataWithNotify(x, y, z, current.getItemDamage() + 1, 3);
         p.destroyCurrentEquippedItem();
         
         return true;
       } 
     } else {
       
       if (w.getBlockMetadata(x, y, z) != 0) {
         
         int meta = w.getBlockMetadata(x, y, z);
         ItemStack stk = new ItemStack(TBItems.nodeFoci, 1, meta - 1);
         EntityItem itm = new EntityItem(w, x + 0.5D, y, z + 0.5D, stk);
         if (!w.isRemote)
           w.spawnEntityInWorld((Entity)itm); 
       } 
       w.setBlockMetadataWithNotify(x, y, z, 0, 3);
     } 
     return true;
   }
 
 
   
   public void breakBlock(World w, int x, int y, int z, Block b, int meta) {
     if (meta > 0) {
       
       ItemStack foci = new ItemStack(TBItems.nodeFoci, 1, meta - 1);
       EntityItem itm = new EntityItem(w, x + 0.5D, y + 0.5D, z + 0.5D, foci);
       if (!w.isRemote)
         w.spawnEntityInWorld((Entity)itm); 
     } 
     super.breakBlock(w, x, y, z, b, meta);
   }
 }


