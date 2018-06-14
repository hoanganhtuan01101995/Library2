package com.alticast.viettelottcommons.loader;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.InquireApi;
import com.alticast.viettelottcommons.api.MobilePhoneApi;
import com.alticast.viettelottcommons.api.ScratchCardApi;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.ChargedResult;
import com.alticast.viettelottcommons.resource.ChargingMethod;
import com.alticast.viettelottcommons.resource.OTPresult;
import com.alticast.viettelottcommons.resource.TopupHistory;
import com.alticast.viettelottcommons.resource.Wallet;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.acms.prePayment.InquireMethod;
import com.alticast.viettelottcommons.serviceMethod.acms.prePayment.MobilePhoneMethod;
import com.alticast.viettelottcommons.serviceMethod.acms.prePayment.ScratchCardMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;
import com.alticast.viettelottcommons.util.Util;

import retrofit2.Call;
import retrofit2.Callback;


public class PrePaymentLoader implements InquireApi, MobilePhoneApi, ScratchCardApi, Callback {

    private static PrePaymentLoader instance = null;

    public synchronized static PrePaymentLoader getInstance() {
        if (instance == null) {
            instance = new PrePaymentLoader();
        }
        return instance;
    }

    private WindmillCallback callback = null;

    @Override
    public void getMyWalletBalance(String access_token, final WindmillCallback callback) {

        InquireMethod inquireMethod = ServiceGenerator.getInstance().createSerive(InquireMethod.class);
        Call<Wallet> call = inquireMethod.getMyWalletBalance(access_token);
        call.enqueue(new Callback<Wallet>() {
            @Override
            public void onResponse(Call<Wallet> call, retrofit2.Response<Wallet> response) {
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
            public void onFailure(Call<Wallet> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    @Override
    public void getInquireTopupMethod(String access_token,final WindmillCallback callback) {
//        this.callback = callback;
        InquireMethod inquireMethod = ServiceGenerator.getInstance().createSerive(InquireMethod.class);
        Call<ChargingMethod> call = inquireMethod.getInquireTopupMethod(access_token);
//        call.enqueue(this);
        call.enqueue(new Callback<ChargingMethod>() {
            @Override
            public void onResponse(Call<ChargingMethod> call, retrofit2.Response<ChargingMethod> response) {
                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {
                    ChargingMethod chargingMethod = response.body();
                    chargingMethod.processData();

                    callback.onSuccess(chargingMethod);

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ChargingMethod> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    @Override
    public void getTopupHistory(String access_token, long since, long unti, int offset, int limit,
                               final WindmillCallback callback) {
        InquireMethod inquireMethod = ServiceGenerator.getInstance().createSerive(InquireMethod.class);
        Call<TopupHistory> call = inquireMethod.getTopupHistory(access_token, since, unti, offset, limit);

        call.enqueue(new Callback<TopupHistory>() {
            @Override
            public void onResponse(Call<TopupHistory> call, retrofit2.Response<TopupHistory> response) {
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
            public void onFailure(Call<TopupHistory> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }


    @Override
    public void requestOtp(String access_token, String phoneNumber,final WindmillCallback callback) {
        this.callback = callback;
        MobilePhoneMethod mobilePhoneMethod = ServiceGenerator.getInstance().createSerive(MobilePhoneMethod.class);
        Call<Void> call = mobilePhoneMethod.requestOTP(access_token, phoneNumber);
//        call.enqueue(this);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
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

    @Override
    public void requsetTopUpByScratchCard(String access_token, String serial, String pin, String promotionId,final WindmillCallback callback) {
        ScratchCardMethod scratchCardMethod = ServiceGenerator.getInstance().createSerive(ScratchCardMethod.class);
        Call<ChargedResult> call = scratchCardMethod.requestTopupByScratchCard(access_token, serial, pin, promotionId);

        call.enqueue(new Callback<ChargedResult>() {
            @Override
            public void onResponse(Call<ChargedResult> call, retrofit2.Response<ChargedResult> response) {
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
            public void onFailure(Call<ChargedResult> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    @Override
    public void requestTopupByMobilePhone(String access_token, String phoneNumber, String otp, int topup_amount, int bonus_rate,
                                          int bonus_amount, String promotionId,final WindmillCallback callback) {
        MobilePhoneMethod mobilePhoneMethod = ServiceGenerator.getInstance().createSerive(MobilePhoneMethod.class);

        String hash = Util.getScretKey(phoneNumber + otp + topup_amount, HandheldAuthorization.getInstance().getTokenSecret());

        Call<ChargedResult> call = mobilePhoneMethod.requestTopupByMobilePhone(access_token, phoneNumber, otp
                , topup_amount, bonus_rate, bonus_amount, promotionId, hash);

        call.enqueue(new Callback<ChargedResult>() {
            @Override
            public void onResponse(Call<ChargedResult> call, retrofit2.Response<ChargedResult> response) {
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
            public void onFailure(Call<ChargedResult> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    @Override
    public void onResponse(Call call, retrofit2.Response response) {
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
    public void onFailure(Call call, Throwable t) {
        if (callback == null) {
            return;
        }

        callback.onFailure(call, t);
    }



}
