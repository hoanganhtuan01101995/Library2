package com.alticast.viettelottcommons.api;

public interface MobilePhoneApi {
    public void requestTopupByMobilePhone(String access_token, String phoneNumber, String otp, int topup_amount,
                                          int bonus_rate, int bonus_amount, String promotionId, WindmillCallback callback);

    public void requestOtp(String access_token, String phoneNumbe, WindmillCallback callback);
}
