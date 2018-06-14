package com.alticast.viettelottcommons.loader;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.manager.UserGradeDataProcessManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.ChannelProduct;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.ProductCatchup;
import com.alticast.viettelottcommons.resource.response.ChannelRes;
import com.alticast.viettelottcommons.resource.response.ScheduleListRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.GetChannelIdsMethod;
import com.alticast.viettelottcommons.serviceMethod.acms.program.ProductMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mc.kim on 8/10/2016.
 */
public class ChannelLoader {
    private final String REGION_OTT = "OTT";
    private final String FORMAT_SHORT = "short";
    private final String FORMAT_LONG = "long";
    private final int LIMIT = 2000;
    private final int OFFSET = 0;

    private static ChannelLoader ourInstance = new ChannelLoader();

    public static ChannelLoader getInstance() {
        return ourInstance;
    }

    private ChannelLoader() {
    }

    public void getChannels(final WindmillCallback callback) {

        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);

        String region = REGION_OTT;
        if(WindmillConfiguration.isAlacaterVersion && AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
            region = HandheldAuthorization.getInstance().getCurrentUser().getSo_id();
        }

        Call<ChannelRes> call = productMethod.getChannels(AuthManager.getAccessToken(),
                OFFSET, region, LIMIT, UserGradeDataProcessManager.getInstacne().getIncludeProduct(), FORMAT_SHORT);


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

    public void getChannelIds(final WindmillCallback callback) {
        GetChannelIdsMethod getChannelIdsMethod = ServiceGenerator.getChannelIdsInstance().createGetChannelIds(GetChannelIdsMethod.class);
        Call<List<String>> call = getChannelIdsMethod.getChannelIds();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
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
            public void onFailure(Call<List<String>> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    // for get each Channel Product including Purchase, Product information

    public void getChannelProduct(boolean isLive, String channelId, final WindmillCallback callback) {

//        if(AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
//            getChannelProductForLive(channelId, callback);
//        } else {
//            getChannelProductForCatchUp(callback);
//        }
        if(isLive) {
            getChannelProductForLive(channelId, callback);
        } else {
            if(AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
                getChannelProductForLive(channelId, callback);
            } else {
                getChannelProductForCatchUp(callback);
            }
//            getChannelProductForCatchUp(callback);
        }
    }
    public void getChannelProductForLive(String channelId, final WindmillCallback callback) {

        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        String url = "/api1/contents/channels/" + channelId;
        String region = AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2 || !WindmillConfiguration.isAlacaterVersion ? "OTT" : HandheldAuthorization.getInstance().getCurrentUser().getSo_id();
        String limit = "1";
        Call<ChannelProduct> call = productMethod.getChannelProduct(url, AuthManager.getAccessToken(), region, limit,
                UserGradeDataProcessManager.getInstacne().getInclude(), FORMAT_LONG);

        call.enqueue(new Callback<ChannelProduct>() {
            @Override
            public void onResponse(Call<ChannelProduct> call, Response<ChannelProduct> response) {
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
            public void onFailure(Call<ChannelProduct> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void getChannelProductForCatchUp(final WindmillCallback callback) {

        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        String url = "/api1/purchases/lookup_product_catchup";
        Call<ProductCatchup> call = productMethod.getChannelProductPair(url, AuthManager.getAccessToken(),true);

        call.enqueue(new Callback<ProductCatchup>() {
            @Override
            public void onResponse(Call<ProductCatchup> call, Response<ProductCatchup> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {



                    ProductCatchup productCatchup = response.body();
                    ChannelProduct channelProduct = new ChannelProduct();
                    channelProduct.setId(productCatchup.getId());
                    for(Product product : productCatchup.getPurchasable_products()) {
                        product.setType(Product.TYPE_SUBSCRIPTION);
                    }
                    ArrayList<Product> listProduct = new ArrayList<Product>();
                    if(productCatchup.getProduct() != null) {
                        productCatchup.getProduct().setType(Product.TYPE_SUBSCRIPTION);

                        listProduct.add(productCatchup.getProduct());
                    } else {
                        listProduct.addAll(productCatchup.getPurchasable_products());
                    }
                    channelProduct.setProduct(listProduct);
                    callback.onSuccess(channelProduct);
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ProductCatchup> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void getCurrentScheduleList(int size, final WindmillCallback callback) {

        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        String now = "now";

        String region = REGION_OTT;
        if(WindmillConfiguration.isAlacaterVersion && AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
            region = HandheldAuthorization.getInstance().getCurrentUser().getSo_id();
        }

        Call<ScheduleListRes> call = productMethod.getCurrentScheduleList(AuthManager.getAccessToken(), region, now, now, size);

        call.enqueue(new Callback<ScheduleListRes>() {
            @Override
            public void onResponse(Call<ScheduleListRes> call, Response<ScheduleListRes> response) {
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
            public void onFailure(Call<ScheduleListRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void getCurrentScheduleList(ArrayList<String> ids, final WindmillCallback callback) {

        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        String now = "now";

        String region = REGION_OTT;
        if(WindmillConfiguration.isAlacaterVersion && AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
            region = HandheldAuthorization.getInstance().getCurrentUser().getSo_id();
        }

        Call<ScheduleListRes> call = productMethod.getCurrentScheduleList(AuthManager.getAccessToken(), ids, region, now, now);

        call.enqueue(new Callback<ScheduleListRes>() {
            @Override
            public void onResponse(Call<ScheduleListRes> call, Response<ScheduleListRes> response) {
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
            public void onFailure(Call<ScheduleListRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }


    public void getScheduleList(String id, long since, long until, int limit, final WindmillCallback callback) {

        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);

        String region = REGION_OTT;
        if(WindmillConfiguration.isAlacaterVersion && AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
            region = HandheldAuthorization.getInstance().getCurrentUser().getSo_id();
        }

        Call<ScheduleListRes> call = productMethod.getScheduleList(AuthManager.getAccessToken(), id, region, since, until, limit);

        call.enqueue(new Callback<ScheduleListRes>() {
            @Override
            public void onResponse(Call<ScheduleListRes> call, Response<ScheduleListRes> response) {
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
            public void onFailure(Call<ScheduleListRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }


}
