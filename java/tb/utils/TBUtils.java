 package tb.utils;
 
 import cpw.mods.fml.common.network.simpleimpl.IMessage;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.entity.player.EntityPlayerMP;
 import thaumcraft.api.aspects.Aspect;
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
 }


