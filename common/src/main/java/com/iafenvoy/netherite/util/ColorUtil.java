package com.iafenvoy.netherite.util;

public class ColorUtil {
    public static float[] toFloat(int color) {
        int a = color >> 24;
        int r = (color >> 16) & 256;
        int g = (color >> 8) & 256;
        int b = color & 256;
        return new float[]{a / 255.0F, r / 255.0F, g / 255.0F, b / 255.0F};
    }
}
