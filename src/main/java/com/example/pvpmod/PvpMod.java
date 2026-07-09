package com.example.pvpmod;

import com.example.pvpmod.client.ModKeybindings;
import com.example.pvpmod.module.ModuleManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // Import qo'shildi!

public class PvpMod implements ModInitializer {
    public static final String MOD_ID = "pvpmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Purple PvP Client yuklanmoqda...");
        
        // Modullarni va tugmalarni ishga tushirish
        ModuleManager.init();
        ModKeybindings.init();
    }
}
