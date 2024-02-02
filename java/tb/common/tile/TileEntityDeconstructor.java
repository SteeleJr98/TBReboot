 package tb.common.tile;
 
 import java.util.List;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.item.EntityItem;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.entity.player.EntityPlayerMP;
 import net.minecraft.item.ItemStack;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.network.NetworkManager;
 import net.minecraft.network.Packet;
 import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
 import net.minecraft.server.MinecraftServer;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.util.AxisAlignedBB;
 import net.minecraft.util.DamageSource;
import tb.utils.DummySteele;
import tb.utils.TBUtils;
 import thaumcraft.api.aspects.Aspect;
 import thaumcraft.api.aspects.AspectList;
 import thaumcraft.codechicken.lib.math.MathHelper;
 import thaumcraft.common.Thaumcraft;
 import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
 import thaumcraft.common.lib.research.ResearchManager;
 import thaumcraft.common.lib.research.ScanManager;
 
 public class TileEntityDeconstructor
   extends TileEntity
 {
   public int tickTime;
   public String placerName = "no placer";
   public boolean hasAir;
   public boolean hasFire;
   public boolean hasWater;
   
   public void readFromNBT(NBTTagCompound tag) {
     super.readFromNBT(tag);
     
     this.hasAir = tag.getBoolean("b0");
     this.hasFire = tag.getBoolean("b1");
     this.hasWater = tag.getBoolean("b2");
     this.hasEarth = tag.getBoolean("b3");
     this.hasOrdo = tag.getBoolean("b4");
     this.hasEntropy = tag.getBoolean("b5");
     this.placerName = tag.getString("placer");
   }
   public boolean hasEarth; public boolean hasOrdo; public boolean hasEntropy;
   
   public void writeToNBT(NBTTagCompound tag) {
     super.writeToNBT(tag);
     
     tag.setBoolean("b0", this.hasAir);
     tag.setBoolean("b1", this.hasFire);
     tag.setBoolean("b2", this.hasWater);
     tag.setBoolean("b3", this.hasEarth);
     tag.setBoolean("b4", this.hasOrdo);
     tag.setBoolean("b5", this.hasEntropy);
     tag.setString("placer", this.placerName);
   }
 
 
   
   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
     if (net.getNetHandler() instanceof net.minecraft.network.play.INetHandlerPlayClient && 
       pkt.func_148853_f() == -10) {
       readFromNBT(pkt.func_148857_g());
     }
   }
 
   
   public Packet getDescriptionPacket() {
     NBTTagCompound nbttagcompound = new NBTTagCompound();
     writeToNBT(nbttagcompound);
     return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, -10, nbttagcompound);
   }
 
 
   
   public void updateEntity() {
     if (!this.worldObj.isRemote && this.tickTime == 0) {
       DummySteele.sendPacketToAllAround(this.worldObj, getDescriptionPacket(), this.xCoord, this.yCoord, this.zCoord, this.worldObj.provider.dimensionId, 16.0D); //Fix in dummysteele
     }
     int additionalStability = 0;
     
     if (this.hasAir)
       additionalStability++; 
     if (this.hasWater)
       additionalStability++; 
     if (this.hasFire)
       additionalStability++; 
     if (this.hasEarth)
       additionalStability++; 
     if (this.hasOrdo)
       additionalStability++; 
     if (this.hasEntropy) {
       additionalStability++;
     }
     if (this.placerName == null || this.placerName.isEmpty() || this.placerName.contains("no placer")) {
       return;
     }
     List<Entity> entities = this.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, (this.xCoord + 1), (this.yCoord + 1), (this.zCoord + 1)));
     if (!entities.isEmpty()) {
       
       Entity e = entities.get(0);
       if (e != null && !e.isDead)
       {
         if (e instanceof EntityItem) {
           
           if (additionalStability >= 6) {
             
             EntityItem itm = (EntityItem)e;
             ItemStack stk = itm.getEntityItem();
             if (stk != null) {
               
               AspectList aspectsCompound = ThaumcraftCraftingManager.getObjectTags(stk);
               aspectsCompound = ThaumcraftCraftingManager.getBonusTags(stk, aspectsCompound);
               if (aspectsCompound != null && aspectsCompound.size() > 0) {
                 
                 this.tickTime++;
                 if (this.tickTime == 40) {
                   
                   this.tickTime = 0;
                   
                   stk.stackSize--;
                   if (stk.stackSize <= 0) {
                     itm.setDead();
                   }
                   AspectList primals = ResearchManager.reduceToPrimals(aspectsCompound);
                   Aspect a = null;
                   if (this.worldObj.rand.nextInt(40) < primals.visSize()) {
                     a = primals.getAspects()[this.worldObj.rand.nextInt((primals.getAspects()).length)];
                   }
                   if (!this.worldObj.isRemote && a != null) {
                     
                     EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(this.placerName);
                     if (player != null) {
                       
                       double distance = player.getDistance(this.xCoord + 0.5D, this.yCoord, this.zCoord + 0.5D);
                       if (additionalStability < 6 && 
                         this.worldObj.rand.nextInt(MathHelper.floor_double(Math.max(1.0D, (128 + additionalStability * 10) - distance))) == 0) {
                         TBUtils.addWarpToPlayer((EntityPlayer)EntityPlayerMP.class.cast(e), 1, 0);
                       }
                       TBUtils.addAspectToKnowledgePool((EntityPlayer)player, a, (short)1);
                     } 
                   } 
                 } 
               } 
               Thaumcraft.proxy.blockRunes(this.worldObj, this.xCoord, this.yCoord + DummySteele.randomDouble(this.worldObj.rand) * 0.5D, this.zCoord, 1.0F, 0.5F, 0.5F, 8, 0.0F);
               
               return;
             } 
           } 
         } else {
           AspectList aspectsCompound = ScanManager.generateEntityAspects(e);
           if (aspectsCompound != null && aspectsCompound.size() > 0) {
             
             this.tickTime++;
             
             this.tickTime += MathHelper.floor_double((additionalStability / 2));
             
             if (this.tickTime == 40) {
               
               this.tickTime = 0;
               e.attackEntityFrom(DamageSource.outOfWorld, 1.0F);
               
               if (e instanceof EntityPlayerMP)
               {
                 TBUtils.addWarpToPlayer((EntityPlayer)EntityPlayerMP.class.cast(e), 1, 0);
               }
               
               Aspect a = aspectsCompound.getAspects()[this.worldObj.rand.nextInt(aspectsCompound.size())];
               
               if (!this.worldObj.isRemote) {
                 
                 EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(this.placerName);
                 if (player != null) {
                   
                   double distance = player.getDistance(this.xCoord + 0.5D, this.yCoord, this.zCoord + 0.5D);
                   if (additionalStability < 6 && 
                     this.worldObj.rand.nextInt(MathHelper.floor_double(Math.max(1.0D, (128 + additionalStability * 10) - distance))) == 0) {
                     TBUtils.addWarpToPlayer((EntityPlayer)EntityPlayerMP.class.cast(e), 1, 0);
                   }
                   TBUtils.addAspectToKnowledgePool((EntityPlayer)player, a, (short)1);
                 } 
               } 
             } 
             Thaumcraft.proxy.blockRunes(this.worldObj, this.xCoord, this.yCoord + DummySteele.randomDouble(this.worldObj.rand) * 0.5D, this.zCoord, 1.0F, 0.5F, 0.5F, 8, 0.0F);
             
             return;
           } 
         } 
       }
     } 
     this.tickTime = 0;
   }
 }


