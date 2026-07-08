package com.example.pvpmod.client;

import com.example.pvpmod.client.gui.ClickGuiScreen;
import com.example.pvpmod.module.Module;
import com.example.pvpmod.module.ModuleManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

public class ModKeybindings {
    public static KeyBinding openMenuKey;

    public static void register() {
        // Menu ochish tugmasi (Right-Shift)
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pvpmod.open_menu", 
                InputUtil.Type.KEYSYM, 
                GLFW.GLFW_KEY_RIGHT_SHIFT, 
                "category.pvpmod.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.interactionManager == null) return;

            // 1. MENU TUGMASINI TEKSHIRISH
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ClickGuiScreen());
                }
            }

            // 2. AUTO SPRINT MODULI
            boolean isAutoSprint = ModuleManager.getModules().stream()
                    .anyMatch(m -> m.getName().equalsIgnoreCase("AutoSprint") && m.isEnabled());
            if (isAutoSprint) {
                if (client.player.input.pressingForward && !client.player.isSneaking() && !client.player.isUsingItem()) {
                    client.player.setSprinting(true);
                }
            }

            // 3. AUTO TOTEM MODULI
            boolean isAutoTotem = ModuleManager.getModules().stream()
                    .anyMatch(m -> m.getName().equalsIgnoreCase("AutoTotem") && m.isEnabled());
            if (isAutoTotem) {
                // Agar chap qo'lda totem bo'lmasa
                if (client.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
                    // Inventardan totem izlaymiz
                    int totemSlot = findItemInInventory(client.player.getInventory());
                    if (totemSlot != -1) {
                        // Totem topilsa, uni tezda chap qo'l slotiga (slot id: 45) o'tkazamiz
                        int windowId = client.player.currentScreenHandler.syncId;
                        
                        // Inventardagi slot indeksini ekran interfeysi indeksiga moslaymiz
                        int slotId = totemSlot < 9 ? totemSlot + 36 : totemSlot;
                        
                        // Paket orqali tezkor klik operatsiyasini bajaramiz
                        client.interactionManager.clickSlot(windowId, slotId, 40, SlotActionType.SWAP, client.player);
                    }
                }
            }

            // 4. AUTO GAPPLE MODULI
            boolean isAutoGapple = ModuleManager.getModules().stream()
                    .anyMatch(m -> m.getName().equalsIgnoreCase("AutoGapple") && m.isEnabled());
            if (isAutoGapple) {
                // Sogliq 10 tadan (5 ta yurak) pastga tushsa va qo'lda gapple bo'lmasa
                if (client.player.getHealth() <= 10.0f && client.player.getMainHandStack().getItem() != Items.GOLDEN_APPLE) {
                    int gappleSlot = findGappleInInventory(client.player.getInventory());
                    if (gappleSlot != -1 && gappleSlot < 9) {
                        // Agar gapple hotbarda bo'lsa, o'sha slotga avtomatik o'tadi
                        client.player.getInventory().selectedSlot = gappleSlot;
                    }
                }
            }
        });
    }

    // Inventardan totem slotini topish funksiyasi
    private static int findItemInInventory(PlayerInventory inventory) {
        for (int i = 0; i < 36; i++) {
            if (inventory.getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                return i;
            }
        }
        return -1;
    }

    // Inventardan gapple slotini topish funksiyasi
    private static int findGappleInInventory(PlayerInventory inventory) {
        for (int i = 0; i < 36; i++) {
            if (inventory.getStack(i).getItem() == Items.GOLDEN_APPLE || inventory.getStack(i).getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
                return i;
            }
        }
        return -1;
    }
}
