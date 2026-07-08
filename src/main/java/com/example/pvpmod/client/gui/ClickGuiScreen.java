package com.example.pvpmod.client.gui;

import com.example.pvpmod.module.Module;
import com.example.pvpmod.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class ClickGuiScreen extends Screen {

    public ClickGuiScreen() {
        super(Text.literal("Click GUI"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderInGameBackground(context);

        int startX = 20;
        int startY = 30;
        int frameWidth = 110;
        int frameHeight = 20;
        int spacing = 25;

        for (Module.Category category : Module.Category.values()) {
            // To'q binafsha rangli sarlavha paneli
            context.fill(startX, startY, startX + frameWidth, startY + frameHeight, 0xFF3A0066);
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(category.name()), startX + frameWidth / 2, startY + 6, 0xFFFFFFFF);

            List<Module> moduleList = ModuleManager.getModulesByCategory(category);
            int currentY = startY + frameHeight;

            for (Module mod : moduleList) {
                // Modul holatiga qarab rang (binafsha yoki tekis to'q qora)
                int buttonColor = mod.isEnabled() ? 0xCC5A0099 : 0xD8101010; 
                
                context.fill(startX, currentY, startX + frameWidth, currentY + frameHeight, buttonColor);
                
                // Yupqa chegara
                context.fill(startX, currentY, startX + frameWidth, currentY + 1, 0xFF4A0E4E);
                
                int textColor = mod.isEnabled() ? 0xFFFFFFFF : 0xFF999999;
                context.drawTextWithShadow(this.textRenderer, Text.literal(mod.getName()), startX + 5, currentY + 6, textColor);

                currentY += frameHeight;
            }

            // Pastki chiziq
            context.fill(startX, currentY, startX + frameWidth, currentY + 2, 0xFF3A0066);
            startX += frameWidth + spacing;
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int startX = 20;
        int startY = 30;
        int frameWidth = 110;
        int frameHeight = 20;
        int spacing = 25;

        if (button == 0) { 
            for (Module.Category category : Module.Category.values()) {
                List<Module> moduleList = ModuleManager.getModulesByCategory(category);
                int currentY = startY + frameHeight;

                for (Module mod : moduleList) {
                    if (mouseX >= startX && mouseX <= startX + frameWidth && mouseY >= currentY && mouseY <= currentY + frameHeight) {
                        mod.toggle(); 
                        return true;
                    }
                    currentY += frameHeight;
                }
                startX += frameWidth + spacing;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPauseGame() { return false; }
}

