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

import android.os.Environment;

import com.alticast.viettelottcommons.WindmillConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Log {

    public static final int LEVEL_DBG = 0;
    public static final int LEVEL_MSG = 1;
    public static final int LEVEL_WARN = 2;
    public static final int LEVEL_ERR = 3;
    public static final int LEVEL_NONE = 4;

    private static final int LOG_LEVEL = WindmillConfiguration.LOG_LEVEL;
//    private static final int LOG_LEVEL = LEVEL_MSG;

    public static final boolean DBG = LOG_LEVEL <= LEVEL_DBG;
    public static final boolean MSG = LOG_LEVEL <= LEVEL_MSG;
    public static final boolean WARN = LOG_LEVEL <= LEVEL_WARN;
    public static final boolean ERR = LOG_LEVEL <= LEVEL_ERR;

    private static final String TAG = "viettel"; // otv

    private final String moduleName;
    
    // for devices that doesn't support system out.
    private final boolean systemOutput = false;
    
    private String fullFilePath = null;
    private File file = null;
    private BufferedWriter bufferedWriter;
    private final long MAX_LOG_SIZE = 2000000L;
    
    public static Log createLog(String moduleName) {
        return new Log(moduleName);
    }

    public static Log createLog() {
        return new Log(null);
    }

    private Log(String moduleName) {
        if (moduleName == null) {
            this.moduleName = "";
        }
        else {
            this.moduleName = "[" + TAG + "/" + moduleName + "] ";
        }
    }

    public void printEx(Throwable tr) {
        if (ERR) {
            println(LEVEL_ERR, getStackTraceString(tr));
        }
    }

    public void printDbg(String s) {
        if (DBG) {
            println(LEVEL_DBG, s);
        }
    }

    public void printMsg(String s) {
        if (MSG) {
            println(LEVEL_MSG, s);
        }
//        if (Def.SUPPORT_DEVELOPER_OPTION) {
//            try {
//                if (PreferenceManager.getInstance().readPreference(PreferenceManager.KEY_SAVE_LOG, false)) {
//                    writeLog(moduleName + s);
//                }
//            }
//            catch (Exception e) {
//
//            }
//        }
    }

    public void printLongMsg(String s) {
        if (MSG) {
            printLongln(LEVEL_MSG, s);
        }
    }
    
    public void printMsgByTime(String s) {
//        if (Def.ENABLE_TEST_CHECK_TIME) {
//            s = s + " CHECK_TIME-(" + System.currentTimeMillis() + ")";
//        }
        printMsg(s);
    }
    
    public void printLongDbg(String s) {
        if (DBG) {
            printLongln(LEVEL_DBG, s);
        }
    }

    public void printWarn(String s) {
        if (WARN) {
            println(LEVEL_WARN, s);
        }
    }

    public void printErr(String s) {
        if (ERR) {
            println(LEVEL_ERR, s);
        }
    }
    
    protected void println(int type, String message) {
        // if message is too long, cut!
        if (message != null && message.length() > 1000) {
            message = message.substring(0, 999);
        }
        
        if (systemOutput) {
            System.out.println(moduleName + message);
        }
        else {
            switch (type) {
                case LEVEL_DBG :
                    android.util.Log.d(TAG, moduleName + message);
                    break;
                case LEVEL_MSG :
                    android.util.Log.i(TAG, moduleName + message);
                    break;
                case LEVEL_WARN :
                    android.util.Log.w(TAG, moduleName + message);
                    break;
                case LEVEL_ERR :
                    android.util.Log.e(TAG, moduleName + message);
                    break;
                case LEVEL_NONE :
                    break;
            }
        }
    }

    protected void printLongln(int type, String message) {
        int startIndex = 0;
        int endIndex = 0;
        int maxLogSize = 999;
        for (int i = 0; i <= Math.ceil(message.length() / maxLogSize); i++) {
            startIndex = i * maxLogSize;
            endIndex = (i + 1) * maxLogSize;
            endIndex = endIndex > message.length() ? message.length() : endIndex;
            if (systemOutput) {
                System.out.println(((i == 0) ? moduleName : "") + message.substring(startIndex, endIndex));
            }
            else {
                switch (type) {
                    case LEVEL_DBG :
                        android.util.Log.d(TAG, ((i == 0) ? moduleName : "") + message.substring(startIndex, endIndex));
                        break;
                    case LEVEL_MSG :
                        android.util.Log.i(TAG, ((i == 0) ? moduleName : "") + message.substring(startIndex, endIndex));
                        break;
                }
            }
        }
    }
    
    private String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    private void writeLog(String log) {
        fullFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "log_otm.txt";
        file = new File(fullFilePath);
        
        if (file.exists() == false) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
            }
        }
        else {
            if (file.length() > MAX_LOG_SIZE) {
                return;
            }
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(fullFilePath, true));
                bufferedWriter.write(log);
                bufferedWriter.write("\n");
                bufferedWriter.flush();
                bufferedWriter.close();
            }
            catch (FileNotFoundException e) {
            }
            catch (IOException e) {
            }
        }
    }
    
    public void printFunctionStack()
    {
    	StringBuffer stacktrace = new StringBuffer();
    	StackTraceElement[] stackTrace = new Exception().getStackTrace();
    	for(int x=0; x<stackTrace.length; x++)
    	{
    	 stacktrace.append(stackTrace[x].toString() + "\n");
    	}
    	printMsg(stacktrace.toString());
    }
}
