package com.example.pvpmod.client.gui;

import com.example.pvpmod.module.Module;
import com.example.pvpmod.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class ClickGuiScreen extends Screen {

    // Hozir tanlanib turgan toifa (Default: VISUAL)
    private Module.Category currentCategory = Module.Category.VISUAL;

    public ClickGuiScreen() {
        super(Text.literal("Click GUI"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Fonni biroz qoraytirish
        super.renderInGameBackground(context);

        // Menyu oynasining o'lchamlari va markazlashtirish
        int menuWidth = 360;
        int menuHeight = 220;
        int x = (this.width - menuWidth) / 2;
        int y = (this.height - menuHeight) / 2;

        // 30% va 70% bo'linish chizig'i (X koordinatasi)
        int sidebarWidth = (int) (menuWidth * 0.30); // 108 piksel
        int contentWidth = menuWidth - sidebarWidth;  // 252 piksel

        // 1. ASOSIY MENU FONI (To'q qora va binafsha aralashgan tekis dizayn)
        context.fill(x, y, x + menuWidth, y + menuHeight, 0xE60A0A0A); // Asosiy to'q fon
        context.fill(x, y, x + menuWidth, y + 2, 0xFF6A0DAD);          // Tepasidagi neon binafsha chiziq
        context.fill(x + sidebarWidth, y + 2, x + sidebarWidth + 1, y + menuHeight, 0xFF3A0066); // 30/70 ajratuvchi chiziq

        // 2. CHAP TOMON (30% - TOIFALAR RO'YXATI)
        int catY = y + 15;
        for (Module.Category category : Module.Category.values()) {
            boolean isSelected = (category == currentCategory);
            
            // Sichqoncha ustida turganini tekshirish (Hover)
            boolean isHovered = mouseX >= x && mouseX <= x + sidebarWidth && mouseY >= catY && mouseY <= catY + 20;

            // Agar tanlangan bo'lsa binafsha fon, bo'lmasa shaffof
            if (isSelected) {
                context.fill(x + 2, catY, x + sidebarWidth - 2, catY + 18, 0xAA5A0099);
            } else if (isHovered) {
                context.fill(x + 2, catY, x + sidebarWidth - 2, catY + 18, 0x22FFFFFF);
            }

            int textColor = isSelected ? 0xFFFFFFFF : 0xFF888888;
            context.drawTextWithShadow(this.textRenderer, Text.literal(category.name()), x + 10, catY + 5, textColor);
            
            catY += 22;
        }

        // 3. O'NG TOMON (70% - MODULLAR VA ULARNING TUSHUNTIRISHLARI)
        List<Module> currentModules = ModuleManager.getModulesByCategory(currentCategory);
        int modX = x + sidebarWidth + 15;
        int modY = y + 15;

        for (Module mod : currentModules) {
            // Modul tugmasi o'lchami
            int btnWidth = 100;
            int btnHeight = 20;

            boolean isHovered = mouseX >= modX && mouseX <= modX + btnWidth && mouseY >= modY && mouseY <= modY + btnHeight;
            
            // Yoqilgan bo'lsa neon binafsha, o'chiq bo'lsa tekis to'q rang
            int btnColor = mod.isEnabled() ? 0xFF6A0DAD : 0xD8151515;
            if (isHovered) btnColor = mod.isEnabled() ? 0xFF8A2BE2 : 0xD8252525; // Hover effekti

            // Tugmani chizish
            context.fill(modX, modY, modX + btnWidth, modY + btnHeight, btnColor);
            context.fill(modX, modY, modX + btnWidth, modY + 1, 0xFF3A0066); // Tugma ustki chizig'i
            
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(mod.getName()), modX + btnWidth / 2, modY + 6, 0xFFFFFFFF);

            // HOVER DESCRIPTION (Sichqoncha modul ustiga kelganda pastda tushuntirish matni chiqadi)
            if (isHovered) {
                String description = getModuleDescription(mod.getName());
                // Menyu oynasining eng pastki qismiga tushuntirishni yozamiz
                context.fill(x + sidebarWidth + 5, y + menuHeight - 25, x + menuWidth - 5, y + menuHeight - 5, 0x99000000);
                context.drawTextWithShadow(this.textRenderer, Text.literal(description), x + sidebarWidth + 10, y + menuHeight - 17, 0xFFBBBBBB);
            }

            // Keyingi modul uchun joy tashlash (2 ta ustun qilib chizish mantiqi)
            modX += btnWidth + 15;
            if (modX + btnWidth > x + menuWidth) {
                modX = x + sidebarWidth + 15;
                modY += btnHeight + 10;
            }
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int menuWidth = 360;
        int menuHeight = 220;
        int x = (this.width - menuWidth) / 2;
        int y = (this.height - menuHeight) / 2;
        int sidebarWidth = (int) (menuWidth * 0.30);

        if (button == 0) { // Chap tugma bosilganda
            
            // 1. Chap tomondagi toifalarni bosishni tekshirish
            int catY = y + 15;
            for (Module.Category category : Module.Category.values()) {
                if (mouseX >= x && mouseX <= x + sidebarWidth && mouseY >= catY && mouseY <= catY + 20) {
                    this.currentCategory = category; // Toifani almashtirish
                    return true;
                }
                catY += 22;
            }

            // 2. O'ng tomondagi modullarni bosishni tekshirish
            List<Module> currentModules = ModuleManager.getModulesByCategory(currentCategory);
            int modX = x + sidebarWidth + 15;
            int modY = y + 15;
            int btnWidth = 100;
            int btnHeight = 20;

            for (Module mod : currentModules) {
                if (mouseX >= modX && mouseX <= modX + btnWidth && mouseY >= modY && mouseY <= modY + btnHeight) {
                    mod.toggle(); // Modulni yoqish/o'chirish
                    return true;
                }
                
                modX += btnWidth + 15;
                if (modX + btnWidth > x + menuWidth) {
                    modX = x + sidebarWidth + 15;
                    modY += btnHeight + 10;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    // Har bir modul uchun tavsif (Description) matni
    private String getModuleDescription(String name) {
        return switch (name.toLowerCase()) {
            case "no hurtcam" -> "Zarba yeganda ekranning silkinishini ochiradi.";
            case "fps counter" -> "Ekranda joriy FPS miqdorini korsatadi.";
            case "tps" -> "Serverning ishlash tezligini (Ticks Per Second) korsatadi.";
            case "hud" -> "Asosiy PvP ma'lumotlarini ekranga chiqaradi.";
            case "armor hud" -> "Egningizdagi bronyalar holatini ekranda aks ettiradi.";
            case "inventory hud" -> "Inventardagi narsalarni ochmasdan ekranda korish.";
            case "autogapple" -> "Sogliq kamayganda avtomatik ravishda oltin olma yeydi.";
            case "autototem" -> "Totem singanda srazi svap qilib qolga totem oladi.";
            case "autosprint" -> "Doimiy ravishda avtomatik yugurishni faollashtiradi.";
            case "auto jump" -> "Avtomatik sakrash (BunnyHop) imkoniyati.";
            case "target visual" -> "Urayotgan dushmaningiz atrofida vizual effektlar.";
            case "norender" -> "Keraksiz effektlarni ochirib FPSni maksimal oshiradi.";
            case "fastplace" -> "Bloklarni juda tez qoyish imkonini beradi.";
            case "beautyscoreboard" -> "Yon tomondagi tabloning ko'rinishini chiroyli qiladi.";
            case "beauty chat" -> "Chat korinishini silliq va chiroyli qiladi.";
            case "hud colors" -> "HUD ranglarini sozlash imkoniyati.";
            default -> "PvP uslubidagi maxsus modul.";
        };
    }

    @Override
    public boolean shouldPauseGame() {
        return false;
    }
}
