package tb.utils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import tb.utils.TBUtils;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.blocks.BlockJarItem;
import thaumcraft.common.blocks.ItemJarFilled;
import thaumcraft.common.config.ConfigBlocks;

public class CommandFillJar extends CommandBase {
	
	private static String AspectArg;
	private static int AmountArg;
	private static Aspect asp;
	

	@Override
	public String getCommandName() {
		return "TBFillJar";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/TBFillJar <aspect> [amount]";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		
		if (args.length < 1 || args.length > 2) {
			TBUtils.sendChatToSender(sender, "Invalid number of args: " + getCommandUsage(sender));
		}
		
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if (!(player.getCurrentEquippedItem().getItem() instanceof BlockJarItem) && !(player.getCurrentEquippedItem().getItem() instanceof ItemJarFilled)) {
				TBUtils.sendChatToSender(sender, "You must be holding a jar");
				return;
			}
			
			else {
				if (args.length == 2) {
					//TBUtils.sendChatToSender(sender, "2 args");
					AspectArg = args[0];
					AmountArg = Integer.valueOf(args[1]);
					
				}
				
				if (args.length == 1) {
					//TBUtils.sendChatToSender(sender, "1 arg");
					AspectArg = args[0];
					AmountArg = 64;
					
				}
			}
			

			asp = Aspect.getAspect(AspectArg);
			TBUtils.sendChatToSender(sender, "Aspect: " + asp.getName());
			TBUtils.sendChatToSender(sender, "Amount: " + AmountArg);
			
			
			
			
			
			AspectList aList = new AspectList().add(asp, AmountArg);
			
			
			if (player.getCurrentEquippedItem().getItem() instanceof BlockJarItem) {
				
				
				
				
				
				
				ItemStack stack = new ItemStack(ConfigBlocks.blockJar);
				
				((ItemJarFilled) stack.getItem()).setAspects(stack, aList);
				
				//((ItemJarFilled) stack.getItem()).setAspects(stack, aList);
				
				
				
				if (player.getCurrentEquippedItem().stackSize == 1) {
					player.setCurrentItemOrArmor(0, stack);
				}
				
			}
			else if (player.getCurrentEquippedItem().getItem() instanceof ItemJarFilled) {
				
				
				//ItemJarFilled jarFilled = new ItemJarFilled();
				ItemStack stack = player.getCurrentEquippedItem();
				TBUtils.sendChatToSender(sender, player.getCurrentEquippedItem());
				((ItemJarFilled) stack.getItem()).setAspects(stack, aList);
				ItemStack realStack = stack;
				
				
				
				player.setCurrentItemOrArmor(0, realStack);
			}
			else {
				TBUtils.sendChatToSender(sender, "HOW?????????");
			}
			
		}
	}
}
