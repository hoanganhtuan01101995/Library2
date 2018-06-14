package com.alticast.viettelottcommons.serviceMethod;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetChannelIdsMethod {

    @GET("/listChannelViettel.txt")
    Call<List<String>> getChannelIds();
}
