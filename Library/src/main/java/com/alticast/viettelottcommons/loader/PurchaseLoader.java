package com.alticast.viettelottcommons.loader;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.manager.TimeManager;
import com.alticast.viettelottcommons.def.entry.EntryPathLogImpl;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.ChannelProduct;
import com.alticast.viettelottcommons.resource.Price;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.Program;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.resource.request.AutomaticRenewalBody;
import com.alticast.viettelottcommons.resource.request.CheckCouponReq;
import com.alticast.viettelottcommons.resource.request.UpsellBody;
import com.alticast.viettelottcommons.resource.response.CheckCouponRes;
import com.alticast.viettelottcommons.resource.response.PaymentResultRes;
import com.alticast.viettelottcommons.resource.response.PurchaseResultRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.acms.purchase.PurchaseMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;
import com.alticast.viettelottcommons.util.Util;

import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mc.kim on 8/8/2016.
 */
public class PurchaseLoader {

    public static enum TypeCurrency

    {
        price, point, wallet, phone
    }

    public static HashMap<TypeCurrency, String> currencyMap = new HashMap();

    static {
        currencyMap.put(TypeCurrency.price, "VND");
        currencyMap.put(TypeCurrency.point, "PNT");
        currencyMap.put(TypeCurrency.wallet, "VND");
        currencyMap.put(TypeCurrency.phone, "VND");
    }


    private static PurchaseLoader ourInstance = new PurchaseLoader();

    public static PurchaseLoader getInstance() {
        return ourInstance;
    }

    private PurchaseLoader() {

    }

    public void purchaseCreatePrice(Object program, Product product, String productCategory, final WindmillCallback callback) {
        PurchaseMethod purchaseMethod = ServiceGenerator.getInstance().createSerive(PurchaseMethod.class);
        String productId = product.getId();
        String productName = program != null ? "" : product.getName();
        String content_id = program != null ? "" : null;
        if(program != null) {
            if (program instanceof Program) {
                Program program1 = (Program) program;
                content_id = ((Program) program).getId();
                productName = product.isSingleProduct() ? program1.getTitle(WindmillConfiguration.LANGUAGE) : product.getName();
            } else if (program instanceof ChannelProduct) {
                content_id = ((ChannelProduct) program).getId();
                productName = product.getName();
            }
        }
        String productType = product.getType();
        String promotionId = null;
        long ts = TimeManager.getInstance().getServerCurrentTimeMillis();
        String nonce = Util.getRandomHexString(6);

        String hash = Util.getScretKey(WindmillConfiguration.clientId + productId + ts + nonce, HandheldAuthorization.getInstance().getTokenSecret());

        if (product.getPromotion() != null) {
            promotionId = product.getPromotion().getId();
        }


        Call<PurchaseResultRes> call = null;
        if (promotionId == null) {
            call = purchaseMethod.purchaseCreate(AuthManager.getAccessToken(), productId, productName, content_id, productCategory, productType
                    , EntryPathLogImpl.getInstance().getPurchaseMenuString(), WindmillConfiguration.clientId, ts, nonce, hash);

        } else {
            call = purchaseMethod.purchaseCreate(AuthManager.getAccessToken(), productId, productName, content_id, productCategory, productType, promotionId
                    , EntryPathLogImpl.getInstance().getPurchaseMenuString(), WindmillConfiguration.clientId, ts, nonce, hash);
        }


        call.enqueue(new Callback<PurchaseResultRes>() {
            @Override
            public void onResponse(Call<PurchaseResultRes> call, Response<PurchaseResultRes> response) {
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
            public void onFailure(Call<PurchaseResultRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });

    }

    public void purchaseCreateRentalPeriod(String programId, Product product, String productCategory, RentalPeriods rentalPeriods, boolean autoRenewal, final WindmillCallback callback) {
        PurchaseMethod purchaseMethod = ServiceGenerator.getInstance().createSerive(PurchaseMethod.class);
        String productId = product.getId();
        String productName = product.getName();
        String content_id = programId;
//        String productCategory = product.getCategory();
        String productType = product.getType();
        String promotionId = null;
        long ts = TimeManager.getInstance().getServerCurrentTimeMillis();
        String nonce = Util.getRandomHexString(6);

        String hash = Util.getScretKey(WindmillConfiguration.clientId + productId + ts + nonce, HandheldAuthorization.getInstance().getTokenSecret());

        if (product.getPromotion() != null) {
            promotionId = product.getPromotion().getId();
        }

        int rental_period = rentalPeriods.getPeriod();
        String unit = rentalPeriods.getUnit();

        String autoRenewString = autoRenewal ? "T" : "F";

        Call<PurchaseResultRes> call = null;
        if (promotionId == null) {
            call = purchaseMethod.purchaseCreate(AuthManager.getAccessToken(), productId, productName, content_id, productCategory, productType
                    , EntryPathLogImpl.getInstance().getPurchaseMenuString(), WindmillConfiguration.clientId, ts, nonce, rental_period, unit, autoRenewString, hash);

        } else {
            call = purchaseMethod.purchaseCreate(AuthManager.getAccessToken(), productId, productName, content_id, productCategory, productType, promotionId
                    , EntryPathLogImpl.getInstance().getPurchaseMenuString(), WindmillConfiguration.clientId, ts, nonce, rental_period, unit, autoRenewString, hash);
        }


        call.enqueue(new Callback<PurchaseResultRes>() {
            @Override
            public void onResponse(Call<PurchaseResultRes> call, Response<PurchaseResultRes> response) {
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
            public void onFailure(Call<PurchaseResultRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }


    public void paymentsCreate(TypeCurrency type,Object program, Product product, float priceValue, String purchaseId, String productCategory, final WindmillCallback callback) {
        PurchaseMethod purchaseMethod = ServiceGenerator.getInstance().createSerive(PurchaseMethod.class);
        String currency = currencyMap.get(type);
//        Price price = rentalPeriods.getPrice(currencyMap.get(type));
        String productId = product.getId();
//        String content_id = programId;
        String productType = product.getType();

        String productName = program != null ? "" : product.getName();
        if(program != null) {
            if (program instanceof Program) {
                Program program1 = (Program) program;
                productName = product.isSingleProduct() ? program1.getTitle(WindmillConfiguration.LANGUAGE) : product.getName();
            } else if (program instanceof ChannelProduct) {
                productName = product.getName();
            }
        }

        if(AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
            if(product.isSingleProduct()) {
                type = PurchaseLoader.TypeCurrency.phone;
            }
            if(HandheldAuthorization.getInstance().getInt(HandheldAuthorization.CHARGE_ACCOUNT) == AuthManager.ChargeAccount.CHARGE_ACCOUNT_VWALLET) {
                type = TypeCurrency.wallet;
            }
        } else {
            int paymentMethod = product.getPaymentOptionChose();

            if (paymentMethod == Product.PREPAID_ONLY) {
                type = TypeCurrency.wallet;
            }
        }

        String saleCode = null;
//        String saleCode = AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3 ? "" : HandheldAuthorization.getInstance().getString(HandheldAuthorization.SALE_CODE);
        String coupon_tx_id = product.getCouponCode();

        Call<PaymentResultRes> call = null;

        switch (type) {
            case phone:

                call = purchaseMethod.paymentsCreatePhone(AuthManager.getAccessToken(), saleCode, coupon_tx_id, currency,
                        productId, productName,purchaseId, productCategory, productType, priceValue);

                break;
            case price:

//                call = purchaseMethod.paymentsCreateNomal(AuthManager.getAccessToken(), saleCode, coupon_tx_id, currency,
//                        productId, productName,purchaseId, productCategory, productType, priceValue * (1f - product.getDiscountRatio()));
                call = purchaseMethod.paymentsCreateNomal(AuthManager.getAccessToken(), saleCode, coupon_tx_id, currency,
                        productId, productName,purchaseId, productCategory, productType, priceValue);
                break;
            case point:

//                call = purchaseMethod.paymentsCreatePoint(AuthManager.getAccessToken(), saleCode, coupon_tx_id, currency,
//                        productId, productName,purchaseId, productCategory, productType, priceValue * (1f - product.getDiscountRatio()));
                call = purchaseMethod.paymentsCreatePoint(AuthManager.getAccessToken(), saleCode, coupon_tx_id, currency,
                        productId, productName,purchaseId, productCategory, productType, priceValue);
                break;
            case wallet:

//                call = purchaseMethod.paymentsCreateWallet(AuthManager.getAccessToken(), saleCode, coupon_tx_id, currency,
//                        productId, productName,purchaseId, productCategory, productType, priceValue * (1f - product.getDiscountRatio()));
                call = purchaseMethod.paymentsCreateWallet(AuthManager.getAccessToken(), saleCode, coupon_tx_id, currency,
                        productId, productName,purchaseId, productCategory, productType, priceValue);
                break;
        }


        call.enqueue(new Callback<PaymentResultRes>() {
            @Override
            public void onResponse(Call<PaymentResultRes> call, Response<PaymentResultRes> response) {
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
            public void onFailure(Call<PaymentResultRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });


    }

    public void purchaseSubscribeBasic(String id,final WindmillCallback callback) {
        PurchaseMethod purchaseMethod = ServiceGenerator.getInstance().createSerive(PurchaseMethod.class);
        Call<Void> call = purchaseMethod.purchaseSubscribeBasic(AuthManager.getAccessToken(), id);

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
    public void purchaseUpsell(String productId, int rentalPeriod, String unit, String sale_code, String coupon_tx_id, final WindmillCallback callback) {
        PurchaseMethod purchaseMethod = ServiceGenerator.getInstance().createSerive(PurchaseMethod.class);
        UpsellBody upsellBody = new UpsellBody();
        upsellBody.setProduct_id(productId);
        upsellBody.setRental_period(rentalPeriod);
        upsellBody.setUnit(unit);
        upsellBody.setSale_code(sale_code);
        upsellBody.setCoupon_tx_id(coupon_tx_id);

        Call<Void> call = purchaseMethod.upsellingFull(AuthManager.getAccessToken(), productId, rentalPeriod, unit);

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

    public void automaticRenewal(String purchaseId, String state,final WindmillCallback callback) {
        PurchaseMethod purchaseMethod = ServiceGenerator.getInstance().createSerive(PurchaseMethod.class);

        AutomaticRenewalBody automaticRenewalBody = new AutomaticRenewalBody();
        automaticRenewalBody.setId(purchaseId);
        automaticRenewalBody.setAuto_renewal(state);


        Call<Void> call = purchaseMethod.automaticRenewal(AuthManager.getAccessToken(), automaticRenewalBody);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (callback == null) {
                    return;
                }
//                callback.onSuccess(response.body());
                if (response.isSuccess()) {
                    callback.onSuccess(response.body());
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    if(error == null || error.getError() == null) {
                        callback.onSuccess(response.body());
                    } else {
                        callback.onError(error);
                    }

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


    public void checkCoupon(String productId, String coupon,final WindmillCallback callback) {
        PurchaseMethod purchaseMethod = ServiceGenerator.getInstance().createSerive(PurchaseMethod.class);

//        CheckCouponReq checkCouponReq = new CheckCouponReq();
//        checkCouponReq.setProduct_id(productId);
//        checkCouponReq.setCoupon_tx_id(coupon);


        Call<CheckCouponRes> call = purchaseMethod.checkCoupon(AuthManager.getAccessToken(), productId, coupon);

        call.enqueue(new Callback<CheckCouponRes>() {
            @Override
            public void onResponse(Call<CheckCouponRes> call, Response<CheckCouponRes> response) {
                if (callback == null) {
                    return;
                }
//                callback.onSuccess(response.body());
                if (response.isSuccess()) {
                    callback.onSuccess(response.body());
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    if(error == null || error.getError() == null) {
                        callback.onSuccess(response.body());
                    } else {
                        callback.onError(error);
                    }

                }
            }

            @Override
            public void onFailure(Call<CheckCouponRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

}
