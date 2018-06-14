package com.alticast.viettelottcommons.serviceMethod;

import com.alticast.viettelottcommons.resource.response.ResumeListRes;
import com.alticast.viettelottcommons.resource.response.STBInfo;
import com.alticast.viettelottcommons.resource.response.UserDataListRes;
import com.alticast.viettelottcommons.resource.response.UserDataRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mc.kim on 8/11/2016.
 */
public interface SendLogMethod {

    @FormUrlEncoded
    @POST("/")
    Call<Void> sendLog(
            @Field("vietteltv_logger") String vietteltv_logger,
            @Field("phone_number") String phone_number,
            @Field("log_type") String log_type,
            @Field("log_content") String log_content,
            @Field("device_type") String device_type,
            @Field("version_name") String version_name,
            @Field("version_code") String version_code,
            @Field("device_code") String device_code,
            @Field("brand_name") String brand_name,
            @Field("key_access") String key_access,
            @Field("environment") String environment);
}
