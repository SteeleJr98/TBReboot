 package tb.common.event;
 
 import java.util.Random;
 import net.minecraft.block.Block;
 import net.minecraft.util.MathHelper;
 import net.minecraft.world.IBlockAccess;
 import net.minecraft.world.World;
 import net.minecraft.world.gen.feature.WorldGenAbstractTree;
 import net.minecraftforge.common.IPlantable;
 import net.minecraftforge.common.util.ForgeDirection;
 import tb.init.TBBlocks;
 
 public class WorldGenBigOak
   extends WorldGenAbstractTree {
   static final byte[] otherCoordPairs = new byte[] { 2, 0, 0, 1, 2, 1 };
   Random rand = new Random();
   World worldObj;
   int[] basePos = new int[] { 0, 0, 0 };
   int heightLimit;
   int height;
   double heightAttenuation = 0.618D;
   double branchDensity = 1.0D;
   double branchSlope = 0.381D;
   double scaleWidth = 1.0D;
   double leafDensity = 1.0D;
   int trunkSize = 1;
   int heightLimitLimit = 12;
   int leafDistanceLimit = 4;
   
   int[][] leafNodes;
   public Block leavesBlock;
   public Block trunkBlock;
   public int leavesMeta;
   public int trunkMeta;
   
   public WorldGenBigOak(boolean doBlockNotify, int minHeight, int metaWood, int metaLeaves, int treeSize, Block tree, Block leaves) {
     super(doBlockNotify);
     this.trunkMeta = metaWood;
     this.leavesMeta = metaLeaves;
     this.leavesBlock = leaves;
     this.trunkBlock = tree;
     this.height = minHeight;
   }
 
 
 
 
   
   void generateLeafNodeList() {
     this.height = (int)(this.heightLimit * this.heightAttenuation);
     
     if (this.height >= this.heightLimit)
     {
       this.height = this.heightLimit - 1;
     }
     
     int i = (int)(1.382D + Math.pow(this.leafDensity * this.heightLimit / 13.0D, 2.0D));
     
     if (i < 1)
     {
       i = 1;
     }
     
     int[][] aint = new int[i * this.heightLimit][4];
     int j = this.basePos[1] + this.heightLimit - this.leafDistanceLimit;
     int k = 1;
     int l = this.basePos[1] + this.height;
     int i1 = j - this.basePos[1];
     aint[0][0] = this.basePos[0];
     aint[0][1] = j;
     aint[0][2] = this.basePos[2];
     aint[0][3] = l;
     j--;
     
     while (i1 >= 0) {
       
       int j1 = 0;
       float f = layerSize(i1);
       
       if (f < 0.0F) {
         
         j--;
         i1--;
         
         continue;
       } 
       for (double d0 = 0.5D; j1 < i; j1++) {
         
         double d1 = this.scaleWidth * f * (this.rand.nextFloat() + 0.328D);
         double d2 = this.rand.nextFloat() * 2.0D * Math.PI;
         int k1 = MathHelper.floor_double(d1 * Math.sin(d2) + this.basePos[0] + d0);
         int l1 = MathHelper.floor_double(d1 * Math.cos(d2) + this.basePos[2] + d0);
         int[] aint1 = { k1, j, l1 };
         int[] aint2 = { k1, j + this.leafDistanceLimit, l1 };
         
         if (checkBlockLine(aint1, aint2) == -1) {
           
           int[] aint3 = { this.basePos[0], this.basePos[1], this.basePos[2] };
           double d3 = Math.sqrt(Math.pow(Math.abs(this.basePos[0] - aint1[0]), 2.0D) + Math.pow(Math.abs(this.basePos[2] - aint1[2]), 2.0D));
           double d4 = d3 * this.branchSlope;
           
           if (aint1[1] - d4 > l) {
             
             aint3[1] = l;
           }
           else {
             
             aint3[1] = (int)(aint1[1] - d4);
           } 
           
           if (checkBlockLine(aint3, aint1) == -1) {
             
             aint[k][0] = k1;
             aint[k][1] = j;
             aint[k][2] = l1;
             aint[k][3] = aint3[1];
             k++;
           } 
         } 
       } 
       
       j--;
       i1--;
     } 
 
     
     this.leafNodes = new int[k][4];
     System.arraycopy(aint, 0, this.leafNodes, 0, k);
   }
 
   
   void func_150529_a(int p_150529_1_, int p_150529_2_, int p_150529_3_, float p_150529_4_, byte p_150529_5_, Block p_150529_6_) {
     int l = (int)(p_150529_4_ + 0.618D);
     byte b1 = otherCoordPairs[p_150529_5_];
     byte b2 = otherCoordPairs[p_150529_5_ + 3];
     int[] aint = { p_150529_1_, p_150529_2_, p_150529_3_ };
     int[] aint1 = { 0, 0, 0 };
     int i1 = -l;
     int j1 = -l;
     
     for (aint1[p_150529_5_] = aint[p_150529_5_]; i1 <= l; i1++) {
       
       aint1[b1] = aint[b1] + i1;
       j1 = -l;
       
       while (j1 <= l) {
         
         double d0 = Math.pow(Math.abs(i1) + 0.5D, 2.0D) + Math.pow(Math.abs(j1) + 0.5D, 2.0D);
         
         if (d0 > (p_150529_4_ * p_150529_4_)) {
           
           j1++;
           
           continue;
         } 
         aint1[b2] = aint[b2] + j1;
         Block block1 = this.worldObj.getBlock(aint1[0], aint1[1], aint1[2]);
         
         if (!block1.isAir((IBlockAccess)this.worldObj, aint1[0], aint1[1], aint1[2]) && !block1.isLeaves((IBlockAccess)this.worldObj, aint1[0], aint1[1], aint1[2])) {
           
           j1++;
           
           continue;
         } 
         setBlockAndNotifyAdequately(this.worldObj, aint1[0], aint1[1], aint1[2], p_150529_6_, this.leavesMeta);
         j1++;
       } 
     } 
   }
 
 
 
 
 
   
   float layerSize(int p_76490_1_) {
     float f2;
     if (p_76490_1_ < this.heightLimit * 0.3D)
     {
       return -1.618F;
     }
 
     
     float f = this.heightLimit / 2.0F;
     float f1 = this.heightLimit / 2.0F - p_76490_1_;
 
     
     if (f1 == 0.0F) {
       
       f2 = f;
     }
     else if (Math.abs(f1) >= f) {
       
       f2 = 0.0F;
     }
     else {
       
       f2 = (float)Math.sqrt(Math.pow(Math.abs(f), 2.0D) - Math.pow(Math.abs(f1), 2.0D));
     } 
     
     f2 *= 0.5F;
     return f2;
   }
 
 
   
   float leafSize(int p_76495_1_) {
     return (p_76495_1_ >= 0 && p_76495_1_ < this.leafDistanceLimit) ? ((p_76495_1_ != 0 && p_76495_1_ != this.leafDistanceLimit - 1) ? 3.0F : 2.0F) : -1.0F;
   }
 
 
 
 
   
   void generateLeafNode(int p_76491_1_, int p_76491_2_, int p_76491_3_) {
     int l = p_76491_2_;
     
     for (int i1 = p_76491_2_ + this.leafDistanceLimit; l < i1; l++) {
       
       float f = leafSize(l - p_76491_2_);
       func_150529_a(p_76491_1_, l, p_76491_3_, f, (byte)1, this.leavesBlock);
     } 
   }
 
   
   void func_150530_a(int[] p_150530_1_, int[] p_150530_2_, Block p_150530_3_) {
     int[] aint2 = { 0, 0, 0 };
     byte b0 = 0;
     
     byte b1;
     for (b1 = 0; b0 < 3; b0 = (byte)(b0 + 1)) {
       
       aint2[b0] = p_150530_2_[b0] - p_150530_1_[b0];
       
       if (Math.abs(aint2[b0]) > Math.abs(aint2[b1]))
       {
         b1 = b0;
       }
     } 
     
     if (aint2[b1] != 0) {
       
       byte b4, b2 = otherCoordPairs[b1];
       byte b3 = otherCoordPairs[b1 + 3];
 
       
       if (aint2[b1] > 0) {
         
         b4 = 1;
       }
       else {
         
         b4 = -1;
       } 
       
       double d0 = aint2[b2] / aint2[b1];
       double d1 = aint2[b3] / aint2[b1];
       int[] aint3 = { 0, 0, 0 };
       int i = 0;
       
       for (int j = aint2[b1] + b4; i != j; i += b4) {
         
         aint3[b1] = MathHelper.floor_double((p_150530_1_[b1] + i) + 0.5D);
         aint3[b2] = MathHelper.floor_double(p_150530_1_[b2] + i * d0 + 0.5D);
         aint3[b3] = MathHelper.floor_double(p_150530_1_[b3] + i * d1 + 0.5D);
         byte b5 = (byte)this.trunkMeta;
         int k = Math.abs(aint3[0] - p_150530_1_[0]);
         int l = Math.abs(aint3[2] - p_150530_1_[2]);
         int i1 = Math.max(k, l);
         
         if (i1 > 0)
         {
           if (k == i1) {
             
             b5 = (byte)(b5 + 4);
           }
           else if (l == i1) {
             
             b5 = (byte)(b5 + 8);
           } 
         }
         
         setBlockAndNotifyAdequately(this.worldObj, aint3[0], aint3[1], aint3[2], p_150530_3_, b5);
       } 
     } 
   }
 
 
 
 
   
   void generateLeaves() {
     int i = 0;
     
     for (int j = this.leafNodes.length; i < j; i++) {
       
       int k = this.leafNodes[i][0];
       int l = this.leafNodes[i][1];
       int i1 = this.leafNodes[i][2];
       generateLeafNode(k, l, i1);
     } 
   }
 
 
 
 
   
   boolean leafNodeNeedsBase(int p_76493_1_) {
     return (p_76493_1_ >= this.heightLimit * 0.2D);
   }
 
 
 
 
 
   
   void generateTrunk() {
     int i = this.basePos[0];
     int j = this.basePos[1];
     int k = this.basePos[1] + this.height;
     int l = this.basePos[2];
     int[] aint = { i, j, l };
     int[] aint1 = { i, k, l };
     func_150530_a(aint, aint1, this.trunkBlock);
     
     if (this.trunkSize == 2) {
       
       aint[0] = aint[0] + 1;
       aint1[0] = aint1[0] + 1;
       func_150530_a(aint, aint1, this.trunkBlock);
       aint[2] = aint[2] + 1;
       aint1[2] = aint1[2] + 1;
       func_150530_a(aint, aint1, this.trunkBlock);
       aint[0] = aint[0] + -1;
       aint1[0] = aint1[0] + -1;
       func_150530_a(aint, aint1, this.trunkBlock);
     } 
   }
 
 
 
 
   
   void generateLeafNodeBases() {
     int i = 0;
     int j = this.leafNodes.length;
     
     for (int[] aint = { this.basePos[0], this.basePos[1], this.basePos[2] }; i < j; i++) {
       
       int[] aint1 = this.leafNodes[i];
       int[] aint2 = { aint1[0], aint1[1], aint1[2] };
       aint[1] = aint1[3];
       int k = aint[1] - this.basePos[1];
       
       if (leafNodeNeedsBase(k))
       {
         func_150530_a(aint, aint2, this.trunkBlock);
       }
     } 
   }
 
 
 
 
   
   int checkBlockLine(int[] p_76496_1_, int[] p_76496_2_) {
     byte b4;
     int[] aint2 = { 0, 0, 0 };
     byte b0 = 0;
     
     byte b1;
     for (b1 = 0; b0 < 3; b0 = (byte)(b0 + 1)) {
       
       aint2[b0] = p_76496_2_[b0] - p_76496_1_[b0];
       
       if (Math.abs(aint2[b0]) > Math.abs(aint2[b1]))
       {
         b1 = b0;
       }
     } 
     
     if (aint2[b1] == 0)
     {
       return -1;
     }
 
     
     byte b2 = otherCoordPairs[b1];
     byte b3 = otherCoordPairs[b1 + 3];
 
     
     if (aint2[b1] > 0) {
       
       b4 = 1;
     }
     else {
       
       b4 = -1;
     } 
     
     double d0 = aint2[b2] / aint2[b1];
     double d1 = aint2[b3] / aint2[b1];
     int[] aint3 = { 0, 0, 0 };
     int i = 0;
     
     int j;
     for (j = aint2[b1] + b4; i != j; i += b4) {
       
       aint3[b1] = p_76496_1_[b1] + i;
       aint3[b2] = MathHelper.floor_double(p_76496_1_[b2] + i * d0);
       aint3[b3] = MathHelper.floor_double(p_76496_1_[b3] + i * d1);
       if (!isReplaceable(this.worldObj, aint3[0], aint3[1], aint3[2])) {
         break;
       }
     } 
 
     
     return (i == j) ? -1 : Math.abs(i);
   }
 
 
 
 
 
 
   
   boolean validTreeLocation() {
     int[] aint = { this.basePos[0], this.basePos[1], this.basePos[2] };
     int[] aint1 = { this.basePos[0], this.basePos[1] + this.heightLimit - 1, this.basePos[2] };
     Block block = this.worldObj.getBlock(this.basePos[0], this.basePos[1] - 1, this.basePos[2]);
     
     boolean isSoil = block.canSustainPlant((IBlockAccess)this.worldObj, this.basePos[0], this.basePos[1] - 1, this.basePos[2], ForgeDirection.UP, (IPlantable)TBBlocks.sapling);
     if (!isSoil)
     {
       return false;
     }
 
     
     int i = checkBlockLine(aint, aint1);
     
     if (i == -1)
     {
       return true;
     }
     if (i < 6)
     {
       return false;
     }
 
     
     this.heightLimit = i;
     return true;
   }
 
 
 
 
 
 
   
   public void setScale(double p_76487_1_, double p_76487_3_, double p_76487_5_) {
     this.heightLimitLimit = (int)(p_76487_1_ * 12.0D);
     
     if (p_76487_1_ > 0.5D)
     {
       this.leafDistanceLimit = 5;
     }
     
     this.scaleWidth = p_76487_3_;
     this.leafDensity = p_76487_5_;
   }
 
   
   public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_) {
     this.worldObj = p_76484_1_;
     long l = p_76484_2_.nextLong();
     this.rand.setSeed(l);
     this.basePos[0] = p_76484_3_;
     this.basePos[1] = p_76484_4_;
     this.basePos[2] = p_76484_5_;
     
     if (this.heightLimit == 0)
     {
       this.heightLimit = 5 + this.rand.nextInt(this.heightLimitLimit);
     }
     
     if (!validTreeLocation()) {
       
       this.worldObj = null;
       return false;
     } 
 
     
     generateLeafNodeList();
     generateLeaves();
     generateTrunk();
     generateLeafNodeBases();
     this.worldObj = null;
     return true;
   }
 }


