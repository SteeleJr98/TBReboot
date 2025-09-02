package tb.client.render.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import tb.common.item.ItemNodeFoci;
import tb.utils.DummySteele;

public class NodeFociRenderer implements IItemRenderer {
	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("thaumicbases", "models/nodeManipulator/foci.obj"));


	public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
		return true;
	}


	public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
		return true;
	}



	public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();

		GL11.glScalef(0.25F, 0.25F, 0.25F);

		if (type == IItemRenderer.ItemRenderType.INVENTORY) {
			GL11.glTranslated(0.0D, -1.0D, 0.0D);
		}
		DummySteele.bindTexture("thaumicbases", "textures/blocks/nodeManipulator/foci_" + ItemNodeFoci.names[item.getItemDamage()] + ".png");
		model.renderAll();

		GL11.glPopMatrix();
	}
}


