package tb.common.tile;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.oredict.OreDictionary;
import tb.common.entity.EntityAIAvoidCampfire;
import thaumcraft.common.config.ConfigBlocks;




public class TileCampfire extends TileEntity {
	
	public int burnTime;
	public int logTime;
	public int uptime;

	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.burnTime = tag.getInteger("burnTime");
		this.logTime = tag.getInteger("logTime");
	}


	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("burnTime", this.burnTime);
		tag.setInteger("logTime", this.logTime);
	}


	public boolean addLog(ItemStack log) {
		if (log == null) {
			return false;
		}
		if (this.logTime > 0) {
			return false;
		}
		if (log.getItem() == Item.getItemFromBlock(ConfigBlocks.blockMagicalLog)) {
			if (log.getItemDamage() % 3 == 0) {

				this.logTime = 6400;
				this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 1, 3);
				return true;
			} 
		}

		if (Block.getBlockFromItem(log.getItem()) instanceof net.minecraft.block.BlockLog) {

			this.logTime = 1600;
			this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 1, 3);
			return true;
		} 

		if ((OreDictionary.getOreIDs(log)).length > 0) {

			int[] ints = OreDictionary.getOreIDs(log);
			boolean isLog = false;
			for (int i = 0; i < ints.length; i++) {

				String str = OreDictionary.getOreName(ints[i]);
				if (str.equals("logWood")) {

					isLog = true;
					break;
				} 
			} 
			if (isLog) {

				this.logTime = 1600;
				this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 1, 3);
				return true;
			} 
		} 

		return false;
	}


	public boolean addFuel(ItemStack item) {
		if (item == null) {
			return false;
		}
		if (this.logTime <= 0) {
			return false;
		}
		if (this.burnTime > 0) {
			return false;
		}
		int burnTime = TileEntityFurnace.getItemBurnTime(item);

		if (burnTime <= 0) {
			return false;
		}
		this.burnTime = burnTime;
		this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 2, 3);

		return true;
	}



	public void updateEntity() {
		this.uptime++;

		if (this.burnTime > 0) {

			//this.burnTime--;
			//note time to go gahtei something something thx tmph

			if (this.logTime > 0) {
				this.logTime--;
			}
		} 
		if (!this.worldObj.isRemote) {

			if (this.burnTime <= 0) {

				int metadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
				if (metadata == 2) {
					this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 1, 3);
				}
				return;
			} 
			if (this.logTime <= 0) {

				int metadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
				if (metadata > 0) {
					this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 0, 3);
				}

				return;
			} 
		} 

		if (!this.worldObj.isRemote) {

			List<EntityCreature> creatures = this.worldObj.getEntitiesWithinAABB(EntityCreature.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, (this.xCoord + 1), (this.yCoord + 1), (this.zCoord + 1)).expand(12.0D, 6.0D, 12.0D));

			for (EntityCreature c : creatures) {

				if (!(c instanceof net.minecraft.entity.monster.IMob)) {
					continue;
				}
				ArrayList<EntityAITasks.EntityAITaskEntry> entries = (ArrayList)c.tasks.taskEntries;

				EntityAIAvoidCampfire campfireAviodable = null;

				boolean hasTask = false;
				for (int i = 0; i < entries.size(); i++) {

					EntityAITasks.EntityAITaskEntry task = entries.get(i);
					if (task.action instanceof EntityAIAvoidCampfire) {

						campfireAviodable = (EntityAIAvoidCampfire)task.action;
						hasTask = true;
						break;
					} 
				} 
				if (campfireAviodable == null) {
					campfireAviodable = new EntityAIAvoidCampfire(c);
				}
				campfireAviodable.isScared = true;
				campfireAviodable.scareTimer = 100;
				campfireAviodable.campfireX = this.xCoord;
				campfireAviodable.campfireY = this.yCoord;
				campfireAviodable.campfireZ = this.zCoord;

				if (!hasTask)
					c.tasks.addTask(1, (EntityAIBase)campfireAviodable); 
			} 
		} 
	}
}


