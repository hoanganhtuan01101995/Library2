package com.alticast.viettelottcommons.resource;

/**
 * Created by mc.kim on 8/8/2016.
 */
public class Promotion {
    private String id = null;
    private String license_start = null;
    private String license_end = null;
    private int rental_period = 0;
    private int discount_rate = 0;

    public String getId() {
        return id;
    }

    public String getLicense_start() {
        return license_start;
    }

    public String getLicense_end() {
        return license_end;
    }

    public int getRental_period() {
        return rental_period;
    }

    public int getDiscount_rate() {
        return discount_rate;
    }
}
