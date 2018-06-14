package com.alticast.viettelottcommons.serviceMethod.acms.category;

import com.alticast.viettelottcommons.resource.response.CategoryInfo;
import com.alticast.viettelottcommons.resource.response.CategoryListRes;
import com.alticast.viettelottcommons.resource.response.ChannelRes;
import com.alticast.viettelottcommons.resource.response.MenuListRes;
import com.alticast.viettelottcommons.resource.response.ProgramList;
import com.alticast.viettelottcommons.resource.response.TrendingRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by mc.kim on 8/8/2016.
 */
public interface MenuMethod {
    final String ACCESS_TOKEN = "access_token";

    @GET("/api1/contents/menus")
    Call<MenuListRes> getMenus(@Query(ACCESS_TOKEN) String accessToken,
                               @Query("child") String child,
                               @Query("version") String version);

    @GET
    Call<CategoryInfo> getCategoryDetailInfo(@Url String categoryId,
                                             @Query(ACCESS_TOKEN) String accessToken
    );

    @GET
    Call<CategoryListRes> getCategories(@Url String url,
                                        @Query(ACCESS_TOKEN) String accessToken,
                                        @Query("offset") int offset,
                                        @Query("limit") int limit
    );

    @GET
    Call<TrendingRes> getTrendingNow(@Url String url
    );


    @GET
    Call<ChannelRes> getMenuChannels(
            @Url String url,
            @Query(ACCESS_TOKEN) String accessToken,
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("include") ArrayList<String> include,
            @Query("format") String format
    );

    //http://kbase.alticast.com/confluence/display/SDP/GET+menus.MENU_ID.programs
    @GET
    Call<ProgramList> getMenuPrograms(
            @Url String url,
            @Query(ACCESS_TOKEN) String accessToken,
            @Query("popular") boolean popular,
            @Query("series") boolean series,
            @Query("price") String price,
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("include") ArrayList<String> include,
            @Query("format") String format);

}
