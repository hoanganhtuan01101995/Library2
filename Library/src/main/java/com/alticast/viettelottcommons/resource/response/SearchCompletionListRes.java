package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.SearchCompleteSuggestion;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class SearchCompletionListRes {
    private int took= 0;
    private int total = 0;
    private ArrayList<SearchCompleteSuggestion> data = null;

    public int getTook() {
        return took;
    }

    public int getTotal() {
        return total;
    }

    public ArrayList<SearchCompleteSuggestion> getData() {
        return data;
    }
}
