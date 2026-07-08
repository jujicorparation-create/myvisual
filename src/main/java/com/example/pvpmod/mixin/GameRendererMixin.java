package com.example.pvpmod.mixin;

import com.example.pvpmod.module.Module;
import com.example.pvpmod.module.ModuleManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    // Minecraft 1.21.4 da dushmandan zarar ko'rgandagi kamerani qiyshaytirish metodi
    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void onTiltViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        // Modullarimiz ichidan "No HurtCam" modulini izlaymiz
        for (Module mod : ModuleManager.getModules()) {
            if (mod.getName().equalsIgnoreCase("No HurtCam") && mod.isEnabled()) {
                // Agar modul menyuda yoqilgan bo'lsa, kameraning tebranish mantiqini bekor qilamiz
                ci.cancel();
                break;
            }
        }
    }
}

