package tb.init;

import net.minecraftforge.common.DimensionManager;
import tb.dimension.WorldProviderCascade;
import tb.utils.TBConfig;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

public class TBDimensionRegistry {
	
	public static void setup() {
		DimensionManager.registerProviderType(TBConfig.cascadeDimID, WorldProviderCascade.class, false);
		DimensionManager.registerDimension(TBConfig.cascadeDimID, TBConfig.cascadeDimID);
		ThaumcraftWorldGenerator.addDimBlacklist(TBConfig.cascadeDimID, 0);
	}

}
