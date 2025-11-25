package tb.utils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.Chunk;
import tb.utils.DummySteele;
import tb.utils.TBUtils;

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
		TBUtils.fillChunkNearPlayer(sender.getEntityWorld(), true, true, Blocks.stone);
//		EntityPlayer player = (EntityPlayer) sender;
//		int PX = (int) player.posX;
//		int PZ = (int) player.posZ;
//		int PCX = Math.floorDiv(PX, 16);
//		int PCZ = Math.floorDiv(PZ, 16);
//		Chunk tempChunk = player.worldObj.getChunkFromBlockCoords(PX, PZ);
//		int WCX = tempChunk.xPosition;
//		int WCZ = tempChunk.zPosition;
//		
//		DummySteele.sendMessageFromServer("PX:  " + PX);
//		DummySteele.sendMessageFromServer("PZ:  " + PZ);
//		DummySteele.sendMessageFromServer("PCX: " + PCX);
//		DummySteele.sendMessageFromServer("PCZ: " + PCZ);
//		DummySteele.sendMessageFromServer("WCX: " + WCX);
//		DummySteele.sendMessageFromServer("WCZ: " + WCZ);
		
		
	}

}
