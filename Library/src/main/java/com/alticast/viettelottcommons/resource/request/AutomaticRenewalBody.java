package com.alticast.viettelottcommons.resource.request;

/**
 * Created by duyuno on 10/25/16.
 */
public class AutomaticRenewalBody {
    public String id;
    public String auto_renewal;// F: canncel, T: active

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuto_renewal() {
        return auto_renewal;
    }

    public void setAuto_renewal(String auto_renewal) {
        this.auto_renewal = auto_renewal;
    }
}
