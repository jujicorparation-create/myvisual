package com.example.pvpmod.mixin;

import com.example.pvpmod.PvpMod;
import com.example.pvpmod.module.ModuleManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    private void onIsGlowing(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        
        // Agar Target Visual moduli yoqilgan bo'lsa va bu entity biz urayotgan dushman bo'lsa
        if (ModuleManager.getModules().stream().anyMatch(m -> m.getName().equalsIgnoreCase("Target Visual") && m.isEnabled())) {
            if (PvpMod.currentTarget != null && PvpMod.currentTarget.getId() == entity.getId()) {
                // Uni majburiy yorqin (Glow) holatga o'tkazamiz
                cir.setReturnValue(true);
            }
        }
    }
}

