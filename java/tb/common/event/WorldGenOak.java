package tb.common.event;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import tb.utils.DummySteele;

public class WorldGenOak extends WorldGenTrees {
	
	public Block leavesBlock;
	public Block trunkBlock;
	public int metaLeaves;
	public int metaWood;
	public int minTreeHeight;

	private boolean vinesGrow = false;

	public WorldGenOak(boolean doBlockNotify, int minHeight, int metaWood, int metaLeaves, boolean doVines, Block tree, Block leaves) {
		super(doBlockNotify, minHeight, metaWood, metaLeaves, doVines);
		this.metaWood = metaWood;
		this.metaLeaves = metaLeaves;
		this.leavesBlock = leaves;
		this.trunkBlock = tree;
		this.minTreeHeight = minHeight;
	}


	@Override
	public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {
		//	 DummySteele.sendMessageFromServer("----Starting tree generation----");
		//	 DummySteele.sendMessageFromServer("Wood type: " + this.trunkBlock.getClass());
		//	 DummySteele.sendMessageFromServer("Wood meta: " + this.metaWood);
		//	 DummySteele.sendMessageFromServer("Leaf type: " + this.leavesBlock.getClass());
		//	 DummySteele.sendMessageFromServer("Leaf meta: " + this.metaLeaves);

		int l = p_76484_2_.nextInt(3) + this.minTreeHeight;
		boolean flag = true;

		if (p_76484_4_ >= 1 && p_76484_4_ + l + 1 <= 256)
		{
			byte b0;
			int k1;
			Block block;

			for (int i1 = p_76484_4_; i1 <= p_76484_4_ + 1 + l; ++i1) {
				b0 = 1;

				if (i1 == p_76484_4_) {
					b0 = 0;
				}

				if (i1 >= p_76484_4_ + 1 + l - 2) {
					b0 = 2;
				}

				for (int j1 = p_76484_3_ - b0; j1 <= p_76484_3_ + b0 && flag; ++j1) {
					for (k1 = p_76484_5_ - b0; k1 <= p_76484_5_ + b0 && flag; ++k1) {
						if (i1 >= 0 && i1 < 256) {
							block = p_76484_1_.getBlock(j1, i1, k1);

							if (!this.isReplaceable(p_76484_1_, j1, i1, k1)) {
								flag = false;
							}
						}
						else {
							flag = false;
						}
					}
				}
			}

			if (!flag) {
				return false;
			}
			else {
				Block block2 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ - 1, p_76484_5_);

				boolean isSoil = block2.canSustainPlant(p_76484_1_, p_76484_3_, p_76484_4_ - 1, p_76484_5_, ForgeDirection.UP, (BlockSapling)Blocks.sapling);
				if (isSoil && p_76484_4_ < 256 - l - 1) {
					block2.onPlantGrow(p_76484_1_, p_76484_3_, p_76484_4_ - 1, p_76484_5_, p_76484_3_, p_76484_4_, p_76484_5_);
					b0 = 3;
					byte b1 = 0;
					int l1;
					int i2;
					int j2;
					int i3;

					for (k1 = p_76484_4_ - b0 + l; k1 <= p_76484_4_ + l; ++k1) {
						i3 = k1 - (p_76484_4_ + l);
						l1 = b1 + 1 - i3 / 2;

						for (i2 = p_76484_3_ - l1; i2 <= p_76484_3_ + l1; ++i2) {
							j2 = i2 - p_76484_3_;

							for (int k2 = p_76484_5_ - l1; k2 <= p_76484_5_ + l1; ++k2) {
								int l2 = k2 - p_76484_5_;

								if (Math.abs(j2) != l1 || Math.abs(l2) != l1 || p_76484_2_.nextInt(2) != 0 && i3 != 0) {
									Block block1 = p_76484_1_.getBlock(i2, k1, k2);

									if (block1.isAir(p_76484_1_, i2, k1, k2) || block1.isLeaves(p_76484_1_, i2, k1, k2)) {
										this.setBlockAndNotifyAdequately(p_76484_1_, i2, k1, k2, this.leavesBlock, this.metaLeaves);
									}
								}
							}
						}
					}

					for (k1 = 0; k1 < l; ++k1) {
						block = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ + k1, p_76484_5_);

						if (block.isAir(p_76484_1_, p_76484_3_, p_76484_4_ + k1, p_76484_5_) || block.isLeaves(p_76484_1_, p_76484_3_, p_76484_4_ + k1, p_76484_5_)) {
							this.setBlockAndNotifyAdequately(p_76484_1_, p_76484_3_, p_76484_4_ + k1, p_76484_5_, this.trunkBlock, this.metaWood);

							if (this.vinesGrow && k1 > 0) {
								if (p_76484_2_.nextInt(3) > 0 && p_76484_1_.isAirBlock(p_76484_3_ - 1, p_76484_4_ + k1, p_76484_5_)) {
									this.setBlockAndNotifyAdequately(p_76484_1_, p_76484_3_ - 1, p_76484_4_ + k1, p_76484_5_, Blocks.vine, 8);
								}

								if (p_76484_2_.nextInt(3) > 0 && p_76484_1_.isAirBlock(p_76484_3_ + 1, p_76484_4_ + k1, p_76484_5_)) {
									this.setBlockAndNotifyAdequately(p_76484_1_, p_76484_3_ + 1, p_76484_4_ + k1, p_76484_5_, Blocks.vine, 2);
								}

								if (p_76484_2_.nextInt(3) > 0 && p_76484_1_.isAirBlock(p_76484_3_, p_76484_4_ + k1, p_76484_5_ - 1)) {
									this.setBlockAndNotifyAdequately(p_76484_1_, p_76484_3_, p_76484_4_ + k1, p_76484_5_ - 1, Blocks.vine, 1);
								}

								if (p_76484_2_.nextInt(3) > 0 && p_76484_1_.isAirBlock(p_76484_3_, p_76484_4_ + k1, p_76484_5_ + 1)) {
									this.setBlockAndNotifyAdequately(p_76484_1_, p_76484_3_, p_76484_4_ + k1, p_76484_5_ + 1, Blocks.vine, 4);
								}
							}
						}
					}



					return true;
				}
				else {
					return false;
				}
			}
		}
		else {
			return false;
		}


		//     if (y >= 1 && y + height + 1 <= 256) {
		//    	 DummySteele.sendMessageFromServer("past main if");
		//    	 
		// 
		// 
		//       
		//       for (int i1 = y; i1 <= y + 1 + height; i1++) {
		//         
		//         byte b0 = 1;
		//         
		//         if (i1 == y)
		//         {
		//           b0 = 0;
		//         }
		//         
		//         if (i1 >= y + 1 + height - 2)
		//         {
		//           b0 = 2;
		//         }
		//         
		//         for (int j1 = x - b0; j1 <= x + b0 && flag; j1++) {
		//           
		//           for (int k1 = z - b0; k1 <= z + b0 && flag; k1++) {
		//             
		//             if (i1 >= 0 && i1 < 256) {
		//               
		//               Block block = w.getBlock(j1, i1, k1);
		//               
		//               if (!isReplaceable(w, j1, i1, k1))
		//               {
		//            	 DummySteele.sendMessageFromServer("Block " + block.getUnlocalizedName() + " reporting as 'not replaceable'");
		//                 flag = false;
		//               }
		//             }
		//             else {
		//               DummySteele.sendMessageFromServer("i1 reporting as not in 0-255 at: " + i1);
		//               flag = false;
		//             } 
		//           } 
		//         } 
		// 
		// 
		//         
		//         if (!flag)
		//         {
		//           return false;
		//         }
		// 
		//         
		//         Block block2 = w.getBlock(x, y - 1, z);
		//         
		//         boolean isSoil = block2.canSustainPlant((IBlockAccess)w, x, y - 1, z, ForgeDirection.UP, (IPlantable)Blocks.sapling);
		//         if (isSoil && y < 256 - height - 1) {
		//           
		//           block2.onPlantGrow(w, x, y - 1, z, x, y, z);
		//           b0 = 3;
		//           byte b1 = 0;
		// 
		//           
		//           int k1;
		// 
		//           
		//           for (k1 = y - b0 + height; k1 <= y + height; k1++) {
		//             
		//             int i3 = k1 - y + height;
		//             int l1 = b1 + 1 - i3 / 2;
		//             
		//             for (int i2 = x - l1; i2 <= x + l1; i2++) {
		//               
		//               int j2 = i2 - x;
		//               
		//               for (int k2 = z - l1; k2 <= z + l1; k2++) {
		//                 
		//                 int l2 = k2 - z;
		//                 
		//                 if (Math.abs(j2) != l1 || Math.abs(l2) != l1 || (rnd.nextInt(2) != 0 && i3 != 0)) {
		//                   
		//                   Block block1 = w.getBlock(i2, k1, k2);
		//                   
		//                   if (block1.isAir((IBlockAccess)w, i2, k1, k2) || block1.isLeaves((IBlockAccess)w, i2, k1, k2))
		//                   {
		//                	 DummySteele.sendMessageFromServer("Attempting to set block " + this.leavesBlock + " with meta " + this.leavesMeta);
		//                     setBlockAndNotifyAdequately(w, i2, k1, k2, this.leavesBlock, this.leavesMeta);
		//                   }
		//                 } 
		//               } 
		//             } 
		//           } 
		//           
		//           for (k1 = 0; k1 < height; k1++)
		//           {
		//             Block block = w.getBlock(x, y + k1, z);
		//             
		//             if (block.isAir((IBlockAccess)w, x, y + k1, z) || block.isLeaves((IBlockAccess)w, x, y + k1, z))
		//             {
		//               setBlockAndNotifyAdequately(w, x, y + k1, z, this.trunkBlock, this.trunkMeta);
		//             }
		//           }
		//         
		//         } else {
		//           
		//           return false;
		//         } 
		//       } 
		//     }
		// 
		//     
		//     return false;
	}
}


