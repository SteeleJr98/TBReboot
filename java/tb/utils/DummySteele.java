package tb.utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

//import DummyCore.Utils.DummyPacketIMSG_Tile;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.ForgeHooksClient;
import tb.core.CreativeTabBlocks;
import tb.core.CreativeTabItems;
import tb.core.TBCore;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;

public class DummySteele {
	
	public static Hashtable<String,ResourceLocation> locTable = new Hashtable<String,ResourceLocation>(); //Used in Draw Utils
	//public static Hashtable<Block,String> blocksList = new Hashtable<Block, String>(); //Used in Block Registry
	//public static Hashtable<Item,String> itemsList = new Hashtable<Item, String>(); //Used in Item Registry
	
	
	//Misc Utils
	public static NBTTagCompound getStackTag(ItemStack stack) {
		makeNBTTag(stack);
		return stack.getTagCompound();
	}
	
	public static void makeNBTTag(ItemStack stack) {
		if (stack.hasTagCompound()) {
			return;
		}
		NBTTagCompound newTag = new NBTTagCompound();
		stack.setTagCompound(newTag);
	}
	
	
	public static void changeBiome(World w, BiomeGenBase biome, int x, int z) {
		Chunk chunk = w.getChunkFromBlockCoords(x, z);
		byte[] b = chunk.getBiomeArray();
		byte bbiome = b[(z & 0xf) << 4 | x & 0xf];
		bbiome = (byte)(biome.biomeID & 0xff);
		b[(z & 0xf) << 4 | x & 0xf] = bbiome;
		chunk.setBiomeArray(b);
		
	}
	
	public static Double[] getDistFromLook(EntityPlayer player, Double dist) {
		
		//Double[] playerPos = {player.posX, player.posY, player.posZ};
		
		Double rSin = Math.sin((Math.toRadians(player.rotationYawHead))) * dist;
		Double rCos = Math.cos((Math.toRadians(player.rotationYawHead))) * dist;
		
		Double[] retPos = {player.posX - rSin, player.posY + 0.5D, player.posZ + rCos}; //Subtract rSin because MC is backwards
		
		return retPos;
	}
	
	public static void dropItemsOnBlockBreak(World par1World, int par2, int par3, int par4, Block par5, int par6)
	{
		//Was causing too much issues, had to add a try/catch statement...
		try
		{
			IInventory inv = (IInventory)par1World.getTileEntity(par2, par3, par4);
	
	        if (inv != null)
	        {
	            for (int j1 = 0; j1 < inv.getSizeInventory(); ++j1)
	            {
	                ItemStack itemstack = inv.getStackInSlot(j1);
	
	                if (itemstack != null)
	                {
	                    float f = par1World.rand.nextFloat() * 0.8F + 0.1F;
	                    float f1 = par1World.rand.nextFloat() * 0.8F + 0.1F;
	                    float f2 = par1World.rand.nextFloat() * 0.8F + 0.1F;
	
	                    while (itemstack.stackSize > 0)
	                    {
	                        int k1 = par1World.rand.nextInt(21) + 10;
	
	                        if (k1 > itemstack.stackSize)
	                        {
	                            k1 = itemstack.stackSize;
	                        }
	
	                        itemstack.stackSize -= k1;
	                        EntityItem entityitem = new EntityItem(par1World, (double)((float)par2 + f), (double)((float)par3 + f1), (double)((float)par4 + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
	
	                        if (itemstack.hasTagCompound())
	                        {
	                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
	                        }
	
	                        float f3 = 0.05F;
	                        entityitem.motionX = (double)((float)par1World.rand.nextGaussian() * f3);
	                        entityitem.motionY = (double)((float)par1World.rand.nextGaussian() * f3 + 0.2F);
	                        entityitem.motionZ = (double)((float)par1World.rand.nextGaussian() * f3);
	                        par1World.spawnEntityInWorld(entityitem);
	                    }
	                }
	            }
	    	}
		}catch(Exception ex)
		{
			TBCore.TBLogger.error("DummyCore", "[ERROR]Trying to drop items upon block breaking, but caught an exception:");
			ex.printStackTrace();
			return;
		}
	}
	
	public static void syncTileEntity(NBTTagCompound tileTag, int packetID)
	{
		DummySteeleTempPacket simplePacket = new DummySteeleTempPacket(tileTag);
		TBCore.network.sendToAll(simplePacket);
	}
	
	public static ArrayList<ChunkCoordinates> findBlocks(World w, int x, int y, int z, int dist, Block block) {
        ArrayList<ChunkCoordinates> openList = new ArrayList<ChunkCoordinates>();
        
        int xMin = x - dist;
        int yMin = y - dist;
        int zMin = z - dist;
        int xMax = x + dist;
        int yMax = y + dist;
        int zMax = z + dist;
        
        for (int i = xMin; i <= xMax; i++) {
        	for (int j = yMin; j <= yMax; j++) {
        		for (int k = zMin; k <= zMax; k++) {
        			if (w.getBlock(i, j, k) == block) {
        				openList.add(new ChunkCoordinates(i, j, k));
        			}
        		}
        	}
        }
        
        return openList;
    }
	
	public static ArrayList<ChunkCoordinates> findOpenBlocks(World w, int x, int y, int z, int dist) {
		return findBlocks(w, x, y, z, dist, Blocks.air);
	}
	
	public static ArrayList<ChunkCoordinates> getLowestNearBlock(World w, int x, int y, int z, int dist) {
		//DummySteele.sendMessageFromServer("in method");
		ArrayList<ChunkCoordinates> openList = findOpenBlocks(w, x, y, z, dist);
		//DummySteele.sendMessageFromServer("open size: " + openList.size());
		ArrayList<ChunkCoordinates> tempList = new ArrayList();
		if (openList.size() > 0) {
			int lowestY = openList.get(0).posY;
			for (ChunkCoordinates c : openList) {
				if (c.posY < lowestY) {
					lowestY = c.posY;
					//DummySteele.sendMessageFromServer("new lowest: " + lowestY);
					tempList.clear();
					tempList.add(c);
				}
				if (c.posY == lowestY) {
					tempList.add(c);
				}
			}
		}
		return tempList;
	}
	
	public static void SpamDebugMessage(int padSpace, String message) {
		SpamDebugMessage(padSpace, message, "");
	}
	
	public static void SpamDebugMessage(int padSpace, String message, String topAndBottom) {
		if (topAndBottom != "") {
			TBCore.TBLogger.log(Level.INFO, topAndBottom);
		}
		
		for (int i = 0; i < (topAndBottom != "" ? padSpace : padSpace - 1); i++) {
			TBCore.TBLogger.log(Level.INFO, "");
		}
		
		TBCore.TBLogger.log(Level.INFO, message);
		
		for (int i = 0; i < (topAndBottom != "" ? padSpace : padSpace - 1); i++) {
			TBCore.TBLogger.log(Level.INFO, "");
		}
		
		if (topAndBottom != "") {
			TBCore.TBLogger.log(Level.INFO, topAndBottom);
		}
	}
	
	//Math Utils
	public static double randomDouble(Random r) {
		return r.nextDouble() - r.nextDouble();
	}
	
	public static float randomFloat(Random rand)
	{
		return rand.nextFloat()-rand.nextFloat();
	}
	
	
	
	//Draw Utils
	@SideOnly(Side.CLIENT)
    public static void renderItemStack_Full(ItemStack stk,double posX, double posY, double posZ, double screenPosX, double screenPosY, double screenPosZ, float rotation, float rotationZ, float colorRed, float colorGreen, float colorBlue, float offsetX, float offsetY, float offsetZ, boolean force3DRender)
    {
    	if(stk != null)
    	{
	    	 ItemStack itemstack = stk.copy();
	    	 itemstack.stackSize = 1; //Doing this so no weird glitches occur.
	         RenderBlocks renderBlocksRi = new RenderBlocks();
	         Random random = new Random(); 
	         boolean renderWithColor = true;
	         if (itemstack != null && itemstack.getItem() != null)
	         {
	             Minecraft.getMinecraft().renderEngine.bindTexture(Minecraft.getMinecraft().renderEngine.getResourceLocation(stk.getItemSpriteNumber()));
	             TextureUtil.func_152777_a(false, false, 1.0F);
	             random.setSeed(187L);
	             GL11.glPushMatrix();
	             float f2 = rotationZ;
	             float f3 = rotation;
	             byte b0 = 1;
	
	             if (stk.stackSize > 1)
	             {
	                 b0 = 2;
	             }
	
	             if (stk.stackSize > 5)
	             {
	                 b0 = 3;
	             }
	
	             if (stk.stackSize > 20)
	             {
	                 b0 = 4;
	             }
	
	             if (stk.stackSize > 40)
	             {
	                 b0 = 5;
	             }
	
	             GL11.glTranslated((float)screenPosX+offsetX, (float)screenPosY+offsetY, (float)screenPosZ+offsetZ);
	             GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	             float f6;
	             float f7;
	             int k;
	             EntityItem fakeItem = new EntityItem(Minecraft.getMinecraft().theWorld, posX, posY, posZ, stk);
	             GL11.glRotatef(f2, 0, 0, 1);
	             if (ForgeHooksClient.renderEntityItem(fakeItem, itemstack, f2, f3, random, Minecraft.getMinecraft().renderEngine, renderBlocksRi, b0))
	             {
	                 
	             }
	             else if (itemstack.getItemSpriteNumber() == 0 && itemstack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType()))
	             {
	                 Block block = Block.getBlockFromItem(itemstack.getItem());
	                 GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
	                 float f9 = 0.25F;
	                 k = block.getRenderType();
	
	                 if (k == 1 || k == 19 || k == 12 || k == 2)
	                 {
	                     f9 = 0.5F;
	                 }
	
	                 if (block.getRenderBlockPass() > 0)
	                 {
	                     GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
	                     GL11.glEnable(GL11.GL_BLEND);
	                     OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
	                 }
	
	                 GL11.glScalef(f9, f9, f9);
	
	                 for (int l = 0; l < b0; ++l)
	                 {
	                     GL11.glPushMatrix();
	
	                     if (l > 0)
	                     {
	                         f6 = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / f9;
	                         f7 = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / f9;
	                         float f8 = (random.nextFloat() * 2.0F - 1.0F) * 0.2F / f9;
	                         GL11.glTranslatef(f6, f7, f8);
	                     }
	
	                     renderBlocksRi.renderBlockAsItem(block, itemstack.getItemDamage(), 1.0F);
	                     GL11.glPopMatrix();
	                 }
	
	                 if (block.getRenderBlockPass() > 0)
	                 {
	                     GL11.glDisable(GL11.GL_BLEND);
	                 }
	             }
	             else
	             {
	                 if (itemstack.getItem().requiresMultipleRenderPasses())
	                 {
	                     GL11.glScalef(0.5F, 0.5F, 0.5F);
	                     for (int j = 0; j < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++j)
	                     {
	                         random.setSeed(187L);
	                         itemstack.getItem().getIcon(itemstack, j);
	                         renderItemStack(stk, posX, posY, posZ, screenPosX, screenPosY, screenPosZ, rotation, colorRed, colorGreen, colorBlue, j, stk.stackSize,force3DRender);
	                     }
	                 }
	                 else
	                 {
	                     if (itemstack != null && itemstack.getItem() instanceof ItemCloth)
	                     {
	                         GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
	                         GL11.glEnable(GL11.GL_BLEND);
	                         OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
	                     }
	                     GL11.glScalef(0.5F, 0.5F, 0.5F);
	                     itemstack.getIconIndex();
	
	                     if (renderWithColor)
	                     {
	                         renderItemStack(stk, posX, posY, posZ, screenPosX, screenPosY, screenPosZ, rotation, colorRed, colorGreen, colorBlue, 0, stk.stackSize,force3DRender);
	                     }
	                     if (itemstack != null && itemstack.getItem() instanceof ItemCloth)
	                     {
	                         GL11.glDisable(GL11.GL_BLEND);
	                     }
	                 }
	             }
	             fakeItem = null;
	             GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	             GL11.glPopMatrix();
	             Minecraft.getMinecraft().renderEngine.bindTexture(Minecraft.getMinecraft().renderEngine.getResourceLocation(stk.getItemSpriteNumber()));
	             TextureUtil.func_147945_b();
	         }
	         itemstack = null;
    	}
    }
	
	@SideOnly(Side.CLIENT)
    public static void renderItemStack(ItemStack stk,double posX, double posY, double posZ, double screenPosX, double screenPosY, double screenPosZ, float rotation, float colorRed, float colorGreen, float colorBlue, int renderPass, int itemsAmount, boolean force3DRender)
    {
        final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
        Random random = new Random();
        IIcon iicon = stk.getItem().getIcon(stk, renderPass);
    	
        Tessellator tessellator = Tessellator.instance;

        if (iicon == null)
        {
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            ResourceLocation resourcelocation = texturemanager.getResourceLocation(stk.getItem().getSpriteNumber());
            iicon = ((TextureMap)texturemanager.getTexture(resourcelocation)).getAtlasSprite("missingno");
        }

        float minU = ((IIcon)iicon).getMinU();
        float maxU = ((IIcon)iicon).getMaxU();
        float minV = ((IIcon)iicon).getMinV();
        float maxV = ((IIcon)iicon).getMaxV();
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.25F;
        float f10;

        if (Minecraft.getMinecraft().gameSettings.fancyGraphics || force3DRender)
        {
            GL11.glPushMatrix();
            GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
            float f9 = 0.0625F;
            f10 = 0.021875F;
            ItemStack itemstack = stk;
            int j = itemstack.stackSize;
            byte b0;

            if (j < 2)
            {
                b0 = 1;
            }
            else if (j < 16)
            {
                b0 = 2;
            }
            else if (j < 32)
            {
                b0 = 3;
            }
            else
            {
                b0 = 4;
            }
            
            GL11.glTranslatef(-f7, -f8, -((f9 + f10) * (float)b0 / 2.0F));

            for (int k = 0; k < b0; ++k)
            {
                if (k > 0)
                {
                    float x = (random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    float y = (random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    random.nextFloat();
                    GL11.glTranslatef(x, y, f9 + f10);
                }
                else
                {
                    GL11.glTranslatef(0f, 0f, f9 + f10);
                }

                if (itemstack.getItemSpriteNumber() == 0)
                {
                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                }
                else
                {
                	Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
                }

                GL11.glColor4f(colorRed, colorGreen, colorBlue, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, maxU, minV, minU, maxV, ((IIcon)iicon).getIconWidth(), ((IIcon)iicon).getIconHeight(), f9);

                if (itemstack.hasEffect(renderPass))
                {
                    GL11.glDepthFunc(GL11.GL_EQUAL);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    Minecraft.getMinecraft().renderEngine.bindTexture(RES_ITEM_GLINT);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                    float f11 = 0.76F;
                    GL11.glColor4f(0.5F * f11, 0.25F * f11, 0.8F * f11, 1.0F);
                    GL11.glMatrixMode(GL11.GL_TEXTURE);
                    GL11.glPushMatrix();
                    float f12 = 0.125F;
                    GL11.glScalef(f12, f12, f12);
                    float f13 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                    GL11.glTranslatef(f13, 0.0F, 0.0F);
                    GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f9);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(f12, f12, f12);
                    f13 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                    GL11.glTranslatef(-f13, 0.0F, 0.0F);
                    GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f9);
                    GL11.glPopMatrix();
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDepthFunc(GL11.GL_LEQUAL);
                }
            }

            GL11.glPopMatrix();
        }
        else
        {
            for (int l = 0; l < itemsAmount; ++l)
            {
                GL11.glPushMatrix();

                if (l > 0)
                {
                    f10 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f16 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f17 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(f10, f16, f17);
                }
                GL11.glColor4f(colorRed, colorGreen, colorBlue, 1.0F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                tessellator.addVertexWithUV((double)(0.0F - f7), (double)(0.0F - f8), 0.0D, (double)minU, (double)maxV);
                tessellator.addVertexWithUV((double)(f6 - f7), (double)(0.0F - f8), 0.0D, (double)maxU, (double)maxV);
                tessellator.addVertexWithUV((double)(f6 - f7), (double)(1.0F - f8), 0.0D, (double)maxU, (double)minV);
                tessellator.addVertexWithUV((double)(0.0F - f7), (double)(1.0F - f8), 0.0D, (double)minU, (double)minV);
                tessellator.draw();
                GL11.glPopMatrix();
            }
        }
    }
	
	@SideOnly(Side.CLIENT)
	public static void bindTexture(String mod, String texture)
	{
		if(locTable.contains(mod+":"+texture))
			Minecraft.getMinecraft().getTextureManager().bindTexture(locTable.get(mod+":"+texture));
		else
		{
			ResourceLocation loc = new ResourceLocation(mod,texture);
			locTable.put(mod+":"+texture, loc);
			Minecraft.getMinecraft().getTextureManager().bindTexture(loc);	
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//Static Pair class
	public static class Pair<T1, T2> {
		public T1 t1;
		public T2 t2;
		
		public T1 getFirst() {
			return t1;
		}
		
		public T2 getSecond() {
			return t2;
		}
		
		public Pair(T1 t11, T2 t22) {
			t1 = t11;
			t2 = t22;
		}
	}
	
	
	//Static 3DCoord Class
	public static class Coord3D {
		
		public float x;
		public float y;
		public float z;
		
		public Coord3D(double posX, double posY, double posZ)
		{
			this.x = (float) posX;
			this.y = (float) posY;
			this.z = (float) posZ;
		}
		
		public Coord3D(float i, float k, float j)
		{
			this.x = i;
			this.y = k;
			this.z = j;
		}
		
		public Coord3D()
		{
			this(0,0,0);
		}
		
		public String toString()
		{
			return "||x:"+x+"||y:"+y+"||z:"+z;
		}
		
		public static Coord3D fromString(String data)
		{
			DummyData[] dt = DataStorage.parseData(data);
			float cX = Float.parseFloat(dt[0].fieldValue);
			float cY = Float.parseFloat(dt[1].fieldValue);
			float cZ = Float.parseFloat(dt[2].fieldValue);
			return new Coord3D(cX,cY,cZ);
		}

	}
	
	
	//Static Data Storage Class
	public static class DataStorage {
		private static String dataString = "";
		
		/**
		 * Adds the given DummyData to the data string
		 * @version From DummyCore 1.2
		 * @param data - the data to be added to the string
		 */
		public static void addDataToString(DummyData data)
		{
			dataString = dataString.concat(data.toString());
		}
		
		/**
		 * Returns the data string, with all data written to it. Also resets the string, so this should be used only once, after you have stored all your data.
		 * @version From DummyCore 1.2
		 * @return Correctly formated DataString
		 */
		public static String getDataString()
		{
			String ret = dataString;
			dataString = "";
			return ret;
		}
		
		/**
		 * Used to get your DummyData from the correctly formated string.
		 * @version From DummyCore 1.2
		 * @param s - the string to extract data from
		 * @return - An array of all data from the given string
		 */
		public static DummyData[] parseData(String s)
		{
			String field = "";
			Object value = null;
			List<DummyData> data = new ArrayList<DummyData>();
			for(int i = 0; i < s.length(); ++i)
			{
				if(i+2 < s.length() && s.substring(i, i+2).contains("||"))
				{
					int size = 0;
					ForSize:for(int i1 = i; i1 < s.length();++i1)
					{
						if(s.charAt(i1) == ':')
							break ForSize;
						++size;
					}
					field = s.substring(i+2, i+size);
				}
				if(i+1 < s.length() && s.substring(i, i+1).contains(":"))
				{
					int size = 0;
					ForSize:for(int i1 = i; i1 < s.length();++i1)
					{
						if(s.charAt(i1) == '|')
							break ForSize;
						++size;
					}
					value = s.substring(i+1, i+size);
				}
				if(field != "" && value != null)
				{
					DummyData date = new DummyData(field,value);
					data.add(date);
					field = "";
					value = null;
				}
			}
			DummyData[] ret = new DummyData[data.size()];
			for(int i = 0; i < ret.length; ++i)
			{
				ret[i] = data.get(i);
			}
			return ret;
		}

	}
	
	
	//Dummy Data Storage
	public static class DummyData {
		public final String fieldName;
		public final String fieldValue;
		public DummyData(String field, String value)
		{
			fieldName = field;
			fieldValue = value;
		}
		
		public DummyData(String field, Object value)
		{
			fieldName = field;
			fieldValue = value.toString();
		}
		
		public String toString()
		{
			String ret = "";
			ret = ret.concat("||").concat(fieldName).concat(":").concat(fieldValue.toString());
			return ret;
		}
		
		public static DummyData makeNull()
		{
			return new DummyData("null","null");
		}
	}
	
	//lighting utils something
	public static class Lightning {
		
		public Random rnd;
		public Coord3D[] lightningVecsStart;
		public Coord3D[] lightningVecsEnd;
		public Coord3D start;
		public Coord3D end;
		public float renderTicksExisted;
		public float[] getLightningColor = new float[3];
		
		/**
		 * Creates a lightning
		 * @param rand - the random on which your lightning will be built upon.
		 * @param begin - the beginning point of the lightning. This NEVER should relate to coordinates in the world! Instead, it can be 0,0,0, if you want the lightning to be created from the bottom corner of a block, for example.
		 * @param stop - the end point of the lightning. This NEVER should relate to coordinates in the world! Instead, it can be 0,10,0, if you want the lightning to go 10 blocks higher.
		 * @param curve - the amount of curving in your lightning. Usually somewhat around 0.3F if you want the lightning to look realistic. If you put 0.0F it will be a straight line, and 1.0F can lead to weird stuff.
		 * @param color - the color array, representing the color of the lightning.
		 */
		public Lightning(Random rand, Coord3D begin, Coord3D stop,float curve, float... color)
		{
			rnd = rand;
			start = begin;
			end = stop;
			getLightningColor = color;
			curve(curve);
		}
		
		private void curve(float factor)
		{
			float distX = end.x-start.x;
			float distY = end.y-start.y;
			float distZ = end.z-start.z;
			float genDistance = (float) Math.sqrt(distX*distX+distY*distY+distZ*distZ);
			lightningVecsStart = new Coord3D[64];
			lightningVecsEnd = new Coord3D[64];
			generateLightningBetween2Points(start, end, 0, 8, factor);
			generateLightningBetween2Points(lightningVecsStart[3], new Coord3D(end.x*(factor*10),end.y,end.z*(factor*10)), 8, 4, factor*3);
			int genIndex = 0;
			for(int i = 0; i < 12; ++i)
			{
				int rndSteps = rnd.nextInt(2);
				genIndex += (1+rndSteps);
				generateLightningBetween2Points(lightningVecsStart[i], new Coord3D(lightningVecsStart[i].x+randomFloat(rnd)*(genDistance/4), lightningVecsStart[i].y+randomFloat(rnd)*(genDistance/4),lightningVecsStart[i].z+randomFloat(rnd)*(genDistance/4)),12+i,1+rndSteps,factor/2);
			}
			for(int i = 12; i < 12+genIndex; ++i)
			{
				if(lightningVecsStart[i] != null)
				generateLightningBetween2Points(lightningVecsStart[i], new Coord3D(lightningVecsStart[i].x+randomFloat(rnd)*(genDistance/8), lightningVecsStart[i].y+randomFloat(rnd)*(genDistance/8),lightningVecsStart[i].z+randomFloat(rnd)*(genDistance/8)),12+genIndex+i,0,factor);
			}
		}
		
		private void generateLightningBetween2Points(Coord3D from, Coord3D to,int beginVecIndex, int steps, float curve)
		{
			float distX = to.x-from.x;
			float distY = to.y-from.y;
			float distZ = to.z-from.z;
			for(int i = 0; i < steps; ++i)
			{
				if(i == 0)
				{
					lightningVecsStart[beginVecIndex+i] = from;
					lightningVecsEnd[beginVecIndex+i] = new Coord3D(lightningVecsStart[beginVecIndex+i].x+distX/steps+randomFloat(rnd)*curve,lightningVecsStart[beginVecIndex+i].y+distY/steps+randomFloat(rnd)*curve,lightningVecsStart[beginVecIndex+i].z+distZ/steps+randomFloat(rnd)*curve);
				}else
				{
					lightningVecsStart[beginVecIndex+i] = lightningVecsEnd[beginVecIndex+i-1];
					lightningVecsEnd[beginVecIndex+i] = new Coord3D(lightningVecsStart[beginVecIndex+i].x+distX/steps+randomFloat(rnd)*curve,lightningVecsStart[beginVecIndex+i].y+distY/steps+randomFloat(rnd)*curve,lightningVecsStart[beginVecIndex+i].z+distZ/steps+randomFloat(rnd)*curve);
				}
			}
		
		}
		
		/**
		 * Use this anywhere in your render code to actually render the lightning.
		 * @param x
		 * @param y
		 * @param z
		 * @param partialTicks
		 */
		public void render(double x, double y, double z, float partialTicks)
		{
			renderTicksExisted += partialTicks/1F;
			if(renderTicksExisted >= this.lightningVecsStart.length)
				renderTicksExisted = this.lightningVecsStart.length;
	        GL11.glPushMatrix();
	        GL11.glEnable(2929);
	        GL11.glEnable(3042);
	        GL11.glBlendFunc(770, 771);
	        GL11.glDisable(3553);
	        GL11.glDisable(2896);
	        GL11.glTranslated(x, y, z);
			for(int i = 0; i < renderTicksExisted; ++i)
			{
				if(this.lightningVecsStart[i] != null)
				{
		            this.renderBeam(lightningVecsStart[i], lightningVecsEnd[i], 0.8F);
		            this.renderBeam(lightningVecsStart[i], lightningVecsEnd[i], 1F);
				}
			}
	        GL11.glEnable(2896);
	        GL11.glEnable(3553);
	        GL11.glPopMatrix();
		}
		
		public void renderBeam(Coord3D begin, Coord3D stop, float type)
		{
			if(type != 1F)
				GL11.glLineWidth(3);
			else
				GL11.glLineWidth(1);
			GL11.glPushMatrix();
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glEnable(GL11.GL_CULL_FACE);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        GL11.glDisable(GL11.GL_ALPHA_TEST);
	        GL11.glBegin(1);
	       
	        if(type != 1)
	        	GL11.glColor4f(1,1,1,0.2F);
	        else
	        	GL11.glColor4f(this.getLightningColor[0],this.getLightningColor[1],this.getLightningColor[2],0.8F);
	        GL11.glVertex3d(begin.x, begin.y, begin.z);
	        GL11.glVertex3d(stop.x, stop.y, stop.z);
	        GL11.glColor3d(1.0D, 1.0D, 1.0D);
	        
	        
	        GL11.glEnd();
	        GL11.glShadeModel(GL11.GL_FLAT);
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glEnable(GL11.GL_ALPHA_TEST);
	        GL11.glDisable(GL11.GL_CULL_FACE);
	        GL11.glPopMatrix();
		}


	}
	
	
	
	
	
	//Block Registry
	public static void registerBlock(Block b, String name, Class<?> modClass, Class<? extends ItemBlock> blockClass)
	{
		if(blockClass == null)
		{
			GameRegistry.registerBlock(b, name);
		}else
		{
			GameRegistry.registerBlock(b, blockClass, name);
		}
		Side s = FMLCommonHandler.instance().getEffectiveSide();
		if(s == Side.CLIENT)
		{
			b.setCreativeTab(CreativeTabBlocks.tbTabBlocks);
			//blocksList.put(b, Core.getBlockTabForMod(modClass).getTabLabel());
		}
	}
	
	//Item Registry
	public static void registerItem(Item i, String name, Class<?> modClass)
	{
		Side s = FMLCommonHandler.instance().getEffectiveSide();
		if(s == Side.CLIENT)
		{
			i.setCreativeTab(CreativeTabItems.tbTabItems);
			//itemsList.put(i, Core.getItemTabForMod(modClass).getTabLabel());
		}
		GameRegistry.registerItem(i, name);
	}

	public static boolean arrayContains(Object[] array, Object x) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == x) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean arrayContains(int[] array, int x) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == x) {
				return true;
			}
		}
		return false;
	}

	public static void sendPacketToAllAround(World w,Packet pkt, int x, int y, int z, int dimId, double distance)
	{
		List<EntityPlayer> playerLst = w.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(x-0.5D, y-0.5D, z-0.5D, x+0.5D, y+0.5D, z+0.5D).expand(distance, distance, distance));
		if(!playerLst.isEmpty())
		{
			for(int i = 0; i < playerLst.size(); ++i)
			{
				EntityPlayer player = playerLst.get(i);
				if(player instanceof EntityPlayerMP)
				{
					if(pkt instanceof S35PacketUpdateTileEntity)
					{
						NBTTagCompound tileTag = new NBTTagCompound();
						w.getTileEntity(x, y, z).writeToNBT(tileTag);
						TBCore.network.sendTo(new DummySteeleTempPacket(tileTag,-10), (EntityPlayerMP) player);
					}else
					{
						if(player.dimension == dimId)
							((EntityPlayerMP)player).getServerForPlayer().func_73046_m().getConfigurationManager().sendPacketToAllPlayers(pkt);
					}
				}else
				{
					//Notifier.notifyDebug("Trying to send packet "+pkt+" to all around on Client side, probably a bug, ending the packet send try");
				}
			}
		}
	}
	
	
	public static void sendMessageFromServer(Object msg){
		if (TBCore.isDev) {
			MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), "/say " + msg.toString());
		}
	}
	
	public static class MystColour {
		public final float r;
		public final float g;
		public final float b;
		
		public MystColour(float r, float g, float b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
		
		public MystColour(int r, int g, int b) {
			this.r = r / 255F;
			this.g = g / 255F;
			this.b = b / 255F;
		}
		
		public int asInt() {
			int tempColour = ((int) (this.r * 255) << 16);
			tempColour += ((int) (this.g * 255) << 8);
			tempColour += ((int) (this.b * 255));
			return tempColour;
		}
		
		
	}
	
	public static class SimpleNodeData {
		public NodeType type;
		public NodeModifier modifier;
		public AspectList aspects;
		private World world;
		
		public SimpleNodeData(World world) {
			this.world = world;
			this.type = this.getNodeType();
			this.modifier = this.getNodeModifier();
			this.aspects = this.getAspects();
		}
		
		private NodeType getNodeType() {
			
			NodeType type = NodeType.NORMAL;
			
			if (this.world.rand.nextInt(10) == 0) {
				int randInt = this.world.rand.nextInt(10);
				
				if (randInt == 0) {
					type = NodeType.DARK;
				}
				else if (randInt == 1) {
					type = NodeType.HUNGRY;
				}
				else {
					type = NodeType.NORMAL;
				}
				
			}
			
			
			return type;
			
			//return NodeType.values()[this.world.rand.nextInt(NodeType.values().length)];
		}
		
		private NodeModifier getNodeModifier() {
			return NodeModifier.values()[this.world.rand.nextInt(NodeModifier.values().length)];
		}
		
		private AspectList getAspects() {
			
			AspectList tempList = new AspectList();
			
			int randInt = this.world.rand.nextInt(5);
			
			for (int i = 0; i <= randInt + 1; i++) {
				tempList.add(Aspect.getAspect(Aspect.aspects.keySet().toArray()[this.world.rand.nextInt(Aspect.aspects.size())].toString()), this.world.rand.nextInt(10));
			}
			
			return tempList;
		}
		
	}
	
}
