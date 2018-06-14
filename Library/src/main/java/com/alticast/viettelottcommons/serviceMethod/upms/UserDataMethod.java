package com.alticast.viettelottcommons.serviceMethod.upms;

import com.alticast.viettelottcommons.resource.response.ResumeListRes;
import com.alticast.viettelottcommons.resource.response.STBInfo;
import com.alticast.viettelottcommons.resource.response.UserDataListRes;
import com.alticast.viettelottcommons.resource.response.UserDataRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mc.kim on 8/11/2016.
 */
public interface UserDataMethod {
    final String ACCESS_TOKEN = "access_token";

    @POST("/api1/me/mycontents/create")
    Call<UserDataRes> createMyContent(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("id") String id,
            @Query("name") String name,
            @Query("type") String type,
            @Query("filter_value") String filter_value,
            @Query("auto_overwrite") boolean auto_overwrite,
            @Query("group") String group);


    @GET("/api1/me/mycontents/list")
    Call<UserDataListRes> getMyContents(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("type") String type,
            @Query("group") String group,
            @Query("sort") String sort
//            ,
//            @Query("filter_value") String filter_value,
//            @Query("operator") String operator
    );


    @POST("/api1/me/mycontents/destroy")
    Call<Void> deleteMyContent(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("id") ArrayList<String> id);


    @POST("/api1/me/mychannels/create")
    Call<UserDataListRes> createMyChannel(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("id") String id,
            @Query("name") String name,
            @Query("type") String type,
            @Query("filter_value") String filter_value,
            @Query("auto_overwrite") boolean auto_overwrite);


    @GET("/api1/me/mychannels/list")
    Call<UserDataListRes> getMyChannels(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("type") String type
//            ,
//            @Query("filter_value") String filter_value,
//            @Query("operator") String operator
    );


    @POST("/api1/me/mychannels/destroy")
    Call<Void> deleteMyChannel(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("id") ArrayList<String> id);


    @GET("api1/watches/resume_list")
    Call<ResumeListRes> getResumeList(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("type") String type

    );

    @GET("api1/watches/ott/watched/list")
    Call<ResumeListRes> getRecentlyWatched(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    @GET("api1/watches/watched/list")
    Call<ResumeListRes> getRecentlyWatchedPair(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    @POST("api1/watches/destroy_resume")
    Call<Void> deleteResumeList(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("id") String id
    );

    @POST("api1/watches/ott/watched/remove")
    Call<Void> deleteRecentlyWatchedList(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("ids") String id
    );

    @POST("api1/watches/watched/remove")
    Call<Void> deleteRecentlyWatchedPairList(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("ids") String id
    );

    @GET("api1/contents/user")
    Call<STBInfo> getSTBInfo(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("with") String stbIp);


}
