package tb.utils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import tb.utils.TBUtils;

public class CommandFindEmptySlot extends CommandBase {

	@Override
	public String getCommandName() {
		return "TBFindEmptySlot";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/TBFindEmptySlot";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		
		EntityPlayer player = (EntityPlayer) sender;
		
		TBUtils.sendChatToSender(sender, player.inventory.getFirstEmptyStack());
		
	}

}
