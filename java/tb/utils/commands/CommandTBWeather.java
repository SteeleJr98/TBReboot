package tb.utils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import tb.utils.TBUtils;

public class CommandTBWeather extends CommandBase {

	@Override
	public String getCommandName() {
		return "TBWeather";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/TBWeather <clear/rain/thunder>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			World world = player.worldObj;
			WorldInfo info = world.getWorldInfo();
			
			if (args.length != 1) {
				TBUtils.sendChatToSender(sender, getCommandUsage(sender));
				return;
			}
			
			if (args[0] == "clear") {
				info.setRainTime(0);
				info.setRaining(false);
				info.setThunderTime(0);
				info.setThundering(false);
			}
			
			if (args[0] == "rain") {
				info.setRainTime(20000);
				info.setRaining(true);
				info.setThunderTime(0);
				info.setThundering(false);
			}
			
			if (args[0] == "thunder") {
				info.setRainTime(0);
				info.setRaining(false);
				info.setThunderTime(20000);
				info.setThundering(true);
			}
			
		}
	}

}
