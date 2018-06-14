package com.alticast.viettelottcommons.loader;

import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.UserGradeDataProcessManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.response.PackageDetailInfoRes;
import com.alticast.viettelottcommons.resource.response.PackageInfoRes;
import com.alticast.viettelottcommons.resource.response.PackageListRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.acms.packageInfo.PackageMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mc.kim on 8/22/2016.
 */
public class PackageLoader {
    private static PackageLoader ourInstance = new PackageLoader();
    private final String FORMAT_LONG = "long";
    private final int LIMIT = 12;

    public static PackageLoader getInstance() {
        return ourInstance;
    }

    private PackageLoader() {
    }


    public void getPackageInfo(String productId, final WindmillCallback callback) {
        PackageMethod packageMethod = ServiceGenerator.getInstance().createSerive(PackageMethod.class);
        String url = "/api1/contents/packages/products/" + productId;

        Call<PackageInfoRes> call = packageMethod.getPackage(url, AuthManager.getAccessToken(),
                UserGradeDataProcessManager.getInstacne().getInclude(), FORMAT_LONG);
        call.enqueue(new Callback<PackageInfoRes>() {
            @Override
            public void onResponse(Call<PackageInfoRes> call, Response<PackageInfoRes> response) {
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
            public void onFailure(Call<PackageInfoRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }


    public void getPackageDetailInfo(String productId, final WindmillCallback callback) {
        PackageMethod packageMethod = ServiceGenerator.getInstance().createSerive(PackageMethod.class);
        String url = "/api1/contents/packages/" + productId;
        Call<PackageDetailInfoRes> call = packageMethod.getPackageDetail(url, AuthManager.getAccessToken(),
                UserGradeDataProcessManager.getInstacne().getInclude(), FORMAT_LONG);
        call.enqueue(new Callback<PackageDetailInfoRes>() {
            @Override
            public void onResponse(Call<PackageDetailInfoRes> call, Response<PackageDetailInfoRes> response) {
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
            public void onFailure(Call<PackageDetailInfoRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void getPackagePrograms(String productId, String since, String until, int offset, boolean pop, boolean series, final WindmillCallback callback) {
        PackageMethod packageMethod = ServiceGenerator.getInstance().createSerive(PackageMethod.class);
        String url = "/api1/contents/packages/products/" + productId + "/programs";
        Call<PackageListRes> call = packageMethod.getPackagePrograms(url, AuthManager.getAccessToken(), since, until, offset,
                LIMIT, UserGradeDataProcessManager.getInstacne().getInclude(), pop, FORMAT_LONG, series);
        call.enqueue(new Callback<PackageListRes>() {
            @Override
            public void onResponse(Call<PackageListRes> call, Response<PackageListRes> response) {
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
            public void onFailure(Call<PackageListRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

}
