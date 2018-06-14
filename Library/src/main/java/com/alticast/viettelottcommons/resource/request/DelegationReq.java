package com.alticast.viettelottcommons.resource.request;

/**
 * Created by mc.kim on 8/3/2016.
 */
public class DelegationReq {

    private String refresh_token = null;
    private String client_id = null;
    private String hash = null;

    public DelegationReq(String refresh_token, String client_id, String hash) {
        this.refresh_token = refresh_token;
        this.client_id = client_id;
        this.hash = hash;
    }
}

