package tb.utils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandCascadeChunk extends CommandBase {

	@Override
	public String getCommandName() {
		return "TBChunk";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/" + getCommandName();
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		
	}

}
