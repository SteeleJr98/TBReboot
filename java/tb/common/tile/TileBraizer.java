 package tb.common.tile;
 
 import java.util.ArrayList;
 import java.util.List;
 import net.minecraft.entity.EntityCreature;
 import net.minecraft.entity.ai.EntityAIBase;
 import net.minecraft.entity.ai.EntityAITasks;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.util.AxisAlignedBB;
 import net.minecraftforge.common.util.ForgeDirection;
 import tb.common.entity.EntityAIAvoidCampfire;
 import thaumcraft.api.aspects.Aspect;
 import thaumcraft.common.lib.events.EssentiaHandler;
 
 
 
 public class TileBraizer
   extends TileEntity
 {
   public int burnTime;
   public int uptime;
   
   public void readFromNBT(NBTTagCompound tag) {
     super.readFromNBT(tag);
     this.burnTime = tag.getInteger("burnTime");
   }
 
   
   public void writeToNBT(NBTTagCompound tag) {
     super.writeToNBT(tag);
     tag.setInteger("burnTime", this.burnTime);
   }
 
 
   
   public void updateEntity() {
     this.uptime++;
     
     if (this.burnTime > 0) {
       
       this.burnTime--;
     
     }
     else if (EssentiaHandler.drainEssentia(this, Aspect.FIRE, ForgeDirection.UNKNOWN, 6, false)) {
       
       this.burnTime = 1600;
       this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 1, 3);
     } 
 
     
     if (!this.worldObj.isRemote)
     {
       if (this.burnTime <= 0) {
         
         int metadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
         if (metadata == 1) {
           this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 0, 3);
         }
         
         return;
       } 
     }
     
     if (!this.worldObj.isRemote) {
       
       List<EntityCreature> creatures = this.worldObj.getEntitiesWithinAABB(EntityCreature.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, (this.xCoord + 1), (this.yCoord + 1), (this.zCoord + 1)).expand(12.0D, 6.0D, 12.0D));
       
       for (EntityCreature c : creatures) {
         
         if (!(c instanceof net.minecraft.entity.monster.IMob)) {
           continue;
         }
         ArrayList<EntityAITasks.EntityAITaskEntry> entries = (ArrayList)c.tasks.taskEntries;
         
         EntityAIAvoidCampfire campfireAviodable = null;
         
         boolean hasTask = false;
         for (int i = 0; i < entries.size(); i++) {
           
           EntityAITasks.EntityAITaskEntry task = entries.get(i);
           if (task.action instanceof EntityAIAvoidCampfire) {
             
             campfireAviodable = (EntityAIAvoidCampfire)task.action;
             hasTask = true;
             break;
           } 
         } 
         if (campfireAviodable == null) {
           campfireAviodable = new EntityAIAvoidCampfire(c);
         }
         campfireAviodable.isScared = true;
         campfireAviodable.scareTimer = 100;
         campfireAviodable.campfireX = this.xCoord;
         campfireAviodable.campfireY = this.yCoord;
         campfireAviodable.campfireZ = this.zCoord;
         
         if (!hasTask)
           c.tasks.addTask(1, (EntityAIBase)campfireAviodable); 
       } 
     } 
   }
 }


