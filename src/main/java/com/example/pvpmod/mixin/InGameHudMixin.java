package com.example.pvpmod.mixin;

import com.example.pvpmod.module.Module;
import com.example.pvpmod.module.ModuleManager;
import com.example.pvpmod.util.TpsTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null && !client.options.hudHidden) {
            
            int currentY = 5; 
            int textColor = 0xFF6A0DAD; // Neon binafsha

            // 1. FPS COUNTER MODULI
            for (Module mod : ModuleManager.getModules()) {
                if (mod.getName().equalsIgnoreCase("FPS Counter") && mod.isEnabled()) {
                    int currentFps = MinecraftClient.getCurrentFps();
                    String fpsText = "FPS: " + currentFps;
                    context.drawTextWithShadow(client.textRenderer, Text.literal(fpsText), 5, currentY, textColor);
                    currentY += 10;
                    break;
                }
            }

            // 2. TPS MODULI
            for (Module mod : ModuleManager.getModules()) {
                if (mod.getName().equalsIgnoreCase("TPS") && mod.isEnabled()) {
                    String tpsText = String.format("TPS: %.1f", TpsTracker.getTps());
                    context.drawTextWithShadow(client.textRenderer, Text.literal(tpsText), 5, currentY, textColor);
                    break;
                }
            }

            // 3. HUD MODULI (Koordinatalar va Yo'nalish)
            for (Module mod : ModuleManager.getModules()) {
                if (mod.getName().equalsIgnoreCase("HUD") && mod.isEnabled()) {
                    int x = (int) client.player.getX();
                    int y = (int) client.player.getY();
                    int z = (int) client.player.getZ();
                    
                    Direction facing = client.player.getHorizontalFacing();
                    String directionName = facing.name().toUpperCase();

                    String coordsText = String.format("XYZ: %d, %d, %d", x, y, z);
                    String facingText = "Facing: " + directionName;

                    int screenHeight = context.getScaledWindowHeight();
                    context.drawTextWithShadow(client.textRenderer, Text.literal(coordsText), 5, screenHeight - 25, textColor);
                    context.drawTextWithShadow(client.textRenderer, Text.literal(facingText), 5, screenHeight - 15, textColor);
                    break;
                }
            }

            // 4. ARMOR HUD MODULI
            for (Module mod : ModuleManager.getModules()) {
                if (mod.getName().equalsIgnoreCase("Armor HUD") && mod.isEnabled()) {
                    List<ItemStack> itemsToRender = new ArrayList<>();
                    itemsToRender.add(client.player.getMainHandStack());
                    for (int i = 3; i >= 0; i--) {
                        itemsToRender.add(client.player.getInventory().getArmorStack(i));
                    }

                    int screenWidth = context.getScaledWindowWidth();
                    int screenHeight = context.getScaledWindowHeight();
                    int armorX = screenWidth / 2 + 95; 
                    int armorY = screenHeight - 22;    

                    for (ItemStack stack : itemsToRender) {
                        if (stack != null && !stack.isEmpty()) {
                            context.drawItem(stack, armorX, armorY);
                            context.drawItemInSlot(client.textRenderer, stack, armorX, armorY);
                            if (stack.isDamageable()) {
                                int maxDamage = stack.getMaxDamage();
                                int currentDamage = maxDamage - stack.getDamage();
                                context.drawTextWithShadow(client.textRenderer, Text.literal(String.valueOf(currentDamage)), armorX + 18, armorY + 4, 0xFFFFFFFF);
                            }
                            armorY -= 18;
                        }
                    }
                    break;
                }
            }

            // 5. INVENTORY HUD MODULI (YANGI QO'SHILGAN QISM)
            for (Module mod : ModuleManager.getModules()) {
                if (mod.getName().equalsIgnoreCase("Inventory HUD") && mod.isEnabled()) {
                    
                    int screenWidth = context.getScaledWindowWidth();
                    
                    // Panelning ekrandagi o'rni (O'ng tomonda, teparoqda joylashadi)
                    int invX = screenWidth - 170;
                    int invY = 35;
                    
                    int slotSize = 18;
                    int padding = 4;
                    
                    // Fon paneli: To'q shaffof qora fon va atrofida yupqa binafsha chegara
                    context.fill(invX - padding, invY - padding, invX + (9 * slotSize) + padding, invY + (3 * slotSize) + padding, 0x990A0A0A);
                    // Ustki neon binafsha dekoratsiya chizig'i
                    context.fill(invX - padding, invY - padding, invX + (9 * slotSize) + padding, invY - padding + 2, 0xFF6A0DAD);

                    // Minecraft inventaridagi asosiy 27 ta slot (9 dan 35 gacha bo'lgan indekslar)
                    for (int row = 0; row < 3; row++) {
                        for (int col = 0; col < 9; col++) {
                            int slotIndex = 9 + (row * 9) + col;
                            ItemStack stack = client.player.getInventory().getStack(slotIndex);
                            
                            int renderX = invX + (col * slotSize);
                            int renderY = invY + (row * slotSize);
                            
                            // Slot katakchalarining orqa foni (biroz seziladigan kulrang tekis to'rtburchak)
                            context.fill(renderX + 1, renderY + 1, renderX + slotSize - 1, renderY + slotSize - 1, 0x15FFFFFF);

                            if (stack != null && !stack.isEmpty()) {
                                // Predmet rasmini chizish
                                context.drawItem(stack, renderX, renderY);
                                // Predmet sonini (masalan, 64) chizish
                                context.drawItemInSlot(client.textRenderer, stack, renderX, renderY);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
}
