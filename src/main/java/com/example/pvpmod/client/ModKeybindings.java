package com.example.pvpmod.client;

import com.example.pvpmod.client.gui.ClickGuiScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeybindings {
    private static KeyBinding CONFIG_KEY;

    public static void init() {
        // Right Shift tugmasi orqali GUI menyuni ochish bindingi
        CONFIG_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pvpmod.config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.pvpmod.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (CONFIG_KEY.wasPressed()) {
                client.setScreen(new ClickGuiScreen());
            }

            // Agar o'yinchi harakatlarini tekshiradigan boshqa funksiyalar bo'lsa:
            if (client.player != null) {
                // pressingForward o'rniga xavfsiz tekshirish
                boolean isMovingForward = client.options.forwardKey.isPressed();
                // Kerakli mantiqni shu yerda davom ettirish mumkin
            }
        });
    }
}
