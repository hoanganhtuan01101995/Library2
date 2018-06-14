package com.alticast.viettelottcommons.resource.request;

/**
 * Created by duyuno on 4/4/17.
 */
public class VerifyCodeReq {
    public String phone_no;
    public String code;
    public String client_id;
    public String hash;

    public VerifyCodeReq(String phone_no, String code, String client_id, String hash) {
        this.phone_no = phone_no;
        this.code = code;
        this.client_id = client_id;
        this.hash = hash;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
