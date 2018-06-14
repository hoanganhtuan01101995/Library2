package com.alticast.viettelottcommons.util;

import android.content.Context;
import android.view.View;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.manager.AuthManager;

/**
 * Created by duyuno on 1/19/17.
 */
public class StringUtil {
    public static final String getBuyPeriodString(Context context, int period) {
        switch (period) {

            case 0:
                return null;
            case 1:
                return context.getString(R.string.buyDayPackage);
            case 7:
                return context.getString(R.string.buyWeekPackage);
            case 30:
                return context.getString(R.string.buyMonthlyPackage);
            default:
                int month = period / 30;
                if (month == 0) {
                    return context.getString(R.string.buyPackage) + " " + period + " " + context.getString(R.string.justDay);
                } else {
                    return context.getString(R.string.buyPackage) + " " + month + " " + context.getString(R.string.justMothn);
                }

        }
    }
    public static final String getPricePeriodString(Context context, int period, int price, String unit) {
        String first = price + " " + unit + "/";
        switch (period) {

            case 0:
                return null;
            case 1:
                return first + context.getString(R.string.justDay);
            case 7:
                return first + context.getString(R.string.justWeek);
            case 30:
                return first +  context.getString(R.string.justMothn);
            default:
                int month = period / 30;
                if (month == 0) {
                    return first + period + " " + context.getString(R.string.justDay);
                } else {
                    return first + month + " " + context.getString(R.string.justMothn);
                }

        }
    }
    public static final String getPeriodString(Context context, int period) {
        switch (period) {

            case 0:
                return AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2 ? "N/A" : context.getString(R.string.justMothn);
            case 1:
                return context.getString(R.string.justDay);
            case 7:
                return context.getString(R.string.justWeek);
            case 30:
                return context.getString(R.string.justMothn);
            default:
                return period + " " + context.getString(R.string.justDay);
//                int month = period / 30;
//                if (month == 0) {
//                    return period + " " + context.getString(R.string.justDay);
//                } else {
//                    return month + " " + context.getString(R.string.justMothn);
//                }

        }
    }
    public static final String getPeriodTimeString(Context context, int period) {
        switch (period) {

            case 0:
                return AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2 ? "N/A" : 1 + " " + context.getString(R.string.justMothn);
            case 1:
                return 1 + " " + context.getString(R.string.justDay);
            case 7:
                return 1 + " " + context.getString(R.string.justWeek);
            case 30:
                return 1 + " " + context.getString(R.string.justMothn);
            default:
                return period + " " + context.getString(R.string.justDay);
//                int month = period / 30;
//                if (month == 0) {
//                    return period + " " + context.getString(R.string.justDay);
//                } else {
//                    return month + " " + context.getString(R.string.justMothn);
//                }

        }
    }

    public static boolean isValidateSaleCode(String saleCode) {

        // Noi dung chi chua cac ky tu a-z, A-Z, 0-9, khoang trang
        int length = saleCode.length();
        for (int i = 0; i < length; i++) {
            char ch = saleCode.charAt(i);
            if (((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
                    || (ch >= '0' && ch <= '9') || ch == '_')) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
    public static boolean isValidateSaleCodeChar(char ch) {
        return ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
                || (ch >= '0' && ch <= '9') || ch == '_');
    }
}
