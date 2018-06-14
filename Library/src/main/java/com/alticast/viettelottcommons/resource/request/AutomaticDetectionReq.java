package com.alticast.viettelottcommons.resource.request;

/**
 * Created by mc.kim on 8/3/2016.
 */
public class AutomaticDetectionReq {
    private String client_ip = null;
    private String client_id = null;
    private String hash = null;
    private MyDeviceInfo device = null;

    public AutomaticDetectionReq(String client_ip, String client_id, String hash, MyDeviceInfo device) {
        this.client_ip = client_ip;
        this.client_id = client_id;
        this.hash = hash;
        this.device = device;
    }
}
