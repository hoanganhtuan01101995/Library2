package com.alticast.viettelottcommons.loader;


import android.os.AsyncTask;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.dialog.ActionListener;
import com.alticast.viettelottcommons.dialog.ListDeviceLoginDialog;
import com.alticast.viettelottcommons.dialog.PhoneLoginFragment;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.ChannelManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.manager.TimeManager;
import com.alticast.viettelottcommons.resource.AccessToken;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.AuthCodeRes;
import com.alticast.viettelottcommons.resource.Login;
import com.alticast.viettelottcommons.resource.MyDeviceAccount;
import com.alticast.viettelottcommons.resource.PairingRes;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.RegistedDevice;
import com.alticast.viettelottcommons.resource.ResultRes;
import com.alticast.viettelottcommons.resource.request.AutomaticDetectionReq;
import com.alticast.viettelottcommons.resource.request.ChangePasswordReq;
import com.alticast.viettelottcommons.resource.request.CheckAuthenticationCodeReq;
import com.alticast.viettelottcommons.resource.request.CheckIdReq;
import com.alticast.viettelottcommons.resource.request.CheckVmSubscriberReq;
import com.alticast.viettelottcommons.resource.request.CreateAccountReq;
import com.alticast.viettelottcommons.resource.request.LoginReq;
import com.alticast.viettelottcommons.resource.request.RequestChangeMethod;
import com.alticast.viettelottcommons.resource.request.RequestPairingReq;
import com.alticast.viettelottcommons.resource.request.ResetPasswordReq;
import com.alticast.viettelottcommons.resource.request.VerifyCodeReq;
import com.alticast.viettelottcommons.resource.response.MeShowRes;
import com.alticast.viettelottcommons.resource.response.PurchaseListRes;
import com.alticast.viettelottcommons.resource.response.VerifyCodeRes;
import com.alticast.viettelottcommons.resource.response.WalletRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.upms.FrontEndMethod;
import com.alticast.viettelottcommons.util.AsSHA1;
import com.alticast.viettelottcommons.util.ErrorUtil;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.ProductUtils;
import com.alticast.viettelottcommons.util.Util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mc.kim on 7/28/2016.
 */
public class FrontEndLoader {
    private final String INCLUDE_ACCOUNT = "device,pairing";
    private static FrontEndLoader ourInstance = new FrontEndLoader();
    public static final String TAG = FrontEndLoader.class.getSimpleName();

    public static FrontEndLoader getInstance() {
        return ourInstance;
    }

    private FrontEndLoader() {
    }


    public void getMyAccount(final WindmillCallback callback, final boolean forTokenReissue, final GlobalActivity globalActivity, final boolean isLoginRequest) {
        globalActivity.showProgress();
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
        Call<MyDeviceAccount> call = frontEndMethod.getMyAccount(AuthManager.getLoginToken(), INCLUDE_ACCOUNT);
//        Call<MyDeviceAccount> call = frontEndMethod.getMyAccount(AuthManager.getAccessToken(), INCLUDE_ACCOUNT);
        call.enqueue(new Callback<MyDeviceAccount>() {
            @Override
            public void onResponse(Call<MyDeviceAccount> call, final Response<MyDeviceAccount> response) {
                globalActivity.hideProgress();
                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {
                    final MyDeviceAccount myDeviceAccount = response.body();
                    if (myDeviceAccount != null && myDeviceAccount.getConfig() != null && myDeviceAccount.getConfig().isViettelMobileBalanceUser() && !myDeviceAccount.getPaymentOption().equals("MB")) {
                        FrontEndLoader.getInstance().changePaymentOption("MB", new WindmillCallback() {
                            @Override
                            public void onSuccess(Object obj) {
                                HandheldAuthorization.getInstance().putInt(HandheldAuthorization.CHARGE_ACCOUNT, 0);
                                HandheldAuthorization.getInstance().getCurrentUser().setPaymentOption("MB");
                                HandheldAuthorization.getInstance().setPaymentMethod(0);
//                                callback.onSuccess(myDeviceAccount);
                                processMydeviceAccount(myDeviceAccount, forTokenReissue, isLoginRequest, callback, globalActivity);
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
//                                callback.onSuccess(myDeviceAccount);
                                processMydeviceAccount(myDeviceAccount, forTokenReissue, isLoginRequest, callback, globalActivity);
                            }

                            @Override
                            public void onError(ApiError error) {
//                                callback.onSuccess(myDeviceAccount);
                                processMydeviceAccount(myDeviceAccount, forTokenReissue, isLoginRequest, callback, globalActivity);
                            }
                        });
                    } else {
                        HandheldAuthorization.getInstance().putInt(HandheldAuthorization.CHARGE_ACCOUNT, 0);
                        HandheldAuthorization.getInstance().getCurrentUser().setPaymentOption("MB");
                        HandheldAuthorization.getInstance().setPaymentMethod(0);
                        processMydeviceAccount(myDeviceAccount, forTokenReissue, isLoginRequest, callback, globalActivity);
                    }
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<MyDeviceAccount> call, Throwable t) {

                globalActivity.hideProgress();

                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void checkMyAccount(final WindmillCallback callback, final GlobalActivity globalActivity) {
        globalActivity.showProgress();
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
        Call<MyDeviceAccount> call = frontEndMethod.getMyAccount(AuthManager.getLoginToken(), INCLUDE_ACCOUNT);
//        Call<MyDeviceAccount> call = frontEndMethod.getMyAccount(AuthManager.getAccessToken(), INCLUDE_ACCOUNT);
        call.enqueue(new Callback<MyDeviceAccount>() {
            @Override
            public void onResponse(Call<MyDeviceAccount> call, final Response<MyDeviceAccount> response) {
                globalActivity.hideProgress();
                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {
                    final MyDeviceAccount myDeviceAccount = response.body();
                    if (myDeviceAccount != null) {
                        HandheldAuthorization.getInstance().setDeviceAccount(myDeviceAccount);
                        HandheldAuthorization.getInstance().setIsPoorUser(!myDeviceAccount.getConfig().isViettelMobileBalanceUser());
                        if (myDeviceAccount.getPairing() == null) {
                            callback.onSuccess(null);
                            HandheldAuthorization.getInstance().setPairingAccessToken(null);
                        } else {
                            getPairingToken(myDeviceAccount, callback, globalActivity);
                        }
                    }
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<MyDeviceAccount> call, Throwable t) {

                globalActivity.hideProgress();

                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void processMydeviceAccount(final MyDeviceAccount myDeviceAccount, final boolean forTokenReissue, final boolean isLoginRequest, final WindmillCallback callback, final GlobalActivity globalActivity) {
        if (myDeviceAccount != null) {
            HandheldAuthorization.getInstance().setDeviceAccount(myDeviceAccount);
            if (isLimitedDeviceThroughStatus(myDeviceAccount.getStatus())) {
                if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
//                    if(isLoginRequest) {
//                        callback.onSuccess(myDeviceAccount);
//                    } else {
//                        globalActivity.showSwitchConfirmDialog(myDeviceAccount, ((MyDeviceAccount) myDeviceAccount).getRegistered_device().getTotal(), null);
//                    }
                    globalActivity.showSwitchConfirmDialog(myDeviceAccount, ((MyDeviceAccount) myDeviceAccount).getRegistered_device().getTotal(), new ActionListener() {
                        @Override
                        public void onConfirm() {
                            callback.onSuccess(myDeviceAccount);
                        }

                        @Override
                        public void onCancel() {
                            callback.onError(null);

                        }
                    });

                    Logger.d("duyuno", "isLimitedDeviceThroughStatus  " + myDeviceAccount.getStatus());
                } else {
                    checkMyAccount(myDeviceAccount, forTokenReissue, callback, globalActivity);
                }

            } else {

                if (!WindmillConfiguration.isBigSmallPackageVersion) {
                    if (myDeviceAccount != null) {
                        HandheldAuthorization.getInstance().putString(HandheldAuthorization.ID, myDeviceAccount.getId());
                        HandheldAuthorization.getInstance().getCurrentUser().setId(myDeviceAccount.getId());
                        Logger.d("duyuno", "checkMyAccount when not limit ");
                        checkMyAccount(myDeviceAccount, forTokenReissue, callback, globalActivity);
                    } else {
                        callback.onError(null);
                    }
                    return;
                }
                PurchaseCheckLoader.getInstance().getPurchaseList(0, false, new WindmillCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        if (obj == null) {
                            if (myDeviceAccount != null) {
                                HandheldAuthorization.getInstance().putString(HandheldAuthorization.ID, myDeviceAccount.getId());
                                HandheldAuthorization.getInstance().getCurrentUser().setId(myDeviceAccount.getId());
                                Logger.d("duyuno", "checkMyAccount when not limit ");
                                checkMyAccount(myDeviceAccount, forTokenReissue, callback, globalActivity);
                            } else {
                                callback.onError(null);
                            }
                            return;
                        }
                        final PurchaseListRes purchaseListRes = (PurchaseListRes) obj;


                        PurchaseCheckLoader.getInstance().getFullpackages(new WindmillCallback() {
                            @Override
                            public void onSuccess(Object obj) {
                                WalletRes walletRes = (WalletRes) obj;

                                final int maxScreen = purchaseListRes.getScreenMax(walletRes.getData());

                                if (maxScreen > 0 && maxScreen < myDeviceAccount.getRegistered_device().getRegistered()) {
                                    ArrayList<Product> listProduct = ProductUtils.filterAllHandheldPurchaseableProduct(walletRes.getData(), purchaseListRes);
                                    if (listProduct == null) {
                                        showUpdatePackage(null, myDeviceAccount, maxScreen, callback, globalActivity);
                                    } else {
                                        ArrayList<Product> listUpdate = ProductUtils.filterUpSellProduct(listProduct, maxScreen);
                                        showUpdatePackage(listUpdate, myDeviceAccount, maxScreen, callback, globalActivity);
                                    }
                                } else {
                                    if (myDeviceAccount != null) {
                                        HandheldAuthorization.getInstance().putString(HandheldAuthorization.ID, myDeviceAccount.getId());
                                        HandheldAuthorization.getInstance().getCurrentUser().setId(myDeviceAccount.getId());
                                        Logger.d("duyuno", "checkMyAccount when not limit ");
                                        checkMyAccount(myDeviceAccount, forTokenReissue, callback, globalActivity);
                                    } else {
                                        callback.onError(null);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                if (myDeviceAccount != null) {
                                    HandheldAuthorization.getInstance().putString(HandheldAuthorization.ID, myDeviceAccount.getId());

                                    if(HandheldAuthorization.getInstance().isLogIn()) {
                                        HandheldAuthorization.getInstance().getCurrentUser().setId(myDeviceAccount.getId());
                                    }
                                    Logger.d("duyuno", "checkMyAccount when not limit ");
                                    checkMyAccount(myDeviceAccount, forTokenReissue, callback, globalActivity);
                                } else {
                                    callback.onError(null);
                                }
                            }

                            @Override
                            public void onError(ApiError error) {
                                if (myDeviceAccount != null) {
                                    HandheldAuthorization.getInstance().putString(HandheldAuthorization.ID, myDeviceAccount.getId());

                                    if(HandheldAuthorization.getInstance().isLogIn()) {
                                        HandheldAuthorization.getInstance().getCurrentUser().setId(myDeviceAccount.getId());
                                    }
                                    Logger.d("duyuno", "checkMyAccount when not limit ");
                                    checkMyAccount(myDeviceAccount, forTokenReissue, callback, globalActivity);
                                } else {
                                    callback.onError(null);
                                }
                            }
                        });



                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        if (myDeviceAccount != null) {
                            HandheldAuthorization.getInstance().putString(HandheldAuthorization.ID, myDeviceAccount.getId());

                            if(HandheldAuthorization.getInstance().isLogIn()) {
                                HandheldAuthorization.getInstance().getCurrentUser().setId(myDeviceAccount.getId());
                            }
                            Logger.d("duyuno", "checkMyAccount when not limit ");
                            checkMyAccount(myDeviceAccount, forTokenReissue, callback, globalActivity);
                        } else {
                            callback.onError(null);
                        }
                    }

                    @Override
                    public void onError(ApiError error) {
                        if (myDeviceAccount != null) {
                            HandheldAuthorization.getInstance().putString(HandheldAuthorization.ID, myDeviceAccount.getId());

                            if(HandheldAuthorization.getInstance().isLogIn()) {
                                HandheldAuthorization.getInstance().getCurrentUser().setId(myDeviceAccount.getId());
                            }
                            Logger.d("duyuno", "checkMyAccount when not limit ");
                            checkMyAccount(myDeviceAccount, forTokenReissue, callback, globalActivity);
                        } else {
                            callback.onError(null);
                        }

                    }
                });

            }

        }
    }

    private void showUpdatePackage(ArrayList<Product> listProduct, final MyDeviceAccount myDeviceAccount, int maxScreen, final WindmillCallback callback, GlobalActivity globalActivity) {
        globalActivity.showUpdatePackage(listProduct, myDeviceAccount, maxScreen, new ActionListener() {
            @Override
            public void onConfirm() {
                if (myDeviceAccount != null) {
                    HandheldAuthorization.getInstance().putString(HandheldAuthorization.ID, myDeviceAccount.getId());
                    HandheldAuthorization.getInstance().getCurrentUser().setId(myDeviceAccount.getId());
                    callback.onSuccess(myDeviceAccount);
                } else {
                    callback.onError(null);
                }

            }

            @Override
            public void onCancel() {
                callback.onError(null);
            }
        });
    }


//    public void refreshToken(final WindmillCallback callback) {
//        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
//        AuthManager.UserLevel level = AuthManager.currentLevel();
//
//        switch (level) {
//            case LEVEL3:
//                getMyAccount(callback, true);
//                break;
//
//            case LEVEL2:
//
//                String refresh_token = HandheldAuthorization.getInstance().getCurrentUser().getRefresh_token();
//                String hash = Util.getScretKey(refresh_token + WindmillConfiguration.clientId,
//                        WindmillConfiguration.secretKey);
//
//                Logger.print(this, "refresh_token : " + refresh_token);
//                Logger.print(this, "hash : " + hash);
//                Logger.print(this, "WindmillConfiguration.clientId : " + WindmillConfiguration.clientId);
//
//                Call<Login> call = frontEndMethod.requestDelegation(new DelegationReq(refresh_token,
//                        WindmillConfiguration.clientId, hash));
//
//
//                call.enqueue(new Callback<Login>() {
//                    @Override
//                    public void onResponse(Call<Login> call, Response<Login> response) {
//                        if (callback == null) {
//                            return;
//                        }
//
//                        if (response.isSuccess()) {
//
//                            HandheldAuthorization.getInstance().setLoginToken(response.body());
//
//                            callback.onSuccess(null);
//                        } else {
//                            ApiError error = ErrorUtil.parseError(response);
//                            callback.onError(error);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Login> call, Throwable t) {
//                        if (callback == null) {
//                            return;
//                        }
//
//                        callback.onFailure(call, t);
//                    }
//                });
//
//
//                break;
//        }
//
//
//    }

    public void checkMyAccount(final MyDeviceAccount myDeviceAccount, boolean forTokenReissue, final WindmillCallback callback, final GlobalActivity globalActivity) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
        Logger.d("", "hanz API checkMyAccount");
        if (forTokenReissue) {
            boolean currentUserPaired = myDeviceAccount.getPairing() != null;

            if (currentUserPaired) {
                //Pairing User
                long ts = TimeManager.getInstance().getServerCurrentTimeMillis();
                String nonce = Util.getRandomHexString(6);

                Call<AccessToken> call = frontEndMethod.postAccessToken(myDeviceAccount.getPairing().getHandheld_id(), WindmillConfiguration.defaultPassword,
                        WindmillConfiguration.clientId, myDeviceAccount.getId(), ts, nonce,
                        Util.getScretKey(
                                WindmillConfiguration.clientId + myDeviceAccount.getId() + ts + nonce, WindmillConfiguration.secretKey));

                call.enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        if (callback == null) {
                            return;
                        }

                        if (response.isSuccess()) {
                            HandheldAuthorization.getInstance().setPairingAccessToken(response.body());
                            getMeShow(myDeviceAccount, callback);
                        } else {
                            ApiError error = ErrorUtil.parseError(response);
                            callback.onError(error);
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        if (callback == null) {
                            return;
                        }

                        callback.onFailure(call, t);
                    }
                });


            } else {
                //UnPaired User
                ApiError error = new ApiError();
                error.tokenExpired();
                callback.onError(error);
            }


        } else {
            HandheldAuthorization.getInstance().setDeviceAccount(myDeviceAccount);
            HandheldAuthorization.getInstance().setIsPoorUser(!myDeviceAccount.getConfig().isViettelMobileBalanceUser());
            if (myDeviceAccount.getPairing() == null) {
                callback.onSuccess(null);
                if(HandheldAuthorization.getInstance().isLogIn()) {
                    HandheldAuthorization.getInstance().setPairingAccessToken(null);
                }
            } else {
                getPairingToken(myDeviceAccount, callback, globalActivity);
            }
        }
    }

    private void getPairingToken(final MyDeviceAccount myDeviceAccount, final WindmillCallback callback, final GlobalActivity globalActivity) {
        globalActivity.showProgress();
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
        long ts = TimeManager.getInstance().getServerCurrentTimeMillis();
        String nonce = Util.getRandomHexString(6);
        Logger.d("", "hanz API getPairingToken");

        Call<AccessToken> call = frontEndMethod.postAccessToken(myDeviceAccount.getPairing().getFamily_id(), WindmillConfiguration.defaultPassword,
                WindmillConfiguration.clientId, myDeviceAccount.getPairing().getHandheld_id(), ts, nonce,
                Util.getScretKey(
                        WindmillConfiguration.clientId + myDeviceAccount.getPairing().getHandheld_id() + ts + nonce, WindmillConfiguration.secretKey));


        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                globalActivity.hideProgress();
                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {
                    HandheldAuthorization.getInstance().setPairingAccessToken(response.body());
                    Logger.d("", "hanz API getPairingToken success");
                    getMeShow(myDeviceAccount, callback);
//                    callback.onSuccess(null);
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                globalActivity.hideProgress();
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });

    }


    private void getPairingToken(PairingRes pairingRes, final WindmillCallback callback, final GlobalActivity globalActivity) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
        long ts = TimeManager.getInstance().getServerCurrentTimeMillis();
        String nonce = Util.getRandomHexString(6);

        Call<AccessToken> call = frontEndMethod.postAccessToken(pairingRes.getFamilyId(), WindmillConfiguration.defaultPassword,
                WindmillConfiguration.clientId, pairingRes.getDeviceId(), ts, nonce,
                Util.getScretKey(
                        WindmillConfiguration.clientId + pairingRes.getDeviceId() + ts + nonce, WindmillConfiguration.secretKey));


        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, final Response<AccessToken> response) {
                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {
                    HandheldAuthorization.getInstance().setPairingAccessToken(response.body());

                    getMyAccount(new WindmillCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            ChannelManager.getInstance().clearData();

                            ChannelManager.getInstance().getChannel(new WindmillCallback() {
                                @Override
                                public void onSuccess(Object obj) {
                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {
                                }

                                @Override
                                public void onError(ApiError error) {
                                }

                            });
                            callback.onSuccess(obj);
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            callback.onFailure(call, t);
                        }

                        @Override
                        public void onError(ApiError error) {
                            callback.onError(error);
                        }
                    }, false, globalActivity, false);

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });

    }

    public class SwitchDevices {
        public ArrayList<String> devices;

        public ArrayList<String> getDevices() {
            return devices;
        }

        public void setDevices(ArrayList<String> devices) {
            this.devices = devices;
        }

        public void setSwitchDevices(ArrayList<MyDeviceAccount.Devices> list) {
            this.devices = new ArrayList<>();
            for (MyDeviceAccount.Devices devices : list) {
                this.devices.add(devices.getId());
            }
        }
    }

    public void requestSwitchDevice(ArrayList<MyDeviceAccount.Devices> device,
                                    final WindmillCallback callback) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
        SwitchDevices switchDevices = new SwitchDevices();
        switchDevices.setSwitchDevices(device);
        Call<Login> call = frontEndMethod.requestSwitchDevice(AuthManager.getAccessToken(), switchDevices);

        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
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
            public void onFailure(Call<Login> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });

    }

    public void setSwitchDevice(MyDeviceAccount deviceAccount, WindmillCallback callback, GlobalActivity globalActivity) {
        HandheldAuthorization.getInstance().putString(HandheldAuthorization.ID, deviceAccount.getCellphone());
        HandheldAuthorization.getInstance().getCurrentUser().setId(deviceAccount.getCellphone());
        FrontEndLoader.getInstance().checkMyAccount(deviceAccount, false, callback, globalActivity);
        getMyAccount(callback, false, globalActivity, false);
    }


    private void getMeShow(final MyDeviceAccount myDeviceAccount, final WindmillCallback callback) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
        Call<MeShowRes> call = frontEndMethod.getMeShow(AuthManager.getAccessToken(), true, true);
        Logger.d("", "hanz API getMeShow");
        call.enqueue(new Callback<MeShowRes>() {
            @Override
            public void onResponse(Call<MeShowRes> call, Response<MeShowRes> response) {
                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {

                    MeShowRes meShowRes = response.body();

                    HandheldAuthorization.getInstance().setSoId(meShowRes.getSo_id());
                    HandheldAuthorization.getInstance().setCust_id(meShowRes.getCustId());

                    callback.onSuccess(myDeviceAccount);
//                    Logger.d("", "hanz API getMeShow success");
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<MeShowRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });

    }

    private boolean isLimitedDevice() {
        if (loginRes != null && loginRes.getStatus().equalsIgnoreCase("ready")) {
            return true;
        } else return false;
    }

    private boolean isLimitedDeviceThroughTotal(RegistedDevice registedDevice) {
        if (registedDevice != null && registedDevice.getRegistered() >= registedDevice.getTotal()) {
            return true;
        } else return false;
    }

    private boolean isLimitedDeviceThroughStatus(String status) {
        if (status != null && status.equalsIgnoreCase(PhoneLoginFragment.STATUS_READY) && AuthManager.currentLevel() != AuthManager.UserLevel.LEVEL3) {
            return true;
        } else
            return false;
    }

    public void requestAuthenticationCode(String id, final WindmillCallback callback) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
//        String hash = Util.getScretKey(
//                WindmillConfiguration.clientId + AltiPlayer.GetDeviceUniqueID() + id, WindmillConfiguration.secretKey);
        String hash = Util.getScretKey(
                WindmillConfiguration.clientId + WindmillConfiguration.deviceId + id, WindmillConfiguration.secretKey);

//        CheckAuthenticationCodeReq checkAuthenticationCodeReq = new CheckAuthenticationCodeReq(id
//                , WindmillConfiguration.clientId, AltiPlayer.GetDeviceUniqueID(), hash);
        CheckAuthenticationCodeReq checkAuthenticationCodeReq = new CheckAuthenticationCodeReq(id
                , WindmillConfiguration.clientId, WindmillConfiguration.deviceId, hash);

        Call<AuthCodeRes> call = frontEndMethod.requestAuthenticationCode(checkAuthenticationCodeReq);

        call.enqueue(new Callback<AuthCodeRes>() {
            @Override
            public void onResponse(Call<AuthCodeRes> call, Response<AuthCodeRes> response) {
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
            public void onFailure(Call<AuthCodeRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }


    public void requestPairing(String otp, final WindmillCallback callback, final GlobalActivity globalActivity) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);

//        String token = AuthManager.getLoginToken();
        String hash = Util.getScretKey(otp + AuthManager.getLoginToken(), WindmillConfiguration.secretKey);
        Call<PairingRes> call = frontEndMethod.requestPairing(AuthManager.getLoginToken(),
                new RequestPairingReq(otp, hash));


        call.enqueue(new Callback<PairingRes>() {
            @Override
            public void onResponse(Call<PairingRes> call, Response<PairingRes> response) {

                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {
                    getPairingToken(response.body(), callback, globalActivity);
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }

            }

            @Override
            public void onFailure(Call<PairingRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });

    }

    public void requestUnPairing(final WindmillCallback callback) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
        Call<Void> call = frontEndMethod.requestUnpairing(AuthManager.getAccessToken());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {
                    HandheldAuthorization.getInstance().setPairingAccessToken(null);
                    ChannelManager.getInstance().clearData();
                    callback.onSuccess(null);
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

    private Login loginRes = null;

    public void requestLogin(final String id, final String password,
                             final WindmillCallback callback, final GlobalActivity globalActivity) {
        globalActivity.showProgress();
        loginRes = null;
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
        String encryptedPassword = AsSHA1.getInstance().encryptHmacSHA1(id, password);
        Logger.d("", "hanz API login");
//        Call<Login> call = frontEndMethod.login(new LoginReq(id, encryptedPassword, WindmillConfiguration.clientId, Util.generateMyDeviceInfo(AltiPlayer.GetDeviceUniqueID())));
        Call<Login> call = frontEndMethod.login(new LoginReq(id, encryptedPassword, WindmillConfiguration.clientId, Util.generateMyDeviceInfo(WindmillConfiguration.deviceId)));

        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                globalActivity.hideProgress();

                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {
                    loginRes = response.body();
                    HandheldAuthorization.getInstance().loginInfoInit(id, password, WindmillConfiguration.deviceId, response.body());
                    ChannelManager.getInstance().clearData();
                    getMyAccount(callback, false, globalActivity, true);
                    Logger.d("", "hanz API login succes");

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                globalActivity.hideProgress();
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void inquireVmSubscriber(
            final WindmillCallback callback) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);

        String id = HandheldAuthorization.getInstance().getCurrentId();

        CheckVmSubscriberReq checkVmSubscriberReq = new CheckVmSubscriberReq(id, WindmillConfiguration.clientId, Util.getScretKey(id + WindmillConfiguration.clientId, WindmillConfiguration.secretKey));

        Call<ResultRes> call = frontEndMethod.inquireVmSubscriber(checkVmSubscriberReq);

        call.enqueue(new Callback<ResultRes>() {
            @Override
            public void onResponse(Call<ResultRes> call, Response<ResultRes> response) {

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
            public void onFailure(Call<ResultRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void requestLogout(final WindmillCallback callback) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
        Call<Void> call = frontEndMethod.logout(AuthManager.getLoginToken());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (callback == null) {
                    return;
                }
                callback.onSuccess(response);
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


    public Login getLoginRes() {
        return loginRes;
    }

    public void changePassword(final String newPassword, final WindmillCallback callback) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
        String id = HandheldAuthorization.getInstance().getCurrentUser().getId();
        String currentPassword = HandheldAuthorization.getInstance().getCurrentUser().getPassword();
        Logger.print(this, "changePassword , currentPW : " + currentPassword);

        String newPasswordEncrypted = AsSHA1.getInstance().encryptHmacSHA1(id, newPassword);
        String currentPasswordEncrypted = AsSHA1.getInstance().encryptHmacSHA1(id, currentPassword);

        Call<Void> call = frontEndMethod.changePassword(AuthManager.getLoginToken(), new ChangePasswordReq(currentPasswordEncrypted, newPasswordEncrypted));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    if (AuthManager.currentLevel() != AuthManager.UserLevel.LEVEL1) {
                        HandheldAuthorization.getInstance().changePassword(newPassword);
                        requestLoginForReissueToken(callback);
                    } else {
                        callback.onSuccess(null);
                    }
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

    private void requestLoginForReissueToken(final WindmillCallback callback) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
        String id = HandheldAuthorization.getInstance().getCurrentId();
        String password = HandheldAuthorization.getInstance().getCurrentUser().getPassword();
        String passwordEncrypted = AsSHA1.getInstance().encryptHmacSHA1(id, password);
        String udid = HandheldAuthorization.getInstance().getCurrentUser().getDeviceUDID();
        if (udid == null) {
            udid = WindmillConfiguration.deviceId;
        }
        Call<Login> call = frontEndMethod.login(new LoginReq(id, passwordEncrypted, WindmillConfiguration.clientId, Util.generateMyDeviceInfo(udid)));
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {

                    HandheldAuthorization.getInstance().setLoginToken(response.body());
                    callback.onSuccess(null);

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });

    }

    /**
     * @Response : result = true -> already registered
     * false -> not registered
     */
    public void checkID(String id, final WindmillCallback callback) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
        String hash = Util.getScretKey(id + WindmillConfiguration.clientId, WindmillConfiguration.secretKey);
        Call<ResultRes> call = frontEndMethod.checkId(new CheckIdReq(id, WindmillConfiguration.clientId, hash));

        call.enqueue(new Callback<ResultRes>() {
            @Override
            public void onResponse(Call<ResultRes> call, Response<ResultRes> response) {
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
            public void onFailure(Call<ResultRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void inquireVmSubscriber(String id, final WindmillCallback callback) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
        String hash = Util.getScretKey(id + WindmillConfiguration.clientId, WindmillConfiguration.secretKey);
        Call<ResultRes> call = frontEndMethod.checkId(new CheckIdReq(id, WindmillConfiguration.clientId, hash));

        call.enqueue(new Callback<ResultRes>() {
            @Override
            public void onResponse(Call<ResultRes> call, Response<ResultRes> response) {
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
            public void onFailure(Call<ResultRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void createAccount(String id, String password, String otp, final WindmillCallback callback) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);

//        CreateAccountReq createAccountReq = Util.generateAccountInfo(id, password, otp, AltiPlayer.GetDeviceUniqueID());
        CreateAccountReq createAccountReq = Util.generateAccountInfo(id, password, otp, WindmillConfiguration.deviceId);
        Call<Void> call = frontEndMethod.createAccount(createAccountReq);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    callback.onSuccess(null);
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


    public void autoLogin(final WindmillCallback callback, final GlobalActivity globalActivity) {
        AsyncTask<String, String, String> asyn = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                String strIpAddress = getLocalIpAddressUsingPing(true);
                if (!strIpAddress.equalsIgnoreCase(""))
                    return strIpAddress;
                else return getLocalIpAddressUsingPing(false);
            }

            @Override
            protected void onPostExecute(String client_ip) {
                super.onPostExecute(client_ip);

                if (client_ip == null && client_ip.isEmpty()) {
                    callback.onError(null);
                    return;
                }

                FrontEndMethod frontEndMethod = ServiceGenerator.getHttpInstance().createHttpSerive(FrontEndMethod.class);
//                String hash = Util.getScretKey(AltiPlayer.GetDeviceUniqueID() + WindmillConfiguration.clientId + client_ip, WindmillConfiguration.secretKey);
                String hash = Util.getScretKey(WindmillConfiguration.deviceId + WindmillConfiguration.clientId + client_ip, WindmillConfiguration.secretKey);
//                AutomaticDetectionReq automaticDetectionReq = new AutomaticDetectionReq(client_ip, WindmillConfiguration.clientId,
//                        hash,
//                        Util.generateMyDeviceInfo(AltiPlayer.GetDeviceUniqueID()));
                AutomaticDetectionReq automaticDetectionReq = new AutomaticDetectionReq(client_ip, WindmillConfiguration.clientId,
                        hash,
                        Util.generateMyDeviceInfo(WindmillConfiguration.deviceId));
                Call<Login> call = frontEndMethod.requestAutoDetech(automaticDetectionReq);

                call.enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {

                        if (response.isSuccess()) {
                            Login login = response.body();

                            if (login != null) {
                                loginRes = login;
                                HandheldAuthorization.getInstance().setLoginToken(loginRes);
                                getMyAccount(callback, false, globalActivity, true);
                            } else {
                                callback.onError(null);
                            }
                        } else {
                            ApiError error = ErrorUtil.parseError(response);
                            callback.onError(error);
                        }


                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {
                        if (callback == null) {
                            return;
                        }

                        callback.onFailure(call, t);
                    }
                });
            }
        };
        asyn.execute();
//        String client_ip = getLocalIpAddressUsingPing();
//        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);
//        String hash = Util.getScretKey(AltiPlayer.GetDeviceUniqueID() + WindmillConfiguration.clientId + client_ip, WindmillConfiguration.secretKey);
//        AutomaticDetectionReq automaticDetectionReq = new AutomaticDetectionReq(client_ip, WindmillConfiguration.clientId,
//                hash,
//                Util.generateMyDeviceInfo(AltiPlayer.GetDeviceUniqueID()));
//        Call<Login> call = frontEndMethod.requestAutoDetech(automaticDetectionReq);
//
//        call.enqueue(new Callback<Login>() {
//            @Override
//            public void onResponse(Call<Login> call, Response<Login> response) {
//
//                Login login = response.body();
//
//                if(login != null) {
//                    HandheldAuthorization.getInstance().setLoginToken(response.body());
//                    getMyAccount(callback, false);
//                } else {
//                    callback.onError(null);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Login> call, Throwable t) {
//                if (callback == null) {
//                    return;
//                }
//
//                callback.onFailure(call, t);
//            }
//        });

    }


    public void resetPassword(String id, final String password, String otp, final WindmillCallback callback) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);

        String passwordEncrypted = AsSHA1.getInstance().encryptHmacSHA1(id, password);

        String hash = Util.getScretKey(WindmillConfiguration.clientId + otp + id + passwordEncrypted, WindmillConfiguration.secretKey);

        ResetPasswordReq resetPasswordReq = new ResetPasswordReq(id, otp, WindmillConfiguration.clientId, hash, passwordEncrypted);
        Call<Void> call = frontEndMethod.resetPassword(resetPasswordReq);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    if (AuthManager.currentLevel() != AuthManager.UserLevel.LEVEL1) {
                        HandheldAuthorization.getInstance().changePassword(password);
                        requestLoginForReissueToken(callback);
                    } else {
                        callback.onSuccess(null);
                    }
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

    public void changePaymentOption(String method, final WindmillCallback callback) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);


        RequestChangeMethod requestChangeMethod = new RequestChangeMethod();
        requestChangeMethod.setSelected_payment_option(method);

        Call<Void> call = frontEndMethod.changePaymentOption(AuthManager.getAccessToken(), requestChangeMethod);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    callback.onSuccess(null);
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

    public void verifyAuthenticationCode(String id, String otp, final WindmillCallback callback) {
        FrontEndMethod frontEndMethod = ServiceGenerator.getInstance().createSerive(FrontEndMethod.class);

        String hash = Util.getScretKey(id + otp + WindmillConfiguration.clientId, WindmillConfiguration.secretKey);

        VerifyCodeReq resetPasswordReq = new VerifyCodeReq(id, otp, WindmillConfiguration.clientId, hash);
        Call<VerifyCodeRes> call = frontEndMethod.verifyAuthenticationCode(resetPasswordReq);

        call.enqueue(new Callback<VerifyCodeRes>() {
            @Override
            public void onResponse(Call<VerifyCodeRes> call, Response<VerifyCodeRes> response) {
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
            public void onFailure(Call<VerifyCodeRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    private String getLocalIpAddressUsingPing() {
        try {
            Socket socket = new Socket("www.google.com", 80);
            return socket.getLocalAddress().toString().replace("/", "").trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getLocalIpAddressUsingPing(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return "";
    }
}
