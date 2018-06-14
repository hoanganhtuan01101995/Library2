package com.alticast.viettelottcommons.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.alticast.viettelottcommons.GlobalKey;
import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.GetSeriesInfoListener;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.def.PlatformFileds;
import com.alticast.viettelottcommons.def.PurchasePathway;
import com.alticast.viettelottcommons.def.entry.EntryPathLogImpl;
import com.alticast.viettelottcommons.dialog.ActionListener;
import com.alticast.viettelottcommons.dialog.ConfirmMessageDialog;
import com.alticast.viettelottcommons.dialog.DateWheelDialog;
import com.alticast.viettelottcommons.dialog.DeviceLimitDialog;
import com.alticast.viettelottcommons.dialog.ListDeviceLoginDialog;
import com.alticast.viettelottcommons.dialog.ListPlayListDialog;
import com.alticast.viettelottcommons.dialog.MessageDialog;
import com.alticast.viettelottcommons.dialog.PackageOptionDialogPhase2;
import com.alticast.viettelottcommons.dialog.PackagePeriodDialogPhase2;
import com.alticast.viettelottcommons.dialog.PhoneLoginFragment;
import com.alticast.viettelottcommons.dialog.ProgressDialogFragment;
import com.alticast.viettelottcommons.dialog.PromotionImageDialog;
import com.alticast.viettelottcommons.dialog.PurchaseDataPackageConfirmDialogPhase2;
import com.alticast.viettelottcommons.dialog.PurchaseOption;
import com.alticast.viettelottcommons.dialog.PurchaseOptionDialog;
import com.alticast.viettelottcommons.dialog.UpdatePackageAlertDialog;
import com.alticast.viettelottcommons.helper.ChannelPurchaseHelper;
import com.alticast.viettelottcommons.helper.ProgramPurchaseHelper;
import com.alticast.viettelottcommons.helper.PurchaseCommonHelper;
import com.alticast.viettelottcommons.helper.RentalPeriodProductPurchaseHelper;
import com.alticast.viettelottcommons.loader.CampaignLoader;
import com.alticast.viettelottcommons.loader.ChannelLoader;
import com.alticast.viettelottcommons.loader.FrontEndLoader;
import com.alticast.viettelottcommons.loader.PlatformLoader;
import com.alticast.viettelottcommons.loader.ProgramLoader;
import com.alticast.viettelottcommons.loader.PurchaseCheckLoader;
import com.alticast.viettelottcommons.loader.PurchaseLoader;
import com.alticast.viettelottcommons.loader.UserDataLoader;
import com.alticast.viettelottcommons.loader.VWalletLoader;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.ChannelManager;
import com.alticast.viettelottcommons.manager.ChromeCastManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.manager.MenuManager;
import com.alticast.viettelottcommons.manager.MyContentManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.ChannelProduct;
import com.alticast.viettelottcommons.resource.ChargedResult;
import com.alticast.viettelottcommons.resource.Location;
import com.alticast.viettelottcommons.resource.Login;
import com.alticast.viettelottcommons.resource.Menu;
import com.alticast.viettelottcommons.resource.MyDeviceAccount;
import com.alticast.viettelottcommons.resource.Path;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.Program;
import com.alticast.viettelottcommons.resource.RegistedDevice;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.resource.Schedule;
import com.alticast.viettelottcommons.resource.Series;
import com.alticast.viettelottcommons.resource.Vod;
import com.alticast.viettelottcommons.resource.ads.Placement;
import com.alticast.viettelottcommons.resource.response.ProgramList;
import com.alticast.viettelottcommons.resource.response.PurchaseListRes;
import com.alticast.viettelottcommons.resource.response.WalletRes;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.NetworkUtil;
import com.alticast.viettelottcommons.util.ProductUtils;
import com.alticast.viettelottcommons.util.Util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

/**
 * Created by mc.kim on 10/9/2016.
 */
public abstract class GlobalActivity extends AppCompatActivity {

    public static final int UNWATCHABLE_VTVCAB = -1;
    public static final int UNWATCHABLE_INVISIBLE = -2;
    public static final int UNWATCHABLE_NOT_EXIST = -3;
    public static final int UNWATCHABLE_NOT_NETWORK = -4;
    public static final int UNWATCHABLE_NOT_LOGIN = -5;
    public static final int UNWATCHABLE_NOT_AVAIL_MOBILE = -5;
    public static final int WATCHABLE_NEED_PURCHASE = 1;
    public static final int WATCHABLE_CAN_PLAY = 2;
    public static final int WATCHABLE_CAN_PLAY_FREE = 3;
    public static final int WATCHABLE_CAN_PLAY_PURCHASED = 4;

    protected final String BUDDLE_SEARCH = "search";
    protected final String BUNDLE_KEYWORD = "keyword";
    public static ProgressDialogFragment pDialog = null;
    public static boolean isLoadingInfo = false;
    public static final String SEARCH_RESULT = "/search/result";
    protected static boolean isFromFacebook;
    protected static String facebookPgmId;
    protected String searchString = "";

    PurchaseOptionDialog purchaseOptionDialog = null;
    PurchaseCommonHelper purchaseCommonHelper;

    private Button networkMode;
    private boolean isProcessingPurchase;

    public static final String RELOAD_ADD_PLAYLIST = "RELOAD_ADD_PLAYLIST";

    //    public Stack<Pair<String, Object>> stackDetailFragment = new Stack<>();
    public ArrayList<Pair<String, Object>> listFragmentDetails = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeStatusBarColor(R.color.main_header);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        WindmillConfiguration.LANGUAGE = WindmillConfiguration.LANGUAGE_VIE;

        String lan = Locale.getDefault().getLanguage();

        if (!lan.equals("vi")) {
            Locale locale = new Locale("vi");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }
    }

    public void pushFragment(Pair<String, Object> pairData) {
        listFragmentDetails.add(pairData);
    }

    public void clearAllVodFragment() {
        listFragmentDetails.clear();
    }

    public boolean isEmptyVodFragment() {
        return listFragmentDetails.size() < 2;
    }

    public void setVWalletChargeTitle(String title) {
    }

    public Pair<String, Object> popFragment() {
        if (listFragmentDetails.isEmpty()) {
            return null;
        }
        listFragmentDetails.remove(listFragmentDetails.size() - 1);
        if (listFragmentDetails.isEmpty()) {
            return null;
        }
        return listFragmentDetails.get(listFragmentDetails.size() - 1);
    }

    public boolean checkAndProcessSearchBackFlow() {
        if (listFragmentDetails.isEmpty()) return false;
        if (listFragmentDetails.size() == 1) {
            listFragmentDetails.clear();
            removeVodDetailFragment();
            return true;
        }

        Pair<String, Object> pair = popFragment();
        if (pair.second instanceof Vod) {
            Vod mVod = (Vod) pair.second;
            goToDetailFragment(mVod);
        } else if (pair.second instanceof String) {
            goToSearchFragment((String) pair.second, null);
        } else {
            return false;
        }

        return true;
    }

    // way = 0: next; way = 1: prev
    public void replaceFragment(Fragment fragment, int id, int way) {

    }

    public void replaceVWalletComplete(ChargedResult chargedResult) {

    }

    public void removeFragment(int id) {

    }

    public void closeVWallet() {

    }

    public void processVod(Vod vod) {

    }

    public void goToSearchFragment(String keyword, View.OnClickListener callback) {

    }

    public void removeVodDetailFragment() {

    }

    public void goToDetailFragment(Vod vod) {

    }

    public void goToSearchFragment(String keyword) {

    }

    public void goToSeriesPlayFragment(Vod vod) {

    }

    public void onChangeNetworkFake() {

    }

    public void goToMyWallet() {

    }

    public void showProgress() {
        try {
            if (isFinishing()) {
                return;
            }

            FragmentManager fragmentManager = getSupportFragmentManager();

            showProgress(fragmentManager);

//            if (pDialog != null && pDialog.isShowing()) {
//                pDialog.dismiss();
//            } else {
//                pDialog = new ProgressDialogFragment();
//            }
//
//
//            pDialog.show(getSupportFragmentManager(), "");

        } catch (Exception e) {
            sendLog("Exception", "" + e);
        }
//        finally {
//            isLoadingInfo = true;
//        }
    }

    public void showProgress(FragmentManager fragmentManager) {
        try {
            if (isFinishing() || fragmentManager == null) {
                return;
            }

//            if(true) {
//                return;
//            }

//            Fragment prev = fragmentManager.findFragmentByTag("ProgressDialogFragment");
//            if (prev != null) {
////                ProgressDialogFragment df = (ProgressDialogFragment) prev;
////                fragmentManager.beginTransaction().remove(prev);
//                FragmentTransaction ft = fragmentManager.beginTransaction();
//                ft.remove(prev);
//
//                if (Build.VERSION.SDK_INT >= 24) {
//                    ft.commit();
//                } else {
//                    ft.commit();
//                    fragmentManager.executePendingTransactions();
//                }
////                df.dismiss();
//            }
//
//            ProgressDialogFragment dialogFragment = new ProgressDialogFragment();
//            dialogFragment.show(fragmentManager, "ProgressDialogFragment");

            if (pDialog != null  && pDialog.isAdded() && pDialog.isShowing()) {
                pDialog.dismiss();
            } else {
                pDialog = new ProgressDialogFragment();
            }


            pDialog.show(fragmentManager, "ProgressDialogFragment");
            Log.e("showProgress", "Done");

        } catch (Exception e) {
            sendLog("Exception", "" + e);
            Log.e("showProgress", "" + e.getMessage());
        }
//        finally {
//            isLoadingInfo = true;
//        }
    }

    public void hideProgress() {

        try {
            if (!isFinishing()) {

                hideProgress(getSupportFragmentManager());


//                pDialog.dismiss();
                Log.e("hideProgress", "Done");
            }
            Log.e("hideProgress", "Not Done");
        } catch (Exception e) {
            sendLog("Exception", "" + e);
            Log.e("hideProgress", "" + e.getMessage());
        }
//        finally {
//            isLoadingInfo = false;
//        }
    }
    public void hideProgress(FragmentManager fragmentManager) {

        try {
            if (!isFinishing()) {

//                if(true) {
//                    return;
//                }

//                Fragment prev = fragmentManager.findFragmentByTag("ProgressDialogFragment");
//                if (prev != null) {
////                    ProgressDialogFragment df = (ProgressDialogFragment) prev;
////                    df.dismiss();
//
//                    FragmentTransaction ft = fragmentManager.beginTransaction();
//                    ft.remove(prev);
//
//                    if (Build.VERSION.SDK_INT >= 24) {
//                        ft.commit();
//                    } else {
//                        ft.commit();
//                        fragmentManager.executePendingTransactions();
//                    }
//                }

                if(pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Log.e("hideProgress", "Done");
            }
            Log.e("hideProgress", "Not Done");
        } catch (Exception e) {
            sendLog("Exception", "" + e);
            Log.e("hideProgress", "" + e.getMessage());
        }
//        finally {
//            isLoadingInfo = false;
//        }
    }

    public final static int SEARCH_CODE = 100;

    public void onDismisDialog() {
        isProcessingPurchase = false;
    }

    public void processAutoLoginResult(Object obj, final ProcessAutoLoginListener processAutoLoginListener) {
        Login login = FrontEndLoader.getInstance().getLoginRes();
        ProgramLoader.getInstance().clearData();
        CampaignLoader.getInstance().clearData();
        //Check register device
        if (obj != null && obj instanceof MyDeviceAccount && isLimitedDeviceThroughStatus(login)) {
            showSwitchConfirmDialog((MyDeviceAccount) obj, 0, new ActionListener() {
                @Override
                public void onConfirm() {
                    processAutoLoginListener.onLoginSuccess();
                }

                @Override
                public void onCancel() {
                    processAutoLoginListener.onLoginFail();
                }
            });

            return;
        } else {
            showLoginMessage(login);
            HandheldAuthorization.getInstance().setLoginInfoSuccessState(true);
            processAutoLoginListener.onLoginSuccess();
        }
    }

    public boolean isLimitedDeviceThroughStatus(Login login) {
        if (login != null && login.getStatus() != null && login.getStatus().equalsIgnoreCase(PhoneLoginFragment.STATUS_READY)) {
            return true;
        } else return false;
    }

    public void showUpdatePackage(final ArrayList<Product> listProduct, final MyDeviceAccount deviceAccount, final int maxScreen, final ActionListener actionListener) {
        if (listProduct == null) {
            showSwitchConfirmDialog(deviceAccount, maxScreen, actionListener);
            return;
        }

        for (Product product : listProduct) {
            product.setMaxScreenCurrent(maxScreen);
        }

        final UpdatePackageAlertDialog dialog = new UpdatePackageAlertDialog();
        dialog.setSrc(maxScreen);

        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnConfirm) {
                    doPurchaseProduct(true, listProduct, new PurchaseSuccess() {
                        @Override
                        public void onPurchaseSuccess() {
                            FrontEndLoader.getInstance().getMyAccount(new WindmillCallback() {
                                @Override
                                public void onSuccess(Object obj) {
                                    if (obj == null) {
                                        actionListener.onConfirm();
                                    }
                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    actionListener.onConfirm();
                                }

                                @Override
                                public void onError(ApiError error) {
                                    actionListener.onConfirm();
                                }
                            }, false, GlobalActivity.this, false);
                        }

                        @Override
                        public void onPurchaseCancel() {
                            showSwitchConfirmDialog(deviceAccount, maxScreen, actionListener);
                        }
                    });
                } else {
                    showSwitchConfirmDialog(deviceAccount, maxScreen, actionListener);
                }

                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), UpdatePackageAlertDialog.CLASS_NAME);
    }

    public void showSwitchConfirmDialog(final MyDeviceAccount deviceAccount, final int maxScreen, final ActionListener actionListener) {
        if (deviceAccount == null) return;
        final DeviceLimitDialog dialog = new DeviceLimitDialog();
        Bundle args = new Bundle();
        args.putString(DeviceLimitDialog.PARAM_TITLE, getString(R.string.msgTitleDeviceLimitDialog));
        args.putString(DeviceLimitDialog.PARAM_SUBTITLE1, getString(R.string.msgsubTitleDeviceLimitDialog1));
        args.putString(DeviceLimitDialog.PARAM_SUBTITLE2, getString(R.string.msgsubTitleDeviceLimitDialog2));

        args.putString(DeviceLimitDialog.PARAM_BUTTON1, getString(R.string.yes));
        args.putString(DeviceLimitDialog.PARAM_BUTTON2, getString(R.string.no));
        args.putString(DeviceLimitDialog.PARAM_ID, deviceAccount.getId());
        final int total = getTotal(deviceAccount.getRegistered_device(), maxScreen);
        args.putInt(DeviceLimitDialog.PARAM_NUM_DEVICE_LIMIT, total);
        args.putBoolean(DeviceLimitDialog.PARAM_CANCEL_ON_TOUCH_OUTSIDE, false);


        dialog.setArguments(args);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnConfirm) {
                    RegistedDevice registedDevice = deviceAccount.getRegistered_device();
                    boolean check = checkMyDevice(registedDevice, maxScreen);

                    if (total == 0) check = false;
                    if (check) {
//                    if (registedDevice != null && (maxScreen > 0 && maxScreen < registedDevice.getRegistered())) {
                        if (total == 1) {
                            switchSelectRegisterDevice(dialog, registedDevice, total, new WindmillCallback() {
                                @Override
                                public void onSuccess(Object obj) {
                                    hideProgress();
                                    if (obj != null) {
                                        MyDeviceAccount myDeviceAccount = (MyDeviceAccount) obj;
                                        RegistedDevice registedDevice = myDeviceAccount.getRegistered_device();

//                                        boolean check = checkMyDevice(registedDevice, maxScreen);
                                        if (registedDevice.getRegistered() > total) {
                                            showListDeviceDialog(myDeviceAccount, total, this, actionListener);
                                        }
                                    }
                                    showLoginMessage(FrontEndLoader.getInstance().getLoginRes());
                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    hideProgress();
                                }

                                @Override
                                public void onError(ApiError error) {
                                    hideProgress();
                                }
                            }, actionListener);
                        } else {
                            showListDeviceDialog(deviceAccount, total, new WindmillCallback() {
                                @Override
                                public void onSuccess(Object obj) {
                                    hideProgress();
                                    if (obj != null) {
                                        MyDeviceAccount myDeviceAccount = (MyDeviceAccount) obj;
                                        RegistedDevice registedDevice = myDeviceAccount.getRegistered_device();

                                        if (registedDevice.getRegistered() > total) {
                                            showListDeviceDialog(myDeviceAccount, total, this, actionListener);
                                        }
                                    }
                                    showLoginMessage(FrontEndLoader.getInstance().getLoginRes());
                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    hideProgress();
                                }

                                @Override
                                public void onError(ApiError error) {
                                    hideProgress();
                                }
                            }, actionListener);
                        }
                    }

                } else {
                    logout();
//                    HandheldAuthorization.getInstance().logOut();
//                    if (actionListener != null)
//                        actionListener.onCancel();
                }
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), DeviceLimitDialog.CLASS_NAME);
    }

    private void logout() {

        if(!HandheldAuthorization.getInstance().isLogIn()) {
            checkAndRemoveOverlayFragment();
            return;
        }

        FrontEndLoader.getInstance().requestLogout(new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                HandheldAuthorization.getInstance().logOut();
                MyContentManager.getInstance().clearData();
                ChannelManager.getInstance().clearData();
                ProgramLoader.getInstance().clearData();
                checkAndRemoveOverlayFragment();
                Intent intent = new Intent(GlobalKey.MainActivityKey.LOGOUT_DATA);
                LocalBroadcastManager.getInstance(GlobalActivity.this).sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }

            @Override
            public void onError(ApiError error) {
            }
        });
    }

    public boolean checkMyDevice(RegistedDevice registedDevice, int maxScreen) {
        return WindmillConfiguration.isBigSmallPackageVersion ? registedDevice != null && (maxScreen > 0 && maxScreen <= registedDevice.getRegistered()) : registedDevice != null && (isLimitedDeviceThroughTotal(registedDevice));
    }

    public int getTotal(RegistedDevice registedDevice, int maxScreen) {
        return WindmillConfiguration.isBigSmallPackageVersion ? maxScreen : (registedDevice != null ? registedDevice.getTotal() : 0);
    }


    public void showPickerDialog(ArrayList<String> list, DateWheelDialog.onPickerClickListener listener) {
        final DateWheelDialog dialog = new DateWheelDialog();
        Bundle args = new Bundle();
        args.putStringArrayList(DateWheelDialog.PARAM_TITLE, list);
        dialog.setArguments(args);
        dialog.setPickerClickListener(listener);
        dialog.show(getSupportFragmentManager(), DateWheelDialog.CLASS_NAME);
    }

    public void showPickerDialog(ArrayList<String> list, int width, int gravity, DateWheelDialog.onPickerClickListener listener) {
        final DateWheelDialog dialog = new DateWheelDialog();
        dialog.setWidth(width);
        dialog.setGravity(gravity);

        Bundle args = new Bundle();
        args.putStringArrayList(DateWheelDialog.PARAM_TITLE, list);
        dialog.setArguments(args);
        dialog.setPickerClickListener(listener);
        dialog.show(getSupportFragmentManager(), DateWheelDialog.CLASS_NAME);
    }

    private void showListDeviceDialog(final MyDeviceAccount deviceAccount, final int maxScreen, final WindmillCallback callback, final ActionListener actionListener) {
        if (deviceAccount == null) return;

        final RegistedDevice registedDevice = deviceAccount.getRegistered_device();
        if (registedDevice == null) return;
        Bundle args = new Bundle();
        args.putParcelable(ListDeviceLoginDialog.PARAM_PRODUCT, registedDevice);
        args.putString(ListDeviceLoginDialog.DEVICE_ACCOUNT, WindmillConfiguration.deviceId);
        final ListDeviceLoginDialog dialog = new ListDeviceLoginDialog();
        dialog.setArguments(args);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnCancel) {
//                    logout(dialog);
                    dialog.dismiss();
                    callback.onError(null);
                } else if (v.getId() == R.id.btnConfirm) {
                    switchSelectRegisterDevice(dialog, registedDevice, maxScreen, callback, actionListener);
                }

            }
        });
        dialog.setCancelable(false);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dialog.show(getSupportFragmentManager(), ListDeviceLoginDialog.CLASS_NAME);
            }
        });

    }

    public void showSelectPlaylistDialog(final WindmillCallback callback) {

        final ListPlayListDialog dialog = new ListPlayListDialog();
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnCancel) {
                    dialog.dismiss();
                    callback.onFailure(null, null);
                } else if (v.getId() == R.id.btnConfirm) {
                    dialog.dismiss();
                    callback.onSuccess(MyContentManager.getInstance().myContentType);
                }

            }
        });
        dialog.setCancelable(false);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dialog.show(getSupportFragmentManager(), ListPlayListDialog.CLASS_NAME);
            }
        });

    }

    public void checkAndRemoveOverlayFragment() {

    }

    private void logout(final DialogFragment dialog) {
        showProgress();
        FrontEndLoader.getInstance().requestLogout(new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                hideProgress();
                HandheldAuthorization.getInstance().logOut();
                MyContentManager.getInstance().clearData();
                ChannelManager.getInstance().clearData();
                ProgramLoader.getInstance().clearData();
                checkAndRemoveOverlayFragment();
                Intent intent = new Intent(GlobalKey.MainActivityKey.REFRESH_DATA);
                LocalBroadcastManager.getInstance(GlobalActivity.this).sendBroadcast(intent);
                if (dialog != null)
                    dialog.dismiss();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                hideProgress();
                App.showAlertDialogNetwork(GlobalActivity.this, getSupportFragmentManager(), null);
            }

            @Override
            public void onError(ApiError error) {
                hideProgress();
                App.showAlertDialog(GlobalActivity.this, getSupportFragmentManager(), error, null);
            }
        });
    }


//    WindmillCallback switchDeviceCallback = new WindmillCallback() {
//        @Override
//        public void onSuccess(Object obj) {
//            hideProgress();
//
//            //Check register device
//            if (obj != null) {
//                MyDeviceAccount myDeviceAccount = (MyDeviceAccount) obj;
//                RegistedDevice registedDevice = myDeviceAccount.getRegistered_device();
//
//                if (registedDevice.getRegistered() > registedDevice.getTotal()) {
//                    showListDeviceDialog(myDeviceAccount, switchDeviceCallback);
//                }
//            }
//            showLoginMessage(FrontEndLoader.getInstance().getLoginRes());
//        }
//
//        @Override
//        public void onFailure(Call call, Throwable t) {
//            hideProgress();
//        }
//
//        @Override
//        public void onError(ApiError error) {
//            hideProgress();
//        }
//    };


    public void showLoginMessage(Login res) {
        if (res == null) return;
        String message = res.getMessage();
        if (message != null) {
            final MessageDialog dialog = new MessageDialog();
            Bundle args = new Bundle();
            args.putString(MessageDialog.PARAM_TITLE, getString(R.string.notice));
            args.putString(MessageDialog.PARAM_MESSAGE, message);
            args.putString(MessageDialog.PARAM_BUTTON_1, getString(R.string.ok));
            dialog.setArguments(args);
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show(getSupportFragmentManager(), MessageDialog.CLASS_NAME);
        }
    }

    public void showEncryptionPopUp() {
        String message = getResources().getString(R.string.encryptionContent);
        if (message != null) {
            final MessageDialog dialog = new MessageDialog();
            Bundle args = new Bundle();
            args.putString(MessageDialog.PARAM_TITLE, getString(R.string.notice));
            args.putString(MessageDialog.PARAM_MESSAGE, message);
            args.putString(MessageDialog.PARAM_BUTTON_1, getString(R.string.ok));
            dialog.setArguments(args);
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show(getSupportFragmentManager(), MessageDialog.CLASS_NAME);
        }
    }

    public void showEncryptionPopUpBasic(FragmentManager fragmentManager) {
        String message = getResources().getString(R.string.encryptionContent);
        if (message != null) {
            final MessageDialog dialog = new MessageDialog();
            Bundle args = new Bundle();
            args.putString(MessageDialog.PARAM_TITLE, getString(R.string.notice));
            args.putString(MessageDialog.PARAM_MESSAGE, message);
            args.putString(MessageDialog.PARAM_BUTTON_1, getString(R.string.ok));
            dialog.setArguments(args);
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.showDialog(fragmentManager, MessageDialog.CLASS_NAME);
        }
    }

    public interface ProcessAutoLoginListener {
        public void onLoginSuccess();

        public void onLoginFail();
    }

    private boolean isLimitedDeviceThroughTotal(RegistedDevice registedDevice) {
        if (registedDevice != null && registedDevice.getRegistered() >= registedDevice.getTotal()) {
            return true;
        } else return false;
    }

    private ArrayList<MyDeviceAccount.Devices> getListDevicesKickout(ArrayList<MyDeviceAccount.Devices> listSource) {
        ArrayList<MyDeviceAccount.Devices> list = null;

        for (MyDeviceAccount.Devices devices : listSource) {
            if (!devices.getId().equals(WindmillConfiguration.deviceId)) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(devices);
            }
        }

        return list;
    }

    private void switchSelectRegisterDevice(final DialogFragment dialog, RegistedDevice registedDevice, int maxScreen, final WindmillCallback callback, final ActionListener actionListener) {
//        showProgress();
        ArrayList<MyDeviceAccount.Devices> device = new ArrayList<>();
        // Case 1 : limit =1
        if (dialog != null && dialog instanceof DeviceLimitDialog && maxScreen == 1) {
//            device = registedDevice.getDevices();
            device = getListDevicesKickout(registedDevice.getDevices());
        }
        // Case 2 : limit >1
        else if (dialog != null && dialog instanceof ListDeviceLoginDialog) {
            if (((ListDeviceLoginDialog) dialog).getSelectedDevice().size() <= 0)
                return;
            else {
//                device = ((ListDeviceLoginDialog) dialog).getSelectedDevice();
                device = getListDevicesKickout(((ListDeviceLoginDialog) dialog).getSelectedDevice());
            }
        }
        if (device == null) {
            return;
        }

        OnSelectDeRegisterDevice(device, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                if (obj != null && obj instanceof Login) {
                    HandheldAuthorization.getInstance().loginInfoInitRefreshToken((Login) obj);
                    HandheldAuthorization.getInstance().setLoginInfoSuccessState(true);
                }
                ChannelManager.getInstance().clearData();
                FrontEndLoader.getInstance().getMyAccount(callback, false, GlobalActivity.this, false);
                dialog.dismiss();
                if (actionListener != null)
                    actionListener.onConfirm();
                hideProgress();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                App.showAlertDialogNetwork(GlobalActivity.this, getSupportFragmentManager(), null);
                if (actionListener != null)
                    actionListener.onCancel();
                hideProgress();
            }

            @Override
            public void onError(ApiError error) {
                App.showAlertDialog(GlobalActivity.this, getSupportFragmentManager(), error);
                if (actionListener != null)
                    actionListener.onCancel();
                hideProgress();
            }
        });
    }

    private void OnSelectDeRegisterDevice(ArrayList<MyDeviceAccount.Devices> device, final WindmillCallback callback
    ) {
        FrontEndLoader.getInstance().requestSwitchDevice(device, callback);
    }


    public void goToPlaybackFromVod(final Vod vod) {

        checkVod(vod, new CheckProgram() {
            @Override
            public void onCanWatch() {
                tuneVod(vod);
            }

            @Override
            public void onLoadComplete(CheckResult checkResult) {

            }
        });

    }

    public void goToPlaybackFromVod(final Vod vod, final boolean isNoVertical, final boolean isPlaylist, final WindmillCallback callback) {

        checkVod(vod, new CheckProgram() {
            @Override
            public void onCanWatch() {
                if (ChromeCastManager.getInstance().isChromeCastState()) {
                    callback.onSuccess(vod);
                    return;
                }
                if (isPlaylist) {
                    ChromeCastManager.getInstance().setPlayListMode(true);
                }
                tuneVod(vod, isNoVertical, isPlaylist);
            }

            @Override
            public void onLoadComplete(CheckResult checkResult) {

            }
        });

    }

    public void checkVod(final Vod vod, final CheckProgram checkProgram) {

        final CheckResult checkResult = new CheckResult();

        if (vod == null) {
            checkProgram.onLoadComplete(checkResult);
            return;
        }

        if (NetworkUtil.checkNetwork(this) == NetworkUtil.NetworkType.DISCONNECT) {
            App.showAlertDialogNetwork(this, getSupportFragmentManager(), null);
            checkProgram.onLoadComplete(checkResult);
            return;
        }

        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL1) {
            showLoginPairingDialog(false);
            checkProgram.onLoadComplete(checkResult);
            return;
        }

        if (vod.isPurchased()) {
            checkProgram.onCanWatch();
            checkResult.setSuccess(true);
            checkProgram.onLoadComplete(checkResult);
            return;
        }

        Product singleProduct = vod.getSingleProduct();
        if (singleProduct == null) {
            App.showAlertDialog(this, getSupportFragmentManager(), "", getString(R.string.cannotWatchContent), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
            checkProgram.onLoadComplete(checkResult);
            return;
        }

        if (singleProduct.isSTBCclass()) {
            App.showAlertDialog(this, getSupportFragmentManager(), "", getString(R.string.cannotWatchContentOnThisDevice), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
            checkProgram.onLoadComplete(checkResult);
            return;
        }

        NetworkUtil.NetworkType networkType = NetworkUtil.checkNetwork(this);

        String checkWatchSingle = checkWatchableSingle(vod, singleProduct, networkType);

        if (checkWatchSingle != null) {

            if (!checkWatchSingle.equals(Product.FREE)) {
                if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3 && networkType == NetworkUtil.NetworkType.MOBILE) {
                    checkProgram.onLoadComplete(checkResult);
                    App.showAlertDialog(this, getSupportFragmentManager(), "",
                            getString(com.alticast.viettelottcommons.R.string.cannotWatchVTVCab), null);
                    return;
                }
                vod.setPurchaseId(checkWatchSingle);
            }
            vod.setPurchased(true);
            checkProgram.onCanWatch();
            checkResult.setSuccess(true);
            checkProgram.onLoadComplete(checkResult);
            return;
        } else {
            if (vod.isSingleOnly()) {
                showPurchaseProcress(vod, singleProduct, null, vod, "vod", new PurchaseSuccess() {
                            @Override
                            public void onPurchaseSuccess() {
      ;                          vod.setPurchased(true);
                                checkProgram.onCanWatch();
                                checkResult.setSuccess(true);
                            }

                            @Override
                            public void onPurchaseCancel() {
                            }
                        }
                );
                return;
            }
        }

        ArrayList<Product> listPackage = vod.getHandheldAvailableProducts();
        ArrayList<Product> listSVOD = getSVODProduct(listPackage);
        if (listSVOD != null) {
            String checkWatchablePackage = checkWatchablePackage(listSVOD, listPackage);
            if (checkWatchablePackage != null) {
                if (!checkWatchablePackage.equals(Product.FREE)) {
                    if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3 && networkType == NetworkUtil.NetworkType.MOBILE) {
                        checkProgram.onLoadComplete(checkResult);
                        App.showAlertDialog(this, getSupportFragmentManager(), "",
                                getString(com.alticast.viettelottcommons.R.string.cannotWatchVTVCab), null);
                        return;
                    }
                    vod.setPurchaseId(checkWatchablePackage);
                }
                vod.setPurchased(true);
                checkProgram.onCanWatch();
                checkResult.setSuccess(true);
                checkProgram.onLoadComplete(checkResult);
            } else {
                checkProgram.onLoadComplete(checkResult);
                if (!WindmillConfiguration.isEnableRVOD) {
                    showPurchaseProcress(vod, null, listSVOD, vod, "vod", new PurchaseSuccess() {
                                @Override
                                public void onPurchaseSuccess() {
                                    vod.setPurchased(true);
                                    checkProgram.onCanWatch();
                                    checkResult.setSuccess(true);

                                }

                                @Override
                                public void onPurchaseCancel() {
                                }
                            }
                    );
                } else {
                    showPurchaseProcress(vod, singleProduct, listSVOD, vod, "vod", new PurchaseSuccess() {
                                @Override
                                public void onPurchaseSuccess() {
                                    vod.setPurchased(true);
                                    checkProgram.onCanWatch();
                                    checkResult.setSuccess(true);
                                }

                                @Override
                                public void onPurchaseCancel() {
                                }
                            }
                    );
                }

            }
            return;
        }

        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {

            String checkWatchablePackage = checkWatchablePackage(listPackage, false, NetworkUtil.NetworkType.WIFI, true);
            if (checkWatchablePackage != null) {
                if (!checkWatchablePackage.equals(Product.FREE)) {
                    if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3 && networkType == NetworkUtil.NetworkType.MOBILE) {
                        checkProgram.onLoadComplete(checkResult);
                        App.showAlertDialog(this, getSupportFragmentManager(), "",
                                getString(com.alticast.viettelottcommons.R.string.cannotWatchVTVCab), null);
                        return;
                    }
                    vod.setPurchaseId(checkWatchablePackage);
                }
                vod.setPurchased(true);
                checkProgram.onCanWatch();
                checkResult.setSuccess(true);
                checkProgram.onLoadComplete(checkResult);
                return;
            }

            listPackage = vod.getPurchaseableProducts(listPackage);
            if (listPackage != null && listPackage.isEmpty())

            {
                listPackage = null;
            }


            checkProgram.onLoadComplete(checkResult);
            if (networkType == NetworkUtil.NetworkType.MOBILE) {
                App.showAlertDialog(this, getSupportFragmentManager(), "", getString(R.string.cannotWatchVTVCab), null);
                return;
            }

            showPurchaseProcress(vod, singleProduct, listPackage, vod, "vod", new PurchaseSuccess() {
                        @Override
                        public void onPurchaseSuccess() {
                            vod.setPurchased(true);
                            checkProgram.onCanWatch();
                        }

                        @Override
                        public void onPurchaseCancel() {

                        }
                    }
            );
        } else {

//            boolean isHasWifiPackage = vod.checkSystemProduct();
            ArrayList<Product> listSystemProduct = vod.checkSystemProduct();

            if (Vod.isNetworkSystemPackagePlayable(listSystemProduct, networkType)) {
                vod.setPurchased(true);
                checkProgram.onCanWatch();
                checkResult.setSuccess(true);
                checkProgram.onLoadComplete(checkResult);
            } else {


                String checkWatchablePackage = checkWatchablePackage(listPackage, false, networkType, false);
                if (checkWatchablePackage != null) {
                    if (!checkWatchablePackage.equals(Product.FREE)) {
                        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3 && networkType == NetworkUtil.NetworkType.MOBILE) {
                            checkProgram.onLoadComplete(checkResult);
                            App.showAlertDialog(this, getSupportFragmentManager(), "",
                                    getString(com.alticast.viettelottcommons.R.string.cannotWatchVTVCab), null);
                            return;
                        }
                        vod.setPurchaseId(checkWatchablePackage);
                    }
                    vod.setPurchased(true);
                    checkProgram.onCanWatch();
                    checkResult.setSuccess(true);
                    checkProgram.onLoadComplete(checkResult);
                    return;
                } else {
                    listPackage = vod.getPurchaseableProducts(listPackage);
                    if (listPackage != null && listPackage.isEmpty()) {
                        listPackage = null;
                    }


                    checkProgram.onLoadComplete(checkResult);


                    // RVOD
                    if (WindmillConfiguration.isEnableRVOD) {
                        showPurchaseProcress(vod, singleProduct, listPackage, vod, "vod", new PurchaseSuccess() {
                            @Override
                            public void onPurchaseSuccess() {
//                                vod.setPurchased(true);
//                                checkProgram.onCanWatch();
                                checkMyAccount(checkProgram, vod);
                            }

                            @Override
                            public void onPurchaseCancel() {

                            }
                        });
                    } else {
                        showPurchaseProcress(vod, null, listPackage, vod, "vod", new PurchaseSuccess() {
                            @Override
                            public void onPurchaseSuccess() {
//                                vod.setPurchased(true);
//                                checkProgram.onCanWatch();
                                checkMyAccount(checkProgram, vod);
                            }

                            @Override
                            public void onPurchaseCancel() {

                            }
                        });
                    }

//                    showPurchaseProcress(null, listPackage, vod, "vod", new PurchaseSuccess() {
//                        @Override
//                        public void onPurchaseSuccess() {
//                            checkProgram.onCanWatch();
//                        }
//                    });
                }
            }
        }

    }

    public void checkMyAccount(final CheckProgram checkProgram, final Vod vod) {
        FrontEndLoader.getInstance().getMyAccount(new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                if (obj == null) {
                    if(HandheldAuthorization.getInstance().isLogIn()) {
                        vod.setPurchased(true);
                        checkProgram.onCanWatch();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if(HandheldAuthorization.getInstance().isLogIn()) {
                    vod.setPurchased(true);
                    checkProgram.onCanWatch();
                }
            }

            @Override
            public void onError(ApiError error) {
                if(HandheldAuthorization.getInstance().isLogIn()) {
                    vod.setPurchased(true);
                    checkProgram.onCanWatch();
                }
            }
        }, false, this, false);
    }

    public int getWidthView(View v) {
        int width = v.getWidth();

        if (width == 0) {
            v.measure(0, 0);
            width = v.getMeasuredWidth();
        }

        return width;
    }

    public int getHeightView(View v) {
        int height = v.getHeight();

        if (height == 0) {
            v.measure(0, 0);
            height = v.getMeasuredHeight();
        }

        return height;
    }

    public int checkAvailable(Vod vod, boolean isShowAlert) {
        if (vod == null) {
            return UNWATCHABLE_NOT_EXIST;
        }

        if (NetworkUtil.checkNetwork(this) == NetworkUtil.NetworkType.DISCONNECT) {
            if (isShowAlert)
                App.showAlertDialogNetwork(this, getSupportFragmentManager(), null);
            return UNWATCHABLE_NOT_NETWORK;
        }

        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL1) {
            if (isShowAlert)
                showLoginPairingDialog(false);
            return UNWATCHABLE_NOT_LOGIN;
        }

        Product singleProduct = vod.getSingleProduct();
        if (singleProduct == null) {
            if (isShowAlert)
                App.showAlertDialog(this, getSupportFragmentManager(), "", getString(R.string.cannotWatchContent), new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
            return UNWATCHABLE_NOT_EXIST;
        }

        if (singleProduct.isSTBCclass()) {
            if (isShowAlert)
                App.showAlertDialog(this, getSupportFragmentManager(), "", getString(R.string.cannotWatchContentOnThisDevice), new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
            return UNWATCHABLE_NOT_EXIST;
        }

        NetworkUtil.NetworkType networkType = NetworkUtil.checkNetwork(this);

        String checkWatchSingle = checkWatchableSingle(vod, singleProduct, networkType);

        if (checkWatchSingle != null) {
            if (!checkWatchSingle.equals(Product.FREE)) {
                return WATCHABLE_CAN_PLAY_PURCHASED;
            }
            return WATCHABLE_CAN_PLAY_FREE;
        }

        ArrayList<Product> listPackage = vod.getHandheldAvailableProducts();
        ArrayList<Product> listSVOD = getSVODProduct(listPackage);
        if (listSVOD != null) {
            String checkWatchablePackage = checkWatchablePackage(listSVOD, listPackage);
            if (checkWatchablePackage != null) {
                if (!checkWatchablePackage.equals(Product.FREE)) {
                    return WATCHABLE_CAN_PLAY_PURCHASED;
                }
                return WATCHABLE_CAN_PLAY_FREE;
            } else {
                return WATCHABLE_NEED_PURCHASE;

            }
        }

        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {

            String checkWatchablePackage = checkWatchablePackage(listPackage, false, NetworkUtil.NetworkType.WIFI, true);
            if (checkWatchablePackage != null) {
                if (!checkWatchablePackage.equals(Product.FREE)) {
                    return WATCHABLE_CAN_PLAY_PURCHASED;
                }
                return WATCHABLE_CAN_PLAY_FREE;
            } else {
                return WATCHABLE_NEED_PURCHASE;
            }

        } else {

//            boolean isHasWifiPackage = vod.checkSystemProduct();
            ArrayList<Product> listSystemProduct = vod.checkSystemProduct();

            if (Vod.isNetworkSystemPackagePlayable(listSystemProduct, networkType)) {
                return WATCHABLE_CAN_PLAY_PURCHASED;
            } else {

//                listPackage = vod.getPurchaseableProducts(listPackage);
                String checkWatchablePackage = checkWatchablePackage(listPackage, false, networkType, false);
                if (checkWatchablePackage != null) {
                    if (!checkWatchablePackage.equals(Product.FREE)) {
                        return WATCHABLE_CAN_PLAY_PURCHASED;
                    }
                    return WATCHABLE_CAN_PLAY_FREE;
                } else {
                    return WATCHABLE_NEED_PURCHASE;
                }
            }
        }
    }

//    public void checkSchedule(final Schedule schedule, final CheckProgram checkProgram) {
//        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL1) {
//            showLoginPairingDialog(false);
//            return;
//        }
//
//
//        final ChannelProduct channelProduct = ChannelManager.getInstance().getChannel(schedule.getChannel().getId());
//
////        getChannelProductInfo(channelProduct, checkProgram);
//
//        checkChannelProduct(channelProduct, checkProgram);
//    }

    public void getChannelProductInfo(final boolean isLive, final ChannelProduct channelProduct,
                                      final CheckProgram checkProgram) {

        final CheckResult checkResult = new CheckResult();

//        checkChannelProduct(channelProduct, checkProgram);
        if (channelProduct == null) {
            App.showAlertDialog(this, getSupportFragmentManager(), "", getString(R.string.cannotWatchContent), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    checkProgram.onLoadComplete(checkResult);
                }
            });
            isProcessingPurchase = false;
            return;
        }
        if (channelProduct.isFree()) {
            checkResult.setSuccess(true);
            checkProgram.onLoadComplete(checkResult);
            checkProgram.onCanWatch();
            isProcessingPurchase = false;
            return;
        }
//        showProgress();
        ChannelLoader.getInstance().getChannelProduct(isLive, channelProduct.getChannel().getId(), new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                hideProgress();
                ChannelProduct channelProduct1 = (ChannelProduct) obj;
//                checkProgram.onLoadComplete();
                checkChannelProduct(isLive, channelProduct1, checkProgram);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                hideProgress();
//                App.showAlertDialogNetwork(GlobalActivity.this, getSupportFragmentManager(), null);
                isProcessingPurchase = false;
                checkProgram.onLoadComplete(checkResult);
            }

            @Override
            public void onError(ApiError error) {
                hideProgress();
                App.showAlertDialog(GlobalActivity.this, getSupportFragmentManager(), error);
                isProcessingPurchase = false;
                checkProgram.onLoadComplete(checkResult);
            }
        });

    }

    public void checkChannelProduct(final boolean isLive, ChannelProduct channelProduct,
                                    final CheckProgram checkProgram) {

        final CheckResult checkResult = new CheckResult();

        ArrayList<Product> listPackage = null;
        ArrayList<Product> listSVOD = null;

        if (isLive) {
            listPackage = channelProduct.getHandheldAvailableProducts();
            listSVOD = getSVODProduct(listPackage);
        } else {
            if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
                listPackage = channelProduct.getHandheldAvailableProducts();
            } else {
                listPackage = channelProduct.getProduct();
            }
        }

        if (listSVOD != null) {
            String checkWatchablePackage = checkWatchablePackage(listSVOD, listPackage);
            if (checkWatchablePackage != null) {
                checkResult.setSuccess(true);
                checkProgram.onLoadComplete(checkResult);
                checkProgram.onCanWatch();
            } else {

                showPurchaseProcress(null, null, listSVOD, channelProduct, "channel", new PurchaseSuccess() {
                    @Override
                    public void onPurchaseSuccess() {
                        checkResult.setSuccess(true);
                        checkProgram.onLoadComplete(checkResult);
                        checkProgram.onCanWatch();
                    }

                    @Override
                    public void onPurchaseCancel() {
                        checkProgram.onLoadComplete(checkResult);
                    }
                });
            }
            return;
        }

        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {

            if (NetworkUtil.checkNetwork(this) == NetworkUtil.NetworkType.MOBILE) {
                App.showAlertDialog(this, getSupportFragmentManager(), "", getString(R.string.cannotWatchVTVCab), null);
                return;
            }

            String checkWatchablePackage = checkWatchablePackage(listPackage, false, NetworkUtil.NetworkType.WIFI, true);
            if (checkWatchablePackage != null) {
                checkResult.setSuccess(true);
                checkProgram.onLoadComplete(checkResult);
                checkProgram.onCanWatch();
                return;
            }

            showPurchaseProcress(null, null, listPackage, channelProduct, "channel", new PurchaseSuccess() {
                @Override
                public void onPurchaseSuccess() {
                    checkResult.setSuccess(true);
                    checkProgram.onLoadComplete(checkResult);
                    checkProgram.onCanWatch();
                }

                @Override
                public void onPurchaseCancel() {
                    checkProgram.onLoadComplete(checkResult);
                }
            });
        } else {
            NetworkUtil.NetworkType networkType = NetworkUtil.checkNetwork(GlobalActivity.this);

            ArrayList<Product> systemProduct = channelProduct.checkSystemProduct();
            if (ChannelProduct.isNetworkSystemPackagePlayable(systemProduct, networkType, channelProduct.getChannel().isVTVCabChannel())) {
                checkResult.setSuccess(true);
                checkProgram.onLoadComplete(checkResult);
                checkProgram.onCanWatch();
            } else {
                String checkWatchablePackage = checkWatchablePackage(listPackage, systemProduct != null, networkType, channelProduct.getChannel().isVTVCabChannel());
                if (checkWatchablePackage != null) {
                    checkResult.setSuccess(true);
                    checkProgram.onLoadComplete(checkResult);
                    checkProgram.onCanWatch();
                    return;
                } else {
                    listPackage = channelProduct.getPurchaseableProducts(listPackage);
                    if (listPackage != null && listPackage.isEmpty()) {
                        listPackage = null;
                    }
//                    checkProgram.onLoadComplete();
                    showPurchaseProcress(null, null, listPackage, channelProduct, "channel", new PurchaseSuccess() {
                        @Override
                        public void onPurchaseSuccess() {
                            checkResult.setSuccess(true);
                            checkProgram.onLoadComplete(checkResult);
                            checkProgram.onCanWatch();
                        }

                        @Override
                        public void onPurchaseCancel() {
                            checkProgram.onLoadComplete(checkResult);
                        }
                    });
                }
            }
        }
    }

    public int checkChannelProduct(final boolean isLive, ChannelProduct channelProduct) {
        ArrayList<Product> listPackage = null;
        ArrayList<Product> listSVOD = null;

        if (isLive) {
            listPackage = channelProduct.getHandheldAvailableProducts();
            listSVOD = getSVODProduct(listPackage);
        } else {
            if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
                listPackage = channelProduct.getHandheldAvailableProducts();
            } else {
                listPackage = channelProduct.getProduct();
            }
        }

        if (listSVOD != null) {
            String checkWatchablePackage = checkWatchablePackage(listSVOD, listPackage);
            if (checkWatchablePackage != null) {
                if (checkWatchablePackage.equals(Product.FREE)) {
                    return WATCHABLE_CAN_PLAY_FREE;
                } else {
                    return WATCHABLE_CAN_PLAY_PURCHASED;
                }

            } else {
                return WATCHABLE_NEED_PURCHASE;
            }
        }

        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
            String checkWatchablePackage = checkWatchablePackage(listPackage, false, NetworkUtil.NetworkType.WIFI, true);
            if (checkWatchablePackage != null) {
                if (checkWatchablePackage.equals(Product.FREE)) {
                    return WATCHABLE_CAN_PLAY_FREE;
                } else {
                    return WATCHABLE_CAN_PLAY_PURCHASED;
                }

            } else {
                return WATCHABLE_NEED_PURCHASE;
            }

        } else {
            NetworkUtil.NetworkType networkType = NetworkUtil.checkNetwork(GlobalActivity.this);
            ArrayList<Product> systemProduct = channelProduct.checkSystemProduct();

            if (ChannelProduct.isNetworkSystemPackagePlayable(systemProduct, networkType, channelProduct.getChannel().isVTVCabChannel())) {
                return WATCHABLE_CAN_PLAY_FREE;
            } else {

//                listPackage = channelProduct.getPurchaseableProducts(listPackage);

                String checkWatchablePackage = checkWatchablePackage(listPackage, systemProduct != null, networkType, channelProduct.getChannel().isVTVCabChannel());
                if (checkWatchablePackage != null) {
                    if (checkWatchablePackage.equals(Product.FREE)) {
                        return WATCHABLE_CAN_PLAY_FREE;
                    } else {
                        return WATCHABLE_CAN_PLAY_PURCHASED;
                    }

                } else {
                    return WATCHABLE_NEED_PURCHASE;
                }
            }
        }
    }

    public interface CheckProgram {
        public void onCanWatch();

        public void onLoadComplete(CheckResult checkResult);
//        public void onMustPurchase(Product singleProduct, ArrayList<Product> listPackage);
    }

    public class CheckResult {
        public boolean isSuccess;

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }
    }

    public void showPurchaseProcress(final Vod vod, Product singleProduct, Object listPackage,
                                     final Object content, final String type, final PurchaseSuccess purchaseSuccess) {

        hideProgress();

        if (listPackage != null) {
            ArrayList<Product> listProduct = (ArrayList<Product>) listPackage;
            ArrayList<Product> svodProduct = getSVODProduct(listProduct);
            if (svodProduct != null) {
                listProduct.clear();
                listProduct.addAll(svodProduct);
                listPackage = listProduct;
            }
        }

        if (WindmillConfiguration.type.equals("phone") && getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

//        if (singleProduct != null && singleProduct.isPurchaseable() && AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3 && listPackage != null) {
        if (singleProduct != null && singleProduct.isPurchaseable() && listPackage != null) {
            ArrayList<Product> listProduct = (ArrayList<Product>) listPackage;
            ArrayList<Product> listProductDisplay = ProductUtils.filterFullPurchaseableProduct(listProduct);
            purchaseOptionDialog = new PurchaseOptionDialog();
            Bundle args = new Bundle();
            args.putParcelable(PurchaseOptionDialog.SINGLE_PRODUCT, singleProduct);
            args.putParcelableArrayList(PurchaseOptionDialog.PACKAGE_PRODUCT, listProductDisplay);

            purchaseOptionDialog.setArguments(args);
            purchaseOptionDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = v.getId();
                    if (i == R.id.btnCancel) {
//                        removeOverlayFragment();
                        purchaseSuccess.onPurchaseCancel();
                    } else if (i == R.id.btnPurchase) {
                        PurchaseOption purchaseOption = purchaseOptionDialog.getSelectedPurchaseOption();

                        purchaseCommonHelper = new PurchaseCommonHelper(GlobalActivity.this, getSupportFragmentManager(), purchaseOption.getObject(), type);
                        purchaseCommonHelper.setPurchaseCallback(new PurchaseCommonHelper.PackageSelectCallback() {
                            @Override
                            public void onSelect(final Product product, RentalPeriods rentalPeriod, boolean isAutoRenewal) {
                                if (product != null || rentalPeriod != null) {
                                    purchaseProduct(product, content, rentalPeriod, isAutoRenewal, new PurchaseSuccess() {
                                        @Override
                                        public void onPurchaseSuccess() {
                                            if (vod != null) {
                                                vod.setPurchaseId(product.getPurchaseId());
                                            }
                                            purchaseSuccess.onPurchaseSuccess();
                                        }

                                        @Override
                                        public void onPurchaseCancel() {
                                            purchaseSuccess.onPurchaseCancel();
//                                            purchaseCommonHelper.notifyResult();
                                        }
                                    });
                                } else {
                                    new Handler().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            purchaseOptionDialog.show(getSupportFragmentManager(), PurchaseOptionDialog.CLASS_NAME);
                                        }
                                    });
                                }
                            }
                        });
                        purchaseCommonHelper.purchase();


                    }
                    isProcessingPurchase = false;
                    purchaseOptionDialog.isDismiss = true;
                    purchaseOptionDialog.dismiss();
                }
            });


            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    purchaseOptionDialog.show(getSupportFragmentManager(), PurchaseOptionDialog.CLASS_NAME);
                }
            });

            return;
        }


        Object obj = null;
        if (listPackage != null) {
            ArrayList<Product> listProduct = (ArrayList<Product>) listPackage;
            ArrayList<Product> listDisplay = ProductUtils.filterFullPurchaseableProduct(listProduct);
            obj = listDisplay;
//            obj = listPackage;
        } else if (singleProduct != null && singleProduct.isPurchaseable()) {
            obj = singleProduct;
        } else {
            App.showAlertDialog(this, getSupportFragmentManager(), "", getString(R.string.cannotWatchContent), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isProcessingPurchase = false;
                }
            });
            return;
        }

        purchaseCommonHelper = new PurchaseCommonHelper(this, getSupportFragmentManager(), obj, type);
        purchaseCommonHelper.setPurchaseCallback(new PurchaseCommonHelper.PackageSelectCallback() {
            @Override
            public void onSelect(final Product product, RentalPeriods rentalPeriod, boolean isAutoRenewal) {
                isProcessingPurchase = false;
                if (product != null || rentalPeriod != null) {
                    purchaseProduct(product, content, rentalPeriod, isAutoRenewal, new PurchaseSuccess() {
                        @Override
                        public void onPurchaseSuccess() {

                            if(!HandheldAuthorization.getInstance().isLogIn()) {
                                purchaseSuccess.onPurchaseCancel();
                                return;
                            }

                            if (vod != null) {
                                vod.setPurchaseId(product.getPurchaseId());
                            }
                            purchaseSuccess.onPurchaseSuccess();
                        }

                        @Override
                        public void onPurchaseCancel() {
                            purchaseSuccess.onPurchaseCancel();
                        }
                    });
                } else {
                    purchaseSuccess.onPurchaseCancel();
                }

            }
        });
        purchaseCommonHelper.purchase();
    }

    public void doPurchaseProduct(final boolean isUpdate, ArrayList<Product> listProduct, final PurchaseSuccess purchaseSuccess) {
        purchaseCommonHelper = new PurchaseCommonHelper(this, getSupportFragmentManager(), listProduct, "VOD");
        purchaseCommonHelper.setPurchaseCallback(new PurchaseCommonHelper.PackageSelectCallback() {
            @Override
            public void onSelect(final Product product,final RentalPeriods rentalPeriod, boolean isAutoRenewal) {
                isProcessingPurchase = false;
                if (product != null || rentalPeriod != null) {
                    if (!isUpdate) {
                        purchaseProduct(product, null, rentalPeriod, isAutoRenewal, new PurchaseSuccess() {
                            @Override
                            public void onPurchaseSuccess() {
                                purchaseSuccess.onPurchaseSuccess();

                            }

                            @Override
                            public void onPurchaseCancel() {
                                purchaseSuccess.onPurchaseCancel();
                            }
                        });
                    } else {

                        final FragmentManager mFragmentManager = getSupportFragmentManager();

                        DialogFragment oldDialog = (DialogFragment) mFragmentManager.findFragmentByTag(PurchaseDataPackageConfirmDialogPhase2.CLASS_NAME);
                        if (oldDialog != null) oldDialog.dismiss();

                        final PurchaseDataPackageConfirmDialogPhase2 dialog = new PurchaseDataPackageConfirmDialogPhase2();
                        dialog.setSrc(product, rentalPeriod);
                        dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (v.getId() == R.id.btnConfirm) {
                                    showProgress();
                                    PurchaseLoader.getInstance().purchaseUpsell(product.getId(), rentalPeriod.getPeriod(), rentalPeriod.getUnit(), "", null, new WindmillCallback() {
                                        @Override
                                        public void onSuccess(Object obj) {
                                            hideProgress();
                                            purchaseSuccess.onPurchaseSuccess();
                                        }

                                        @Override
                                        public void onFailure(Call call, Throwable t) {
                                            hideProgress();
                                            App.showAlertDialogNetwork(GlobalActivity.this, getSupportFragmentManager(), null);
                                            purchaseSuccess.onPurchaseCancel();
                                        }

                                        @Override
                                        public void onError(ApiError error) {
                                            hideProgress();
                                            App.showAlertDialog(GlobalActivity.this, getSupportFragmentManager(), error, null);
                                            purchaseSuccess.onPurchaseCancel();
                                        }
                                    });
                                } else if (v.getId() == R.id.btnCancel) {
                                    purchaseSuccess.onPurchaseCancel();
                                }

                            }
                        });

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show(mFragmentManager, PurchaseDataPackageConfirmDialogPhase2.CLASS_NAME);
                            }
                        });


                    }
                } else {
                    purchaseSuccess.onPurchaseCancel();
                }

            }
        });
        purchaseCommonHelper.purchase();
    }

    public void doPurchaseProduct(Product product, final PurchaseSuccess purchaseSuccess) {

//        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2 && product.getRental_periods() != null && product.getRental_periods().size() == 1) {
//            RentalPeriods rentalPeriods = product.getRental_periods().get(0);
//            purchaseProduct(product, null, rentalPeriods, true, new PurchaseSuccess() {
//                @Override
//                public void onPurchaseSuccess() {
//                    purchaseSuccess.onPurchaseSuccess();
//                }
//
//                @Override
//                public void onPurchaseCancel() {
//                    purchaseSuccess.onPurchaseCancel();
//                }
//            });
//            return;
//        }

        purchaseCommonHelper = new PurchaseCommonHelper(this, getSupportFragmentManager(), product, "");
        purchaseCommonHelper.setPurchaseCallback(new PurchaseCommonHelper.PackageSelectCallback() {
            @Override
            public void onSelect(final Product product, RentalPeriods rentalPeriod, boolean isAutoRenewal) {
                isProcessingPurchase = false;
                if (product != null || rentalPeriod != null) {
                    purchaseProduct(product, null, rentalPeriod, isAutoRenewal, new PurchaseSuccess() {
                        @Override
                        public void onPurchaseSuccess() {
                            purchaseSuccess.onPurchaseSuccess();
                        }

                        @Override
                        public void onPurchaseCancel() {
                            purchaseSuccess.onPurchaseCancel();
                        }
                    });
                } else {
                    purchaseSuccess.onPurchaseCancel();
                }

            }
        });
        purchaseCommonHelper.purchase();
    }

    public void doPurchaseProduct(RentalPeriods rentalPeriods, Product product, final PurchaseSuccess purchaseSuccess) {

        purchaseCommonHelper = new PurchaseCommonHelper(this, getSupportFragmentManager(), product, "");
        purchaseCommonHelper.setInitRental(rentalPeriods);
        purchaseCommonHelper.setPurchaseCallback(new PurchaseCommonHelper.PackageSelectCallback() {
            @Override
            public void onSelect(final Product product, RentalPeriods rentalPeriod, boolean isAutoRenewal) {
                isProcessingPurchase = false;
                if (product != null || rentalPeriod != null) {
                    purchaseProduct(product, null, rentalPeriod, isAutoRenewal, new PurchaseSuccess() {
                        @Override
                        public void onPurchaseSuccess() {
                            purchaseSuccess.onPurchaseSuccess();
                        }

                        @Override
                        public void onPurchaseCancel() {
                            purchaseSuccess.onPurchaseCancel();
                        }
                    });
                } else {
                    purchaseSuccess.onPurchaseCancel();
                }

            }
        });
        purchaseCommonHelper.purchase();
    }

    public void processNotify(Bundle bundle) {
        if (bundle != null) {
            Logger.print(this, "Navi Has notify");

            String key = bundle.getString(GlobalKey.NotifiKey.KEY);
            final String id = bundle.getString(GlobalKey.NotifiKey.ID);
            switch (key) {
                case "channel":
                case "CHANNEL":

                    if (ChannelManager.getInstance().checkData()) {
                        processNotifyChannel(id);
                    } else {
                        ChannelManager.getInstance().getChannel(new WindmillCallback() {
                            @Override
                            public void onSuccess(Object obj) {
                                processNotifyChannel(id);
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                App.showAlertDialog(GlobalActivity.this, getSupportFragmentManager(), "", getString(com.alticast.viettelottcommons.R.string.cannotWatchContent), null);

                            }

                            @Override
                            public void onError(ApiError error) {
                                App.showAlertDialog(GlobalActivity.this, getSupportFragmentManager(), "", getString(com.alticast.viettelottcommons.R.string.cannotWatchContent), null);
                            }
                        });
                    }

                    break;
                case "program":
                case "VODS":
                    processNotifyVod(id);
                    break;
                case "category":
                    processNotifyCategory(id);
                    break;
                case "DRAMA":
                    processNotifyVod(id);
                    break;
            }

        } else {
            Logger.print(this, "Navi Has no notify");
        }
    }

    private boolean checkEncryption(Vod vod) {
        ArrayList<Product> listProduct = vod.getProduct();
        for (Product product : listProduct) {
            ArrayList<Location> listLocation = product.getLocations();
            if (listLocation == null) continue;
            for (Location location : listLocation) {
                if (location.isEncryption()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void processCastisPromotion(Bundle bundle) {
        if (bundle != null) {
            Logger.print(this, "Navi Has notify");

            String key = bundle.getString(GlobalKey.NotifiKey.KEY);
            final String id = bundle.getString(GlobalKey.NotifiKey.ID);
            switch (key) {
                case "channel":
                case "CHANNEL":

                    if (ChannelManager.getInstance().checkData()) {
                        processCastisPromotionChannel(id);
                    } else {
                        ChannelManager.getInstance().getChannel(new WindmillCallback() {
                            @Override
                            public void onSuccess(Object obj) {
                                processCastisPromotionChannel(id);
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                App.showAlertDialog(GlobalActivity.this, getSupportFragmentManager(), "", getString(com.alticast.viettelottcommons.R.string.cannotWatchContent), null);

                            }

                            @Override
                            public void onError(ApiError error) {
                                App.showAlertDialog(GlobalActivity.this, getSupportFragmentManager(), "", getString(com.alticast.viettelottcommons.R.string.cannotWatchContent), null);
                            }
                        });
                    }

                    break;
                case "contentDetail":
                    processCastisPromotionVod(id);
                    break;
                case "menu":

                    processCastisPromotionMenu(id);
                    break;
                case Placement.TYPE_url:

                    processCastisPromotionUrl(id);
                    break;
                case Placement.TYPE_image:
                    processCastisPromotionImage(id);
                    break;
            }

        } else {
            Logger.print(this, "Navi Has no notify");
        }
    }

    private void processCastisPromotionImage(String id) {
        final PromotionImageDialog dialog = new PromotionImageDialog();
        Bundle bundle = new Bundle();
        bundle.putString(PromotionImageDialog.PARAM_CONTENT_URL, id);
        dialog.setArguments(bundle);
        (new Handler()).post(new Runnable() {
            @Override
            public void run() {
                dialog.show(getSupportFragmentManager(), PromotionImageDialog.CLASS_NAME);
            }
        });
    }

    public void closeOpenedDialogs(FragmentActivity activity) {
        List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof DialogFragment) {
                    ((DialogFragment) fragment).dismiss();
                }
            }
        }
    }

    public void processCastisPromotionUrl(String clickThrough) {
        if (clickThrough == null)
            return;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickThrough));
        startActivity(browserIntent);
    }

    public void processCastisPromotionMenu(String pathId) {
        ArrayList<Menu> list = MenuManager.getInstance().getMenuList();
        for (Menu menu : list) {
            if (menu.getPath_id().contains(pathId)) {
                goToMenu(menu);
                return;
            }
        }

    }

    public void goToMenu(final Menu menu) {

    }

    public void processCastisPromotionChannel(String serviceId) {
        if (checkOverlayFragment() != null) {
            removeOverlayFragment();
        }
        ChannelProduct channelProduct = ChannelManager.getInstance().getChannelThroughServiceId(serviceId);
        if (channelProduct == null) return;
        checkAndGoToChannelPlayback(channelProduct, null);
    }

    public void processCastisPromotionVod(final String programId) {
        ArrayList<String> listId = new ArrayList<>();
        listId.add(programId);
        showProgress();
        ProgramLoader.getInstance().getPrograms(listId, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                hideProgress();
                if (obj == null) return;
                ProgramList programList = (ProgramList) obj;
                if (programList == null || programList.getData() == null || programList.getData().size() == 0)
                    return;

                final Vod vod = programList.getData().get(0);
                EntryPathLogImpl.getInstance().updateLog(PurchasePathway.ENTRY_GENERAL, programId);
                Path path = new Path(PurchasePathway.ENTRY_GENERAL, EntryPathLogImpl.getInstance().getPurchaseMenuString(), vod.getProgram().getId());
                vod.setPath(path);

                if (checkOverlayFragment() != null) {
                    removeOverlayFragment();
                }
                processVod(vod);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                hideProgress();
            }

            @Override
            public void onError(ApiError error) {
                hideProgress();
            }
        });
    }

    public void processNotifyChannel(String id) {
        if (checkOverlayFragment() != null) {
            removeOverlayFragment();
        }
        checkAndGoToChannelPlayback(ChannelManager.getInstance().getChannel(id), null);
    }

    public void processNotifyVod(final String programId) {
        showProgress();
        ProgramLoader.getInstance().getProgram(programId, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                hideProgress();

                final Vod vod = (Vod) obj;

                if (vod != null && ChromeCastManager.getInstance().isChromeCastState() && checkEncryption(vod)) {
                    showEncryptionPopUp();
                    return;
                }

                EntryPathLogImpl.getInstance().updateLog(PurchasePathway.ENTRY_GENERAL, programId);
//                    goToPlaybackFromVod(vod);
                Path path = new Path(PurchasePathway.ENTRY_GENERAL, EntryPathLogImpl.getInstance().getPurchaseMenuString(), vod.getProgram().getId());
                vod.setPath(path);

                if (checkOverlayFragment() != null) {
                    removeOverlayFragment();
                }
                processVod(vod);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                hideProgress();
            }

            @Override
            public void onError(ApiError error) {
                hideProgress();
                App.showAlertDialog(GlobalActivity.this, getSupportFragmentManager(), error);
            }
        });
    }

    public void processNotifyCategory(String categoryId) {
        showProgress();
        ProgramLoader.getInstance().getCategoryVod(categoryId, 0, 1, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                hideProgress();
                if (obj == null) {
                    App.showAlertDialog(GlobalActivity.this, getSupportFragmentManager(), "", getString(R.string.cannotWatchContent), new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                    return;
                }

                ProgramList programList = (ProgramList) obj;
                if (programList != null && programList.getData() != null && !programList.getData().isEmpty()) {
                    Vod vod = programList.getData().get(0);

//                    for(Vod v : programList.getData()) {
//                        if(AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2 && v.getSingleProduct().isHandheldCclass()) {
//                            vod = v;
//                        } else if(AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3 && v.getSingleProduct().isHandheldSTBCclass()) {
//                            vod = v;
//                        }
//                    }
//
//                    if(vod == null) {
//                        App.showAlertDialog(GlobalActivity.this, getSupportFragmentManager(), "", getString(R.string.cannotWatchContent), new DialogInterface.OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogInterface dialog) {
//
//                            }
//                        });
//                        return;
//                    }
                    showProgress();
                    ProgramLoader.getInstance().getProgram(vod.getProgram().getId(), new WindmillCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            hideProgress();
                            Vod vod = (Vod) obj;
                            EntryPathLogImpl.getInstance().updateLog(PurchasePathway.ENTRY_GENERAL, vod.getProgram().getId());
                            Path path = new Path(PurchasePathway.ENTRY_GENERAL, EntryPathLogImpl.getInstance().getPurchaseMenuString(), vod.getProgram().getId());
                            vod.setPath(path);

                            if (checkOverlayFragment() != null) {
                                removeOverlayFragment();
                            }
                            goToPlaybackFromVod(vod);
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            hideProgress();
                        }

                        @Override
                        public void onError(ApiError error) {
                            hideProgress();
                        }
                    });


                } else {
                    App.showAlertDialog(GlobalActivity.this, getSupportFragmentManager(), "", getString(R.string.cannotWatchContent), new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                hideProgress();
            }

            @Override
            public void onError(ApiError error) {
                hideProgress();
            }
        });
    }

    public Fragment checkOverlayFragment() {
        return null;
    }

    public void removeOverlayFragment() {

    }


    public interface PurchaseSuccess {
        public void onPurchaseSuccess();

        public void onPurchaseCancel();
    }

    private void purchaseProduct(final Product product, final Object obj, RentalPeriods
            rentalPeriod,
                                 boolean isAutoRenewal, final PurchaseSuccess purchaseSuccess) {
        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL1) {
            showLoginPairingDialog(false);
            return;
        }
        //TODO change in app purchase
        if (obj instanceof Vod) {

            final Vod vod = (Vod) obj;
            if (product != null) {
                if (rentalPeriod != null) {
                    String productCategory = product.isFpackage() ? "full" : "vod";
                    new RentalPeriodProductPurchaseHelper(this, getSupportFragmentManager(), vod.getProgram(), null,
                            product, RentalPeriodProductPurchaseHelper.TYPE_PROGRAM, rentalPeriod, productCategory, isAutoRenewal).setPurchaseCallback(
                            new RentalPeriodProductPurchaseHelper.PurchaseCallback() {
                                @Override
                                public void onSuccess(Object obj) {
                                    if(!HandheldAuthorization.getInstance().isLogIn()) {
                                        purchaseSuccess.onPurchaseCancel();
                                        return;
                                    }
                                    purchaseSuccess.onPurchaseSuccess();
                                }

                                @Override
                                public void onCancel() {
                                    purchaseSuccess.onPurchaseCancel();
                                }
                            }).purchase();
                } else {
                    new ProgramPurchaseHelper(this, getSupportFragmentManager(), vod.getProgram(), product).setPurchaseCallback(
                            new ProgramPurchaseHelper.PurchaseCallback() {
                                @Override
                                public void onSuccess(Program program) {
                                    if(!HandheldAuthorization.getInstance().isLogIn()) {
                                        purchaseSuccess.onPurchaseCancel();
                                        return;
                                    }
                                    purchaseSuccess.onPurchaseSuccess();
                                }

                                @Override
                                public void onCancel() {
                                    purchaseSuccess.onPurchaseCancel();
                                }
                            }).purchase();
                }
            }
        } else if (obj instanceof ChannelProduct) {

            ChannelProduct channelProduct = (ChannelProduct) obj;

            if (rentalPeriod != null/*product.isFpackage()*/) {
                //String entryPath = new PathBuilder().setHost(PathBuilder.HOST_CHANNEL).build();
                String productCategory = product.isFpackage() ? "full" : "channel";
                new RentalPeriodProductPurchaseHelper(this, getSupportFragmentManager(), null, channelProduct, product,
                        RentalPeriodProductPurchaseHelper.TYPE_CHANNEL, rentalPeriod, productCategory, isAutoRenewal)
                        .setPurchaseCallback(new RentalPeriodProductPurchaseHelper.PurchaseCallback() {
                            @Override
                            public void onSuccess(Object obj) {
                                if(!HandheldAuthorization.getInstance().isLogIn()) {
                                    purchaseSuccess.onPurchaseCancel();
                                    return;
                                }
                                purchaseSuccess.onPurchaseSuccess();
                            }

                            @Override
                            public void onCancel() {
                                purchaseSuccess.onPurchaseCancel();
                            }
                        }).purchase();
            } else {
                new ChannelPurchaseHelper(this, getSupportFragmentManager(), channelProduct, product)//TODO 확인
                        .setPurchaseCallback(new ChannelPurchaseHelper.PurchaseCallback() {
                            @Override
                            public void onSuccess(ChannelProduct channel) {
                                if(!HandheldAuthorization.getInstance().isLogIn()) {
                                    purchaseSuccess.onPurchaseCancel();
                                    return;
                                }
                                purchaseSuccess.onPurchaseSuccess();
                            }

                            @Override
                            public void onCancel() {
                                purchaseSuccess.onPurchaseCancel();
                            }
                        }).purchase();
            }
        } else {
            if (product != null) {
                if (rentalPeriod != null) {
                    String productCategory = product.isFpackage() ? "full" : "vod";
                    new RentalPeriodProductPurchaseHelper(this, getSupportFragmentManager(), null, null,
                            product, RentalPeriodProductPurchaseHelper.TYPE_PROGRAM, rentalPeriod, productCategory, isAutoRenewal).setPurchaseCallback(
                            new RentalPeriodProductPurchaseHelper.PurchaseCallback() {
                                @Override
                                public void onSuccess(Object obj) {
                                    if(!HandheldAuthorization.getInstance().isLogIn()) {
                                        purchaseSuccess.onPurchaseCancel();
                                        return;
                                    }
                                    purchaseSuccess.onPurchaseSuccess();
                                }

                                @Override
                                public void onCancel() {
                                    purchaseSuccess.onPurchaseCancel();
                                }
                            }).purchase();
                } else {
                    new ProgramPurchaseHelper(this, getSupportFragmentManager(), null, product).setPurchaseCallback(
                            new ProgramPurchaseHelper.PurchaseCallback() {
                                @Override
                                public void onSuccess(Program program) {
                                    if(!HandheldAuthorization.getInstance().isLogIn()) {
                                        purchaseSuccess.onPurchaseCancel();
                                        return;
                                    }
                                    purchaseSuccess.onPurchaseSuccess();
                                }

                                @Override
                                public void onCancel() {
                                    purchaseSuccess.onPurchaseCancel();
                                }
                            }).purchase();
                }
            }
        }
    }

    public static void dismissAllDialogs(FragmentManager manager) {
        List<Fragment> fragments = manager.getFragments();

        if (fragments == null)
            return;

        for (Fragment fragment : fragments) {

            if(fragment == null) {
                continue;
            }
            if (fragment instanceof DialogFragment) {
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismissAllowingStateLoss();
            }

            FragmentManager childFragmentManager = fragment.getChildFragmentManager();
            if (childFragmentManager != null)
                dismissAllDialogs(childFragmentManager);
        }
    }

    public void tuneVod(Vod vod) {
    }

    public void tuneVod(Vod vod, boolean isNoVerticalPlayback) {
    }

    public void tuneVod(Vod vod, boolean isNoVerticalPlayback, boolean isPlaylist) {
    }

    public void tuneVod(Vod vod, int offset) {
    }

    public void goToPlaybackFromCatchup(final Schedule schedule) {

        if (isProcessingPurchase) {
            return;
        }

        isProcessingPurchase = true;

        if (NetworkUtil.checkNetwork(this) == NetworkUtil.NetworkType.DISCONNECT) {
            App.showAlertDialogNetwork(this, getSupportFragmentManager(), null);
            isProcessingPurchase = false;
            return;
        }

        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL1) {
            showLoginPairingDialog(false);
            isProcessingPurchase = false;
            return;
        }

        ChannelProduct channelProduct = ChannelManager.getInstance().getChannel(schedule.getChannel().getId());

        getChannelProductInfo(false, channelProduct, new CheckProgram() {
            @Override
            public void onCanWatch() {
                isProcessingPurchase = false;
                tuneCatchUp(schedule);
            }

            @Override
            public void onLoadComplete(CheckResult checkResult) {

            }
        });

//        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3 || !channelProduct.isEncryption()) {
//            tuneCatchUp(schedule);
//            return;
//        }

//        getChannelProductInfo(channelProduct, new CheckProgram() {
//            @Override
//            public void onCanWatch() {
//                tuneCatchUp(schedule);
//            }
//        });

//        checkChannelProduct(channelProduct, new CheckProgram() {
//            @Override
//            public void onCanWatch() {
//                tuneCatchUp(schedule);
//            }
//        });

//        checkChannelProduct(channelProduct, new CheckProgram() {
//            @Override
//            public void onCanWatch() {
//                tuneCatchUp(schedule);
//            }
//        });
    }


    public void tuneCatchUp(Schedule schedule) {
//        Intent intent = new Intent(this, PlaybackActivity.class);
//        CatchupVodInfo programInfo = new CatchupVodInfo(schedule);
//        intent.putExtra(VodInfo.class.getName(), programInfo);
//        intent.putExtra(TitleBarFragment.RATING, schedule.getRating());
//        intent.putExtra(TitleBarFragment.HD_QUALITY, schedule.isHd());
//        startActivity(intent);
    }

    public void goToPlaybackFromLive(final Schedule schedule) {

        if (isProcessingPurchase) {
            return;
        }

        isProcessingPurchase = true;
        NetworkUtil.NetworkType networkType = NetworkUtil.checkNetwork(this);
        if (networkType == NetworkUtil.NetworkType.DISCONNECT) {
            App.showAlertDialogNetwork(this, getSupportFragmentManager(), null);
            isProcessingPurchase = false;
            return;
        }

        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL1) {
            showLoginPairingDialog(false);
            isProcessingPurchase = false;
            return;
        }

        ChannelProduct channelProduct = ChannelManager.getInstance().getChannel(schedule.getChannel().getId());

        int checkChannelResult = checkChannelProduct(true, channelProduct, networkType, true, null);
        if (checkChannelResult < 0) {
            isProcessingPurchase = false;
            return;
        }
        if (checkChannelResult == WATCHABLE_CAN_PLAY) {
            isProcessingPurchase = false;
            tuneLive(schedule);
//            tuneLiveFragment(schedule);
            return;
        }

        getChannelProductInfo(true, channelProduct, new CheckProgram() {
            @Override
            public void onCanWatch() {
                isProcessingPurchase = false;
                tuneLive(schedule);
//                tuneLiveFragment(schedule);
            }

            @Override
            public void onLoadComplete(CheckResult checkResult) {
                isProcessingPurchase = false;
            }
        });
    }

    public void goToPlaybackFromLive(final String channelId) {

        if (isProcessingPurchase) {
            return;
        }

        isProcessingPurchase = true;
        NetworkUtil.NetworkType networkType = NetworkUtil.checkNetwork(this);
        if (networkType == NetworkUtil.NetworkType.DISCONNECT) {
            App.showAlertDialogNetwork(this, getSupportFragmentManager(), null);
            isProcessingPurchase = false;
            return;
        }

        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL1) {
            showLoginPairingDialog(false);
            isProcessingPurchase = false;
            return;
        }

        ChannelProduct channelProduct = ChannelManager.getInstance().getChannel(channelId);

        int checkChannelResult = checkChannelProduct(true, channelProduct, networkType, true, null);
        if (checkChannelResult < 0) {
            isProcessingPurchase = false;
            return;
        }
        if (checkChannelResult == WATCHABLE_CAN_PLAY) {
            isProcessingPurchase = false;
            tuneLive(channelId);
            return;
        }

        getChannelProductInfo(true, channelProduct, new CheckProgram() {
            @Override
            public void onCanWatch() {
                isProcessingPurchase = false;
                tuneLive(channelId);
            }

            @Override
            public void onLoadComplete(CheckResult checkResult) {
                isProcessingPurchase = false;
            }
        });
    }

    public int checkChannelProduct(boolean isLive, ChannelProduct channelProduct, NetworkUtil.NetworkType networkType, boolean isAlert, DialogInterface.OnDismissListener onDismissListener) {
        if (channelProduct == null) {
            if (isAlert)
                App.showAlertDialog(this, getSupportFragmentManager(), "", getString(R.string.cannotWatchContent), onDismissListener);
            return UNWATCHABLE_NOT_EXIST;
        }

        if (!WindmillConfiguration.isAlacaterVersion) {
            if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
                return WATCHABLE_CAN_PLAY;
            } else {
                return WATCHABLE_NEED_PURCHASE;
            }
        }


        if (!channelProduct.isVisible() && (isLive || AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2)) {
            if (isAlert)
                App.showAlertDialog(this, getSupportFragmentManager(), "", String.format(getString(R.string.cannotWatchInvisible), PlatformLoader.getInstance().getPlatFormValue(PlatformFileds.REGIONAL_PHONE)), onDismissListener);
            return UNWATCHABLE_INVISIBLE;
        } else {
            if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
                if (channelProduct.getChannel() != null && channelProduct.getChannel().isVTVCabChannel()) {
                    if (networkType == NetworkUtil.NetworkType.MOBILE) {
                        if (isAlert)
                            App.showAlertDialog(this, getSupportFragmentManager(), "", getString(R.string.cannotWatchVTVCab), onDismissListener);
                        return UNWATCHABLE_VTVCAB;
                    } else {
                        return isLive ? WATCHABLE_CAN_PLAY : WATCHABLE_NEED_PURCHASE;
                    }
                } else {
                    if (channelProduct.isFree()) {
                        return isLive ? WATCHABLE_CAN_PLAY : WATCHABLE_NEED_PURCHASE;
                    }
                    if (networkType == NetworkUtil.NetworkType.MOBILE) {
                        if (isAlert)
                            App.showAlertDialog(this, getSupportFragmentManager(), "", getString(R.string.cannotWatchVTVCab), onDismissListener);
                        return UNWATCHABLE_VTVCAB;
                    } else {
                        return isLive ? WATCHABLE_CAN_PLAY : WATCHABLE_NEED_PURCHASE;
                    }
//                    return isLive ? WATCHABLE_CAN_PLAY : WATCHABLE_NEED_PURCHASE;
                }
            } else {
                if (channelProduct.isFree()) {
                    return isLive ? WATCHABLE_CAN_PLAY : WATCHABLE_NEED_PURCHASE;
                }
                return WATCHABLE_NEED_PURCHASE;
            }

        }
    }

    public void checkSchedule(final Schedule schedule, final CheckProgram checkProgram) {
        if (isProcessingPurchase) {
            return;
        }

        CheckResult checkResult = new CheckResult();

        isProcessingPurchase = true;

        NetworkUtil.NetworkType networkType = NetworkUtil.checkNetwork(this);
        if (networkType == NetworkUtil.NetworkType.DISCONNECT) {
            App.showAlertDialogNetwork(this, getSupportFragmentManager(), null);
            isProcessingPurchase = false;
            checkProgram.onLoadComplete(checkResult);
            return;
        }

        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL1) {

            showLoginPairingDialog(false);
            isProcessingPurchase = false;
            checkProgram.onLoadComplete(checkResult);
            return;
        }

        ChannelProduct channelProduct = ChannelManager.getInstance().getChannel(schedule.getChannel().getId());

//        if (!WindmillConfiguration.isAlacaterVersion && AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
//            finishAction.onFisnish();
//            tuneLive(channelProduct.getChannel().getId());
//            isProcessingPurchase = false;
//            return;
//        }

        int checkChannelResult = checkChannelProduct(false, channelProduct, networkType, true, null);
        if (checkChannelResult < 0) {
            isProcessingPurchase = false;
            return;
        }
        if (checkChannelResult == WATCHABLE_CAN_PLAY) {
            isProcessingPurchase = false;
            schedule.setPurchased(true);
            checkProgram.onCanWatch();
//            tuneLiveFragment(schedule);
            return;
        }

        getChannelProductInfo(false, channelProduct, new CheckProgram() {
            @Override
            public void onCanWatch() {
                schedule.setPurchased(true);
                checkProgram.onCanWatch();
                isProcessingPurchase = false;
            }

            @Override
            public void onLoadComplete(CheckResult checkResult) {
                checkProgram.onLoadComplete(checkResult);
                isProcessingPurchase = false;
            }
        });
    }

    public void goToPlaybackFromLive(final Schedule schedule, final FinishAction finishAction) {

        if (isProcessingPurchase) {
            return;
        }

        isProcessingPurchase = true;

        NetworkUtil.NetworkType networkType = NetworkUtil.checkNetwork(this);
        if (networkType == NetworkUtil.NetworkType.DISCONNECT) {
            App.showAlertDialogNetwork(this, getSupportFragmentManager(), null);
            isProcessingPurchase = false;
            return;
        }

        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL1) {
            finishAction.onFisnish();
            showLoginPairingDialog(false);
            isProcessingPurchase = false;
            return;
        }

        ChannelProduct channelProduct = ChannelManager.getInstance().getChannel(schedule.getChannel().getId());

//        if (!WindmillConfiguration.isAlacaterVersion && AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
//            finishAction.onFisnish();
//            tuneLive(channelProduct.getChannel().getId());
//            isProcessingPurchase = false;
//            return;
//        }

        int checkChannelResult = checkChannelProduct(true, channelProduct, networkType, true, null);
        if (checkChannelResult < 0) {
            isProcessingPurchase = false;
            return;
        }
        if (checkChannelResult == WATCHABLE_CAN_PLAY) {
            isProcessingPurchase = false;
            tuneLive(schedule);
//            tuneLiveFragment(schedule);
            return;
        }

        getChannelProductInfo(true, channelProduct, new CheckProgram() {
            @Override
            public void onCanWatch() {
                finishAction.onFisnish();
//                if(WindmillConfiguration.isNewUIPhase2) {
//                    tuneLiveFragment(schedule);
//                } else {
//                    tuneLive(schedule);
//                }
                tuneLive(schedule);
                isProcessingPurchase = false;
            }

            @Override
            public void onLoadComplete(CheckResult checkResult) {
                finishAction.onFisnish();
            }
        });

//        checkChannelProduct(channelProduct, new CheckProgram() {
//            @Override
//            public void onCanWatch() {
//                tuneLive(schedule);
//            }
//        });
    }

    public void goToPlaybackFromLive(final ChannelProduct channelProduct, final FinishAction finishAction) {
        if (isProcessingPurchase) {
            return;
        }

        isProcessingPurchase = true;

        NetworkUtil.NetworkType networkType = NetworkUtil.checkNetwork(this);

        if (networkType == NetworkUtil.NetworkType.DISCONNECT) {
            App.showAlertDialogNetwork(this, getSupportFragmentManager(), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finishAction.onFisnish();
                }
            });
            return;
        }

        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL1) {
            showLoginPairingDialog(false);
            finishAction.onFisnish();
            return;
        }

//        if (!WindmillConfiguration.isAlacaterVersion && AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
//            tuneLive(channelProduct.getChannel().getId());
//            finishAction.onFisnish();
//            isProcessingPurchase = false;
//            return;
//        }

        int checkChannelResult = checkChannelProduct(true, channelProduct, networkType, true, null);
        if (checkChannelResult < 0) {
            isProcessingPurchase = false;
            return;
        }
        if (checkChannelResult == WATCHABLE_CAN_PLAY) {
            tuneLive(channelProduct.getChannel().getId());
//            tuneLiveFragment(channelProduct.getChannel().getId());
            finishAction.onFisnish();
            isProcessingPurchase = false;
            return;
        }

        getChannelProductInfo(true, channelProduct, new CheckProgram() {
            @Override
            public void onCanWatch() {
                tuneLive(channelProduct.getChannel().getId());
//                tuneLiveFragment(channelProduct.getChannel().getId());
                finishAction.onFisnish();
                isProcessingPurchase = false;

            }

            @Override
            public void onLoadComplete(CheckResult checkResult) {
                finishAction.onFisnish();
            }
        });

//        checkChannelProduct(channelProduct, new CheckProgram() {
//            @Override
//            public void onCanWatch() {
//                tuneLive(schedule);
//            }
//        });
    }

    public void checkAndGoToChannelPlayback(final ChannelProduct channelProduct, final CheckProgram checkProgram) {
        if (isProcessingPurchase) {
            return;
        }
        if (WindmillConfiguration.type.equals("phone") && getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if (ChromeCastManager.getInstance().isChromeCastState() && channelProduct != null && channelProduct.getChannel() != null
                && checkChromeCastEncryption(channelProduct.getChannel().getId())) {
            showEncryptionPopUp();
            return;
        }

        final CheckResult checkResult = new CheckResult();

        isProcessingPurchase = true;

        NetworkUtil.NetworkType networkType = NetworkUtil.checkNetwork(this);

        if (networkType == NetworkUtil.NetworkType.DISCONNECT) {
            App.showAlertDialogNetwork(this, getSupportFragmentManager(), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (checkProgram != null)
                        checkProgram.onLoadComplete(checkResult);
                }
            });
            isProcessingPurchase = false;
            return;
        }

        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL1) {
            isProcessingPurchase = false;
            showLoginPairingDialog(false);
            if (checkProgram != null)
                checkProgram.onLoadComplete(checkResult);
            return;
        }

//        if (!WindmillConfiguration.isAlacaterVersion && AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
//            tuneLive(channelProduct.getChannel().getId());
//            finishAction.onFisnish();
//            isProcessingPurchase = false;
//            return;
//        }

        int checkChannelResult = checkChannelProduct(true, channelProduct, networkType, true, null);
        if (checkChannelResult < 0) {
            isProcessingPurchase = false;
            if (checkProgram != null)
                checkProgram.onLoadComplete(checkResult);
            return;
        }
        if (checkChannelResult == WATCHABLE_CAN_PLAY) {
            tuneLive(channelProduct.getChannel().getId());
            isProcessingPurchase = false;
            if (checkProgram != null)
                checkProgram.onLoadComplete(checkResult);
            return;
        }

        getChannelProductInfo(true, channelProduct, new CheckProgram() {
            @Override
            public void onCanWatch() {
//                checkProgram.onCanWatch();
                tuneLive(channelProduct.getChannel().getId());
                isProcessingPurchase = false;
                if (checkProgram != null) {
                    checkResult.setSuccess(true);
                    checkProgram.onLoadComplete(checkResult);
                }
            }

            @Override
            public void onLoadComplete(CheckResult checkResult) {
                isProcessingPurchase = false;
                if (checkProgram != null)
                    checkProgram.onLoadComplete(checkResult);
            }
        });

//        checkChannelProduct(channelProduct, new CheckProgram() {
//            @Override
//            public void onCanWatch() {
//                tuneLive(schedule);
//            }
//        });
    }

    public boolean checkChromeCastEncryption(String channelId) {
        if (!ChromeCastManager.getInstance().isChromeCastState()) return false;
        ChannelProduct product = ChannelManager.getInstance().getChannel(channelId);
        if (product == null) return false;
        if (product.isEncryption()) {
            return true;
        }
        return false;
    }

    public void checkChannelPlayable(final ChannelProduct channelProduct, final CheckProgram checkProgram) {
        if (isProcessingPurchase) {
            return;
        }

        final CheckResult checkResult = new CheckResult();

        isProcessingPurchase = true;

        NetworkUtil.NetworkType networkType = NetworkUtil.checkNetwork(this);

        if (networkType == NetworkUtil.NetworkType.DISCONNECT) {
            App.showAlertDialogNetwork(this, getSupportFragmentManager(), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (checkProgram != null)
                        checkProgram.onLoadComplete(checkResult);
                }
            });
            isProcessingPurchase = false;
            return;
        }

        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL1) {
            isProcessingPurchase = false;
            showLoginPairingDialog(false);
            if (checkProgram != null)
                checkProgram.onLoadComplete(checkResult);
            return;
        }

        int checkChannelResult = checkChannelProduct(true, channelProduct, networkType, true, null);
        if (checkChannelResult < 0) {
            isProcessingPurchase = false;
            if (checkProgram != null)
                checkProgram.onLoadComplete(checkResult);
            return;
        }
        if (checkChannelResult == WATCHABLE_CAN_PLAY) {

            isProcessingPurchase = false;
            if (checkProgram != null) {
                checkProgram.onCanWatch();
                checkResult.setSuccess(true);
                checkProgram.onLoadComplete(checkResult);
            }
            return;
        }

        getChannelProductInfo(true, channelProduct, new CheckProgram() {
            @Override
            public void onCanWatch() {
                isProcessingPurchase = false;
                if (checkProgram != null) {
                    checkProgram.onCanWatch();
                    checkResult.setSuccess(true);
                    checkProgram.onLoadComplete(checkResult);
                }
            }

            @Override
            public void onLoadComplete(CheckResult checkResult) {
                if (checkProgram != null)
                    checkProgram.onLoadComplete(checkResult);
            }
        });
    }


    public interface FinishAction {
        public void onFisnish();

    }

    protected void tuneLive(Schedule schedule) {
//        Intent intent = new Intent(this, PlaybackActivity.class);
////        intent.putExtra(Schedule.class.getName(), schedule);
//        intent.putExtra(PlaybackActivity.BUNDLE_KEY_CHANNEL_ID, schedule.getChannel().getId());
//        intent.putExtra(TitleBarFragment.RATING, schedule.getRating());
//        intent.putExtra(TitleBarFragment.HD_QUALITY, schedule.isHd());
//        startActivity(intent);
    }

//    protected void tuneLiveFragment(Schedule schedule) {
//    }

    protected void tuneLive(String channelId) {
//        Intent intent = new Intent(this, PlaybackActivity.class);
//        intent.putExtra(PlaybackActivity.BUNDLE_KEY_CHANNEL_ID, channelId);
//        startActivity(intent);
    }

//    protected void tuneLiveFragment(String channelId) {
//    }

//    protected void goToVodDetail(Vod vod) {
//        goToVodDetail(vod.getProgram().getId(), vod.getPath());
//    }

    public void addDetailFragment(Object object, View.OnClickListener callback) {
        if (object instanceof Vod) {
            setupVodDetailFragment((Vod) object);
        } else if (object instanceof String) {
            setupSearchFragment((String) object, callback);
        }
    }

    public void setupSearchFragment(String keyword, View.OnClickListener callback) {

    }

    public void setupVodDetailFragment(Vod vod) {

    }

    public void getLastestSeries(final Vod vod, final GetSeriesInfoListener getSeriesInfoListener) {
        showProgress();

        AsyncTask<String, String, Pair<Series, ArrayList<Vod>>> asyn = new AsyncTask<String, String, Pair<Series, ArrayList<Vod>>>() {
            @Override
            protected Pair<Series, ArrayList<Vod>> doInBackground(String... params) {
                ArrayList<Vod> listEpisodes = new ArrayList<>();
                Series series = null;

                try {
                    ProgramList programList = ProgramLoader.getInstance().getSeries(vod, 0);
                    if (programList == null || programList.getData() == null || programList.getData().isEmpty()) {
                        return null;
                    }

                    listEpisodes.addAll(programList.getData());
                    int total = programList.getTotal();

                    while (listEpisodes.size() < total) {
                        int newOffset = listEpisodes.size();
                        programList = ProgramLoader.getInstance().getSeries(vod, newOffset);
                        if (programList == null || programList.getData() == null || programList.getData().isEmpty()) {
                            break;
                        }
                        listEpisodes.addAll(programList.getData());
                    }

                    series = ProgramLoader.getInstance().getSeriesLastWatchedEpisode(vod);

                    return new Pair<>(series, listEpisodes);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Pair<Series, ArrayList<Vod>> s) {
                super.onPostExecute(s);
                if (s == null) {
                    getSeriesInfoListener.onComplete(null, null);
                } else {
                    getSeriesInfoListener.onComplete(s.first, s.second);
                }

                hideProgress();
            }
        };
        asyn.execute();
    }


//    protected void goToVodDetail(String programId, Path path) {
//    }

    public void showLoginPairingDialog(boolean pairingOnly) {
//        final FragmentManager fm = getSupportFragmentManager();
//        final LoginPairingFragment fragment = new LoginPairingFragment();
//        fragment.setFinishCallback(new Runnable() {
//            @Override
//            public void run() {
//                fm.beginTransaction().remove(fragment).commit();
//            }
//        });
//
//        Bundle args = new Bundle();
//        args.putBoolean(LoginPairingFragment.PARAM_PAIRING_ONLY, pairingOnly);
//        fragment.setArguments(args);
//        Fragment oldFragment = fm.findFragmentByTag(LoginPairingFragment.CLASS_NAME);
//        FragmentTransaction ft = fm.beginTransaction();
//        if (oldFragment != null)
//            ft.remove(oldFragment);
//        ft.add(fragment, LoginPairingFragment.CLASS_NAME);
//        ft.commit();
    }

    public void removeProfileFragment() {

    }

    protected void checkAutoLogin(Context context) {
    }

    protected void showLimitNoticeDialog(boolean pairingOnly, boolean directLogin) {
    }

    public void showLimitNoticeDialog() {
    }

    public String checkWatchableSingle(Product singleProduct) {
        if (singleProduct.isFlag() || singleProduct.isFree()) {
            return Product.FREE;
        }
        return singleProduct.isPurchased() ? singleProduct.getPurchaseId() : null;
    }

    //    public boolean checkWatchableSingle(Product singleProduct) {
//        return singleProduct.isFlag() || singleProduct.isFree() || singleProduct.isPurchased();
//    }
    public String checkWatchableSingle(Vod vod, Product singleProduct, NetworkUtil.NetworkType networkType) {
        if (singleProduct.isFlag() || singleProduct.isFree()) {
            return Product.FREE;
        }
        return singleProduct.checkSinglePurchased(networkType) ? singleProduct.getPurchaseId() : null;
    }

    public String checkWatchablePackage(ArrayList<Product> listPackage,
                                        boolean isHasWifiPackage, NetworkUtil.NetworkType networkType, boolean isIgnoreSystemPackage) {

        if (listPackage == null) {
            return null;
        }

        for (Product product : listPackage) {
            if (product.isFree()) {
                return Product.FREE;
            }
            if (product.isPurchased(isHasWifiPackage, networkType, isIgnoreSystemPackage)) {
                return product.getPurchaseId();
            }

//            // Check TH mua goi cu nhung goi cu bi disable
//            if(!product.isPurchaseable() && product.isPurchased() && product.getNetwork_type() != null && product.getNetwork_type().equals(Product.NETWORK_TYPE_ALL)) {
//                return product.getPurchaseId();
//            }
        }

        return null;
    }
//    public boolean checkWatchablePackage(ArrayList<Product> listPackage,
//                                         boolean isHasWifiPackage, NetworkUtil.NetworkType networkType) {
//
//        if (listPackage == null) {
//            return false;
//        }
//
//        for (Product product : listPackage) {
//            if (product.isFree() || product.isPurchased(isHasWifiPackage, networkType)) {
//                return true;
//            }
//        }
//
//        return false;
//    }

    public String checkWatchablePackage(ArrayList<Product> listPackage, ArrayList<Product> listSource) {

        if (listPackage == null) {
            return null;
        }

        for (Product product : listPackage) {
            if (product.isFree()) {
                return Product.FREE;
            }
            if (product.isPurchased()) {
                return product.getPurchaseId();
            }
        }

        if (listSource != null) {
            for (Product product : listSource) {

                if (product.isSingleProduct()) continue;

                if (product.isFree()) {
                    return Product.FREE;
                }
                if (product.isPurchased() && !product.isPurchaseable() && product.getNetwork_type() != null && product.getNetwork_type().equals(Product.NETWORK_TYPE_ALL)) {
                    return product.getPurchaseId();
                }
            }
        }

        return null;
    }
//    public boolean checkWatchablePackage(ArrayList<Product> listPackage) {
//
//        if (listPackage == null) {
//            return false;
//        }
//
//        for (Product product : listPackage) {
//            if (product.isFree() || product.isPurchased()) {
//                return true;
//            }
//        }
//
//        return false;
//    }

    public ArrayList<Product> getSVODProduct(ArrayList<Product> listPackage) {
        if (!WindmillConfiguration.isAlacaterVersion) {
            return null;
        }
        if (listPackage == null) {
            return null;
        }
        ArrayList<Product> list = null;
        for (Product product : listPackage) {
            if (product.isSVODProduct()) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(product);
            }
        }
        return list;
    }

//    public void showReservedDialog(final Reservation reservation) {
//        final MessageDialog dialog = new MessageDialog();
//        Bundle args = new Bundle();
//        args.putString(MessageDialog.PARAM_TITLE, reservation.getTitle(WindmillConfiguration.LANGUAGE));
//        args.putString(MessageDialog.PARAM_MESSAGE, getString(R.string.channle_popup_desc1) + "\n" + getString(R.string.channle_popup_desc2));
//        args.putString(MessageDialog.PARAM_BUTTON_1, getString(R.string.channle_popup_button_tune_now));
//        args.putString(MessageDialog.PARAM_BUTTON_2, getString(R.string.channle_popup_button_tune_later));
//        dialog.setArguments(args);
//        dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v.getId() == R.id.button1) {
//                    showProgress();
//                    ChannelManager.getInstance().getCurrentScheduleList(new WindmillCallback() {
//                        @Override
//                        public void onSuccess(Object obj) {
//                            ArrayList<Schedule> listSchedule = ((ScheduleListRes) obj).getData();
//                            int size = listSchedule.size();
//                            for (int i = 0; i < size; i++) {
//                                if (listSchedule.get(i).getChannel().getId().equalsIgnoreCase(reservation.getChannelId())) {
//                                    goToPlaybackFromLive(listSchedule.get(i));
//                                    hideProgress();
//                                    break;
//                                }
//                            }
//                            hideProgress();
//                        }
//
//                        @Override
//                        public void onFailure(Call call, Throwable t) {
//                            hideProgress();
//                        }
//
//                        @Override
//                        public void onError(ApiError error) {
//                            hideProgress();
//                        }
//                    });
//                }
//                dialog.dismiss();
//            }
//        });
//        dialog.show(getSupportFragmentManager(), MessageDialog.CLASS_NAME);
//    }


    public SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'ViettelTV'");

    /**
     * @param logType (Exception, Error)
     * @param content
     */
    public void sendLog(String logType, String content) {
//        if (WindmillConfiguration.LOG_LEVEL == Log.LEVEL_NONE) {
//            return;
//        }

        String date = sdf.format(new Date());

        String md5 = Util.md5(date);

        String id = HandheldAuthorization.getInstance().getCurrentId();
        if (id == null || id.isEmpty()) {
            id = "NONE";
        }

        PackageInfo i = null;
        try {
            i = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String versionName = i.versionName;
        int versionCode = i.versionCode;

        String model_no = android.os.Build.MODEL;
        String os_name = android.os.Build.ID;
        String os_version = android.os.Build.VERSION.RELEASE;

        model_no += " " + os_name + " " + Build.VERSION.SDK_INT;

        String manufacturer = Build.MANUFACTURER;

        UserDataLoader.getInstance().sendLog(true, id, logType, content, WindmillConfiguration.model, versionName, versionCode, model_no, manufacturer, md5, (WindmillConfiguration.LIVE ? "LIVE" : "TEST"));
    }

    public void hideAllKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showKeyBoard(EditText editText) {
        if (editText != null) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            InputMethodManager inputMethodManager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
            inputMethodManager.showSoftInput(editText, 1);
        }
    }

//    public boolean isFullScreen = false;
//
//    public void turnOnFullScreen() {
//        toggleFullscreen(this, false);
//    }
//
//    public void turnOffFullScreen() {
//        toggleFullscreen(this, true);
//    }

    public boolean isFullScreen() {

        return (getWindow().getAttributes().flags &
                WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
    }

    public void setFullScreen(boolean full) {

        Window window = getWindow();
        if (full) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        if (Build.VERSION.SDK_INT >= 11) {
            if (getActionBar() != null) {
                if (full) {
                    getActionBar().hide();
                } else {
                    getActionBar().show();
                }
            }
        }
        if (WindmillConfiguration.type.equals("phone")) {
            if (full) {
                hideNavBar();
            } else {
                showNavBar();
            }
        }

        changeStatusBarColor(R.color.main_header);

    }

    public static final int SYSTEM_LAYOUT_MODE_19 = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
    public static final int HIDE_NAV_BAR_19 = SYSTEM_LAYOUT_MODE_19
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    public static final int SYSTEM_LAYOUT_MODE_17 = 0x00000400;
    public static final int SYSTEM_UI_FLAG_FULLSCREEN_17 = 0x00000004;

    public void hideNavBar() {
        if (Build.VERSION.SDK_INT > 19) {
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(HIDE_NAV_BAR_19);
        }
//        else {
//
//            View v = getWindow().getDecorView();
//            v.setSystemUiVisibility(SYSTEM_LAYOUT_MODE_17
//                    | View.SYSTEM_UI_FLAG_LOW_PROFILE | SYSTEM_UI_FLAG_FULLSCREEN_17);
//        }

//        View v = getWindow().getDecorView();
//        v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void showNavBar() {
//        View v = getWindow().getDecorView();
//        v.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                        | View.SYSTEM_UI_FLAG_VISIBLE
//        );
        if (Build.VERSION.SDK_INT > 19) {
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(SYSTEM_LAYOUT_MODE_19);
//            v.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                    | View.SYSTEM_UI_FLAG_VISIBLE
//            );
        }
//        else {
//
//            View v = getWindow().getDecorView();
//            v.setSystemUiVisibility(SYSTEM_LAYOUT_MODE_17);
//        }
    }

    public boolean hasNavBar(Context context) {
        Resources resources = context.getResources();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            return resources.getBoolean(id);
        } else {    // Check for keys
            boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !hasMenuKey && !hasBackKey;
        }
    }

    public void applyFlagSecure(boolean flagSecure)
    {

        if(flagSecure) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }

//        Window window = getWindow();
//        WindowManager wm = getWindowManager();
//
//        // is change needed?
//        int flags = window.getAttributes().flags;
//        if (flagSecure && (flags & WindowManager.LayoutParams.FLAG_SECURE) != 0) {
//            // already set, change is not needed.
//            return;
//        } else if (!flagSecure && (flags & WindowManager.LayoutParams.FLAG_SECURE) == 0) {
//            // already cleared, change is not needed.
//            return;
//        }
//
//        // apply (or clear) the FLAG_SECURE flag to/from Activity this Fragment is attached to.
//        boolean flagsChanged = false;
//        if (flagSecure) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
//            flagsChanged = true;
//        } else {
//            // FIXME Do NOT unset FLAG_SECURE flag from Activity's Window if Activity explicitly set it itself.
//            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
//            flagsChanged = true;
//        }
//
//        // Re-apply (re-draw) Window's DecorView so the change to the Window flags will be in place immediately.
//        if (flagsChanged && ViewCompat.isAttachedToWindow(window.getDecorView())) {
//            // FIXME Removing the View and attaching it back makes visible re-draw on Android 4.x, 5+ is good.
//            wm.removeViewImmediate(window.getDecorView());
//            wm.addView(window.getDecorView(), window.getAttributes());
//        }
    }


//    @SuppressLint("NewApi")
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus)
//    {
//        super.onWindowFocusChanged(hasFocus);
//        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
//        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
//        {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//    }

    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(color));
            getWindow().setStatusBarColor(getResources().getColor(color));
        }
    }

    private void toggleFullscreen(Activity activity, boolean fullscreen) {
        if (Build.VERSION.SDK_INT >= 11) {
            // The UI options currently enabled are represented by a bitfield.
            // getSystemUiVisibility() gives us that bitfield.
            int uiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();
            int newUiOptions = uiOptions;
            boolean isImmersiveModeEnabled =
                    ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);

            // Navigation bar hiding:  Backwards compatible to ICS.
            if (Build.VERSION.SDK_INT >= 14) {
                newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }

            // Status bar hiding: Backwards compatible to Jellybean
            if (Build.VERSION.SDK_INT >= 16) {
                newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
            }

            // Immersive mode: Backward compatible to KitKat.
            // Note that this flag doesn't do anything by itself, it only augments the behavior
            // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
            // all three flags are being toggled together.
            // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
            // Sticky immersive mode differs in that it makes the navigation and status bars
            // semi-transparent, and the UI flag does not get cleared when the user interacts with
            // the screen.
            if (Build.VERSION.SDK_INT >= 18) {
                newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            activity.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        }
//        else {
//            // for android pre 11
//            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
//            if (fullscreen) {
//                attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//            } else {
//                attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
//            }
//            activity.getWindow().setAttributes(attrs);
//        }

//        try {
//            if (Build.VERSION.SDK_INT >= 11) {
//                if (getActionBar() != null) {
//                    if (fullscreen) {
//                        getActionBar().hide();
//                    } else {
//                        getActionBar().show();
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public void lockSlideMenu() {
    }

    public void unlockSlideMenu() {
    }

    //    private void showSwitchConfirmDialog(final MyDeviceAccount deviceAccount, final FragmentManager fragmentManager, final WindmillCallback windmillCallback, final ActionListener actionListener) {
//        if (deviceAccount == null) return;
//        final DeviceLimitDialog dialog = new DeviceLimitDialog();
//        Bundle args = new Bundle();
//        args.putString(DeviceLimitDialog.PARAM_TITLE, getString(R.string.msgTitleDeviceLimitDialog));
//        args.putString(DeviceLimitDialog.PARAM_SUBTITLE1, getString(R.string.msgsubTitleDeviceLimitDialog1));
//        args.putString(DeviceLimitDialog.PARAM_SUBTITLE2, getString(R.string.msgsubTitleDeviceLimitDialog2));
//
//        args.putString(DeviceLimitDialog.PARAM_BUTTON1, getString(R.string.yes));
//        args.putString(DeviceLimitDialog.PARAM_BUTTON2, getString(R.string.no));
//        args.putString(DeviceLimitDialog.PARAM_ID, deviceAccount.getId());
//        args.putInt(DeviceLimitDialog.PARAM_NUM_DEVICE_LIMIT, deviceAccount.getRegistered_device().getTotal());
//        args.putBoolean(DeviceLimitDialog.PARAM_CANCEL_ON_TOUCH_OUTSIDE, false);
//
//
//        dialog.setArguments(args);
//        dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v.getId() == R.id.btnConfirm) {
//                    RegistedDevice registedDevice = deviceAccount.getRegistered_device();
//                    if (registedDevice != null && isLimitedDeviceThroughTotal(registedDevice)) {
//                        if (registedDevice.getTotal() == 1) {
//                            switchSelectRegisterDevice(dialog, registedDevice, registedDevice.getTotal(), windmillCallback, actionListener);
//                        } else {
//                            showListDeviceDialog(deviceAccount,registedDevice.getTotal(), windmillCallback, actionListener);
//                        }
//                    }
//                    dialog.dismiss();
//                } else {
//                    logout(dialog);
//                }
//            }
//        });
//        dialog.setCancelable(false);
//        dialog.show(fragmentManager, DeviceLimitDialog.CLASS_NAME);
//    }
//    private void showSwitchConfirmDialog(final MyDeviceAccount deviceAccount, final FragmentManager fragmentManager, final WindmillCallback windmillCallback, final ActionListener actionListener) {
//        if (deviceAccount == null) return;
//        final DeviceLimitDialog dialog = new DeviceLimitDialog();
//        Bundle args = new Bundle();
//        args.putString(DeviceLimitDialog.PARAM_TITLE, getString(R.string.msgTitleDeviceLimitDialog));
//        args.putString(DeviceLimitDialog.PARAM_SUBTITLE1, getString(R.string.msgsubTitleDeviceLimitDialog1));
//        args.putString(DeviceLimitDialog.PARAM_SUBTITLE2, getString(R.string.msgsubTitleDeviceLimitDialog2));
//
//        args.putString(DeviceLimitDialog.PARAM_BUTTON1, getString(R.string.yes));
//        args.putString(DeviceLimitDialog.PARAM_BUTTON2, getString(R.string.no));
//        args.putString(DeviceLimitDialog.PARAM_ID, deviceAccount.getId());
//        args.putInt(DeviceLimitDialog.PARAM_NUM_DEVICE_LIMIT, deviceAccount.getRegistered_device().getTotal());
//        args.putBoolean(DeviceLimitDialog.PARAM_CANCEL_ON_TOUCH_OUTSIDE, false);
//
//
//        dialog.setArguments(args);
//        dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v.getId() == R.id.btnConfirm) {
//                    RegistedDevice registedDevice = deviceAccount.getRegistered_device();
//                    if (registedDevice != null && isLimitedDeviceThroughTotal(registedDevice)) {
//                        if (registedDevice.getTotal() == 1) {
//                            switchSelectRegisterDevice(dialog, registedDevice, registedDevice.getTotal(), windmillCallback, actionListener);
//                        } else {
//                            showListDeviceDialog(deviceAccount, registedDevice.getTotal(), windmillCallback, actionListener);
//                        }
//                    }
//                    dialog.dismiss();
//                } else {
//                    logout(dialog);
//                }
//            }
//        });
//        dialog.setCancelable(false);
//        dialog.show(fragmentManager, DeviceLimitDialog.CLASS_NAME);
//    }

//    public void showListDeviceDialog(final MyDeviceAccount deviceAccount, final FragmentManager fragmentManager, final WindmillCallback callback, final ActionListener actionListener) {
//        if (deviceAccount == null) return;
//
//        final RegistedDevice registedDevice = deviceAccount.getRegistered_device();
//        if (registedDevice == null) return;
//        Bundle args = new Bundle();
//        args.putParcelable(ListDeviceLoginDialog.PARAM_PRODUCT, registedDevice);
//        args.putString(ListDeviceLoginDialog.DEVICE_ACCOUNT, WindmillConfiguration.deviceId);
//        final ListDeviceLoginDialog dialog = new ListDeviceLoginDialog();
//        dialog.setArguments(args);
//        dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v.getId() == R.id.btnCancel) {
//                    logout(null);
//                    dialog.dismiss();
//                } else if (v.getId() == R.id.btnConfirm) {
//                    switchSelectRegisterDevice(dialog, registedDevice, registedDevice.getTotal(), callback, actionListener);
//                }
//
//            }
//        });
//        dialog.setCancelable(false);
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                dialog.show(fragmentManager, ListDeviceLoginDialog.CLASS_NAME);
//            }
//        });
//    }
//    public void showListDeviceDialog(final MyDeviceAccount deviceAccount, final FragmentManager fragmentManager, final WindmillCallback callback, final ActionListener actionListener) {
//        if (deviceAccount == null) return;
//
//        final RegistedDevice registedDevice = deviceAccount.getRegistered_device();
//        if (registedDevice == null) return;
//        Bundle args = new Bundle();
//        args.putParcelable(ListDeviceLoginDialog.PARAM_PRODUCT, registedDevice);
//        args.putString(ListDeviceLoginDialog.DEVICE_ACCOUNT, WindmillConfiguration.deviceId);
//        final ListDeviceLoginDialog dialog = new ListDeviceLoginDialog();
//        dialog.setArguments(args);
//        dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v.getId() == R.id.btnCancel) {
//                    logout(null);
//                    dialog.dismiss();
//                } else if (v.getId() == R.id.btnConfirm) {
//                    switchSelectRegisterDevice(dialog, registedDevice, registedDevice.getTotal(), callback, actionListener);
//                }
//
//            }
//        });
//        dialog.setCancelable(false);
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                dialog.show(fragmentManager, ListDeviceLoginDialog.CLASS_NAME);
//            }
//        });
//
//    }
//
//    public void switchSelectRegisterDevice(final DialogFragment dialog, RegistedDevice registedDevice, final FragmentManager fragmentManager, final WindmillCallback callback, final ActionListener actionListener) {
//        showProgress();
//        ArrayList<MyDeviceAccount.Devices> device = new ArrayList<>();
//        // Case 1 : limit =1
//        if (dialog != null && dialog instanceof DeviceLimitDialog && registedDevice.getTotal() == 1) {
//            device = registedDevice.getDevices();
//        }
//        // Case 2 : limit >1
//        else if (dialog != null && dialog instanceof ListDeviceLoginDialog) {
//            if (((ListDeviceLoginDialog) dialog).getSelectedDevice().size() <= 0)
//                return;
//            else {
//                device = ((ListDeviceLoginDialog) dialog).getSelectedDevice();
//            }
//        }
//
//        OnSelectDeRegisterDevice(device, new WindmillCallback() {
//            @Override
//            public void onSuccess(Object obj) {
//                if (obj != null && obj instanceof Login) {
//                    HandheldAuthorization.getInstance().loginInfoInitRefreshToken((Login) obj);
//                }
//                ChannelManager.getInstance().clearData();
//                FrontEndLoader.getInstance().getMyAccount(callback, false, GlobalActivity.this, false);
//                actionListener.onConfirm();
//                dialog.dismiss();
//                hideProgress();
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//                App.showAlertDialogNetwork(GlobalActivity.this, fragmentManager, null);
//                actionListener.onCancel();
//                hideProgress();
//            }
//
//            @Override
//            public void onError(ApiError error) {
//                App.showAlertDialog(GlobalActivity.this, fragmentManager, error);
//                actionListener.onCancel();
//                hideProgress();
//            }
//        });
//    }

    public void checkProduct(final CheckProductInterface checkProductInterface) {

        PurchaseCheckLoader.getInstance().getPurchaseList(0, false, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                if (obj == null) {
                    checkProductInterface.onComplete();
                    return;
                }
                final PurchaseListRes purchaseListRes = (PurchaseListRes) obj;
                if (purchaseListRes.checkAvaiBigPackage()) {
                    checkProductInterface.onComplete();
                } else {
                    PurchaseCheckLoader.getInstance().getFullpackages(new WindmillCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            WalletRes walletRes = (WalletRes) obj;

                            ArrayList<Product> listProduct = ProductUtils.filterAllHandheldPurchaseableProduct(walletRes.getData());
                            if (listProduct == null) {
                                checkProductInterface.onComplete();
                            } else {
                                final ArrayList<Product> listBigProduct = ProductUtils.filterBigProduct(listProduct);
                                final UpdatePackageAlertDialog dialog = new UpdatePackageAlertDialog();
                                dialog.setSrc(purchaseListRes.getScreenMax(listBigProduct));

                                dialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (v.getId() == R.id.btnConfirm) {

                                            doPurchaseProduct(true, listBigProduct, new PurchaseSuccess() {
                                                @Override
                                                public void onPurchaseSuccess() {
                                                    checkProductInterface.onComplete();
                                                }

                                                @Override
                                                public void onPurchaseCancel() {
                                                    checkProductInterface.onComplete();
                                                }
                                            });
                                        } else {
                                            checkProductInterface.onComplete();
                                        }

                                        dialog.dismiss();
                                    }
                                });
                                dialog.setCancelable(false);
                                dialog.show(getSupportFragmentManager(), UpdatePackageAlertDialog.CLASS_NAME);
                            }

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            checkProductInterface.onComplete();
                        }

                        @Override
                        public void onError(ApiError error) {
                            checkProductInterface.onComplete();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                checkProductInterface.onComplete();
            }

            @Override
            public void onError(ApiError error) {
                checkProductInterface.onComplete();
            }
        });

//        PurchaseCheckLoader.getInstance().getFullpackages(new WindmillCallback() {
//            @Override
//            public void onSuccess(Object obj) {
//                hideProgress();
//                WalletRes walletRes = (WalletRes) obj;
////                fetchData(walletRes, purchaseListRes);
//
//                checkProductInterface.onComplete();
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//                checkProductInterface.onComplete();
//            }
//
//            @Override
//            public void onError(ApiError error) {
//                checkProductInterface.onComplete();
//            }
//        });
    }

    public boolean checkHasBigProduct() {
        return false;
    }

    public interface CheckProductInterface {
        public void onComplete();
    }

}
