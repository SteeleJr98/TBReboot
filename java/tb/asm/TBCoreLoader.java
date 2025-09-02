package tb.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.Name(TBCoreLoader.NAME)

public class TBCoreLoader implements IFMLLoadingPlugin {

	public static final String NAME = "TB CORE";

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {TBCoreTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return TBContainerCore.class.getName();
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
