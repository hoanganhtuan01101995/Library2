package com.alticast.viettelottcommons.serviceMethod.acms.program;


import com.alticast.viettelottcommons.resource.ChannelProduct;
import com.alticast.viettelottcommons.resource.PhotoRes;
import com.alticast.viettelottcommons.resource.ProductCatchup;
import com.alticast.viettelottcommons.resource.Series;
import com.alticast.viettelottcommons.resource.TrailerRes;
import com.alticast.viettelottcommons.resource.Vod;
import com.alticast.viettelottcommons.resource.response.ChannelRes;
import com.alticast.viettelottcommons.resource.response.ProgramList;
import com.alticast.viettelottcommons.resource.response.ProgramProductRes;
import com.alticast.viettelottcommons.resource.response.ScheduleListRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by mc.kim on 7/28/2016.
 */
public interface ProductMethod {
    @GET("/api1/contents/programs/series")
    Call<ProgramList> getSeries(@Query("access_token") String access_token,
                                @Query("id") String id,
                                @Query("include") ArrayList<String> include,
                                @Query("offset") int offset,
//                                @Query("limit") int limit,
                                @Query("format") String format);
    @GET("/api1/contents/programs/series")
    Call<ProgramList> getSeries(@Query("access_token") String access_token,
                                @Query("id") String id,
                                @Query("include") ArrayList<String> include,
                                @Query("offset") int offset,
                                @Query("limit") int limit,
                                @Query("format") String format);

    @GET("/api1/me/watches/series/episode")
    Call<Series> getSeriesLastWatchEpisode(@Query("access_token") String access_token,
                                           @Query("series") String seriesId,
                                           @Query("season") String seasonId);


    @GET("/api1/contents/programs")
    Call<ProgramList> getPrograms(@Query("access_token") String access_token,
                                  @Query("id") ArrayList<String> id,
                                  @Query("include") ArrayList<String> include,
                                  @Query("format") String format
    );
    @GET("/api1/contents/programs/products")
    Call<ProgramProductRes> getProductProgram(@Query("access_token") String access_token,
                                              @Query("id") String id,
                                              @Query("include") ArrayList<String> include,
                                              @Query("format") String format
    );


    @GET
    Call<TrailerRes> getTrailer(@Url String url,
                                @Query("access_token") String access_token,
                                @Query("offset") int offset,
//                                 @Query("since") int since,
//                                 @Query("until") int until,
                                @Query("limit") int limit
    );


    @GET
    Call<PhotoRes> getPhoto(@Url String url,
                            @Query("access_token") String access_token,
                            @Query("offset") int offset,
                            @Query("limit") int limits
    );

    @GET
    Call<ProgramList> getCategoryPrograms(@Url String url,
                                          @Query("access_token") String access_token,
                                          @Query("until") String until,
                                          @Query("limit") int limit,
                                          @Query("offset") int offset,
                                          @Query("include") ArrayList<String> include,
                                          @Query("popular") boolean popular,
                                          @Query("format") String format);

    @GET
    Call<ProgramList> getCategoryPrograms(@Url String url,
                                          @Query("access_token") String access_token,
                                          @Query("limit") int limit,
                                          @Query("offset") int offset,
                                          @Query("include") ArrayList<String> include,
                                          @Query("popular") boolean popular,
                                          @Query("format") String format);

    @GET
    Call<ProgramList> getCategoryPrograms(@Url String url,
                                          @Query("access_token") String access_token,
                                          @Query("limit") int limit,
                                          @Query("offset") int offset,
                                          @Query("until") String until,
                                          @Query("include") ArrayList<String> include,
                                          @Query("format") String format);


    @GET
    Call<Vod> getProgram(@Url String url,
                         @Query("access_token") String access_token,
                         @Query("include") ArrayList<String> include,
                         @Query("format") String format);


    @GET("/api1/contents/channels")
    Call<ChannelRes> getChannels(@Query("access_token") String access_token,
                                 @Query("offset") int offset,
                                 @Query("region") String region,
                                 @Query("limit") int limit,
                                 @Query("include") ArrayList<String> include,
                                 @Query("format") String format);


    @GET
    Call<ChannelProduct> getChannelProduct(
            @Url String url,
            @Query("access_token") String access_token,
            @Query("region") String region,
            @Query("limit") String limit,
            @Query("include") ArrayList<String> include,
            @Query("format") String format);

    @GET
    Call<ProductCatchup> getChannelProductPair(
            @Url String url,
            @Query("access_token") String access_token,
            @Query("multilang") Boolean multi);


    @GET("/api1/contents/channels/schedules")
    Call<ScheduleListRes> getCurrentScheduleList(@Query("access_token") String access_token,
                                                 @Query("region") String region,
                                                 @Query("since") String since,
                                                 @Query("until") String until,
                                                 @Query("limit") int limit
    );

    @GET("/api1/contents/channels/schedules")
    Call<ScheduleListRes> getCurrentScheduleList(@Query("access_token") String access_token,
                                                 @Query("id") ArrayList<String> ids,
                                                 @Query("region") String region,
                                                 @Query("since") String since,
                                                 @Query("until") String until
    );

    @GET("/api1/contents/channels/schedules")
    Call<ScheduleListRes> getScheduleList(@Query("access_token") String access_token,
                                          @Query("id") String id,
                                          @Query("region") String region,
                                          @Query("since") long since,
                                          @Query("until") long until,
                                          @Query("limit") int limit);


    @POST
    Call<Void> requestReview(
            @Url String url,
            @Query("access_token") String accessToken,
            @Query("message") String message,
            @Query("rating") int rating);
}
