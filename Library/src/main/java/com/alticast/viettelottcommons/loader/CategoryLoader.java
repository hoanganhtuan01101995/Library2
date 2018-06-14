package com.alticast.viettelottcommons.loader;

import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.response.CategoryInfo;
import com.alticast.viettelottcommons.resource.response.CategoryListRes;
import com.alticast.viettelottcommons.resource.response.TrendingRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.acms.category.MenuMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mc.kim on 8/9/2016.
 */
public class CategoryLoader {
    private final int CATEGORY_PREVIEW_LIMIT = 20;
    private final int CATEGORY_LOAD_LIMIT = 20;
    private static CategoryLoader ourInstance = new CategoryLoader();

    public static CategoryLoader getInstance() {
        return ourInstance;
    }

    private CategoryLoader() {
    }


    public void getCategoryDetailInformation(String categoryId, final WindmillCallback callback) {
        MenuMethod menuMethod = ServiceGenerator.getInstance().createSerive(MenuMethod.class);
        String url = "/api1/contents/categories/";
        Call<CategoryInfo> call = menuMethod.getCategoryDetailInfo(url + categoryId, AuthManager.getAccessToken());
        call.enqueue(new Callback<CategoryInfo>() {
            @Override
            public void onResponse(Call<CategoryInfo> call, Response<CategoryInfo> response) {
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
            public void onFailure(Call<CategoryInfo> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);

            }
        });
    }

    public void getCategory(String categoryId, int offset, final WindmillCallback callback) {
        MenuMethod menuMethod = ServiceGenerator.getInstance().createSerive(MenuMethod.class);
        String url = "/api1/contents/categories/" + categoryId + "/categories";
        Call<CategoryListRes> call = menuMethod.getCategories(url, AuthManager.getAccessToken(), offset, CATEGORY_LOAD_LIMIT);
        call.enqueue(new Callback<CategoryListRes>() {
            @Override
            public void onResponse(Call<CategoryListRes> call, Response<CategoryListRes> response) {
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
            public void onFailure(Call<CategoryListRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);

            }
        });
    }

    public void getCategoryPreView(String categoryId, final WindmillCallback callback) {
        MenuMethod menuMethod = ServiceGenerator.getInstance().createSerive(MenuMethod.class);
        String url = "/api1/contents/categories/" + categoryId + "/categories";
        Call<CategoryListRes> call = menuMethod.getCategories(url, AuthManager.getAccessToken(), 0, CATEGORY_PREVIEW_LIMIT);
        call.enqueue(new Callback<CategoryListRes>() {
            @Override
            public void onResponse(Call<CategoryListRes> call, Response<CategoryListRes> response) {
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
            public void onFailure(Call<CategoryListRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);

            }
        });
    }


    public void getTrendingNow(final WindmillCallback callback) {
        MenuMethod menuMethod = ServiceGenerator.getInstance().createSerive(MenuMethod.class);
        String url = "so-web-app/so/recommend?\n" +
                "frmt=json&dp=VT_Phone_049_TrendingNow_CAT_VOD&pc=1\n" +
                "&cust=1000375346\n" +
                "&category_path_id=/GÓI%20CHỌN%20LỌC/D-Online";
        Call<TrendingRes> call = menuMethod.getTrendingNow(url);
        call.enqueue(new Callback<TrendingRes>() {
            @Override
            public void onResponse(Call<TrendingRes> call, Response<TrendingRes> response) {
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
            public void onFailure(Call<TrendingRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);

            }
        });
    }

    public void getPickForU(final WindmillCallback callback) {
        MenuMethod menuMethod = ServiceGenerator.getInstance().createSerive(MenuMethod.class);

        String url = "so-web-app/so/recommend?frmt=json&dp=VT_STB_042_HOME_BANNER_VOD&pc=1&cust=1000375346";
        Call<TrendingRes> call = menuMethod.getTrendingNow(url);
        call.enqueue(new Callback<TrendingRes>() {
            @Override
            public void onResponse(Call<TrendingRes> call, Response<TrendingRes> response) {
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
            public void onFailure(Call<TrendingRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);

            }
        });
    }
}
