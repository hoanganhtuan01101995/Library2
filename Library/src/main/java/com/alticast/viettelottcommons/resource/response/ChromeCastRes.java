package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.Mail;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class ChromeCastRes {


    private String requestId  = null;
    private ArrayList<String> glbAddress  = new ArrayList<>();
    private String version    = null;

    public String getRequestId() {
        return requestId;
    }

    public ArrayList<String> getGlbAddress() {
        return glbAddress;
    }

    public String getVersion() {
        return version;
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getErrorString() {
        return errorString;
    }

    private String resultCode  = null;
    private String errorString  = null;

}
