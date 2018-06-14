package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.Price;

/**
 * Created by mc.kim on 8/8/2016.
 */
public class PaymentResultRes {
    private String id = null;
    private String date = null;
    private Price total_amount = null;

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public Price getTotal_amount() {
        return total_amount;
    }
}
