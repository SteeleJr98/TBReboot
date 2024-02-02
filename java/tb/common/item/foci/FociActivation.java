 package tb.common.item.foci;
 
 import cpw.mods.fml.relauncher.Side;
 import cpw.mods.fml.relauncher.SideOnly;
 import java.util.List;
 import net.minecraft.block.Block;
 import net.minecraft.client.renderer.texture.IIconRegister;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.ItemStack;
 import net.minecraft.nbt.NBTBase;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.util.ChatComponentText;
 import net.minecraft.util.IChatComponent;
 import net.minecraft.util.MovingObjectPosition;
 import net.minecraft.util.StatCollector;
 import net.minecraft.util.Vec3;
 import net.minecraft.world.World;
import tb.utils.DummySteele;
import thaumcraft.api.aspects.Aspect;
 import thaumcraft.api.aspects.AspectList;
 import thaumcraft.api.wands.FocusUpgradeType;
 import thaumcraft.api.wands.ItemFocusBasic;
 import thaumcraft.common.items.wands.WandManager;
 
 public class FociActivation
   extends ItemFocusBasic
 {
   public int getFocusColor(ItemStack focusstack) {
     return 11184810;
   }
   
   public String getSortingHelper(ItemStack focusstack) {
     String out = "AC";
     for (short id : getAppliedUpgrades(focusstack)) {
       out = out + id;
     }
     return out;
   }
   
   public AspectList getVisCost(ItemStack focusstack) {
     return (new AspectList()).add(Aspect.ORDER, 5).add(Aspect.EARTH, 10);
   }
 
   
   public int getActivationCooldown(ItemStack focusstack) {
     return 0;
   }
   
   public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack focusstack, int rank) {
     return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency };
   }
 
   
   public ItemStack onFocusRightClick(ItemStack wandstack, World world, EntityPlayer player, MovingObjectPosition movingobjectposition) {
     if (wandstack == null) {
       return wandstack;
     }
     if (player == null) {
       return wandstack;
     }
     if (player.isSneaking()) {
       
       if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
         
         NBTTagCompound fociTag = DummySteele.getStackTag(wandstack).getCompoundTag("focus").getCompoundTag("tag");
         fociTag.setInteger("blockX", movingobjectposition.blockX);
         fociTag.setInteger("blockY", movingobjectposition.blockY);
         fociTag.setInteger("blockZ", movingobjectposition.blockZ);
         fociTag.setInteger("dim", player.dimension);
         ((NBTTagCompound)DummySteele.getStackTag(wandstack).getTag("focus")).setTag("tag", (NBTBase)fociTag);
         player.swingItem();
         WandManager.consumeVisFromInventory(player, (new AspectList()).add(Aspect.ORDER, 100));
       } 
     } else {
       
       NBTTagCompound fociTag = DummySteele.getStackTag(wandstack).getCompoundTag("focus").getCompoundTag("tag");
       if (fociTag.hasKey("blockX")) {
         
         int x = fociTag.getInteger("blockX");
         int y = fociTag.getInteger("blockY");
         int z = fociTag.getInteger("blockZ");
         
         ItemStack foci = ItemStack.loadItemStackFromNBT(DummySteele.getStackTag(wandstack).getCompoundTag("focus"));
         
         int potencyLevel = getUpgradeLevel(foci, FocusUpgradeType.potency);
         
         int maxRange = 32 * (potencyLevel + 1);
         
         double range = player.getDistance(x, y, z);
         
         int aspectCost = 10;
         
         if (!WandManager.consumeVisFromInventory(player, (new AspectList()).add(Aspect.EARTH, aspectCost).add(Aspect.ORDER, aspectCost / 2))) {
           return wandstack;
         }
         if (range > maxRange) {
           
           if (player.worldObj.isRemote)
             player.addChatMessage((IChatComponent)new ChatComponentText(StatCollector.translateToLocal("tb.txt.tooFar"))); 
           return wandstack;
         } 
         
         int dim = fociTag.getInteger("dim");
         
         if (dim != player.dimension) {
           
           if (player.worldObj.isRemote)
             player.addChatMessage((IChatComponent)new ChatComponentText(StatCollector.translateToLocal("tb.txt.wrongDimension"))); 
           return wandstack;
         } 
         
         if (world.checkChunksExist(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1))
         {
           if (world.blockExists(x, y, z)) {
             
             Block b = world.getBlock(x, y, z);
             Vec3 vec = player.getLookVec();
             b.onBlockActivated(world, x, y, z, player, 0, (float)vec.xCoord, (float)vec.yCoord, (float)vec.zCoord);
           } 
         }
 
         
         foci = null;
       } 
     } 
     return wandstack;
   }
 
   
   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister reg) {
     super.registerIcons(reg);
     this.icon = reg.registerIcon(getIconString());
   }
 
 
   
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
     super.addInformation(stack, player, list, par4);
     
     NBTTagCompound tag = DummySteele.getStackTag(stack);
     
     if (tag.hasKey("blockX")) {
       
       list.add("X: " + tag.getInteger("blockX"));
       list.add("Y: " + tag.getInteger("blockY"));
       list.add("Z: " + tag.getInteger("blockZ"));
       list.add("Dim: " + tag.getInteger("dim"));
     } 
   }
 }


