package tb.common.tile;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.wands.IWandable;

public class TileRelocator extends TileEntity implements IWandable {

	public boolean firstTick = true;
	public int checkCount = 0;

	public int power;


	public void updateEntity() {
		ForgeDirection orientation = ForgeDirection.values()[this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) % 6];
		ForgeDirection opposite = orientation.getOpposite();

		if (this.firstTick || this.checkCount % 100 == 0) {

			this.power = 0;
			this.firstTick = false;
			int cont = 0;
			while (this.worldObj.getBlock(this.xCoord + opposite.offsetX * cont, this.yCoord + opposite.offsetY * cont, this.zCoord + opposite.offsetZ * cont) instanceof tb.common.block.BlockRelocator) {
				cont++;
			}

			int cnt = 1;

			while (cnt <= cont * 10 && (this.worldObj.isAirBlock(this.xCoord + orientation.offsetX * cnt, this.yCoord + orientation.offsetY * cnt, this.zCoord + orientation.offsetZ * cnt) || !this.worldObj.getBlock(this.xCoord + orientation.offsetX * cnt, this.yCoord + orientation.offsetY * cnt, this.zCoord + orientation.offsetZ * cnt).isNormalCube((IBlockAccess)this.worldObj, this.xCoord + orientation.offsetX * cnt, this.yCoord + orientation.offsetY * cnt, this.zCoord + orientation.offsetZ * cnt))) {


				this.power++;
				cnt++;
			} 
		} 



		if (!isBlockPowered()) {
			if (!this.worldObj.getBlock(this.xCoord + orientation.offsetX, this.yCoord + orientation.offsetY, this.zCoord + orientation.offsetZ).isSideSolid((IBlockAccess)this.worldObj, this.xCoord, this.yCoord, this.zCoord, opposite)) {

				int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) % 6;
				boolean attract = (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) > 5);
				AxisAlignedBB aabb = null;
				if (meta == 0)
					aabb = AxisAlignedBB.getBoundingBox(this.xCoord, (this.yCoord - this.power), this.zCoord, (this.xCoord + 1), (this.yCoord + 1), (this.zCoord + 1)); 
				if (meta == 1)
					aabb = AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, (this.xCoord + 1), (this.yCoord + 1 + this.power), (this.zCoord + 1)); 
				if (meta == 2)
					aabb = AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, (this.zCoord - this.power), (this.xCoord + 1), (this.yCoord + 1), (this.zCoord + 1)); 
				if (meta == 3)
					aabb = AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, (this.xCoord + 1), (this.yCoord + 1), (this.zCoord + 1 + this.power)); 
				if (meta == 4)
					aabb = AxisAlignedBB.getBoundingBox((this.xCoord - this.power), this.yCoord, this.zCoord, (this.xCoord + 1), (this.yCoord + 1), (this.zCoord + 1)); 
				if (meta == 5) {
					aabb = AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, (this.xCoord + 1 + this.power), (this.yCoord + 1), (this.zCoord + 1));
				}
				List<Entity> affected = this.worldObj.getEntitiesWithinAABB(Entity.class, aabb);

				for (Entity base : affected) {

					boolean invert = attract ? (!base.isSneaking()) : base.isSneaking();
					if (invert) {

						if (meta == 0) {
							base.motionY += 0.1D;
						}
						if (meta == 1) {

							base.motionY -= 0.1D;
							base.fallDistance = 0.0F;
						} 
						if (meta == 2) {
							base.motionZ += 0.1D;
						}
						if (meta == 3) {
							base.motionZ -= 0.1D;
						}
						if (meta == 4) {
							base.motionX += 0.1D;
						}
						if (meta == 5) {
							base.motionX -= 0.1D;
						}
						continue;
					} 
					if (meta == 0) {

						base.motionY -= 0.1D;
						base.fallDistance = 0.0F;
					} 
					if (meta == 1) {
						base.motionY += 0.1D;
					}
					if (meta == 2) {
						base.motionZ -= 0.1D;
					}
					if (meta == 3) {
						base.motionZ += 0.1D;
					}
					if (meta == 4) {
						base.motionX -= 0.1D;
					}
					if (meta == 5) {
						base.motionX += 0.1D;
					}
				} 
			} 
		}


		this.checkCount++;
	}



	public int onWandRightClick(World paramWorld, ItemStack paramItemStack, EntityPlayer paramEntityPlayer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
		paramEntityPlayer.swingItem();
		paramWorld.playSound(this.xCoord, this.yCoord, this.zCoord, "thaumcraft:cameraticks", 1.0F, 1.0F, false);
		if (!paramEntityPlayer.isSneaking()) {

			int meta = (paramWorld.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) > 5) ? 6 : 0;
			paramWorld.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, meta + paramInt4, 3);
		}
		else {

			int meta = (paramWorld.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) > 5) ? 6 : 0;
			paramWorld.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, meta + ForgeDirection.OPPOSITES[paramInt4], 3);
		} 
		return 0;
	}



	public ItemStack onWandRightClick(World paramWorld, ItemStack paramItemStack, EntityPlayer paramEntityPlayer) {
		return paramItemStack;
	}




	public void onUsingWandTick(ItemStack paramItemStack, EntityPlayer paramEntityPlayer, int paramInt) {}




	public void onWandStoppedUsing(ItemStack paramItemStack, World paramWorld, EntityPlayer paramEntityPlayer, int paramInt) {}




	public boolean isBlockPowered() {
		return (this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) || this.worldObj.getBlockPowerInput(this.xCoord, this.yCoord, this.zCoord) > 0);
	}
}


