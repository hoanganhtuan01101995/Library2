package com.alticast.viettelottcommons.resource;

public class TrendingScenarioResultList {

    int scenarioId;//: 11,
    int targetId;//: 0,
    String status;//: "Success",
    String nonce;//: "10bcbc987c8c4ca8da93",
    TrendingDisplay display;

    public int getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public TrendingDisplay getDisplay() {
        return display;
    }

    public void setDisplay(TrendingDisplay display) {
        this.display = display;
    }

}

