package tb.utils;

import java.io.File;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;
import tb.common.potion.PotionVoidCall;
import tb.core.TBCore;

public class TBConfig {
	static Configuration cfg;

	public static void load() {
		elderKnowledgeID = cfg.getInt("elderKnowledgeEnchantmentID", "Enchantments", 98, 0, Enchantment.enchantmentsList.length, "");
		eldritchBaneID = cfg.getInt("eldritchBaneEnchantmentID", "Enchantments", 99, 0, Enchantment.enchantmentsList.length, "");
		magicTouchID = cfg.getInt("magicTouchEnchantmentID", "Enchantments", 100, 0, Enchantment.enchantmentsList.length, "");
		taintedID = cfg.getInt("taintedEnchantmentID", "Enchantments", 101, 0, Enchantment.enchantmentsList.length, "");
		vaporisingID = cfg.getInt("vaporisingEnchantmentID", "Enchantments", 102, 0, Enchantment.enchantmentsList.length, "");

		int pTypeTotal = adjustPotionTypesIfNeeded();
		
		potionVoidCallID = cfg.getInt("potionVoidCallID", "General", 45, 0, 50, "");
		PotionVoidCall.instance = new PotionVoidCall(TBConfig.potionVoidCallID, true, 7889559);
		PotionVoidCall.init();

		aquaticFociUID = cfg.getInt("aquaticFociUID", "Foci", 10548, 0, 2147483647, "");
		nethericFociUID = cfg.getInt("nethericFociUID", "Foci", 10550, 0, 2147483647, "");
		decomposingFociUID = cfg.getInt("decomposingFociUID", "Foci", 10552, 0, 2147483647, "");
		vaporisingFociUID = cfg.getInt("vaporisingFociUID", "Foci", 10554, 0, 2147483647, "");
		calmingFociUID = cfg.getInt("calmingFociUID", "Foci", 10556, 0, 2147483647, "");
		crystalizationFociUID = cfg.getInt("crystalizationFociUID", "Foci", 10558, 0, 2147483647, "");
		warpingFociUID = cfg.getInt("warpingFociUID", "Foci", 10560, 0, 2147483647, "");
		spillageFociUID = cfg.getInt("spillageFociUID", "Foci", 10562, 0, 2147483647, "");

		cascadeDimID = cfg.getInt("cascadeDimID", "General", 41275, Integer.MIN_VALUE, Integer.MAX_VALUE, "Dimension ID");
		cascadeBiomeID = cfg.get("cascadeBiomeID", "General", 196, "Biome ID").getInt();
		cascadeDimMovementScale = cfg.getInt("cascadeDimMovementScale", "General", 5, 0, 100, "Integer percent scale of how quickly Dimension scales to overworld");
		cascadeDimReset = cfg.getInt("cascadeDimReset", "General", 1, 1, 200, "Number of hours Dimension exists before resetting scale");
		resetTries = cfg.getInt("resetTries", "General", 10, 1, 255, "Number of attemps to delete the Dimension on reset");
		cascadeDimSPM = cfg.getInt("cascadeDimSPM", "General", 1, 1, 60, "Lightning strikes per X minutes on average");

		allowTobacco = cfg.getBoolean("allowTobacco", "General", true, "If set to false the tobacco will be disabled - there will be no recipes/no entries in Thaumonomicon");
		minBlazePowderFromPyrofluid = cfg.getInt("minBlazePowderFromPyrofluid", "Pyrofluid", 5, 0, 2147483647, "");
		maxBlazePowderFromPyrofluid = cfg.getInt("maxBlazePowderFromPyrofluid", "Pyrofluid", 37, 0, 2147483647, "");

		decompGain = cfg.getInt("decompGain", "General", 80, 10, 80, "Percent of aspect amount transferd from Decomp. Focus");
		decompStackSize = cfg.getInt("decompStackSize", "General", 10, 5, 64, "Max size a stack can be for the Decomp. Focus");

		warnTimer = cfg.getInt("scytheWarnTimer", "Scythe", 15, 5, 3600, "Time in seconds the scythe will wait to warn player about mobs");
		warnRange = cfg.getInt("scytheWarnRange", "Scythe", 10, 2, 20, "Range in blocks the scythe will warn player about mobs");


		speedMultiplierForFurnace = cfg.getInt("speedMultiplierForFurnace", "Advanced Alchemy Furnace", 2, 0, 2147483647, "This is the speed of the Advanced Alchamical Furnace. TC's basic has 1.");
		makeRequireAlumentium = cfg.getBoolean("makeRequireAlumentium", "Advanced Alchemy Furnace", true, "Does the Advanced Alchemical Furnace requires Alumentium to work faster");

		shardsFromOre = cfg.getInt("shardsFromOre", "General", 8, 1, 64, "Amount of shards recieved from crucible ore processing");
		brightFociRequiresPrimordialPearl = cfg.getBoolean("brightFociRequiresPrimordialPearl", "General", true, "Does the Brightness Foci for the Node Manipulator requires a Primordial Pearl");

		wisdomMaxAspect = cfg.getInt("wisdomMaxAspects", "General", 250, 100, 500, "Max number of any aspect you can recieve from the wisdom tobacco");

		autoScannerSpeed = cfg.getFloat("autoScannerSpeed", "General", 5f, 0.5F, 60F, "Number, in seconds, it takes for the Auto Scanner to scan");

		overchanterBlacklist = cfg.get("General", "overchanterBlacklist", overchanterBlacklistDefault, "Blacklist enchantment IDs from being boosted in the Overchanter. IDs split with newlines, not commas").getIntList();
		overchantCap = cfg.getBoolean("overchanterCap", "General", false, "Stop the Overchanter from going past 1 level higher than any enchantment's max level");

		peacefulLeafSpawnChance = cfg.getInt("peacefulLeafSpawnChance", "General", 3, 0, 100, "Percent chance of Peaceful Leaves spawning mob per tick");
		netherLeafSpawnChance = cfg.getInt("netherLeafSpawnChance", "General", 5, 0, 100, "Percent chance of Nether Leaves spawning mob per tick");
		enderLeafSpawnChance = cfg.getInt("enderLeafSpawnChance", "General", 2, 0, 100, "Percent chance of Ender Leaves spawning mob per tick");

		enableTTCompathability = cfg.getBoolean("enableTTCompathability", "General", true, "Allow the mod to register it's enchantments in the Thaumic Tinkerer's enchanter? Set to false if Thaumic Tinkerer is crashing you.");

		pendantDimBlacklist = cfg.get("General", "attunedPendantDimBlacklist", pendantDimBlacklistDefault, "Blacklist dimension IDs from being attuned to the upgraded pendant. IDs split with newlines, not commas").getIntList();

		collectorAmount = cfg.getInt("collectorAmmount", "General", 4, 1, 8, "Amount Cascade Collector adds per strike");
		collectorDist = cfg.getInt("collectorDist", "General", 16, 2, 64, "How close, in blocks, a collector needs to be to activate");
		
		if (cfg.hasChanged()) {
			cfg.save();
		}
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
	public static int spillageFociUID;
	public static boolean allowTobacco;
	public static boolean enableTTCompathability;
	public static int minBlazePowderFromPyrofluid;
	public static int maxBlazePowderFromPyrofluid;
	public static int speedMultiplierForFurnace;
	public static boolean makeRequireAlumentium;
	public static int shardsFromOre;
	public static boolean brightFociRequiresPrimordialPearl;
	public static int wisdomMaxAspect;
	public static float autoScannerSpeed;
	public static int[] overchanterBlacklist;
	public static int[] overchanterBlacklistDefault = {};
	public static int peacefulLeafSpawnChance;
	public static int netherLeafSpawnChance;
	public static int enderLeafSpawnChance;
	public static int warnTimer;
	public static double warnRange;
	public static int decompGain;
	public static int decompStackSize;
	public static boolean overchantCap;
	public static int cascadeDimID;
	public static int cascadeBiomeID;
	public static int cascadeDimMovementScale;
	public static int cascadeDimReset;
	public static int resetTries;
	public static int cascadeDimSPM;
	public static int potionVoidCallID;
	public static int[] pendantDimBlacklist;
	public static int[] pendantDimBlacklistDefault = {1};
	public static int collectorAmount;
	public static int collectorDist;

	public static Configuration getConfiguration() {
		return cfg;
	}

	public static void init(String configDir) {

		if (cfg == null) {
			File path = new File(configDir + "/" + "ThaumicBases" + ".cfg");
			cfg = new Configuration(path);
			load();
		}
	}

	private static int adjustPotionTypesIfNeeded() {
		
		int typeLength = Potion.potionTypes.length;
		TBCore.TBLogger.info("Attempting some number magic (that I may or may not have totally stolen >:3c)");
		
		if (typeLength < 128) {
			Potion[] potionTypes = new Potion[128];
			System.arraycopy(Potion.potionTypes, 0, potionTypes, 0, typeLength);
			thaumcraft.common.lib.utils.Utils.setPrivateFinalValue(Potion.class, null, potionTypes, new String[] { "potionTypes", "field_76425_a", "a" });
		}
		
		return Potion.potionTypes.length;
	}
	
}


