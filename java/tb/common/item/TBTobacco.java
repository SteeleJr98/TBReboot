package tb.common.item;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import tb.api.ITobacco;
import tb.core.TBCore;
import tb.init.TBItems;
import tb.network.proxy.TBNetworkManager;
import tb.utils.TBConfig;
import tb.utils.TBUtils;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.EntityWisp;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;
import thaumcraft.common.lib.research.PlayerKnowledge;
import thaumcraft.common.lib.research.ResearchManager;

public class TBTobacco extends Item implements ITobacco {

	public TBTobacco() {
		setHasSubtypes(true);
		this.setMaxStackSize(64);
		//this.maxStackSize = getDamage(new ItemStack(this)) == 9 ? 1 : 2;
	}

	public static final String[] names = new String[] { "tobacco_pile", "tobacco_eldritch", "tobacco_fighting", "tobacco_hunger", "tobacco_knowledge", "tobacco_mining", "tobacco_sanity", "tobacco_tainted", "tobacco_wispy", "tobacco_homeward"};
	private static final int maxAspect = TBConfig.wisdomMaxAspect;


	//workaround for other mods using outdated interface
	public void performTobaccoEffect(EntityPlayer player, int meta, boolean bool) {
		performTobaccoEffect(player, meta, bool ? 1 : 0);
	}



	public void performTobaccoEffect(EntityPlayer smoker, int metadata, int isSilverwood) {
		ArrayList<Aspect> aspects;
		ArrayList<Integer> aspectNum;
		Collection<Aspect> pa;
		EntityWisp wisp;



		//     if (isSilverwood == 2) {
		//    	 smoker.addChatComponentMessage(new ChatComponentText("smoking is bad for you"));
		//    	 return;
		//     }

		switch (metadata) {


		case 0: //regular
			if (isSilverwood == 2 && smoker.worldObj.rand.nextFloat() <= 0.3F) {
				TBUtils.addWarpToPlayer(smoker, smoker.worldObj.rand.nextInt(20), 0);
			}
			if (isSilverwood == 1 && smoker.worldObj.rand.nextFloat() <= 0.3F) {
				TBUtils.addWarpToPlayer(smoker, -1, 0);
			}
			if (isSilverwood == 0 && smoker.worldObj.rand.nextFloat() <= 0.1F) {
				TBUtils.addWarpToPlayer(smoker, -1, 0);
			}
			break;


		case 1://eldritch
			if (!smoker.worldObj.isRemote) {

				smoker.addPotionEffect(new PotionEffect(Config.potionDeathGazeID, isSilverwood == 2 ? smoker.worldObj.rand.nextInt(1000) : 2000, isSilverwood == 2 ? 1 : 0, true));
				if (isSilverwood == 0) {
					TBUtils.addWarpToPlayer(smoker, 3, 0);
				}
				if (isSilverwood == 2) {
					TBUtils.addAspectToKnowledgePool(smoker, Aspect.ELDRITCH, (short) 5);
				}
			} 
			break;


		case 2://fighting
			if (!smoker.worldObj.isRemote) {

				smoker.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 8000, isSilverwood == 2 ? 2 : 1, true));
				if (isSilverwood == 1 && smoker.worldObj.rand.nextFloat() <= 0.1F) {
					smoker.addPotionEffect(new PotionEffect(Potion.resistance.id, 8000, 0, true));
				}
				if (isSilverwood == 2) {
					smoker.addPotionEffect(new PotionEffect(Potion.blindness.id, 2000, 0, false));
				}
			} 
			break;

		case 3://hunger
			if (!smoker.worldObj.isRemote) {

				smoker.getFoodStats().addStats(isSilverwood == 2 ? 5 : 3, 3.0F);
				if (isSilverwood == 0 && smoker.worldObj.rand.nextFloat() <= 0.4F) {
					smoker.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0, true));
				}
				if (isSilverwood == 2 && smoker.worldObj.rand.nextFloat() <= 0.1F) {
					smoker.addPotionEffect(new PotionEffect(Potion.hunger.id, 1000, 0, true));
				}
			} 
			break;

		case 4: //fix knowledge cap bug //knowledge
			if (!smoker.worldObj.isRemote) {

				//ArrayList<Aspect> arrayList = new ArrayList<Aspect>(Aspect.aspects.values());
				//Collection<Aspect> collection = Aspect.aspects.values();
				ArrayList<Aspect> validList = new ArrayList<Aspect>();

				AspectList pKnow = Thaumcraft.proxy.playerKnowledge.getAspectsDiscovered(smoker.getDisplayName());

				Aspect[] pList = pKnow.getAspects();
				for (Aspect a : pList) {
					if (Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(smoker.getDisplayName(), a) < maxAspect) {
						//smoker.addChatComponentMessage(new ChatComponentText(a.getName() + " : " + (Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(smoker.getDisplayName(), a))));
						validList.add(a);
					}
				}


				//Aspect[] validArray = (Aspect[]) validList.toArray();

				if (validList.size() > 0) {
					for (int i = 0; i < (isSilverwood == 2 ? 25 : (isSilverwood == 1 ? 20 : 10)); i++) {
						Aspect a = validList.get(smoker.worldObj.rand.nextInt(validList.size()));

						if (isSilverwood == 2 && smoker.worldObj.rand.nextFloat() <= 0.07F) {
							Aspect[] corruptAspects = {Aspect.DEATH, Aspect.DARKNESS, Aspect.ELDRITCH, Aspect.BEAST, Aspect.GREED, Aspect.HUNGER, Aspect.TAINT};
							a = corruptAspects[smoker.worldObj.rand.nextInt(corruptAspects.length)];
							TBUtils.addWarpToPlayer(smoker, 4, 0);
						}

						TBUtils.addAspectToKnowledgePool(smoker, a, (short) (isSilverwood + 1));
						if (a == Aspect.TAINT && isSilverwood == 0) {
							TBUtils.addWarpToPlayer(smoker, 1, 0);
						}

					}
				}
				else {
					smoker.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("tb.txt.fullSmoke1")));
					smoker.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("tb.txt.fullSmoke2"))); 
				}
			} 
			break;

		case 5://mining
			if (!smoker.worldObj.isRemote) {

				smoker.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 8000, isSilverwood == 2 ? 2 : 1, true));
				if (isSilverwood == 1 && smoker.worldObj.rand.nextFloat() <= 0.3F) {
					smoker.addPotionEffect(new PotionEffect(Potion.nightVision.id, 8000, 0, true));
				}
				if (isSilverwood == 2 && smoker.worldObj.rand.nextFloat() <= 0.2F) {
					smoker.addPotionEffect(new PotionEffect(Potion.blindness.id, 400, 0, false));
				}
			} 
			break;

		case 6://sanity
			if (!smoker.worldObj.isRemote) {

				if (smoker.worldObj.rand.nextFloat() <= (isSilverwood == 1 ? 0.6F : 0.4F)) {
					TBUtils.addWarpToPlayer(smoker, -1, 0);
				}
				if ((isSilverwood == 1 || isSilverwood == 2) && smoker.worldObj.rand.nextFloat() <= 0.1F) {
					TBUtils.addWarpToPlayer(smoker, isSilverwood == 2 ? -5 : -1, 1);
				}
				else {
					if (isSilverwood == 2) {
						TBUtils.addWarpToPlayer(smoker, 3, 1);
					}
				}
			} 
			break;

		case 7://tainted
			if (!smoker.worldObj.isRemote) {

				if (isSilverwood == 0) {

					TBUtils.addWarpToPlayer(smoker, 1 + smoker.worldObj.rand.nextInt(3), 0);
					if (smoker.worldObj.rand.nextFloat() <= 0.4F)
						TBUtils.addWarpToPlayer(smoker, 1, 1); 
					break;
				}
				ItemStack stk = smoker.getCurrentEquippedItem();
				if (stk != null) {

					TBNetworkManager.playSoundOnServer(smoker.worldObj, "ambient.weather.thunder", smoker.posX, smoker.posY + 2.0F, smoker.posZ, 1, 1);
					ItemStack pipeStack = new ItemStack(TBItems.corruptedPipe);
					smoker.inventory.setInventorySlotContents(smoker.inventory.currentItem, pipeStack);
					if (isSilverwood == 2) {
						smoker.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("tb.txt.burnpipe")));
						smoker.renderBrokenItemStack(stk);
						smoker.inventory.setInventorySlotContents(smoker.inventory.currentItem, null);
					}
					else {
						if (ThaumcraftApiHelper.isResearchComplete(smoker.getCommandSenderName(), "TB.SilverwoodPipe") && !ThaumcraftApiHelper.isResearchComplete(smoker.getCommandSenderName(), "TB.CorruptedPipe")) {
							PacketHandler.INSTANCE.sendTo((IMessage)new PacketResearchComplete("TB.CorruptedPipe"), (EntityPlayerMP)smoker);
							Thaumcraft.proxy.getResearchManager().completeResearch(smoker, "TB.CorruptedPipe");
						}
						smoker.dropOneItem(true);
					}

				} 
				return;
			} 
			break;



		case 8://wispy
			aspects = new ArrayList<Aspect>();
			pa = Aspect.aspects.values();
			for (Aspect aspect : pa) {
				aspects.add(aspect);
			}

			if (isSilverwood == 1) {
				aspects.remove(Aspect.TAINT);
			}

			if (!smoker.worldObj.isRemote) {
				for (int i = 0; i < (isSilverwood == 2 ? smoker.worldObj.rand.nextInt(6) + 1 : 1); i++) {
					wisp = new EntityWisp(smoker.worldObj);
					wisp.setPositionAndRotation(smoker.posX, smoker.posY, smoker.posZ, 0.0F, 0.0F);
					wisp.setType(((Aspect)aspects.get(smoker.worldObj.rand.nextInt(aspects.size()))).getTag());
					smoker.worldObj.spawnEntityInWorld((Entity)wisp);
				}
			} 
			break;

		case 9://homeward tobacco
			ChunkCoordinates spawnCoords = smoker.getBedLocation(smoker.dimension);
			ChunkCoordinates playerCoords = smoker.getPlayerCoordinates();
			ChunkCoordinates worldCoords = smoker.worldObj.getSpawnPoint();

			if (!smoker.worldObj.isRemote) {



				//System.out.println("X: " + spawnCoords.posX + " Y: " + spawnCoords.posY + " Z: " + spawnCoords.posZ);
				//TBCore.TBLogger.log(Level.INFO, "X: " + spawnCoords.posX + " Y: " + spawnCoords.posY + " Z: " + spawnCoords.posZ);
				if (spawnCoords != null) {
					if (spawnCoords != worldCoords) {
						//smoker.addChatMessage(new ChatComponentText("player spawn: " + spawnCoords.posX + " " + spawnCoords.posY + " " + spawnCoords.posZ));
						//smoker.addChatMessage(new ChatComponentText("world spawn: " + worldCoords.posX + " " + worldCoords.posY + " " + worldCoords.posZ));
						//smoker.setPositionAndUpdate(spawnCoords.posX, spawnCoords.posY, spawnCoords.posZ);
						double dist = TBUtils.SimpleDist(spawnCoords, playerCoords);
						if (dist < (isSilverwood == 2 ? 9000 : (isSilverwood == 1 ? 6000 : 700)) && dist > 5) {

							if (isSilverwood == 2 && smoker.worldObj.rand.nextFloat() <= 0.005) {
								int randX = smoker.worldObj.rand.nextInt(10000);
								int randZ = smoker.worldObj.rand.nextInt(10000);
								int randY = TBUtils.getTopBlock(smoker.worldObj, randX, randZ) + 2;
								spawnCoords = new ChunkCoordinates(randX, randY, randZ);
								//smoker.addChatMessage(new ChatComponentText("Tryint to warp to: " + randX + " " + randY + " " + randZ));
								for (int i = 0; i < smoker.inventory.getSizeInventory(); i++) {
									ItemStack tempStack = smoker.inventory.getStackInSlot(i);
									if (tempStack != null && tempStack.getItem() != null && tempStack.getItem() instanceof ITobacco && tempStack.getItem().getDamage(tempStack) == 9) {
										smoker.inventory.setInventorySlotContents(i, null);
									}
								}
								if (randY <= 1) {
									smoker.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("tb.txt.warprandlucky")));
									break;
								}
							}

							TBNetworkManager.playSoundOnServer(smoker.worldObj, "mob.endermen.portal", smoker.posX, smoker.posY + 0.5D, smoker.posZ, 1.0F, 1.0F);

							smoker.setPositionAndUpdate(spawnCoords.posX + 0.5D, spawnCoords.posY + 1.0D, spawnCoords.posZ + 0.5D);

							//smoker.worldObj.playSoundEffect(smoker.serverPosX, smoker.serverPosY, smoker.serverPosZ, "mob.cat.meow1", 1.0F, 1.0F);
							TBNetworkManager.playSoundOnServer(smoker.worldObj, "mob.endermen.portal", smoker.posX, smoker.posY, smoker.posZ, 1.0F, 1.0F);
						}
						else {
							if (dist > 5) {
								smoker.addChatMessage(new ChatComponentText(StatCollector.translateToLocal((isSilverwood == 1 || isSilverwood == 2) ? "tb.txt.nosilvbedwarp" : "tb.txt.noregbedwarp")));
							}
							else {
								smoker.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("tb.txt.bedtooclose")));
							}
						}
					}
				}
				else {
					smoker.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("tb.txt.nobedwarp")));
				}

			}

			break;



		} 
	}


	public static IIcon[] icons = new IIcon[names.length];


	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return icons[meta];
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}


	public String getUnlocalizedName(ItemStack is) {
		return super.getUnlocalizedName(is) + "." + names[is.getItemDamage()].replace('/', '.');
	}


	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		for (int i = 0; i < names.length; i++) {
			icons[i] = reg.registerIcon("thaumicbases:" + names[i]);
		}
	}


	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itm, CreativeTabs tab, List lst) {
		for (int i = 0; i < names.length; i++) {
			lst.add(new ItemStack(itm, 1, i));
		}
	}



}


