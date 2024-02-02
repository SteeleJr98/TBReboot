 package tb.common.block;
 
 import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.Side;
 import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;
 import net.minecraft.block.Block;
 import net.minecraft.block.BlockAnvil;
 import net.minecraft.client.renderer.texture.IIconRegister;
 import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
 import net.minecraft.world.World;
 import tb.core.TBCore;
import tb.utils.TBUtils;
import thaumcraft.api.crafting.IInfusionStabiliser;
 import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.PlayerKnowledge;
 
 public class BlockVoidAnvil
   extends BlockAnvil
   implements IInfusionStabiliser
 {
   public static final String[] anvilDamageNames = new String[] { "intact", "slightlyDamaged", "veryDamaged" };
   private static final String[] anvilIconNames = new String[] { "top_damaged_0", "top_damaged_1", "top_damaged_2" };
   //private static final ChatComponentText warpText1 = new ChatComponentText("Add warp message ");
   //private static final ChatComponentText warpText2 = new ChatComponentText(EnumChatFormatting.DARK_BLUE + "here");
   
   
   @SideOnly(Side.CLIENT)
   private IIcon[] anvilIcons;
 
   
   public BlockVoidAnvil() {
     setHardness(12.0F);
     setResistance(Float.MAX_VALUE);
     setTickRandomly(true);
     setStepSound(Block.soundTypeAnvil);
     setHarvestLevel("pickaxe", 3);
   }
   
   public void updateHealth(World w, int x, int y, int z, boolean forceUpdate ) {
	   int meta = w.getBlockMetadata(x, y, z);
	   long worldTime = w.getTotalWorldTime();
	   AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x - 5, y - 5, z - 5, x + 5, y + 5, z + 5);
	   List playerList = w.getEntitiesWithinAABB(EntityPlayer.class, box);
	   //System.out.println("Meta: " + meta + " World Time: " + worldTime + " Should Repair: " + (worldTime % 100 == 0 ? "True" : "False") + " Players: " + playerList.toString());
	   

	   if (worldTime % 5 == 0 || forceUpdate) {
		   if (meta / 4 > 0) {
			   w.setBlockMetadataWithNotify(x, y, z, meta - 4, 3);
			   warpPlayers(w, playerList);
		   }
	   }
   }
 
   @Override
   public void updateTick(World w, int x, int y, int z, Random rnd) {
     super.updateTick(w, x, y, z, rnd);
     if (!w.isRemote) {
	     updateHealth(w, x, y, z, false);
     }
   }
   
   @Override
   public int tickRate(World p_149738_1_)
   {
       return 100;
   }
 
   @Override
   public void randomDisplayTick(World w, int x, int y, int z, Random r) {
     Thaumcraft.proxy.sparkle(x + r.nextFloat(), y + r.nextFloat(), z + r.nextFloat(), 2.0F, 5, -0.1F);
     //updateHealth(w, x, y, z, false);
     //System.out.print(w.getBlockMetadata(x, y, z));
   }
 
   
   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
     if (this.anvilRenderSide == 3 && p_149691_1_ == 1) {
       
       int k = (p_149691_2_ >> 2) % this.anvilIcons.length;
       return this.anvilIcons[k];
     } 
 
     
     return this.blockIcon;
   }
 
 
   
   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister p_149651_1_) {
     this.blockIcon = p_149651_1_.registerIcon(getTextureName() + "base");
     this.anvilIcons = new IIcon[anvilIconNames.length];
     
     for (int i = 0; i < this.anvilIcons.length; i++)
     {
       this.anvilIcons[i] = p_149651_1_.registerIcon(getTextureName() + anvilIconNames[i]);
     }
   }
 
   
   public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int side, float vecX, float vecY, float vecZ) {
     if (w.isRemote) {
       return true;
     }
     p.openGui(TBCore.instance, 4331808, w, x, y, z);
     
     return true;
   }
 
 
   
   public boolean canStabaliseInfusion(World w, int x, int y, int z) {
     return true;
   }
   
   public void warpPlayers(World w, List playerList) {

	   if (!playerList.isEmpty()) {
		   for (Object playerObject : playerList) {
			   EntityPlayer player = (EntityPlayer) playerObject;
			   //player.addChatMessage(new ChatComponentText("test"));
			   //Thaumcraft.addWarpToPlayer(player, 1, true); //how much warp to add?
			   TBUtils.addWarpToPlayer(player, 10, 0);

		   }
	   }
   }
 }


