package com.alticast.viettelottcommons.api;

public interface ScratchCardApi {
    public void requsetTopUpByScratchCard(String access_token, String serial, String pin, String promotionId, WindmillCallback callback);
}
