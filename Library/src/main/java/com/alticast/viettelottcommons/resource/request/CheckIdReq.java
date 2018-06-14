package com.alticast.viettelottcommons.resource.request;

/**
 * Created by mc.kim on 8/3/2016.
 */
public class CheckIdReq {
    private String id = null;
    private String client_id = null;
    private String hash = null;

    public CheckIdReq(String id, String client_id, String hash) {
        this.id = id;
        this.client_id = client_id;
        this.hash = hash;
    }
}
