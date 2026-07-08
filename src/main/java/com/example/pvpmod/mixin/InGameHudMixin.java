package com.example.pvpmod.mixin;

import com.example.pvpmod.module.Module;
import com.example.pvpmod.module.ModuleManager;
import com.example.pvpmod.util.TpsTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    /**
     * Minecraft 1.21.4 da ekranga HUD elementlarini chizish tugayotgan nuqtaga (TAIL) ulanamiz.
     * Bu biz chizayotgan matnlar o'yin elementlari ostida qolib ketmasligini ta'minlaydi.
     */
    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        // O'yinchi dunyoda bo'lsa va F3 standart menyusi yopiq bo'lsa ekran elementlarini chizamiz
        if (client.player != null && !client.options.hudHidden) {
            
            int currentY = 5; // Birinchi matnning tepadan joylashish masofasi (Y koordinatasi)
            int textColor = 0xFF6A0DAD; // Bizning neon binafsha rang kodimiz

            // 1. FPS COUNTER MODULINI TEKSHIRISH VA CHIZISH
            for (Module mod : ModuleManager.getModules()) {
                if (mod.getName().equalsIgnoreCase("FPS Counter") && mod.isEnabled()) {
                    int currentFps = MinecraftClient.getCurrentFps();
                    String fpsText = "FPS: " + currentFps;
                    
                    context.drawTextWithShadow(client.textRenderer, Text.literal(fpsText), 5, currentY, textColor);
                    currentY += 10; // Keyingi matn birinchisining ustiga tushmasligi uchun Y ni pastga suramiz
                    break;
                }
            }

            // 2. TPS MODULINI TEKSHIRISH VA CHIZISH
            for (Module mod : ModuleManager.getModules()) {
                if (mod.getName().equalsIgnoreCase("TPS") && mod.isEnabled()) {
                    String tpsText = String.format("TPS: %.1f", TpsTracker.getTps());
                    
                    context.drawTextWithShadow(client.textRenderer, Text.literal(tpsText), 5, currentY, textColor);
                    break;
                }
            }
        }
    }
}
