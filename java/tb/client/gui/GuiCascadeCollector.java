package tb.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import tb.common.inventory.ContainerAutoDeconstructor;
import tb.common.inventory.ContainerCascadeCollector;
import tb.common.tile.TileAutoDeconstructor;
import tb.common.tile.TileCascadeCollector;

public class GuiCascadeCollector extends GuiContainer {

	
	private static final ResourceLocation bgTexture = new ResourceLocation("thaumicbases", "textures/gui/cascadeCollector.png");
	private TileCascadeCollector tile;
	
	public GuiCascadeCollector(InventoryPlayer pInv, World world, int x, int y, int z) {
		super((Container)new ContainerCascadeCollector(pInv, (TileCascadeCollector) world.getTileEntity(x, y, z)));
		this.tile = (TileCascadeCollector) world.getTileEntity(x, y, z);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		//String itemString = this.tile.getStackInSlot(0) != null ? this.tile.getStackInSlot(0).getDisplayName() : "item is null";
		//this.fontRendererObj.drawString(itemString, this.xSize / 2 - this.fontRendererObj.getStringWidth(itemString) / 2, 6, 4210752);

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(bgTexture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

}
