package com.example.pvpmod.client;

import com.example.pvpmod.client.gui.ClickGuiScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeybindings {
    public static KeyBinding openMenuKey;

    public static void register() {
        // Right-Shift tugmasini o'yinga qo'shish
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pvpmod.open_menu", 
                InputUtil.Type.KEYSYM, 
                GLFW.GLFW_KEY_RIGHT_SHIFT, 
                "category.pvpmod.general"
        ));

        // Har tickda tugma bosilganini tekshirish
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.player != null && client.currentScreen == null) {
                    client.setScreen(new ClickGuiScreen());
                }
            }
        });
    }
}

