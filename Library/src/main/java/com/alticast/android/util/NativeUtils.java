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
package com.alticast.android.util;

public class NativeUtils {

    private static final Log LOG = Log.createLog("NativeUtils");

    static {
        System.loadLibrary("altiutils");
    }

    public static String getCPUArchitecture() {
    	return getCPUArchitecture0();
    }

    public static String getCPUFeatures() {
    	return getCPUFeatures0();
    }
    
    public static boolean isNeonSupported() {
        if (isNeonSupported0()) {
            if (Log.MSG) {
                LOG.printMsg("support NEON");
            }
            return true;
        }
        else {
            if (Log.MSG) {
                LOG.printMsg("does not support NEON");
            }
            return false;
        }
    }

    private static native String getCPUArchitecture0();
    private static native String getCPUFeatures0();
    private static native boolean isNeonSupported0();
}
