package tb.utils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import tb.handlers.DimesnionTickHandler;
import tb.utils.TBUtils;

public class CommandDimTime extends CommandBase {

	@Override
	public String getCommandName() {
		return "TBDimTime";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/TBDimTime [time]";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length == 1) {
			DimesnionTickHandler.setDimAge(Integer.valueOf(args[0]));
		}
		else if (args.length == 0) {
			TBUtils.sendChatToSender(sender, DimesnionTickHandler.getDimAge());
		}
	}

}
