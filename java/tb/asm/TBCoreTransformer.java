package tb.asm;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import net.minecraft.world.NextTickListEntry;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import scala.collection.parallel.ParIterableLike.Forall;
import scala.reflect.internal.Trees.New;
import tb.common.block.TBBlock;
import tb.core.TBCore;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.common.Thaumcraft;

public class TBCoreTransformer implements IClassTransformer {

	static boolean isDeob;
	private static int[] WGBytePattern = {Opcodes.NEW};
	public static boolean inPatchingMethod = false;
	public static boolean inTooltipMethod = false;


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



	//	private byte[] patchToolTips(byte[] origCode, boolean deobf)
	//	{
	//		TBCore.TBLogger.log(Level.INFO, "[CORE] Patching test nothing here");
	//
	//		final String methodToPatch = "getTooltip";
	//		final String methodToPatch_srg = "func_70670_a";
	//		final String methodToPatch_obf = "a";
	//		final String desc1 = "(Lnet/minecraftforge/event/entity/player/ItemTooltipEvent;)V";
	//		final String desc1_obf = "(Lrw;)V";
	//		final String desc2 = deobf?"(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/potion/PotionEffect;)V":"(Lsv;Lrw;)V";
	//
	//		ClassReader cr = new ClassReader(origCode);
	//
	//		ClassNode classNode=new ClassNode();
	//		cr.accept(classNode, 0);
	//		for(MethodNode methodNode : classNode.methods)
	//			if( (methodNode.name.equals(methodToPatch)||methodNode.name.equals(methodToPatch_srg)||methodNode.name.equals(methodToPatch_obf)) && (methodNode.desc.equals(desc1)||methodNode.desc.equals(desc1_obf)))
	//			{
	//				Iterator<AbstractInsnNode> insnNodes=methodNode.instructions.iterator();
	//				while(insnNodes.hasNext())
	//				{
	//					AbstractInsnNode insn=insnNodes.next();
	//					if(insn.getOpcode()==Opcodes.ALOAD) 
	//					{
	//						InsnList endList=new InsnList();
	//						//endList.add(new VarInsnNode(Opcodes.ALOAD, 0));
	//						endList.add(new VarInsnNode(Opcodes.ALOAD, 1));
	//						endList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "tb/asm/TBCoreTransformer", "makeToolTip", desc1, false));
	//						methodNode.instructions.insertBefore(insn, endList);
	//
	//					}
	//				}
	//			}
	//		ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
	//		classNode.accept(cw);
	//
	//		return cw.toByteArray();
	//	}


	private byte[] patchToolTips(byte[] origCode, boolean isDeob) {

		System.out.println("+++++++++In patching method+++++++++");
		inPatchingMethod = true;

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(origCode);
		classReader.accept(classNode, 0);


		int interfaceCounter = 0;
		List<Integer> interfaceIndexList = new ArrayList<Integer>();

		Iterator<MethodNode> methods = classNode.methods.iterator();
		while(methods.hasNext()) {
			MethodNode m = methods.next();

			if (m.name.equals("getTooltip")) {
				System.out.println("+++++++++Found WG tooltip Method+++++++++");
				inTooltipMethod = true;
				AbstractInsnNode currentNode = null;
				AbstractInsnNode targetNode = null;
				int popIndex = -1;
				Iterator<AbstractInsnNode> iter = m.instructions.iterator();
				int index = -1;
				while (iter.hasNext()) {
					index++;
					currentNode = iter.next();

					if (currentNode.getOpcode() == Opcodes.INVOKEINTERFACE) { //need interface 2 & 3
						targetNode = currentNode;
						popIndex = index;
						interfaceCounter++;
						interfaceIndexList.add(index);
						//System.out.println("-----------------Have pop index-----------------");
						//System.out.println(targetNode.getOpcode());
						//System.out.println(popIndex);
						//break;
					}
				}


				System.out.println("INVOKEINTERFACE count: " + interfaceCounter);
				//interfaceIndexList.remove(0);
				System.out.println("List Size: " + interfaceIndexList.size());

				for (int i = 0; i < interfaceIndexList.size(); i++) {
					System.out.println(interfaceIndexList.get(i));
				}
				//				
				//				System.out.println("New List Size: " + interfaceIndexList.size());

				//				List<AbstractInsnNode> remNodes = new ArrayList<AbstractInsnNode>();
				//				for (int i = 0; i < 10; i++) {
				//					//remNodes.add(m.instructions.get(i + 1));
				//					//System.out.println(i + popIndex + 1);
				//					//System.out.println(m.instructions.get(i + popIndex).getOpcode());
				//					//m.instructions.remove(m.instructions.get(i + popIndex));
				//				}

				//				m.instructions.remove(m.instructions.get(interfaceIndexList.get(1)));
				//				m.instructions.remove(m.instructions.get(interfaceIndexList.get(1) + 1));
				//				m.instructions.remove(m.instructions.get(interfaceIndexList.get(1) - 1));
				//				m.instructions.remove(m.instructions.get(interfaceIndexList.get(1) - 2));
				//				m.instructions.remove(m.instructions.get(interfaceIndexList.get(1) - 3));
				//				m.instructions.remove(m.instructions.get(interfaceIndexList.get(1) - 4));
				//				m.instructions.remove(m.instructions.get(interfaceIndexList.get(1) - 5));
				//				m.instructions.remove(m.instructions.get(interfaceIndexList.get(1) - 6));
				//				m.instructions.remove(m.instructions.get(interfaceIndexList.get(1) - 7));
				//				m.instructions.remove(m.instructions.get(interfaceIndexList.get(1) - 8));
				//				m.instructions.remove(m.instructions.get(interfaceIndexList.get(1) - 9));
				//				m.instructions.remove(m.instructions.get(interfaceIndexList.get(1) - 10));

				//35
				for (int i = 1; i >= -35; i--) {
					m.instructions.remove(m.instructions.get(interfaceIndexList.get(2) + i)); //remove WG tooltip for blocks
				}

				for (int i = 1; i >= -10; i--) {
					m.instructions.remove(m.instructions.get(interfaceIndexList.get(1) + i)); //remove EG tooltip for skulls
				}






				//				m.instructions.remove(m.instructions.get(interfaceIndexList.get(2)));
				//				m.instructions.remove(m.instructions.get(interfaceIndexList.get(2) + 1));




				break;
			}
		}

		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(classWriter);

		return classWriter.toByteArray();

	}



	public static void makeToolTip(ItemTooltipEvent event) {
		TBCore.TBLogger.log(Level.INFO, "THIS IS A TEST MESSAGE");
	}






}
