package com.alticast.viettelottcommons.api;


import com.alticast.viettelottcommons.resource.ApiError;

import retrofit2.Call;

/**
 * Created by mc.kim on 3/11/2016.
 */
public interface WindmillCallback {
    public void onSuccess(Object obj);
    public void onFailure(Call call, Throwable t);
    public void onError(ApiError error);
}
