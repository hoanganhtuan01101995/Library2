package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;

public class TrendingDp {

    String dpId;
    String status; //: "Success"
    ArrayList<TrendingScenarioResultList> scenarioResultList;
    TrendingItemList itemList;

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<TrendingScenarioResultList> getScenarioResultList() {
        return scenarioResultList;
    }

    public void setScenarioResultList(ArrayList<TrendingScenarioResultList> scenarioResultList) {
        this.scenarioResultList = scenarioResultList;
    }

    public TrendingItemList getItemList() {
        return itemList;
    }

    public void setItemList(TrendingItemList itemList) {
        this.itemList = itemList;
    }

    public static class TrendingItemList {
        public ArrayList<TrendingItem> getItems() {
            return items;
        }

        public void setItems(ArrayList<TrendingItem> items) {
            this.items = items;
        }

        ArrayList<TrendingItem> items;
    }
}

