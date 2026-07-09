package com.example.pvpmod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        if (client.options.hudHidden) return;

        // 1. FPS ko'rsatkichini to'g'ri olish (Static xatolik tuzatildi)
        int currentFps = client.getCurrentFps();
        context.drawTextWithShadow(client.textRenderer, "FPS: " + currentFps, 10, 10, 0xFFFFFFFF);

        // 2. Armor HUD uchun drawItemInSlot o'rniga drawItem ishlatildi
        int armorX = 10;
        int armorY = 30;
        for (ItemStack stack : client.player.getInventory().armor) {
            if (!stack.isEmpty()) {
                context.drawItem(stack, armorX, armorY);
                armorX += 18;
            }
        }
    }
}
