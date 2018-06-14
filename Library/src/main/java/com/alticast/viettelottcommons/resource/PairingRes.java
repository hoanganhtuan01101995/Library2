package com.alticast.viettelottcommons.resource;

/**
 * Created by mc.kim on 7/28/2016.
 */
public class PairingRes extends ApiError {


    private String family_id = null;
    private String device_id = null;
    private String code = null;

    public String getCode() {
        return code;
    }

    public String getFamilyId() {
        return family_id;
    }

    public String getDeviceId() {
        return device_id;
    }
}
