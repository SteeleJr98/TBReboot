package tb.utils.commands;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryNamespaced;
import tb.utils.TBUtils;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncScannedItems;
import thaumcraft.common.lib.research.ScanManager;
import tb.core.TBCore;

public class CommandScanAll extends CommandBase {

	@Override
	public String getCommandName() {
		return "TBScanAll";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/TBScanAll";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		
		
		
		if ((!(sender instanceof EntityPlayer)) || sender.getEntityWorld().isRemote) {
			return;
		}
		
		Iterator iterator = Item.itemRegistry.iterator();

		
		
		EntityPlayer player = (EntityPlayer) sender;
		
        while (iterator.hasNext()) {
            Item item = (Item)iterator.next();

            if (item == null)
            {
                continue;
            }
            
            ItemStack stack = new ItemStack(item);
            
            if (item.getHasSubtypes() || TBCore.isDev) {
            	for (int i = 0; i <= 20; i++) {
            		ScanResult currentScan = new ScanResult((byte)1, Item.getIdFromItem(stack.getItem()), i, null, "");
                    ScanManager.completeScan(player, currentScan, "@");
            	}
            }
            
//            else {
//            	ScanResult currentScan = new ScanResult((byte)1, Item.getIdFromItem(stack.getItem()), stack.getItemDamage(), null, "");
//                ScanManager.completeScan(player, currentScan, "@");
//            }
            
            
        }
        
        PacketHandler.INSTANCE.sendTo((IMessage)new PacketSyncScannedItems(player), (EntityPlayerMP) player);
        
	}

}
