package com.alticast.viettelottcommons.util;

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.resource.Vod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by duyuno on 9/30/16.
 */
public class DetailUtil {

    // okie from master

    public static interface BundleKey {
        public static final String BUNDLE_VOD_INFORMATION = "VOD_INFORMATION";
        public static final String PROGRAM_ID = "PROGRAM_ID";
        public static final String VOD_PATH = "VOD_PATH";
    }

    private static SimpleDateFormat sdfProDateFromServer = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdfProDateConvert = new SimpleDateFormat("yyyy");

    private static SimpleDateFormat sdfDurationFromServer = new SimpleDateFormat("hh:mm:ss");
    private static SimpleDateFormat sdfDurationConvert = new SimpleDateFormat("H'h' mm'm'");

    public static HashMap<String, Vod> hashMapVod;
    public static HashMap<String, ArrayList<Vod>> hashMapVodRelateContent;
    public static Stack<Vod> vodStack;

    static {
        hashMapVod = new HashMap<>();
        hashMapVodRelateContent = new HashMap<>();
        vodStack = new Stack<>();
    }

    public static int scrWidth, scrHeight;

    public static void getConfigInfo(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        scrHeight = metrics.heightPixels;
        scrWidth = metrics.widthPixels;
    }

    public static String getProductionYear(String productionDate) {

        if (productionDate == null || productionDate.length() == 0) {
            return null;
        }

        try {
            Date date = sdfProDateFromServer.parse(productionDate);
            return sdfProDateConvert.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTimeDuration(String duration) {
        if(duration == null || duration.length() == 0) {
            return "N/A";
        }
        try {
            Date date = sdfDurationFromServer.parse(duration);
            return sdfDurationConvert.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    public static String getGenres(String[] genres) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0, length = genres.length; i < length; i++) {
            if (i == 0) {
                sb.append(genres[i]);
            } else {
                sb.append(", ").append(genres[i]);
            }
        }

        return sb.length() == 0 ? null : sb.toString();
    }

    public static final String TYPE_LOGO = "logo";
    public static final String TYPE_POSTER = "poster";
    public static final int IMAGE_SIZE = 400;

    public static String getPosterUrl(String id, String type, int width, int height) {
        String url = null;
        try {
            if (type.equals(TYPE_LOGO)) {
                url = WindmillConfiguration.getBaseUrl() + "api1/contents/pictures/" + id + "?type=original" + "&width=" + width + "&height="
                        + height;
            } else {
                url = WindmillConfiguration.getBaseUrl() + "api1/contents/pictures/" + id + "?type=custom" + "&width=" + width + "&height="
                        + height;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getPosterUrl(String id, String type) {
        String url = null;
        try {
            if (type.equals(TYPE_LOGO)) {
                url = WindmillConfiguration.getBaseUrl() + "api1/contents/pictures/" + id + "?type=original";
            } else {
                url = WindmillConfiguration.getBaseUrl() + "api1/contents/pictures/" + id + "?type=custom";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static void addVod(String id, Vod vod) {
        if (hashMapVod == null) {
            hashMapVod = new HashMap<>();
        }

        hashMapVod.put(id, vod);
    }

    public static void addVodRelateContent(String id, ArrayList<Vod> listVod) {
        if (hashMapVodRelateContent == null) {
            hashMapVodRelateContent = new HashMap<>();
        }

        hashMapVodRelateContent.put(id, listVod);
    }

    public static Vod getVod(String id) {
        if (hashMapVod == null || !hashMapVod.containsKey(id)) {
            return null;
        }

        return hashMapVod.get(id);
    }

    public static ArrayList<Vod> getVodRalateContents(String id) {
        if (hashMapVodRelateContent == null || !hashMapVodRelateContent.containsKey(id)) {
            return null;
        }

        return hashMapVodRelateContent.get(id);
    }

    public static void pushStack(Vod vod) {
        if (vodStack == null) {
            vodStack = new Stack<>();
        }

        vodStack.push(vod);
    }

    public static Vod popStact() {
        if (vodStack == null || vodStack.isEmpty()) {
            return null;
        }
        return vodStack.pop();
    }

    public static Vod popFirst() {
        if (vodStack == null || vodStack.isEmpty()) {
            return null;
        }

        Vod vod = null;

        while (!vodStack.isEmpty()) {
            vod = vodStack.pop();
        }

        return vod;
    }

    public static boolean checkIsReateMode() {
        if(vodStack == null || vodStack.isEmpty()) {
            return false;
        }
        return true;
    }

    public static void clearData() {
        if (hashMapVod != null) {
            hashMapVod.clear();
        }

        if (hashMapVodRelateContent != null) {
            hashMapVodRelateContent.clear();
        }

        if (vodStack != null)
            vodStack.clear();
    }

    // Make textview with underline and clickable
    public void makeClickableSpan(final String s, String fullString, SpannableStringBuilder strBuilder) {
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {

            }
        };

        int start = fullString.indexOf(s);
        int end = start + s.length();
        if (start == -1) return;

        strBuilder.setSpan(clickable, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        strBuilder.setSpan(new UnderlineSpan(), start, end, 0);

        strBuilder.setSpan(new ForegroundColorSpan(Color.WHITE), start, end, 0);
    }

}
