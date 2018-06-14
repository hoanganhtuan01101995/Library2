package com.alticast.viettelottcommons.resource.request;

/**
 * Created by mc.kim on 8/3/2016.
 */
public class ResetPasswordReq {
    private String id = null;
    private String code = null;
    private String client_id = null;
    private String hash = null;
    private String password = null;

    public ResetPasswordReq(String id, String code, String client_id, String hash) {
        this.id = id;
        this.code = code;
        this.client_id = client_id;
        this.hash = hash;
    }

    public ResetPasswordReq(String id, String code, String client_id, String hash, String password) {
        this.id = id;
        this.code = code;
        this.client_id = client_id;
        this.hash = hash;
        this.password = password;
    }
}
