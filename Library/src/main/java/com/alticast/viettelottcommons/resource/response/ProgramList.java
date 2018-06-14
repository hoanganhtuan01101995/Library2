package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.Vod;

import java.util.ArrayList;

/**
 * Created by mc.kim on 7/28/2016.
 */
public class ProgramList {
    private int total = 0;
    private String categoryId = null;
    private ArrayList<Vod> data = null;

    public int getTotal() {
        return total;
    }

    public ArrayList<Vod> getData() {
        return data;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
