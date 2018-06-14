package com.alticast.viettelottcommons.serviceMethod.acms.prePayment;


import com.alticast.viettelottcommons.resource.ChargedResult;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ScratchCardMethod {

    @POST("/api1/payments/wallet/topup_by_scard")
    Call<ChargedResult> requestTopupByScratchCard(@Query("access_token") String accessToken,
                                                  @Query("card_serial") String card_serial,
                                                  @Query("card_pin") String card_pin,
                                                  @Query("promotion_id") String promotion_id
    );
}
