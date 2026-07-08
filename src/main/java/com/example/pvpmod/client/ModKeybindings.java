package com.example.pvpmod.client;

import com.example.pvpmod.client.gui.ClickGuiScreen;
import com.example.pvpmod.module.Module;
import com.example.pvpmod.module.ModuleManager;
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

        // Har bir o'yin tick'ida (sekundiga 20 marta) tekshirish
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            
            // 1. Menu ochish tugmasini tekshirish
            while (openMenuKey.wasPressed()) {
                if (client.player != null && client.currentScreen == null) {
                    client.setScreen(new ClickGuiScreen());
                }
            }

            // 2. MOVEMENT -> AutoSprint mantiqi
            if (client.player != null) {
                // Modullar ichidan AutoSprint yoqilganligini tekshiramiz
                boolean isAutoSprintEnabled = ModuleManager.getModules().stream()
                        .anyMatch(m -> m.getName().equalsIgnoreCase("AutoSprint") && m.isEnabled());
                
                if (isAutoSprintEnabled) {
                    // Agar o'yinchi oldinga yurayotgan bo'lsa, engashmagan (sneak) va biror narsa yeyotgan bo'lmasa
                    if (client.player.input.pressingForward && !client.player.isSneaking() && !client.player.isUsingItem()) {
                        // O'yinchini majburiy tez yugurish rejimiga o'tkazamiz
                        client.player.setSprinting(true);
                    }
                }
            }
        });
    }
}
