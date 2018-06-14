package com.alticast.viettelottcommons.resource.request;

/**
 * Created by mc.kim on 8/3/2016.
 */
public class CheckAuthenticationCodeReq {

    private String phone_no = null;
    private String client_id = null;
    private String device_udid = null;
    private String hash = null;


    public CheckAuthenticationCodeReq(String phone_no, String client_id, String device_udid, String hash) {
        this.phone_no = phone_no;
        this.client_id = client_id;
        this.device_udid = device_udid;
        this.hash = hash;
    }
}
