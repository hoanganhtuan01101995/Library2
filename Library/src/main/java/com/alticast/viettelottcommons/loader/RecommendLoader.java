package com.alticast.viettelottcommons.loader;


import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.RecommendedVod;
import com.alticast.viettelottcommons.resource.TrendingDp;
import com.alticast.viettelottcommons.resource.Vod;
import com.alticast.viettelottcommons.resource.response.TrendingRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.acms.recommendation.RecommendMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;
import com.alticast.viettelottcommons.util.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mc.kim on 7/28/2016.
 */
public class RecommendLoader {
    private final int RELATED_VOD_SIZE = 14;
    private final int RELATED_CHANNEl_SIZE_RECOMMEND = 24;
    private final int RELATED_VOD_SIZE_RECOMMEND = 40;
    private final int RECOMMEND_MENU_SIZE = 50;
    private final int RECOMMEND_CHANNEL_SIZE = 14;
    public static final String RECOMMEND_CHANNEL_NETHRU = "VT_Phone_051_RCMD_List_Channel";

    private final String RECOMMEND_RELATED_VOD = "VT_TAB_011_VODINFO_AR_VOD";
    private final String RECOMMEND_VOD = "VT_TAB_010_VOD_PREF_VOD";
    private final String RECOMMEND_SVOD = "VT_TAB_009_SVOD_PREF_SVOD";
    private final String RECOMMEND_MENU = "VT_TAB_008_MYH_PREF_VOD";
    private final String RECOMMEND_CHANNEL = "VT_TAB_007_PRG_REL_VOD";
    private final int MAX_SIZE = 25;
    private static RecommendLoader instance = null;
    private final String TYPE_ENCORED = "json";

    public static final String RECOMMEND_TRENDING = "VT_Phone_049_TrendingNow_CAT_VOD";
    public static final String RECOMMEND_HOME = "VT_PHONE_045_HOME_VOD";
    public static final String RECOMMEND_BCW_TYPE_A = "VT_PHONE_045_HOME_VOD_BYW_A";
    public static final String RECOMMEND_BCW_TYPE_B = "VT_PHONE_045_HOME_VOD_BYW_B";
    public static final String RECOMMEND_BCW_TYPE_C = "VT_PHONE_045_HOME_VOD_BYW_C";

    private final String ENTRY_DETAIL = "vod/detail/";
    private final String ENTRY_MY_HOME = "myhome";
    private final String ENTRY_MY_MENU = "hot";


    public synchronized static RecommendLoader getInstance() {
        if (instance == null) {
            instance = new RecommendLoader();
        }
        return instance;
    }

    public void getRecommendHot(String id, final WindmillCallback callback) {

        RecommendMethod recommendMethod = ServiceGenerator.getInstance().createSerive(RecommendMethod.class);
        Call<RecommendedVod> call = recommendMethod.getRecommendedData(RECOMMEND_SVOD, id, "1", RELATED_VOD_SIZE, TYPE_ENCORED);
        call.enqueue(new Callback<RecommendedVod>() {
            @Override
            public void onResponse(Call<RecommendedVod> call, Response<RecommendedVod> response) {
                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {
                    ProgramLoader.getInstance().getRecommendedVod(response, ENTRY_MY_MENU, callback);

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<RecommendedVod> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void getTrendingNow(String type, final WindmillCallback callback) {
        RecommendMethod recommendMethod = ServiceGenerator.getInstance().createSerive(RecommendMethod.class);
        String id = HandheldAuthorization.getInstance().getCurrentId();
        Call<TrendingRes> call;
        String url = "http://10.60.70.209:18080/api1/contents/menus?version=OTT_NewUI&access_token=__touchott__";
        call = recommendMethod.getMenuId(url);
        call.enqueue(new Callback<TrendingRes>() {
            @Override
            public void onResponse(Call<TrendingRes> call, Response<TrendingRes> response) {
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
            public void onFailure(Call<TrendingRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    private String getUserId() {
        String userId = "GUEST";
        userId = HandheldAuthorization.getInstance().getCurrentId();
        if (userId == null) {
            userId = "GUEST";
        }
        return userId;
    }

    public void getRecommendTrending(String categoryId, String type, final WindmillCallback callback) {

        // type RECOMMEND_TRENDING
        RecommendMethod recommendMethod = ServiceGenerator.getInstance().createSerive(RecommendMethod.class);
        String id;
        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
            id = HandheldAuthorization.getInstance().getCurrentUser().getCust_id();
        } else {
            id = getUserId();
        }

        Call<TrendingRes> call;
//        String url = "http://10.60.70.206:8912/so-web-app/so/recommend";
        if (type.equalsIgnoreCase(RECOMMEND_TRENDING)) {
            call = recommendMethod.getRecommendedDataNowNew(type, id, "1", RELATED_VOD_SIZE_RECOMMEND, TYPE_ENCORED, categoryId);
        } else if (type.equalsIgnoreCase(RECOMMEND_CHANNEL_NETHRU)) {
            call = recommendMethod.getRecommendedDataNew(type, id, "1", RELATED_CHANNEl_SIZE_RECOMMEND, TYPE_ENCORED);
        } else {
            call = recommendMethod.getRecommendedDataNew(type, id, "1", RELATED_VOD_SIZE_RECOMMEND, TYPE_ENCORED);
        }
        call.enqueue(new Callback<TrendingRes>() {
            @Override
            public void onResponse(Call<TrendingRes> call, Response<TrendingRes> response) {
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
            public void onFailure(Call<TrendingRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }


    public void getRecommendRelatedVOD(Vod vod, final WindmillCallback callback) {

        RecommendMethod recommendMethod = ServiceGenerator.getInstance().createSerive(RecommendMethod.class);
        Call<RecommendedVod> call = recommendMethod.getRecommendedVod(RECOMMEND_RELATED_VOD,
                HandheldAuthorization.getInstance().getCurrentId(), "1", vod.getProgram().getId(),
                RELATED_VOD_SIZE, TYPE_ENCORED);
        call.enqueue(new Callback<RecommendedVod>() {
            @Override
            public void onResponse(Call<RecommendedVod> call, Response<RecommendedVod> response) {
                if (callback == null) {
                    return;
                }

                if (response != null && response.isSuccess()) {
                    ProgramLoader.getInstance().getRecommendedVod(response, ENTRY_DETAIL, callback);
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<RecommendedVod> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });


    }

    public void getRecommendMenu(final WindmillCallback callback) {
        RecommendMethod recommendMethod = ServiceGenerator.getInstance().createSerive(RecommendMethod.class);
        Call<RecommendedVod> call = recommendMethod.reqeustRecommendMenu(RECOMMEND_MENU, HandheldAuthorization.getInstance().getCurrentId(), "1", MAX_SIZE, MAX_SIZE,
                RECOMMEND_MENU_SIZE, TYPE_ENCORED);
        call.enqueue(new Callback<RecommendedVod>() {
            @Override
            public void onResponse(Call<RecommendedVod> call, Response<RecommendedVod> response) {
                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {
                    ProgramLoader.getInstance().getRecommendedVod(response, ENTRY_MY_HOME, callback);

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<RecommendedVod> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });

    }

    public void getRecommendChannel(final WindmillCallback callback) {
        RecommendMethod recommendMethod = ServiceGenerator.getInstance().createSerive(RecommendMethod.class);

        Call<RecommendedVod> call = recommendMethod.getRecommendedData
                (RECOMMEND_CHANNEL, HandheldAuthorization.getInstance().getCurrentId(), "1", RECOMMEND_CHANNEL_SIZE, TYPE_ENCORED);
        call.enqueue(new Callback<RecommendedVod>() {
            @Override
            public void onResponse(Call<RecommendedVod> call, Response<RecommendedVod> response) {
                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {
                    ProgramLoader.getInstance().getRecommendedVod(response, ENTRY_DETAIL, callback);

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<RecommendedVod> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });


    }

    public void sendRecommendLog(String id) {

        RecommendMethod inquireMethod = ServiceGenerator.getInstance().createSerive(RecommendMethod.class);
        Call<Void> call = inquireMethod.sendLog(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Logger.print(this, "sendRecommendLog : " + response.isSuccess());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Logger.print(this, "sendRecommendLog : Fail");
            }
        });
    }

}
