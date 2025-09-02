package tb.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tb.utils.DummySteele;

public class TBTestBlock extends Block {

	public TBTestBlock(Material mat) {
		super(mat);

	}

	@Override
	public void fillWithRain(World world, int x, int y, int z) {

		this.dropBlockAsItem(world, x, y, z, new ItemStack(this));


		//world.setBlock(x, y, z, Blocks.stone);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity entity) {
		if (!world.isRemote && entity instanceof EntityArrow) {
			DummySteele.sendMessageFromServer("Collided entity: " + entity.getClass().toString());
			EntityLightningBolt lightning = new EntityLightningBolt(world, p_149670_2_, p_149670_3_, p_149670_4_);
			//world.addWeatherEffect(lightning);
			world.spawnEntityInWorld(lightning);
		}
	}

}
