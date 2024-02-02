package tb.utils;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import net.minecraft.nbt.NBTTagCompound;

public class DummySteeleTempPacket implements IMessage {
	
	public NBTTagCompound dataTag;
	
	public DummySteeleTempPacket() {

	}
	
	public DummySteeleTempPacket(NBTTagCompound data, int id)
	{
		dataTag = data;
		dataTag.setInteger("packetID", id);
	}
	
	public DummySteeleTempPacket(NBTTagCompound data)
	{
		dataTag = data;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dataTag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, dataTag);
		
	}


}
