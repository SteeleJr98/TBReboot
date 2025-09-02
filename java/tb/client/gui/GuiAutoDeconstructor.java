package tb.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import tb.common.inventory.ContainerAutoDeconstructor;
import tb.common.tile.TileAutoDeconstructor;

public class GuiAutoDeconstructor extends GuiContainer {



	private static final ResourceLocation bgTexture = new ResourceLocation("thaumicbases", "textures/gui/auto_crafter.png");
	private TileAutoDeconstructor tile;

	public GuiAutoDeconstructor(InventoryPlayer playerInv, World world, int x, int y, int z) {
		super((Container)new ContainerAutoDeconstructor(playerInv, (TileAutoDeconstructor) world.getTileEntity(x, y, z)));
		this.tile = (TileAutoDeconstructor) world.getTileEntity(x, y, z);
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
		int time = this.tile.getTimer();

		if (time > 0)
		{
			int j1 = (int)(33F * ((float)time / this.tile.loopTime));

			//            if (j1 > 0)
			//            {
			//                this.drawTexturedModalRect(k + 97, l + 16, 176, 0, 9, j1);
			//            }

			//            int k1 = time / 2 % 7;
			//            switch (k1)
			//            {
			//                case 0:
			//                    j1 = 29;
			//                    break;
			//                case 1:
			//                    j1 = 24;
			//                    break;
			//                case 2:
			//                    j1 = 20;
			//                    break;
			//                case 3:
			//                    j1 = 16;
			//                    break;
			//                case 4:
			//                    j1 = 11;
			//                    break;
			//                case 5:
			//                    j1 = 6;
			//                    break;
			//                case 6:
			//                    j1 = 0;
			//            }

			//where I want it to be: 70, 21
			//where it is: 190, 0
			//dims: 34x33

			if (j1 > 0)
			{
				this.drawTexturedModalRect(k + 70, l + 21 + 33 - j1, 190, 33 - j1, 34, j1);
			}
		}

	}

}
