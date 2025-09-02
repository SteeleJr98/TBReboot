package tb.utils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import tb.utils.TBConfig;
import tb.utils.TBUtils;

public class CommandLightning extends CommandBase {

	@Override
	public String getCommandName() {
		return "TBLightning";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/TBLightning";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if (player.dimension == TBConfig.cascadeDimID) {
				World world = player.worldObj;
				EntityLightningBolt lightning = new EntityLightningBolt(world, 10, TBUtils.getTopBlock(world, 10, 10), 10);
				world.spawnEntityInWorld(lightning);
				world.addWeatherEffect(lightning);
			}
		}
	}

}
