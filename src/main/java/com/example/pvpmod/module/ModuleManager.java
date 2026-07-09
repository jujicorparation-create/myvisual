package com.example.pvpmod.module;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        // 1. PREVISUAL (Bu yerda VISUAL so'zlari PREVISUAL ga muvaffaqiyatli almashtirildi)
        modules.add(new Module("No HurtCam", Module.Category.PREVISUAL) {});
        modules.add(new Module("FPS Counter", Module.Category.PREVISUAL) {});
        modules.add(new Module("TPS", Module.Category.PREVISUAL) {});
        modules.add(new Module("HUD", Module.Category.PREVISUAL) {});
        modules.add(new Module("Armor HUD", Module.Category.PREVISUAL) {});
        modules.add(new Module("Inventory HUD", Module.Category.PREVISUAL) {});
        modules.add(new Module("AutoGapple", Module.Category.PREVISUAL) {});
        modules.add(new Module("AutoTotem", Module.Category.PREVISUAL) {});

        // 2. MOVEMENT
        modules.add(new Module("AutoSprint", Module.Category.MOVEMENT) {});
        modules.add(new Module("AutoJump", Module.Category.MOVEMENT) {});

        // 3. MISC
        modules.add(new Module("Target Visual", Module.Category.MISC) {});

        // 4. RENDER
        modules.add(new Module("NoRender", Module.Category.RENDER) {});
        modules.add(new Module("FastPlace", Module.Category.RENDER) {});
        modules.add(new Module("BeautyScoreboard", Module.Category.RENDER) {});
        modules.add(new Module("Beauty Chat", Module.Category.RENDER) {});
        modules.add(new Module("HUD Colors", Module.Category.RENDER) {});
    }

    public static List<Module> getModules() { return modules; }

    public static List<Module> getModulesByCategory(Module.Category category) {
        return modules.stream().filter(m -> m.getCategory() == category).collect(Collectors.toList());
    }
}
