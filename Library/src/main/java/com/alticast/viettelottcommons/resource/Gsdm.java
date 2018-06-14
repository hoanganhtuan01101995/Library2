package com.alticast.viettelottcommons.resource;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by duyuno on 10/10/16.
 */
public class Gsdm {
    public String vod_request_id;
    public ArrayList<String> glb_addresses;

    public String getVod_request_id() {
//        try {
//            return URLDecoder.decode(vod_request_id, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return "";
        return vod_request_id;
    }

    public ArrayList<String> getGlb_addresses() {
        return glb_addresses;
    }
}
