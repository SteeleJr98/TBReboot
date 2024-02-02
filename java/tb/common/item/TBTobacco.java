 package tb.common.item;
 
 import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.Side;
 import cpw.mods.fml.relauncher.SideOnly;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.List;

import org.apache.logging.log4j.Level;

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
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import tb.api.ITobacco;
import tb.core.TBCore;
import tb.init.TBItems;
import tb.network.proxy.TBNetworkManager;
import tb.utils.TBConfig;
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
   
   public static final String[] names = new String[] { "tobacco_pile", "tobacco_eldritch", "tobacco_fighting", "tobacco_hunger", "tobacco_knowledge", "tobacco_mining", "tobacco_sanity", "tobacco_tainted", "tobacco_wispy", "tobacco_homeward"};
   private static final int maxAspect = TBConfig.wisdomMaxAspect;
 
 
 
 
 
 
   
   public void performTobaccoEffect(EntityPlayer smoker, int metadata, int isSilverwood) {
     ArrayList<Aspect> aspects;
     ArrayList<Integer> aspectNum;
     Collection<Aspect> pa;
     EntityWisp wisp;
     
     if (isSilverwood == 2) {
    	 smoker.addChatComponentMessage(new ChatComponentText("smoking is bad for you"));
    	 return;
     }
     
     switch (metadata) {
 
       
       case 0:
         if (isSilverwood == 1 && smoker.worldObj.rand.nextFloat() <= 0.3F) {
           TBUtils.addWarpToPlayer(smoker, -1, 0);
         }
         if (isSilverwood == 0 && smoker.worldObj.rand.nextFloat() <= 0.1F) {
           TBUtils.addWarpToPlayer(smoker, -1, 0);
         }
         break;
 
       
       case 1:
         if (!smoker.worldObj.isRemote) {
           
           smoker.addPotionEffect(new PotionEffect(Config.potionDeathGazeID, 2000, 0, true));
           if (isSilverwood == 0) {
             TBUtils.addWarpToPlayer(smoker, 3, 0);
           }
         } 
         break;
 
       
       case 2:
         if (!smoker.worldObj.isRemote) {
           
           smoker.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 8000, 1, true));
           if (isSilverwood == 1 && smoker.worldObj.rand.nextFloat() <= 0.1F) {
             smoker.addPotionEffect(new PotionEffect(Potion.resistance.id, 8000, 0, true));
           }
         } 
         break;
       
       case 3:
         if (!smoker.worldObj.isRemote) {
           
           smoker.getFoodStats().addStats(3, 3.0F);
           if (isSilverwood == 0 && smoker.worldObj.rand.nextFloat() <= 0.4F) {
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
           
           
           //Aspect[] validArray = (Aspect[]) validList.toArray();
           
           if (validList.size() > 0) {
	           for (int i = 0; i < (isSilverwood == 1 ? 20 : 10); i++) {
	        	 Aspect a = validList.get(smoker.worldObj.rand.nextInt(validList.size()));

	        	 
	             TBUtils.addAspectToKnowledgePool(smoker, a, (short)(isSilverwood == 1 ? 2 : 1));
	             if (a == Aspect.TAINT && isSilverwood == 0) {
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
           if (isSilverwood == 1 && smoker.worldObj.rand.nextFloat() <= 0.3F) {
             smoker.addPotionEffect(new PotionEffect(Potion.nightVision.id, 8000, 0, true));
           }
         } 
         break;
       
       case 6:
         if (!smoker.worldObj.isRemote) {
           
           if (smoker.worldObj.rand.nextFloat() <= (isSilverwood == 1 ? 0.6F : 0.4F))
           {
             TBUtils.addWarpToPlayer(smoker, -1, 0);
           }
           if (isSilverwood == 1 && smoker.worldObj.rand.nextFloat() <= 0.1F) {
             TBUtils.addWarpToPlayer(smoker, -1, 1);
           }
         } 
         break;
       
       case 7:
         if (!smoker.worldObj.isRemote) {
           
           if (isSilverwood == 0) {
             
             TBUtils.addWarpToPlayer(smoker, 1 + smoker.worldObj.rand.nextInt(3), 0);
             if (smoker.worldObj.rand.nextFloat() <= 0.4F)
               TBUtils.addWarpToPlayer(smoker, 1, 1); 
             break;
           } 
           ItemStack stk = smoker.getCurrentEquippedItem();
           if (stk != null) {
             
        	   TBNetworkManager.playSoundOnServer(smoker.worldObj, "ambient.weather.thunder", smoker.posX, smoker.posY + 2.0F, smoker.posZ, 1, 1);
        	   ItemStack pipeStack = new ItemStack(TBItems.corruptedPipe);
        	   smoker.inventory.setInventorySlotContents(smoker.inventory.currentItem, pipeStack);
        	   smoker.dropOneItem(true);
        	   
             //smoker.renderBrokenItemStack(stk);
             //smoker.inventory.setInventorySlotContents(smoker.inventory.currentItem, null);
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
         
         if (isSilverwood == 1) {
           aspects.remove(Aspect.TAINT);
         }
         wisp = new EntityWisp(smoker.worldObj);
         wisp.setPositionAndRotation(smoker.posX, smoker.posY, smoker.posZ, 0.0F, 0.0F);
         if (!smoker.worldObj.isRemote) {
           
           wisp.setType(((Aspect)aspects.get(smoker.worldObj.rand.nextInt(aspects.size()))).getTag());
           smoker.worldObj.spawnEntityInWorld((Entity)wisp);
         } 
         break;
         
       case 9: //homeward tobacco
    	   ChunkCoordinates spawnCoords = smoker.getBedLocation(smoker.dimension);
    	   ChunkCoordinates playerCoords = smoker.getPlayerCoordinates();
    	   ChunkCoordinates worldCoords = smoker.worldObj.getSpawnPoint();
    	   
    	   if (!smoker.worldObj.isRemote) {
    		   
    		   
    		   
    		   //System.out.println("X: " + spawnCoords.posX + " Y: " + spawnCoords.posY + " Z: " + spawnCoords.posZ);
    		   //TBCore.TBLogger.log(Level.INFO, "X: " + spawnCoords.posX + " Y: " + spawnCoords.posY + " Z: " + spawnCoords.posZ);
    		   if (spawnCoords != null) {
    			   if (spawnCoords != worldCoords) {
    				   //smoker.addChatMessage(new ChatComponentText("player spawn: " + spawnCoords.posX + " " + spawnCoords.posY + " " + spawnCoords.posZ));
    	    		   //smoker.addChatMessage(new ChatComponentText("world spawn: " + worldCoords.posX + " " + worldCoords.posY + " " + worldCoords.posZ));
	    			   //smoker.setPositionAndUpdate(spawnCoords.posX, spawnCoords.posY, spawnCoords.posZ);
	    			   double dist = TBUtils.SimpleDist(spawnCoords, playerCoords);
	    			   if (dist < (isSilverwood == 1 ? 6000 : 700) && dist > 5) {
	    				   TBNetworkManager.playSoundOnServer(smoker.worldObj, "mob.endermen.portal", smoker.posX, smoker.posY, smoker.posZ, 1.0F, 1.0F);
	    				   smoker.setPositionAndUpdate(spawnCoords.posX, spawnCoords.posY, spawnCoords.posZ);
	    				   //smoker.worldObj.playSoundEffect(smoker.serverPosX, smoker.serverPosY, smoker.serverPosZ, "mob.cat.meow1", 1.0F, 1.0F);
	    				   TBNetworkManager.playSoundOnServer(smoker.worldObj, "mob.endermen.portal", smoker.posX, smoker.posY, smoker.posZ, 1.0F, 1.0F);
	    			   } else {
	    				   if (dist > 5) {
	    					   smoker.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(isSilverwood == 1 ? "tb.txt.nosilvbedwarp" : "tb.txt.noregbedwarp")));
	    				   } else {
	    					   smoker.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("tb.txt.bedtooclose")));
						}
    			   	}
    			   }
    		   } else {
    			   smoker.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("tb.txt.nobedwarp")));
    		   }
    		   
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


