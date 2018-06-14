package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.Menu;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/8/2016.
 */
public class MenuListRes {
    private int total = 0;
    private ArrayList<Menu> data = null;

    public int getTotal() {
        return total;
    }

    public ArrayList<Menu> getData() {
        return data;
    }
}
