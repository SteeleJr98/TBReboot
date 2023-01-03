 package tb.common.entity;
 
 import net.minecraft.entity.EntityCreature;
 import net.minecraft.entity.ai.EntityAIBase;
 import net.minecraft.entity.ai.RandomPositionGenerator;
 import net.minecraft.pathfinding.PathEntity;
 import net.minecraft.pathfinding.PathNavigate;
 import net.minecraft.util.Vec3;
 import net.minecraft.world.World;
 
 
 
 public class EntityAIAvoidCampfire
   extends EntityAIBase
 {
   public boolean isScared;
   public int scareTimer;
   public int campfireX;
   public int campfireY;
   public int campfireZ;
   public World worldObj;
   public EntityCreature entityObj;
   private PathEntity entityPathEntity;
   private PathNavigate entityPathNavigate;
   private double farSpeed;
   private double nearSpeed;
   
   public EntityAIAvoidCampfire(EntityCreature creature) {
     this.scareTimer = 100;
     this.worldObj = creature.worldObj;
     this.entityObj = creature;
     setMutexBits(1);
     this.entityPathNavigate = creature.getNavigator();
     this.nearSpeed = 2.0D;
     this.farSpeed = this.nearSpeed * 2.0D;
   }
 
 
   
   public boolean shouldExecute() {
     if (this.isScared) {
       
       Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entityObj, 16, 10, Vec3.createVectorHelper(this.campfireX + 0.5D, this.campfireY, this.campfireZ + 0.5D));
       
       if (vec3 == null) {
         return false;
       }
       this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
       return (this.entityPathEntity == null) ? false : this.entityPathEntity.isDestinationSame(vec3);
     } 
     return false;
   }
 
   
   public boolean continueExecuting() {
     return (!this.entityObj.getNavigator().noPath() && this.scareTimer > 0);
   }
 
 
   
   public void resetTask() {}
 
   
   public void updateTask() {
     if (this.entityPathNavigate != null && (this.entityPathNavigate.getPath() == null || this.entityPathNavigate.getPath() != this.entityPathEntity))
     {
       this.entityPathNavigate.setPath(this.entityPathEntity, this.nearSpeed);
     }
     
     this.scareTimer--;
     if (this.scareTimer <= 0) {
       this.isScared = false;
     }
     if (this.entityObj.getDistanceSq(this.campfireX + 0.5D, this.campfireY, this.campfireZ + 0.5D) < 49.0D) {
       
       this.entityObj.getNavigator().setSpeed(this.nearSpeed);
     }
     else {
       
       this.entityObj.getNavigator().setSpeed(this.farSpeed);
     } 
   }
 
   
   public void startExecuting() {
     this.entityPathNavigate.setPath(this.entityPathEntity, this.nearSpeed);
   }
 }


