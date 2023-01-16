 package tb.utils;
 
 import java.lang.reflect.Method;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.common.Thaumcraft;
 import thaumcraft.common.lib.network.PacketHandler;
 import thaumcraft.common.lib.network.playerdata.PacketAspectPool;
 import thaumcraft.common.lib.research.ResearchManager;
 
 public class TBUtils
 {
   public static void addAspectToKnowledgePool(EntityPlayer addedTo, Aspect added, short amount) {
     Thaumcraft.proxy.playerKnowledge.addAspectPool(addedTo.getCommandSenderName(), added, amount);
     ResearchManager.scheduleSave(addedTo);
     if (addedTo instanceof EntityPlayerMP) {
       PacketHandler.INSTANCE.sendTo((IMessage)new PacketAspectPool(added.getTag(), Short.valueOf(amount), Short.valueOf(Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(addedTo.getCommandSenderName(), added))), (EntityPlayerMP)addedTo);
     }
   }
 
 
 
   
   public static void addWarpToPlayer(EntityPlayer addTo, int amount, int type) {
     switch (type) {
 
       
       case 2:
         Thaumcraft.addWarpToPlayer(addTo, amount, false);
         return;
 
       
       case 1:
         Thaumcraft.addStickyWarpToPlayer(addTo, amount);
         return;
 
       
       case 0:
         Thaumcraft.addWarpToPlayer(addTo, amount, true);
         return;
     } 
 
     
     Thaumcraft.addWarpToPlayer(addTo, amount, false);
   }
   
   public static double SimpleDist(ChunkCoordinates c1, ChunkCoordinates c2) {
	   int deltaX = c1.posX - c2.posX;
	   int deltaY = c1.posY - c2.posY;
	   int deltaZ = c1.posZ - c2.posZ;
	   return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
   }
}


