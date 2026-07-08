package com.example.pvpmod.mixin;

import com.example.pvpmod.PvpMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow public HitResult crosshairTarget;

    @Inject(method = "doAttack", at = @At("HEAD"))
    private void onDoAttack(CallbackInfoReturnable<Boolean> cir) {
        // Agar o'yinchi biron bir tirik jonzotga/o'yinchiga hujum qilsa
        if (crosshairTarget != null && crosshairTarget.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) crosshairTarget;
            // O'sha dushmanni nishon sifatida saqlab qo'yamiz
            PvpMod.currentTarget = entityHitResult.getEntity();
        }
    }
}

