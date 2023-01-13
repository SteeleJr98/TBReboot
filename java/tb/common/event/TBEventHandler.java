 package tb.common.event;
 
 import java.lang.reflect.Method;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import tb.asm.TBCoreTransformer;
import tb.core.TBCore;
import thaumcraft.api.crafting.IInfusionStabiliser;
 
 
 
 public class TBEventHandler
 {
   @SubscribeEvent
   public void nameFormatEvent(PlayerEvent.NameFormat event) {
     if (event.entityPlayer != null && event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() instanceof tb.common.item.ItemHerobrinesScythe)
       event.displayname = "Herobrine"; 
   }
   
  @SubscribeEvent
  public void makeToolTip(ItemTooltipEvent event) {
	  if(event.itemStack.getItem().equals(Items.skull))
		  event.toolTip.add(StatCollector.translateToLocal("Can be used as an infusion stabilizer"));
	  else if(Block.getBlockFromItem(event.itemStack.getItem())!=null)
		  for(Method m : Block.getBlockFromItem(event.itemStack.getItem()).getClass().getMethods())
			  if(m.getName().endsWith("canStabaliseInfusion")) {

				  //event.toolTip.add(StatCollector.translateToLocal("has method"));
				  Block sBlock = Block.getBlockFromItem(event.itemStack.getItem());
					
				  IInfusionStabiliser conv = (IInfusionStabiliser) sBlock;
				  if (conv.canStabaliseInfusion(event.entity.worldObj, 0, 0, 0)) {
					  event.toolTip.add(StatCollector.translateToLocal("Can be used as an infusion stabilizer"));
				  }
				  
				  if (TBCore.isDev) {
					  boolean isDeob = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
					 event.toolTip.add(StatCollector.translateToLocal("Is deob: " + isDeob));
					 event.toolTip.add(StatCollector.translateToLocal("Got into patching method: " + TBCoreTransformer.inPatchingMethod));
					 event.toolTip.add(StatCollector.translateToLocal("Got into tooltip method: " + TBCoreTransformer.inTooltipMethod));
				  }
			  }			
	}
 }


