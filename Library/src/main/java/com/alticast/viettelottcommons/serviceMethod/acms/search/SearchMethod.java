package com.alticast.viettelottcommons.serviceMethod.acms.search;

import com.alticast.viettelottcommons.resource.response.SearchCompletionListRes;
import com.alticast.viettelottcommons.resource.response.SearchKeywordHistoryListRes;
import com.alticast.viettelottcommons.resource.response.SearchKeywordListRes;
import com.alticast.viettelottcommons.resource.response.SearchResultRes;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mc.kim on 8/12/2016.
 */
public interface SearchMethod {
    final String ACCESS_TOKEN = "access_token";

    @GET("/api1/search/search")
    Call<SearchResultRes> getSearchResult(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("d_license") String d_license,
            @Query("q") String q,
            @Query("cat") String cat,
            @Query("m_field") String m_field,
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("filter") String filter,
            @Query("vsuppress") String vsuppress,
            @Query("region") String region,
            @Query("input_route") String input_route);

    @GET("/api1/search/search")
    Call<SearchResultRes> getSearchResult(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("d_license") String d_license,
            @Query("q") String q,
            @Query("cat") String cat,
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("filter") String filter,
            @Query("vsuppress") String vsuppress,
            @Query("region") String region,
            @Query("input_route") String input_route);

    @GET("/api1/search/complete")
    Call<SearchCompletionListRes> getSearchComplete(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("d_license") String d_license,
            @Query("q") String q, @Query("limit") int limit);


    @GET("/api1/search/keyword/popular")
    Call<SearchKeywordListRes> getSearchKeywordPop(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("d_license") String d_license,
            @Query("limit") int limit);

    @GET("/api1/search/keyword/recommend")
    Call<SearchKeywordListRes> getSearchKeywordRcmd(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("d_license") String d_license,
            @Query("limit") int limit);

    @GET("/api1/search/keyword/history")
    Call<SearchKeywordHistoryListRes> getSearchKeywordHistory(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("d_license") String d_license,
            @Query("limit") int limit);

    @POST("/api1/search/keyword/history")
    Call<Void> deleteSearchKeywordHistory(
            @Query(ACCESS_TOKEN) String access_token,
            @Query("_method") String method);
}