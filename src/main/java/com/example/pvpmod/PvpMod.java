package com.example.pvpmod;

import com.example.pvpmod.module.ModuleManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PvpMod implements ModInitializer {
    public static final String MOD_ID = "pvpmod";
    public static final Logger LOGGER = LoggerFactory.LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("PvP Mod asosiy qismi yuklanmoqda...");
        // Modullarni xotiraga yuklash
        ModuleManager.init();
    }
}

