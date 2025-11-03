package tb.common.potion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import tb.utils.TBConfig;

public class PotionVoidCall extends Potion {
	
	public static PotionVoidCall instance = null;
	static final ResourceLocation rl = new ResourceLocation("thaumicbases", "textures/gui/potions.png");

	public PotionVoidCall(int id, boolean idBadEffect, int liquidColour) {
		super(id, idBadEffect, liquidColour);
	}
	
	public static void init() {
		/* 23 */     instance.setPotionName("potion.voidcall");
		/* 24 */     instance.setIconIndex(3, 1);
		/* 25 */     instance.setEffectiveness(0.25D);
		/*    */   }
	

	
	@SideOnly(Side.CLIENT)
	/*    */   public int getStatusIconIndex() {
	/* 36 */     (Minecraft.getMinecraft()).renderEngine.bindTexture(rl);
	/* 37 */     return super.getStatusIconIndex();
	/*    */   }
	
}
