package com.alticast.viettelottcommons.loader;

import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.UserGradeDataProcessManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.response.ChannelRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.acms.category.MenuMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mc.kim on 8/22/2016.
 */
public class MenuLoader {
    private final int LIMIT = 12;
    private static MenuLoader ourInstance = new MenuLoader();

    public static MenuLoader getInstance() {
        return ourInstance;
    }

    private MenuLoader() {
    }


    public void getMenuChannels(String menuId, int offset, final WindmillCallback callback) {
        MenuMethod menuMethod = ServiceGenerator.getInstance().createSerive(MenuMethod.class);
        String url = "/api1/contents/menus/" + menuId + "/channels";
        Call<ChannelRes> call = menuMethod.getMenuChannels(url, AuthManager.getAccessToken(), LIMIT, offset, UserGradeDataProcessManager.getInstacne().getInclude(), "long");

        call.enqueue(new Callback<ChannelRes>() {
            @Override
            public void onResponse(Call<ChannelRes> call, Response<ChannelRes> response) {
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
            public void onFailure(Call<ChannelRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });

    }
}
