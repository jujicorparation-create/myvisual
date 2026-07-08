package com.example.pvpmod.util;

import java.util.Arrays;

public class TpsTracker {
    private static final float[] tickRates = new float[20];
    private static int nextIndex = 0;
    private static long lastTime = -1L;

    public static void onTimeUpdate() {
        if (lastTime != -1L) {
            long currentTime = System.currentTimeMillis();
            long timeElapsed = currentTime - lastTime;
            
            // 1 tick ideal holatda 50ms davom etishi kerak
            float currentTps = 20000.0f / timeElapsed;
            if (currentTps > 20.0f) currentTps = 20.0f;
            
            tickRates[nextIndex % tickRates.length] = currentTps;
            nextIndex++;
        }
        lastTime = System.currentTimeMillis();
    }

    public static float getTps() {
        if (nextIndex == 0) return 20.0f;
        float sum = 0;
        int count = Math.min(nextIndex, tickRates.length);
        for (int i = 0; i < count; i++) {
            sum += tickRates[i];
        }
        return sum / count;
    }
}

