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

			if (player.isSneaking()) {
				//				Entity testEntity = new DopeSquid(world);
				//				testEntity.posX = player.posX;
				//				testEntity.posY = player.posY + 1;
				//				testEntity.posZ = player.posZ;
				//				world.spawnEntityInWorld(testEntity);
//				AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(player.posX - 5.0D, player.posY - 5.0D, player.posZ - 5.0D, player.posX + 5.0D, player.posY + 5.0D, player.posZ + 5.0D);
//				
//
//				for (Object o : world.getEntitiesWithinAABB(EntityLivingBase.class, aabb)) {
//					
//					EntityLivingBase e = (EntityLivingBase) o;
//					player.addChatComponentMessage(new ChatComponentText("entity: " + e.getClass()));
//					if (!(e instanceof EntityPlayer)) {
//						if (e.attackEntityFrom(DamageSource.outOfWorld, e.getMaxHealth() * 2)) {
//							player.addChatComponentMessage(new ChatComponentText("hurt entity"));
//						}
//					}
//				}
				player.addChatComponentMessage(new ChatComponentText("Block meta: " + world.getBlockMetadata(x, y, z)));
				
			}
			else {
				
				if (block == Blocks.dirt || block == Blocks.grass) {
					if (world.rand.nextBoolean()) {
						world.setBlock(x, y + 1, z, Blocks.tallgrass, 1, 3);
					}
					else {
//						world.setBlock(x, y + 1, z, Blocks.double_plant, 2, 3);
//						world.setBlock(x, y + 2, z, Blocks.double_plant, 8, 3);
						Blocks.double_plant.func_149889_c(world, x, y + 1, z, 2, 2);
					}
					
				}
				
//				PositionImpl tempPos = TBUtils.getCornerOfCube(world, x, y, z);
//				world.setBlock((int)tempPos.getX(), (int)tempPos.getY(), (int)tempPos.getZ(), Blocks.redstone_block);
				
				
				
				
//				int dist = 1;
//				for (int xx = -dist; xx <= dist; xx++) {
//					for (int yy = -dist; yy <= dist; yy++) {
//						for (int zz = -dist; zz <= dist; zz++) {
//							Block tempBlock = world.getBlock(x + xx, y + yy, z + zz);
//							DummySteele.sendMessageFromServer(tempBlock.getClass().toString());
//							if (tempBlock instanceof BlockBush) {
//								DummySteele.sendMessageFromServer("in bush if");
//								Random rnd = world.rand;
//								BlockBush tempBush = (BlockBush) tempBlock;
//								tempBush.updateTick(world, x + xx, y + yy, z + zz, rnd);
//							}
//						}
//					}
//				}
				
				
//				if (!(world.getBlock(x, y, z) == Blocks.end_stone)) {
//					TBUtils.sendChatToSender(player, "Block Class: " + block.getClass().toString());
//					TBUtils.sendChatToSender(player, Item.getItemFromBlock(block).getClass().toString());
//					TBUtils.sendChatToSender(player, "Block Meta: " + world.getBlockMetadata(x, y, z));
//					Block worldBlock = world.getBlock(x, y, z);
//					ItemStack worldStack = new ItemStack(worldBlock);
//					TBUtils.sendChatToSender(player, "Stack Damage: " + worldStack.getItemDamage());
//				}
//				else {
//					AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(x - 5, y - 5, z - 5, x + 5, y + 5, z + 5);
//					List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, aabb);
//					if (!(list.size() == 0)) {
//						for (EntityItem e : list) {
//							int damage = e.getEntityItem().getItemDamage();
//							TBUtils.sendChatToSender(player, "Item class: " + TBUtils.getClassLastString(e.getEntityItem().getItem().getClass().toString()));
//						}
//					}
//					else {
//						TBUtils.sendChatToSender(player, "No Entity Items found");
//					}
//				}
			}

		}



		return true;
	}

}
