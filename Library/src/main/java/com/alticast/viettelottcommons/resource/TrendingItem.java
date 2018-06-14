package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;

public class TrendingItem {

    public static final String RECOM_ITEM_VALUE = "ITEM_VALUE";
    public static final String RECOM_CATEGORY_PATH_ID = "CATEGORY_PATH_ID";

    public static final int RECOM_SCENARIO_TRENDING = 1;
    public static final int RECOM_SCENARIO_TOP_PICK = 2;
    public static final int RECOM_SCENARIO_WATCH = 3;

    public static final int RECOM_SCENARIO_WATCH_TYPE_A = 4;
    public static final int RECOM_SCENARIO_WATCH_TYPE_B = 5;
    public static final int RECOM_SCENARIO_WATCH_TYPE_C = 6;

    public static final int RECOM_SCENARIO_TOP_PICK_ID = 33;
    public static final int RECOM_SCENARIO_WATCHED_ID = 34;

    public static final String RCM_TOP_VOD = "TOP_VOD";

    TrendingBasisInfo basisInfo;
    ArrayList<TrendingParam> properties;
    int itemSetId;//: 30,
    int scenarioId;//: 29,

    public int getTypeRecommend() {
        return typeRecommend;
    }

    public void setTypeRecommend(int typeRecommend) {
        this.typeRecommend = typeRecommend;
    }

    private int typeRecommend;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    String clickUrl;//
    // :"/so-logging/log/client?custId=1000375346&pcId=1&nonce=48f6cb0da789a6ddb8ac&dpId=
    // VT_STB_042_HOME_BANNER_VOD&scnId=29&tgId=0&itemSetId=30&itemValue=583801be718c0f4158290ac7&click=1"

    public TrendingBasisInfo getBasisInfo() {
        return basisInfo;
    }

    public void setBasisInfo(TrendingBasisInfo basisInfo) {
        this.basisInfo = basisInfo;
    }

    public ArrayList<TrendingParam> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<TrendingParam> properties) {
        this.properties = properties;
    }

    public int getItemSetId() {
        return itemSetId;
    }

    public void setItemSetId(int itemSetId) {
        this.itemSetId = itemSetId;
    }

    public int getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }


}
