package com.alticast.viettelottcommons.util;


import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.service.ServiceGenerator;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by mc.kim on 3/11/2016.
 */
public class ErrorUtil {

    public static ApiError parseError(Response<?> response) {

        try {
            Converter<ResponseBody, ApiError> converter =
                    ServiceGenerator.getRetrofit()
                            .responseBodyConverter(ApiError.class, new Annotation[0]);

            ApiError error;
            error = converter.convert(response.errorBody());
            return error;
        } catch (IOException e) {
            return new ApiError("", "" + e);
        }



    }


    public static ApiError parseError(okhttp3.Response response) {




        try {
            Converter<ResponseBody, ApiError> converter =
                    ServiceGenerator.getRetrofit()
                            .responseBodyConverter(ApiError.class, new Annotation[0]);

            ApiError error;
            error = converter.convert(response.body());
            return error;
        } catch (IOException e) {
            return new ApiError("", "" + e);
        }


    }
}
