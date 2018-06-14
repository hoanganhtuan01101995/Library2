package com.alticast.viettelottcommons.loader;

import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.ChargeAmountObj;
import com.alticast.viettelottcommons.resource.Wallet;
import com.alticast.viettelottcommons.resource.WalletTopupMethod;

import retrofit2.Call;

/**
 * Created by duyuno on 9/12/17.
 */
public class VWalletLoader {

    private static VWalletLoader instance = null;

    public synchronized static VWalletLoader getInstance() {
        if (instance == null) {
            instance = new VWalletLoader();
        }
        return instance;
    }

    public void getMyWalletBalance(final WindmillCallback callback) {
        PrePaymentLoader.getInstance().getMyWalletBalance(AuthManager.getAccessToken(), callback);
    }
    public void getMyWalletMethod(final WindmillCallback callback) {
        PrePaymentLoader.getInstance().getInquireTopupMethod(AuthManager.getAccessToken(), callback);
    }
    public void requestOtp(String phoneNumber, final WindmillCallback callback) {
        PrePaymentLoader.getInstance().requestOtp(AuthManager.getAccessToken(),phoneNumber, callback);
    }
    public void requestTopupByMobilePhone(String phoneNumber, String otp, ChargeAmountObj chargeAmountObj, WalletTopupMethod walletTopupMethod, final WindmillCallback callback) {
        PrePaymentLoader.getInstance().requestTopupByMobilePhone(AuthManager.getAccessToken(), phoneNumber, otp, (int)chargeAmountObj.getAmount(),
                chargeAmountObj.getBonusRate(walletTopupMethod), chargeAmountObj.getBonusAmount(walletTopupMethod),
                walletTopupMethod.getPromotionId(),
                callback);
    }

    public void requsetTopUpByScratchCard(String serial, String pin, String promotionId,final WindmillCallback callback) {
        PrePaymentLoader.getInstance().requsetTopUpByScratchCard(AuthManager.getAccessToken(), serial, pin, promotionId, callback);
    }
    public void getTopupHistory(long since, long unti, int offset, int limit, final WindmillCallback callback) {
        PrePaymentLoader.getInstance().getTopupHistory(AuthManager.getAccessToken(), since, unti, offset, limit, callback);
    }

    public void checkWalletBalance(final float price, final WindmillCallback windmillCallback) {
        PrePaymentLoader.getInstance().getMyWalletBalance(AuthManager.getAccessToken(), new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                Wallet wallet = (Wallet) obj;

                int total = wallet.getTopup() + wallet.getBonus();
                windmillCallback.onSuccess(total - price);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                windmillCallback.onFailure(call, t);
            }

            @Override
            public void onError(ApiError error) {
                windmillCallback.onError(error);
            }
        });
    }
}
