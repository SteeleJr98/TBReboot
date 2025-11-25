package tb.utils.commands;

import org.apache.logging.log4j.Level;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import tb.core.TBCore;
import tb.dimension.WorldProviderCascade;
import tb.utils.DummySteele;
import tb.utils.TBUtils;

public class CommandInfo extends CommandBase {

	@Override
	public String getCommandName() {
		return "TBInfo";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/TBInfo";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			TBUtils.sendChatToSender(sender, player.getGameProfile().getId().toString());
		}
	}

}
