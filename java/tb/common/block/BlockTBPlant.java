 package tb.common.block;
 
 import cpw.mods.fml.relauncher.Side;
 import cpw.mods.fml.relauncher.SideOnly;
 import java.util.ArrayList;
 import java.util.Random;
 import net.minecraft.block.Block;
 import net.minecraft.block.BlockBush;
 import net.minecraft.block.IGrowable;
 import net.minecraft.client.renderer.texture.IIconRegister;
 import net.minecraft.init.Blocks;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.IIcon;
 import net.minecraft.world.IBlockAccess;
 import net.minecraft.world.World;
 import net.minecraftforge.common.EnumPlantType;
 import net.minecraftforge.common.IPlantable;
 import net.minecraftforge.common.util.ForgeDirection;
 
 
 
 public class BlockTBPlant
   extends BlockBush
   implements IGrowable
 {
   public int growthStages;
   public int growthDelay;
   public boolean requiresFarmland;
   public IIcon[] growthIcons;
   public ItemStack dropItem;
   public ItemStack dropSeed;
   
   public BlockTBPlant(int stages, int delay, boolean isCrop) {
     this.growthStages = stages;
     this.growthDelay = delay;
     this.requiresFarmland = isCrop;
     setTickRandomly(true);
     float f = 0.5F;
     if (isCrop) {
       setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
     } else {
       setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.75F, 0.75F);
     } 
     setHardness(0.0F);
     setStepSound(soundTypeGrass);
     disableStats();
   }
 
   
   @SideOnly(Side.CLIENT)
   public Item getItem(World w, int x, int y, int z) {
     return (this.dropSeed != null) ? this.dropSeed.getItem() : Item.getItemFromBlock((Block)this);
   }
 
   
   protected boolean canPlaceBlockOn(Block b) {
     return this.requiresFarmland ? ((b == Blocks.farmland)) : true;
   }
 
   
   public void updateTick(World w, int x, int y, int z, Random rnd) {
     super.updateTick(w, x, y, z, rnd);
     
     if (w.getBlockLightValue(x, y + 1, z) >= 9) {
       
       int l = w.getBlockMetadata(x, y, z);
       
       if (l < this.growthStages) {
         
         float f = calculateGrowth(w, x, y, z);
         
         if (rnd.nextInt((int)(this.growthDelay / f) + 1) == 0) {
           
           l++;
           w.setBlockMetadataWithNotify(x, y, z, l, 2);
         } 
       } 
     } 
   }
 
 
   
   public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
     return this.requiresFarmland ? EnumPlantType.Crop : EnumPlantType.Plains;
   }
 
   
   private float calculateGrowth(World w, int x, int y, int z) {
     float f = 1.0F;
     Block block = w.getBlock(x, y, z - 1);
     Block block1 = w.getBlock(x, y, z + 1);
     Block block2 = w.getBlock(x - 1, y, z);
     Block block3 = w.getBlock(x + 1, y, z);
     Block block4 = w.getBlock(x - 1, y, z - 1);
     Block block5 = w.getBlock(x + 1, y, z - 1);
     Block block6 = w.getBlock(x + 1, y, z + 1);
     Block block7 = w.getBlock(x - 1, y, z + 1);
     boolean flag = (block2 == this || block3 == this);
     boolean flag1 = (block == this || block1 == this);
     boolean flag2 = (block4 == this || block5 == this || block6 == this || block7 == this);
     
     for (int l = x - 1; l <= x + 1; l++) {
       
       for (int i1 = z - 1; i1 <= z + 1; i1++) {
         
         float f1 = 0.0F;
         
         if (w.getBlock(l, y - 1, i1).canSustainPlant((IBlockAccess)w, l, y - 1, i1, ForgeDirection.UP, (IPlantable)this)) {
           
           f1 = 1.0F;
           
           if (w.getBlock(l, y - 1, i1).isFertile(w, l, y - 1, i1))
           {
             f1 = 3.0F;
           }
         } 
         
         if (l != x || i1 != z)
         {
           f1 /= 4.0F;
         }
         
         f += f1;
       } 
     } 
     
     if (flag2 || (flag && flag1))
     {
       f /= 2.0F;
     }
     
     return f;
   }
 
 
   
   public boolean func_149851_a(World w, int x, int y, int z, boolean remote) {
     return (w.getBlockMetadata(x, y, z) < this.growthStages - 1);
   }
 
 
   
   public boolean func_149852_a(World w, Random r, int x, int y, int z) {
     return (w.getBlockLightValue(x, y + 1, z) >= 9);
   }
 
 
 
   
   public void func_149853_b(World w, Random r, int x, int y, int z) {
     w.setBlockMetadataWithNotify(x, y, z, Math.min(this.growthStages - 1, w.getBlockMetadata(x, y, z) + r.nextInt(3) + 1), 3);
   }
 
   
   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister reg) {
     this.growthIcons = new IIcon[this.growthStages];
     for (int i = 0; i < this.growthStages; i++) {
       this.growthIcons[i] = reg.registerIcon(this.textureName + "stage_" + i);
     }
   }
   
   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
     return this.growthIcons[(meta >= this.growthIcons.length) ? (this.growthIcons.length - 1) : meta];
   }
 
   
   public int getRenderType() {
     return this.requiresFarmland ? 6 : 1;
   }
 
 
   
   public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
     ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
     
     if (metadata >= this.growthStages - 1) {
       int i;
       for (i = 0; i < ((world.rand.nextDouble() * fortune > 0.75D) ? 2 : 1); i++) {
         
         if (world.rand.nextInt(this.growthStages) <= metadata)
         {
           if (this.dropSeed != null) {
             
             ret.add(this.dropSeed.copy());
             if (this.dropSeed.getItem() instanceof net.minecraft.item.ItemBlock) {
               break;
             }
           } 
         }
       } 
       for (i = 0; i < 1 + world.rand.nextInt(fortune + 1); i++) {
         
         if (world.rand.nextInt(this.growthStages) <= metadata)
         {
           if (this.dropItem != null) {
             ret.add(this.dropItem.copy());
           }
         }
       } 
     } else if (this.dropSeed != null) {
       ret.add(this.dropSeed.copy());
     } 
     return ret;
   }
 }


