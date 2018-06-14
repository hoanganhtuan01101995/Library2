package com.alticast.viettelottcommons.serviceMethod.acms.playback;


import android.content.Context;

import com.alticast.viettelottcommons.api.converter.Converter;
import com.alticast.viettelottcommons.resource.ChargingMethod;
import com.alticast.viettelottcommons.resource.TopupHistory;
import com.alticast.viettelottcommons.resource.Vod;
import com.alticast.viettelottcommons.resource.Wallet;
import com.alticast.viettelottcommons.resource.ads.Ad;
import com.alticast.viettelottcommons.resource.request.ChromeCastPrepareBody;
import com.alticast.viettelottcommons.resource.request.LivePrepareBody;
import com.alticast.viettelottcommons.resource.response.ChromeCastRes;
import com.alticast.viettelottcommons.resource.response.PrepareChannelRes;
import com.alticast.viettelottcommons.resource.response.PrepareVodRes;
import com.alticast.viettelottcommons.resource.response.ScheduleListRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface StreamInfoMethod {

    @GET("/api1/watches/vod/prepare")
    Call<PrepareVodRes> prepareVod(
            @Query("access_token") String access_token,
            @Query("bw_profile") String bwProfile,
            @Query("service_provider") String service_provider,
            @Query("id") String id,
            @Query("product_id") String product_id,
            @Query("version") String version,
            @Query("category_id") String category_id,
//                                   @Query("payment_type") String payment_type,
            @Header("Content-Type") String contentType,
            @Header("Accept-Language") String acceptLanguage,
            @Header("Accept") String accept
    );

    @GET("/api1/watches/catchup/prepare")
    Call<PrepareVodRes> prepareCatchUp(
            @Query("access_token") String access_token,
            @Query("bw_profile") String bwProfile,
            @Query("service_provider") String service_provider,
            @Query("id") String id,
            @Query("channel_id") String channel_id,
//                                       @Query("category_id") String category_id,
            @Query("version") String version,
            @Query("payment_type") String payment_type,
//            @Header("Authorization") String token,
            @Header("Content-Type") String contentType,
            @Header("Accept-Language") String acceptLanguage,
            @Header("Accept") String accept
    );

    @POST("/api1/watches/handheld/live/prepare")
    Call<PrepareChannelRes> prepareChannel(
            @Header("Authorization") String token,
            @Header("Content-Type") String contentType,
            @Header("Accept-Language") String acceptLanguage,
            @Header("Accept") String accept,
            @Body LivePrepareBody body
    );

    @POST("/demandRequestId.json")
    Call<ChromeCastRes> prepareVodChromeCast(
            @Body ChromeCastPrepareBody body
    );


    @POST("/api1/watches/handheld/live/prepare")
    Call<Void> prepareChromeCast(
            @Header("Authorization") String token,
            @Header("Content-Type") String contentType,
            @Header("Accept-Language") String acceptLanguage,
            @Header("Accept") String accept,
            @Body ChromeCastPrepareBody body
    );

    //    @POST("/api2/watches/handheld/live/prepare")
    @POST("/api1/watches/handheld/live/preparetoken")
    Call<PrepareChannelRes> prepareChannelNew(
            @Query("access_token") String access_token,
            @Query("hash") String hash,
            @Query("ts") String ts,
            @Query("client_id") String client_id,
            @Header("Authorization") String token,
            @Header("Content-Type") String contentType,
            @Header("Accept-Language") String acceptLanguage,
            @Header("Accept") String accept,
            @Body LivePrepareBody body
    );

    @POST("/api1/watches/handheld/live/preparechang")
    Call<PrepareChannelRes> prepareChannelChange(
            @Header("Authorization") String token,
            @Header("Content-Type") String contentType,
            @Header("Accept-Language") String acceptLanguage,
            @Header("Accept") String accept,
            @Body LivePrepareBody body
    );

    @GET
    Call<Vod> findNextEpisode(@Url String url,
                              @Query("access_token") String access_token,
                              @Query("format") String format,
                              @Query("include") String include,
                              @Header("Content-Type") String contentType,
                              @Header("Accept-Language") String acceptLanguage,
                              @Header("Accept") String accept
//                              @Header("X-App-Presence") String logs
    );

    @POST("/api1/watches/vod/pause")
    Call<Void> pauseVod(@Query("id") String id,
                        @Query("name") String name,
                        @Query("start_time") String start_time,
                        @Query("end_time") String end_time,
                        @Query("time_offset") String time_offset,
                        @Query("product_id") String product_id,
                        @Query("running_time") String running_time,
                        @Query("entry_path") String entry_path,
                        @Query("menu_id") String menu_id,
                        @Query("product_pid") String product_pid,
                        @Query("purchase_id") String purchase_id,
                        @Header("Authorization") String token,
                        @Header("Content-Type") String contentType,
                        @Header("Accept-Language") String acceptLanguage,
                        @Header("Accept") String accept
    );

    @POST("/api1/watches/vod/pause")
    Call<Void> pauseVodSeries(
            @Query("access_token") String access_token,
            @Query("id") String id,
            @Query("name") String name,
            @Query("start_time") String start_time,
            @Query("end_time") String end_time,
            @Query("time_offset") String time_offset,
            @Query("product_id") String product_id,
            @Query("running_time") String running_time,
            @Query("entry_path") String entry_path,
            @Query("product_pid") String product_pid,
            @Query("purchase_id") String purchase_id,
            @Query("menu_id") String menu_id,
            @Query("series") String series,
            @Query("season") String season,
            @Query("episode") String episode,
            @Header("Authorization") String token,
            @Header("Content-Type") String contentType,
            @Header("Accept-Language") String acceptLanguage,
            @Header("Accept") String accept
    );

    @POST("/api1/watches/vod/finish")
    Call<Void> finishVod(@Query("id") String id,
                         @Query("name") String name,
                         @Query("start_time") String start_time,
                         @Query("end_time") String end_time,
                         @Query("product_id") String product_id,
                         @Query("running_time") String running_time,
                         @Query("entry_path") String entry_path,
                         @Query("menu_id") String menu_id,
                         @Query("product_pid") String product_pid,
                         @Query("purchase_id") String purchase_id,
                         @Header("Authorization") String token,
                         @Header("Content-Type") String contentType,
                         @Header("Accept-Language") String acceptLanguage,
                         @Header("Accept") String accept
    );

    @POST("/api1/watches/vod/finish")
    Call<Void> finishVodSeries(
            @Query("access_token") String access_token,
            @Query("id") String id,
            @Query("name") String name,
            @Query("start_time") String start_time,
            @Query("end_time") String end_time,
            @Query("product_id") String product_id,
            @Query("running_time") String running_time,
            @Query("entry_path") String entry_path,
            @Query("series") String series,
            @Query("season") String season,
            @Query("episode ") String episode,
            @Query("menu_id") String menu_id,
            @Query("product_pid") String product_pid,
            @Query("purchase_id") String purchase_id,
            @Header("Authorization") String token,
            @Header("Content-Type") String contentType,
            @Header("Accept-Language") String acceptLanguage,
            @Header("Accept") String accept
    );

    @POST("/api1/watches/vod/destroy_resume")
    Call<Void> destroyVod(@Query("id") String id,
                          @Header("Authorization") String token,
                          @Header("Content-Type") String contentType,
                          @Header("Accept-Language") String acceptLanguage,
                          @Header("Accept") String accept
    );

    @POST("/api1/watches/catchup/pause")
    Call<Void> pauseCatchUp(@Query("id") String id,
                            @Query("name") String name,
                            @Query("start_time") String start_time,
                            @Query("end_time") String end_time,
                            @Query("time_offset") String time_offset,
                            @Query("running_time") String running_time,
                            @Query("entry_path") String entry_path,
                            @Query("purchase_id") String purchase_id,
                            @Header("Authorization") String token,
                            @Header("Content-Type") String contentType,
                            @Header("Accept-Language") String acceptLanguage,
                            @Header("Accept") String accept
    );

    @POST("/api1/watches/catchup/finish")
    Call<Void> finishCatchUp(@Query("id") String id,
                             @Query("name") String name,
                             @Query("start_time") String start_time,
                             @Query("end_time") String end_time,
                             @Query("running_time") String running_time,
                             @Query("entry_path") String entry_path,
                             @Query("purchase_id") String purchase_id,
                             @Header("Authorization") String token,
                             @Header("Content-Type") String contentType,
                             @Header("Accept-Language") String acceptLanguage,
                             @Header("Accept") String accept
    );

    @POST
    Call<Void> likeVod(@Url String url,
                       @Query("access_token") String access_token,
                       @Header("Content-Type") String contentType,
                       @Header("Accept-Language") String acceptLanguage,
                       @Header("Accept") String accept
    );

    @DELETE
    Call<Void> unlikeVod(@Url String url,
                         @Query("access_token") String access_token,
                         @Query("_method") String delete,
                         @Header("Content-Type") String contentType,
                         @Header("Accept-Language") String acceptLanguage,
                         @Header("Accept") String accept
    );

    @GET
    Call<Ad> getPlacementDecisionRequest(
            @Url String url,
            @Query("messageId") String messageId,
            @Query("requestId") String requestId,
            @Query("resume") String resume,
            @Query("inventoryType") String inventoryType,
            @Query("regionId") String regionId,
            @Query("advPlatformType") String advPlatformType,
            @Query("userId") String userId,
            @Query("opportunityType") String opportunityType,
            @Header("Content-Type") String contentType,
            @Header("Accept-Language") String acceptLanguage,
            @Header("Accept") String accept
    );

    @GET
    Call<Void> sendAdsLog(
            @Url String url
    );
}
