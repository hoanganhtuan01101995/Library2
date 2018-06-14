package com.alticast.viettelottcommons.serviceMethod.acms.platform;

import com.alticast.viettelottcommons.resource.response.ConfigListRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mc.kim on 10/5/2016.
 */
public interface ConfigMethod {
    final String ACCESS_TOKEN = "access_token";

    @GET("/api1/contents/configs")
    Call<ConfigListRes> getConfig(@Query(ACCESS_TOKEN) String accessToken, @Query("pid") String pid);
}
