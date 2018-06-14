package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class SearchCategory {
    private int total = 0;
    private int[] docs = null;
    private ArrayList<SearchItem> data = null;

    public ArrayList<SearchItem> getData() {
        return data;
    }

    public int getTotal() {
        return total;
    }

    public int[] getDocs() {
        return docs;
    }

}
