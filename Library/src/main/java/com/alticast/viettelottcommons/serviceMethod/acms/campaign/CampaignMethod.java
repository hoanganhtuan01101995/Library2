package com.alticast.viettelottcommons.serviceMethod.acms.campaign;

import com.alticast.viettelottcommons.resource.RegisterTagDevice;
import com.alticast.viettelottcommons.resource.RequestTagDevice;
import com.alticast.viettelottcommons.resource.ResultRes;
import com.alticast.viettelottcommons.resource.request.CheckVmSubscriberReq;
import com.alticast.viettelottcommons.resource.response.AdDecisionRes;
import com.alticast.viettelottcommons.resource.response.CampaignGroupRes;
import com.alticast.viettelottcommons.resource.response.CampaignsRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by mc.kim on 8/10/2016.
 */
public interface CampaignMethod {

    final String ACCESS_TOKEN = "access_token";

    @GET("/api1/contents/campaigns/campaigns")
    Call<CampaignGroupRes> getCampaigns(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("pid") ArrayList<String> pid);


    @GET
    Call<CampaignsRes> getCampaign(@Url String url,
                                   @Query(ACCESS_TOKEN) String access_token);

    @GET
    Call<AdDecisionRes> getPromotionBanner(
            @Url String url,
            @Query("messageId") String messageId,
            @Query("userId") String userId,
            @Query("advPlatformType") String advPlatformType,
            @Query("sceneId") String sceneId,
            @Query("regionId") String regionId);


    @POST
    Call<ResultRes> registerPushWoodTag(@Url String url,
                                        @Body RequestTagDevice requestTagDevice);
}
