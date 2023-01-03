 package tb.client.render.block;
 
 import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
 import net.minecraft.block.Block;
 import net.minecraft.client.renderer.RenderBlocks;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.init.Blocks;
 import net.minecraft.world.IBlockAccess;
 import org.lwjgl.opengl.GL11;
 import tb.common.block.BlockRelocator;
 import tb.common.tile.TileRelocator;
 
 
 
 public class ThaumicRelocatorRenderer
   implements ISimpleBlockRenderingHandler
 {
   public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
     BlockRelocator rl = (BlockRelocator)block;
     
     int color = (metadata < 6) ? 16777079 : 30719;
     
     Tessellator tessellator = Tessellator.instance;
     
     GL11.glPushMatrix();
     
     GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
     GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
     tessellator.startDrawingQuads();
     tessellator.setNormal(0.0F, -1.0F, 0.0F);
     renderer.renderFaceYNeg(Blocks.glass, 0.0D, 0.0D, 0.0D, rl.icons[1]);
     tessellator.draw();
 
     
     tessellator.startDrawingQuads();
     tessellator.setNormal(0.0F, 1.0F, 0.0F);
     tessellator.setColorOpaque_I(color);
     tessellator.setBrightness(250);
     renderer.renderFaceYPos(Blocks.glass, 0.0D, 0.0D, 0.0D, rl.icons[3]);
     tessellator.draw();
 
     
     tessellator.startDrawingQuads();
     tessellator.setNormal(0.0F, 1.0F, 0.0F);
     renderer.renderFaceYPos(Blocks.glass, 0.0D, 0.0D, 0.0D, rl.icons[0]);
     tessellator.draw();
 
     
     tessellator.startDrawingQuads();
     tessellator.setColorOpaque_I(color);
     tessellator.setBrightness(250);
     tessellator.setNormal(0.0F, 0.0F, -1.0F);
     renderer.renderFaceZNeg(Blocks.glass, 0.0D, 0.0D, 0.0D, rl.icons[3]);
     tessellator.draw();
 
     
     tessellator.startDrawingQuads();
     tessellator.setNormal(0.0F, 0.0F, -1.0F);
     renderer.renderFaceZNeg(Blocks.glass, 0.0D, 0.0D, 0.0D, rl.icons[2]);
     tessellator.draw();
 
     
     tessellator.startDrawingQuads();
     tessellator.setColorOpaque_I(color);
     tessellator.setBrightness(250);
     tessellator.setNormal(0.0F, 0.0F, 1.0F);
     renderer.renderFaceZPos(Blocks.glass, 0.0D, 0.0D, 0.0D, rl.icons[3]);
     tessellator.draw();
 
     
     tessellator.startDrawingQuads();
     tessellator.setNormal(0.0F, 0.0F, 1.0F);
     renderer.renderFaceZPos(Blocks.glass, 0.0D, 0.0D, 0.0D, rl.icons[2]);
     tessellator.draw();
 
     
     tessellator.startDrawingQuads();
     tessellator.setColorOpaque_I(color);
     tessellator.setBrightness(250);
     tessellator.setNormal(-1.0F, 0.0F, 0.0F);
     renderer.renderFaceXNeg(Blocks.glass, 0.0D, 0.0D, 0.0D, rl.icons[3]);
     tessellator.draw();
 
     
     tessellator.startDrawingQuads();
     tessellator.setNormal(-1.0F, 0.0F, 0.0F);
     renderer.renderFaceXNeg(Blocks.glass, 0.0D, 0.0D, 0.0D, rl.icons[2]);
     tessellator.draw();
 
     
     tessellator.startDrawingQuads();
     tessellator.setColorOpaque_I(color);
     tessellator.setBrightness(250);
     tessellator.setNormal(1.0F, 0.0F, 0.0F);
     renderer.renderFaceXPos(Blocks.glass, 0.0D, 0.0D, 0.0D, rl.icons[3]);
     tessellator.draw();
 
     
     tessellator.startDrawingQuads();
     tessellator.setNormal(1.0F, 0.0F, 0.0F);
     renderer.renderFaceXPos(Blocks.glass, 0.0D, 0.0D, 0.0D, rl.icons[2]);
     tessellator.draw();
     
     GL11.glTranslatef(0.5F, 0.5F, 0.5F);
     
     GL11.glPopMatrix();
   }
 
 
 
   
   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
     int meta = world.getBlockMetadata(x, y, z);
     
     int color = (meta < 6) ? 16777079 : 30719;
     
     BlockRelocator rl = (BlockRelocator)block;
     
     TileRelocator tile = (TileRelocator)world.getTileEntity(x, y, z);
     
     if (tile.isBlockPowered()) {
       color = 3355443;
     }
     renderer.renderStandardBlock(block, x, y, z);
     
     Tessellator.instance.setColorOpaque_I(color);
     
     renderer.renderFaceYNeg(Blocks.glass, x, y + 0.01D, z, rl.icons[3]);
     
     renderer.renderFaceYPos(Blocks.glass, x, y - 0.01D, z, rl.icons[3]);
     
     renderer.renderFaceXNeg(Blocks.glass, x + 0.01D, y, z, rl.icons[3]);
     
     renderer.renderFaceXPos(Blocks.glass, x - 0.01D, y, z, rl.icons[3]);
     
     renderer.renderFaceZNeg(Blocks.glass, x, y, z + 0.01D, rl.icons[3]);
     
     renderer.renderFaceZPos(Blocks.glass, x, y, z - 0.01D, rl.icons[3]);
     
     return false;
   }
 
 
   
   public boolean shouldRender3DInInventory(int modelId) {
     return true;
   }
 
 
   
   public int getRenderId() {
     return 1196799;
   }
 }


