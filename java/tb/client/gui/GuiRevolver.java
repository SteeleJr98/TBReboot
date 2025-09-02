package tb.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import tb.common.inventory.ContainerRevolver;

public class GuiRevolver extends GuiContainer {
	public static final ResourceLocation revolverTextures = new ResourceLocation("thaumicbases", "textures/gui/revolver.png");

	public int blockedSlot;

	public GuiRevolver(InventoryPlayer inv, World w, int x, int y, int z) {
		super((Container)new ContainerRevolver(inv, w, x, y, z));
		this.blockedSlot = inv.currentItem;
	}



	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
		(Minecraft.getMinecraft()).renderEngine.bindTexture(revolverTextures);
		float storedZLevel = this.zLevel;

		this.zLevel = 200.0F;

		GL11.glDisable(3008);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);

		drawTexturedModalRect(8 + this.blockedSlot * 18, 142, 240, 0, 16, 16);

		GL11.glDisable(3042);
		GL11.glEnable(3008);

		this.zLevel = storedZLevel;
	}


	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseZ) {
		if ((Minecraft.getMinecraft()).thePlayer.inventory.mainInventory[this.blockedSlot] == null) {
			(Minecraft.getMinecraft()).thePlayer.closeScreen();
		}
		(Minecraft.getMinecraft()).renderEngine.bindTexture(revolverTextures);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;

		drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
}


