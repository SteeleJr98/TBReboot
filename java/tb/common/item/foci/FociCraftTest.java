package tb.common.item.foci;

import java.lang.reflect.Method;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import tb.core.TBCore;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.WarpEvents;
import thaumcraft.common.tiles.TileMagicWorkbench;

public class FociCraftTest extends ItemFocusBasic {

	@Override
	public int getFocusColor(ItemStack focusStack) {
		return 11184810;
	}

	@Override
	public String getSortingHelper(ItemStack focusstack) {
		String out = "CT";
		for (short id : getAppliedUpgrades(focusstack)) {
			out = out + id;
		}
		return out;
	}

	@Override
	public AspectList getVisCost(ItemStack focusstack) {
		return (new AspectList()).add(Aspect.ORDER, 5).add(Aspect.EARTH, 10);
	}

	@Override
	public int getActivationCooldown(ItemStack focusstack) {
		return 0;
	}

	@Override
	public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack focusstack, int rank) {
		return new FocusUpgradeType[] { FocusUpgradeType.frugal, FocusUpgradeType.potency };
	}

	public ItemStack onFocusRightClick(ItemStack wandstack, World world, EntityPlayer player, MovingObjectPosition movingobjectposition) {



		if (!world.isRemote){
			player.openGui(TBCore.instance, 4331811, world, (int)player.posX, (int)player.posY, (int)player.posZ);




			//			Class<WarpEvents> warpClass = WarpEvents.class;
			//			try {
			//				Method method = warpClass.getDeclaredMethod("suddenlySpiders", new Class[] { EntityPlayer.class, int.class, boolean.class });
			//				boolean access = method.isAccessible();
			//				
			//				if (!access) {
			//					method.setAccessible(true);
			//				}
			//				
			//				method.invoke((Object)null, new Object[] {player, 5, false});
			//				
			//				
			//				if (!access) {
			//					method.setAccessible(false);
			//				}
			//				
			//				
			//				
			//			} catch (Exception e) {
			//				return wandstack;
			//			}


		}
		return wandstack;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		super.registerIcons(reg);
		this.icon = reg.registerIcon(getIconString());
	}

}
