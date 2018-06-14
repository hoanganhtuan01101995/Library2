package com.alticast.viettelottcommons.resource.request;

/**
 * Created by mc.kim on 8/3/2016.
 */
public class ChangePasswordReq {
    private String current_password = null;
    private String new_password = null;

    public ChangePasswordReq(String current_password, String new_password) {
        this.current_password = current_password;
        this.new_password = new_password;
    }
}
