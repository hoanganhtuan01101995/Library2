package com.alticast.viettelottcommons.resource.request;

/**
 * Created by mc.kim on 8/3/2016.
 */
public class RequestPairingReq {
    private String otp =  null;
    private String hash =  null;


    public RequestPairingReq(String otp, String hash) {
        this.otp = otp;
        this.hash = hash;
    }
}
