package com.alticast.viettelottcommons.loader;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.UserGradeDataProcessManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.AvailableMethod;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.Purchase;
import com.alticast.viettelottcommons.resource.response.AdditionalRes;
import com.alticast.viettelottcommons.resource.response.LookUpProduct;
import com.alticast.viettelottcommons.resource.response.PaidAmountRes;
import com.alticast.viettelottcommons.resource.response.ProgramList;
import com.alticast.viettelottcommons.resource.response.PurchaseListRes;
import com.alticast.viettelottcommons.resource.response.WalletRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.acms.purchase.CheckMethod;
import com.alticast.viettelottcommons.serviceMethod.acms.purchase.PurchaseMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mc.kim on 8/8/2016.
 */
public class PurchaseCheckLoader {
    private static PurchaseCheckLoader ourInstance = new PurchaseCheckLoader();
    public static final int limit = 24;

    public static PurchaseCheckLoader getInstance() {
        return ourInstance;
    }

    private PurchaseCheckLoader() {
    }

    private ArrayList<String> categories = new ArrayList<String>();


    public void getPurchaseList(final int offset, final boolean single, final WindmillCallback callback) {
        CheckMethod checkMethod = ServiceGenerator.getInstance().createSerive(CheckMethod.class);
        categories.clear();
        categories.add("vod");
        categories.add("channel");
        categories.add("ott");
        categories.add("catchup");
        categories.add("npvr");
        categories.add("full");
        String type = single ? "single" : "subscription,package,fpackage";

        Call<PurchaseListRes> call =
                checkMethod.getPurchaseList(AuthManager.getAccessToken(), offset, limit, categories,
                        type, false, UserGradeDataProcessManager.getInstacne().getInclude());


        call.enqueue(new Callback<PurchaseListRes>() {
            @Override
            public void onResponse(Call<PurchaseListRes> call, final Response<PurchaseListRes> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    response.body().setOffset(offset);
                    if (single) {

                        PurchaseListRes purchaseListRes = response.body();
                        ArrayList<Purchase> purchases = purchaseListRes.getData();

                        if (purchases != null && purchases.size() > 0) {
                            ArrayList<String> ids = new ArrayList<String>();
                            for (int i = 0; i < purchases.size(); i++) {
                                ids.add(purchases.get(i).getId());
                            }

                            ProgramLoader.getInstance().getPrograms(ids, new WindmillCallback() {
                                @Override
                                public void onSuccess(Object obj) {
                                    response.body().setProgramList((ProgramList) obj);
                                    callback.onSuccess(response.body());
                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    callback.onFailure(call, t);
                                }

                                @Override
                                public void onError(ApiError error) {
                                    callback.onError(error);
                                }
                            });

                        } else {
                            callback.onSuccess(response.body());
                        }


                    } else {

                        callback.onSuccess(response.body());
                    }
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<PurchaseListRes> call, Throwable t) {
                CheckMethod checkMethod = ServiceGenerator.getInstance().createSerive(CheckMethod.class);
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });

    }

    public void getFullpackages(final WindmillCallback callback) {
        CheckMethod checkMethod = ServiceGenerator.getInstance().createSerive(CheckMethod.class);

        Call<WalletRes> call =
                checkMethod.getFullpackages(AuthManager.getAccessToken(), 0, limit,"long", UserGradeDataProcessManager.getInstacne().getInclude());


        call.enqueue(new Callback<WalletRes>() {
            @Override
            public void onResponse(Call<WalletRes> call, final Response<WalletRes> response) {
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
            public void onFailure(Call<WalletRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });

    }
    public void getPackages(final WindmillCallback callback) {
        CheckMethod checkMethod = ServiceGenerator.getInstance().createSerive(CheckMethod.class);

        Call<AdditionalRes> call =
                checkMethod.getPackages(AuthManager.getAccessToken(), 0, limit,"long", UserGradeDataProcessManager.getInstacne().getInclude());


        call.enqueue(new Callback<AdditionalRes>() {
            @Override
            public void onResponse(Call<AdditionalRes> call, final Response<AdditionalRes> response) {
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
            public void onFailure(Call<AdditionalRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });

    }


    public void hidePurchaseItem(String id, final WindmillCallback callback) {
        CheckMethod checkMethod = ServiceGenerator.getInstance().createSerive(CheckMethod.class);
        Call<Void> call =
                checkMethod.purchaseHide(AuthManager.getAccessToken(), id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                CheckMethod checkMethod = ServiceGenerator.getInstance().createSerive(CheckMethod.class);
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
            public void onFailure(Call<Void> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }


    public void cancelPurchase(String id, final WindmillCallback callback) {
        CheckMethod checkMethod = ServiceGenerator.getInstance().createSerive(CheckMethod.class);
        Call<Void> call =
                checkMethod.purchaseCancel(AuthManager.getAccessToken(), id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
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
            public void onFailure(Call<Void> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void checkPaidAmount(int month, final WindmillCallback callback) {
        CheckMethod checkMethod = ServiceGenerator.getInstance().createSerive(CheckMethod.class);
        Call<PaidAmountRes> call =
                checkMethod.checkPaidAmount(AuthManager.getAccessToken(), month);
        call.enqueue(new Callback<PaidAmountRes>() {
            @Override
            public void onResponse(Call<PaidAmountRes> call, Response<PaidAmountRes> response) {
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
            public void onFailure(Call<PaidAmountRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }


    public void checkPaymentsAvailable(Product product, String productCategory, final WindmillCallback callback) {
        CheckMethod checkMethod = ServiceGenerator.getInstance().createSerive(CheckMethod.class);
        String productId = product.getId();
        String pType = product.getType();
        if(productCategory == null || productCategory.isEmpty()) {
            productCategory = "vod";
        }
        Call<AvailableMethod> call = checkMethod.checkAvailableMethods(AuthManager.getAccessToken(), productId, productCategory, pType);

        call.enqueue(new Callback<AvailableMethod>() {
            @Override
            public void onResponse(Call<AvailableMethod> call, Response<AvailableMethod> response) {
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
            public void onFailure(Call<AvailableMethod> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }


    public void lookUpCatchUp(final WindmillCallback callback) {
        CheckMethod checkMethod = ServiceGenerator.getInstance().createSerive(CheckMethod.class);

        Call<LookUpProduct> call = checkMethod.lookupProductCatchUp(AuthManager.getAccessToken(), WindmillConfiguration.MULTI_LANGUAGE);

        call.enqueue(new Callback<LookUpProduct>() {
            @Override
            public void onResponse(Call<LookUpProduct> call, Response<LookUpProduct> response) {
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
            public void onFailure(Call<LookUpProduct> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void lookUpBasic(final WindmillCallback callback) {
        CheckMethod checkMethod = ServiceGenerator.getInstance().createSerive(CheckMethod.class);

        Call<LookUpProduct> call = checkMethod.lookupProductBasic(AuthManager.getAccessToken(), WindmillConfiguration.MULTI_LANGUAGE);

        call.enqueue(new Callback<LookUpProduct>() {
            @Override
            public void onResponse(Call<LookUpProduct> call, Response<LookUpProduct> response) {
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
            public void onFailure(Call<LookUpProduct> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void lookUpNPVR(final WindmillCallback callback) {
        CheckMethod checkMethod = ServiceGenerator.getInstance().createSerive(CheckMethod.class);

        Call<LookUpProduct> call = checkMethod.lookupProductNPVR(AuthManager.getAccessToken(), WindmillConfiguration.MULTI_LANGUAGE);

        call.enqueue(new Callback<LookUpProduct>() {
            @Override
            public void onResponse(Call<LookUpProduct> call, Response<LookUpProduct> response) {
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
            public void onFailure(Call<LookUpProduct> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }
}
