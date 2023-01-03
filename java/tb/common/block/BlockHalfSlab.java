 package tb.common.block;
 
 import cpw.mods.fml.relauncher.Side;
 import cpw.mods.fml.relauncher.SideOnly;
 import java.util.List;
 import java.util.Random;
 import net.minecraft.block.Block;
 import net.minecraft.block.BlockSlab;
 import net.minecraft.block.material.Material;
 import net.minecraft.creativetab.CreativeTabs;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.IIcon;
 import net.minecraft.world.World;
 import tb.init.TBBlocks;
 import thaumcraft.api.crafting.IInfusionStabiliser;
 
 public class BlockHalfSlab
   extends BlockSlab
   implements IInfusionStabiliser {
   public static Block[] parents = new Block[] { TBBlocks.eldritchArk, TBBlocks.oldBrick, TBBlocks.oldCobble, TBBlocks.oldCobbleMossy, TBBlocks.oldDiamond, TBBlocks.oldGold, TBBlocks.oldIron, TBBlocks.oldLapis };
 
 
 
 
 
 
 
 
 
 
   
   public BlockHalfSlab(boolean fullBlock, Material material) {
     super(fullBlock, material);
   }
 
   
   public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
     return Item.getItemFromBlock(TBBlocks.genericSlab);
   }
 
   
   protected ItemStack createStackedBlock(int p_149644_1_) {
     return new ItemStack(Item.getItemFromBlock(TBBlocks.genericSlab), 2, p_149644_1_ & 0x7);
   }
 
   
   @SideOnly(Side.CLIENT)
   public Item getItem(World w, int x, int y, int z) {
     return Item.getItemFromBlock(TBBlocks.genericSlab);
   }
 
   
   public String func_150002_b(int meta) {
     return getUnlocalizedName() + parents[meta].getUnlocalizedName();
   }
 
   
   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
     return parents[meta & 0x7].getIcon(side, 0);
   }
 
 
   
   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item itm, CreativeTabs tab, List lst) {
     for (int i = 0; i < parents.length; i++)
     {
       lst.add(new ItemStack(itm, 1, i));
     }
   }
 
   
   public boolean canStabaliseInfusion(World world, int x, int y, int z) {
     int meta = world.getBlockMetadata(x, y, z) & 0x7;
     return (parents[meta] instanceof IInfusionStabiliser) ? ((IInfusionStabiliser)IInfusionStabiliser.class.cast(parents[meta])).canStabaliseInfusion(world, x, y, z) : false;
   }
 }


