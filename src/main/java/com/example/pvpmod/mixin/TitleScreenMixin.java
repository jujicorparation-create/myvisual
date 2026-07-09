package com.example.pvpmod.mixin;

import com.example.pvpmod.client.gui.AltManagerScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    private static final Identifier CUSTOM_BACKGROUND = Identifier.of("pvpmod", "textures/gui/background.png");

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void onInit(CallbackInfo ci) {
        ci.cancel();

        int btnWidth = 160;
        int btnHeight = 24;
        int startX = 40; 
        int startY = this.height / 2 - 60; 

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Singleplayer"), button -> 
                this.client.setScreen(new SelectWorldScreen(this)))
                .dimensions(startX, startY, btnWidth, btnHeight).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Multiplayer"), button -> 
                this.client.setScreen(new MultiplayerScreen(this)))
                .dimensions(startX, startY + 30, btnWidth, btnHeight).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("AltManager"), button -> 
                this.client.setScreen(new AltManagerScreen(this)))
                .dimensions(startX, startY + 60, btnWidth, btnHeight).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Settings"), button -> 
                this.client.setScreen(new OptionsScreen(this, this.client.options)))
                .dimensions(startX, startY + 90, btnWidth, btnHeight).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Quit Game"), button -> 
                this.client.scheduleStop())
                .dimensions(startX, startY + 120, btnWidth, btnHeight).build());
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ci.cancel();

        // 1.21+ uchun mutlaqo xatosiz, universal to'liq ekranli fon chizish metodi:
        context.drawGuiTexture(CUSTOM_BACKGROUND, 0, 0, this.width, this.height);
        
        // Shaffof qora yon panel (Tugmalar foni)
        context.fill(0, 0, 240, this.height, 0x55000000);
        
        // Sarlavha matni
        context.drawTextWithShadow(this.textRenderer, Text.literal("PURPLE PvP CLIENT"), 40, this.height / 2 - 95, 0xFF9D00FF);

        super.render(context, mouseX, mouseY, delta);
    }
}
