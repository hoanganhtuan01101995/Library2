package com.alticast.viettelottcommons.serviceMethod.acms.prePayment;


import com.alticast.viettelottcommons.resource.ChargingMethod;
import com.alticast.viettelottcommons.resource.TopupHistory;
import com.alticast.viettelottcommons.resource.Wallet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InquireMethod {

    @GET("/api1/payments/wallet/inquire_wallet")
    Call<Wallet> getMyWalletBalance(@Query("access_token") String access_token);


    @GET("/api1/payments/wallet/inquire_topup_method")
    Call<ChargingMethod> getInquireTopupMethod(@Query("access_token") String access_token);

    @GET("/api1/payments/wallet/inquire_topup_history")
    Call<TopupHistory> getTopupHistory(@Query("access_token") String access_token,
                                       @Query("since") long since,
                                       @Query("until") long until,
                                       @Query("offset") int offset,
                                       @Query("limit") int limit);
}
