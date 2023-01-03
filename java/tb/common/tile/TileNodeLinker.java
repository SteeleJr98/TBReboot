 package tb.common.tile;
 
 import DummyCore.Utils.Coord3D;
 import DummyCore.Utils.MiscUtils;
 import cpw.mods.fml.common.network.NetworkRegistry;
 import cpw.mods.fml.common.network.simpleimpl.IMessage;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.List;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.ItemStack;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.network.NetworkManager;
 import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.util.AxisAlignedBB;
 import net.minecraft.util.ChatComponentText;
 import net.minecraft.util.DamageSource;
 import net.minecraft.util.IChatComponent;
 import net.minecraft.util.StatCollector;
 import net.minecraft.world.World;
 import net.minecraftforge.common.util.ForgeDirection;
 import thaumcraft.api.aspects.Aspect;
 import thaumcraft.api.aspects.AspectList;
 import thaumcraft.api.wands.IWandable;
 import thaumcraft.codechicken.lib.math.MathHelper;
 import thaumcraft.common.Thaumcraft;
 import thaumcraft.common.config.ConfigBlocks;
 import thaumcraft.common.entities.monster.EntityWisp;
 import thaumcraft.common.lib.network.PacketHandler;
 import thaumcraft.common.lib.network.fx.PacketFXBlockZap;
 import thaumcraft.common.tiles.TileNode;
 
 public class TileNodeLinker
   extends TileEntity implements IWandable {
   public Coord3D linkCoord;
   public int syncTimer;
   public int instability;
   
   public void updateEntity() {
     if (this.syncTimer <= 0) {
       
       this.syncTimer = 100;
       NBTTagCompound tg = new NBTTagCompound();
       if (this.linkCoord != null) {
         
         tg.setFloat("coordsX", this.linkCoord.x);
         tg.setFloat("coordsY", this.linkCoord.y);
         tg.setFloat("coordsZ", this.linkCoord.z);
       } 
       tg.setInteger("x", this.xCoord);
       tg.setInteger("y", this.yCoord);
       tg.setInteger("z", this.zCoord);
       MiscUtils.syncTileEntity(tg, 0);
     } else {
       this.syncTimer--;
     } 
     if (this.linkCoord != null)
     
     { int x = MathHelper.floor_double(this.linkCoord.x);
       int y = MathHelper.floor_double(this.linkCoord.y);
       int z = MathHelper.floor_double(this.linkCoord.z);
       
       if (this.worldObj.getTileEntity(x, y, z) instanceof TileNodeLinker && ((TileNodeLinker)TileNodeLinker.class.cast(this.worldObj.getTileEntity(x, y, z))).linkCoord == null) {
         
         if (this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == ConfigBlocks.blockAiry && this.worldObj.getBlock(x, y - 1, z) == ConfigBlocks.blockAiry)
         {
           if (this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord) instanceof thaumcraft.api.nodes.INode && this.worldObj.getTileEntity(x, y - 1, z) instanceof thaumcraft.api.nodes.INode)
           {
             if (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == 0 && this.worldObj.getBlockMetadata(x, y - 1, z) == 0) {
               
               double sX = this.linkCoord.x + 0.5D;
               double sY = this.linkCoord.y + 0.825D;
               double sZ = this.linkCoord.z + 0.5D;
               
               double eX = this.xCoord + 0.5D;
               double eY = this.yCoord + 0.825D;
               double eZ = this.zCoord + 0.5D;
               
               if (this.worldObj.isRemote) {
                 Thaumcraft.proxy.beam(this.worldObj, sX, sY, sZ, eX, eY, eZ, 1, 16777215, false, 1.0F, 1);
               }
               if (this.worldObj.isRemote) {
                 Thaumcraft.proxy.nodeBolt(this.worldObj, (float)sX, (float)(sY - 1.2999999523162842D), (float)sZ, (float)sX, (float)sY - 0.6F, (float)sZ);
               }
               if (this.worldObj.isRemote) {
                 Thaumcraft.proxy.nodeBolt(this.worldObj, (float)eX, (float)eY - 0.6F, (float)eZ, (float)eX, (float)eY - 1.3F, (float)eZ);
               }
               if (this.syncTimer % 10 == 0 && !this.worldObj.isRemote)
               {
                 TileNode reciever = (TileNode)this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
                 TileNode transmitter = (TileNode)this.worldObj.getTileEntity(x, y - 1, z);
                 boolean isPushPerm = (this.worldObj.rand.nextDouble() < 0.3D) ? true : (!(transmitter.getAspects().visSize() > 0));
                 AspectList pullFrom = isPushPerm ? transmitter.getAspectsBase() : transmitter.getAspects();
                 AspectList pushTo = isPushPerm ? reciever.getAspectsBase() : reciever.getAspects();
                 if (pullFrom.visSize() <= 0) {
                   
                   this.worldObj.setBlockToAir(x, y - 1, z);
                   
                   return;
                 } 
                 Aspect randomAspect = pullFrom.getAspects()[this.worldObj.rand.nextInt((pullFrom.getAspects()).length)];
                 if (isPushPerm) {
                   
                   if (this.worldObj.rand.nextDouble() <= 0.7D) {
                     
                     if (instabilityCheck()) {
                       
                       pushTo.add(randomAspect, 1);
                       reciever.getAspects().add(randomAspect, 1);
                     } 
                     pullFrom.reduce(randomAspect, 1);
                   } else {
                     
                     pullFrom.reduce(randomAspect, 1);
                   } 
                   this.instability++;
                 } else {
                   
                   if (pushTo.getAmount(randomAspect) > 0 && pushTo.getAmount(randomAspect) <= reciever.getAspectsBase().getAmount(randomAspect)) {
                     
                     if (instabilityCheck()) {
                       pushTo.add(randomAspect, 1);
                     }
                     pullFrom.reduce(randomAspect, 1);
                   } else {
                     
                     pullFrom.reduce(randomAspect, 1);
                   } 
                   if (this.worldObj.rand.nextDouble() <= 0.33D) {
                     this.instability++;
                   }
                 } 
                 transmitter.markDirty();
                 reciever.markDirty();
                 this.worldObj.markBlockForUpdate(x, y - 1, z);
                 this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord - 1, this.zCoord);
               }
             
             } 
           }
         }
       } else {
         
         this.linkCoord = null;
       }  }
     else { this.instability = 0; }
   
   }
   
   public boolean instabilityCheck() {
     if (this.worldObj.rand.nextInt(50) <= this.instability) {
       
       int rnd = this.worldObj.rand.nextInt(this.instability);
       if (rnd == 49) {
         
         this.instability -= explodeTransmitter();
       
       }
       else if (rnd >= 45) {
         
         this.instability -= harmTransmitter();
       
       }
       else if (rnd >= 31) {
         
         this.instability -= wisp();
       
       }
       else if (rnd >= 20) {
         
         this.instability -= zap();
       
       }
       else if (rnd >= 10) {
         
         this.instability -= fluxNode();
       } else {
         
         this.instability -= fluxTransmission();
       } 
 
 
 
       
       return false;
     } 
     return true;
   }
 
   
   public int harmTransmitter() {
     int x = MathHelper.floor_double(this.linkCoord.x);
     int y = MathHelper.floor_double(this.linkCoord.y);
     int z = MathHelper.floor_double(this.linkCoord.z);
     boolean reducePerm = this.worldObj.rand.nextBoolean();
     TileNode transmitter = (TileNode)this.worldObj.getTileEntity(x, y - 1, z);
     AspectList reduced = reducePerm ? transmitter.getAspectsBase() : transmitter.getAspects();
     
     if (reduced.visSize() <= 0 || reduced.size() <= 0) {
       return 0;
     }
     Aspect rnd = reduced.getAspects()[this.worldObj.rand.nextInt(reduced.size())];
     int reduceBy = Math.min(4 + this.worldObj.rand.nextInt(16), reduced.getAmount(rnd));
     
     reduced.reduce(rnd, reduceBy);
     transmitter.markDirty();
     this.worldObj.markBlockForUpdate(x, y - 1, z);
     
     return reducePerm ? 25 : 6;
   }
 
 
   
   public int zap() {
     boolean side = this.worldObj.rand.nextBoolean();
     int x = side ? MathHelper.floor_double(this.linkCoord.x) : this.xCoord;
     int y = side ? MathHelper.floor_double(this.linkCoord.y) : this.yCoord;
     int z = side ? MathHelper.floor_double(this.linkCoord.z) : this.zCoord;
     List<EntityPlayer> players = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox((x - 5), (y - 6), (z - 5), (x + 5), (y + 4), (z + 5)));
     if (!players.isEmpty()) {
       
       EntityPlayer target = players.get(this.worldObj.rand.nextInt(players.size()));
       PacketHandler.INSTANCE.sendToAllAround((IMessage)new PacketFXBlockZap(x + 0.5F, y - 0.5F, z + 0.5F, (float)target.posX, (float)target.posY + target.height / 2.0F, (float)target.posZ), new NetworkRegistry.TargetPoint(this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 32.0D));
       target.attackEntityFrom(DamageSource.magic, (4 + this.worldObj.rand.nextInt(4)));
       return 5;
     } 
     return 0;
   }
 
   
   public int wisp() {
     boolean side = this.worldObj.rand.nextBoolean();
     int x = side ? MathHelper.floor_double(this.linkCoord.x) : this.xCoord;
     int y = side ? MathHelper.floor_double(this.linkCoord.y) : this.yCoord;
     int z = side ? MathHelper.floor_double(this.linkCoord.z) : this.zCoord;
     ArrayList<Aspect> aspects = new ArrayList<Aspect>();
     Collection<Aspect> pa = Aspect.aspects.values();
     for (Aspect aspect : pa)
     {
       aspects.add(aspect);
     }
     
     EntityWisp wisp = new EntityWisp(this.worldObj);
     wisp.setPositionAndRotation(x + 0.5D, y - 0.5D, z + 0.5D, 0.0F, 0.0F);
     wisp.setType(((Aspect)aspects.get(this.worldObj.rand.nextInt(aspects.size()))).getTag());
     this.worldObj.spawnEntityInWorld((Entity)wisp);
     
     return 9;
   }
 
   
   public int fluxNode() {
     boolean side = this.worldObj.rand.nextBoolean();
     int x = side ? MathHelper.floor_double(this.linkCoord.x) : this.xCoord;
     int y = side ? MathHelper.floor_double(this.linkCoord.y) : this.yCoord;
     int z = side ? MathHelper.floor_double(this.linkCoord.z) : this.zCoord;
     int fluxMeta = this.worldObj.rand.nextInt(8);
     int pos = 2 + this.worldObj.rand.nextInt(4);
     ForgeDirection off = ForgeDirection.VALID_DIRECTIONS[pos];
     this.worldObj.setBlock(x + off.offsetX, y - 1, z + off.offsetZ, ConfigBlocks.blockFluxGoo, fluxMeta, 3);
     return 5;
   }
 
   
   public int fluxTransmission() {
     boolean side = this.worldObj.rand.nextBoolean();
     int x = side ? MathHelper.floor_double(this.linkCoord.x) : this.xCoord;
     int y = side ? MathHelper.floor_double(this.linkCoord.y) : this.yCoord;
     int z = side ? MathHelper.floor_double(this.linkCoord.z) : this.zCoord;
     int fluxMeta = this.worldObj.rand.nextInt(8);
     this.worldObj.setBlock(x, y + 1, z, ConfigBlocks.blockFluxGas, fluxMeta, 3);
     return 3;
   }
 
   
   public int explodeTransmitter() {
     int x = MathHelper.floor_double(this.linkCoord.x);
     int y = MathHelper.floor_double(this.linkCoord.y);
     int z = MathHelper.floor_double(this.linkCoord.z);
     this.worldObj.setBlockToAir(x, y - 1, z);
     this.worldObj.createExplosion(null, x + 0.5D, y - 0.5D, z + 0.5D, 2.0F, true);
     return this.instability;
   }
 
   
   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
     if (pkt.func_148857_g().hasKey("coordsX")) {
       
       this.linkCoord = new Coord3D(pkt.func_148857_g().getFloat("coordsX"), pkt.func_148857_g().getFloat("coordsY"), pkt.func_148857_g().getFloat("coordsZ"));
     } else {
       this.linkCoord = null;
     } 
   }
   
   public void readFromNBT(NBTTagCompound tag) {
     super.readFromNBT(tag);
     if (tag.hasKey("coordsX"))
     {
       this.linkCoord = new Coord3D(tag.getFloat("coordsX"), tag.getFloat("coordsY"), tag.getFloat("coordsZ"));
     }
     this.instability = tag.getInteger("instability");
   }
 
   
   public void writeToNBT(NBTTagCompound tag) {
     super.writeToNBT(tag);
     if (this.linkCoord != null) {
       
       tag.setFloat("coordsX", this.linkCoord.x);
       tag.setFloat("coordsY", this.linkCoord.y);
       tag.setFloat("coordsZ", this.linkCoord.z);
     } 
     tag.setInteger("instability", this.instability);
   }
 
 
 
 
   
   public int onWandRightClick(World paramWorld, ItemStack paramItemStack, EntityPlayer paramEntityPlayer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
     return 0;
   }
 
 
   
   public ItemStack onWandRightClick(World paramWorld, ItemStack paramItemStack, EntityPlayer paramEntityPlayer) {
     paramEntityPlayer.swingItem();
     if (!paramItemStack.hasTagCompound()) {
       paramItemStack.setTagCompound(new NBTTagCompound());
     }
     if (paramItemStack.getTagCompound().hasKey("linkCoordX")) {
       
       float x = paramItemStack.getTagCompound().getFloat("linkCoordX");
       float y = paramItemStack.getTagCompound().getFloat("linkCoordY");
       float z = paramItemStack.getTagCompound().getFloat("linkCoordZ");
       if (this.worldObj.getTileEntity(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z)) instanceof TileNodeLinker) {
         
         TileNodeLinker tile = (TileNodeLinker)this.worldObj.getTileEntity(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
         tile.linkCoord = new Coord3D(this.xCoord, this.yCoord, this.zCoord);
         paramItemStack.getTagCompound().removeTag("linkCoordX");
         paramItemStack.getTagCompound().removeTag("linkCoordY");
         paramItemStack.getTagCompound().removeTag("linkCoordZ");
         if (paramWorld.isRemote) {
           paramEntityPlayer.addChatMessage((IChatComponent)new ChatComponentText(StatCollector.translateToLocal("tb.txt.linkEstabilished")));
         }
       } 
     } else {
       paramItemStack.getTagCompound().setFloat("linkCoordX", this.xCoord);
       paramItemStack.getTagCompound().setFloat("linkCoordY", this.yCoord);
       paramItemStack.getTagCompound().setFloat("linkCoordZ", this.zCoord);
       if (paramWorld.isRemote) {
         paramEntityPlayer.addChatMessage((IChatComponent)new ChatComponentText(StatCollector.translateToLocal("tb.txt.linkStarted")));
       }
     } 
     return paramItemStack;
   }
   
   public void onUsingWandTick(ItemStack paramItemStack, EntityPlayer paramEntityPlayer, int paramInt) {}
   
   public void onWandStoppedUsing(ItemStack paramItemStack, World paramWorld, EntityPlayer paramEntityPlayer, int paramInt) {}
 }


