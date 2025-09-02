package tb.common.tile;

import java.util.ArrayList;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import tb.network.proxy.TBNetworkManager;
import tb.utils.TBConfig;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncScannedItems;
import thaumcraft.common.lib.research.ScanManager;

public class TileAutoDeconstructor extends TileEntity implements ISidedInventory {

	private static final int[] inputSlot = new int[] {0};
	private static final int[] outputSlot = new int[] {1};

	private ItemStack[] inventory = new ItemStack[2];
	private int timer = 0;
	private boolean setToMove = false;
	private boolean goodToCheckScan = true;
	private ScanResult currentScan;
	public final float loopTime = 20 * TBConfig.autoScannerSpeed;
	public ArrayList<String> playerNames = new ArrayList();

	@Override
	public void updateEntity() {
		if (worldObj.isRemote) {
			return;
		}
		if (this.inventory[0] != null && !this.setToMove) {

			if (this.goodToCheckScan) {
				ItemStack stack = this.inventory[0];
				this.currentScan = new ScanResult((byte)1, Item.getIdFromItem(stack.getItem()), stack.getItemDamage(), null, "");
				this.goodToCheckScan = false;
				if (worldObj.getPlayerEntityByName(this.playerNames.get(0)) == null) {
					this.setToMove = true;
					return;
				}
				if (ScanManager.hasBeenScanned(worldObj.getPlayerEntityByName(this.playerNames.get(0)), this.currentScan)) {
					this.setToMove = true;
					return;
				}
			}

			this.timer++;
			if (this.timer % 2 == 0) {
				TBNetworkManager.playSoundOnServer(worldObj, "thaumcraft:cameraticks", this.xCoord, this.yCoord, this.zCoord, 0.2F, 0.45F + worldObj.rand.nextFloat() * 0.1F);
			}
			if (this.timer > loopTime) {
				this.timer = 0;

				//ScanningManager.scanTheThing(worldObj.getPlayerEntityByName(playerNames.get(0)), inventory[0]);
				ItemStack stack = this.inventory[0];

				if (worldObj.getPlayerEntityByName(this.playerNames.get(0)) == null) {
					this.setToMove = true;
					TBNetworkManager.playSoundOnServer(worldObj, "random.fizz", this.xCoord, this.yCoord, this.zCoord, 0.2F, 1);
				}

				else if (ScanManager.isValidScanTarget(worldObj.getPlayerEntityByName(this.playerNames.get(0)), this.currentScan, "@")) {
					EntityPlayer thePlayer = worldObj.getPlayerEntityByName(this.playerNames.get(0));
					ScanManager.completeScan(thePlayer, this.currentScan, "@");
					PacketHandler.INSTANCE.sendTo((IMessage)new PacketSyncScannedItems(thePlayer), (EntityPlayerMP) thePlayer);
					this.setToMove = true;
				}
				tryMoveInv();

				markDirty();
			}

		}
		else {
			this.timer = 0;
			tryMoveInv();
		}



		super.updateEntity();
	}

	private void tryMoveInv() {
		if (this.inventory[1] == null && this.inventory[0] != null) {
			this.inventory[1] = this.inventory[0];
			this.inventory[0] = null;
			this.setToMove = false;
			this.goodToCheckScan = true;
		}
	}

	public int getTimer() {
		return this.timer;
	}

	@SideOnly(Side.CLIENT)
	public void setTimer(int time) {
		this.timer = time;
	}

	public ArrayList<String> getNames() {
		return this.playerNames;
	}


	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		// TODO Auto-generated method stub
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int num) {
		if (this.inventory[slot] != null) {



			if (this.inventory[slot].stackSize <= num) {

				ItemStack itemStack = this.inventory[slot];
				this.inventory[slot] = null;
				markDirty();
				return itemStack;
			} 


			ItemStack itemstack = this.inventory[slot].splitStack(num);

			if (this.inventory[slot].stackSize == 0) {
				this.inventory[slot] = null;
			}

			markDirty();
			return itemstack;
		} 



		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return this.inventory[slot];
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.inventory[slot] = stack;

	}

	@Override
	public String getInventoryName() {
		return "tb.auto_deconstructor";
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return (player.dimension == this.worldObj.provider.dimensionId && this.worldObj.blockExists(this.xCoord, this.yCoord, this.zCoord));
	}

	@Override
	public void openInventory() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot != 1;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);

		this.timer = tag.getInteger("time");

		if (tag.hasKey("itm")) {
			this.inventory[0] = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("itm"));
		}

		NBTTagList playerList = (NBTTagList)tag.getTag("playerList");

		for (int i = 0; i < playerList.tagCount(); i++) {
			this.playerNames.add(playerList.getStringTagAt(i));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);

		tag.setInteger("time", this.timer);

		if (this.inventory[0] != null) {

			NBTTagCompound t = new NBTTagCompound();
			this.inventory[0].writeToNBT(t);
			tag.setTag("itm", (NBTBase)t);
		}

		NBTTagList playerList = new NBTTagList();
		for (String nameString : playerNames) {
			playerList.appendTag(new NBTTagString(nameString));
		}
		//pName.setString("playerName", playerName);
		tag.setTag("playerList", playerList);

		markDirty();
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? outputSlot : inputSlot;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return this.isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		// TODO Auto-generated method stub
		return true;
	}

}
