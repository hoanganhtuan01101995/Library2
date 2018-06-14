package com.alticast.viettelottcommons.serviceMethod.acms.purchase;

import com.alticast.viettelottcommons.resource.request.AutomaticRenewalBody;
import com.alticast.viettelottcommons.resource.request.CheckCouponReq;
import com.alticast.viettelottcommons.resource.request.UpsellBody;
import com.alticast.viettelottcommons.resource.response.CheckCouponRes;
import com.alticast.viettelottcommons.resource.response.PaymentResultRes;
import com.alticast.viettelottcommons.resource.response.PurchaseResultRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mc.kim on 8/5/2016.
 */
public interface PurchaseMethod {
    final String ACCESS_TOKEN = "access_token";


    @POST("/api1/purchases/create")
    Call<PurchaseResultRes> purchaseCreate(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("product_id") String product_id,
            @Query("product_name") String product_name,
            @Query("content_id") String content_id,
            @Query("product_category") String product_category,
            @Query("product_type") String product_type,
            @Query("promotion_id") String promotion_id,
            @Query("entry_path") String entry_path,
            @Query("client_id") String client_id,
            @Query("ts") long ts,
            @Query("nonce") String nonce,
            @Query("rental_period") int rental_period,
            @Query("unit") String unit,
            @Query("auto_renewal") String auto_renewal,
            @Query("hash") String hash);

    @POST("/api1/purchases/create")
    Call<PurchaseResultRes> purchaseCreate(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("product_id") String product_id,
            @Query("product_name") String product_name,
            @Query("content_id") String content_id,
            @Query("product_category") String product_category,
            @Query("product_type") String product_type,
            @Query("entry_path") String entry_path,
            @Query("client_id") String client_id,
            @Query("ts") long ts,
            @Query("nonce") String nonce,
            @Query("rental_period") int rental_period,
            @Query("unit") String unit,
            @Query("auto_renewal") String auto_renewal,
            @Query("hash") String hash);


    @POST("/api1/purchases/create")
    Call<PurchaseResultRes> purchaseCreate(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("product_id") String product_id,
            @Query("product_name") String product_name,
            @Query("content_id") String content_id,
            @Query("product_category") String product_category,
            @Query("product_type") String product_type,
            @Query("entry_path") String entry_path,
            @Query("client_id") String client_id,
            @Query("ts") long ts,
            @Query("nonce") String nonce,
            @Query("hash") String hash);

    @POST("/api1/purchases/create")
    Call<PurchaseResultRes> purchaseCreate(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("product_id") String product_id,
            @Query("product_name") String product_name,
            @Query("content_id") String content_id,
            @Query("product_category") String product_category,
            @Query("product_type") String product_type,
            @Query("promotion_id") String promotion_id,
            @Query("entry_path") String entry_path,
            @Query("client_id") String client_id,
            @Query("ts") long ts,
            @Query("nonce") String nonce,
            @Query("hash") String hash);


    @POST("/api1/payments/create")
    Call<PaymentResultRes> paymentsCreateNomal(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("sale_code") String sale_code,
            @Query("coupon_tx_id") String coupon_tx_id,
            @Query("currency") String currency,
            @Query("product_id") String product_id,
            @Query("product_name") String product_name,
            @Query("purchase_id") String purchase_id,
            @Query("product_category") String product_category,
            @Query("product_type") String product_type,
            @Query("normal") float normal);


    @POST("/api1/payments/create")
    Call<PaymentResultRes> paymentsCreatePoint(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("sale_code") String sale_code,
            @Query("coupon_tx_id") String coupon_tx_id,
            @Query("currency") String currency,
            @Query("product_id") String product_id,
            @Query("product_name") String product_name,
            @Query("purchase_id") String purchase_id,
            @Query("product_category") String product_category,
            @Query("product_type") String product_type,
            @Query("point") float normal);

    @POST("/api1/payments/create")
    Call<PaymentResultRes> paymentsCreateWallet(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("sale_code") String sale_code,
            @Query("coupon_tx_id") String coupon_tx_id,
            @Query("currency") String currency,
            @Query("product_id") String product_id,
            @Query("product_name") String product_name,
            @Query("purchase_id") String purchase_id,
            @Query("product_category") String product_category,
            @Query("product_type") String product_type,
            @Query("wallet") float normal);


    @POST("/api1/payments/create")
    Call<PaymentResultRes> paymentsCreatePhone(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("sale_code") String sale_code,
            @Query("coupon_tx_id") String coupon_tx_id,
            @Query("currency") String currency,
            @Query("product_id") String product_id,
            @Query("product_name") String product_name,
            @Query("purchase_id") String purchase_id,
            @Query("product_category") String product_category,
            @Query("product_type") String product_type,
            @Query("phone") float phone
    );


    @POST("/api1/purchases/subscribe_basic")
    Call<Void> purchaseSubscribeBasic(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("id") String id);

    @POST("/ott/purchases/automatic_renewal")
    Call<Void> automaticRenewal(
            @Query(ACCESS_TOKEN) String access_token,
            @Body AutomaticRenewalBody body
    );
    @POST("/api1/purchases/upselling_full")
    Call<Void> upsellingFull(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("product_id") String product_id,
            @Query("rental_period") int rental_period,
            @Query("unit") String unit
    );

    @GET("/api1/payments/check_coupon")
    Call<CheckCouponRes> checkCoupon(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("product_id") String product_id,
            @Query("coupon_tx_id") String coupon_tx_id
    );
}
