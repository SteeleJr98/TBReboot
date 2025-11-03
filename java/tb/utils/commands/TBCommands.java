package tb.utils.commands;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.command.ICommand;

public class TBCommands {
	
	public static void registerCommands(FMLServerStartingEvent event) {
		event.registerServerCommand((ICommand)new CommandFillJar());
		event.registerServerCommand((ICommand)new CommandFindEmptySlot());
		event.registerServerCommand((ICommand)new CommandBuildStructure());
		event.registerServerCommand((ICommand)new CommandGetWarp());
		event.registerServerCommand((ICommand)new CommandGetNote());
		event.registerServerCommand((ICommand)new CommandEnchant());
		event.registerServerCommand((ICommand)new CommandInfo());
		event.registerServerCommand((ICommand)new CommandDimTime());
		event.registerServerCommand((ICommand)new CommandLightning());
		event.registerServerCommand((ICommand)new CommandTBWeather());
		event.registerServerCommand((ICommand)new CommandForceResetDim());
		event.registerServerCommand((ICommand)new CommandScanAll());
		event.registerServerCommand((ICommand)new CommandCascadeChunk());
	}

}
