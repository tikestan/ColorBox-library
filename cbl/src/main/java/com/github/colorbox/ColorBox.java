package com.github.colorbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ColorBox {

    private static Map<String, Boolean> isPreferenceChanged = new HashMap<>();
    private static String mTag;
    private static boolean sPreference = false;
    private static int mTheme;
    private static String RECENT_KEY = "recent_set";

    static final int THEME_DEFAULT = 0;
    static final int THEME_DARK = 1;
    static final int THEME_BLACK = 2;

    public static int LIGHT = R.style.ColorBoxTheme;
    public static int DARK = R.style.ColorBoxThemeDark;
    public static int BLACK = R.style.ColorBoxThemeBlack;

    //method to show ColorBox
    public static void showColorBox(String tag, Context context, int theme) {

        context.startActivity(new Intent(context, ColorBoxActivity.class));
        mTag = tag;
        mTheme = theme;
    }

    static int getTheme() {
        return mTheme;
    }

    //method to show ColorBox
    static void showColorBoxPreference(String tag, Context context, int theme) {

        context.startActivity(new Intent(context, ColorBoxActivity.class));
        mTag = tag;
        mTheme = theme;
        sPreference = true;
    }

    static String getTag() {
        return mTag;
    }

    public static void registerPreferenceUpdater(Activity activity) {

            if (isPreferenceChanged.containsKey(mTag) && sPreference && isPreferenceChanged.get(mTag)) {

                activity.recreate();
                sPreference = false;
            }
    }

    static void setColor(Activity activity, int color) {
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putInt(mTag, color).apply();
        if (sPreference) {
            isPreferenceChanged.put(mTag, true);
        }
        activity.finish();
    }

    public static int getColor(String tag, Context context) {

        int defaultColor = Color.WHITE;
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(tag, defaultColor);
    }

    static Set<String> getLatest(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getStringSet(RECENT_KEY, null);
    }

    static void deleteLatest(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putStringSet(RECENT_KEY, null).apply();
    }

    static void saveToLatest(Context context, int color) {

        Set<String> latest = getLatest(context);

        if (latest == null) {
            latest = new HashSet<>();
        }

        latest.add(String.valueOf(color));

        PreferenceManager.getDefaultSharedPreferences(context).edit().putStringSet(RECENT_KEY, latest).apply();
    }

    //get complementary color
    public static int getComplementaryColor(int colorToInvert) {

        int r = Color.red(colorToInvert);
        int g = Color.green(colorToInvert);
        int b = Color.blue(colorToInvert);
        int red = 255 - r;
        int green = 255 - g;
        int blue = 255 - b;

        return Color.argb(255, red, green, blue);
    }

    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;

        return darkness > 0.5;
    }

    public static String getHexadecimal(int color) {
        return String.format("#%06X", (color));
    }
}
