package tb.utils.commands;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.command.ICommand;

public class TBCommands {
	
	public static void registerCommands(FMLServerStartingEvent event) {
		event.registerServerCommand((ICommand)new CommandFillJar());
		event.registerServerCommand((ICommand)new CommandFindEmptySlot());
	}

}
