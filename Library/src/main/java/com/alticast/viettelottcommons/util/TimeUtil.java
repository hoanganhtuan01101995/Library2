/*
 *  Copyright (c) 2012 Alticast Corp.
 *  All rights reserved. http://www.alticast.com/
 *
 *  This software is the confidential and proprietary information of
 *  Alticast Corp. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into
 *  with Alticast.
 */
package com.alticast.viettelottcommons.util;

import android.os.Handler;
import android.os.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * <code>TimeUtil</code>.
 *
 * @author JEK
 * @since 2012. 8. 7.
 */
public class TimeUtil {

    public static final int DELAY_MILLIS = 5000;

    /**
     * Time milliseconds to string.
     *
     * @param time   the time
     * @param format the format
     * @return the string
     */
    public static String timeMillSecToString(long time, String format) {
        return timeMillSecToString(time, format, null);
    }

    public static String timeMillSecToString(long time, String format, Locale locale) {
        String str = dateToString(new Date(time), format, locale);
        return str;
    }

    /**
     * Date to string.
     *
     * @param date   the date
     * @param format the format
     * @return the string
     */
    public static String dateToString(Date date, String format) {
        return dateToString(date, format, null);
    }

    public static String dateToString(Date date, String format, Locale locale) {
        String dateString = null;
        try {
            SimpleDateFormat dateFormat = null;
            if (locale != null) {
                dateFormat = new SimpleDateFormat(format, locale);
            } else {
                dateFormat = new SimpleDateFormat(format);
                dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            }
            dateString = dateFormat.format(date);
        } catch (Exception e) {
        }
        return dateString;
    }

    /**
     * 날짜 시간 문자열을 다른 형식으로 표현하고자 할 때 기존 날짜값과 그 형식 그리고 새로운 형식을 넘겨서 변환된 포맷의 날짜 시간
     * 문자열을 얻는다.
     * <p/>
     * ex) getDateStringFormat("201106150430", "yyyyMMddhhmm", "yyyy.MM.dd") 일 때
     * 201106150430 -> 2011.06.15 리턴됨
     *
     * @param originalDate the original date String
     * @param formatStr    the original format String
     * @param newFormatStr the new target format String
     * @return formatted string
     * @see SimpleDateFormat patterns and examples
     */
    public static String changeDateStringFormat(String originalDate, String formatStr, String newFormatStr) {
        String changedDateString = originalDate;
        try {
            SimpleDateFormat orginFormat = new SimpleDateFormat(formatStr);
            orginFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            SimpleDateFormat targetFormat = new SimpleDateFormat(newFormatStr);
            targetFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            Date srcDate = orginFormat.parse(originalDate);

            changedDateString = targetFormat.format(srcDate);
        } catch (Exception e) {
        }

        return changedDateString;
    }

    public static int changeDateSecond(String originalDate, String formatStr) {
        SimpleDateFormat orginFormat = new SimpleDateFormat(formatStr);
        orginFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        Date srcDate = null;
        try {
            srcDate = orginFormat.parse(originalDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return calTime(srcDate);
    }

    public static int getVodRunningTime(String date) {
        if (date == null) {
            return 0;
        }
        String[] tmpTime = date.split(Pattern.quote(":"));
        int size = tmpTime.length;
        if (size == 3) {
            int hour = Integer.parseInt(tmpTime[0]);
            int min = Integer.parseInt(tmpTime[1]);
            int sec = Integer.parseInt(tmpTime[2]);

            return 1000 * (hour * 60 * 60 + min * 60 + sec);

        } else {
            return 0;
        }

    }

    private static int calTime(Date date) {
        long currentTime = date.getTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH");
        timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat minForamt = new SimpleDateFormat("mm");
        minForamt.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat secFormat = new SimpleDateFormat("ss");
        secFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        int hour = Integer.valueOf(timeFormat.format(currentTime));
        int min = Integer.valueOf(minForamt.format(currentTime));
        int sec = Integer.valueOf(secFormat.format(currentTime));

        return hour * 3600 + min * 60 + sec;
    }

    public static void main(String args[]) {
        // System.out.println(TimeUtil.dateToString(new Date(), "hh:mm",
        // Locale.KOREA));
        // System.out.println(TimeUtil.dateToString(new Date(), "M.dd E ahh:mm",
        // Locale.KOREA));
        // System.out.println(TimeUtil.dateToString(new Date(), "yyyy.MM.dd",
        // Locale.KOREA));
        // System.out.println(TimeUtil.changeDateStringFormat("2012.08.01 15:30",
        // "yyyy.MM.dd HH:mm", "M.dd(E) a h:mm"));
        String a = null;
    }

    public static long getLongTime(String originalDate, String formatS) {
        Date date = getDate(originalDate, formatS);
        if (date == null) {
            return 0;
        } else {
            return date.getTime();
        }
    }

    public static String getTime(long time, String formatS) {
        Date date = new Date(time);
        SimpleDateFormat orginFormat = new SimpleDateFormat(formatS);
        orginFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        return orginFormat.format(date);
    }

    public static String getTimeNoTimeZone(long time) {
        time = time / 1000;
        int hour = (int) (time / 3600);
        int min = (int) ((time - hour * 3600) / 60);
        int sec = (int) (time - hour * 3600 - min * 60);

        String hourStr = hour < 10 ? "0" + hour : hour + "";
        String minStr = min < 10 ? "0" + min : min + "";
        String secStr = sec < 10 ? "0" + sec : sec + "";
        return hourStr + ":" + minStr + ":" + secStr;
    }

    // for time HH:mm:ss
    public static long getTimeLong(String time) {
        String[] list = time.split(":");
        int hour = Integer.parseInt(list[0]);
        int min = Integer.parseInt(list[1]);
        int sec = Integer.parseInt(list[2]);

        return hour * 3600 + min * 60 + sec;
    }

    public static long getLongTime(String originalDate, String formatS, Locale locale) {
        Date date = null;
        SimpleDateFormat orginFormat = new SimpleDateFormat(formatS, locale);
        try {
            date = orginFormat.parse(originalDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (date == null) {
            return 0;
        } else {
            return date.getTime();
        }
    }

    public static int secToDay(long time) {
        long sec = time;

        long hour = sec / 3600;
        return (int) hour / 24;

    }

    public static Date getDate(String originalDate, String formatS) {
        if (originalDate == null || originalDate.length() == 0) {
            return null;
        }

        SimpleDateFormat orginFormat = new SimpleDateFormat(formatS);
        orginFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        try {
            Date date = orginFormat.parse(originalDate);
            return date;
        } catch (ParseException e) {
        }

        return null;
    }

    public static String changeDateStringFormat(String originalDate, String newFormatStr) {
        String changedDateString = originalDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(newFormatStr);

            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            Date resultdate = new Date(Long.parseLong(originalDate));

            changedDateString = sdf.format(resultdate);
        } catch (Exception e) {
        }
        return changedDateString;
    }

    private class CntHandler extends Handler {
        private String KEY_ACTION = "action";
        private int DELAY_TIME = 1000;

        public CntHandler(Callback callback) {
            super(callback);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


        }
    }


}
