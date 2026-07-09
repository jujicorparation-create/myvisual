package com.example.pvpmod;

import com.example.pvpmod.module.ModuleManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.Entity; // Import qo'shildi
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PvpMod implements ModInitializer {
    public static final String MOD_ID = "pvpmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Mixinlar qidirayotgan currentTarget o'zgaruvchisi qo'shildi!
    public static Entity currentTarget = null; 

    @Override
    public void onInitialize() {
        LOGGER.info("Purple PvP Client yuklanmoqda...");
        ModuleManager.init();
    }
}
