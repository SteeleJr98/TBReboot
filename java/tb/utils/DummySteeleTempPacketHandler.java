package tb.utils;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import tb.core.TBCore;

public class DummySteeleTempPacketHandler implements IMessageHandler<DummySteeleTempPacket, IMessage> {

	@Override
	public IMessage onMessage(DummySteeleTempPacket message, MessageContext ctx) 
	{
		Side s = ctx.side;
		int packetID = -10;
		if(message.dataTag.hasKey("packetID"))
		{
			packetID = message.dataTag.getInteger("packetID");
			message.dataTag.removeTag("packetID");
		}
		S35PacketUpdateTileEntity genPkt = new S35PacketUpdateTileEntity(message.dataTag.getInteger("x"),message.dataTag.getInteger("y"),message.dataTag.getInteger("z"),packetID,message.dataTag);
		if(s == Side.CLIENT)
		{
			ctx.getClientHandler().handleUpdateTileEntity(genPkt);
		}else
		{
			
		}
		return null;
	}
	
	public static void sendToAll(DummySteeleTempPacket message)
	{
		TBCore.network.sendToAll(message);
	}
	
	public static void sendToAllAround(DummySteeleTempPacket message, TargetPoint pnt)
	{
		TBCore.network.sendToAllAround(message, pnt);
	}
	
	public static void sendToAllAround(DummySteeleTempPacket message, int dim)
	{
		TBCore.network.sendToDimension(message, dim);
	}
	
	public static void sendToPlayer(DummySteeleTempPacket message, EntityPlayerMP player)
	{
		TBCore.network.sendTo(message, (EntityPlayerMP) player);
	}
	
	public static void sendToServer(DummySteeleTempPacket message)
	{
		TBCore.network.sendToServer(message);
	}

}


