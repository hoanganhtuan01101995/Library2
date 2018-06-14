package com.alticast.viettelottcommons.serviceMethod.acms.prePayment;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mc.kim on 3/8/2016.
 */
public interface ScrachMethod {

    @POST("/api1/payments/wallet/topup_by_scard")
    Call<Object> topupByScard(@Query("access_token") String access_token,
                              @Query("card_serial") String card_serial,
                              @Query("card_pin") String card_pin,
                              @Query("promotion_id") String promotion_id);

}
