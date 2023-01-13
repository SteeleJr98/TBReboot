 package tb.utils;
 
 import DummyCore.Utils.IDummyConfig;
 import net.minecraft.enchantment.Enchantment;
 import net.minecraftforge.common.config.Configuration;
 
 public class TBConfig implements IDummyConfig {
   static Configuration cfg;
   
   public void load(Configuration config) {
     cfg = config;
     elderKnowledgeID = cfg.getInt("elderKnowledgeEnchantmentID", "Enchantments", 98, 0, Enchantment.enchantmentsList.length, "");
     eldritchBaneID = cfg.getInt("eldritchBaneEnchantmentID", "Enchantments", 99, 0, Enchantment.enchantmentsList.length, "");
     magicTouchID = cfg.getInt("magicTouchEnchantmentID", "Enchantments", 100, 0, Enchantment.enchantmentsList.length, "");
     taintedID = cfg.getInt("taintedEnchantmentID", "Enchantments", 101, 0, Enchantment.enchantmentsList.length, "");
     vaporisingID = cfg.getInt("vaporisingEnchantmentID", "Enchantments", 102, 0, Enchantment.enchantmentsList.length, "");
     
     aquaticFociUID = cfg.getInt("aquaticFociUID", "Foci", 42, 0, 2147483647, "");
     nethericFociUID = cfg.getInt("nethericFociUID", "Foci", 43, 0, 2147483647, "");
     decomposingFociUID = cfg.getInt("decomposingFociUID", "Foci", 44, 0, 2147483647, "");
     vaporisingFociUID = cfg.getInt("vaporisingFociUID", "Foci", 45, 0, 2147483647, "");
     calmingFociUID = cfg.getInt("calmingFociUID", "Foci", 46, 0, 2147483647, "");
     crystalizationFociUID = cfg.getInt("crystalizationFociUID", "Foci", 47, 0, 2147483647, "");
     warpingFociUID = cfg.getInt("warpingFociUID", "Foci", 48, 0, 2147483647, "");
     
     allowTobacco = cfg.getBoolean("allowTobacco", "General", true, "If set to falso the tobacco will be disabled - there will be no recipes/no entries in Thaumonomicon");
     minBlazePowderFromPyrofluid = cfg.getInt("minBlazePowderFromPyrofluid", "Pyrofluid", 5, 0, 2147483647, "");
     maxBlazePowderFromPyrofluid = cfg.getInt("maxBlazePowderFromPyrofluid", "Pyrofluid", 37, 0, 2147483647, "");
     
     speedMultiplierForFurnace = cfg.getInt("speedMultiplierForFurnace", "Advanced Alchemy Furnace", 2, 0, 2147483647, "This is the speed of the Advanced Alchamical Furnace. TC's basic has 1.");
     makeRequireAlumentium = cfg.getBoolean("makeRequireAlumentium", "Advanced Alchemy Furnace", true, "Does the Advanced Alchemical Furnace requires Alumentium to work faster");
     
     shardsFromOre = cfg.getInt("shardsFromOre", "General", 8, 1, 64, "Amount of shards recieved from crucible ore processing");
     brightFociRequiresPrimordialPearl = cfg.getBoolean("brightFociRequiresPrimordialPearl", "General", true, "Does the Brightness Foci for the Node Manipulator requires a Primordial Pearl");
     
     wisdomMaxAspect = cfg.getInt("wisdomMaxAspects", "General", 250, 100, 500, "Max number of any aspect you can recieve from the wisdom tobacco");
     
     enableTTCompathability = cfg.getBoolean("enableTTCompathability", "General", true, "Allow the mod to register it's enchantments in the Thaumic Tinkerer's enchanter? Set to false if Thaumic Tinkerer is crashing you.");
   }
   
   public static int elderKnowledgeID;
   public static int eldritchBaneID;
   public static int magicTouchID;
   public static int taintedID;
   public static int vaporisingID;
   public static int aquaticFociUID;
   public static int nethericFociUID;
   public static int decomposingFociUID;
   public static int vaporisingFociUID;
   public static int calmingFociUID;
   public static int crystalizationFociUID;
   public static int warpingFociUID;
   public static boolean allowTobacco;
   public static boolean enableTTCompathability;
   public static int minBlazePowderFromPyrofluid;
   public static int maxBlazePowderFromPyrofluid;
   public static int speedMultiplierForFurnace;
   public static boolean makeRequireAlumentium;
   public static int shardsFromOre;
   public static boolean brightFociRequiresPrimordialPearl;
   public static int wisdomMaxAspect;
 }


