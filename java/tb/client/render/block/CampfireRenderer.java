package tb.client.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import thaumcraft.common.config.ConfigBlocks;


public class CampfireRenderer implements ISimpleBlockRenderingHandler {
	
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {}

	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		renderer.renderAllFaces = true;

		renderer.setOverrideBlockTexture(Blocks.cobblestone.getIcon(0, 0));

		Random rand = new Random((x + y + z));

		for (int i = 0; i < 6 + rand.nextInt(5); i++) {

			double dx = rand.nextDouble();
			double dy = 0.0D;
			double dz = rand.nextDouble();

			renderer.setRenderBounds(dx, dy, dz, dx + 0.1D, dy + 0.1D, dz + 0.1D);
			renderer.renderStandardBlock(Blocks.stone, x, y, z);
		} 

		renderer.setOverrideBlockTexture(Blocks.coal_block.getIcon(0, 0));

		renderer.setRenderBounds(0.25D, 0.0D, 0.25D, 0.75D, 0.06D, 0.75D);
		renderer.renderStandardBlock(Blocks.stone, x, y, z);

		renderer.setOverrideBlockTexture(ConfigBlocks.blockMagicalLog.getIcon(2, 0));
		double yIndex = 0.0D;
		if (world.getBlockMetadata(x, y, z) > 0) {
			for (int j = 0; j < 2; j++) {

				renderer.setRenderBounds(0.2D, 0.0D + yIndex + 0.1D * j, 0.1D, 0.3D, 0.11D + yIndex + 0.1D * j, 0.9D);
				renderer.renderStandardBlock(Blocks.stone, x, y, z);
				renderer.setRenderBounds(0.7D, 0.0D + yIndex + 0.1D * j, 0.1D, 0.8D, 0.11D + yIndex + 0.1D * j, 0.9D);
				renderer.renderStandardBlock(Blocks.stone, x, y, z);

				renderer.setRenderBounds(0.1D, 0.1D + yIndex + 0.1D * j, 0.2D, 0.9D, 0.21D + yIndex + 0.1D * j, 0.3D);
				renderer.renderStandardBlock(Blocks.stone, x, y, z);
				renderer.setRenderBounds(0.1D, 0.1D + yIndex + 0.1D * j, 0.7D, 0.9D, 0.21D + yIndex + 0.1D * j, 0.8D);
				renderer.renderStandardBlock(Blocks.stone, x, y, z);
				yIndex += 0.1D;
			} 
		}
		renderer.setOverrideBlockTexture(Blocks.gravel.getIcon(0, 0));
		renderer.setRenderBounds(0.05D, 0.0D, 0.05D, 0.95D, 0.03D, 0.95D);
		renderer.renderStandardBlock(Blocks.stone, x, y, z);

		if (world.getBlockMetadata(x, y, z) > 1) {

			renderer.setOverrideBlockTexture(Blocks.fire.getIcon(2, 0));
			Tessellator.instance.setBrightness(247);
			Tessellator.instance.setColorOpaque_I(16777215);
			renderer.drawCrossedSquares(Blocks.fire.getIcon(2, 0), x, y, z, 0.75F);
		} 

		renderer.renderAllFaces = false;
		renderer.clearOverrideBlockTexture();
		return true;
	}



	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}



	public int getRenderId() {
		return 1196798;
	}
}


