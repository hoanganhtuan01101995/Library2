package com.alticast.viettelottcommons.serviceMethod.acms.reservation;


import com.alticast.viettelottcommons.resource.Reservation;
import com.alticast.viettelottcommons.resource.response.ProgramList;
import com.alticast.viettelottcommons.resource.response.ReservationRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ReservationMethod {

    @POST("/api1/me/services/domains/create")
    Call<Void> create(
            @Query("domain") String domain,
            @Query("data") String data,
            @Header("Authorization") String token,
            @Header("Content-Type") String contentType,
            @Header("Accept-Language") String acceptLanguage,
            @Header("Accept") String accept
    );

    @GET("/api1/me/services/domains/show")
    Call<ReservationRes> show(
            @Query("domain") String domain,
            @Query("access_token") String access_token,
            @Header("Content-Type") String contentType,
            @Header("Accept-Language") String acceptLanguage,
            @Header("Accept") String accept
    );

    @GET("/api1/me/reservations/tvshow/list")
    Call<ReservationRes> getListReservation(
            @Query("offset") String offset,
            @Query("limit") String limit
    );
}
