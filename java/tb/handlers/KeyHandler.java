package tb.handlers;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import tb.core.TBCore;
import tb.utils.DummySteele;
import thaumcraft.client.lib.ClientTickEventsFML;

public class KeyHandler {

	public static final int DEBUG_KEY = 0;
	public static final int TEST_KEY = 1;
	
	public static boolean isDebugActive = false;
	public static boolean isTestActive = false;
	
	public static long lastKeyPressed = 0;

	private static final String[] keyDesc = {"key.tbDebugKey.desc", "key.tbTestKey.desc"};
	private static final int[] keyValues = {Keyboard.KEY_M, Keyboard.KEY_K};
	private static KeyBinding[] keys = {};

	public KeyHandler() {
		keys = new KeyBinding[keyValues.length];
		for (int i = 0; i < keyValues.length; i++) {
			keys[i] = new KeyBinding(keyDesc[i], keyValues[i], "key.tbCategory.name");
			ClientRegistry.registerKeyBinding(keys[i]);
			//DummySteele.SpamDebugMessage(5, "Registered key: " + keys[i].getKeyDescription());
		}
	}

	@SideOnly(value=Side.CLIENT)
	@SubscribeEvent
	public void TBKeyAction(PlayerTickEvent event) {

		if (keys[DEBUG_KEY].getIsKeyPressed() && isDebugActive == false && (event.player.worldObj.getTotalWorldTime() - lastKeyPressed > 20)) {
			isDebugActive = true;
			//TBCore.TBLogger.log(Level.INFO, "Test... TWO");
			lastKeyPressed = event.player.worldObj.getTotalWorldTime();
		}
		else if (keys[DEBUG_KEY].getIsKeyPressed() && isDebugActive == true && (event.player.worldObj.getTotalWorldTime() - lastKeyPressed > 20)) {
			isDebugActive = false;
			//TBCore.TBLogger.log(Level.INFO, "Test... TREE");
			lastKeyPressed = event.player.worldObj.getTotalWorldTime();
		}

	}
	
	public static boolean getKeyPressed(int key) {
		return keys[key].getIsKeyPressed();
	}
	
	@SubscribeEvent
	public void testTick(TickEvent.ClientTickEvent event) {
		if (keys[TEST_KEY].getIsKeyPressed()) {
			TBCore.TBLogger.log(Level.INFO, "test");
			isTestActive = true;
		}
		else {
			isTestActive = false;
		}
	}

}
