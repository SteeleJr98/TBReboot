 package tb.network.proxy;
 
 import cpw.mods.fml.common.network.IGuiHandler;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.world.World;
 import tb.common.inventory.ContainerOverchanter;
 import tb.common.inventory.ContainerRevolver;
 import tb.common.inventory.ContainerThaumicAnvil;
 import tb.common.inventory.ContainerVoidAnvil;
import thaumcraft.common.container.ContainerArcaneWorkbench;
import thaumcraft.common.tiles.TileArcaneWorkbench;
import tb.common.inventory.FociCrafterContainer;
import tb.common.tile.FakeWorkbenchTile;
 
 
 
 public class TBServer
   implements IGuiHandler
 {
   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
     if (ID == 4331810) {
       
       TileEntity tile = world.getTileEntity(x, y, z);
       
       if (tile != null)
       {
         if (tile instanceof tb.common.tile.TileOverchanter)
         {
           return new ContainerOverchanter(player.inventory, tile);
         }
       }
     }
     else {
       
       if (ID == 4331809) {
         return new ContainerThaumicAnvil(player.inventory, world, x, y, z, player);
       }
       if (ID == 4331808) {
         return new ContainerVoidAnvil(player.inventory, world, x, y, z, player);
       }
       if (ID == 4331801) {
         return new ContainerRevolver(player.inventory, world, x, y, z);
       }
       if (ID == 4331811) {
    	   //return new FociCrafterContainer(player.inventory, world, x, y, z);
    	   FakeWorkbenchTile tempTile = new FakeWorkbenchTile();
    	   tempTile.xCoord = x;
    	   tempTile.yCoord = y;
    	   tempTile.zCoord = z;
    	   return new FociCrafterContainer(player.inventory, tempTile);
       }
     } 
     return null;
   }
 
 
   
   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
     return null;
   }
 
 
 
   
   public void registerRenderInformation() {}
 
 
 
   
   public void lightning(World world, double sx, double sy, double sz, double ex, double ey, double ez, int dur, float curve, int speed, int type) {}
 
 
   
   public void sparkle(World w, double x, double y, double z, double dx, double dy, double dz, int color, float scale) {}
 
 
   
   public World clientWorld() {
     return null;
   }
 }


