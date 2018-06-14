package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.AdditionalObject;

import java.util.ArrayList;

/**
 * Created by duyuno on 8/18/17.
 */
public class AdditionalRes {
    private int total;
    private ArrayList<AdditionalObject> data;

    public int getTotal() {
        return total;
    }

    public ArrayList<AdditionalObject> getData() {
        return data;
    }
}
