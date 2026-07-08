package com.example.pvpmod.mixin;

import com.example.pvpmod.module.ModuleManager;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    
    // O'yinda har qanday zarracha (tutun, portlash, olov effekti) paydo bo'layotganda uni ushlaymiz
    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    private void onAddParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir) {
        if (ModuleManager.getModules().stream().anyMatch(m -> m.getName().equalsIgnoreCase("NoRender") && m.isEnabled())) {
            // NoRender yoqilgan bo'lsa, zarralarni yaratishni rad etamiz (null qaytaramiz)
            // Bu portlash va PvP effektlarida FPS keskin tushib ketishini oldini oladi
            cir.setReturnValue(null);
        }
    }
}

