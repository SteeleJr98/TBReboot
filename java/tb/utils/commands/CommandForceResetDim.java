package tb.utils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;
import tb.utils.TBConfig;
import tb.utils.TBUtils;

public class CommandForceResetDim extends CommandBase {

	@Override
	public String getCommandName() {
		return "TBResetDim";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/TBResetDim";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if (TBUtils.isTBDimLoaded()) {
				TBUtils.primeForReset(DimensionManager.getWorld(TBConfig.cascadeDimID));
				//TBUtils.TBDimensionReset();
			}
			else {
				TBUtils.sendChatToSender(sender, "TBDimension not loaded");
			}
		}
	}
	
	

}
