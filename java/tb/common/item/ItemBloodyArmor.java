 package tb.common.item;
 
 import com.google.common.collect.HashMultimap;
 import com.google.common.collect.Multimap;
 import java.util.List;
 import java.util.UUID;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.SharedMonsterAttributes;
 import net.minecraft.entity.ai.attributes.AttributeModifier;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.ItemArmor;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.EnumChatFormatting;
 import net.minecraft.util.StatCollector;
 import thaumcraft.api.IRepairable;
 import thaumcraft.api.IRunicArmor;
 import thaumcraft.api.IVisDiscountGear;
 import thaumcraft.api.aspects.Aspect;
 
 public class ItemBloodyArmor
   extends ItemArmor
   implements IRepairable, IVisDiscountGear, IRunicArmor
 {
   int aType;
   
   public ItemBloodyArmor(ItemArmor.ArmorMaterial mat, int aType) {
     super(mat, 0, aType);
     this.aType = aType;
   }
 
   
   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
     return (slot == 2) ? "thaumicbases:textures/items/armor/bloody/bloody_2.png" : "thaumicbases:textures/items/armor/bloody/bloody_1.png";
   }
 
 
   
   public int getVisDiscount(ItemStack stack, EntityPlayer player, Aspect aspect) {
     return discount[this.aType];
   }
   
   static final int[] discount = new int[] { 5, 4, 3, 2 };
 
   
   public int getRunicCharge(ItemStack itemstack) {
     return 0;
   }
 
 
   
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
     list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": " + getVisDiscount(stack, player, null) + "%");
     super.addInformation(stack, player, list, par4);
   }
 
 
   
   public Multimap getAttributeModifiers(ItemStack stack) {
     HashMultimap map = HashMultimap.create();
     
     switch (this.aType) {
 
       
       case 1:
         map.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(UUID.fromString("96042c45-dfe3-4366-b93b-84663c4d828d"), "maxHealth", 0.20000000298023224D, 2));
         break;
 
       
       case 2:
         map.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(UUID.fromString("e4e1d8b2-87f2-44f5-8f24-e1876060a04c"), "knockback", 0.5D, 2));
         break;
 
       
       case 3:
         map.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), new AttributeModifier(UUID.fromString("f6d1384c-74c3-4cce-9a80-11b91dbd4ff4"), "moveSpeed", 0.10000000149011612D, 2));
         break;
     } 
 
     
     return (Multimap)map;
   }
 }


