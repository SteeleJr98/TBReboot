 package tb.common.event;
 
 import cpw.mods.fml.common.eventhandler.SubscribeEvent;
 import net.minecraftforge.event.entity.player.PlayerEvent;
 
 
 
 public class TBEventHandler
 {
   @SubscribeEvent
   public void nameFormatEvent(PlayerEvent.NameFormat event) {
     if (event.entityPlayer != null && event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() instanceof tb.common.item.ItemHerobrinesScythe)
       event.displayname = "Herobrine"; 
   }
 }


