package tb.utils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import tb.utils.TBUtils;
import thaumcraft.common.Thaumcraft;

public class CommandGetWarp extends CommandBase {

	@Override
	public String getCommandName() {
		return "TBGetWarp";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/TBGetWarp";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		
		EntityPlayer player = (EntityPlayer) sender;
		
		int warpPerm = Thaumcraft.proxy.getPlayerKnowledge().getWarpPerm(player.getCommandSenderName());
		int warpTemp = Thaumcraft.proxy.getPlayerKnowledge().getWarpTemp(player.getCommandSenderName());
		int warpSticky = Thaumcraft.proxy.getPlayerKnowledge().getWarpSticky(player.getCommandSenderName());
		int warpTotal = Thaumcraft.proxy.getPlayerKnowledge().getWarpTotal(player.getCommandSenderName());
		
		TBUtils.sendChatToSender(sender, "Perm: " + warpPerm + "\nTemp: " + warpTemp + "\nSticky: " + warpSticky + "\nTotal: " + warpTotal);
		
	}

}
