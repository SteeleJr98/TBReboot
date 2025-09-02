package tb.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import tb.common.item.ItemNodeFoci;
import tb.utils.DummySteele;

public class RenderNodeManipulator extends TileEntitySpecialRenderer {
	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("thaumicbases", "models/nodeManipulator/nodeManipulator.obj"));
	public static final ResourceLocation genIcon = new ResourceLocation("thaumicbases", "textures/blocks/nodeManipulator/baseUVMap.png");



	public void renderTileEntityAt(TileEntity tile, double screenX, double screenY, double screenZ, float partialTicks) {
		GL11.glPushMatrix();
		GL11.glTranslated(screenX + 0.5D, screenY + 1.0D, screenZ + 0.5D);
		GL11.glRotated(180.0D, 1.0D, 0.0D, 0.0D);
		GL11.glScaled(0.5D, 0.5D, 0.5D);

		(Minecraft.getMinecraft()).renderEngine.bindTexture(genIcon);
		model.renderPart("base_Cube.001");
		model.renderPart("handle_0_Cube.002");
		model.renderPart("handle_1_Cube.002");
		model.renderPart("handle_2_Cube.002");
		model.renderPart("handle_3_Cube.002");

		int meta = tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);
		if (meta != 0) {

			DummySteele.bindTexture("thaumicbases", "textures/blocks/nodeManipulator/foci_" + ItemNodeFoci.names[meta - 1] + ".png");
			model.renderPart("foci_Cube.003");
		} 

		GL11.glPopMatrix();
	}
}


