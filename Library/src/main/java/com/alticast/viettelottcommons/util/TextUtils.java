package com.alticast.viettelottcommons.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.LruCache;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class TextUtils {
    private static final String TAG = TextUtils.class.getSimpleName();
    private static Map<String, String> LANGUAGE_MAP = new HashMap<>();
    private static LruCache<String, SimpleDateFormat> DATE_FORMAT_CACHE = new LruCache<>(64);

    public static final String TIME_GMT = "GMT+7:00";

    static {
        LANGUAGE_MAP.put(Locale.KOREAN.getLanguage(), "kor");
        LANGUAGE_MAP.put(Locale.ENGLISH.getLanguage(), "eng");
        LANGUAGE_MAP.put(Locale.JAPANESE.getLanguage(), "jpn");
        LANGUAGE_MAP.put(Locale.CHINESE.getLanguage(), "chn");
        LANGUAGE_MAP.put("vi", "vie");
    }

    public static String getLocaleString(Bundle multiText) {
        if (multiText == null) return null;
        String language = Locale.getDefault().getLanguage();
        String ret = multiText.getString(language);
        if (ret != null) return ret;
        ret = multiText.getString(LANGUAGE_MAP.get(language));
        if (ret != null) return ret;

        language = Locale.ENGLISH.getLanguage();
        ret = multiText.getString(language);
        if (ret != null) return ret;
        ret = multiText.getString(LANGUAGE_MAP.get(language));
        if (ret != null) return ret;

        Set<String> strings = multiText.keySet();
        Iterator<String> iterator = strings.iterator();
        String languageKey = iterator.next();
        return multiText.getString(languageKey);
    }

    public static String getNumberString(int number) {
        return NumberFormat.getInstance().format(number);
    }
    public static String getNumberString(float number) {
        return NumberFormat.getInstance().format(number);
    }
    public static String getDateString(long date, String format) {
        Locale locale = Locale.ENGLISH;//language
//        Locale locale = Locale.getDefault();//language
//        if (locale == null || !"vi".equals(locale.getLanguage())) {
//            locale = Locale.ENGLISH;
//        }

        SimpleDateFormat dateFormat = DATE_FORMAT_CACHE.get(format + locale.getLanguage());
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(format, locale);
            DATE_FORMAT_CACHE.put(format + locale.getLanguage(), dateFormat);
        }
//        Logger.d("TextUtils", " dateFormat.getDate() " +date+" "+dateFormat.format(new Date(date)));
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        if(timeZone.getRawOffset() == 0) timeZone = TimeZone.getTimeZone("GMT+07");
        dateFormat.setTimeZone(timeZone);
 //       Logger.d("TextUtils", " dateFormat.getDate() " + dateFormat.format(new Date(date)));
        return dateFormat.format(new Date(date));
    }

    public static Bundle getMultiTextFromJsonArray(JSONArray jText) throws JSONException {
        Bundle names = new Bundle();
        int length = jText.length();
        for (int i = 0; i < length; i++) {
            JSONObject name = jText.getJSONObject(i);
            String key = name.getString("lang");
            String value = name.getString("text");
            names.putString(key, value);
        }
        return names;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setDefaultFont(String staticTypefaceFieldName, Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);

            final Field sDefaults = Typeface.class.getDeclaredField("sDefaults");
            sDefaults.setAccessible(true);
            Typeface[] defaults = (Typeface[]) sDefaults.get(null);
            if ("DEFAULT".equals(staticTypefaceFieldName)) {
                defaults[Typeface.NORMAL] = newTypeface;
                setDefaultFont(newTypeface);

                final Field sDefaultTypeface = Typeface.class.getDeclaredField("sDefaultTypeface");
                sDefaultTypeface.setAccessible(true);
                sDefaultTypeface.set(null, newTypeface);
            } else if ("DEFAULT_BOLD".equals(staticTypefaceFieldName)) {
                defaults[Typeface.BOLD] = newTypeface;
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
    }

    public static void setDefaultFont(Context context, String staticTypefaceFieldName, String assetPath) {
        final Typeface newTypeface = Typeface.createFromAsset(context.getAssets(), assetPath);
        setDefaultFont(staticTypefaceFieldName, newTypeface);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setDefaultFont(Typeface typeface) {
        try {
            final Field sSystemFontMap = Typeface.class.getDeclaredField("sSystemFontMap");
            sSystemFontMap.setAccessible(true);
            Map<String, Typeface> systemFontMap = (Map<String, Typeface>) sSystemFontMap.get(null);
            systemFontMap.put("sans-serif", typeface);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
    }


}
