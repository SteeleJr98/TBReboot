package tb.api;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import java.util.ArrayList;
import java.util.Hashtable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.IExtendedEntityProperties;
import tb.utils.TBUtils;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.research.ScanManager;





public class RevolverUpgrade {
	public static RevolverUpgrade[] allUpgrades = new RevolverUpgrade[24];
	public static Hashtable<String, Integer> textToIntMapping = new Hashtable<String, Integer>();
	public static Hashtable<Integer, String> intToTextMapping = new Hashtable<Integer, String>();

	public int maxLevel;

	public final int id;
	public final String text_id;
	public int instability;
	public static final ResourceLocation eldritchTextures = new ResourceLocation("thaumicbases", "textures/items/revolver/revolverDarkMetalEldritch.png");
	public static final ResourceLocation primalTextures = new ResourceLocation("thaumicbases", "textures/items/revolver/revolverGunPrimalUV.png");
	public static final ResourceLocation taintedTextures = new ResourceLocation("thaumicbases", "textures/items/revolver/revolverGunTaintedUV.png");
	public static final ResourceLocation voidTextures = new ResourceLocation("thaumicbases", "textures/items/revolver/revolverHandleVoidUV.png");

	public final ArrayList<RevolverUpgrade> conflicts = new ArrayList<RevolverUpgrade>();


	public RevolverUpgrade(int id, String name) {
		this.text_id = name;
		this.id = id;

		if (id < allUpgrades.length && allUpgrades[id] != null) {

			FMLLog.warning("[TB]Attempting to register revolver upgrade " + name + "[" + id + "], but the ID is already occupied by " + (allUpgrades[id]).text_id + "[" + (allUpgrades[id]).id + "], ignoring.", new Object[0]);

			return;
		} 
		if (id >= allUpgrades.length) {

			RevolverUpgrade[] copied = new RevolverUpgrade[allUpgrades.length + 1];
			System.arraycopy(allUpgrades, 0, copied, 0, allUpgrades.length);
			allUpgrades = copied;
		} 

		allUpgrades[id] = this;
	}


	public String getName() {
		return StatCollector.translateToLocal("revolverUpgrade." + this.text_id + ".name");
	}


	public int getID() {
		return this.id;
	}


	public String getTextID() {
		return this.text_id;
	}


	public int getMaxLevel() {
		return this.maxLevel;
	}


	public RevolverUpgrade setMaxLevel(int i) {
		this.maxLevel = i;
		return this;
	}


	public boolean conflictsWith(RevolverUpgrade upgrade) {
		return this.conflicts.contains(upgrade);
	}


	public RevolverUpgrade addConflict(RevolverUpgrade... upgrade) {
		for (RevolverUpgrade rUpgr : upgrade) {

			if (!this.conflicts.contains(rUpgr)) {
				this.conflicts.add(rUpgr);
			}
			if (!rUpgr.conflicts.contains(this)) {
				rUpgr.conflicts.add(this);
			}
		} 
		return this;
	}


	public int getInstabilityForLevel(int level) {
		return this.instability * level;
	}


	public RevolverUpgrade setInstabilityPerLevel(int newValue) {
		this.instability = newValue;
		return this;
	}










	public float modifyDamage_start(EntityLivingBase base, ItemStack revolver, float currentModification, int modLevel) {
		if (base != null) {

			if (this == power) {
				return (float)(currentModification * Math.pow(1.100000023841858D, modLevel));
			}
			if (this == silver && (base.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD || isBMEntity(base) || (base instanceof EntityPlayer && isWerewolf(EntityPlayer.class.cast(base))))) {
				return (float)(currentModification * Math.pow(1.2000000476837158D, modLevel));
			}
			if (this == atropodsBane && base.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD) {
				return (float)(currentModification * Math.pow(1.2000000476837158D, modLevel));
			}
			if (this == eldritchBane && (base instanceof thaumcraft.api.entities.IEldritchMob || base instanceof net.minecraft.entity.monster.EntityEnderman)) {
				return (float)(currentModification * Math.pow(1.2000000476837158D, modLevel));
			}
			if (this == dueling && base instanceof EntityPlayer) {
				return (float)(currentModification * Math.pow(1.2000000476837158D, modLevel));
			}
			if (this == eldritch) {

				if (base instanceof net.minecraft.entity.boss.IBossDisplayData) {
					return (float)(currentModification * Math.pow(1.2999999523162842D, modLevel));
				}
				return currentModification / 1.2F;
			} 

			if (this == piercig) {
				return currentModification * 1.1F;
			}
			if (this == tainted) {

				if (base instanceof thaumcraft.api.entities.ITaintedMob) {
					return currentModification / 1.6F;
				}
				return (float)(currentModification * Math.pow(1.2000000476837158D, modLevel));
			} 
		} 
		return currentModification;
	}










	public float modifyDamage_end(EntityLivingBase base, ItemStack revolver, float currentModification, int modLevel) {
		if (this == primal) {
			return currentModification * 2.0F;
		}
		return currentModification;
	}











	public boolean afterhit(EntityLivingBase base, EntityPlayer user, ItemStack revolver, float damageDealt, int modLevel) {
		if (this == tainted && 
				base.worldObj.rand.nextDouble() <= 0.1D) {
			base.addPotionEffect(new PotionEffect(Config.potionTaintPoisonID, 200, 1, true));
		}
		if (this == poisoned) {
			base.addPotionEffect(new PotionEffect(Potion.poison.id, 200, 1, true));
		}
		if (this == knowledge && base.getHealth() <= 0.0F && user != null)
		{
			if (user.worldObj.rand.nextInt(Math.max(1, 7 - modLevel)) == 0) {

				AspectList aspectsCompound = ScanManager.generateEntityAspects((Entity)base);
				if (aspectsCompound != null && aspectsCompound.size() > 0) {

					Aspect[] al = aspectsCompound.getAspects();
					for (int i = 0; i < al.length; i++) {

						TBUtils.addAspectToKnowledgePool(user, al[i], (short)1);
						if (user.worldObj.rand.nextBoolean()) {
							break;
						}
					} 
				} 
			} 
		}

		if (this == piercig) {
			return false;
		}
		return true;
	}









	public boolean bulletNoclip(EntityPlayer user, ItemStack revolver, int modLevel) {
		if (this == primal) {
			return true;
		}
		return false;
	}











	public int modifyShots(EntityPlayer user, ItemStack revolver, int modLevel, int originalLevel, boolean hasPrimal) {
		if (this == primal) {
			return -1;
		}
		if (this == efficiency) {
			return hasPrimal ? ((modLevel == 5) ? 1 : -1) : (originalLevel + modLevel);
		}
		return originalLevel;
	}










	public double modifySpeed(EntityPlayer user, ItemStack revolver, float origSpeed, int modLevel) {
		if (this == speed)
			return origSpeed * Math.pow(1.090000033378601D, modLevel); 
		return origSpeed;
	}









	public ResourceLocation getOverridePartTexture(ItemStack revolver, int part, int level) {
		if (this == eldritch && part == 2) {
			return eldritchTextures;
		}
		if (this == primal && part == 3) {
			return primalTextures;
		}
		if (this == tainted && part == 3) {
			return taintedTextures;
		}
		if (this == uvoid && part == 0) {
			return voidTextures;
		}
		return null;
	}


	public boolean isBMEntity(EntityLivingBase base) {
		if (Loader.isModLoaded("AWWayofTime")) {

			try {

				Class<?> demonClass = Class.forName("WayofTime.alchemicalWizardry.common.IDemon");
				if (demonClass.isInstance(base)) {
					return true;
				}
			} catch (Exception e) {

				return false;
			} 
		}
		return false;
	}


	public boolean isWerewolf(EntityPlayer p) {
		if (Loader.isModLoaded("witchery"))
		{
			if (p.getExtendedProperties("WitcheryExtendedPlayer") != null) {

				IExtendedEntityProperties props = p.getExtendedProperties("WitcheryExtendedPlayer");
				NBTTagCompound tag = new NBTTagCompound();
				props.saveNBTData(tag);
				return (tag.getCompoundTag("WitcheryExtendedPlayer").getInteger("WerewolfLevel") > 0 || tag.getInteger("WerewolfLevel") > 0);
			} 
		}
		return false;
	}


	public static void initConflictingMappings() {
		power.addConflict(new RevolverUpgrade[] { silver, atropodsBane, eldritchBane, dueling, eldritch });
		silver.addConflict(new RevolverUpgrade[] { atropodsBane, eldritchBane, dueling, eldritch });
		atropodsBane.addConflict(new RevolverUpgrade[] { eldritchBane, dueling, eldritch });
		eldritchBane.addConflict(new RevolverUpgrade[] { dueling, eldritch });
		dueling.addConflict(new RevolverUpgrade[] { eldritch });

		accuracy.addConflict(new RevolverUpgrade[] { speed, efficiency });
		speed.addConflict(new RevolverUpgrade[] { efficiency });

		poisoned.addConflict(new RevolverUpgrade[] { primal, tainted, knowledge });
		primal.addConflict(new RevolverUpgrade[] { tainted, knowledge });
		tainted.addConflict(new RevolverUpgrade[] { knowledge });
	}

	public static final RevolverUpgrade power = (new RevolverUpgrade(0, "POWER")).setMaxLevel(5).setInstabilityPerLevel(2);
	public static final RevolverUpgrade accuracy = (new RevolverUpgrade(1, "ACCURACY")).setMaxLevel(3).setInstabilityPerLevel(1);
	public static final RevolverUpgrade silver = (new RevolverUpgrade(2, "SILVER")).setMaxLevel(5).setInstabilityPerLevel(3);
	public static final RevolverUpgrade poisoned = (new RevolverUpgrade(3, "POISONED")).setMaxLevel(3).setInstabilityPerLevel(1);
	public static final RevolverUpgrade atropodsBane = (new RevolverUpgrade(4, "BANE_OF_ATROPODS")).setMaxLevel(5).setInstabilityPerLevel(3);
	public static final RevolverUpgrade eldritchBane = (new RevolverUpgrade(5, "BANE_OF_ELDRITCH")).setMaxLevel(5).setInstabilityPerLevel(3);
	public static final RevolverUpgrade piercig = (new RevolverUpgrade(6, "PIERCING")).setMaxLevel(1).setInstabilityPerLevel(12);
	public static final RevolverUpgrade dueling = (new RevolverUpgrade(7, "DUELING")).setMaxLevel(5).setInstabilityPerLevel(3);
	public static final RevolverUpgrade tainted = (new RevolverUpgrade(8, "TAINTED")).setMaxLevel(3).setInstabilityPerLevel(5);
	public static final RevolverUpgrade primal = (new RevolverUpgrade(9, "PRIMAL")).setMaxLevel(1).setInstabilityPerLevel(12);
	public static final RevolverUpgrade speed = (new RevolverUpgrade(10, "SPEED")).setMaxLevel(5).setInstabilityPerLevel(2);
	public static final RevolverUpgrade efficiency = (new RevolverUpgrade(11, "EFFICIENCY")).setMaxLevel(5).setInstabilityPerLevel(4);
	public static final RevolverUpgrade eldritch = (new RevolverUpgrade(12, "ELDRITCH")).setMaxLevel(2).setInstabilityPerLevel(7);
	public static final RevolverUpgrade uvoid = (new RevolverUpgrade(13, "VOID")).setMaxLevel(3).setInstabilityPerLevel(4);
	public static final RevolverUpgrade knowledge = (new RevolverUpgrade(14, "KNOWLEDGE")).setMaxLevel(5).setInstabilityPerLevel(1);
	public static final RevolverUpgrade heavy = (new RevolverUpgrade(15, "HEAVY")).setMaxLevel(5).setInstabilityPerLevel(1);
}


