package com.alticast.viettelottcommons.serviceMethod.upms;


import com.alticast.viettelottcommons.loader.FrontEndLoader;
import com.alticast.viettelottcommons.resource.AccessToken;
import com.alticast.viettelottcommons.resource.AuthCodeRes;
import com.alticast.viettelottcommons.resource.Login;
import com.alticast.viettelottcommons.resource.MyDeviceAccount;
import com.alticast.viettelottcommons.resource.PairingRes;
import com.alticast.viettelottcommons.resource.ResultRes;
import com.alticast.viettelottcommons.resource.request.AutomaticDetectionReq;
import com.alticast.viettelottcommons.resource.request.ChangePasswordReq;
import com.alticast.viettelottcommons.resource.request.CheckAuthenticationCodeReq;
import com.alticast.viettelottcommons.resource.request.CheckIdReq;
import com.alticast.viettelottcommons.resource.request.CheckVmSubscriberReq;
import com.alticast.viettelottcommons.resource.request.CreateAccountReq;
import com.alticast.viettelottcommons.resource.request.DelegationReq;
import com.alticast.viettelottcommons.resource.request.LoginReq;
import com.alticast.viettelottcommons.resource.request.RequestChangeMethod;
import com.alticast.viettelottcommons.resource.request.RequestPairingReq;
import com.alticast.viettelottcommons.resource.request.ResetPasswordReq;
import com.alticast.viettelottcommons.resource.request.VerifyCodeReq;
import com.alticast.viettelottcommons.resource.response.MeShowRes;
import com.alticast.viettelottcommons.resource.response.VerifyCodeRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mc.kim on 7/28/2016.
 */
public interface FrontEndMethod {
    final String ACCESS_TOKEN = "access_token";

    @GET("/ott/accounts/show")
    Call<MyDeviceAccount> getMyAccount(@Query(ACCESS_TOKEN) String access_token
            , @Query("include") String include
    );

    @GET("/api1/me/show")
    Call<MeShowRes> getMeShow(@Query(ACCESS_TOKEN) String access_token
            , @Query("include_stb") boolean include_stb
            , @Query("include_hh") boolean include_hh
    );

    @POST("/ott/accounts/inquire_vm_subscriber")
    Call<ResultRes> inquireVmSubscriber(@Body CheckVmSubscriberReq checkVmSubscriberReq);


    @POST("/ott/accounts/requestAuthenticationCode")
    Call<AuthCodeRes> requestAuthenticationCode(@Body CheckAuthenticationCodeReq checkAuthenticationCodeReq
    );

    @POST("/ott/accounts/checkId")
    Call<ResultRes> checkId(@Body CheckIdReq checkIdReq);

    @POST("/ott/accounts/login")
    Call<Login> login(
            @Body LoginReq loginReq);

    @POST("/ott/accounts/logout")
    Call<Void> logout(
            @Query(ACCESS_TOKEN) String access_token);

    @POST("/ott/device/requestPairing")
    Call<PairingRes> requestPairing(@Query(ACCESS_TOKEN) String access_token,
                                    @Body RequestPairingReq requestPairingReq);

    @POST("/ott/accounts/create")
    Call<Void> createAccount(@Body CreateAccountReq deviceAccount);


    @POST("/ott/accounts/resetPassword")
    Call<Void> resetPassword(@Body ResetPasswordReq resetPasswordReq);

    @POST("/ott/accounts/change_payment_option")
    Call<Void> changePaymentOption(@Query(ACCESS_TOKEN) String access_token,
                                   @Body RequestChangeMethod requestChangeMethod
                        );

//    @POST("/ott/accounts/requestAuthenticationCode")
//    Call<VerifyCodeRes> verifyAuthenticationCode(@Body VerifyCodeReq verifyCodeReq);
    @POST("/ott/accounts/verifyAuthenticationCode")
    Call<VerifyCodeRes> verifyAuthenticationCode(@Body VerifyCodeReq verifyCodeReq);


    @POST("/ott/accounts/changePassword")
    Call<Void> changePassword(
            @Query(ACCESS_TOKEN) String access_token,
            @Body ChangePasswordReq changePasswordReq);

    @POST("/api1/auth/access_token")
    Call<AccessToken> postAccessToken(
            @Query("login_id") String login_id,
            @Query("password") String password,
            @Query("client_id") String client_id,
            @Query("device_id") String device_id,
            @Query("ts") long ts,
            @Query("nonce") String nonce,
            @Query("hash") String hash);


    @POST("/ott/device/requestUnpairing")
    Call<Void> requestUnpairing(
            @Query("access_token") String access_token);


    @POST("/ott/accounts/delegation")
    Call<Login> requestDelegation(
            @Body DelegationReq delegationReq);


    @POST("/ott/accounts/automatic_detection_msisdn")
    Call<Login> requestAutoDetech(@Body AutomaticDetectionReq automaticDetectionReq);
//    @POST("/ott/accounts/automatic_detection")
//    Call<Login> requestAutoDetech(@Body AutomaticDetectionReq automaticDetectionReq);

    @POST("/ott/accounts/devices/switch")
    Call<Login> requestSwitchDevice(
            @Query("access_token") String access_token,
            @Body FrontEndLoader.SwitchDevices devices);

//    @POST("/ott/accounts/devices/switch")
//    Call<Void> requestSwitchDevice(
//            @Query("access_token") String access_token,
//            @Query("devices") String devices);

}
