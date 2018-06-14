package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.TrendingDp;

public class TrendingRes {
    String status;
    String customerId;
    String pcId;
    String rqId;
    TrendingDp dp;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPcId() {
        return pcId;
    }

    public void setPcId(String pcId) {
        this.pcId = pcId;
    }

    public String getRqId() {
        return rqId;
    }

    public void setRqId(String rqId) {
        this.rqId = rqId;
    }

    public TrendingDp getDp() {
        return dp;
    }

    public void setDp(TrendingDp dp) {
        this.dp = dp;
    }


}
