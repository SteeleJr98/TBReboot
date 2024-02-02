package tb.client.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import tb.common.inventory.FociCrafterContainer;
import tb.common.tile.FakeWorkbenchTile;
import thaumcraft.common.container.ContainerArcaneWorkbench;
import thaumcraft.common.tiles.TileArcaneWorkbench;

@SideOnly(Side.CLIENT)
public class GuiFociCrafter extends GuiContainer {
	
	public float funnyR = 1F;
	public float funnyG = 1F;
	public float funnyB = 1F;

	
	
	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation("thaumicbases", "textures/gui/gui_arcaneworkbench.png");
	
	



	//private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation("textures/gui/container/crafting_table.png");
	
	
	

    public GuiFociCrafter(InventoryPlayer player, World w, int x, int y, int z)
    {
        //super((Container) new FociCrafterContainer(player, w, x, y, z));
    	
    	super((Container) new FociCrafterContainer(player, new FakeWorkbenchTile()));
    	
    	this.ySize = 234;
    	this.xSize = 190;
    	
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        //this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 28, 6, 4210752);
        //this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    	
    	
    	
    }

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
    	

    	
        GL11.glColor4f(this.funnyR, this.funnyG, this.funnyB, 0.1F);
        this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        
        GL11.glDisable(3042);
    }
    
    private float clamp(float f) {
    	return f < 1.0F ? f : 0F;
    }

}
