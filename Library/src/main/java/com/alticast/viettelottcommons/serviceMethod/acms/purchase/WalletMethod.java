package com.alticast.viettelottcommons.serviceMethod.acms.purchase;

import com.alticast.viettelottcommons.resource.AvailableMethod;
import com.alticast.viettelottcommons.resource.response.InquireWalletRes;
import com.alticast.viettelottcommons.resource.response.LookUpProduct;
import com.alticast.viettelottcommons.resource.response.PaidAmountRes;
import com.alticast.viettelottcommons.resource.response.PurchaseListRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mc.kim on 8/5/2016.
 */
public interface WalletMethod {
    final String ACCESS_TOKEN = "access_token";

    @GET("/api1/payments/wallet/inquire_topup_method")
    Call<PaidAmountRes> inquireTopupMethod(@Query(ACCESS_TOKEN) String access_token);


    @GET("/api1/payments/wallet/inquire_wallet")
    Call<InquireWalletRes> inquireWallet(@Query(ACCESS_TOKEN) String access_token);


    @GET("/api1/payments/wallet/inquire_topup_history")
    Call<LookUpProduct> inquireTopupHistory(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("multilang") boolean multilang
    );

}

