package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;

public class TrendingBasisInfo {

    ArrayList<TrendingParam> basisList;
    public static final String ITEM_VALUE = "ITEM_VALUE";

    public ArrayList<TrendingParam> getBasisList() {
        return basisList;
    }

    public void setBasisList(ArrayList<TrendingParam> basisList) {
        this.basisList = basisList;
    }

    public String getITEM_VALUE() {
        if (basisList == null || basisList.size() == 0) return "";
        for (TrendingParam trendingParam : basisList) {
            if (trendingParam.getKey().equalsIgnoreCase(ITEM_VALUE)) {
                return trendingParam.getValue();
            }
        }
        return "";
    }
}
