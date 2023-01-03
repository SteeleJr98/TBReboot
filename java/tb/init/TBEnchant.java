 package tb.init;
 
 import cpw.mods.fml.common.Loader;
 import java.lang.reflect.Method;
 import net.minecraft.enchantment.Enchantment;
 import net.minecraft.enchantment.EnumEnchantmentType;
 import tb.common.enchantment.EnchantmentElderKnowledge;
 import tb.common.enchantment.EnchantmentEldritchBane;
 import tb.common.enchantment.EnchantmentMagicTouch;
 import tb.common.enchantment.EnchantmentTainted;
 import tb.common.enchantment.EnchantmentVaporising;
 import tb.utils.TBConfig;
 import thaumcraft.api.aspects.Aspect;
 import thaumcraft.api.aspects.AspectList;
 
 public class TBEnchant {
   public static Enchantment elderKnowledge;
   public static Enchantment eldritchBane;
   
   public static void setup() {
     elderKnowledge = (new EnchantmentElderKnowledge(TBConfig.elderKnowledgeID, 12, EnumEnchantmentType.weapon)).setName("elderKnowledge");
     eldritchBane = (new EnchantmentEldritchBane(TBConfig.eldritchBaneID, 7, EnumEnchantmentType.weapon)).setName("eldritchBane");
     magicTouch = (new EnchantmentMagicTouch(TBConfig.magicTouchID, 11, EnumEnchantmentType.weapon)).setName("magicTouch");
     tainted = (new EnchantmentTainted(TBConfig.taintedID, 9, EnumEnchantmentType.weapon)).setName("tainted");
     vaporising = (new EnchantmentVaporising(TBConfig.vaporisingID, 6, EnumEnchantmentType.weapon)).setName("vaporising");
     
     if (Loader.isModLoaded("ThaumicTinkerer") && TBConfig.enableTTCompathability)
       
       try {
         
         Class<?> c = Class.forName("thaumic.tinkerer.common.enchantment.core.EnchantmentManager");
         Method reg = c.getMethod("registerExponentialCostData", new Class[] { Enchantment.class, String.class, boolean.class, AspectList.class });
         reg.invoke(null, new Object[] { elderKnowledge, "thaumicbases:textures/enchantments/elder_knowledge.png", Boolean.valueOf(false), (new AspectList()).add(Aspect.AIR, 8).add(Aspect.ORDER, 12).add(Aspect.EARTH, 16) });
         reg.invoke(null, new Object[] { eldritchBane, "thaumicbases:textures/enchantments/eldritch_bane.png", Boolean.valueOf(false), (new AspectList()).add(Aspect.FIRE, 12).add(Aspect.ENTROPY, 12).add(Aspect.ORDER, 8) });
         reg.invoke(null, new Object[] { magicTouch, "thaumicbases:textures/enchantments/magic_touched.png", Boolean.valueOf(false), (new AspectList()).add(Aspect.FIRE, 16).add(Aspect.ORDER, 16).add(Aspect.WATER, 8).add(Aspect.ENTROPY, 12) });
         reg.invoke(null, new Object[] { tainted, "thaumicbases:textures/enchantments/tainted.png", Boolean.valueOf(false), (new AspectList()).add(Aspect.FIRE, 12).add(Aspect.ENTROPY, 16) });
         reg.invoke(null, new Object[] { vaporising, "thaumicbases:textures/enchantments/vaporising.png", Boolean.valueOf(false), (new AspectList()).add(Aspect.ENTROPY, 8).add(Aspect.ORDER, 12).add(Aspect.FIRE, 16) });
       }
       catch (Exception e) {
         
         System.out.println("[ThaumicBases]Unable to add ThaumicTinkerer integration - mod not found");
         return;
       }  
   }
   
   public static Enchantment magicTouch;
   public static Enchantment tainted;
   public static Enchantment vaporising;
 }


