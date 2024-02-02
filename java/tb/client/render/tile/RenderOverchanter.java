 package tb.client.render.tile;
 
 import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
 import net.minecraft.tileentity.TileEntity;
 import org.lwjgl.opengl.GL11;
 import tb.common.tile.TileOverchanter;
import tb.utils.DummySteele;
 
 
 
 public class RenderOverchanter
   extends TileEntitySpecialRenderer
 {
   public void renderTileEntityAt(TileEntity tile, double screenX, double screenY, double screenZ, float partialTicks) {
     TileOverchanter overchanter = (TileOverchanter)tile;
     
     if (overchanter.inventory != null) {
       
       GL11.glPushMatrix();
       
       DummySteele.renderItemStack_Full(overchanter.inventory, 0.0D, 0.0D, 0.0D, screenX, screenY, screenZ, (float)(tile.getWorldObj().getWorldTime() % 360L), 0.0F, 1.0F, 1.0F, 1.0F, 0.5F, 0.9F, 0.5F, false);
       
       GL11.glPopMatrix();
     } 
     
     if (overchanter.renderedLightning != null) {
       
       GL11.glPushMatrix();
       
       overchanter.renderedLightning.render(screenX + 0.5D, screenY + 1.0D, screenZ + 0.5D, partialTicks);
       
       GL11.glPopMatrix();
     } 
   }
 }


