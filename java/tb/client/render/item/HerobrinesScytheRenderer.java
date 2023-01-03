 package tb.client.render.item;
 
 import net.minecraft.client.renderer.ItemRenderer;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.entity.RenderItem;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.IIcon;
 import net.minecraftforge.client.IItemRenderer;
 import org.lwjgl.opengl.GL11;
 
 public class HerobrinesScytheRenderer
   implements IItemRenderer
 {
   protected static RenderItem itemRender = new RenderItem();
 
   
   public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
     return true;
   }
 
 
   
   public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
     return (type != IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON && type != IItemRenderer.ItemRenderType.ENTITY);
   }
 
   
   public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
     if (item != null) {
       
       GL11.glPushMatrix();
       
       if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
         
         GL11.glScaled(3.0D, 3.0D, 3.0D);
         GL11.glTranslated(0.0D, -0.4D, 0.3D);
         GL11.glRotated(15.0D, 0.0D, 1.0D, 0.0D);
         GL11.glRotated(15.0D, 0.0D, 0.0D, 1.0D);
         IIcon icon = item.getIconIndex();
         ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.03125F);
       } 
       
       if (type == IItemRenderer.ItemRenderType.INVENTORY) {
         
         GL11.glDisable(3008);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         
         GL11.glTranslatef(0.7F, -0.9F, 1.0F);
         GL11.glScaled(2.0D, 2.0D, 2.0D);
         GL11.glRotated(60.0D, 0.0D, 1.0D, 1.0D);
         IIcon icon = item.getIconIndex();
         ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
         GL11.glDisable(3042);
         GL11.glEnable(3008);
       } 
       
       if (type == IItemRenderer.ItemRenderType.ENTITY) {
         
         GL11.glScaled(4.0D, 4.0D, 4.0D);
         GL11.glRotated(90.0D, 1.0D, 0.0D, 0.0D);
         GL11.glRotated(35.0D, 0.0D, 0.0D, 1.0D);
         GL11.glTranslated(-0.5D, -0.5D, 0.05D);
         IIcon icon = item.getIconIndex();
         ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
       } 
       
       if (type == IItemRenderer.ItemRenderType.EQUIPPED) {
         
         GL11.glScaled(6.0D, 6.0D, 6.0D);
         GL11.glRotated(135.0D, 0.0D, 1.0D, 0.0D);
         GL11.glRotatef(-35.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslated(-0.7D, -0.4D, 0.01D);
         IIcon icon = item.getIconIndex();
         ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0225F);
       } 
       
       GL11.glPopMatrix();
     } 
   }
 }


