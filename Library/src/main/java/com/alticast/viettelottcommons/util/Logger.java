/*
 *  @(#)Logger.java 1.0 2013.01.03
 *  Copyright (c) 2010 Alticast Corp.
 *  All rights reserved. http://www.alticast.com/
 *
 *  This software is the confidential and proprietary information of
 *  Alticast Corp. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into
 *  with Alticast.
 */

package com.alticast.viettelottcommons.util;

import android.util.Log;

import com.alticast.viettelottcommons.WindmillConfiguration;


/**
 * <code>Logger</code> log�� ����ϱ� ���� Ŭ����.
 *
 * @author tklee
 * @version $Revision: 1.1 $ $Date: 2014/12/09 13:57:09 $
 * @since 2013.01.03
 */
public class Logger {
    /**
     * RootScene ��/����.
     * <p/>
     *
     * @param obj     Object
     * @param message Log Message
     */
    public static void print(Object obj, String message) {
        String classFullyName = obj.getClass().getName();
        String className = classFullyName.substring(classFullyName
                .lastIndexOf('.') + 1);
        // System.out.println(Config.LOG_PREFIX + className + "|" +
        // message);
        Log.d(className,"Viettel OTT Commons : "+ WindmillConfiguration.applicationVersion + " | " + message);
    }

    public static volatile boolean sLogEnabled = false;

    public static void d(String tag, String message) {
        Log.d(tag, message);
    }

    public static void i(String tag, String message) {
        Log.i(tag, message);
    }

    public static void w(String tag, String message) {
        Log.w(tag, message);
    }

    public static void e(String tag, String message) {
        Log.e(tag, message);
    }

    public static void d(String tag, String message, Throwable throwable) {
        Log.d(tag, message, throwable);
    }

    public static void i(String tag, String message, Throwable throwable) {
        Log.i(tag, message, throwable);
    }

    public static void w(String tag, String message, Throwable throwable) {
        Log.w(tag, message, throwable);
    }

    public static void e(String tag, String message, Throwable throwable) {
        Log.e(tag, message, throwable);
    }
}
