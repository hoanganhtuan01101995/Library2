package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.SearchHistoryWord;
import com.alticast.viettelottcommons.resource.SearchPopularWord;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class SearchKeywordHistoryListRes {
    private int total = 0;
    private ArrayList<SearchHistoryWord> data = null;

    public int getTotal() {
        return total;
    }

    public ArrayList<SearchHistoryWord> getData() {
        return data;
    }
}
