package com.alticast.viettelottcommons.helper;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.activity.App;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.dialog.FindPasswordFragment;
import com.alticast.viettelottcommons.dialog.InputBoxDialog;
import com.alticast.viettelottcommons.dialog.MessageDialog;
import com.alticast.viettelottcommons.dialog.PaymentOptionDialogPhase;
import com.alticast.viettelottcommons.dialog.ProgramPurchaseConfirmDialog;
import com.alticast.viettelottcommons.dialog.ProgramPurchaseConfirmDialogPhase2;
import com.alticast.viettelottcommons.dialog.ProgramPurchaseDialogPhase2;
import com.alticast.viettelottcommons.dialog.ProgressDialogFragment;
import com.alticast.viettelottcommons.dialog.PurchaseConfirmDialog;
import com.alticast.viettelottcommons.dialog.PurchaseDialog;
import com.alticast.viettelottcommons.dialog.ReConfirmDialog;
import com.alticast.viettelottcommons.loader.FrontEndLoader;
import com.alticast.viettelottcommons.loader.PurchaseCheckLoader;
import com.alticast.viettelottcommons.loader.PurchaseLoader;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.AvailableMethod;
import com.alticast.viettelottcommons.resource.BasicProduct;
import com.alticast.viettelottcommons.resource.ChannelProduct;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.Program;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.resource.response.PurchaseResultRes;
import com.alticast.viettelottcommons.util.Logger;

import java.util.Locale;

import retrofit2.Call;

public class ChannelPurchaseHelper {
    public static final String TAG = ChannelPurchaseHelper.class.getSimpleName();

    private static final int PRICE_ERR = -1;
    private static final int PRICE_ONLY = 0;
    private static final int POINT_ONLY = 1;
    private static final int POINT_PRICE = 2;

    public static final int PRODUCT_TYPE_SINGLE = 0;
    public static final int PRODUCT_TYPE_SUBSCRIPTION = 1;

    private GlobalActivity mContext;
    private FragmentManager mFragmentManager;
    private ProgressDialogFragment mProgressDialogFragment;
    private ChannelProduct mChannel;
    private PurchaseCallback mPurchaseCallback;
    private BasicProduct mBasicProduct;
    private Product mProduct;
    private boolean mIsPointUser;

    public ChannelPurchaseHelper(GlobalActivity context, FragmentManager fragmentManager,
                                 ChannelProduct channel, Product product) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mChannel = channel;
        mProduct = product;
    }

    public void purchase() {
        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
            if(HandheldAuthorization.getInstance().isPoorUser()) {
                //Poor user can not buy
                return;
            }
        } else if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
            if (mProduct.isSingleProduct()) {//우선순위
                mProduct = mChannel.getProduct(ChannelProduct.PRODUCT_TYPE_PACKAGE);
                if (mProduct == null)
                    mProduct = mChannel.getProduct(ChannelProduct.PRODUCT_TYPE_SINGLE);
            } else if (mProduct.isSubscriptionProduct()) {
                mProduct = mChannel.getProduct(ChannelProduct.PRODUCT_TYPE_SUBSCIPTION);
            }
        }
        int check = checkOnlyProduct();
        if (check == PRICE_ERR || (check == POINT_ONLY && !mIsPointUser)) {
            //TODO Error popup
            ApiError error = new ApiError(0, "H0512", getString(R.string.error_h0512));
            App.showAlertDialog(mContext, mFragmentManager, error);
        } else {
//                mLoaderHelper.load(new PaymentAvailableLoader(mContext, mProduct, PaymentAvailableLoader.TYPE_CHANNEL), this);

//            if(WindmillConfiguration.isIgnoreCheckPayment) {
//                dismissProgress();
//                showPurchaseDialog(false);
//                return;
//            }

//            if(true) {
//                dismissProgress();
//                showPurchaseDialog(false);
//                return;
//            }

            showProgress();

            PurchaseCheckLoader.getInstance().checkPaymentsAvailable(mProduct, PurchaseCommonHelper.TYPE_CHANNEL, new WindmillCallback() {
                @Override
                public void onSuccess(Object obj) {
                    dismissProgress();
                    AvailableMethod availableMethod = (AvailableMethod) obj;

                    mIsPointUser = availableMethod.isPointUser();
                    HandheldAuthorization.getInstance().setIsPointUser(mIsPointUser);
                    showPurchaseDialog(mIsPointUser);
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    dismissProgress();
                }

                @Override
                public void onError(ApiError error) {
                    dismissProgress();
                    App.showAlertDialog(mContext, mFragmentManager, error);
                }
            });
        }
    }

    public ChannelPurchaseHelper setPurchaseCallback(PurchaseCallback purchaseCallback) {
        mPurchaseCallback = purchaseCallback;
        return this;
    }

    private void showPurchaseDialog(final boolean isPointUser) {
        dismissProgress();
        final PurchaseDialog dialog = new PurchaseDialog();
        Bundle args = new Bundle();
//        args.putString(PurchaseDialog.PARAM_TITLE, mChannel.getChannel().getName(WindmillConfiguration.LANGUAGE));
        args.putString(PurchaseDialog.PARAM_TITLE, mProduct.getName());
//        args.putLong(PurchaseDialog.PARAM_AVAILABLE_DATE, mProduct.getPurchaseEnd());
        args.putInt(PurchaseDialog.PARAM_CASH_PRICE, (int) (mProduct.getPriceValue(Product.CURRENCY_VND)));
        args.putInt(PurchaseDialog.PARAM_POINT_PRICE, (int) (mProduct.getPriceValue(Product.CURRENCY_PNT)));
        args.putBoolean(PurchaseDialog.PARAM_IS_POINT_USER, isPointUser);
        dialog.setArguments(args);
        dialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.purchase_cash_button) {
                    int price = (int) (mProduct.getPriceValue(Product.CURRENCY_VND));
                    if (price == 0) {
                        App.getToast(mContext, getString(R.string.vod_noti), mChannel.getChannel().getName(WindmillConfiguration.LANGUAGE), false).show();
                        if (mPurchaseCallback != null) mPurchaseCallback.onSuccess(mChannel);
                        dismissProgress();
                    } else {
                        showPurchaseConfirmDialog(PurchaseLoader.TypeCurrency.price, mProduct.getPriceValue(Product.CURRENCY_VND));
                    }
                } else if (v.getId() == R.id.purchase_point_button) {//point user인 경우만 존재
                    if (isPointUser) {
                        int price = (int) (mProduct.getPriceValue(Product.CURRENCY_PNT));
                        if (price == 0) {
                            App.getToast(mContext, getString(R.string.vod_noti), mChannel.getChannel().getName(WindmillConfiguration.LANGUAGE), false).show();
                            if (mPurchaseCallback != null) mPurchaseCallback.onSuccess(mChannel);
                            dismissProgress();
                        } else {
                            showPurchaseConfirmDialog(PurchaseLoader.TypeCurrency.point, mProduct.getPriceValue(Product.CURRENCY_PNT));
                        }
                    } else {
                        loadUpgradePolicy();
                    }
                } else if (v.getId() == R.id.cancel_button) {
                    if(mPurchaseCallback != null) mPurchaseCallback.onCancel();
                }
                dialog.dismiss();
            }
        });
        dialog.show(mFragmentManager, PurchaseDialog.CLASS_NAME);
    }

    private void showPurchaseConfirmDialog(final PurchaseLoader.TypeCurrency purchaseType, final float price) {
        dismissProgress();

        final PaymentOptionDialogPhase dialog = new PaymentOptionDialogPhase();
        dialog.setSrc(mProduct, null, mIsPointUser);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (v.getId() == com.alticast.viettelottcommons.R.id.btnConfirm) {
                    showProgramPurchaseConfirmDialogPhase2(purchaseType, price);
                }

            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dialog.show(mFragmentManager, ProgramPurchaseConfirmDialogPhase2.CLASS_NAME);
            }
        });
    }

    public void showProgramPurchaseConfirmDialogPhase2(final PurchaseLoader.TypeCurrency purchaseType, final float price) {
        final ProgramPurchaseConfirmDialogPhase2 dialog = new ProgramPurchaseConfirmDialogPhase2();
        dialog.setSrc(mProduct, null, mIsPointUser);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (v.getId() == com.alticast.viettelottcommons.R.id.btnConfirm) {
                    showPurchaseReConfirmDialog(purchaseType, price);
                } else {
                    showPurchaseDialog(mIsPointUser);
                }

            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dialog.show(mFragmentManager, ProgramPurchaseConfirmDialogPhase2.CLASS_NAME);
            }
        });
    }

    private void showPurchaseReConfirmDialog(final PurchaseLoader.TypeCurrency purchaseType, final float price) {
        dismissProgress();
        boolean isQuickOption = HandheldAuthorization.getInstance().isQuickOption();
        if (!isQuickOption) {
            processPurchase(purchaseType, price);
        } else {
            checkPasswordDialog(purchaseType, price);
        }

//        final ReConfirmDialog dialog = new ReConfirmDialog();
//        dialog.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (v.getId() == R.id.yes_button) {
//                    boolean isQuickOption = HandheldAuthorization.getInstance().isQuickOption();
//                    if (!isQuickOption) {
//                        processPurchase(purchaseType, price);
//                    } else {
//                        checkPasswordDialog(purchaseType, price);
//                    }
//                }
//                dialog.dismiss();
//            }
//        });
//        dialog.show(mFragmentManager, ReConfirmDialog.CLASS_NAME);
    }

    private void checkPasswordDialog(final PurchaseLoader.TypeCurrency purchaseType, final float price) {
        dismissProgress();
        final InputBoxDialog dialog = new InputBoxDialog();
        Bundle args = new Bundle();
        String title = getString(R.string.purchase_cancel_confirm_title);
        String logMsg = getString(R.string.purchase_cancel_confirm_message);
        String btn1 = getString(R.string.enter);
        String btn2 = getString(R.string.cancel);
        args.putString(InputBoxDialog.PARAM_TITLE, title);
        args.putString(InputBoxDialog.PARAM_MESSAGE, logMsg);
        args.putString(InputBoxDialog.PARAM_BUTTON_1, btn1);
        args.putString(InputBoxDialog.PARAM_BUTTON_2, btn2);
        args.putInt(InputBoxDialog.PARAM_INPUT_TYPE, InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialog.setArguments(args);

        dialog.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button1) {
                    showProgress();
                    String inputMsg = dialog.getInputString();
                    HandheldAuthorization.LoginUserInfo user = HandheldAuthorization.getInstance().getCurrentUser();

                    FrontEndLoader.getInstance().requestLogin(user.getId(), inputMsg, new WindmillCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            Logger.d(TAG, "called onResult()");
                            dismissProgress();
                            processPurchase(purchaseType, price);
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            dismissProgress();
                            dialog.setEmphasisText(getString(R.string.error_h1001));
                        }

                        @Override
                        public void onError(ApiError error) {
                            dismissProgress();
                            if (error.getStatusCode() == 401 && error.getErrorCode().equals("F0102")) {
                                dialog.setEmphasisText(getString(R.string.wrongpassword));
                            } else {
                                App.showAlertDialog(mContext, mFragmentManager, error);
                            }

                        }
                    }, mContext);
                } else if (v.getId() == R.id.button2) {
                    dialog.dismiss();
                } else if (v.getId() == R.id.tv_login_forgot_pw) {
                    showFindPasswordDialog();
                }
            }
        });
        dialog.show(mFragmentManager, InputBoxDialog.CLASS_NAME);
    }
    private void showFindPasswordDialog() {
        FindPasswordFragment dialog = new FindPasswordFragment();
        dialog.show(mFragmentManager, FindPasswordFragment.CLASS_NAME);
    }

    private void loadUpgradePolicy() {
        String pid;
        Locale locale = Locale.getDefault();
        Logger.d("getCampaignByPid", "locale:" + locale);
        if (locale != null) Logger.d("getCampaignByPid", "locale:" + locale.getLanguage());
        if (locale != null && "vi".equals(locale.getLanguage())) {
            pid = "campaign://FLEXI.TO.FLEXIPLUS";
        } else {
            pid = "campaign://FLEXI.TO.FLEXIPLUS.ENG";
        }
//        mLoaderHelper.load(new UpgradeImageLoader(mContext, pid), this);
//        showProgress();
    }

//    private void showUpgradeDialog(Bitmap bitmap) {
//        final UpgradeDialog dialog = new UpgradeDialog();
//        Bundle args = new Bundle();
//        args.putParcelable(UpgradeDialog.PARAM_IMAGE, bitmap);
//        dialog.setArguments(args);
//        dialog.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v.getId() == R.id.upgrade_button) {
//                    showUpgradeConfirmDialog();
//                }
//                dialog.dismiss();
//            }
//        });
//        dialog.show(mFragmentManager, MessageDialog.CLASS_NAME);
//    }


//    private void showUpgradeConfirmDialog() {
//        dismissProgress();
//        final UpgradeConfirmDialog dialog = new UpgradeConfirmDialog();
//        Bundle args = new Bundle();
//        args.putParcelable(BasicProduct.class.getName(), mBasicProduct);
//        dialog.setArguments(args);
//        dialog.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v.getId() == R.id.upgrade_button) {
//                    processUpgrade();
//                }
//                dialog.dismiss();
//
//            }
//        });
//        dialog.show(mFragmentManager, InputBoxDialog.CLASS_NAME);
//    }

//    private void showUpgradeConfirmDialog() {
//        dismissProgress();
//
//        if (mBasicProduct != null && mBasicProduct.getPurchasableProduct(BasicProduct.TYPE_FLEXI_PLUS) != null) {
//            final UpgradeConfirmDialog dialog = new UpgradeConfirmDialog();
//            Bundle args = new Bundle();
//            args.putParcelable(BasicProduct.class.getName(), mBasicProduct);
//            dialog.setArguments(args);
//            dialog.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (v.getId() == R.id.upgrade_button) {
//                        processUpgrade();
//                    }
//                    dialog.dismiss();
//
//                }
//            });
//            dialog.show(mFragmentManager, InputBoxDialog.CLASS_NAME);
//        } else {
//            String title = getString(R.string.upgradeLimitedTitleHeader) + BasicProduct.TYPE_FLEXI_PLUS + getString(R.string.upgradeLimitedTitleFooter);
//            showMsgDialog(title);
//
//        }
//    }

    private void showMsgDialog(String msg) {
        final MessageDialog dialog = new MessageDialog();
        Bundle args = new Bundle();
        args.putString(MessageDialog.PARAM_TITLE, getString(R.string.notice));
        args.putString(MessageDialog.PARAM_MESSAGE, msg);
        args.putString(MessageDialog.PARAM_BUTTON_1, getString(R.string.ok));

        dialog.setArguments(args);
        dialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show(mFragmentManager, MessageDialog.CLASS_NAME);
    }

    private void processUpgrade() {
//        mLoaderHelper.load(new SubscribeBasicLoader(mContext, mBasicProduct), this);

//        showProgress();
    }


    private void processPurchase(final PurchaseLoader.TypeCurrency purchaseType, final float price) {

        PurchaseLoader.getInstance().purchaseCreatePrice(mChannel, mProduct, "channel", new WindmillCallback() {

            @Override
            public void onSuccess(Object obj) {
                PurchaseResultRes purchaseResultRes = (PurchaseResultRes) obj;

                PurchaseLoader.getInstance().paymentsCreate(purchaseType, mChannel, mProduct, price, purchaseResultRes.getId(), "vod", new WindmillCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        App.getToast(mContext, getString(R.string.vod_noti), mProduct.getName(), false).show();
                        mPurchaseCallback.onSuccess(mChannel);
                        dismissProgress();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        dismissProgress();
                    }

                    @Override
                    public void onError(ApiError error) {
                        App.showAlertDialog(mContext, mFragmentManager, error);
                        dismissProgress();
                    }
                });
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }

            @Override
            public void onError(ApiError error) {

            }
        });
    }

    private String getString(int resId, Object... params) {
        return mContext.getString(resId, params);
    }

    private void showProgress() {
        mContext.showProgress();
//        if (mProgressDialogFragment == null) mProgressDialogFragment = new ProgressDialogFragment();
//        mProgressDialogFragment.show(mFragmentManager, ProgressDialogFragment.CLASS_NAME);
    }

    private void dismissProgress() {
        mContext.hideProgress();
//        try {
//
//            mProgressDialogFragment.dismiss();
//        } catch (NullPointerException ignored) {
//        }
    }

    public interface PurchaseCallback {
        public void onSuccess(ChannelProduct channel);
        public void onCancel();
    }

    private int checkOnlyProduct() {
        int pointPrice = mProduct.getFinalPrice(Product.CURRENCY_PNT);
        int normalPrice = mProduct.getFinalPrice(Product.CURRENCY_VND);
        Logger.d(TAG, "called checkOnlyProduct()-pointPrice : " + pointPrice + " , normalPrice : " + normalPrice);
        if (pointPrice < 0 && normalPrice < 0) {//비정상 프로그램
            return PRICE_ERR;
        } else if (pointPrice < 0) {//point only 상품
            return POINT_ONLY;
        } else if (normalPrice < 0) {//price only 상품
            return PRICE_ONLY;
        } else {//보통 상품
            return POINT_PRICE;
        }
    }
}
