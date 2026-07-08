package com.example.pvpmod.mixin;

import com.example.pvpmod.module.Module;
import com.example.pvpmod.module.ModuleManager;
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

    // 1.21.4 da ekranga HUD elementlarini chizish metodi "render" deb nomlanadi
    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Agar o'yinchi dunyoda bo'lsa va F3 bosib ochiladigan standart menyu yopiq bo'lsa
        if (client.player != null && !client.options.hudHidden) {
            
            // Modullar ichidan FPS Counter yoqilganligini tekshiramiz
            for (Module mod : ModuleManager.getModules()) {
                if (mod.getName().equalsIgnoreCase("FPS Counter") && mod.isEnabled()) {
                    
                    // Minecraft'dan joriy FPS qiymatini olamiz
                    int currentFps = MinecraftClient.getCurrentFps();
                    
                    // Ekranga chiqadigan matn (Masalan: "FPS: 144")
                    String fpsText = "FPS: " + currentFps;
                    
                    // Ekrandagi koordinatalari: X=5 (chapdan), Y=5 (tepadan)
                    // Rang: 0xFF6A0DAD (Bizning sevimli neon binafsha rangimiz)
                    context.drawTextWithShadow(
                            client.textRenderer, 
                            Text.literal(fpsText), 
                            5, 5, 
                            0xFF6A0DAD
                    );
                    break;
                }
            }
        }
    }
}

