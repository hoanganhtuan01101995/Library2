package com.alticast.viettelottcommons.resource.response;

/**
 * Created by duyuno on 4/4/17.
 */
public class VerifyCodeRes {
    private String code;
    private String created_time;
    private long timeout;

    public String getCode() {
        return code;
    }

    public String getCreated_time() {
        return created_time;
    }

    public long getTimeout() {
        return timeout;
    }
}
