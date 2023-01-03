 package tb.network.proxy;
 import DummyCore.Client.GuiCommon;
 import cpw.mods.fml.client.registry.ClientRegistry;
 import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
 import cpw.mods.fml.client.registry.RenderingRegistry;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.particle.EntityFX;
 import net.minecraft.client.renderer.entity.Render;
 import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.inventory.Container;
 import net.minecraft.item.Item;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.world.World;
 import net.minecraftforge.client.IItemRenderer;
 import net.minecraftforge.client.MinecraftForgeClient;
 import net.minecraftforge.common.MinecraftForge;
 import tb.client.RevolverEvents;
 import tb.client.gui.GuiRevolver;
 import tb.client.gui.GuiVoidAnvil;
import tb.client.gui.GuiThaumicAnvil;
 import tb.client.render.block.BrazierRenderer;
 import tb.client.render.block.CampfireRenderer;
 import tb.client.render.block.ThaumicRelocatorRenderer;
 import tb.client.render.entity.RenderBullet;
 import tb.client.render.item.CastingBraceletRenderer;
 import tb.client.render.item.HerobrinesScytheRenderer;
 import tb.client.render.item.NodeFociRenderer;
 import tb.client.render.item.NodeLinkerItemRenderer;
 import tb.client.render.item.NodeManipulatorItemRenderer;
 import tb.client.render.item.RenderRevolver;
 import tb.client.render.tile.RenderEntityDeconstructor;
 import tb.client.render.tile.RenderNodeLinker;
 import tb.client.render.tile.RenderNodeManipulator;
 import tb.client.render.tile.RenderOverchanter;
 import tb.common.entity.EntityRevolverBullet;
import tb.common.inventory.ContainerOverchanter;
 import tb.common.tile.TileEntityDeconstructor;
 import tb.common.tile.TileNodeLinker;
 import tb.common.tile.TileNodeManipulator;
 import tb.common.tile.TileOverchanter;
 import tb.init.TBBlocks;
 import tb.init.TBItems;
 import thaumcraft.client.fx.ParticleEngine;
 import thaumcraft.client.fx.bolt.FXLightningBolt;
 import thaumcraft.client.fx.particles.FXSparkle;
 
 public class TBClient extends TBServer {
   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
     if (ID == 4331810) {
       
       TileEntity tile = world.getTileEntity(x, y, z);
       
       if (tile != null)
       {
         if (tile instanceof TileOverchanter)
         {
           return new GuiCommon((Container)new ContainerOverchanter(player.inventory, tile), tile);
         }
       }
     } else {
       
       if (ID == 4331809) {
         return new GuiThaumicAnvil(player.inventory, world, x, y, z);
       }
       if (ID == 4331808) {
         return new GuiVoidAnvil(player.inventory, world, x, y, z);
       }
       if (ID == 4331801) {
         return new GuiRevolver(player.inventory, world, x, y, z);
       }
     } 
     return null;
   }
 
 
   
   public void registerRenderInformation() {
     ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDeconstructor.class, (TileEntitySpecialRenderer)new RenderEntityDeconstructor());
     ClientRegistry.bindTileEntitySpecialRenderer(TileOverchanter.class, (TileEntitySpecialRenderer)new RenderOverchanter());
     ClientRegistry.bindTileEntitySpecialRenderer(TileNodeManipulator.class, (TileEntitySpecialRenderer)new RenderNodeManipulator());
     ClientRegistry.bindTileEntitySpecialRenderer(TileNodeLinker.class, (TileEntitySpecialRenderer)new RenderNodeLinker());
     
     MinecraftForgeClient.registerItemRenderer(TBItems.nodeFoci, (IItemRenderer)new NodeFociRenderer());
     MinecraftForgeClient.registerItemRenderer(TBItems.herobrinesScythe, (IItemRenderer)new HerobrinesScytheRenderer());
     MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TBBlocks.nodeManipulator), (IItemRenderer)new NodeManipulatorItemRenderer());
     MinecraftForgeClient.registerItemRenderer(TBItems.revolver, (IItemRenderer)new RenderRevolver());
     MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TBBlocks.nodeLinker), (IItemRenderer)new NodeLinkerItemRenderer());
     MinecraftForgeClient.registerItemRenderer(TBItems.castingBracelet, (IItemRenderer)new CastingBraceletRenderer());
     
     RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new ThaumicRelocatorRenderer());
     RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new CampfireRenderer());
     RenderingRegistry.registerBlockHandler((ISimpleBlockRenderingHandler)new BrazierRenderer());
     RenderingRegistry.registerEntityRenderingHandler(EntityRevolverBullet.class, (Render)new RenderBullet());
     
     MinecraftForge.EVENT_BUS.register(new RevolverEvents());
   }
 
 
   
   public void lightning(World world, double sx, double sy, double sz, double ex, double ey, double ez, int dur, float curve, int speed, int type) {
     FXLightningBolt bolt = new FXLightningBolt(world, sx, sy, sz, ex, ey, ez, world.rand.nextLong(), dur, curve, speed);
     
     bolt.defaultFractal();
     bolt.setType(type);
     bolt.setWidth(0.125F);
     bolt.finalizeBolt();
   }
 
   
   public void sparkle(World w, double x, double y, double z, double dx, double dy, double dz, int color, float scale) {
     FXSparkle fx = new FXSparkle(w, x, y, z, dx, dy, dz, scale, color, 1);
     fx.noClip = true;
     ParticleEngine.instance.addEffect(w, (EntityFX)fx);
   }
 
   
   public World clientWorld() {
     return (World)(Minecraft.getMinecraft()).theWorld;
   }
 }


