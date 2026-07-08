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

            // 4. ARMOR HUD MODULI (YANGI QO'SHILGAN QISM)
            for (Module mod : ModuleManager.getModules()) {
                if (mod.getName().equalsIgnoreCase("Armor HUD") && mod.isEnabled()) {
                    
                    // Chizilishi kerak bo'lgan narsalar ro'yxati (Asosiy qo'l, va 4 ta bronya)
                    List<ItemStack> itemsToRender = new ArrayList<>();
                    itemsToRender.add(client.player.getMainHandStack());
                    
                    // Bronyalarni tartib bo'yicha olamiz (Shlem, Nagrudnik, Shim, Etik)
                    for (int i = 3; i >= 0; i--) {
                        itemsToRender.add(client.player.getInventory().getArmorStack(i));
                    }

                    // Ekrandagi joylashuv koordinatalari (Hotbar-ning o'ng tomonida chizamiz)
                    int screenWidth = context.getScaledWindowWidth();
                    int screenHeight = context.getScaledWindowHeight();
                    
                    int armorX = screenWidth / 2 + 95; // Hotbardan o'ngroqda
                    int armorY = screenHeight - 22;    // Hotbar bilan bir xil balandlikda

                    for (ItemStack stack : itemsToRender) {
                        if (stack != null && !stack.isEmpty()) {
                            // 1. Bronya/Qurol ikonkasini ekranga chizish
                            context.drawItem(stack, armorX, armorY);
                            // 2. Chidamlilik panelini (durability bar) chizish
                            context.drawItemInSlot(client.textRenderer, stack, armorX, armorY);

                            // 3. Agar narsa sinadigan bo'lsa (qurol yoki bronya), qolgan chidamlilik sonini yozish
                            if (stack.isDamageable()) {
                                int maxDamage = stack.getMaxDamage();
                                int currentDamage = maxDamage - stack.getDamage();
                                String durText = String.valueOf(currentDamage);
                                
                                // Matnni ikonkaning o'ng tomoniga chizamiz
                                context.drawTextWithShadow(client.textRenderer, Text.literal(durText), armorX + 18, armorY + 4, 0xFFFFFFFF);
                            }
                            
                            // Keyingi bronya elementi uchun Y koordinatasini tepaga suramiz (vertikal ustun)
                            armorY -= 18;
                        }
                    }
                    break;
                }
            }
        }
    }
}
