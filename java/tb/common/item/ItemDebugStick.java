package tb.common.item;

import java.util.List;
import java.util.Random;

//import com.ilya3point999k.thaumicconcilium.common.entities.DopeSquid;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import tb.utils.DummySteele;
import tb.utils.TBUtils;

public class ItemDebugStick extends Item {

	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int p_77648_7_, float hitX, float hitY, float hitZ) {

		Block block = world.getBlock(x, y, z);
		if (!world.isRemote) {
			int eX = (int) player.posX + 5;
			int eZ = (int) player.posZ + 5;
			int eY = TBUtils.getTopBlock(world, eX, eZ);
			world.createExplosion(null, eX, eY + 1, eZ, 5, false);
		}

		return true;
	}

}
