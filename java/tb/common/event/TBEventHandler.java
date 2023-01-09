 package tb.common.event;
 
 import java.lang.reflect.Method;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import thaumcraft.api.crafting.IInfusionStabiliser;
 
 
 
 public class TBEventHandler
 {
   @SubscribeEvent
   public void nameFormatEvent(PlayerEvent.NameFormat event) {
     if (event.entityPlayer != null && event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() instanceof tb.common.item.ItemHerobrinesScythe)
       event.displayname = "Herobrine"; 
   }
   
  @SubscribeEvent
  public static void makeToolTip(ItemTooltipEvent event) {
	  if(event.itemStack.getItem().equals(Items.skull))
		  event.toolTip.add(StatCollector.translateToLocal("hello from tb asm"));
	  else if(Block.getBlockFromItem(event.itemStack.getItem())!=null)
		  for(Method m : Block.getBlockFromItem(event.itemStack.getItem()).getClass().getMethods())
			  if(m.getName().endsWith("canStabaliseInfusion")) {

				  event.toolTip.add(StatCollector.translateToLocal("has method"));
				  Block sBlock = Block.getBlockFromItem(event.itemStack.getItem());
					
				  IInfusionStabiliser conv = (IInfusionStabiliser) sBlock;
					
					
				  event.toolTip.add(StatCollector.translateToLocal("Can stabalize: " + String.valueOf(conv.canStabaliseInfusion(event.entity.worldObj, 0, 0, 0))));
			  }			
	}
 }


