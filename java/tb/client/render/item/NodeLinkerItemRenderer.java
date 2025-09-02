package tb.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public class NodeLinkerItemRenderer implements IItemRenderer {
	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("thaumicbases", "models/nodeLinker/nodeLinker.obj"));
	public static final ResourceLocation genIcon = new ResourceLocation("thaumicbases", "textures/blocks/nodeLinker/nodeLinkerUV.png");


	public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
		return true;
	}



	public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
		return true;
	}


	public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();

		GL11.glScaled(0.5D, 0.5D, 0.5D);
		GL11.glTranslated(0.0D, -0.3D, 0.0D);

		(Minecraft.getMinecraft()).renderEngine.bindTexture(genIcon);
		model.renderAll();

		GL11.glPopMatrix();
	}
}


