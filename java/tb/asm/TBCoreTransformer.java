package tb.asm;


import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;


import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.steelehook.SteeleCore.Handlers.Logging;
import scala.collection.parallel.ParIterableLike.Forall;
import scala.reflect.internal.Trees.New;
import tb.common.block.TBBlock;
import tb.core.TBCore;
import thaumcraft.api.crafting.IInfusionStabiliser;

public class TBCoreTransformer implements IClassTransformer {

	static boolean isDeob;
	
	@Override
	public byte[] transform(String className, String newClassName, byte[] origCode) {
		isDeob = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
		if (className.equals("witchinggadgets.client.ClientEventHandler")) {
			return patchToolTips(/*className,*/ origCode, isDeob);
		} else {
//			Logging.writeToConsole(Level.INFO, "class not found");
//			for(int i = 0;i < 10; i++) {
//				Logging.writeToConsole(Level.INFO, "");
//			}
			//TBCore.TBLogger.log(Level.INFO, "Class not found");
		}
		
		return origCode;
	}

//	private byte[] patchToolTips(String className, byte[] origCode, boolean isDeob2) {
//		TBCore.TBLogger.log(Level.INFO, "Patching WG tooltips");
//		for(int i = 0;i < 10; i++) {
//			TBCore.TBLogger.log(Level.INFO, "...");
//		}
//		TBCore.TBLogger.log(Level.INFO, String.valueOf(isDeob2));
//		
//		//import net.minecraftforge.event.entity.player.ItemTooltipEvent;
//		
//		final String ttString = "getTooltip";
//		String nameString = ttString;
//		String descString = "(Lnet/minecraftforge/event/entity/player/ItemTooltipEvent;)V";
//		
//		ClassNode classNode = new ClassNode();		
//		ClassReader cReader = new ClassReader(origCode);
//		cReader.accept(classNode, 0);
//		
//		for (MethodNode m : classNode.methods) {
//			if (m.name.equals(nameString)) {
//				TBCore.TBLogger.log(Level.INFO, "Method does exist in findings");
//				AbstractInsnNode targetNode = null;
//				
//				java.util.Iterator<AbstractInsnNode> insNodes = m.instructions.iterator();
//				while(insNodes.hasNext()) {
//					AbstractInsnNode insn=insNodes.next();
//					
//					if(insn.getOpcode()==Opcodes.IRETURN
//							||insn.getOpcode()==Opcodes.RETURN
//							||insn.getOpcode()==Opcodes.ARETURN
//							||insn.getOpcode()==Opcodes.LRETURN
//							||insn.getOpcode()==Opcodes.DRETURN) {
//						m.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKEDYNAMIC, "tb/asm/TBCoreTransformer", "TestMethod", descString, false));
//					}
//				}
//				
//				
////				for (AbstractInsnNode inst : m.instructions.toArray()) {
////					if (inst.getOpcode() == Opcodes.NEW) {
////						if (inst.getOpcode() == Opcodes.NEW) {
////							if (inst.getNext().getNext().getOpcode() == Opcodes.GETSTATIC) {
////								targetNode = inst;
////								break;
////							}
////						}
////					}
////				}
//			}
//		}
//		
//		ClassWriter cWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//		
////		MethodVisitor mVisitor = cWriter.visitMethod(Opcodes.ACC_PUBLIC, nameString, descString, null, null);
////		mVisitor.visitCode();
////		mVisitor.visitVarInsn(Opcodes.ALOAD, 1);
////		mVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "tb/asm/TBCoreTransformer", "makeToolTip", descString, false);
////		mVisitor.visitInsn(Opcodes.RETURN);
////		mVisitor.visitMaxs(32, 1);
////		mVisitor.visitEnd();
//		
//		
//		
//		classNode.accept(cWriter);
//		return cWriter.toByteArray();
//		//return origCode;
//	}
	
	
	
	private byte[] patchToolTips(byte[] origCode, boolean deobf)
	{
		TBCore.TBLogger.log(Level.INFO, "[CORE] Patching test nothing here");

		final String methodToPatch = "getTooltip";
		final String methodToPatch_srg = "func_70670_a";
		final String methodToPatch_obf = "a";
		final String desc1 = "(Lnet/minecraftforge/event/entity/player/ItemTooltipEvent;)V";
		final String desc1_obf = "(Lrw;)V";
		final String desc2 = deobf?"(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/potion/PotionEffect;)V":"(Lsv;Lrw;)V";

		ClassReader cr = new ClassReader(origCode);

		ClassNode classNode=new ClassNode();
		cr.accept(classNode, 0);
		for(MethodNode methodNode : classNode.methods)
			if( (methodNode.name.equals(methodToPatch)||methodNode.name.equals(methodToPatch_srg)||methodNode.name.equals(methodToPatch_obf)) && (methodNode.desc.equals(desc1)||methodNode.desc.equals(desc1_obf)))
			{
				Iterator<AbstractInsnNode> insnNodes=methodNode.instructions.iterator();
				while(insnNodes.hasNext())
				{
					AbstractInsnNode insn=insnNodes.next();
					if(insn.getOpcode()==Opcodes.IRETURN
							||insn.getOpcode()==Opcodes.RETURN
							||insn.getOpcode()==Opcodes.ARETURN
							||insn.getOpcode()==Opcodes.LRETURN
							||insn.getOpcode()==Opcodes.DRETURN)
					{
						InsnList endList=new InsnList();
						//endList.add(new VarInsnNode(Opcodes.ALOAD, 0));
						endList.add(new VarInsnNode(Opcodes.ALOAD, 1));
						endList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "tb/asm/TBCoreTransformer", "makeToolTip", desc1, false));
						methodNode.instructions.insertBefore(insn, endList);
					}
				}
			}
		ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(cw);

		return cw.toByteArray();
	}
	
	
	
	
	public void TestMethod(ItemTooltipEvent event) {
		TBCore.TBLogger.log(Level.INFO, "THIS IS A TEST MESSAGE");
	}
	

	public static void makeToolTip(ItemTooltipEvent event) {
		if(event.itemStack.getItem().equals(Items.skull))
			event.toolTip.add(StatCollector.translateToLocal("hello from tb asm"));
		else if(Block.getBlockFromItem(event.itemStack.getItem())!=null)
			for(Method m : Block.getBlockFromItem(event.itemStack.getItem()).getClass().getMethods())
				if(m.getName().endsWith("canStabaliseInfusion")) {
					
					
					
					event.toolTip.add(StatCollector.translateToLocal("has method"));
					Block sBlock = Block.getBlockFromItem(event.itemStack.getItem());
					
					IInfusionStabiliser conv = (IInfusionStabiliser) sBlock;
					
					
					event.toolTip.add(StatCollector.translateToLocal("Can stabalize: " + String.valueOf(conv.canStabaliseInfusion(event.entity.worldObj, 0, 0, 0))));
				}
					
					
	}



}
