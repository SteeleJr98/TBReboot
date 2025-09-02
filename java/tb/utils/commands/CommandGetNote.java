package tb.utils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import thaumcraft.common.lib.research.ResearchManager;

public class CommandGetNote extends CommandBase {

	@Override
	public String getCommandName() {
		return "TBNote";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/TBNote <research name>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		String noteName = "TB.AutoDeconst";
		if (args.length > 1) {
			noteName = args[0];
		}
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			ResearchManager.createResearchNoteForPlayer(player.worldObj, player, noteName);
		}
		else {
			ResearchManager.createResearchNoteForPlayer(sender.getEntityWorld(), (EntityPlayer) sender.getEntityWorld().playerEntities.get(0), noteName);
		}
	}

}
