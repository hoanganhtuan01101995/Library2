package com.alticast.viettelottcommons.loader;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.response.ConfigListRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.acms.platform.ConfigMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mc.kim on 10/5/2016.
 */
public class ConfigLoader {
    private final String KEY_AGREE_VIE = "agree.vie";
    private final String KEY_AGREE_ENG = "agree.eng";
    private static ConfigLoader ourInstance = new ConfigLoader();

    public static ConfigLoader getInstance() {
        return ourInstance;
    }

    private ConfigLoader() {
    }

    private HashMap<String, String> configMap = new HashMap<>();

    public void loadConfig(final WindmillCallback callback) {
        ConfigMethod platformMethod = ServiceGenerator.getInstance().createSerive(ConfigMethod.class);
        Call<ConfigListRes> call = platformMethod.getConfig(AuthManager.getGuestToken(), "user.terms");
        call.enqueue(new Callback<ConfigListRes>() {
            @Override
            public void onResponse(Call<ConfigListRes> call, Response<ConfigListRes> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    initMap(response.body());
                    callback.onSuccess(response.body());
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ConfigListRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void initMap(ConfigListRes res) {
        int size = res.getData().size();
        for (int i = 0; i < size; i++) {
            configMap.put(KEY_AGREE_VIE, res.getData().get(i).getValue(KEY_AGREE_VIE));
            configMap.put(KEY_AGREE_ENG, res.getData().get(i).getValue(KEY_AGREE_ENG));
        }
    }

    public String getAgree() {
        if (WindmillConfiguration.LANGUAGE.equals("eng")) {
            return configMap.get(KEY_AGREE_ENG);
        } else {
            return configMap.get(KEY_AGREE_VIE);
        }
    }

}
