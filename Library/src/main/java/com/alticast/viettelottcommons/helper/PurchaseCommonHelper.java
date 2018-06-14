package com.alticast.viettelottcommons.helper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.activity.App;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.dialog.MessageDialog;
import com.alticast.viettelottcommons.dialog.NotViettelSubWarningDialog;
import com.alticast.viettelottcommons.dialog.PackageOptionChangeDialog;
import com.alticast.viettelottcommons.dialog.PackageOptionDialog;
import com.alticast.viettelottcommons.dialog.PackageOptionDialogPhase2;
import com.alticast.viettelottcommons.dialog.PackagePeriodDialog;
import com.alticast.viettelottcommons.dialog.PackagePeriodDialogPhase2;
import com.alticast.viettelottcommons.dialog.ProgressDialogFragment;
import com.alticast.viettelottcommons.dialog.WarningLimitDialog;
import com.alticast.viettelottcommons.loader.FrontEndLoader;
import com.alticast.viettelottcommons.loader.PurchaseCheckLoader;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.CheckUserPaymentManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.manager.TimeManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.AvailableMethod;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.resource.response.PaidAmountRes;
import com.alticast.viettelottcommons.util.Logger;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by user on 2016-07-01.
 */
public class PurchaseCommonHelper {
    public static final String TAG = PurchaseCommonHelper.class.getSimpleName();

    public static final String TYPE_VOD = "vod";
    public static final String TYPE_CHANNEL = "channel";
    public static final String TYPE_CATCHUP = "catchup";

    private static final int PRICE_ERR = -1;
    private static final int PRICE_ONLY = 0;
    private static final int POINT_ONLY = 1;
    private static final int POINT_PRICE = 2;

    private GlobalActivity mContext;
    private FragmentManager mFragmentManager;

    private RentalPeriods initRental;

    private ProgressDialogFragment mProgressDialogFragment;
    private PackageSelectCallback mPackageSelectCallback = null;

    private Object mPurchaseProduct;

    private String mCategoryType = null;
    private boolean mIsPointUser = false;

    public PurchaseCommonHelper(GlobalActivity context, FragmentManager fragmentManager, Object purchaseProduct, String categoryType) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mPurchaseProduct = purchaseProduct;
        mCategoryType = categoryType;
    }

    public void setInitRental(RentalPeriods initRental) {
        this.initRental = initRental;
    }

    public synchronized void purchase() {

//        mContext.showProgress();
        FrontEndLoader.getInstance().checkMyAccount(new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
//                mContext.hideProgress();
                processPurchase();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                mPackageSelectCallback.onSelect(null, null, false);
                App.showAlertDialogNetwork(mContext, mFragmentManager, null);
//                mContext.hideProgress();
            }

            @Override
            public void onError(ApiError error) {
                mPackageSelectCallback.onSelect(null, null, false);
                App.showAlertDialog(mContext, mFragmentManager, error);
//                mContext.hideProgress();
            }
        }, mContext);


    }

    public void processPurchase() {
        HandheldAuthorization.LoginUserInfo loginUserInfo = HandheldAuthorization.getInstance().getCurrentUser();
        int paymentMethod = HandheldAuthorization.getInstance().getPaymentMethod();

        Logger.d(TAG, "called purchase()");
//        mIsPointUser = loginUserInfo.getIsPointUser();
        Logger.d(TAG, "currentUser: " + AuthManager.currentLevel());
//        Logger.d(TAG, "Viettel Mobile: " + AccountManager.get().getViettelMobileBalanceUser());
        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2 && !loginUserInfo.isViettelMobileBalanceUser() && paymentMethod == AuthManager.ChargeAccount.CHARGE_ACCOUNT_MOBILE) {
            showCannotPurchaseDialog();
        } else {

//            if(true) {
//                notifyResult();
//                return;
//            }

            mContext.showProgress();
//            mLoaderHelper.load(new PurchaseLimitLoader(mContext, TimeManager.getInstance().getMonth()), this);

            PurchaseCheckLoader.getInstance().checkPaidAmount(TimeManager.getInstance().getMonth(), new WindmillCallback() {
                @Override
                public void onSuccess(Object obj) {
                    mContext.hideProgress();
                    if (obj != null) {
                        CheckUserPaymentManager.getInstance().checkPaymentWarning((PaidAmountRes)obj, new CheckUserPaymentManager.paymentCheckListener() {
                            @Override
                            public void paymentAvailable(boolean available, float price) {
                                if (available) {
                                    //TODO package Select popup
//                                    dismissProgress();
                                    notifyResult();
                                } else {
                                    boolean state = HandheldAuthorization.getInstance().getWaringState(TimeManager.getInstance().getMonth());
                                    if (state) {
                                        //TODO package Select popup
                                        notifyResult();
                                    } else {
                                        showLimitDialog(String.valueOf(price));
//                                        mContext.hideProgress();
                                    }

                                }
                            }
                        });

                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mPackageSelectCallback.onSelect(null, null, false);
                    App.showAlertDialogNetwork(mContext, mFragmentManager, null);
                    mContext.hideProgress();

                }

                @Override
                public void onError(ApiError error) {
                    mPackageSelectCallback.onSelect(null, null, false);
                    App.showAlertDialog(mContext, mFragmentManager, error);
                    mContext.hideProgress();
                }
            });
        }
    }

    public PurchaseCommonHelper setPurchaseCallback(PackageSelectCallback packageSelectCallback) {
        mPackageSelectCallback = packageSelectCallback;
        return this;
    }

    public void notifyResult() {
        Logger.d(TAG, "called notifyResult() - mIsPointUser");

        if (mPurchaseProduct instanceof ArrayList) {
            if (((ArrayList) mPurchaseProduct).size() == 1) {

                checkAvailableData(((ArrayList<Product>) mPurchaseProduct).get(0), mCategoryType);
            } else {
                mContext.hideProgress();
                mIsPointUser = HandheldAuthorization.getInstance().isPointUser();
                showPackageOptionDialog((ArrayList<Product>) mPurchaseProduct);

            }
        } else if (mPurchaseProduct instanceof Product) {
            checkAvailableData((Product) mPurchaseProduct, mCategoryType);
        }
    }

    private void showPackageOptionDialog(ArrayList<Product> products) {
        Logger.d(TAG, "called showPackageOptionDialog()");
//        final PackageOptionDialog dialog = new PackageOptionDialog();
//        Bundle args = new Bundle();
//        args.putParcelableArray(PackageOptionDialog.PARAM_PACKAGES, products.toArray(new Product[products.size()]));
//        args.putBoolean(PackageOptionDialog.PARAM_IS_POINT_USER, mIsPointUser);
//        dialog.setArguments(args);
//        dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Logger.d(TAG, "called onClick");
//                dialog.dismiss();
//                mPackageSelectCallback.onSelect(null, null, false);
//            }
//        });
//        dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Logger.d(TAG, "called onItemClick()");
//                Product product = (Product) parent.getAdapter().getItem(position);
//                Logger.d(TAG, "called onItemClick()-name :" + product.getName());
//                dialog.dismiss();
//                checkAvailableData(product, mCategoryType);
//            }
//        });
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                dialog.show(mFragmentManager, PackageOptionDialog.CLASS_NAME);
//            }
//        });


        DialogFragment oldDialog = (DialogFragment) mFragmentManager.findFragmentByTag(PackageOptionDialogPhase2.CLASS_NAME);
        if (oldDialog != null) oldDialog.dismiss();

        final PackageOptionDialogPhase2 dialog = new PackageOptionDialogPhase2();
        dialog.setSrc(mIsPointUser, products);
        dialog.setCancelable(false);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnConfirm) {
                    Product product = dialog.getChooseProduct();
                    if(product != null) {
                        Logger.d(TAG, "called onItemClick()-name :" + product.getName());
                        dialog.dismiss();
                        checkAvailableData(product, mCategoryType);
                    }
                } else if(v.getId() == R.id.btnCancel) {
                    mPackageSelectCallback.onSelect(null, null, false);
                    dialog.dismiss();
                }

            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dialog.show(mFragmentManager, PackageOptionDialogPhase2.CLASS_NAME);
            }
        });

//        dialog.show(getChildFragmentManager(), PackageOptionChangeDialog.CLASS_NAME);

    }

    private void showPackagePeriodDialog(final Product product) {
//        final PackagePeriodDialog dialog = new PackagePeriodDialog();
//        Bundle args = new Bundle();
//        args.putParcelable(PackagePeriodDialog.PARAM_PRODUCT, product);
//        dialog.setArguments(args);
//        dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Logger.d(TAG, "called onClick");
//                if(v.getId() == R.id.btnCancel){
//                    mPackageSelectCallback.onSelect(null, null, false);
//                }else if(v.getId() == R.id.btnPurchase){
//                    mPackageSelectCallback.onSelect(product, dialog.getSelectedRentalPeriod(), dialog.selectedAutoRenewal());
//                }
//                dialog.dismiss();
//            }
//        });
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                dialog.show(mFragmentManager, PackageOptionDialog.CLASS_NAME);
//            }
//        });


        DialogFragment oldDialog = (DialogFragment) mFragmentManager.findFragmentByTag(PackagePeriodDialogPhase2.CLASS_NAME);
        if (oldDialog != null) oldDialog.dismiss();

        final PackagePeriodDialogPhase2 dialog = new PackagePeriodDialogPhase2();
        dialog.setSrc(initRental, product);
        dialog.setCancelable(false);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnConfirm) {
                    mPackageSelectCallback.onSelect(product, dialog.getChoseRental(), true);
                } else if(v.getId() == R.id.btnCancel) {
//                    notifyResult();
                    if (mPurchaseProduct instanceof ArrayList && ((ArrayList) mPurchaseProduct).size() > 1) {
                        showPackageOptionDialog((ArrayList<Product>) mPurchaseProduct);
                    } else {
                        mPackageSelectCallback.onSelect(null, null, false);
                    }
                }
                dialog.dismiss();
            }
        });


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dialog.show(mFragmentManager, PackageOptionDialogPhase2.CLASS_NAME);
            }
        });

    }

    private void checkAvailableData(final Product product, String productCategory) {
//        mLoaderHelper.load(new PaymentAvailableLoader(mContext, product, productCategory), this);

        if(AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
            if(HandheldAuthorization.getInstance().isPoorUser() && HandheldAuthorization.getInstance().getPaymentMethod() == AuthManager.ChargeAccount.CHARGE_ACCOUNT_MOBILE) {
                showStandAloneUserDescriptionDialog(product);
            } else {
                checkPaymentOnlyProduct(product);
            }
            return;
        }

//        if(WindmillConfiguration.isIgnoreCheckPayment) {
//            mContext.hideProgress();
//            checkPaymentOnlyProduct(product);
//            return;
//        }
//        if(true) {
//            mContext.hideProgress();
//            checkPaymentOnlyProduct(product);
//            return;
//        }

//        mContext.showProgress();
        PurchaseCheckLoader.getInstance().checkPaymentsAvailable(product,productCategory, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
//                mContext.hideProgress();
                AvailableMethod purchaseMethod = (AvailableMethod) obj;
                mIsPointUser = purchaseMethod.isPointUser();
//                HandheldAuthorization.getInstance().setIsPointUser(mIsPointUser);

                product.setPointPurchaseable(purchaseMethod.isPointUser());
                int check = checkOnlyProduct(product);
                Logger.d(TAG, "called purchase()-check : " + check + ",mIsPointUser : " + mIsPointUser);
                if (check == PRICE_ERR || (check == POINT_ONLY && !mIsPointUser)) {
                    //TODO Error popup
                    Logger.d(TAG, "called purchase()-error");
                    ApiError error = new ApiError(0, "H0512", mContext.getString(R.string.error_h0512));
                    App.showAlertDialog(mContext, mFragmentManager, error);
//                        dismissProgress();
                } else {
                    checkPaymentOnlyProduct(product);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                App.showAlertDialogNetwork(mContext, mFragmentManager, null);
//                mContext.hideProgress();
            }

            @Override
            public void onError(ApiError error) {
                App.showAlertDialog(mContext, mFragmentManager, error);
//                mContext.hideProgress();
            }
        });
    }

    private void showCannotPurchaseDialog() {
        Logger.d(TAG, "called showCannotPurchaseDialog()");
        final MessageDialog dialog = new MessageDialog();
        Bundle args = new Bundle();
        args.putString(MessageDialog.PARAM_TITLE, mContext.getString(R.string.notice));
        args.putString(MessageDialog.PARAM_MESSAGE, mContext.getString(R.string.msgCannotPurchase));
        args.putString(MessageDialog.PARAM_BUTTON_1, mContext.getString(R.string.close));
        dialog.setArguments(args);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button1) {
                }
                dialog.dismiss();

            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dialog.show(mFragmentManager, MessageDialog.CLASS_NAME);
            }
        });

    }


    private void showStandAloneUserDescriptionDialog(Product product) {
        Logger.d(TAG, "called showStandAloneUserDescriptionDialog()");
        final MessageDialog dialog = new MessageDialog();
        Bundle args = new Bundle();

        args.putString(MessageDialog.PARAM_TITLE, product.getName() == null ? mContext.getString(R.string.notice) : product.getName());
        args.putString(MessageDialog.PARAM_MESSAGE, product.getDescription());
        args.putString(MessageDialog.PARAM_BUTTON_1, mContext.getString(R.string.ok));
        dialog.setArguments(args);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Logger.d(TAG, "showAlertDialog show");

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dialog.show(mFragmentManager, MessageDialog.CLASS_NAME);
            }
        });
    }

    private void checkPaymentOnlyProduct(Product product) {
        Logger.d(TAG, "called checkPaymentOnlyProduct()");
        //TODO 확인 필요함.
        int paymentMethod = product.getPaymentMethods();
        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {

//            if (paymentMethod == Product.PREPAID_ONLY || paymentMethod == Product.PREPAID_BONUS) {
//                final MessageDialog dialog = new MessageDialog();
//                Bundle args = new Bundle();
//
//                args.putString(MessageDialog.PARAM_TITLE, mContext.getString(R.string.notice));
//                args.putString(MessageDialog.PARAM_MESSAGE, mContext.getString(R.string.error_prepaid_only));
//                args.putString(MessageDialog.PARAM_BUTTON_1, mContext.getString(R.string.ok));
//                dialog.setArguments(args);
//                dialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                new Handler().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        dialog.show(mFragmentManager, MessageDialog.CLASS_NAME);
//                    }
//                });
//
//            } else {
//                mPackageSelectCallback.onSelect(product, null, false);
//            }
            mPackageSelectCallback.onSelect(product, null, false);
        } else if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
            if (paymentMethod == Product.POSTPAID_ONLY) {
                final MessageDialog dialog = new MessageDialog();
                Bundle args = new Bundle();

                args.putString(MessageDialog.PARAM_TITLE, mContext.getString(R.string.notice));
                args.putString(MessageDialog.PARAM_MESSAGE, mContext.getString(R.string.error_postpaid_only));
                args.putString(MessageDialog.PARAM_BUTTON_1, mContext.getString(R.string.ok));
                dialog.setArguments(args);
                dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show(mFragmentManager, MessageDialog.CLASS_NAME);
                    }
                });
            } else {
                // if (MainActivity.canInapppurchase) {
                if (product.isRentalPeriodProduct()) {
                    showPackagePeriodDialog(product);
                } else {
                    mPackageSelectCallback.onSelect(product, null, false);
                }
                //  mPackageSelectCallback.onSelect(product,null);
                // } else {
                //     showStandAloneUserDescriptionDialog(product);
                // }
            }
        }
    }

    private int checkOnlyProduct(Product product) {
        if (product.isRentalPeriodProduct()) {
            return PRICE_ONLY;
        } else {
            int pointPrice = product.getFinalPrice(Product.CURRENCY_PNT);
            int normalPrice = product.getFinalPrice(Product.CURRENCY_VND);
            Logger.d(TAG, "called checkOnlyProduct()-pointPrice : " + pointPrice + " , normalPrice : " + normalPrice);
            if (pointPrice < 0 && normalPrice < 0) {//비정상 프로그램
                return PRICE_ERR;
            } else if (pointPrice < 0) {//price only 상품
                return PRICE_ONLY;
            } else if (normalPrice < 0) {//point only 상품
                return POINT_ONLY;
            } else {//보통 상품
                return POINT_PRICE;
            }
        }
    }



    private void showLimitDialog(String price) {
        Logger.d(TAG, "called showLimitDialog() - price : " + price);
        final WarningLimitDialog dialog = new WarningLimitDialog();
        Bundle bundle = new Bundle();
        bundle.putString(WarningLimitDialog.PARAM_MESSAGE, price);
        dialog.setArguments(bundle);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnPurchase) {
                    notifyResult();
                }
                dialog.dismiss();
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dialog.show(mFragmentManager, WarningLimitDialog.CLASS_NAME);
            }
        });
    }


//    private void showProgress() {
//        Logger.d(TAG, "called showProgress()");
//        if (mProgressDialogFragment == null) mProgressDialogFragment = new ProgressDialogFragment();
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                if (!mProgressDialogFragment.isVisible() || !mProgressDialogFragment.isShowing()) {
//                    mProgressDialogFragment.show(mFragmentManager, ProgressDialogFragment.CLASS_NAME);
//                }
//            }
//        });
//    }
//
//    private void dismissProgress() {
//        try {
//            new Handler().post(new Runnable() {
//                @Override
//                public void run() {
//                    mProgressDialogFragment.dismiss();
//                }
//            });
//        } catch (NullPointerException ignored) {
//        }
//    }

    public interface PackageSelectCallback {
        public void onSelect(Product product, RentalPeriods rentalPeriod, boolean isAutoRenewal);
    }

}