package com.example.pvpmod.mixin;

import com.example.pvpmod.module.Module;
import com.example.pvpmod.module.ModuleManager;
import com.example.pvpmod.util.TpsTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null && !client.options.hudHidden) {
            
            int currentY = 5; // Tepadan boshlanadigan masofa
            int textColor = 0xFF6A0DAD; // Neon binafsha rang

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

            // 3. HUD MODULI (Koordinatalar va Yo'nalish - YANGI QO'SHILGAN QISM)
            for (Module mod : ModuleManager.getModules()) {
                if (mod.getName().equalsIgnoreCase("HUD") && mod.isEnabled()) {
                    
                    // O'yinchining X, Y, Z koordinatalarini butun songa aylantirib olamiz
                    int x = (int) client.player.getX();
                    int y = (int) client.player.getY();
                    int z = (int) client.player.getZ();
                    
                    // O'yinchining qaysi tomonga qaraganini aniqlaymiz (North, South, West, East)
                    Direction facing = client.player.getHorizontalFacing();
                    String directionName = facing.name().toUpperCase();

                    // Matnlarni tayyorlaymiz
                    String coordsText = String.format("XYZ: %d, %d, %d", x, y, z);
                    String facingText = "Facing: " + directionName;

                    // Ekranning pastki chap burchagi koordinatasini hisoblaymiz
                    int screenHeight = context.getScaledWindowHeight();
                    
                    // Koordinatalarni pastdan tepaga qarab chizamiz
                    context.drawTextWithShadow(client.textRenderer, Text.literal(coordsText), 5, screenHeight - 25, textColor);
                    context.drawTextWithShadow(client.textRenderer, Text.literal(facingText), 5, screenHeight - 15, textColor);
                    break;
                }
            }
        }
    }
}
