 package tb.common.event;
 
 import java.lang.reflect.Method;
import java.util.ArrayList;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import tb.asm.TBCoreTransformer;
import tb.core.TBCore;
import tb.utils.DummySteele;
import tb.utils.TBUtils;
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
	  else if(Block.getBlockFromItem(event.itemStack.getItem())!=null) {
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
	  if (TBCore.isDev) {
		  event.toolTip.add(event.itemStack.getItem().getClass().toString());
	  }
	}
  
  
  

  
  
  
 }


