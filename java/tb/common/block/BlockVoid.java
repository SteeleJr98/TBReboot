package tb.common.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.common.Thaumcraft;

public class BlockVoid extends Block implements IInfusionStabiliser {
	
	public BlockVoid() {
		super(Material.iron);
		setTickRandomly(true);
		setHardness(5.0F);
		setResistance(Float.MAX_VALUE);
		setHarvestLevel("pickaxe", 3);
		setBlockTextureName("thaumicbases:voidblock");
	}


	public void randomDisplayTick(World w, int x, int y, int z, Random r) {
		Thaumcraft.proxy.sparkle(x + r.nextFloat(), y + r.nextFloat(), z + r.nextFloat(), 2.0F, 5, -0.1F);
	}

	public void updateTick(World w, int x, int y, int z, Random rnd) {}

	public boolean canStabaliseInfusion(World w, int x, int y, int z) {
		return true;
	}
}


