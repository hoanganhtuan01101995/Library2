package com.alticast.viettelottcommons.serviceMethod.acms.recommendation;


import com.alticast.viettelottcommons.resource.RecommendedVod;
import com.alticast.viettelottcommons.resource.response.TrendingRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by mc.kim on 7/28/2016.
 */
public interface RecommendMethod {

    @GET("/so-web-app/so/recommend")
    Call<RecommendedVod> reqeustRecommend(@Query("dp") String dp,
                                          @Query("cust") String cust,
                                          @Query("pc") String pc,
                                          @Query("max_items") int max_items,
                                          @Query("program_id") String program_id,
                                          @Query("channelgenre") String channelgenre,
                                          @Query("genre") String genre,
                                          @Query("director") String director,
                                          @Query("actor") String actor,
                                          @Query("rating") String rating,
                                          @Query("adultyn") String adultyn,
                                          @Query("frmt") String frmt
    );


    @GET("/so-web-app/so/recommend")
    Call<RecommendedVod> reqeustRecommendMenu(@Query("dp") String dp,
                                              @Query("cust") String cust,
                                              @Query("pc") String pc,
                                              @Query("hot_max") int hot_max,
                                              @Query("new_max") int new_max,
                                              @Query("max_items") int max_items,
                                              @Query("frmt") String frmt
    );


    @GET("/so-web-app/so/recommend")
    Call<RecommendedVod> getRecommendedData(@Query("dp") String dp,
                                            @Query("cust") String cust,
                                            @Query("pc") String pc,
                                            @Query("max_items") int max_items,
                                            @Query("frmt") String frmt
    );

    @GET
    Call<TrendingRes> getRecommendedDataNew(
                                            @Url String url,
                                            @Query("dp") String dp,
                                            @Query("cust") String cust,
                                            @Query("pc") String pc,
                                            @Query("max_items") int max_items,
                                            @Query("frmt") String frmt
    );

    @GET ("/so-web-app/so/recommend")
    Call<TrendingRes> getRecommendedDataNowNew(
//                                            @Url String url,
                                            @Query("dp") String dp,
                                            @Query("cust") String cust,
                                            @Query("pc") String pc,
                                            @Query("max_items") int max_items,
                                            @Query("frmt") String frmt,
                                            @Query("category_id") String category_id
    );

    @GET
    Call<TrendingRes> getMenuId(
            @Url String url

    );

    @GET("/so-web-app/so/recommend")
    Call<TrendingRes> getRecommendedDataNew(@Query("dp") String dp,
                                            @Query("cust") String cust,
                                            @Query("pc") String pc,
                                            @Query("max_items") int max_items,
                                            @Query("frmt") String frmt
    );

    @GET("/so-web-app/so/recommend")
    Call<RecommendedVod> getRecommendedVod(@Query("dp") String dp,
                                           @Query("cust") String cust,
                                           @Query("pc") String pc,
                                           @Query("program_id") String program_id,
                                           @Query("max_items") int max_items,
                                           @Query("frmt") String frmt
    );

    @GET("/so-web-app/so/recommend")
    Call<RecommendedVod> getRecommendedVodTrending(@Query("dp") String dp,
                                                   @Query("cust") String cust,
                                                   @Query("pc") String pc,
                                                   @Query("max_items") int max_items,
                                                   @Query("frmt") String frmt
    );

    @POST
    Call<Void> sendLog(@Url String url
    );

}
