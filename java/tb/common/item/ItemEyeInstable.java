package tb.common.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import tb.common.block.BlockCrystalBlock;
import tb.common.itemblock.ItemBlockCrystal;
import tb.init.TBBlocks;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.crafting.InfusionRunicAugmentRecipe;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.events.EventHandlerRunic;
import thaumcraft.common.tiles.TileInfusionMatrix;
import thaumcraft.common.tiles.TilePedestal;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.api.crafting.InfusionEnchantmentRecipe;
import thaumcraft.api.crafting.InfusionRecipe;

import tb.core.TBCore;

public class ItemEyeInstable extends Item {
	

	public void Eyeinstable() {
	     setFull3D();
	     setMaxStackSize(1);
	     
	   }
	
	
	
	@Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int faceQ, float hitXQ, float hityQ, float hitZQ)
    {
		
		Block block = world.getBlock(x, y, z);
		

		
		if (!world.isRemote && TBCore.isDev) {
			player.worldObj.playSoundEffect(player.serverPosX + 0.5D, player.serverPosY + 1.5D, player.serverPosZ + 0.5D, "random.chestopen", 1.0F, 1.0F);

			for(Class intf : block.getClass().getInterfaces()) {
				
			}
			
		}
		
		if (block.hasTileEntity(world.getBlockMetadata(x, y, z))) {
			if (world.getTileEntity(x, y, z) instanceof thaumcraft.common.tiles.TileInfusionMatrix) {
				if (!world.isRemote && TBCore.isDev) {
					
				}
				TileInfusionMatrix te = (TileInfusionMatrix) world.getTileEntity(x, y, z);
				String instabilityString = String.valueOf(te.instability);
				//MessageLogging.sendFromClient(player, "Instability: " + instabilityString);
				if (!world.isRemote) {
					getInstability(world, player, x, y, z);
				}
				
			}
			else {

			}
		} else {
			//Block testBlock  = Block.getBlockFromItem(Item.getItemFromBlock(TBBlocks.crystalBlock));
			

			
		}
		
        return false;
    }
	
	public int getInstability(World world, EntityPlayer player, int targetX, int targetY, int targetZ) {
		
		
		{
			ArrayList<ChunkCoordinates> stabilizers = new ArrayList();
			ArrayList<ChunkCoordinates> pedestals = new ArrayList();
			ArrayList<Object[]> warnings = new ArrayList();
			ArrayList<ItemStack> components = new ArrayList();

			for(int xx=-12; xx<=12; xx++)
				for(int zz=-12; zz<=12; zz++)
				{
					boolean skip = false;
					for(int yy=-10; yy<=5; yy++)
						if(xx!=0 || zz!=0)
						{
							int x = targetX+xx;
							int y = targetY+yy;
							int z = targetZ+zz;

							TileEntity te = world.getTileEntity(x, y, z);
							if(!skip && te!=null && te instanceof TilePedestal)
							{
								if(yy>=0)
									warnings.add(new Object[]{"pedestalHeight",x,y,z});
								else if(Math.abs(xx)>8 || Math.abs(zz)>8)
									warnings.add(new Object[]{"pedestalPos",x,y,z});
								else
								{
									pedestals.add(new ChunkCoordinates(x, y, z));
									skip = true;
								}
							}
							else
							{
								Block bi = world.getBlock(x, y, z);
								if(bi == Blocks.skull || (bi instanceof IInfusionStabiliser && ((IInfusionStabiliser)bi).canStabaliseInfusion(world, x, y, z)) )
									stabilizers.add(new ChunkCoordinates(x, y, z));
							}
						}
				}
			int symmetry = 0;
			for(ChunkCoordinates cc : pedestals)
			{
				boolean items = false;
				int dx = targetX - cc.posX;
				int dz = targetZ - cc.posZ;

				TileEntity te = world.getTileEntity(cc.posX, cc.posY, cc.posZ);
				if(te!=null && te instanceof TilePedestal)
				{
					symmetry += 2;
					if( ((TilePedestal)te).getStackInSlot(0) != null)
					{
						symmetry += 1;
						items = true;
						components.add(((TilePedestal)te).getStackInSlot(0));
					}
				}
				int xx = targetX + dx;
				int zz = targetZ + dz;
				te = world.getTileEntity(xx, cc.posY, zz);
				if(te!=null && te instanceof TilePedestal)
				{
					symmetry -= 2;
					if( ((TilePedestal)te).getStackInSlot(0)!=null)
					{
						if(items)
							symmetry -= 1;
						else
							warnings.add(new Object[]{"noPartnerItem",xx, cc.posY, zz});
					}
				}
				else
					warnings.add(new Object[]{"noPartnerPedestal",cc.posX, cc.posY, cc.posZ});
			}
			float sym = 0.0F;
			for (ChunkCoordinates cc : stabilizers)
			{
				int dx = targetX- cc.posX;
				int dz = targetZ- cc.posZ;
				Block bi = world.getBlock(cc.posX, cc.posY, cc.posZ);
				if(bi==Blocks.skull || (bi instanceof IInfusionStabiliser && ((IInfusionStabiliser)bi).canStabaliseInfusion(world, cc.posX, cc.posY, cc.posZ)) )
					sym += 0.1F;
				int xx = targetX+ dx;
				int zz = targetZ+ dz;
				bi = world.getBlock(xx, cc.posY, zz);
				if(bi==Blocks.skull || (bi instanceof IInfusionStabiliser && ((IInfusionStabiliser)bi).canStabaliseInfusion(world, xx,cc.posY,zz)) )
					sym -= 0.2F;
				else
					warnings.add(new Object[]{"noPartnerStabilizer",cc.posX, cc.posY, cc.posZ});
			}
			symmetry = ((int)(symmetry + sym));
			player.addChatMessage(new ChatComponentTranslation("wg.chat."+"infusionInfo.stabilityTotal",(symmetry*-1)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_PURPLE)) );

			ItemStack central = null;
			TileEntity te = world.getTileEntity(targetX,targetY-2,targetZ);
			if(te instanceof TilePedestal)
				if(((TilePedestal)te).getStackInSlot(0) != null)
					central = ((TilePedestal)te).getStackInSlot(0).copy();
			if(central!=null)
			{
				InfusionRecipe infRecipe = ThaumcraftCraftingManager.findMatchingInfusionRecipe(components, central, player);
				InfusionEnchantmentRecipe enchRecipe = ThaumcraftCraftingManager.findMatchingInfusionEnchantmentRecipe(components, central, player);
				if(enchRecipe!=null)
				{
					player.addChatMessage(new ChatComponentTranslation("wg.chat."+"infusionInfo.instability",enchRecipe.calcInstability(central)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_PURPLE)) );
					float essmod = 1+enchRecipe.getEssentiaMod(central);
					if(essmod>1)
					{
						String plaintext = "";
						Iterator<Entry<Aspect, Integer>> it = enchRecipe.aspects.aspects.entrySet().iterator();
						while(it.hasNext())
						{
							Entry<Aspect, Integer> e = it.next();
							plaintext += (int)(e.getValue()*essmod)+ " " +e.getKey().getName() + (it.hasNext()?", ":"");
						}
						player.addChatMessage(new ChatComponentTranslation("wg.chat."+"infusionInfo.essentiaMod",essmod).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)) );
						player.addChatMessage(new ChatComponentText(plaintext).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)) );
					}
				}
				if(infRecipe!=null)
				{
					player.addChatMessage(new ChatComponentTranslation("wg.chat."+"infusionInfo.instability",infRecipe.getInstability(central)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_PURPLE)) );
					if(infRecipe instanceof InfusionRunicAugmentRecipe)
					{
						int vis = (int)(32.0D * Math.pow(2.0D, EventHandlerRunic.getFinalCharge(central)));
						String plaintext = "";
						if (vis > 0)
							plaintext += vis+" "+Aspect.ENERGY.getName()+ ", " +(vis/2)+" " +Aspect.MAGIC.getName()+ ", " + (vis/2)+" "+Aspect.ARMOR.getName();
						player.addChatMessage(new ChatComponentTranslation("wg.chat."+"infusionInfo.essentiaRunicMod",EventHandlerRunic.getFinalCharge(central)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)) );
						player.addChatMessage(new ChatComponentText(plaintext).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)) );
					}
				}
			}

			for(Object[] warning : warnings)
			{
				String w = "wg.chat."+"infusionWarning."+warning[0];
				player.addChatMessage(new ChatComponentTranslation(w,warning[1],warning[2],warning[3]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_GRAY)) );
			}
			
			
			
			
			
			return 0;
		}
	}


//	@SideOnly(Side.CLIENT)
//	@SubscribeEvent
//	public void makeToolTip(ItemTooltipEvent event) {
//		if(event.itemStack.getItem().equals(Items.skull))
//			event.toolTip.add(StatCollector.translateToLocal("can be stabiliser"));
//		else if(Block.getBlockFromItem(event.itemStack.getItem())!=null)
//			for(Class intf : Block.getBlockFromItem(event.itemStack.getItem()).getClass().getInterfaces())
//				if(intf.getCanonicalName().endsWith("IInfusionStabiliser"))
//					event.toolTip.add(StatCollector.translateToLocal("can be stabiliser"));
//	}

		

	
	
	

}
