package com.alticast.viettelottcommons.service;


import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.alticast.android.util.Log;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.dialog.PhoneLoginFragment;
import com.alticast.viettelottcommons.loader.FrontEndLoader;
import com.alticast.viettelottcommons.loader.ProgramLoader;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.ChannelManager;
import com.alticast.viettelottcommons.manager.CollectLogManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.manager.MyContentManager;
import com.alticast.viettelottcommons.manager.TimeManager;
import com.alticast.viettelottcommons.resource.AccessToken;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.Login;
import com.alticast.viettelottcommons.resource.MyDeviceAccount;
import com.alticast.viettelottcommons.resource.request.DelegationReq;
import com.alticast.viettelottcommons.serviceMethod.upms.FrontEndMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.Util;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.HttpURLConnection;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Response.Builder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mc.kim on 3/7/2016.
 */
public class ServiceGenerator {

    public static final Log LOG = Log.createLog("ServiceGenerator");

    private static Retrofit retrofit = null;
    private static Retrofit retrofitChrome = null;
    private static Retrofit retrofitAds = null;
    private static Retrofit retrofitSendLog = null;
    private static Retrofit retrofitHttps = null;
    private static Retrofit retrofitHttp = null;
    private static Retrofit retrofitGetChannelIds = null;
    public static final String ERROR_CODE_1024 = "U0124";
    public static final String ERROR_CODE_BLOCK_ID = "F104";

    public static final String[] listSendLogUrl= new String[] {
        "api1/contents"
    };


    private static ServiceGenerator ourInstance = new ServiceGenerator(false);
    private static ServiceGenerator ourAdsInstance = new ServiceGenerator();
    private static ServiceGenerator ourHttpsInstance = new ServiceGenerator(true);
    private static ServiceGenerator ourHttpInstance = new ServiceGenerator();
    private static ServiceGenerator chromeInstance = new ServiceGenerator(WindmillConfiguration.chromeUrl, true);
    private static ServiceGenerator sendLogInstant = new ServiceGenerator(WindmillConfiguration.SENDLOG_URL);
    private static ServiceGenerator getChannelIdInstance = new ServiceGenerator(WindmillConfiguration.baseUrl, 0);

    public static ServiceGenerator getInstance() {
        return ourInstance;
    }
    public static ServiceGenerator getChannelIdsInstance() {
        return getChannelIdInstance;
    }

    public static ServiceGenerator getChromeInstance() {
        return chromeInstance;
    }

    public static ServiceGenerator getAdsInstance() {
        return ourAdsInstance;
    }

    public static ServiceGenerator getSendLogInstance() {
        return sendLogInstant;
    }

    public static ServiceGenerator getHttpsInstance() {
        return ourHttpsInstance;
    }
    public static ServiceGenerator getHttpInstance() {
        return ourHttpInstance;
    }



    public static boolean isSendLogUrl (String originalUrl) {
        for(String url : listSendLogUrl) {
            if(originalUrl.contains(url)) {
                return true;
            }
        }

        return false;
    }

    protected ServiceGenerator() {
        retrofitAds = createAdsRetrofit();
        initHttp();
    }

    protected ServiceGenerator(String url, int i) {
        retrofitGetChannelIds = createUnJsonFormat(url);
    }

    protected Retrofit createUnJsonFormat(String baseUrl) {

        Gson gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat(DateFormat.LONG)
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .setVersion(1.0)
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                ;
        if (baseUrl.contains("https://")) {
            builder.client(createTrustHttpClient());
        } else {
            builder.client(createHttpClient());
        }
        return builder.build();
    }

    public void initHttp() {
        retrofitHttp = createRetrofit(WindmillConfiguration.getBaseUrl());
    }

    protected ServiceGenerator(boolean isHttps) {
        if (isHttps) {
            if (retrofitHttps == null)
                retrofitHttps = createRetrofit(WindmillConfiguration.getBaseHttpsUrl());
        } else {
            if (retrofit == null)
                retrofit = createRetrofit(WindmillConfiguration.getBaseHttpsUrl());
        }
    }

    protected ServiceGenerator(boolean isHttps, boolean isChrome) {
        retrofitChrome = createChromeRetrofit(WindmillConfiguration.chromeUrl);
    }

    protected ServiceGenerator(String url) {
        retrofitSendLog = createRetrofit(url);
    }

    protected ServiceGenerator(String url, boolean is) {
        retrofitChrome = createChromeRetrofit(url);
    }

//    private static boolean isInit = false;

    //    protected Retrofit createRetrofit(String baseUrl, OkHttpClient httpClient) {
//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create());
//        if (httpClient != null) {
//            builder.client(httpClient);
//        }
//        return builder.build();
//    }
    protected Retrofit createRetrofit(String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());
        if (baseUrl.contains("https://")) {
            builder.client(createTrustHttpClient());
        } else {
            builder.client(createHttpClient());
        }
        return builder.build();
    }
    protected Retrofit createAdsRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(WindmillConfiguration.getBaseADDS())
                .addConverterFactory(GsonConverterFactory.create());
        if (WindmillConfiguration.getBaseADDS().contains("https://")) {
            builder.client(createTrustHttpClient(ServiceConfiguration.CONNECTION_ADS_TIME_OUT));
        } else {
            builder.client(createHttpClient(ServiceConfiguration.CONNECTION_ADS_TIME_OUT));
        }
        return builder.build();
    }

    protected Retrofit createChromeRetrofit(String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());
        builder.client(createHttpChromeClient());
        return builder.build();
    }


    public <S> S createSerive(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
    public <S> S createHttpSerive(Class<S> serviceClass) {
        return retrofitHttp.create(serviceClass);
    }

    public <S> S createAdsSerive(Class<S> serviceClass) {
        return retrofitAds.create(serviceClass);
    }

    public <S> S createSeriveSendLog(Class<S> serviceClass) {
        return retrofitSendLog.create(serviceClass);
    }

    public <S> S createChrome(Class<S> serviceClass) {
        return retrofitChrome.create(serviceClass);
    }

    public <S> S createSeriveHttps(Class<S> serviceClass) {
        return retrofitHttps.create(serviceClass);
    }

    public <S> S createGetChannelIds(Class<S> serviceClass) {
        return retrofitGetChannelIds.create(serviceClass);
    }

//    public static <S> S createService(Class<S> serviceClass) {
////        if(!isInit){
////            isInit = true;
//        httpClient.interceptors().add(new HeaderInterceptor());
//        httpClient.networkInterceptors().add(new ResponseInterceptor());
//        httpClient.retryOnConnectionFailure(true);
//        httpClient.connectTimeout(ServiceConfiguration.CONNECTION_TIME_OUT, TimeUnit.SECONDS);
//
//        OkHttpClient client = httpClient.build();
//        retrofit = builder.client(client).build();
////        }
//
//        return retrofit.create(serviceClass);
//    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public OkHttpClient createHttpClient() {
        ResponseInterceptor responseInterceptor = new ResponseInterceptor();
        HeaderInterceptor requestHeaderInterceptor = new HeaderInterceptor();
        return new OkHttpClient.Builder()
                .addInterceptor(responseInterceptor)
                .addInterceptor(requestHeaderInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(ServiceConfiguration.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(ServiceConfiguration.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .build();
    }
    public OkHttpClient createHttpClient(int timeout) {
        ResponseInterceptor responseInterceptor = new ResponseInterceptor();
        HeaderInterceptor requestHeaderInterceptor = new HeaderInterceptor();
        return new OkHttpClient.Builder()
                .addInterceptor(responseInterceptor)
                .addInterceptor(requestHeaderInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .build();
    }

    public OkHttpClient createHttpChromeClient() {
//        ResponseInterceptor responseInterceptor = new ResponseInterceptor();
        HeaderInterceptor requestHeaderInterceptor = new HeaderInterceptor();
        return new OkHttpClient.Builder()
//                .addInterceptor(responseInterceptor)
                .addInterceptor(requestHeaderInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(ServiceConfiguration.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(ServiceConfiguration.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    public OkHttpClient createTrustHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            ResponseInterceptor responseInterceptor = new ResponseInterceptor();
            HeaderInterceptor requestHeaderInterceptor = new HeaderInterceptor();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                    .addInterceptor(responseInterceptor)
                    .addInterceptor(requestHeaderInterceptor)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(ServiceConfiguration.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(ServiceConfiguration.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                    .build();


            return okHttpClient;
        } catch (Exception e) {
            return null;
        }
    }
    public OkHttpClient createTrustHttpClient(int timeout) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            ResponseInterceptor responseInterceptor = new ResponseInterceptor();
            HeaderInterceptor requestHeaderInterceptor = new HeaderInterceptor();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                    .addInterceptor(responseInterceptor)
                    .addInterceptor(requestHeaderInterceptor)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .build();


            return okHttpClient;
        } catch (Exception e) {
            return null;
        }
    }

    private static class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
//            original.url().newBuilder().addQueryParameter("uuid", "" + WindmillConfiguration.deviceId);
            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Accept", "text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("charset", "UTF-8")
                    .addHeader("Accept-Language", WindmillConfiguration.LANGUAGE)
                    .method(original.method(), original.body());


            if(isSendLogUrl(original.url().toString()) && CollectLogManager.get().isHasLog()) {

                requestBuilder.addHeader("X-App-Presence", CollectLogManager.get().flush());

            }
//            else {
//                requestBuilder.addHeader("X-App-Presence", "Dont have log");
//            }

            Request request = requestBuilder.build();

//            request.url().newBuilder().setEncodedQueryParameter("uuid", "123");
//            request.url().newBuilder().addEncodedQueryParameter("uuid", "123");
//            request.url().newBuilder().addQueryParameter("uuid", "" + WindmillConfiguration.deviceId);
//
//            LOG.printMsg("request url : " + request.url().toString());
            if(request.header("X-App-Presence") != null && !request.header("X-App-Presence").trim().isEmpty()) {
                LOG.printMsg("X-App-Presence url : " + request.url().toString());
                LOG.printMsg("X-App-Presence value : " + request.header("X-App-Presence"));
            }
            return chain.proceed(request);
        }
    }


    private static class ResponseInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {

            return checkTokenValidation(chain, chain.request());
        }
    }


    private static Response checkTokenValidation(Interceptor.Chain chain, Request request) throws IOException {

        Response response = chain.proceed(request);


//        boolean isTokenUsed = request.url().toString().contains("access_token");
        String token = request.url().queryParameter("access_token");
//        LOG.printMsg("access_token: " + params);


        TimeManager.getInstance().initTime(response);
//        LOG.printMsg("checkTokenValidation : url : " + request.url().toString() + " | " + response.code());
//        LOG.printMsg("checkTokenValidation : url : " + request.url().toString() + " | " + response.code());
        Logger.d("duyuno", "checkTokenValidation : url : " + request.url().toString() + " | " + response.code());

        if (token != null && !token.isEmpty() && !token.equals(WindmillConfiguration.guestToken) && !response.isSuccessful()) {
            if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                try {
//                    Logger.d("duyuno", "token invalid : url : " + request.url().toString() + " | " + response.code());
                    AuthManager.UserLevel level = AuthManager.currentLevel();

                    if (level == AuthManager.UserLevel.LEVEL1) {
                        return response;
                    }

                    if(level == AuthManager.UserLevel.LEVEL3) {
                        Logger.d("duyuno", "pairing token invalid : url : " + request.url().toString() + " | " + response.code());
                        HandheldAuthorization.getInstance().setPairingAccessToken(null);
                        reissuesPairingToken(chain, request, response);
                    } else {
                        response = reissueToken(chain, request, response);
                    }


                } catch (Exception e) {
//                    LOG.printMsg("Exception when delegation: " + e.getMessage());
                }
            }
        }
        return response;
    }

    private static boolean tokenReissueS = false;
    private static int timeRetry = -1;

    private static Response reissuesPairingToken(final Interceptor.Chain chain, final Request request, Response originalResponse) throws IOException {

        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);

        Call<MyDeviceAccount> callMyAccount = frontEndMethod.getMyAccount(AuthManager.getLoginToken(), "device,pairing");
        retrofit2.Response<MyDeviceAccount> responseCallMyAccount = callMyAccount.execute();

        if (!responseCallMyAccount.isSuccess()) {
            return originalResponse;
        }

        MyDeviceAccount myDeviceAccount = responseCallMyAccount.body();

        HandheldAuthorization.getInstance().putString(HandheldAuthorization.ID, myDeviceAccount.getId());
        HandheldAuthorization.getInstance().getCurrentUser().setId(myDeviceAccount.getId());


        boolean currentUserPaired = myDeviceAccount.getPairing() != null;

        if (!currentUserPaired) {
            HandheldAuthorization.getInstance().setPairingAccessToken(null);
        } else {
            long ts = TimeManager.getInstance().getServerCurrentTimeMillis();
            String nonce = Util.getRandomHexString(6);

            Call<AccessToken> callCheckAccount = frontEndMethod.postAccessToken(myDeviceAccount.getPairing().getHandheld_id(), WindmillConfiguration.defaultPassword,
                    WindmillConfiguration.clientId, myDeviceAccount.getId(), ts, nonce,
                    Util.getScretKey(
                            WindmillConfiguration.clientId + myDeviceAccount.getId() + ts + nonce, WindmillConfiguration.secretKey));

            retrofit2.Response<AccessToken> responseCheckAccount = callCheckAccount.execute();

            if (!responseCallMyAccount.isSuccess()) {
                HandheldAuthorization.getInstance().setPairingAccessToken(null);
//                    return originalResponse;
            } else {
                HandheldAuthorization.getInstance().setPairingAccessToken(responseCheckAccount.body());
            }
        }
        request.url().newBuilder().setEncodedQueryParameter("access_token", AuthManager.getAccessToken());

        Logger.d("duyuno", "refresh pairing token thanh cong : url : " + AuthManager.getAccessToken());

        Response response = chain.proceed(request);
        return response;
    }

    private static Response reissueToken(final Interceptor.Chain chain, final Request request, Response originalResponse) throws IOException {
        tokenReissueS = false;

//        Logger.print(ServiceGenerator.class, "old Token: " + AuthManager.getAccessToken());
        if (timeRetry < 0) {
            timeRetry = 0;
        }
        if (timeRetry >= 3) {
            timeRetry = -1;
            return originalResponse;
        }
        timeRetry++;

        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
        AuthManager.UserLevel level = AuthManager.currentLevel();

        if (level == AuthManager.UserLevel.LEVEL1) {
            return originalResponse;
        }

        String refresh_token = HandheldAuthorization.getInstance().getCurrentUser().getRefresh_token();
        String hash = Util.getScretKey(refresh_token + WindmillConfiguration.clientId,
                WindmillConfiguration.secretKey);

        Call<Login> call = frontEndMethod.requestDelegation(new DelegationReq(refresh_token,
                WindmillConfiguration.clientId, hash));

//        Logger.print(ServiceGenerator.class, "get new Token: ");
        retrofit2.Response<Login> responseRefresh = null;
        try {
            responseRefresh = call.execute();
        } catch (Exception e) {
//            LOG.printMsg("Exception when delegation: " + e.getMessage());
//            Logger.d("duyuno", "Exception when delegation: " + e.getMessage());
        }

//        Logger.print(ServiceGenerator.class, "after get new Token: ");

//        if (responseRefresh == null || !responseRefresh.isSuccess()) {
////            Logger.print(ServiceGenerator.class, "refreshFail Token: ");
//            return originalResponse;
//        }

        if (responseRefresh != null) {
            if(responseRefresh.isSuccess()) {
//                Logger.d("duyuno", "Token: " + responseRefresh.body().getAccess_token());
                HandheldAuthorization.getInstance().setLoginToken(responseRefresh.body());
            } else {
//                Logger.d("duyuno", "responseRefresh: " + responseRefresh.body());
                ApiError error = ErrorUtil.parseError(responseRefresh);
//                Logger.d("duyuno", "responseRefresh error: " + error);
                if (error != null && error.getErrorCode().equalsIgnoreCase(ERROR_CODE_1024)) {
//                    Logger.d("duyuno", "" + error);
                    HandheldAuthorization.getInstance().putBoolean(ERROR_CODE_1024, true);
                    return originalResponse;
                }
            }

        }
//        Logger.d("duyuno", "Response: " + responseRefresh.body());
//        if (responseRefresh != null && responseRefresh.isSuccess()) {
//
//        }


//        if (level == AuthManager.UserLevel.LEVEL3) {
//            Call<MyDeviceAccount> callMyAccount = frontEndMethod.getMyAccount(AuthManager.getLoginToken(), "device,pairing");
//            retrofit2.Response<MyDeviceAccount> responseCallMyAccount = callMyAccount.execute();
//
//            if (!responseCallMyAccount.isSuccess()) {
//                return originalResponse;
//            }
//
//            MyDeviceAccount myDeviceAccount = responseCallMyAccount.body();
//
//            HandheldAuthorization.getInstance().putString(HandheldAuthorization.ID, myDeviceAccount.getCellphone());
//            HandheldAuthorization.getInstance().getCurrentUser().setId(myDeviceAccount.getCellphone());
//
//
//            boolean currentUserPaired = myDeviceAccount.getPairing() != null;
//
//            if (!currentUserPaired) {
//                HandheldAuthorization.getInstance().setPairingAccessToken(null);
//            } else {
//                long ts = TimeManager.getInstance().getServerCurrentTimeMillis();
//                String nonce = Util.getRandomHexString(6);
//
//                Call<AccessToken> callCheckAccount = frontEndMethod.postAccessToken(myDeviceAccount.getPairing().getHandheld_id(), WindmillConfiguration.defaultPassword,
//                        WindmillConfiguration.clientId, myDeviceAccount.getId(), ts, nonce,
//                        Util.getScretKey(
//                                WindmillConfiguration.clientId + myDeviceAccount.getId() + ts + nonce, WindmillConfiguration.secretKey));
//
//                retrofit2.Response<AccessToken> responseCheckAccount = callCheckAccount.execute();
//
//                if (!responseCallMyAccount.isSuccess()) {
//                    HandheldAuthorization.getInstance().setPairingAccessToken(null);
////                    return originalResponse;
//                } else {
//                    HandheldAuthorization.getInstance().setPairingAccessToken(responseCheckAccount.body());
//                }
//
//
//            }
//
//
//        }

//        Logger.print(ServiceGenerator.class, "new Token: " + AuthManager.getAccessToken());
//        Logger.d("duyuno", "new Token: " + AuthManager.getAccessToken());

        request.url().newBuilder().setEncodedQueryParameter("access_token", AuthManager.getAccessToken());

        Response response = chain.proceed(request);
        return response;

    }


//        FrontEndLoader.getInstance().refreshToken(new WindmillCallback() {
//            @Override
//            public void onSuccess(Object obj) {
//                //성공 retry
//                tokenReissueS = true;
//
//                Logger.print(this, "reissueToken  onSuccess " + AuthManager.getAccessToken());
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//                tokenReissueS = false;
//                Logger.print(this, "reissueToken  onFailure , " + t.getMessage());
//            }
//
//            @Override
//            public void onError(ApiError error) {
//                tokenReissueS = false;
//                Logger.print(this, "reissueToken  onError ");
//
//            }
//        });
//
//
//        if (tokenReissueS) {
//            request.url().newBuilder().setEncodedQueryParameter("access_token", AuthManager.getAccessToken());
//            Response response = chain.proceed(request);
//            return response;
//        } else {
//            return originalResponse;
//        }
}
