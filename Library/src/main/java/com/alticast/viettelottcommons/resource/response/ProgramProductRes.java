package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.Vod;

import java.util.ArrayList;

/**
 * Created by duyuno on 8/24/17.
 */
public class ProgramProductRes {
    private int total;
    private ArrayList<Vod> data;

    public int getTotal() {
        return total;
    }

    public ArrayList<Vod> getData() {
        return data;
    }
}
