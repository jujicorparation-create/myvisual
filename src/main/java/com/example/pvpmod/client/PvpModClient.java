package com.example.pvpmod.client;

import net.fabricmc.api.ClientModInitializer;

public class PvpModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // O'yin ichidagi tugmalarni (Right-Shift) ro'yxatga olish
        ModKeybindings.register();
    }
}

