package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.ChannelProduct;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/10/2016.
 */
public class ChannelRes {
    private int total = 0;
    private ArrayList<ChannelProduct> data = null;

    public int getTotal() {
        return total;
    }

    public ArrayList<ChannelProduct> getData() {
        return data;
    }
}
