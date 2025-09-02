package tb.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tb.common.tile.TileEntityDeconstructor;
import thaumcraft.common.lib.research.ResearchManager;


public class BlockEntityDeconstructor extends BlockContainer {
	
	public BlockEntityDeconstructor() {
		super(Material.rock);
		float f = 0.1F;
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
	}


	public TileEntity createNewTileEntity(World w, int meta) {
		return (TileEntity)new TileEntityDeconstructor();
	}


	public boolean isOpaqueCube() {
		return false;
	}


	public boolean renderAsNormalBlock() {
		return false;
	}


	public int getRenderType() {
		return -1;
	}


	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int side, float vecX, float vecY, float vecZ) {
		if (p.getCurrentEquippedItem() != null) {
			if (p.getCurrentEquippedItem().getItem() instanceof tb.common.itemblock.ItemBlockCrystal) {
				if (ResearchManager.isResearchComplete(p.getCommandSenderName(), "TB.EntityDecAdv") && p.getCurrentEquippedItem().getItemDamage() < 6) {

					TileEntityDeconstructor d = TileEntityDeconstructor.class.cast(w.getTileEntity(x, y, z));
					switch (p.getCurrentEquippedItem().getItemDamage()) {


					case 0:
						if (!d.hasAir) {

							d.hasAir = true;
							p.inventory.decrStackSize(p.inventory.currentItem, 1);
							d.tickTime = 0;
						} 
						break;


					case 1:
						if (!d.hasFire) {

							d.hasFire = true;
							p.inventory.decrStackSize(p.inventory.currentItem, 1);
							d.tickTime = 0;
						} 
						break;


					case 2:
						if (!d.hasWater) {

							d.hasWater = true;
							p.inventory.decrStackSize(p.inventory.currentItem, 1);
							d.tickTime = 0;
						} 
						break;


					case 3:
						if (!d.hasEarth) {

							d.hasEarth = true;
							p.inventory.decrStackSize(p.inventory.currentItem, 1);
							d.tickTime = 0;
						} 
						break;


					case 4:
						if (!d.hasOrdo) {

							d.hasOrdo = true;
							p.inventory.decrStackSize(p.inventory.currentItem, 1);
							d.tickTime = 0;
						} 
						break;


					case 5:
						if (!d.hasEntropy) {

							d.hasEntropy = true;
							p.inventory.decrStackSize(p.inventory.currentItem, 1);
							d.tickTime = 0;
						} 
						break;
					} 

				} 
			}
		}
		((TileEntityDeconstructor)TileEntityDeconstructor.class.cast(w.getTileEntity(x, y, z))).placerName = p.getCommandSenderName();
		return true;
	}
}


