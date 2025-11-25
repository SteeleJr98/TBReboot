package tb.common.event;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.logging.log4j.Level;

import com.google.common.eventbus.EventBus;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;
import tb.asm.TBCoreTransformer;
import tb.common.item.ItemAttunedCascadePendant;
import tb.common.item.ItemCascadeDispel;
import tb.common.item.ItemThauminiteArmor;
import tb.common.item.ItemVoidCompass;
import tb.common.tile.TileCascadeCollector;
import tb.core.TBCore;
import tb.handlers.DimesnionTickHandler;
import tb.handlers.KeyHandler;
import tb.init.TBBlocks;
import tb.init.TBItems;
import tb.utils.DummySteele;
import tb.utils.TBConfig;
import tb.utils.TBUtils;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.ItemJarFilled;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;



public class TBEventHandler {
	
	@SubscribeEvent
	public void nameFormatEvent(PlayerEvent.NameFormat event) {
		if (event.entityPlayer != null && event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() instanceof tb.common.item.ItemHerobrinesScythe)
			event.displayname = "Herobrine"; 
	}
	
	@SubscribeEvent
	public void lightningStrikeEvent(EntityJoinWorldEvent event) {
		if (!event.world.isRemote) {
			if (event.entity instanceof EntityLightningBolt && event.entity.dimension == TBConfig.cascadeDimID) {
				int r = TBConfig.collectorDist;
				ArrayList<ChunkCoordinates> collectors = new ArrayList<ChunkCoordinates>();
				collectors = DummySteele.findBlocks(event.world, (int) event.entity.posX, (int) event.entity.posY, (int) event.entity.posZ, r, TBBlocks.cascadeCollector);
				if (collectors.size() > 0) {
					ChunkCoordinates cc = collectors.get(event.world.rand.nextInt(collectors.size()));
					TileCascadeCollector tile = (TileCascadeCollector) event.world.getTileEntity(cc.posX, cc.posY, cc.posZ);
					tile.tryFillJar();
				}
			}
		}
	}

	@SubscribeEvent
	public void makeToolTip(ItemTooltipEvent event) {
		if(event.itemStack.getItem().equals(Items.skull))
			event.toolTip.add(StatCollector.translateToLocal("Can be used as an infusion stabilizer"));
		else if(Block.getBlockFromItem(event.itemStack.getItem())!=null) {
			for(Method m : Block.getBlockFromItem(event.itemStack.getItem()).getClass().getMethods())
				if(m.getName().endsWith("canStabaliseInfusion")) {

					//event.toolTip.add(StatCollector.translateToLocal("has method"));
					Block sBlock = Block.getBlockFromItem(event.itemStack.getItem());

					IInfusionStabiliser conv = (IInfusionStabiliser) sBlock;
					if (conv.canStabaliseInfusion(event.entity.worldObj, 0, 0, 0)) {
						event.toolTip.add(StatCollector.translateToLocal("Can be used as an infusion stabilizer"));
					}

					if (TBCore.isDev) {
						boolean isDeob = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
						event.toolTip.add(StatCollector.translateToLocal("Is deob: " + isDeob));
						event.toolTip.add(StatCollector.translateToLocal("Got into patching method: " + TBCoreTransformer.inPatchingMethod));
						event.toolTip.add(StatCollector.translateToLocal("Got into tooltip method: " + TBCoreTransformer.inTooltipMethod));
					}
				}
		}

		if (event.itemStack.getItem() instanceof ItemAttunedCascadePendant) {
			event.toolTip.add(StatCollector.translateToLocal("tb.txt.pendantTooltip") + " " + ((ItemAttunedCascadePendant) event.itemStack.getItem()).getDimension(event.itemStack));
		}

		if (TBCore.isDev && KeyHandler.isDebugActive) {
			ItemStack eventStack = event.itemStack;
			event.toolTip.add(eventStack.getItem().getClass().toString());
			event.toolTip.add("Stack Damage: " + eventStack.getItemDamageForDisplay());
			if (eventStack.hasTagCompound()) {
				Set<String> keySet = eventStack.getTagCompound().func_150296_c();
				Iterator<String> $s = keySet.iterator();
				while ($s.hasNext()) {
					String tempString = $s.next().toString();
					event.toolTip.add(tempString);
					if (tempString.equalsIgnoreCase("ench")) {
						LinkedHashMap<Integer, Integer> enchMap = (LinkedHashMap<Integer, Integer>) EnchantmentHelper.getEnchantments(eventStack);
						Set<Integer> enchSet = enchMap.keySet();
						Iterator<Integer> $i = enchSet.iterator();
						while ($i.hasNext()) {
							event.toolTip.add("    " + Enchantment.enchantmentsList[$i.next().intValue()].getName());
						}
					}
				}
			}
			//		  if (event.itemStack.getItem() != null && event.itemStack.getItem() instanceof ItemJarFilled) {
			//			  ItemJarFilled ijf = (ItemJarFilled) event.itemStack.getItem();
			//		  		event.toolTip.add(ijf.getAspects(event.itemStack).getAspects()[0].getName());
			//		  }
		}
	}
	
	@SubscribeEvent
	public void saveEvent(Save event) {
		if (!event.world.isRemote) {
			if (event.world.provider.dimensionId == TBConfig.cascadeDimID) {
				String timeString = String.valueOf(DimesnionTickHandler.getDimAge());
				TBCore.TBLogger.log(Level.INFO, "Saved cascade dim || dim time of: " + timeString);
				File saveFolder = event.world.getSaveHandler().getWorldDirectory();
				File timeSaveFile = new File(saveFolder, "dimInfo.txt");
				try {
					FileWriter tempWriter = new FileWriter(timeSaveFile);
					tempWriter.write(timeString);
					tempWriter.close();
				} catch (IOException e) {
					TBCore.TBLogger.log(Level.ERROR, "Cannot write world time file. Dimension time will not be consistent on next load");
					e.printStackTrace();
				}
				
			}
		}
	}
	
	
	
	@SubscribeEvent
	public void loadEvent(Load event) throws IOException {
		if (!event.world.isRemote) {
			if (event.world.provider.dimensionId == TBConfig.cascadeDimID) {
				TBCore.TBLogger.log(Level.INFO, "Loaded cascade dim");
				File saveFolder = event.world.getSaveHandler().getWorldDirectory();
				File timeSaveFile = new File(saveFolder, "dimInfo.txt");
				try {
					BufferedReader tempReader = new BufferedReader(new FileReader(timeSaveFile));
					String timeString = tempReader.readLine();
					tempReader.close();
					int tempInt = Integer.valueOf(timeString);
					DimesnionTickHandler.setDimAge(tempInt);
					TBCore.TBLogger.log(Level.INFO, "Loaded dimension with time value of: " + tempInt);
				} catch (FileNotFoundException e) {
					TBCore.TBLogger.log(Level.ERROR, "Cannot load world time file. Falling back to dimension time reset");
					DimesnionTickHandler.setDimAge(0);
					e.printStackTrace();
				}
			}
		}
	}

	@SubscribeEvent
	public void takeDamageEvent(LivingHurtEvent event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			//player.addChatComponentMessage(new ChatComponentText("Damage source: " + event.source.damageType));
			if ((event.source.getEntity() != player && event.source.damageType == "indirectMagic") || (event.source.getEntity() instanceof thaumcraft.common.entities.monster.EntityWisp)) {


				//player.addChatComponentMessage(new ChatComponentText("Damage source: " + event.source.damageType + " from source: " + event.source.getEntity()));
				int armourCount = 0;
				int i;
				for (i = 0; i < 4; i++) {
					if (player.inventory.armorInventory[i] != null) {
						if (player.inventory.armorInventory[i].getItem() instanceof ItemThauminiteArmor) {
							armourCount++;
						}
					}
				}
				if (armourCount > 0) {
					//player.addChatComponentMessage(new ChatComponentText(event.ammount + " -> " + (event.ammount * (1.0F - (0.05F * armourCount)))));
					event.ammount *= (1.0F - (0.035F * armourCount));
					//player.addChatComponentMessage(new ChatComponentText("Armour count: " + armourCount));
					for (i = 0; i < player.inventory.getSizeInventory(); i++) {
						ItemStack invStack = player.inventory.getStackInSlot(i);
						if (invStack != null && invStack.getItem() instanceof ItemWandCasting) {
							//player.addChatComponentMessage(new ChatComponentText("found wand in slot: " + i));
							ItemWandCasting wand = (ItemWandCasting) player.inventory.getStackInSlot(i).getItem();
							AspectList chargePotential = wand.getAllVis(invStack);
							int wandMax = wand.getMaxVis(invStack);
							//						  String s = "";
							//						  for (Aspect a : chargePotential.getAspects()) {
							//							  s += "Aspect: " + a.getName() + " Amount: " + chargePotential.getAmount(a) + "\n";
							//						  }
							//player.addChatComponentMessage(new ChatComponentText("aspect list: " + s + "\n\nwand max: " + wandMax));
							for (Aspect a : chargePotential.getAspects()) {
								int armourBonus = (int) (armourCount * 100 * player.worldObj.rand.nextFloat());
								//player.addChatComponentMessage(new ChatComponentText("Armour bonus: " + armourBonus));
								int storeAmount = chargePotential.getAmount(a) + armourBonus;
								if (storeAmount >= wandMax) {
									storeAmount = wandMax;
								}
								wand.storeVis(player.inventory.getStackInSlot(i), a, storeAmount);
								//player.addChatComponentMessage(new ChatComponentText("Stored " + storeAmount + " of " + a.getName()));
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void playerBreakGrassEvent(BlockEvent.HarvestDropsEvent event) {
		//DummySteele.sendMessageFromServer("break block event");
		if (event.harvester != null) {
			if (event.harvester.dimension == TBConfig.cascadeDimID) {
				if (event.block == Blocks.tallgrass || event.block == Blocks.double_plant) {

					if (event.harvester.worldObj.rand.nextInt(1000) == 0 || TBCore.isDev) {
						event.drops.add(new ItemStack(TBItems.resource, 1, 9));
					}
					
					//					if (event.drops.size() > 0) {
//						for (ItemStack s : event.drops) {
//							event.harvester.addChatComponentMessage(new ChatComponentText(s.getDisplayName()));
//						}
//					}
//					else {
//						event.harvester.addChatComponentMessage(new ChatComponentText("no drops"));
//					}
				}
			}
		}
	}
	
	//protected DataWatcher watcher;
	
	@SubscribeEvent
	public void playerTickEvent(LivingEvent.LivingUpdateEvent event) {
		if (!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPlayer && !Config.wuss && !event.entityLiving.isPotionActive(Config.potionWarpWardID)) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			PotionEffect effect = player.getActivePotionEffect(Potion.potionTypes[TBConfig.potionVoidCallID]);
			if (effect != null) {
				if (effect.getDuration() > 1) {
					IInventory baubles = BaublesApi.getBaubles(player);
					for (int i = 0; i < baubles.getSizeInventory(); i++) {
						if (baubles.getStackInSlot(i) != null && baubles.getStackInSlot(i).getItem() instanceof ItemCascadeDispel) {
							player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tb.txt.pendant.saveCall2")));
							player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tb.txt.pendant.saveCall3")));
							player.removePotionEffect(TBConfig.potionVoidCallID);
						}
					}
				}
				else {
					PotionEffect effects[] = {new PotionEffect(Potion.moveSlowdown.id, 45*20), new PotionEffect(Potion.blindness.id, 45*20), new PotionEffect(Potion.wither.id, 10*20, 2), new PotionEffect(Potion.weakness.id, 30*20)};
					player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tb.txt.failCall")));
					for (PotionEffect e : effects) {
						player.addPotionEffect(e);
					}
					
				}
				//TBCore.TBLogger.info("Player has " + effect.getEffectName() + " with " + effect.getDuration() + " remaining");
			}
			
			if (ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "TB.CascadeDispel")) {
				if (player.ticksExisted > 0 && player.ticksExisted % (20*60*30) == 0) {
					if (player.worldObj.rand.nextInt(100) == 0) {
						IInventory baubles = BaublesApi.getBaubles(player);
						for (int i = 0; i < baubles.getSizeInventory(); i++) {
							if (baubles.getStackInSlot(i) != null && baubles.getStackInSlot(i).getItem() instanceof ItemCascadeDispel) {
								player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "" + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tb.txt.pendant.saveCall")));
								return;
							}
						}
						PotionEffect potion = new PotionEffect(TBConfig.potionVoidCallID, 20*60*5, 0, false);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void itemPickupEvent(EntityItemPickupEvent event) {
		if (!event.entityPlayer.worldObj.isRemote) {
			ItemStack tempStack = event.item.getEntityItem();
			DummySteele.sendMessageFromServer(tempStack.getItem().getUnlocalizedName());
			DummySteele.sendMessageFromServer(tempStack.getItemDamage());
			if (tempStack.getItem() == TBItems.resource && tempStack.getItemDamage() == 9 && ThaumcraftApiHelper.isResearchComplete(event.entityPlayer.getCommandSenderName(), "TB.CascadeDim") && !ThaumcraftApiHelper.isResearchComplete(event.entityPlayer.getCommandSenderName(), "TB.CascadeDispel")) {
				DummySteele.sendMessageFromServer("beep");
				PacketHandler.INSTANCE.sendTo((IMessage)new PacketResearchComplete("TB.CascadeDispel"), (EntityPlayerMP)event.entityPlayer);
				Thaumcraft.proxy.getResearchManager().completeResearch(event.entityPlayer, "TB.CascadeDispel");
			}
		}
	}

	
//	@SubscribeEvent
//	public void lightningSpawnPortal(EntityJoinWorldEvent event) {
//		if (event.entity instanceof EntityLightningBolt) {
//			if (event.entity.dimension == TBConfig.cascadeDimID) {
//				if (!event.world.isRemote) {
//					event.world.setBlock((int) event.entity.posX, ((int) event.entity.posY) + 1, (int) event.entity.posZ, TBBlocks.cascadeBlock);
//				}
//			}
//		}
//	}
	
//	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
//	public void LiveDeath(LivingDeathEvent event) {
//		DummySteele.sendMessageFromServer("Dead Entity: " + event.entityLiving.getClass() + " | Damage Source: " + event.source.damageType);
//		if (event.source.damageType.contains("outOfWorld")) {
//			DummySteele.sendMessageFromServer("Attempting to cancel");
//			event.entityLiving.setDead();
//			event.setCanceled(true);
//			
//		}
//	}


	//  @SubscribeEvent
	//  public void funniChat(ClientChatReceivedEvent event) {
	//	  event.message = new ChatComponentText("pingas");
	//  }

	//  @SubscribeEvent
	//  public void freezeNewEnt(EntityJoinWorldEvent event) {
	//	  if (TBCore.isDev) {
	//		  if (event.entity instanceof EntityEnderPearl) {
	//			  EntityEnderPearl eventEntity = (EntityEnderPearl) event.entity;
	//			  //event.world.createExplosion(eventEntity, eventEntity.posX, eventEntity.posY, eventEntity.posZ, 0, false);
	//			  eventEntity.setVelocity(0, 0, 0);
	//			  eventEntity.setInWeb();
	//		  }
	//	  }
	//  }

	//  @SubscribeEvent
	//  public void playerInteract(EntityInteractEvent event) {
	//	  if (TBCore.isDev) {
	//		  if (event.target instanceof EntityPlayer) {
	//			  EntityPlayer player = event.entityPlayer;
	//			  if (!player.worldObj.isRemote) {
	//				  EntityPlayer eventPlayer = (EntityPlayer) event.target;
	//				  //eventZombie.setCustomNameTag("test");
	//				  //eventZombie.setAlwaysRenderNameTag(false);
	//				  //TBUtils.getTaggedEntityInArea(player, 10);
	//				  if (TBUtils.getTaggedEntityLookingAway(eventPlayer, 1, EntityPlayer.class) != null) {
	//					  player.openGui(TBCore.instance, 4331820, player.worldObj, player.serverPosX, player.serverPosY, player.serverPosZ);
	//				  }
	//				  
	//			  }
	//		  }
	//	  }
	//  }






}


