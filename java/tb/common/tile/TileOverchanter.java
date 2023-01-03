 package tb.common.tile;
 
 import DummyCore.Utils.Coord3D;
 import DummyCore.Utils.Lightning;
 import DummyCore.Utils.MathUtils;
 import DummyCore.Utils.MiscUtils;
 import java.util.Iterator;
 import java.util.LinkedHashMap;
 import java.util.List;
 import java.util.Set;
 import net.minecraft.enchantment.EnchantmentHelper;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.inventory.IInventory;
 import net.minecraft.item.ItemStack;
 import net.minecraft.nbt.NBTBase;
 import net.minecraft.nbt.NBTTagCompound;
 import net.minecraft.nbt.NBTTagList;
 import net.minecraft.network.NetworkManager;
 import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.util.AxisAlignedBB;
 import net.minecraft.util.DamageSource;
 import net.minecraft.world.World;
 import net.minecraftforge.common.util.ForgeDirection;
 import thaumcraft.api.aspects.Aspect;
 import thaumcraft.api.wands.IWandable;
 import thaumcraft.common.lib.events.EssentiaHandler;
 
 
 public class TileOverchanter
   extends TileEntity
   implements IInventory, IWandable
 {
   public ItemStack inventory;
   public int enchantingTime;
   public boolean xpAbsorbed;
   public boolean isEnchantingStarted;
   public int syncTimer;
   public Lightning renderedLightning;
   
   public int getSizeInventory() {
     return 1;
   }
 
 
   
   public void updateEntity() {
     if (this.syncTimer <= 0) {
       
       this.syncTimer = 100;
       NBTTagCompound tg = new NBTTagCompound();
       tg.setInteger("0", this.enchantingTime);
       tg.setBoolean("1", this.xpAbsorbed);
       tg.setBoolean("2", this.isEnchantingStarted);
       tg.setInteger("x", this.xCoord);
       tg.setInteger("y", this.yCoord);
       tg.setInteger("z", this.zCoord);
       MiscUtils.syncTileEntity(tg, 0);
     } else {
       this.syncTimer--;
     } 
     if (this.inventory == null) {
       
       this.isEnchantingStarted = false;
       this.xpAbsorbed = false;
       this.enchantingTime = 0;
       this.renderedLightning = null;
     
     }
     else if (this.isEnchantingStarted) {
       
       if (this.worldObj.getWorldTime() % 20L == 0L) {
         
         this.renderedLightning = new Lightning(this.worldObj.rand, new Coord3D(0.0F, 0.0F, 0.0F), new Coord3D(MathUtils.randomDouble(this.worldObj.rand) / 50.0D, MathUtils.randomDouble(this.worldObj.rand) / 50.0D, MathUtils.randomDouble(this.worldObj.rand) / 50.0D), 0.3F, new float[] { 1.0F, 0.0F, 1.0F });
         this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "thaumcraft:infuserstart", 1.0F, 1.0F);
         if (EssentiaHandler.drainEssentia(this, Aspect.MAGIC, ForgeDirection.UNKNOWN, 8, false)) {
           
           this.enchantingTime++;
           if (this.enchantingTime >= 16 && !this.xpAbsorbed) {
             
             List<EntityPlayer> players = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, (this.xCoord + 1), (this.yCoord + 1), (this.zCoord + 1)).expand(6.0D, 3.0D, 6.0D));
             if (!players.isEmpty())
             {
               for (int i = 0; i < players.size(); i++) {
                 
                 EntityPlayer p = players.get(i);
                 if (p.experienceLevel >= 30) {
                   
                   p.attackEntityFrom(DamageSource.magic, 8.0F);
                   this.worldObj.playSoundEffect(p.posX, p.posY, p.posZ, "thaumcraft:zap", 1.0F, 1.0F);
                   p.experienceLevel -= 30;
                   this.xpAbsorbed = true;
                   
                   break;
                 } 
               } 
             }
           } 
           if (this.xpAbsorbed && this.enchantingTime >= 32)
           {
             int enchId = findEnchantment(this.inventory);
             NBTTagList nbttaglist = this.inventory.getEnchantmentTagList();
             for (int i = 0; i < nbttaglist.tagCount(); i++) {
               
               NBTTagCompound tag = nbttaglist.getCompoundTagAt(i);
               if (tag != null && Integer.valueOf(tag.getShort("id")).intValue() == enchId) {
                 
                 tag.setShort("lvl", (short)(Integer.valueOf(tag.getShort("lvl")).intValue() + 1));
                 NBTTagCompound stackTag = MiscUtils.getStackTag(this.inventory);
                 if (!stackTag.hasKey("overchants")) {
                   
                   stackTag.setIntArray("overchants", new int[] { enchId });
                   break;
                 } 
                 int[] arrayInt = stackTag.getIntArray("overchants");
                 int[] newArrayInt = new int[arrayInt.length + 1];
                 for (int j = 0; j < arrayInt.length; j++)
                 {
                   newArrayInt[j] = arrayInt[j];
                 }
                 newArrayInt[newArrayInt.length - 1] = enchId;
                 
                 stackTag.setIntArray("overchants", newArrayInt);
                 
                 break;
               } 
             } 
             this.isEnchantingStarted = false;
             this.xpAbsorbed = false;
             this.enchantingTime = 0;
             this.renderedLightning = null;
             this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "thaumcraft:wand", 1.0F, 1.0F);
           }
         
         } else {
           
           this.enchantingTime--;
         } 
       } 
     } 
   }
 
 
   
   public boolean canStartEnchanting() {
     if (!this.isEnchantingStarted && 
       this.inventory != null)
     {
       if (this.inventory.getEnchantmentTagList() != null && this.inventory.getEnchantmentTagList().tagCount() > 0)
       {
         if (findEnchantment(this.inventory) != -1)
         {
           return true;
         }
       }
     }
     return false;
   }
 
 
   
   public int findEnchantment(ItemStack enchanted) {
     NBTTagCompound stackTag = MiscUtils.getStackTag(this.inventory);
     LinkedHashMap<Integer, Integer> ench = (LinkedHashMap<Integer, Integer>)EnchantmentHelper.getEnchantments(enchanted);
     Set<Integer> keys = ench.keySet();
     Iterator<Integer> $i = keys.iterator();
     
     while ($i.hasNext()) {
       
       int i = ((Integer)$i.next()).intValue();
       if (!stackTag.hasKey("overchants"))
       {
         return i;
       }
       
       int[] overchants = stackTag.getIntArray("overchants");
       if (MathUtils.arrayContains(overchants, i)) {
         continue;
       }
       return i;
     } 
 
     
     return -1;
   }
 
   
   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
     this.enchantingTime = pkt.func_148857_g().getInteger("0");
     this.xpAbsorbed = pkt.func_148857_g().getBoolean("1");
     this.isEnchantingStarted = pkt.func_148857_g().getBoolean("2");
   }
 
   
   public ItemStack getStackInSlot(int slot) {
     return this.inventory;
   }
 
   
   public ItemStack decrStackSize(int slot, int num) {
     if (this.inventory != null) {
 
 
       
       if (this.inventory.stackSize <= num) {
         
         ItemStack itemStack = this.inventory;
         this.inventory = null;
         markDirty();
         return itemStack;
       } 
 
       
       ItemStack itemstack = this.inventory.splitStack(num);
       
       if (this.inventory.stackSize == 0)
       {
         this.inventory = null;
       }
       
       markDirty();
       return itemstack;
     } 
 
 
     
     return null;
   }
 
 
   
   public ItemStack getStackInSlotOnClosing(int slot) {
     return this.inventory;
   }
 
   
   public void setInventorySlotContents(int slot, ItemStack stk) {
     this.inventory = stk;
   }
 
   
   public String getInventoryName() {
     return "tb.overchanter";
   }
 
   
   public boolean hasCustomInventoryName() {
     return false;
   }
 
   
   public int getInventoryStackLimit() {
     return 1;
   }
 
   
   public boolean isUseableByPlayer(EntityPlayer player) {
     return (player.dimension == this.worldObj.provider.dimensionId && this.worldObj.blockExists(this.xCoord, this.yCoord, this.zCoord));
   }
 
 
   
   public void openInventory() {}
 
 
   
   public void closeInventory() {}
 
 
   
   public boolean isItemValidForSlot(int slot, ItemStack stk) {
     return (stk.hasTagCompound() && stk.getEnchantmentTagList() != null);
   }
 
   
   public void readFromNBT(NBTTagCompound tag) {
     super.readFromNBT(tag);
     
     this.enchantingTime = tag.getInteger("enchTime");
     this.xpAbsorbed = tag.getBoolean("xpAbsorbed");
     this.isEnchantingStarted = tag.getBoolean("enchStarted");
     
     if (tag.hasKey("itm")) {
       this.inventory = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("itm"));
     }
   }
   
   public void writeToNBT(NBTTagCompound tag) {
     super.writeToNBT(tag);
     
     tag.setInteger("enchTime", this.enchantingTime);
     tag.setBoolean("xpAbsorbed", this.xpAbsorbed);
     tag.setBoolean("enchStarted", this.isEnchantingStarted);
     
     if (this.inventory != null) {
       
       NBTTagCompound t = new NBTTagCompound();
       this.inventory.writeToNBT(t);
       tag.setTag("itm", (NBTBase)t);
     } 
   }
 
 
 
   
   public int onWandRightClick(World world, ItemStack wandstack, EntityPlayer player, int x, int y, int z, int side, int md) {
     if (canStartEnchanting()) {
       
       this.isEnchantingStarted = true;
       player.swingItem();
       this.syncTimer = 0;
       this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "thaumcraft:craftstart", 0.5F, 1.0F);
       return 1;
     } 
     return -1;
   }
 
 
   
   public ItemStack onWandRightClick(World world, ItemStack wandstack, EntityPlayer player) {
     return wandstack;
   }
   
   public void onUsingWandTick(ItemStack wandstack, EntityPlayer player, int count) {}
   
   public void onWandStoppedUsing(ItemStack wandstack, World world, EntityPlayer player, int count) {}
 }


