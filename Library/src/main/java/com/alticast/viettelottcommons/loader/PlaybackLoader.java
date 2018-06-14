package com.alticast.viettelottcommons.loader;

import android.accounts.AccountManager;
import android.util.Log;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.def.entry.EntryPathLogImpl;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.ChannelManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.manager.TimeManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.ChannelProduct;
import com.alticast.viettelottcommons.resource.Path;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.Program;
import com.alticast.viettelottcommons.resource.Schedule;
import com.alticast.viettelottcommons.resource.Series;
import com.alticast.viettelottcommons.resource.Vod;
import com.alticast.viettelottcommons.resource.ads.Ad;
import com.alticast.viettelottcommons.resource.request.ChromeCastPrepareBody;
import com.alticast.viettelottcommons.resource.request.LivePrepareBody;
import com.alticast.viettelottcommons.resource.response.ChromeCastRes;
import com.alticast.viettelottcommons.resource.response.PrepareChannelRes;
import com.alticast.viettelottcommons.resource.response.PrepareVodRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.acms.playback.StreamInfoMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.NetworkUtil;
import com.alticast.viettelottcommons.util.Util;
import com.google.gson.Gson;

import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by duyuno on 10/10/16.
 */
public class PlaybackLoader {

    public static final int PAUSE_VOD = 0;
    public static final int FINISH_VOD = 1;
    public static final int DESTROY_VOD = 2;

    public static final int PAUSE_CATCHUP = 0;
    public static final int FINISH_CATCHUP = 1;

    public static final int TYPE_VOD = 0;
    public static final int TYPE_CATCH_UP = 1;

    public static final String TYPE_3G_4G = "3";
    public static final String TYPE_Wifi = "4";
    public static final String CHROME_CAST = "5";

    public static final String PROVIDER = "NewViettelTV";

    private static PlaybackLoader ourInstance = new PlaybackLoader();

    public static PlaybackLoader getInstance() {
        return ourInstance;
    }

    private PlaybackLoader() {
    }

    public void prepare(NetworkUtil.NetworkType networkType, boolean isUseMenuPath, int type, final WindmillCallback callback, String... params) {

        String bwProfile = networkType == NetworkUtil.NetworkType.MOBILE ? TYPE_3G_4G : TYPE_Wifi;

        switch (type) {
            case TYPE_VOD:
                prepareVod(isUseMenuPath, bwProfile, params[0], params[1], callback);
                break;
            case TYPE_CATCH_UP:
                prepareCatchUp(bwProfile, params[0], params[1], callback);
                break;
        }
    }

    public void prepare(NetworkUtil.NetworkType networkType, boolean isUseMenuPath, int type, final WindmillCallback callback, boolean isChromeCast, String... params) {

        String bwProfile;
        if (isChromeCast) {
            bwProfile = CHROME_CAST;
        } else {
            bwProfile = networkType == NetworkUtil.NetworkType.MOBILE ? TYPE_3G_4G : TYPE_Wifi;
        }

        switch (type) {
            case TYPE_VOD:
                if (WindmillConfiguration.isTestBandWidth) {
                    prepareVodChromeCast(bwProfile, callback);
                } else {
                    prepareVod(isUseMenuPath, bwProfile, params[0], params[1], callback);
                }
                break;
            case TYPE_CATCH_UP:
                prepareCatchUp(bwProfile, params[0], params[1], callback);
                break;
        }
    }


    public void prepareVod(boolean isUseMenuPath, String bwProfile, String programId, String productId, final WindmillCallback callback) {
        StreamInfoMethod vODInfoMethod = ServiceGenerator.getInstance().createSerive(StreamInfoMethod.class);

        String version = WindmillConfiguration.isAdVersion ? "2" : "1";

//        String authorization = "Bearer " + AuthManager.getAccessToken();
        String contentType = "application/json;charset=UTF-8";
        String acceptLanguage = Locale.getDefault().getLanguage();
        String accept = "*/*";
        String menuPathId = null;
        menuPathId = isUseMenuPath ? EntryPathLogImpl.getInstance().getWatchMenuId() : EntryPathLogImpl.GENERAL_PATH;
//        String paymentType = "FREE";

        Call<PrepareVodRes> call = vODInfoMethod.prepareVod(AuthManager.getAccessToken(), bwProfile, PROVIDER, programId, productId, version, menuPathId,
                contentType, acceptLanguage, accept);

        call.enqueue(new Callback<PrepareVodRes>() {
            @Override
            public void onResponse(Call<PrepareVodRes> call, Response<PrepareVodRes> response) {
                if (callback == null) {
                    return;
                }

                if (response != null && response.isSuccess()) {
                    callback.onSuccess(response.body());

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<PrepareVodRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void prepareVodChromeCast(String bwProfile, final WindmillCallback callback) {
        StreamInfoMethod vODInfoMethod = ServiceGenerator.getChromeInstance().createChrome(StreamInfoMethod.class);

        ChromeCastPrepareBody livePrepareBody = new ChromeCastPrepareBody();
        livePrepareBody.setVersion(2);
        livePrepareBody.setRegionId("GUEST");
        livePrepareBody.setAssetId("83353_4");
        livePrepareBody.setFilename(String.format("%s.m3u8", "83353_4"));
        livePrepareBody.setUserId("83353_4");
        livePrepareBody.setBwProfile(bwProfile);
        livePrepareBody.setUserDeviceType("HANDHELD");
        livePrepareBody.setCategoryId("VC_OTT");
        Call<ChromeCastRes> call = vODInfoMethod.prepareVodChromeCast(livePrepareBody);

//        Call<PrepareVodRes> call = vODInfoMethod.prepareVod(AuthManager.getAccessToken(), bwProfile, programId, productId, version, menuPathId,
//                contentType, acceptLanguage, accept);

        call.enqueue(new Callback<ChromeCastRes>() {
            @Override
            public void onResponse(Call<ChromeCastRes> call, Response<ChromeCastRes> response) {
                if (callback == null) {
                    return;
                }

                if (response != null && response.isSuccess()) {
                    callback.onSuccess(response.body());

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ChromeCastRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void prepareCatchUp(String bwProfile, String catchUpId, String serviceId, final WindmillCallback callback) {
        StreamInfoMethod vODInfoMethod = ServiceGenerator.getInstance().createSerive(StreamInfoMethod.class);

        String version = WindmillConfiguration.isAdVersion ? "2" : "1";

//        String authorization = "Bearer " + AuthManager.getAccessToken();
        String contentType = "application/json;charset=UTF-8";
        String acceptLanguage = Locale.getDefault().getLanguage();
        String accept = "*/*";
        String menuPathId = EntryPathLogImpl.getInstance().getWatchMenuId();
        String paymentType = "FREE";

        Call<PrepareVodRes> call = vODInfoMethod.prepareCatchUp
                (AuthManager.getAccessToken(), bwProfile, PROVIDER
                        , catchUpId, serviceId, version, paymentType,
                        contentType, acceptLanguage, accept);
//        Call<PrepareVodRes> call = vODInfoMethod.prepareCatchUp(id1, id2, menuPathId, version,
//                                    authorization, contentType, acceptLanguage, accept);

        call.enqueue(new Callback<PrepareVodRes>() {
            @Override
            public void onResponse(Call<PrepareVodRes> call, Response<PrepareVodRes> response) {
                if (callback == null) {
                    return;
                }

                if (response != null && response.isSuccess()) {
                    callback.onSuccess(response.body());
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<PrepareVodRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void prepareLive(NetworkUtil.NetworkType networkType, ChannelProduct channelProduct, long mTimeDistance, final WindmillCallback callback) {
//        StreamInfoMethod vODInfoMethod = ServiceGenerator.getInstance().createSerive(StreamInfoMethod.class);

        String bwProfile = (networkType == NetworkUtil.NetworkType.MOBILE) ? TYPE_3G_4G : TYPE_Wifi;

        StreamInfoMethod vODInfoMethod = ServiceGenerator.getHttpsInstance().createSeriveHttps(StreamInfoMethod.class);

        String authorization = "Bearer " + AuthManager.getLoginToken();
        String contentType = "application/json;charset=UTF-8";
        String acceptLanguage = Locale.getDefault().getLanguage();
        String accept = "*/*";

        LivePrepareBody livePrepareBody = new LivePrepareBody();
        livePrepareBody.setVersion(WindmillConfiguration.isAdImageEnable ? 2 : 1);
        livePrepareBody.setRegionId("GUEST");
        livePrepareBody.setAssetId(channelProduct.getService_id());
        livePrepareBody.setChannelId(channelProduct.getService_id());
        livePrepareBody.setFilename(String.format("%s.m3u8", channelProduct.getService_id()));
        livePrepareBody.setManifestType("HLS");
        livePrepareBody.setBwProfile(bwProfile);
        livePrepareBody.setClient_id(WindmillConfiguration.clientId);
        livePrepareBody.setUserId(HandheldAuthorization.getInstance().getCurrentUser().getId());

        long ts = TimeManager.getInstance().getServerCurrentTimeMillis();

        String data = WindmillConfiguration.clientId + WindmillConfiguration.deviceId + HandheldAuthorization.getInstance().getCurrentUser().getId() + ts;
        String key = WindmillConfiguration.secretKey;

        String hash = Util.getScretKey(data, key);
        livePrepareBody.setHash(hash);

//        Call<PrepareChannelRes> call = WindmillConfiguration.isUseNewPrepareApi ?
//                vODInfoMethod.prepareChannelNew(AuthManager.getLoginToken(), hash, "" + ts, WindmillConfiguration.clientId, authorization, contentType, acceptLanguage, accept, livePrepareBody)
//                : vODInfoMethod.prepareChannelChange(authorization, contentType, acceptLanguage, accept, livePrepareBody);
        Call<PrepareChannelRes> call = vODInfoMethod.prepareChannelNew(AuthManager.getLoginToken(), hash, "" + ts, WindmillConfiguration.clientId, authorization, contentType, acceptLanguage, accept, livePrepareBody);

        call.enqueue(new Callback<PrepareChannelRes>() {
            @Override
            public void onResponse(Call<PrepareChannelRes> call, Response<PrepareChannelRes> response) {
                if (callback == null) {
                    return;
                }

                if (response != null && response.isSuccess()) {
                    callback.onSuccess(response.body());

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<PrepareChannelRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void prepareLiveEncryption(NetworkUtil.NetworkType networkType, ChannelProduct channelProduct, long mTimeDistance, final WindmillCallback callback) {
//        StreamInfoMethod vODInfoMethod = ServiceGenerator.getInstance().createSerive(StreamInfoMethod.class);

        String serviceId = channelProduct.getService_id();
        String newServiceId = Integer.parseInt(serviceId) + 1000 + ""; // service Id for channel that use in chrome cast

        String bwProfile = (networkType == NetworkUtil.NetworkType.MOBILE) ? TYPE_3G_4G : TYPE_Wifi;

        StreamInfoMethod vODInfoMethod = ServiceGenerator.getHttpsInstance().createSeriveHttps(StreamInfoMethod.class);

        String authorization = "Bearer " + AuthManager.getLoginToken();
        String contentType = "application/json;charset=UTF-8";
        String acceptLanguage = Locale.getDefault().getLanguage();
        String accept = "*/*";

        LivePrepareBody livePrepareBody = new LivePrepareBody();
        livePrepareBody.setVersion(WindmillConfiguration.isAdImageEnable ? 2 : 1);
        livePrepareBody.setRegionId("GUEST");
        livePrepareBody.setAssetId(newServiceId);
        livePrepareBody.setChannelId(newServiceId);
        livePrepareBody.setFilename(String.format("%s.m3u8", newServiceId));
        livePrepareBody.setManifestType("HLS");
        livePrepareBody.setBwProfile(bwProfile);
        livePrepareBody.setClient_id(WindmillConfiguration.clientId);
        livePrepareBody.setUserId(HandheldAuthorization.getInstance().getCurrentUser().getId());

        long ts = TimeManager.getInstance().getServerCurrentTimeMillis();

        String data = WindmillConfiguration.clientId + WindmillConfiguration.deviceId + HandheldAuthorization.getInstance().getCurrentUser().getId() + ts;
        String key = WindmillConfiguration.secretKey;

        String hash = Util.getScretKey(data, key);
        livePrepareBody.setHash(hash);

//        Call<PrepareChannelRes> call = WindmillConfiguration.isUseNewPrepareApi ?
//                vODInfoMethod.prepareChannelNew(AuthManager.getLoginToken(), hash, "" + ts, WindmillConfiguration.clientId, authorization, contentType, acceptLanguage, accept, livePrepareBody)
//                : vODInfoMethod.prepareChannelChange(authorization, contentType, acceptLanguage, accept, livePrepareBody);
        Call<PrepareChannelRes> call = vODInfoMethod.prepareChannelNew(AuthManager.getLoginToken(), hash, "" + ts, WindmillConfiguration.clientId, authorization, contentType, acceptLanguage, accept, livePrepareBody);

        call.enqueue(new Callback<PrepareChannelRes>() {
            @Override
            public void onResponse(Call<PrepareChannelRes> call, Response<PrepareChannelRes> response) {
                if (callback == null) {
                    return;
                }

                if (response != null && response.isSuccess()) {
                    callback.onSuccess(response.body());

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<PrepareChannelRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }


    public void findNextEpisode(String programId, final WindmillCallback callback) {
        StreamInfoMethod vODInfoMethod = ServiceGenerator.getChromeInstance().createSerive(StreamInfoMethod.class);

        String contentType = "application/json;charset=UTF-8";
        String acceptLanguage = Locale.getDefault().getLanguage();
        String accept = "*/*";

        String include = null;
        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
            include = "product,purchase,multilang";
        } else if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
            include = "product,purchase,fpackage,multilang";
        } else {
            include = "product,fpackage,multilang";
        }

//        String logs = ChannelLogManager.get().flush();

        String url = "api1/contents/programs/" + programId + "/find_episode/next";
        Call<Vod> call = vODInfoMethod.findNextEpisode(url, AuthManager.getAccessToken(), "long", include,
                contentType, acceptLanguage, accept);

        call.enqueue(new Callback<Vod>() {
            @Override
            public void onResponse(Call<Vod> call, Response<Vod> response) {
                if (callback == null) {
                    return;
                }

                if (response != null && response.isSuccess()) {
                    callback.onSuccess(response.body());

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<Vod> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void changeVodStatus(int type, Program program, Product product, String purchaseId, long startTime, int offset, int runningTime, final WindmillCallback callback) {
        StreamInfoMethod vODInfoMethod = ServiceGenerator.getInstance().createSerive(StreamInfoMethod.class);

        String authorization = "Bearer " + AuthManager.getAccessToken();
        String contentType = "application/json;charset=UTF-8";
        String acceptLanguage = Locale.getDefault().getLanguage();
        String accept = "*/*";
        String menuPathId = EntryPathLogImpl.getInstance().getWatchMenuId();
        String entryPath = EntryPathLogImpl.getInstance().getPurchaseMenuString();

        Call<Void> call = null;

        switch (type) {
            case PAUSE_VOD:
                if (program.getSeries() == null) {
                    call = vODInfoMethod.pauseVod(program.getId(), program.getTitle(WindmillConfiguration.LANGUAGE),
                            String.valueOf(startTime), String.valueOf(TimeManager.getInstance().getServerCurrentTimeMillis()),
                            String.valueOf(offset),
                            product.getId(), String.valueOf(runningTime), entryPath, menuPathId, product.getPid(), purchaseId,
                            authorization, contentType, acceptLanguage, accept);
                } else {
                    call = vODInfoMethod.pauseVodSeries(AuthManager.getAccessToken(), program.getId(), program.getTitle(WindmillConfiguration.LANGUAGE),
                            String.valueOf(startTime), String.valueOf(TimeManager.getInstance().getServerCurrentTimeMillis()),
                            String.valueOf(offset), product.getId(), String.valueOf(runningTime), entryPath,
                            product.getPid(), purchaseId, menuPathId,
                            program.getSeries().getId(), program.getSeries().getSeason(),
                            program.getSeries().getEpisode(),
                            authorization, contentType, acceptLanguage, accept);
                }
                break;
            case FINISH_VOD:
                if (program.getSeries() == null) {
                    call = vODInfoMethod.finishVod(program.getId(), program.getTitle(WindmillConfiguration.LANGUAGE),
                            String.valueOf(startTime), String.valueOf(TimeManager.getInstance().getServerCurrentTimeMillis()),
                            product.getId(), String.valueOf(runningTime), purchaseId, entryPath, menuPathId, product.getPid(),
                            authorization, contentType, acceptLanguage, accept);
                } else {
                    call = vODInfoMethod.finishVodSeries(AuthManager.getAccessToken(), program.getId(), program.getTitle(WindmillConfiguration.LANGUAGE),
                            String.valueOf(startTime), String.valueOf(TimeManager.getInstance().getServerCurrentTimeMillis()),
                            product.getId(), purchaseId, String.valueOf(runningTime), entryPath, program.getSeries().getId(), program.getSeries().getSeason(),
                            program.getSeries().getEpisode(), menuPathId, product.getPid(),
                            authorization, contentType, acceptLanguage, accept);
                }
                break;
            case DESTROY_VOD:
                call = vODInfoMethod.destroyVod(program.getId(),
                        authorization, contentType, acceptLanguage, accept);
                break;
        }


        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (callback == null) {
                    return;
                }

                if (response != null && response.isSuccess()) {
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

    public void changeCatchUpStatus(int type, String catchUpId, String purchaseId, Schedule schedule, long startTime, int offset, int runningTime, final WindmillCallback callback) {
        StreamInfoMethod vODInfoMethod = ServiceGenerator.getInstance().createSerive(StreamInfoMethod.class);

        String authorization = "Bearer " + AuthManager.getAccessToken();
        String contentType = "application/json;charset=UTF-8";
        String acceptLanguage = Locale.getDefault().getLanguage();
        String accept = "*/*";
        String menuPathId = EntryPathLogImpl.getInstance().getWatchMenuId();
        String entryPath = EntryPathLogImpl.getInstance().getPurchaseMenuString();

        Call<Void> call = null;

        switch (type) {
            case PAUSE_CATCHUP:
                call = vODInfoMethod.pauseCatchUp(catchUpId, schedule.getTitle(WindmillConfiguration.LANGUAGE),
                        String.valueOf(startTime), String.valueOf(TimeManager.getInstance().getServerCurrentTimeMillis()),
                        String.valueOf(offset), String.valueOf(runningTime), entryPath, purchaseId,
                        authorization, contentType, acceptLanguage, accept);
                break;
            case FINISH_CATCHUP:
                call = vODInfoMethod.finishCatchUp(catchUpId, schedule.getTitle(WindmillConfiguration.LANGUAGE),
                        String.valueOf(startTime), String.valueOf(TimeManager.getInstance().getServerCurrentTimeMillis()),
                        String.valueOf(runningTime), entryPath, purchaseId,
                        authorization, contentType, acceptLanguage, accept);
                break;
        }


        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (callback == null) {
                    return;
                }

                if (response != null && response.isSuccess()) {
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

    public void likeVod(String id, final WindmillCallback callback) {
        StreamInfoMethod vODInfoMethod = ServiceGenerator.getInstance().createSerive(StreamInfoMethod.class);

        String url = "api1/contents/programs/" + id + "/likes";

        String contentType = "application/json;charset=UTF-8";
        String acceptLanguage = Locale.getDefault().getLanguage();
        String accept = "*/*";

//        String logs = ChannelLogManager.get().flush();

        Call<Void> call = vODInfoMethod.likeVod(url, AuthManager.getAccessToken(), contentType, acceptLanguage, accept);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (callback == null) {
                    return;
                }

                if (response != null && response.isSuccess()) {
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

    public void unlikeVod(String id, final WindmillCallback callback) {
        StreamInfoMethod vODInfoMethod = ServiceGenerator.getInstance().createSerive(StreamInfoMethod.class);

        String url = "api1/contents/programs/" + id + "/likes";

        String contentType = "application/json;charset=UTF-8";
        String acceptLanguage = Locale.getDefault().getLanguage();
        String accept = "*/*";

//        String logs = ChannelLogManager.get().flush();

        Call<Void> call = vODInfoMethod.unlikeVod(url, AuthManager.getAccessToken(), "delete", contentType, acceptLanguage, accept);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (callback == null) {
                    return;
                }

                if (response != null && response.isSuccess()) {
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

    public void getPlacementDecisionRequest(String requestId, boolean isResume, String inventoryType, final WindmillCallback callback) {
        StreamInfoMethod vODInfoMethod = ServiceGenerator.getAdsInstance().createAdsSerive(StreamInfoMethod.class);

        String contentType = "application/json;charset=UTF-8";
        String acceptLanguage = Locale.getDefault().getLanguage();
        String accept = "*/*";
        String region = HandheldAuthorization.getInstance().getCurrentUser().getSo_id();
        if (region == null || region.isEmpty()) {
            region = "OTT";
        }

        String url = WindmillConfiguration.getBaseADDS() + "ADDSse/PlacementDecisionRequest/3.0";
        Call<Ad> call = vODInfoMethod.getPlacementDecisionRequest(url, UUID.randomUUID().toString(), requestId, "" + isResume, inventoryType, region
                , "HANDHELD", HandheldAuthorization.getInstance().getCurrentId(), "*"
                , contentType, acceptLanguage, accept);

        call.enqueue(new Callback<Ad>() {
            @Override
            public void onResponse(Call<Ad> call, Response<Ad> response) {
                if (callback == null) {
                    return;
                }

                if (response != null && response.isSuccess()) {
                    callback.onSuccess(response.body());

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<Ad> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void sendAdsLogs(String url) {

        if (url == null || url.isEmpty()) {
            return;
        }

//        Log.e("Ads Collector", "" + url);

        StreamInfoMethod vODInfoMethod = ServiceGenerator.getAdsInstance().createAdsSerive(StreamInfoMethod.class);


        Call<Void> call = vODInfoMethod.sendAdsLog(url);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

}
