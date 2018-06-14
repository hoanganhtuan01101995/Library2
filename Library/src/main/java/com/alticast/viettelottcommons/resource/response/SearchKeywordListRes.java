package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.SearchPopularWord;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class SearchKeywordListRes {
    private int total = 0;
    private ArrayList<SearchPopularWord> data = null;

    public int getTotal() {
        return total;
    }

    public ArrayList<SearchPopularWord> getData() {
        return data;
    }
}
