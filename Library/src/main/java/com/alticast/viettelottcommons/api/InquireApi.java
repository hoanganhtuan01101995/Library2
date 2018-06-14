package com.alticast.viettelottcommons.api;

public interface InquireApi {

    public void getMyWalletBalance(String access_token, WindmillCallback callback);

    public void getInquireTopupMethod(String access_token, WindmillCallback callback);

    public void getTopupHistory(String access_token, long since, long unti, int offset, int limit,
                                WindmillCallback callback);

}
