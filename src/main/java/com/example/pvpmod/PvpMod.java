package com.example.pvpmod;

import com.example.pvpmod.module.ModuleManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PvpMod implements ModInitializer {
    public static final String MOD_ID = "pvpmod";
    public static final Logger LOGGER = LoggerFactory.LoggerFactory.getLogger(MOD_ID);
    
    // Oxirgi urilgan dushmanni saqlash uchun universal o'zgaruvchi
    public static Entity currentTarget = null;

    @Override
    public void onInitialize() {
        LOGGER.info("PvP Mod asosiy qismi yuklanmoqda...");
        ModuleManager.init();
    }
}
