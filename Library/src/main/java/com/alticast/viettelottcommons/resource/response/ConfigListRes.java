package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.Platform;

import java.util.ArrayList;

/**
 * Created by mc.kim on 10/5/2016.
 */
public class ConfigListRes {
    private int total = 0;
    private ArrayList<Platform> data = null;


    public int getTotal() {
        return total;
    }

    public ArrayList<Platform> getData() {
        return data;
    }
}
