 package tb.common.tile;
 
 //import DummyCore.Utils.MiscUtils; //Seriously i need help
 import java.util.Hashtable;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.ItemStack;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.world.World;
import tb.utils.DummySteele;
import thaumcraft.api.aspects.Aspect;
 import thaumcraft.api.nodes.INode;
 import thaumcraft.api.nodes.NodeModifier;
 import thaumcraft.api.nodes.NodeType;
 import thaumcraft.api.wands.IWandable;
 import thaumcraft.common.Thaumcraft;
 import thaumcraft.common.entities.EntityAspectOrb;
 
 public class TileNodeManipulator
   extends TileEntity implements IWandable {
   public int workTime = 0;
   public Hashtable<String, Integer> nodeAspects = new Hashtable<String, Integer>();
   public Hashtable<String, Integer> newNodeAspects = new Hashtable<String, Integer>();
   
   public boolean firstTick = true;
   
   public void updateEntity() {
     INode node = getNode();
     
     if (node == null) {
       return;
     }
     this.newNodeAspects.clear(); int i;
     for (i = 0; i < node.getAspects().size(); i++)
     {
       this.newNodeAspects.put(node.getAspects().getAspects()[i].getTag(), Integer.valueOf(node.getAspects().getAmount(node.getAspects().getAspects()[i])));
     }
     
     if (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) == 0 || getNode() == null) {
       this.workTime = 0;
     }
     if (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) != 0 && getNode() != null) {
       
       int color = 16777215;
       int effect = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) - 1;
       
       switch (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) - 1) {
 
         
         case 0:
           color = 16777215;
           break;
 
         
         case 1:
           color = 5130070;
           break;
 
         
         case 2:
           color = 13816320;
           break;
 
         
         case 3:
           color = 11500579;
           break;
 
         
         case 4:
           color = 740674;
           break;
 
         
         case 5:
           color = 13420791;
           break;
 
         
         case 6:
           color = 6569051;
           break;
 
         
         case 7:
           color = 15395562;
           break;
 
         
         case 8:
           color = 13689080;
           break;
 
         
         case 9:
           color = 7419030;
           break;
       } 
       
       if (!this.firstTick && this.worldObj.isRemote) {
         Thaumcraft.proxy.beam(this.worldObj, this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, this.xCoord + 0.5D, this.yCoord - 0.5D, this.zCoord + 0.5D, 2, color, false, 0.5F, 2);
       }
       if (!this.firstTick) {
         
         if (effect == 7)
         {
           if (!this.worldObj.isRemote) {
             
             boolean isNodeChanged = false;
             for (int j = 0; j < node.getAspects().size(); j++) {
               
               Aspect a = node.getAspects().getAspects()[j];
               int max = node.getNodeVisBase(a);
               int current = node.getAspects().getAmount(a);
               
               if (current < max && this.worldObj.rand.nextFloat() < 0.01F) {
                 
                 node.getAspects().add(a, 1);
                 isNodeChanged = true;
               } 
             } 
             if (isNodeChanged)
             {
               DummySteele.sendPacketToAllAround(this.worldObj, this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord).getDescriptionPacket(), this.xCoord, this.yCoord, this.zCoord, this.worldObj.provider.dimensionId, 6.0D);
             }
           } 
         }
         if (effect == 1) {
           
           int maxTimeRequired = (node.getNodeModifier() == NodeModifier.BRIGHT) ? 1200 : ((node.getNodeModifier() == NodeModifier.PALE) ? 3600 : ((node.getNodeModifier() == NodeModifier.FADING) ? 7200 : 2400));
           if (this.workTime >= maxTimeRequired) {
             
             this.workTime = 0;
             if (node.getNodeModifier() == NodeModifier.FADING) {
               
               this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
               return;
             } 
             node.setNodeModifier((node.getNodeModifier() == NodeModifier.BRIGHT) ? null : ((node.getNodeModifier() == NodeModifier.PALE) ? NodeModifier.FADING : ((node.getNodeModifier() == NodeModifier.FADING) ? NodeModifier.FADING : NodeModifier.PALE)));
           }
           else {
             
             this.workTime++;
             if (this.worldObj.getWorldTime() % 10L == 0L) {
               
               Aspect a = node.getAspects().getAspects()[this.worldObj.rand.nextInt((node.getAspects().getAspects()).length)];
               EntityAspectOrb aspect = new EntityAspectOrb(this.worldObj, this.xCoord + 0.5D, this.yCoord - 0.5D, this.zCoord + 0.5D, a, 1);
               if (!this.worldObj.isRemote)
               {
                 this.worldObj.spawnEntityInWorld((Entity)aspect);
               }
             } 
           } 
         } 
         if (effect == 2)
         {
           for (int j = 0; j < node.getAspects().size(); j++) {
             
             Aspect a = node.getAspects().getAspects()[j];
             int current = node.getAspects().getAmount(a);
             if (this.nodeAspects.containsKey(a.getTag())) {
               
               int prev = ((Integer)this.nodeAspects.get(a.getTag())).intValue();
               
               if (current < prev && this.worldObj.rand.nextFloat() < 0.5F)
               {
                 node.getAspects().add(a, 1);
               }
             } 
           } 
         }
         if (effect == 3)
         {
           if (node.getNodeType() == NodeType.NORMAL)
           {
             if (this.workTime > 6000) {
               
               this.workTime = 0;
               node.setNodeType(NodeType.HUNGRY);
             } else {
               
               this.workTime++;
             } 
           }
         }
         if (effect == 4)
         {
           if (node.getNodeType() == NodeType.NORMAL)
           {
             if (this.workTime > 8400) {
               
               this.workTime = 0;
               node.setNodeType(NodeType.UNSTABLE);
             } else {
               
               this.workTime++;
             } 
           }
         }
         if (effect == 5)
         {
           if (node.getNodeType() == NodeType.NORMAL)
           {
             if (this.workTime > 3600) {
               
               this.workTime = 0;
               node.setNodeType(NodeType.PURE);
             } else {
               
               this.workTime++;
             } 
           }
         }
         if (effect == 6)
         {
           if (node.getNodeType() == NodeType.NORMAL)
           {
             if (this.workTime > 7200) {
               
               this.workTime = 0;
               node.setNodeType(NodeType.DARK);
             } else {
               
               this.workTime++;
             } 
           }
         }
         if (effect == 8) {
           
           int maxTimeRequired = 0;
           
           if (node.getNodeModifier() == NodeModifier.FADING)
           {
             maxTimeRequired = 6000;
           }
           
           if (node.getNodeModifier() == NodeModifier.PALE)
           {
             maxTimeRequired = 12000;
           }
           
           if (node.getNodeType() == NodeType.DARK)
           {
             maxTimeRequired = 2400;
           }
           
           if (node.getNodeType() == NodeType.HUNGRY)
           {
             maxTimeRequired = 600;
           }
           
           if (node.getNodeType() == NodeType.UNSTABLE)
           {
             maxTimeRequired = 8400;
           }
           
           if (this.workTime >= maxTimeRequired) {
             
             this.workTime = 0;
             if (node.getNodeModifier() == NodeModifier.FADING) {
               
               node.setNodeModifier(NodeModifier.PALE);
               
               return;
             } 
             if (node.getNodeModifier() == NodeModifier.PALE) {
               
               node.setNodeModifier(null);
               
               return;
             } 
             if (node.getNodeType() == NodeType.DARK) {
               
               node.setNodeType(NodeType.NORMAL);
               
               return;
             } 
             if (node.getNodeType() == NodeType.HUNGRY) {
               
               node.setNodeType(NodeType.NORMAL);
               
               return;
             } 
             if (node.getNodeType() == NodeType.UNSTABLE) {
               
               node.setNodeType(NodeType.NORMAL);
 
               
               return;
             } 
           } else {
             this.workTime++;
           } 
         } 
         if (effect == 0)
         {
           if (node.getNodeModifier() == null)
           {
             if (this.workTime > 24000) {
               
               this.workTime = 0;
               node.setNodeModifier(NodeModifier.BRIGHT);
             } else {
               
               this.workTime++;
             } 
           }
         }
         if (effect == 9)
         {
           if (node.getNodeType() == NodeType.NORMAL)
           {
             if (this.workTime > 7200) {
               
               this.workTime = 0;
               node.setNodeType(NodeType.TAINTED);
             } else {
               
               this.workTime++;
             } 
           }
         }
       } 
     } 
     
     this.firstTick = false;
     
     this.nodeAspects.clear();
     for (i = 0; i < node.getAspects().size(); i++)
     {
       this.nodeAspects.put(node.getAspects().getAspects()[i].getTag(), Integer.valueOf(node.getAspects().getAmount(node.getAspects().getAspects()[i])));
     }
   }
 
   
   public INode getNode() {
     if (this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord) instanceof INode) {
       return INode.class.cast(this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord));
     }
     return null;
   }
 
   
   public void readFromNBT(NBTTagCompound tag) {
     super.readFromNBT(tag);
     this.workTime = tag.getInteger("workTime");
   }
 
   
   public void writeToNBT(NBTTagCompound tag) {
     super.writeToNBT(tag);
     tag.setInteger("workTime", this.workTime);
   }
 
   
   public int onWandRightClick(World world, ItemStack wandstack, EntityPlayer player, int x, int y, int z, int side, int md) {
     return 0;
   }
 
   
   public ItemStack onWandRightClick(World world, ItemStack wandstack, EntityPlayer player) {
     return wandstack;
   }
   
   public void onUsingWandTick(ItemStack wandstack, EntityPlayer player, int count) {}
   
   public void onWandStoppedUsing(ItemStack wandstack, World world, EntityPlayer player, int count) {}
 }


