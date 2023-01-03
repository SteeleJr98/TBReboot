 package tb.common.item;
 
 import cpw.mods.fml.relauncher.Side;
 import cpw.mods.fml.relauncher.SideOnly;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.List;
 import net.minecraft.client.renderer.texture.IIconRegister;
 import net.minecraft.creativetab.CreativeTabs;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 import net.minecraft.potion.Potion;
 import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.steelehook.SteeleCore.Handlers.MessageLogging;
import tb.api.ITobacco;
import tb.core.TBCore;
import tb.utils.TBUtils;
 import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
 import thaumcraft.common.entities.monster.EntityWisp;
import thaumcraft.common.lib.research.PlayerKnowledge;
 
 public class TBTobacco
   extends Item
   implements ITobacco
 {
   public TBTobacco() {
     setHasSubtypes(true);
   }
   
   public static final String[] names = new String[] { "tobacco_pile", "tobacco_eldritch", "tobacco_fighting", "tobacco_hunger", "tobacco_knowledge", "tobacco_mining", "tobacco_sanity", "tobacco_tainted", "tobacco_wispy"};
   private static final int maxAspect = 250;
 
 
 
 
 
 
   
   public void performTobaccoEffect(EntityPlayer smoker, int metadata, boolean isSilverwood) {
     ArrayList<Aspect> aspects;
     ArrayList<Integer> aspectNum;
     Collection<Aspect> pa;
     EntityWisp wisp;
     switch (metadata) {
 
       
       case 0:
         if (isSilverwood && smoker.worldObj.rand.nextFloat() <= 0.3F) {
           TBUtils.addWarpToPlayer(smoker, -1, 0);
         }
         if (!isSilverwood && smoker.worldObj.rand.nextFloat() <= 0.1F) {
           TBUtils.addWarpToPlayer(smoker, -1, 0);
         }
         break;
 
       
       case 1:
         if (!smoker.worldObj.isRemote) {
           
           smoker.addPotionEffect(new PotionEffect(Config.potionDeathGazeID, 2000, 0, true));
           if (!isSilverwood) {
             TBUtils.addWarpToPlayer(smoker, 3, 0);
           }
         } 
         break;
 
       
       case 2:
         if (!smoker.worldObj.isRemote) {
           
           smoker.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 8000, 1, true));
           if (isSilverwood && smoker.worldObj.rand.nextFloat() <= 0.1F) {
             smoker.addPotionEffect(new PotionEffect(Potion.resistance.id, 8000, 0, true));
           }
         } 
         break;
       
       case 3:
         if (!smoker.worldObj.isRemote) {
           
           smoker.getFoodStats().addStats(3, 3.0F);
           if (!isSilverwood && smoker.worldObj.rand.nextFloat() <= 0.4F) {
             smoker.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0, true));
           }
         } 
         break;
       
       case 4: //fix knowledge cap bug
         if (!smoker.worldObj.isRemote) {
           
           //ArrayList<Aspect> arrayList = new ArrayList<Aspect>(Aspect.aspects.values());
           //Collection<Aspect> collection = Aspect.aspects.values();
           ArrayList<Aspect> validList = new ArrayList<Aspect>();
        	 
           AspectList pKnow = Thaumcraft.proxy.playerKnowledge.getAspectsDiscovered(smoker.getDisplayName());
           
           Aspect[] pList = pKnow.getAspects();
           for (Aspect a : pList) {
        	   if (Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(smoker.getDisplayName(), a) < maxAspect) {
        		   validList.add(a);
        	   }
           }
           
           if (TBCore.isDev) {MessageLogging.sendFromClient(smoker, "Valid aspect list length, " + validList.size());}
           //Aspect[] validArray = (Aspect[]) validList.toArray();
           
           if (validList.size() > 0) {
	           for (int i = 0; i < (isSilverwood ? 20 : 10); i++) {
	        	 Aspect a = validList.get(smoker.worldObj.rand.nextInt(validList.size()));
	        	 if (TBCore.isDev) {MessageLogging.sendFromClient(smoker, a.getName());}
	        	 
	             TBUtils.addAspectToKnowledgePool(smoker, a, (short)(isSilverwood ? 2 : 1));
	             if (a == Aspect.TAINT && !isSilverwood) {
	               TBUtils.addWarpToPlayer(smoker, 1, 0);
	             }
	           }
           }
           else {
        	   smoker.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("tb.txt.fullSmoke1")));
        	   smoker.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("tb.txt.fullSmoke2"))); 
           }
         } 
         break;
       
       case 5:
         if (!smoker.worldObj.isRemote) {
           
           smoker.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 8000, 1, true));
           if (isSilverwood && smoker.worldObj.rand.nextFloat() <= 0.3F) {
             smoker.addPotionEffect(new PotionEffect(Potion.nightVision.id, 8000, 0, true));
           }
         } 
         break;
       
       case 6:
         if (!smoker.worldObj.isRemote) {
           
           if (smoker.worldObj.rand.nextFloat() <= (isSilverwood ? 0.6F : 0.4F))
           {
             TBUtils.addWarpToPlayer(smoker, -1, 0);
           }
           if (isSilverwood && smoker.worldObj.rand.nextFloat() <= 0.1F) {
             TBUtils.addWarpToPlayer(smoker, -1, 1);
           }
         } 
         break;
       
       case 7:
         if (!smoker.worldObj.isRemote) {
           
           if (!isSilverwood) {
             
             TBUtils.addWarpToPlayer(smoker, 1 + smoker.worldObj.rand.nextInt(3), 0);
             if (smoker.worldObj.rand.nextFloat() <= 0.4F)
               TBUtils.addWarpToPlayer(smoker, 1, 1); 
             break;
           } 
           ItemStack stk = smoker.getCurrentEquippedItem();
           if (stk != null) {
             
             smoker.renderBrokenItemStack(stk);
             smoker.inventory.setInventorySlotContents(smoker.inventory.currentItem, null);
           } 
           return;
         } 
         break;
 
 
       
       case 8:
         aspects = new ArrayList<Aspect>();
         pa = Aspect.aspects.values();
         for (Aspect aspect : pa)
         {
           aspects.add(aspect);
         }
         
         if (isSilverwood) {
           aspects.remove(Aspect.TAINT);
         }
         wisp = new EntityWisp(smoker.worldObj);
         wisp.setPositionAndRotation(smoker.posX, smoker.posY, smoker.posZ, 0.0F, 0.0F);
         if (!smoker.worldObj.isRemote) {
           
           wisp.setType(((Aspect)aspects.get(smoker.worldObj.rand.nextInt(aspects.size()))).getTag());
           smoker.worldObj.spawnEntityInWorld((Entity)wisp);
         } 
         break;
         
       case 9:
    	   aspects = new ArrayList<Aspect>(Aspect.aspects.values());
    	   //aspectNum = new ArrayList<Integer>();
    	   AspectList pKnow = Thaumcraft.proxy.playerKnowledge.getAspectsDiscovered(smoker.getDisplayName());
    	   Aspect[] aList = pKnow.getAspects();
    	   for (int i = 0; i < aList.length; i++) {
    		   //System.out.println(aList[i].getName());
    		   MessageLogging.sendFromClient(smoker, aList[i].getName());
    		   TBUtils.addAspectToKnowledgePool(smoker, aList[i], (short)50);
    		   
    	   }
    	break;
    	   
    	   
     } 
   }
 
   
   public static IIcon[] icons = new IIcon[names.length];
 
   
   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int meta) {
     return icons[meta];
   }
 
   
   public int getMetadata(int meta) {
     return meta;
   }
 
   
   public String getUnlocalizedName(ItemStack is) {
     return super.getUnlocalizedName(is) + "." + names[is.getItemDamage()].replace('/', '.');
   }
 
   
   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister reg) {
     for (int i = 0; i < names.length; i++) {
       icons[i] = reg.registerIcon("thaumicbases:" + names[i]);
     }
   }
 
   
   @SideOnly(Side.CLIENT)
   public void getSubItems(Item itm, CreativeTabs tab, List lst) {
     for (int i = 0; i < names.length; i++) {
       lst.add(new ItemStack(itm, 1, i));
     }
   }
 }


