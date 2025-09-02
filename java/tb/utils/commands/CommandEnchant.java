package tb.utils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import tb.utils.TBUtils;

public class CommandEnchant extends CommandBase {

	@Override
	public String getCommandName() {
		return "TBEnchant";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/TBEnchant <id> <level>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length != 2) {
			TBUtils.sendChatToSender(sender, "Invalid number of args: " + getCommandUsage(sender));
			return;
		}
		
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			player.getCurrentEquippedItem().addEnchantment(Enchantment.enchantmentsList[Integer.valueOf(args[0])], Integer.valueOf(args[1]));
		}
		
	}

}
