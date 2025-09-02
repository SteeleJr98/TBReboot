package tb.asm;

import java.util.ArrayList;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import tb.core.TBCore;



public class TBContainerCore extends DummyModContainer {

	public TBContainerCore() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = TBCore.modid + "Core";
		meta.name = TBCore.name + " Core";
		meta.version = TBCore.version;
		ArrayList<String> authors = new ArrayList<String>();
		authors.add("Modbder");
		authors.add("Steele");
		meta.authorList = authors;
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

	@Subscribe
	public void modConstruction(FMLConstructionEvent event) {}
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}
}
