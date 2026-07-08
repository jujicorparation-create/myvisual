package com.example.pvpmod.mixin;

import com.example.pvpmod.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class RenderModificationsMixin {
    
    @Shadow private int itemUseCooldown;

    // FastPlace: Blok qo'yish o'rtasidagi kechikishni nolga tushiradi
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (ModuleManager.getModules().stream().anyMatch(m -> m.getName().equalsIgnoreCase("FastPlace") && m.isEnabled())) {
            // Agar modul yoqilgan bo'lsa, o'yin ichidagi buyum ishlatish "cooldown"ini doim 0 qilamiz
            this.itemUseCooldown = 0;
        }
    }
}

