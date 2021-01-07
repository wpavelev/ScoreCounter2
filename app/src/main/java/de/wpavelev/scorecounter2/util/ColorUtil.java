package de.wpavelev.scorecounter2.util;

import android.content.res.TypedArray;

public class ColorUtil {

    private static TypedArray sActivePlayerArrayColor;
    private static TypedArray sInactivePlayerArrayColor;

    public static TypedArray getActivePlayerArrayColor() {
        return sActivePlayerArrayColor;
    }

    public static void setActivePlayerArrayColor(TypedArray activePlayerArrayColor) {
        ColorUtil.sActivePlayerArrayColor = activePlayerArrayColor;
    }

    public static TypedArray getInactivePlayerArrayColor() {
        return sInactivePlayerArrayColor;
    }

    public static void setInactivePlayerArrayColor(TypedArray inactivePlayerArrayColor) {
        ColorUtil.sInactivePlayerArrayColor = inactivePlayerArrayColor;
    }



}
