package com.example.pvpmod.client.gui;

import com.example.pvpmod.module.Module;
import com.example.pvpmod.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClickGuiScreen extends Screen {

    // VISUAL o'rniga PREVISUAL deb o'rnatildi
    private Module.Category currentCategory = Module.Category.PREVISUAL;

    public ClickGuiScreen() {
        super(Text.literal("Click GUI"));
    }

    @Override
    protected void init() {
        super.init();
        // Bu yerga agar tugmalar qo'shmoqchi bo'lsangiz, kod yozishingiz mumkin
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Orqa fonni standart qoraytirish
        this.renderBackground(context, mouseX, mouseY, delta);
        
        // Sarlavha chizish
        context.drawTextWithShadow(this.textRenderer, Text.literal("Purple Client - GUI"), 20, 20, 0xFF9D00FF);
        
        // Toifalarni va modullarni chizish mantiqi
        int yOffset = 50;
        for (Module module : ModuleManager.getModulesByCategory(currentCategory)) {
            String status = module.isEnabled() ? "[ON]" : "[OFF]";
            int color = module.isEnabled() ? 0xFF00FF00 : 0xFFFF0000;
            
            context.drawTextWithShadow(this.textRenderer, Text.literal(module.getName() + " " + status), 30, yOffset, color);
            yOffset += 15;
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Sichqoncha bosilganda modullarni yoqib-o'chirish mantiqi shu yerda bo'ladi
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false; // GUI ochilganda o'yin to'xtab qolmaydi (multiplayer uchun muhim)
    }
}
