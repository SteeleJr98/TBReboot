 package tb.core;
 
 import cpw.mods.fml.common.FMLCommonHandler;
 import cpw.mods.fml.common.Mod;
 import cpw.mods.fml.common.Mod.EventHandler;
 import cpw.mods.fml.common.ModMetadata;
 import cpw.mods.fml.common.SidedProxy;
 import cpw.mods.fml.common.event.FMLInitializationEvent;
 import cpw.mods.fml.common.event.FMLPostInitializationEvent;
 import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.IGuiHandler;
 import cpw.mods.fml.common.network.NetworkRegistry;
 import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
 import cpw.mods.fml.common.registry.EntityRegistry;
 import cpw.mods.fml.relauncher.Side;

import java.io.File;
import java.io.IOException;
 import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.command.ICommand;
import net.minecraftforge.common.MinecraftForge;
import scala.reflect.internal.Trees.New;
import tb.api.RevolverUpgrade;
 import tb.common.enchantment.EnchantmentHandler;
 import tb.common.entity.EntityRevolverBullet;
 import tb.common.event.TBEventHandler;
import tb.common.item.ItemEyeInstable;
import tb.init.TBBlocks;
 import tb.init.TBEnchant;
 import tb.init.TBFociUpgrades;
 import tb.init.TBItems;
 import tb.init.TBRecipes;
 import tb.init.TBThaumonomicon;
 import tb.init.TBTiles;
 import tb.network.proxy.PacketTB;
 import tb.network.proxy.TBNetworkManager;
 import tb.network.proxy.TBServer;
import tb.utils.DummySteeleTempPacket;
import tb.utils.DummySteeleTempPacketHandler;
import tb.utils.TBConfig;
import tb.utils.commands.CommandFillJar;
import tb.utils.commands.TBCommands;
 
 
 @Mod(modid = TBCore.modid, version = TBCore.version, name = TBCore.name, dependencies = TBCore.dependencies)
 public class TBCore
 {
   public static final String modid = "thaumicbases";
   public static final String version = "2.1.3";
   public static final String name = "Thaumic Bases Reboot";
   public static final String serverProxy = "tb.network.proxy.TBServer";
   public static final String clientProxy = "tb.network.proxy.TBClient";
   public static final String dependencies = "required-after:Thaumcraft@[4.2.3.5,);required-after:Baubles@[1.0.1.10,);";
   public static final TBConfig cfg = new TBConfig();
   

   
   public static final boolean isDev = true;
   
   public static final Logger TBLogger = LogManager.getLogger("TBLogger");
   
   
   
   @SidedProxy(serverSide = "tb.network.proxy.TBServer", clientSide = "tb.network.proxy.TBClient")
   public static TBServer proxy;
   
   public static SimpleNetworkWrapper network;
   
   public static TBCore instance;
   
   
   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
	 
	 if (isDev) {
		 //Logging.writeToConsole(Level.INFO, "Dev environment detected, Loading dev tools...");
		 TBLogger.log(Level.INFO, "Dev environment detected, Loading dev tools...");
//		 for(int i = 0; i < 10; i++) {
//			 Logging.writeToConsole(Level.INFO, "");
//		 }
	 }
	 
	 
	 network = NetworkRegistry.INSTANCE.newSimpleChannel("thaumbases");
     network.registerMessage(TBNetworkManager.class, PacketTB.class, 0, Side.SERVER);
     network.registerMessage(TBNetworkManager.class, PacketTB.class, 0, Side.CLIENT);
//   network.registerMessage(DummyPacketHandler.class, DummyPacketIMSG.class, 0, Side.SERVER);
//	 network.registerMessage(DummyPacketHandler.class, DummyPacketIMSG.class, 0, Side.CLIENT);
	 network.registerMessage(DummySteeleTempPacketHandler.class, DummySteeleTempPacket.class, 1, Side.SERVER);
	 network.registerMessage(DummySteeleTempPacketHandler.class, DummySteeleTempPacket.class, 1, Side.CLIENT);
	 
	   
     instance = this;
       //Core.registerModAbsolute(getClass(), "Thaumic Bases", event.getModConfigurationDirectory().getAbsolutePath(), (IDummyConfig)cfg);
 
     String configDir = event.getModConfigurationDirectory().toString();
     TBConfig.init(configDir);
     
     setupModInfo(event.getModMetadata());
     TBBlocks.setup();
     TBItems.setup();
     TBEnchant.setup();
     TBTiles.setup();

   }
 
   
   @EventHandler
   public void init(FMLInitializationEvent event) {
     MinecraftForge.EVENT_BUS.register(new EnchantmentHandler());
     MinecraftForge.EVENT_BUS.register(new TBEventHandler());
     if (isDev) {
    	 MinecraftForge.EVENT_BUS.register(new ItemEyeInstable());
     }
     NetworkRegistry.INSTANCE.registerGuiHandler(instance, (IGuiHandler)proxy);
     
     EntityRegistry.registerModEntity(EntityRevolverBullet.class, "revolverBullet", 0, this, 32, 1, true);
     
     TBFociUpgrades.init();
     proxy.registerRenderInformation();
     RevolverUpgrade.initConflictingMappings();
     
     
   }
 
   
   @EventHandler
   public void postInit(FMLPostInitializationEvent event) {
     TBRecipes.setup();
     TBThaumonomicon.setup();
     
   }
   
   @EventHandler
   public void serverStarting(FMLServerStartingEvent event) {
	   TBCommands.registerCommands(event);
   }
 
   
   public void setupModInfo(ModMetadata meta) {
     meta.autogenerated = false;
     meta.modId = "thaumicbases";
     meta.name = "Thaumic Bases Reboot";
     meta.version = TBCore.version;
     meta.description = "A Thaumcraft addon, that adds more earlygame and midgame content. Small tweaks by Steele";
//meta.description = "Only 33 errors to go!";
     ArrayList<String> authors = new ArrayList<String>();
     authors.add("Modbder");
	 authors.add("Steele");
     meta.authorList = authors;
   }
 }


