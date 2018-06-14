package com.alticast.viettelottcommons.resource.response;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/22/2016.
 */
public class PackageListRes {
    private int total = 0;

    private ArrayList<PackageInfoRes> data = null;

    public int getTotal() {
        return total;
    }

    public ArrayList<PackageInfoRes> getData() {
        return data;
    }
}
