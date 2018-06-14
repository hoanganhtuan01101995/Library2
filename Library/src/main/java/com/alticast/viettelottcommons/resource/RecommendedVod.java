/*
 *  Copyright (c) 2012 Alticast Corp.
 *  All rights reserved. http://www.alticast.com/
 *
 *  This software is the confidential and proprietary information of
 *  Alticast Corp. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into
 *  with Alticast.
 */
package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>RecommendedVod</code>
 *
 * @author S H Chung
 * @since 2012. 11. 16.
 */
public class RecommendedVod {

    // 데이터 filtering key 값
    private static final String KEY = "ITEM_VALUE";

    private final String STATUS_R0101 = "Success";
    private final String STATUS_R0102 = "NoScenarioResult";
    private final String STATUS_R0201 = "NoCustomer";
    private final String STATUS_R0202 = "UnableToQueryCustomer";
    private final String STATUS_R0203 = "DoNotRecommendCustomer";
    private final String STATUS_R0204 = "NoDP";
    private final String STATUS_R0205 = "MalformedRequest";
    private final String STATUS_R0206 = "UnknownError";

    private final String CODE_R0101 = "R0101";
    private final String CODE_R0102 = "R0102";
    private final String CODE_R0201 = "R0201";
    private final String CODE_R0202 = "R0202";
    private final String CODE_R0203 = "R0203";
    private final String CODE_R0204 = "R0204";
    private final String CODE_R0205 = "R0205";
    private final String CODE_R0206 = "R0206";

    /**
     * 추천 상태
     */
    public String status;

    /**
     * 추천 상태 코드
     */
    private String code;

    /**
     * 고객 아이디
     */
    public String customerId;

    /**
     * PC 아이디
     */
    public String pcId;

    /**
     * DP별 추천 응답
     */
    public Dp dp;

    public String rqId;

    /**
     * 추천 상태 성공 여부
     */
    private boolean succeeded;

    /**
     * 응답 데이터 성공 여부를 확인한다.
     *
     * @return status of response
     */
    public boolean isSucceeded() {
        // if (status.equalsIgnoreCase(STATUS_R0101) &&
        // dp.status.equalsIgnoreCase(STATUS_R0101)) {
        // succeeded = true;
        // }
        if (status.equalsIgnoreCase(STATUS_R0101)) {
            if (dp.status.equalsIgnoreCase(STATUS_R0101) || dp.status.equalsIgnoreCase(STATUS_R0102)) {
                succeeded = true;
            }
        }

        return succeeded;
    }

    /**
     * 추천 상태 코드를 조회한다.
     *
     * @return status of response code
     */
    public String getStausCode() {
        if (status.equalsIgnoreCase(STATUS_R0101)) {
            if (dp.status.equalsIgnoreCase(STATUS_R0101)) {
                code = CODE_R0101;
            } else if (dp.status.equalsIgnoreCase(STATUS_R0102)) {
                code = CODE_R0102;
            }
        } else if (status.equalsIgnoreCase(STATUS_R0201)) {
            code = CODE_R0201;
        } else if (status.equalsIgnoreCase(STATUS_R0202)) {
            code = CODE_R0202;
        } else if (status.equalsIgnoreCase(STATUS_R0203)) {
            code = CODE_R0203;
        } else if (status.equalsIgnoreCase(STATUS_R0204)) {
            code = CODE_R0204;
        } else if (status.equalsIgnoreCase(STATUS_R0205)) {
            code = CODE_R0205;
        } else if (status.equalsIgnoreCase(STATUS_R0206)) {
            code = CODE_R0206;
        }
        return code;
    }

    /**
     * 시나리오 조회 결과를 얻는다.
     *
     * @return array of ScenarioResult object
     */
    private ScenarioResult[] getScenarioResultList() {
        if (dp.scenarioResultList == null || dp.scenarioResultList.length <= 0) {
            return null;
        }
        List list = new ArrayList();
        int len = dp.scenarioResultList.length;
        for (int i = 0; i < len; i++) {
            if (dp.scenarioResultList[i].status.equalsIgnoreCase(STATUS_R0101)) {
                list.add(dp.scenarioResultList[i]);
            }
        }

        if (list == null || list.size() <= 0) {
            return null;
        }

        ScenarioResult[] scenarioResult = (ScenarioResult[]) list.toArray(new ScenarioResult[list.size()]);

        return scenarioResult;
    }

    /**
     * 추천 item 목록을 얻는다.
     *
     * @return array of TrendingItemList object
     */
    private ItemList getItemList() {

        ItemList itemList = dp.itemList;

        return itemList;
    }

    /**
     * 추천 item을 얻는다.
     *
     * @return array of Item object
     */
    private Item[] getItems() {
        if (getItemList() == null) {
            return null;
        }

        Item[] items = getItemList().items;

        return items;
    }

    /**
     * 추천 근거를 얻는다.
     *
     * @return array of TrendingBasisInfo object
     */
    private BasisInfo[] getBasisInfo() {
        if (getItems() == null) {
            return null;
        }

        Item[] items = getItems();
        int len = items.length;
        BasisInfo[] basisInfo = null;
        basisInfo = new BasisInfo[len];
        for (int i = 0; i < len; i++) {
            basisInfo[i] = items[i].basisInfo;
        }

        return basisInfo;
    }

    /**
     * 추천 근거 항목을 얻는다.
     *
     * @return array of Basis object
     */
    private Basis[][] getBasisList() {
        if (getBasisInfo() == null) {
            return null;
        }

        BasisInfo[] basisInfo = getBasisInfo();
        int len = basisInfo.length;

        Basis[][] basis = null;

        basis = new Basis[len][];

        for (int i = 0; i < len; i++) {
            basis[i] = getBasisInfo()[i].basisList;
        }

        return basis;
    }

    /**
     * 추천 근거 항목을 얻는다.
     *
     * @return array of Basis object
     */
    public Basis[] getBasis() {
        if (getBasisList() == null) {
            return null;
        }

        List list = new ArrayList();
        // ScenarioResult[] scenarioResultList = getScenarioResultList();
        Basis[][] basisList = getBasisList();
        int len = basisList.length;

        for (int i = 0; i < len; i++) {
            // String loggingUrl = scenarioResultList[i].loggingUrl;
            for (int j = 0; j < basisList[i].length; j++) {
                if (basisList[i][j].key.equalsIgnoreCase(KEY)) {
                    basisList[i][j].setLoggingUrl(getLoggingUrl());
                    list.add(basisList[i][j]);
                }
            }
        }

        if (list == null || list.size() <= 0) {
            return null;
        }

        Basis[] basis = (Basis[]) list.toArray(new Basis[list.size()]);


        return basis;
    }

    // public String[] getLoggingUrlList() {
    // if (getScenarioResultList() == null || getScenarioResultList().length <=
    // 0) {
    // return null;
    // }
    // int len = getScenarioResultList().length;
    // String[] loggingUrlList = new String[len];
    //
    // for (int i = 0; i < len; i++) {
    // loggingUrlList[i] = getScenarioResultList()[i].loggingUrl;
    // }
    //
    // return loggingUrlList;
    // }

    /**
     * 로깅 URL을 얻는다.
     *
     * @return logging url string
     */
    public String getLoggingUrl() {
        if (getItems() == null || getItems().length <= 0) {
            return null;
        }
        int len = getItems().length;
        String loggingUrl = null;

        for (int i = 0; i < len; i++) {
            loggingUrl = getItems()[i].clickUrl;
        }

        return loggingUrl;
    }

    public String toString() {
        StringBuffer bf = new StringBuffer();
        bf.append("Recommend Vod ==============================").append("\n").append("   status : ").append(status).append("\n").append("   status code : ")
                .append(getStausCode()).append("\n").append("   customer id : ").append(customerId).append("\n").append("   pc id : ").append(pcId)
                .append("\n").append("   rq id : ").append(rqId).append("\n");

        if (dp != null) {
            bf.append(dp.toString());
        }

        return bf.toString();
    }

    public static class Dp {

        /**
         * TrendingDp id
         */
        public String dpId;

        /**
         * DP별 추천 상태
         */
        public String status;

        /**
         * 시나리오 조회 결과
         */
        public ScenarioResult[] scenarioResultList;
        public ItemList itemList;

        public String toString() {
            StringBuffer bf = new StringBuffer();
            bf.append("Content :").append("\n").append("   dp id : ").append(dpId).append("\n").append("   status : ").append(status).append("\n");

            if (scenarioResultList != null && scenarioResultList.length > 0) {
                for (int i = 0; i < scenarioResultList.length; i++) {
                    bf.append(scenarioResultList[i]);
                }
            }

            return bf.toString();
        }

        public Dp() {
        }
    }

    public static class ScenarioResult {

        /**
         * 시나리오 id
         */
        public String scenarioId;

        /**
         * 고객군 id
         */
        public int targetId;

        /**
         * 시나리오 조회 결과 상태
         */
        public String status;

        public String nonce;

        /** 추천 item 목록 */

        /** 로깅 URL */

        /**
         * 출력 형식 설정 정보
         */
        public Display display;

        public ScenarioResult() {
        }
    }

    public static class Display {

        /**
         * display 상태
         */
        public String status;

        /**
         * 새창 사용 여부
         */
        public boolean newWindow;

        public String toString() {
            StringBuffer bf = new StringBuffer();
            bf.append("TrendingDisplay :").append("\n").append("   status : ").append(status).append("\n").append("   newWindow : ").append(newWindow).append("\n");

            return bf.toString();
        }

        public Display() {
        }
    }

    public static class ItemList {

        /**
         * item set id
         */
        public int itemSetId;

        /**
         * 추천 item
         */
        public Item[] items;

        public String toString() {
            StringBuffer bf = new StringBuffer();
            bf.append("Item List :").append("\n").append("   item_set id :").append(itemSetId).append("\n");
            if (items != null && items.length > 0) {
                for (int i = 0; i < items.length; i++) {
                    bf.append(items[i]);
                }
            }

            return bf.toString();
        }

        public ItemList() {
        }
    }

    public static class Item {

        /**
         * 추천 근거
         */
        public BasisInfo basisInfo;
        public String clickUrl;
        public int scenarioId;
        public int itemSetId;

        public String toString() {
            StringBuffer bf = new StringBuffer();
            bf.append("Item : ").append("\n");
            if (basisInfo != null) {
                bf.append(basisInfo);
            }

            return bf.toString();
        }

        public Item() {
        }
    }

    public static class BasisInfo {

        /**
         * 추천 근거 항목
         */
        public Basis[] basisList;

        public String toString() {
            StringBuffer bf = new StringBuffer();
            bf.append("TrendingBasisInfo : ").append("\n");
            if (basisList != null && basisList.length > 0) {
                for (int i = 0; i < basisList.length; i++) {
                    bf.append(basisList[i]);
                }
            }

            return bf.toString();
        }

        public BasisInfo() {
        }
    }

    public static class Basis {

        /**
         * 추천 근거 key
         */
        public String key;

        /**
         * 추천 근거 value
         */
        public String value;

        /**
         * 로깅 URL
         */
        public String loggingUrl;

        public void setLoggingUrl(String loggingUrl) {
            this.loggingUrl = loggingUrl;
        }

        public String getLoggingUrl() {
            return loggingUrl;
        }

        public String toString() {
            StringBuffer bf = new StringBuffer();
            bf.append("Basis :").append("\n").append("   key : ").append(key).append("\n").append("   value : ").append(value).append("\n")
                    .append("   loggingUrl : ").append(loggingUrl).append("\n").append("=============================").append("\n");

            return bf.toString();
        }

        public Basis() {
        }
    }

}
