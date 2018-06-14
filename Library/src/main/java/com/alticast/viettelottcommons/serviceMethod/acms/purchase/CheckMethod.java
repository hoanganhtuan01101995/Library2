package com.alticast.viettelottcommons.serviceMethod.acms.purchase;

import com.alticast.viettelottcommons.resource.AvailableMethod;
import com.alticast.viettelottcommons.resource.response.AdditionalRes;
import com.alticast.viettelottcommons.resource.response.LookUpProduct;
import com.alticast.viettelottcommons.resource.response.PaidAmountRes;
import com.alticast.viettelottcommons.resource.response.PurchaseListRes;
import com.alticast.viettelottcommons.resource.response.WalletRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mc.kim on 8/5/2016.
 */
public interface CheckMethod {
    final String ACCESS_TOKEN = "access_token";

    @GET("/api1/payments/check_paid_amount")
    Call<PaidAmountRes> checkPaidAmount(@Query(ACCESS_TOKEN) String access_token,
                                        @Query("month") int month);


    @GET("/api1/payments/available_methods")
    Call<AvailableMethod> checkAvailableMethods(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("product_id") String product_id,
            @Query("product_category") String product_category,
            @Query("product_type") String product_type
    );


    @GET("/api1/purchases/lookup_product_basic")
    Call<LookUpProduct> lookupProductBasic(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("multilang") boolean multilang
    );

    @GET("/api1/purchases/lookup_product_catchup")
    Call<LookUpProduct> lookupProductCatchUp(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("multilang") boolean multilang
    );

    @GET("/api1/purchases/lookup_product_npvr")
    Call<LookUpProduct> lookupProductNPVR(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("multilang") boolean multilang
    );


    @POST("/api1/purchases/hide")
    Call<Void> purchaseHide(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("id") String id
    );

    @POST("/api1/purchases/reserve_cancel")
    Call<Void> purchaseCancel(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("id") String id
    );

    @GET("/api1/me/purchases/list")
    Call<PurchaseListRes> getPurchaseList(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("product_category") ArrayList<String> product_category,
            @Query("product_type") String product_type,
            @Query("include_hidden") boolean include_hidden,
            @Query("include") ArrayList<String> include
    );
    @GET("/api1/contents/fullpackages")
    Call<WalletRes> getFullpackages(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("format") String format,
            @Query("include") ArrayList<String> include
    );
    @GET("/api1/contents/packages")
    Call<AdditionalRes> getPackages(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("format") String format,
            @Query("include") ArrayList<String> include
    );

}

