package tb.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.steelehook.SteeleCore.Handlers.Logging;
import net.steelehook.SteeleCore.Handlers.MessageLogging;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.TileInfusionMatrix;

public class EyeInstable extends Item {
	
	public void Eyeinstable() {
	     setFull3D();
	     setMaxStackSize(1);
	   }
	
	@Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int faceQ, float hitXQ, float hityQ, float hitZQ)
    {
		if (world.isRemote) {
			MessageLogging.sendFromClient(player, world.getBlock(x, y, z).getLocalizedName());
		}
		Block block = world.getBlock(x, y, z);
		if (block.hasTileEntity(world.getBlockMetadata(x, y, z))) {
			if (world.getTileEntity(x, y, z) instanceof thaumcraft.common.tiles.TileInfusionMatrix) {
				if (world.isRemote) {
					MessageLogging.sendFromClient(player, "is matrix");
				}
				TileInfusionMatrix te = (TileInfusionMatrix) world.getTileEntity(x, y, z);
				String instabilityString = String.valueOf(te.instability);
				MessageLogging.sendFromClient(player, "Instability: " + instabilityString);
				
				
			}
			else {
				if (world.isRemote) {
					MessageLogging.sendFromClient(player, "not matrix");
				}
			}
		} else {
			if (world.isRemote) {
				MessageLogging.sendFromClient(player, "not tile");
			}
			
		}
		
        return false;
    }
	
	
	

}
