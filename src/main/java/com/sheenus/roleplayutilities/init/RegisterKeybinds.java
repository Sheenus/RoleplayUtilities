package com.sheenus.roleplayutilities.init;

import org.lwjgl.input.Keyboard;

import com.sheenus.roleplayutilities.RoleplayUtilities;
import com.sheenus.roleplayutilities.gui.GuiHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class RegisterKeybinds {
	
	private static Minecraft mc = Minecraft.getMinecraft();
    private static KeyBinding refKey = new KeyBinding("key.reference", Keyboard.KEY_BACKSLASH, "key.category");

    public RegisterKeybinds() {
        ClientRegistry.registerKeyBinding(refKey);
    }

    @SubscribeEvent
    public void onKey(KeyInputEvent event) {
        if (mc.player != null && mc.world != null && refKey.isPressed()) {
            mc.player.openGui(RoleplayUtilities.instance, GuiHandler.MAIN_REFERENCE_ID, mc.world, 0, 0, 0);
        }
    }
}
