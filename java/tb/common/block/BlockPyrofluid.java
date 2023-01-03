 package tb.common.block;
 
 import cpw.mods.fml.relauncher.Side;
 import cpw.mods.fml.relauncher.SideOnly;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Random;
 import net.minecraft.block.Block;
 import net.minecraft.block.material.Material;
 import net.minecraft.client.renderer.texture.IIconRegister;
 import net.minecraft.creativetab.CreativeTabs;
 import net.minecraft.init.Items;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.AxisAlignedBB;
 import net.minecraft.util.IIcon;
 import net.minecraft.world.IBlockAccess;
 import net.minecraft.world.World;
 import tb.utils.TBConfig;
 
 
 public class BlockPyrofluid
   extends Block
 {
   public static IIcon fluidIcon;
   public static IIcon staticIcon;
   
   public BlockPyrofluid() {
     super(Material.lava);
     float f = 0.0F;
     float f1 = 0.0F;
     setBlockBounds(0.0F + f1, 0.0F + f, 0.0F + f1, 1.0F + f1, 1.0F + f, 1.0F + f1);
     setTickRandomly(true);
   }
 
   
   public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
     return false;
   }
 
   
   public boolean renderAsNormalBlock() {
     return false;
   }
 
   
   public boolean isOpaqueCube() {
     return false;
   }
 
   
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
     return null;
   }
 
   
   public int getRenderType() {
     return 4;
   }
 
   
   public int idDropped(int par1, Random par2Random, int par3) {
     return 0;
   }
 
   
   public int quantityDropped(Random par1Random) {
     return 0;
   }
 
   
   public int tickRate(World par1World) {
     return 5;
   }
 
 
 
 
   
   public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
     double d5 = (par2 + par5Random.nextFloat());
     double d7 = par3 - 0.5D + this.maxY;
     double d6 = (par4 + par5Random.nextFloat());
     if (this.blockMaterial == Material.lava && par1World.getBlock(par2, par3 + 1, par4).getMaterial() == Material.air && !par1World.getBlock(par2, par3 + 1, par4).isOpaqueCube()) {
       
       if (par5Random.nextInt(100) == 0) {
         
         d5 = (par2 + par5Random.nextFloat());
         d7 = par3 + this.maxY;
         d6 = (par4 + par5Random.nextFloat());
         par1World.spawnParticle("lava", d5, d7, d6, 0.0D, 0.0D, 0.0D);
         par1World.playSound(d5, d7, d6, "liquid.lavapop", 0.2F + par5Random.nextFloat() * 0.2F, 0.9F + par5Random.nextFloat() * 0.15F, false);
       } 
       
       if (par5Random.nextInt(200) == 0)
       {
         par1World.playSound(par2, par3, par4, "liquid.lava", 0.2F + par5Random.nextFloat() * 0.2F, 0.9F + par5Random.nextFloat() * 0.15F, false);
       }
     } 
     
     if (par5Random.nextInt(10) == 0 && World.doesBlockHaveSolidTopSurface((IBlockAccess)par1World, par2, par3 - 1, par4) && !par1World.getBlock(par2, par3 - 2, par4).getBlocksMovement((IBlockAccess)par1World, par2, par3, par4))
     {
       par1World.spawnParticle("dripLava", d5, d7, d6, 0.0D, 0.0D, 0.0D);
     }
   }
 
   
   public void updateTick(World w, int x, int y, int z, Random rnd) {
     int meta = w.getBlockMetadata(x, y, z) + 1;
     if (meta > 15) {
       return;
     }
     if (meta >= 8) {
       meta = 15;
     }
     w.setBlockMetadataWithNotify(x, y, z, meta, 3);
   }
 
   
   public void registerBlockIcons(IIconRegister ir) {
     fluidIcon = ir.registerIcon("thaumicbases:blazingFluid/block");
     staticIcon = ir.registerIcon("thaumicbases:blazingFluid/leftovers");
   }
 
 
   
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
     for (int i = 0; i < 8; i++) {
       par3List.add(new ItemStack(par1, 1, i));
     }
     par3List.add(new ItemStack(par1, 1, 15));
   }
 
   
   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
     return (meta == 15) ? staticIcon : fluidIcon;
   }
 
   
   public float getBlockHardness(World w, int x, int y, int z) {
     return (w.getBlockMetadata(x, y, z) == 15) ? 5.0F : -1.0F;
   }
 
   
   public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
     ArrayList<ItemStack> retLst = new ArrayList<ItemStack>();
     
     retLst.add(new ItemStack(Items.blaze_powder, TBConfig.minBlazePowderFromPyrofluid + world.rand.nextInt(TBConfig.maxBlazePowderFromPyrofluid - TBConfig.minBlazePowderFromPyrofluid), 0));
     
     return retLst;
   }
 }


