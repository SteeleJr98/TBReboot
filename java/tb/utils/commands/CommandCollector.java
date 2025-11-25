package tb.utils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import tb.common.tile.TileCascadeCollector;
import tb.init.TBBlocks;

public class CommandCollector extends CommandBase {

	@Override
	public String getCommandName() {
		return "TBCollector";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/" + getCommandName();
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			int startX = (int) player.posX;
			int startY = (int) player.posY;
			int startZ = (int) player.posZ;
			
			for (int i = startX - 5; i <= startX + 5; i++) {
				for (int j = startY - 5; j <= startY + 5; j++) {
					for (int k = startZ - 5; k <= startZ + 5; k++) {
						if (player.worldObj.getBlock(i, j, k) == TBBlocks.cascadeCollector) {
							TileCascadeCollector tile = (TileCascadeCollector) player.worldObj.getTileEntity(i, j, k);
							tile.tryFillJar();
							break;
						}
					}
				}
			}
		}
	}

}
