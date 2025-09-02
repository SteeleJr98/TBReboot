package tb.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tb.init.TBBlocks;
import thaumcraft.api.crafting.IInfusionStabiliser;

public class TBBlock extends Block implements IInfusionStabiliser {
	
	boolean isGlass;
	boolean stabilise;

	public TBBlock(Material m, boolean b) {
		super(m);
		this.isGlass = b;
	}


	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
		if (entity instanceof net.minecraft.entity.boss.EntityDragon) {
			return (this != TBBlocks.enderPlanks);
		}
		return super.canEntityDestroy(world, x, y, z, entity);
	}


	public Block stabilise() {
		this.stabilise = true;
		return this;
	}


	public boolean isOpaqueCube() {
		return !this.isGlass;
	}


	public int getLightOpacity() {
		return this.isGlass ? 7 : 15;
	}


	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return this.isGlass ? 1 : 0;
	}


	public boolean canStabaliseInfusion(World world, int x, int y, int z) {
		return this.stabilise;
	}
}


