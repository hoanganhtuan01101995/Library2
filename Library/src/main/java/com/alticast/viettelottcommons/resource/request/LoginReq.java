package com.alticast.viettelottcommons.resource.request;

/**
 * Created by mc.kim on 8/3/2016.
 */
public class LoginReq {
    private String id = null;
    private String password = null;
    private String client_id = null;
    private MyDeviceInfo device = null;

    public LoginReq(String id, String password, String client_id, MyDeviceInfo device) {
        this.id = id;
        this.password = password;
        this.client_id = client_id;
        this.device = device;
    }

}
