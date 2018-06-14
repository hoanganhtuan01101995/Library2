package com.alticast.viettelottcommons.loader;

import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.def.PlatformFileds;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.Mail;
import com.alticast.viettelottcommons.resource.Platform;
import com.alticast.viettelottcommons.resource.response.MailListRes;
import com.alticast.viettelottcommons.resource.response.PlatformRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.acms.platform.PlatformMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mc.kim on 8/10/2016.
 */
public class PlatformLoader {

    public static final int MAIL_LIMIT = 30;
    private final String FORMAT_SHORT = "short";
    private final String FORMAT_LONG = "long";
    private static PlatformLoader ourInstance = new PlatformLoader();

    public static PlatformLoader getInstance() {
        return ourInstance;
    }

    private PlatformLoader() {
    }

    private HashMap<String, String> platformMap = new HashMap<>();

    public void loadPlatform(final WindmillCallback callback) {
        PlatformMethod platformMethod = ServiceGenerator.getInstance().createSerive(PlatformMethod.class);
        Call<PlatformRes> call = platformMethod.getPlatform(AuthManager.getGuestToken());
        call.enqueue(new Callback<PlatformRes>() {
            @Override
            public void onResponse(Call<PlatformRes> call, Response<PlatformRes> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    Platform platform = response.body().getPlatform();
                    initPlatFormMap(platform);
                    callback.onSuccess(response.body());
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<PlatformRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }


    public String getPlatFormValue(String key) {
        return platformMap.get(key) != null ? platformMap.get(key) : "";
    }


    private void initPlatFormMap(Platform platform) {
        platformMap.put(PlatformFileds.REGIONAL_PN, platform.getValue(PlatformFileds.REGIONAL_PN));
        platformMap.put(PlatformFileds.VERSION_PAD, platform.getValue(PlatformFileds.VERSION_PAD));
        platformMap.put(PlatformFileds.VERSION_PHONE, platform.getValue(PlatformFileds.VERSION_PHONE));
        platformMap.put(PlatformFileds.REGIONAL_PHONE, platform.getValue(PlatformFileds.REGIONAL_PHONE));
    }

    public void getPairingGuide(final WindmillCallback callback) {
        PlatformMethod platformMethod = ServiceGenerator.getInstance().createSerive(PlatformMethod.class);
        String from = "ott";
        String type = "window";
        String format = "long";

        Call<MailListRes> call = platformMethod.getPairingGuide(AuthManager.getAccessToken(), type, format, from);
        call.enqueue(new Callback<MailListRes>() {
            @Override
            public void onResponse(Call<MailListRes> call, Response<MailListRes> response) {
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
            public void onFailure(Call<MailListRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void getMailList(String type, int offset, final WindmillCallback callback) {
        PlatformMethod platformMethod = ServiceGenerator.getInstance().createSerive(PlatformMethod.class);
        String until = null;
        String since = null;
        String from = "ott";
        Call<MailListRes> call = platformMethod.getMailList(AuthManager.getAccessToken(), type, since, until, offset, MAIL_LIMIT, FORMAT_LONG, from);

        call.enqueue(new Callback<MailListRes>() {
            @Override
            public void onResponse(Call<MailListRes> call, Response<MailListRes> response) {
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
            public void onFailure(Call<MailListRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });

    }

    public void getMailContent(String mailId, final WindmillCallback callback) {
        PlatformMethod platformMethod = ServiceGenerator.getInstance().createSerive(PlatformMethod.class);
        String url = "/api1/contents/mails/" + mailId;
        Call<Mail> call = platformMethod.getMailContent(url,AuthManager.getAccessToken());

        call.enqueue(new Callback<Mail>() {
            @Override
            public void onResponse(Call<Mail> call, Response<Mail> response) {
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
            public void onFailure(Call<Mail> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });

    }


}
