package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.CheckPoint;
import com.alticast.viettelottcommons.resource.Gsdm;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by duyuno on 10/10/16.
 */
public class PrepareChannelRes {
    public String requestId;
    public ArrayList<String> glbAddress;

    public String getRequestId() {
//        try {
//            return URLDecoder.decode(requestId, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return "";
        return requestId;
    }

    public ArrayList<String> getGlbAddress() {
        return glbAddress;
    }
}
