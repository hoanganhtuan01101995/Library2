package com.alticast.viettelottcommons.serviceMethod.acms.packageInfo;

import com.alticast.viettelottcommons.resource.response.PackageDetailInfoRes;
import com.alticast.viettelottcommons.resource.response.PackageInfoRes;
import com.alticast.viettelottcommons.resource.response.PackageListRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by mc.kim on 8/22/2016.
 */
public interface PackageMethod {

    @GET
    Call<PackageInfoRes> getPackage(
            @Url String url,
            @Query("access_token") String accessToken,
            @Query("include") ArrayList<String> include,
            @Query("format") String format);


    @GET
    Call<PackageDetailInfoRes> getPackageDetail(
            @Url String url,
            @Query("access_token") String accessToken,
            @Query("include") ArrayList<String> include,
            @Query("format") String format
    );


    @GET
    Call<PackageListRes> getPackagePrograms(
            @Url String url,
            @Query("access_token") String accessToken,
            @Query("since") String since,
            @Query("until") String until,
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("include") ArrayList<String> include,
            @Query("popular") boolean popular,
            @Query("format") String format,
            @Query("series") boolean series
    );


}
