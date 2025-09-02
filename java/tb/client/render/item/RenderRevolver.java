package tb.client.render.item;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import tb.api.RevolverUpgrade;
import tb.common.item.ItemRevolver;
import tb.utils.DummySteele.Pair;



public class RenderRevolver implements IItemRenderer {
	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("thaumicbases", "models/revolver/revolver.obj"));
	public static final ResourceLocation handle = new ResourceLocation("thaumicbases", "textures/items/revolver/revolverHandleUV.png");
	public static final ResourceLocation barrel = new ResourceLocation("thaumicbases", "textures/items/revolver/revolverBarrelUV.png");
	public static final ResourceLocation metal = new ResourceLocation("thaumicbases", "textures/items/revolver/revolverDarkMetal.png");
	public static final ResourceLocation gun = new ResourceLocation("thaumicbases", "textures/items/revolver/revolverGunUV.png");
	public static final ResourceLocation press = new ResourceLocation("thaumicbases", "textures/items/revolver/revolverPressUV.png");


	public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
		return true;
	}


	public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
		return true;
	}



	public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();

		GL11.glDisable(3008);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);

		if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			if ((Minecraft.getMinecraft()).thePlayer.isSneaking()) {

				GL11.glScaled(0.4D, 0.4D, 0.4D);
				GL11.glRotated(-45.0D, 0.0D, 1.0D, 0.0D);
				GL11.glTranslated(-1.73D, 0.0D, 4.0D);
			} else {

				GL11.glScaled(0.4D, 0.4D, 0.4D);
				GL11.glRotated(-45.0D, 0.0D, 1.0D, 0.0D);
				GL11.glTranslated(4.0D, -1.0D, 1.0D);
			} 
		}

		if (type == IItemRenderer.ItemRenderType.INVENTORY) {

			GL11.glScaled(0.2D, 0.2D, 0.2D);
			GL11.glTranslated(-5.0D, -5.0D, 0.0D);
		} 

		if (type == IItemRenderer.ItemRenderType.EQUIPPED) {

			Entity e = (Entity)data[1];
			if (e.isSneaking()) {

				GL11.glScaled(0.2D, 0.2D, 0.2D);
				GL11.glRotated(45.0D, 0.0D, 1.0D, 0.0D);
				GL11.glRotated(25.0D, 1.0D, 0.0D, 0.0D);
				GL11.glTranslated(0.0D, 4.0D, 6.0D);
			} else {

				GL11.glScaled(0.2D, 0.2D, 0.2D);
				GL11.glRotated(45.0D, 0.0D, 1.0D, 0.0D);
				GL11.glTranslated(0.0D, 2.0D, 6.0D);
			} 
		} 

		if (type == IItemRenderer.ItemRenderType.ENTITY) {

			GL11.glScaled(0.2D, 0.2D, 0.2D);
			GL11.glTranslated(0.0D, 0.0D, 5.0D);
		} 

		ResourceLocation handle = RenderRevolver.handle;
		ResourceLocation barrel = RenderRevolver.barrel;
		ResourceLocation metal = RenderRevolver.metal;
		ResourceLocation gun = RenderRevolver.gun;
		ResourceLocation press = RenderRevolver.press;

		ArrayList<Pair<RevolverUpgrade, Integer>> upgrades = ItemRevolver.getAllUpgradesFor(item);
		for (Pair<RevolverUpgrade, Integer> p : upgrades) {

			RevolverUpgrade ru = (RevolverUpgrade)p.getFirst();
			handle = (ru.getOverridePartTexture(item, 0, ((Integer)p.getSecond()).intValue()) == null) ? handle : ru.getOverridePartTexture(item, 0, ((Integer)p.getSecond()).intValue());
			barrel = (ru.getOverridePartTexture(item, 1, ((Integer)p.getSecond()).intValue()) == null) ? barrel : ru.getOverridePartTexture(item, 1, ((Integer)p.getSecond()).intValue());
			metal = (ru.getOverridePartTexture(item, 2, ((Integer)p.getSecond()).intValue()) == null) ? metal : ru.getOverridePartTexture(item, 2, ((Integer)p.getSecond()).intValue());
			gun = (ru.getOverridePartTexture(item, 3, ((Integer)p.getSecond()).intValue()) == null) ? gun : ru.getOverridePartTexture(item, 3, ((Integer)p.getSecond()).intValue());
			press = (ru.getOverridePartTexture(item, 4, ((Integer)p.getSecond()).intValue()) == null) ? press : ru.getOverridePartTexture(item, 4, ((Integer)p.getSecond()).intValue());
		} 

		(Minecraft.getMinecraft()).renderEngine.bindTexture(handle);
		model.renderPart("Cylinder");
		model.renderPart("Plane");

		double rotation = item.hasTagCompound() ? item.getTagCompound().getDouble("renderedRotation") : 0.0D;

		GL11.glPushMatrix();
		GL11.glTranslated(0.0D, 2.9D, 0.0D);
		GL11.glTranslated(0.0D, -Math.cos(Math.toRadians(rotation)) * 2.8D, 0.0D);
		GL11.glTranslated(Math.sin(Math.toRadians(rotation)) * 2.8D, 0.0D, 0.0D);
		GL11.glRotated(rotation, 0.0D, 0.0D, 1.0D);


		(Minecraft.getMinecraft()).renderEngine.bindTexture(barrel);
		model.renderPart("rBarrel_Cube.001");
		GL11.glPopMatrix();


		(Minecraft.getMinecraft()).renderEngine.bindTexture(metal);
		model.renderPart("Cube.003_Cube.004");
		model.renderPart("Cube.002_Cube.003");
		model.renderPart("Cube_Cube.001");
		(Minecraft.getMinecraft()).renderEngine.bindTexture(gun);
		model.renderPart("Cylinder.002");
		model.renderPart("Plane.001");
		(Minecraft.getMinecraft()).renderEngine.bindTexture(press);
		model.renderPart("Cube.001_Cube.005");

		if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON && item.getTagCompound() != null && item.getTagCompound().getBoolean("hasJar")) {

			GL11.glPushMatrix();

			GL11.glScaled(1.1D, 1.1D, 1.1D);
			GL11.glTranslated(-0.11D, -0.25D, 1.0D);

			double tickIndex = (Minecraft.getMinecraft()).thePlayer.ticksExisted % 40.0D / 20.0D;
			if (tickIndex > 1.0D) {
				tickIndex = -tickIndex + 2.0D;
			}
			GL11.glColor4d(1.0D, 0.0D, 0.0D, tickIndex);

			(Minecraft.getMinecraft()).renderEngine.bindTexture(metal);
			model.renderPart("Cube_Cube.001");

			GL11.glPopMatrix();
		} 

		GL11.glDisable(3042);
		GL11.glEnable(3008);

		GL11.glPopMatrix();
	}
}


