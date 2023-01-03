 package tb.core;
 
 import DummyCore.Core.Core;
 import DummyCore.Utils.IDummyConfig;
 import cpw.mods.fml.common.FMLCommonHandler;
 import cpw.mods.fml.common.Mod;
 import cpw.mods.fml.common.Mod.EventHandler;
 import cpw.mods.fml.common.ModMetadata;
 import cpw.mods.fml.common.SidedProxy;
 import cpw.mods.fml.common.event.FMLInitializationEvent;
 import cpw.mods.fml.common.event.FMLPostInitializationEvent;
 import cpw.mods.fml.common.event.FMLPreInitializationEvent;
 import cpw.mods.fml.common.network.IGuiHandler;
 import cpw.mods.fml.common.network.NetworkRegistry;
 import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
 import cpw.mods.fml.common.registry.EntityRegistry;
 import cpw.mods.fml.relauncher.Side;
 import java.io.IOException;
 import java.util.ArrayList;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.MinecraftForge;
import net.steelehook.SteeleCore.Handlers.Logging;
import tb.api.RevolverUpgrade;
 import tb.common.enchantment.EnchantmentHandler;
 import tb.common.entity.EntityRevolverBullet;
 import tb.common.event.TBEventHandler;
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
 import tb.utils.TBConfig;
 
 
 @Mod(modid = "thaumicbases", version = "1.3.1710.2", name = "Thaumic Bases Reboot", dependencies = "required-after:Thaumcraft@[4.2.3.5,);required-after:Baubles@[1.0.1.10,);required-after:DummyCore@[1.6,);")
 public class TBCore
 {
   public static final String modid = "thaumicbases";
   public static final String version = "1.3.1710.2";
   public static final String name = "Thaumic Bases Reboot";
   public static final String serverProxy = "tb.network.proxy.TBServer";
   public static final String clientProxy = "tb.network.proxy.TBClient";
   public static final String dependencies = "required-after:Thaumcraft@[4.2.3.5,);required-after:Baubles@[1.0.1.10,);required-after:DummyCore@[1.6,);";
   public static final TBConfig cfg = new TBConfig();
   
   public static final boolean isDev = System.getProperty("user.name").equals("Steele");
   
   
   
   @SidedProxy(serverSide = "tb.network.proxy.TBServer", clientSide = "tb.network.proxy.TBClient")
   public static TBServer proxy;
   
   public static SimpleNetworkWrapper network;
   
   public static TBCore instance;
   
   
   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
	   
	 Logging.writeToConsole(Level.DEBUG, "Dev environment detected, Loading dev tools...");
	 
	   
     instance = this;
       Core.registerModAbsolute(getClass(), "Thaumic Bases", event.getModConfigurationDirectory().getAbsolutePath(), (IDummyConfig)cfg);
 
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
     NetworkRegistry.INSTANCE.registerGuiHandler(instance, (IGuiHandler)proxy);
     
     EntityRegistry.registerModEntity(EntityRevolverBullet.class, "revolverBullet", 0, this, 32, 1, true);
     
     TBFociUpgrades.init();
     proxy.registerRenderInformation();
     RevolverUpgrade.initConflictingMappings();
     
     network = NetworkRegistry.INSTANCE.newSimpleChannel("thaumbases");
     network.registerMessage(TBNetworkManager.class, PacketTB.class, 0, Side.SERVER);
     network.registerMessage(TBNetworkManager.class, PacketTB.class, 0, Side.CLIENT);
   }
 
   
   @EventHandler
   public void postInit(FMLPostInitializationEvent event) {
     TBRecipes.setup();
     TBThaumonomicon.setup();
     
   }
 
   
   public void setupModInfo(ModMetadata meta) {
     meta.autogenerated = false;
     meta.modId = "thaumicbases";
     meta.name = "Thaumic Bases Reboot";
     meta.version = "1.3.1710.2";
     meta.description = "A Thaumcraft addon, that adds more earlygame and midgame content. Small tweaks by Steele";
//meta.description = "Only 33 errors to go!";
     ArrayList<String> authors = new ArrayList<String>();
     authors.add("Modbder");
	 authors.add("Steele");
     meta.authorList = authors;
   }
 }


