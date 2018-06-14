package com.alticast.viettelottcommons.resource.response;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/11/2016.
 */
public class UserDataListRes {
    private int total = 0;
    private ArrayList<UserDataRes> data = null;

    public int getTotal() {
        return total;
    }

    public ArrayList<UserDataRes> getData() {
        return data;
    }
}
