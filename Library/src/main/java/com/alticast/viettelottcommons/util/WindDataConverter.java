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

//import windmill.frwk.ui.wind.FrameworkMain;
//import windmill.ui.config.tb.MessageDef;

import com.alticast.viettelottcommons.resource.CustomValue;
import com.alticast.viettelottcommons.resource.MultiLingualText;
import com.alticast.viettelottcommons.resource.Payment;
import com.alticast.viettelottcommons.resource.Schedule;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


/*
 * 
 * dm.wind resource와 관련한 데이터의 포맷을 변경하기 위한 util
 * 
 * 
 * @author JEK
 * @since 2012. 10. 18.
 */
public class WindDataConverter {
    public static String PROGRAM_TIME_FORMAT = "HH:mm";
    public static String HTTP_HEADER_TIME_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static String WINDMILL_SERVER_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static String WINDMILL_SERVER_UPDATED_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static String WINDMIL_FILTER_TIME_FORMAT = "EEE/dd/MM";
    public static String WINDMIL_MAIL_DATE_FORMAT = "EEE MM.dd.yyyy";
    public static String WINDMIL_PURCHASE_DATE_FORMAT = "EEE MM.dd HH:mm";
    public static String WINDMIL_PURCHASE_DETAIL_DATE_FORMAT = "EEE dd/MM";
    public static String WINDMIL_TOPUP_DATE_FORMAT = "dd.MM.yyyy";
    public static String WINDMIL_TOPUP_HOUR_FORMAT = "HH:mm a";
    public static String WINDMIL_NOMAIL_TYPE = "yyyyMMddHHmmss";
    public static String WINDMIL_PURCHAE_DATE = "MM/dd/yyyy";
    public static String WINDMIL_PURCHAE_TIME = "hh:mm aaa";
    public static String FORMAT_DATE_PURCHASE = "dd/MM/yyyy";

    public static String getStartTimeStr(Schedule schedule) {
        String timeS = schedule.getStart_time();
        return TimeUtil.changeDateStringFormat(timeS, WINDMILL_SERVER_TIME_FORMAT, PROGRAM_TIME_FORMAT);
    }

    public static String getEndTimeStr(Schedule schedule) {
        String timeS = schedule.getEnd_time();
        return TimeUtil.changeDateStringFormat(timeS, WINDMILL_SERVER_TIME_FORMAT, PROGRAM_TIME_FORMAT);
    }

    public static long getStartTime(Schedule schedule) {
        String timeS = schedule.getStart_time();
        return TimeUtil.getLongTime(timeS, WINDMILL_SERVER_TIME_FORMAT);
    }

    public static long getEndTime(Schedule schedule) {
        String timeS = schedule.getEnd_time();
        return TimeUtil.getLongTime(timeS, WINDMILL_SERVER_TIME_FORMAT);
    }

    public static String chargeCurrency(int value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###", symbols);
        String prezzo = decimalFormat.format(value);
        return prezzo;
    }
    public static String chargeCurrency(float value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###", symbols);
        String prezzo = decimalFormat.format(value);
        return prezzo;
    }
    public static String getLangString(MultiLingualText[] text, String lang) {
        String reStr = null;
        if (text != null && text.length > 0) {
            for (int i = 0; i < text.length; i++) {
                if (lang.equals(text[i].getLang())) {
                    reStr = text[i].getText();
                    break;
                }
            }
        }
        return reStr;
    }

    public static String getCustomValue(CustomValue[] customValues, String customName) {
        if (customValues == null || customName == null) {
            return null;
        }

        for (int i = 0; i < customValues.length; i++) {
            if (customName.equals(customValues[i].getName())) {
                return customValues[i].getValue();
            }
        }
        return null;
    }

    public static String getPaymentType(Payment payment) {
        if (payment == null) {
            return null;
        }

        // if(payment.payment_type.equals(Payment.TYPE_NORMAL)) {
        // return
        // FrameworkMain.localeBundle.getMessage(MessageDef.VOD_PAYMENT_NORMAL);
        // // "일반결제";
        // } else if(payment.payment_type.equals(Payment.TYPE_COUPON)) {
        // return
        // FrameworkMain.localeBundle.getMessage(MessageDef.VOD_PAYMENT_COUPON);
        // // "쿠폰결제";
        // } else if(payment.payment_type.equals(Payment.TYPE_POINT)) {
        // return
        // FrameworkMain.localeBundle.getMessage(MessageDef.VOD_PAYMENT_POINT);//
        // "포인트결제";
        // } else {
        // return "";
        // }
        return "";
    }

    public static String getTextByLanguage(MultiLingualText[] values, String language) {
        String value = null;
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++) {
                if (language.equals(values[i].getLang())) {
                    value = values[i].getText();
                    break;
                }
            }
            if (value == null) {
                value = values[0].getText();
            }
        }
        return value;
    }

    /**
     * server에서 리턴하는 currency 데이터를 화면에서 보여줄 스트링으로 변환
     *
     * @param currency
     * @return
     */
    public static String getCurrencyStr(String currency) {
        // Log.debug("WindDataConverter", "getCurrencyStr() currency = " +
        // currency);
        // if ("KRW".equals(currency)){
        // return FrameworkMain.localeBundle.getMessage(MessageDef.LABEL_WON);
        // // "원";
        // } else {
        // return null;
        // }
        return "";
    }

    public static void main(String args[]) {
        System.out.println("DDDDDDDDDDD");
    }

}
