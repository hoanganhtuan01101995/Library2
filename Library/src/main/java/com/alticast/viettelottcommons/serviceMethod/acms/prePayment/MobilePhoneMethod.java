package com.alticast.viettelottcommons.serviceMethod.acms.prePayment;


import com.alticast.viettelottcommons.resource.ChargedResult;
import com.alticast.viettelottcommons.resource.OTPresult;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MobilePhoneMethod {


    @POST("/api1/payments/wallet/request_otp")
    Call<Void> requestOTP(@Query("access_token") String accessToken, @Query("phone_number") String phone_number);


    @POST("/api1/payments/wallet/topup_by_phone")
    Call<ChargedResult> requestTopupByMobilePhone(@Query("access_token") String accessToken,
                                                  @Query("phone_number") String phone_number,
                                                  @Query("otp") String otp,
                                                  @Query("topup_amount") int topup_amount,
                                                  @Query("bonus_rate") int bonus_rate,
                                                  @Query("bonus_amount") int bonus_amount,
                                                  @Query("promotion_id") String promotion_id,
                                                  @Query("hash") String hash
    );

}
