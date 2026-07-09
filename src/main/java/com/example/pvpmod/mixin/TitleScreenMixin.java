package com.example.pvpmod.mixin;

import com.example.pvpmod.client.gui.AltManagerScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongederived.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    // Biz qo'shgan rasmning manzili
    private static final Identifier CUSTOM_BACKGROUND = Identifier.of("pvpmod", "textures/gui/background.png");

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void onInit(CallbackInfo ci) {
        // Minecraft'ning standart Singleplayer, Multiplayer tugmalarini yaratilishdan to'xtatamiz
        ci.cancel();

        // Tugmalarning o'lchamlari (Kattaroq va chiroyli)
        int btnWidth = 160;
        int btnHeight = 24;
        
        // Chap tomondan masofa va tepadagi birinchi tugmaning Y o'qi
        int startX = 40; 
        int startY = this.height / 2 - 60; 

        // 1. Singleplayer Tugmasi
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Singleplayer"), button -> 
                this.client.setScreen(new SelectWorldScreen(this)))
                .dimensions(startX, startY, btnWidth, btnHeight).build());

        // 2. Multiplayer Tugmasi
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Multiplayer"), button -> 
                this.client.setScreen(new MultiplayerScreen(this)))
                .dimensions(startX, startY + 30, btnWidth, btnHeight).build());

        // 3. AltManager Tugmasi (Siz so'ragan qism)
        this.addDrawableChild(ButtonWidget.builder(Text.literal("AltManager"), button -> 
                this.client.setScreen(new AltManagerScreen(this)))
                .dimensions(startX, startY + 60, btnWidth, btnHeight).build());

        // 4. Settings Tugmasi
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Settings"), button -> 
                this.client.setScreen(new OptionsScreen(this, this.client.options)))
                .dimensions(startX, startY + 90, btnWidth, btnHeight).build());

        // 5. O'yindan chiqish
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Quit Game"), button -> 
                this.client.scheduleStop())
                .dimensions(startX, startY + 120, btnWidth, btnHeight).build());
    }

    // Standart fon rasmlari va Minecraft logosini chizadigan metodlarni butunlay o'zgartiramiz
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ci.cancel(); // Standart hamma narsani (panorama, logo) o'chirish

        // 1. O'zingiz yuborgan binafsha rasmni butun ekranga cho'zib chizish
        context.drawTexture(CUSTOM_BACKGROUND, 0, 0, 0, 0, this.width, this.height, this.width, this.height);

        // 2. Chap tomondagi tugmalar orqasiga chiroyli shaffof qora gradient berish (matnlar yaxshi ko'rinishi uchun)
        context.fill(0, 0, 240, this.height, 0x55000000);

        // 3. Mod nomini tepaga chiroyli neon rangda yozib qo'yamiz
        context.drawTextWithShadow(this.textRenderer, Text.literal("PURPLE PvP CLIENT"), 40, this.height / 2 - 95, 0xFF9D00FF);

        // Tugmalarni ekranga chiqarish
        for (ClickableWidget widget : this.drawables) {
            widget.render(context, mouseX, mouseY, delta);
        }
    }
}

