package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.Category;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/9/2016.
 */
public class CategoryListRes {
    private int total = 0;
    private ArrayList<Category> data = null;

    public int getTotal() {
        return total;
    }

    public ArrayList<Category> getData() {
        return data;
    }
}
