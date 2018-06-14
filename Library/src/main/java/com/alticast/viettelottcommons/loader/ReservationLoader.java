package com.alticast.viettelottcommons.loader;

import android.content.Context;
import android.util.Log;

import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.Reservation;
import com.alticast.viettelottcommons.resource.response.ReservationRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.acms.reservation.ReservationMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationLoader {

    private static ReservationLoader ourInstance = new ReservationLoader();

    public static ReservationLoader getInstance() {
        return ourInstance;
    }

    private ReservationLoader() {
    }

    public void create(String data, final WindmillCallback callback) {
        ReservationMethod reservationMethod = ServiceGenerator.getInstance().createSerive(ReservationMethod.class);


        String authorization = "Bearer " + AuthManager.getAccessToken();
        String contentType = "application/json;charset=UTF-8";
        String acceptLanguage = Locale.getDefault().getLanguage();
        String accept = "*/*";
        String domain = "user.reserved.programs";


        Call<Void> call = reservationMethod.create(domain, data, authorization, contentType, acceptLanguage, accept);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    callback.onSuccess(response.body());

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void show(final WindmillCallback callback) {
        ReservationMethod reservationMethod = ServiceGenerator.getInstance().createSerive(ReservationMethod.class);
        String contentType = "application/json;charset=UTF-8";
        String acceptLanguage = Locale.getDefault().getLanguage();
        String accept = "*/*";
        String domain = "user.reserved.programs";
        String access_token = AuthManager.getAccessToken();

        Call<ReservationRes> call = reservationMethod.show(domain, access_token, contentType, acceptLanguage, accept);
        call.enqueue(new Callback<ReservationRes>() {
            @Override
            public void onResponse(Call<ReservationRes> call, Response<ReservationRes> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ReservationRes> call, Throwable t) {

            }
        });
    }

    private void getReservationList(String offset, String limit, final WindmillCallback callback) {
        ReservationMethod reservationMethod = ServiceGenerator.getInstance().createSerive(ReservationMethod.class);
        Call<ReservationRes> call = reservationMethod.getListReservation(offset, limit);
        call.enqueue(new Callback<ReservationRes>() {
            @Override
            public void onResponse(Call<ReservationRes> call, Response<ReservationRes> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(Call<ReservationRes> call, Throwable t) {

            }
        });
    }

}
