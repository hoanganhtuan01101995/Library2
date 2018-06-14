package com.alticast.viettelottcommons.manager;


import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.Response;


/**
 * Created by mc.kim on 8/3/2016.
 */
public class TimeManager {
    private final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";

    private final String PATTERNS = "EEE MMM dd HH:mm:ss zzz yyyy";
    private static TimeManager ourInstance = new TimeManager();
    private long serverTime = 0;
    private long timeOffset = 0;
    public static final long GMT_7 = 7 * 60 * 60 * 1000;

    public static TimeManager getInstance() {
        return ourInstance;
    }

    private TimeManager() {
    }


    public void initTime(Response response) {
        if(response == null) return;
        Logger.print(this, "response : " + response.headers().getDate("date").toString());

        long d = TimeUtil.getLongTime(response.headers().getDate("date").toString(), PATTERNS, Locale.US);
        long systemTime = System.currentTimeMillis();
        serverTime = d;
        timeOffset = systemTime - d;
    }

    public long getServerCurrentTimeMillis() {
        long system = System.currentTimeMillis();
        calculateTimeOffset();
        long tmpTime = timeOffset + serverTime;
        return system - timeOffset;
    }

    public long getCurrentTime(long inputTime) {
        calculateTimeOffset();
        return inputTime + timeOffset;
    }

    public void calculateTimeOffset() {
        Calendar calInitial = Calendar.getInstance();
        calInitial.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        long offsetInitial = calInitial.getTimeInMillis();
    }

    public int getMonth() {
        SimpleDateFormat format = new SimpleDateFormat("MM");
        String formattedDate = format.format(serverTime);
        return Integer.valueOf(formattedDate);
    }

    public long getTodayStartTimeMills(){
        long today = -1;
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        if (timeZone.getRawOffset() == 0) timeZone = TimeZone.getTimeZone("GMT+07");
        Calendar currentCalendar = Calendar.getInstance(timeZone);
        currentCalendar.setTimeInMillis(TimeManager.getInstance().getServerCurrentTimeMillis());
//        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
//        currentCalendar.set(Calendar.MINUTE, 0);
//        currentCalendar.set(Calendar.SECOND, 0);
//        currentCalendar.set(Calendar.MILLISECOND, 0);
        setTimeToBeginningOfDay(currentCalendar);
        today = currentCalendar.getTimeInMillis();

        Logger.print("TimeManager", "getTodayStartTime()-today : " + today);
        return today;
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

}
