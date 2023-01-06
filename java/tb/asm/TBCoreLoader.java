package tb.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.Name(TBCoreLoader.NAME)

public class TBCoreLoader implements IFMLLoadingPlugin {
	
	public static final String NAME = "TB CORE";

	@Override
	public String[] getASMTransformerClass() {
		// TODO Auto-generated method stub
		return new String[] {TBCoreTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		// TODO Auto-generated method stub
		//return TBContainerCore.class.getName();
		return "tb.asm.TBContainerCore";
	}

	@Override
	public String getSetupClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAccessTransformerClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
