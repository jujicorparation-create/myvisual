package com.example.pvpmod.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class AltManagerScreen extends Screen {
    private final Screen parent;

    public AltManagerScreen(Screen parent) {
        super(Text.literal("Alt Manager"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // Orqaga qaytish tugmasi
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Orqaga"), button -> this.client.setScreen(parent))
                .dimensions(this.width / 2 - 100, this.height - 40, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderInGameBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, "Alt Manager — Tez kunda akkaunt almashtirish tizimi...", this.width / 2, this.height / 2, 0xFFFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
