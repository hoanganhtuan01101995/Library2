package com.alticast.viettelottcommons.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;

import com.alticast.viettelottcommons.GlobalKey;
import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.activity.App;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.dialog.ConfirmMessageDialog;
import com.alticast.viettelottcommons.dialog.FindPasswordFragment;
import com.alticast.viettelottcommons.dialog.InputBoxDialog;
import com.alticast.viettelottcommons.dialog.PackageOptionDialogPhase2;
import com.alticast.viettelottcommons.dialog.PackagePeriodDialogPhase2;
import com.alticast.viettelottcommons.dialog.PaymentOptionDialogPhase;
import com.alticast.viettelottcommons.dialog.ProgramPurchaseConfirmDialog;
import com.alticast.viettelottcommons.dialog.ProgramPurchaseConfirmDialogPhase2;
import com.alticast.viettelottcommons.dialog.ProgramPurchaseDialog;
import com.alticast.viettelottcommons.dialog.ProgramPurchaseDialogPhase2;
import com.alticast.viettelottcommons.dialog.ProgressDialogFragment;
import com.alticast.viettelottcommons.dialog.PurchaseConfirmDialog;
import com.alticast.viettelottcommons.dialog.PurchaseDataPackageConfirmDialogPhase2;
import com.alticast.viettelottcommons.dialog.PurchaseDialog;
import com.alticast.viettelottcommons.dialog.ReConfirmDialog;
import com.alticast.viettelottcommons.loader.FrontEndLoader;
import com.alticast.viettelottcommons.loader.PurchaseCheckLoader;
import com.alticast.viettelottcommons.loader.PurchaseLoader;
import com.alticast.viettelottcommons.loader.VWalletLoader;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.AvailableMethod;
import com.alticast.viettelottcommons.resource.Price;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.Program;
import com.alticast.viettelottcommons.resource.response.PurchaseResultRes;
import com.alticast.viettelottcommons.util.Logger;

import retrofit2.Call;

public class ProgramPurchaseHelper {
    private static final String TAG = ProgramPurchaseHelper.class.getSimpleName();

    private Context mContext;
    private FragmentManager mFragmentManager;
    private ProgressDialogFragment mProgressDialogFragment;
    private Program mProgram;
    private PurchaseCallback mPurchaseCallback;
    private Product mProduct;
    private boolean mIsPointUser = false;
    private String mEntryPath;
    private String producCategory;

    //    private int purchaseType = -1;
    private PurchaseLoader.TypeCurrency purchaseType = PurchaseLoader.TypeCurrency.phone;

    public ProgramPurchaseHelper(Context context, FragmentManager fragmentManager, Program program, Product product, String entryPath, boolean isPointUser) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mProgram = program;
        mProduct = product;
        mEntryPath = entryPath;
        mIsPointUser = isPointUser;
    }

    public ProgramPurchaseHelper(Context context, FragmentManager fragmentManager, Program program, Product product) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mProgram = program;
        mProduct = product;
        mIsPointUser = HandheldAuthorization.getInstance().isPointUser();
    }

    public void purchase() {
        showPurchaseDialog();
    }

    public ProgramPurchaseHelper setPurchaseCallback(PurchaseCallback purchaseCallback) {
        mPurchaseCallback = purchaseCallback;
        return this;
    }

    public void onPurchaseFinished(Program program) {
        Intent intent = new Intent(GlobalKey.VodController.REFRESH_DATA);
        intent.putExtra(Program.class.getName(), program);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        if (mPurchaseCallback != null)
            mPurchaseCallback.onSuccess(program);
    }

    private void showPurchaseDialog() {

        Logger.d(TAG, "showPurchaseDialog() - mProduct.getType(): " + mProduct.getType() + " ,mIsPointUser : " + mIsPointUser);
        dismissProgress();

        if(mProduct.isSingleProduct()) {
            if(AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
                showPurchaseConfirmDialog(false, mProduct, mProduct.getPriceValue(PurchaseLoader.currencyMap.get(PurchaseLoader.TypeCurrency.price)), Product.TYPE_PRICE);
            } else {
                Price price = mProduct.getPrice((mIsPointUser ? "PNT" : "VND"));
                showPurchaseConfirmDialog(false, mProduct, mProduct.getPriceValue(PurchaseLoader.currencyMap.get(price.getCurrency().equals("VND") ? PurchaseLoader.TypeCurrency.price : PurchaseLoader.TypeCurrency.point)), price.getCurrency().equals("VND") ? Product.TYPE_PRICE : Product.TYPE_POINT);
            }
            return;
        }

//        if (mProduct.isSingleProduct() && AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
//            showPurchaseConfirmDialog(false, mProduct, mProduct.getPriceValue(PurchaseLoader.currencyMap.get(PurchaseLoader.TypeCurrency.price)), Product.TYPE_PRICE);
//            return;
//        }

//        final ProgramPurchaseDialog dialog = new ProgramPurchaseDialog();
//        Bundle args = new Bundle();
//        args.putParcelable(Product.class.getName(), mProduct);
//        args.putParcelable(Program.class.getName(), mProgram);
//        args.putBoolean(ProgramPurchaseDialog.PARAM_IS_POINT_USER, mIsPointUser);
//        dialog.setArguments(args);
//        dialog.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v.getId() == R.id.purchase_point_button) {
//                    if (mIsPointUser) {
//                        showPurchaseConfirmDialog(false, mProduct, mProduct.getPriceValue(PurchaseLoader.currencyMap.get(PurchaseLoader.TypeCurrency.point)), Product.TYPE_POINT);//이 경우만 생김.
//                    }
//                } else if (v.getId() == R.id.purchase_normal_button) {
//                    showPurchaseConfirmDialog(false, mProduct,mProduct.getPriceValue(PurchaseLoader.currencyMap.get(PurchaseLoader.TypeCurrency.price)), Product.TYPE_PRICE);
//                }
//                dialog.dismiss();
//            }
//        });
//        dialog.show(mFragmentManager, PurchaseDialog.CLASS_NAME);

        showProgress();
        PurchaseCheckLoader.getInstance().checkPaymentsAvailable(mProduct, PurchaseCommonHelper.TYPE_CHANNEL, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                dismissProgress();
                AvailableMethod availableMethod = (AvailableMethod) obj;

                mIsPointUser = availableMethod.isPointUser();
                HandheldAuthorization.getInstance().setIsPointUser(mIsPointUser);

                Price price = mProduct.getPrice((mIsPointUser ? "PNT" : "VND"));
                showPurchaseConfirmDialog(false, mProduct, mProduct.getPriceValue(PurchaseLoader.currencyMap.get(price.getCurrency().equals("VND") ? PurchaseLoader.TypeCurrency.price : PurchaseLoader.TypeCurrency.point)), price.getCurrency().equals("VND") ? Product.TYPE_PRICE : Product.TYPE_POINT);

//                if (mIsPointUser) {
//                    showPurchaseConfirmDialog(false, mProduct, mProduct.getPriceValue(PurchaseLoader.currencyMap.get(PurchaseLoader.TypeCurrency.point)), Product.TYPE_POINT);//이 경우만 생김.
//                } else {
//                    showPurchaseConfirmDialog(false, mProduct, mProduct.getPriceValue(PurchaseLoader.currencyMap.get(PurchaseLoader.TypeCurrency.price)), Product.TYPE_PRICE);
//                }
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

//        if (mIsPointUser) {
//            showPurchaseConfirmDialog(false, mProduct, mProduct.getPriceValue(PurchaseLoader.currencyMap.get(PurchaseLoader.TypeCurrency.point)), Product.TYPE_POINT);//이 경우만 생김.
//        } else {
//            showPurchaseConfirmDialog(false, mProduct, mProduct.getPriceValue(PurchaseLoader.currencyMap.get(PurchaseLoader.TypeCurrency.price)), Product.TYPE_PRICE);
//        }

//        DialogFragment oldDialog = (DialogFragment) mFragmentManager.findFragmentByTag(ProgramPurchaseDialogPhase2.CLASS_NAME);
//        if (oldDialog != null) oldDialog.dismiss();
//
//        final ProgramPurchaseDialogPhase2 dialog = new ProgramPurchaseDialogPhase2();
//        dialog.setSrc(mProduct, mProgram, mIsPointUser);
//        dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (v.getId() == com.alticast.viettelottcommons.R.id.btnConfirm) {
//                    if (mIsPointUser) {
//                        showPurchaseConfirmDialog(false, mProduct, mProduct.getPriceValue(PurchaseLoader.currencyMap.get(PurchaseLoader.TypeCurrency.point)), Product.TYPE_POINT);//이 경우만 생김.
//                    } else {
//                        showPurchaseConfirmDialog(false, mProduct,mProduct.getPriceValue(PurchaseLoader.currencyMap.get(PurchaseLoader.TypeCurrency.price)), Product.TYPE_PRICE);
//                    }
//                } else if(v.getId() == com.alticast.viettelottcommons.R.id.btnCancel) {
//                    mPurchaseCallback.onCancel();
//                }
//
//            }
//        });
//
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                dialog.show(mFragmentManager, ProgramPurchaseDialogPhase2.CLASS_NAME);
//            }
//        });

    }

    private void checkPasswordDialog(final Product product, final float price) {
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
                            processPurchase(product, price, purchaseType);
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
                    }, (GlobalActivity) mContext);
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

    private void showPurchaseReConfirmDialog(boolean isAlternativeProduct, final Product product, final float price) {
        dismissProgress();
        boolean isQuickOption = HandheldAuthorization.getInstance().isQuickOption();
        if (!isQuickOption) {
            processPurchase(product, price, purchaseType);
        } else {
            checkPasswordDialog(product, price);
        }
//        final ReConfirmDialog dialog = new ReConfirmDialog();
//        dialog.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (v.getId() == R.id.yes_button) {
//                    boolean isQuickOption = HandheldAuthorization.getInstance().isQuickOption();
//                    if (!isQuickOption) {
//                        processPurchase(product,price, purchaseType);
//                    } else {
//                        checkPasswordDialog(product, price);
//                    }
//                }
//                dialog.dismiss();
//            }
//        });
//        dialog.show(mFragmentManager, ReConfirmDialog.CLASS_NAME);
    }

    public void showPurchaseConfirmDialog(final boolean isAlternativeProduct, final Product product, final float price, int selectType) {

        switch (selectType) {
            case Product.TYPE_POINT:
                purchaseType = PurchaseLoader.TypeCurrency.point;
                break;
            case Product.TYPE_PHONE:
                purchaseType = PurchaseLoader.TypeCurrency.phone;
                break;
            case Product.TYPE_PREPAID:
                purchaseType = PurchaseLoader.TypeCurrency.wallet;
                break;
            case Product.TYPE_PRICE:
                purchaseType = PurchaseLoader.TypeCurrency.price;
                break;
        }

        dismissProgress();

        if(AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {

            final PaymentOptionDialogPhase dialog = new PaymentOptionDialogPhase();
            dialog.setSrc(mProduct, mProgram, mIsPointUser);
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (v.getId() == com.alticast.viettelottcommons.R.id.btnConfirm) {
                        showProgramPurchaseConfirmDialogPhase2(isAlternativeProduct, product, price);
                    }

                }
            });

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    dialog.show(mFragmentManager, ProgramPurchaseConfirmDialogPhase2.CLASS_NAME);
                }
            });
        } else {
            showProgramPurchaseConfirmDialogPhase2(isAlternativeProduct, product, price);
        }


    }

    public void showProgramPurchaseConfirmDialogPhase2(final boolean isAlternativeProduct, final Product product, final float price) {
        final ProgramPurchaseConfirmDialogPhase2 dialog = new ProgramPurchaseConfirmDialogPhase2();
        dialog.setSrc(mProduct, mProgram, mIsPointUser);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (v.getId() == com.alticast.viettelottcommons.R.id.btnConfirm) {

                    if ((AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2 && HandheldAuthorization.getInstance().isPoorUser() && HandheldAuthorization.getInstance().getPaymentMethod() == AuthManager.ChargeAccount.CHARGE_ACCOUNT_VWALLET)
                            || (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3 && product.getPaymentOptionChose() == Product.PREPAID_ONLY)) {
                        VWalletLoader.getInstance().checkWalletBalance(price, new WindmillCallback() {
                            @Override
                            public void onSuccess(Object obj) {
                                Float result = (Float) obj;
                                if (result >= 0) {
                                    boolean isQuickOption = HandheldAuthorization.getInstance().isQuickOption();
                                    if (!isQuickOption) {
                                        processPurchase(product, price, purchaseType);
                                    } else {
                                        checkPasswordDialog(product, price);
                                    }
                                } else {
                                    DialogFragment oldDialog = (DialogFragment) mFragmentManager.findFragmentByTag(PackagePeriodDialogPhase2.CLASS_NAME);
                                    if (oldDialog != null) oldDialog.dismiss();

                                    final ConfirmMessageDialog dialog = new ConfirmMessageDialog();
                                    dialog.setSrc(mContext.getString(R.string.alert),
                                            mContext.getString(R.string.out_of_money_message),
                                            mContext.getString(R.string.confirmChargeWallet));
                                    dialog.setCancelable(false);
                                    dialog.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (v.getId() == R.id.btnConfirm) {
                                                ((GlobalActivity) mContext).goToMyWallet();
                                                dialog.dismiss();
                                            } else if (v.getId() == R.id.btnCancel) {
                                                dialog.dismiss();
                                                if (mPurchaseCallback != null)
                                                    mPurchaseCallback.onCancel();
                                            }

                                        }
                                    });

                                    new Handler().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.show(mFragmentManager, PackageOptionDialogPhase2.CLASS_NAME);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                App.showAlertDialogNetwork(mContext, mFragmentManager, null);
                            }

                            @Override
                            public void onError(ApiError error) {
                                App.showAlertDialog(mContext, mFragmentManager, error);
                            }
                        });
                    } else {
                        boolean isQuickOption = HandheldAuthorization.getInstance().isQuickOption();
                        if (!isQuickOption) {
                            processPurchase(product, price, purchaseType);
                        } else {
                            checkPasswordDialog(product, price);
                        }
                    }


//                    showPurchaseReConfirmDialog(isAlternativeProduct, product, price);
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

    private void processPurchase(final Product product, final float price, final PurchaseLoader.TypeCurrency purchaseType) {
        showProgress();
        PurchaseLoader.getInstance().purchaseCreatePrice(mProgram, product, mProgram != null ? "vod" : "full", new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {

                final PurchaseResultRes purchaseResultRes = (PurchaseResultRes) obj;

                PurchaseLoader.getInstance().paymentsCreate(purchaseType, mProgram, product, price, purchaseResultRes.getId(), "vod", new WindmillCallback() {
                    @Override
                    public void onSuccess(Object obj) {

                        String productName = product.isSingleProduct() ? mProgram.getTitle(WindmillConfiguration.LANGUAGE) : product.getName();
                        product.setPurchaseId(purchaseResultRes.getPurchaser_id());
                        App.getToast(mContext, getString(R.string.vod_noti), productName, false).show();
                        onPurchaseFinished(mProgram);
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

//                    MainApp.getToast(mContext, getString(R.string.vod_noti), mProduct.getName(), false).show();
//                    onPurchaseFinished(mProgram);
//
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


//        PurchaseLoader.getInstance().paymentsCreate(purchaseType, product,price, mProgram.getId(), "vod", new WindmillCallback() {
//            @Override
//            public void onSuccess(Object obj) {
//                MainApp.getToast(mContext, getString(R.string.vod_noti), mProgram.getTitle(WindmillConfiguration.LANGUAGE), false).show();
//                onPurchaseFinished(mProgram);
//
//                dismissProgress();
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//                dismissProgress();
//            }
//
//            @Override
//            public void onError(ApiError error) {
//                MainApp.showAlertDialog(mContext, mFragmentManager, error);
//                dismissProgress();
//            }
//        });


    }
    //TODO 타입이 없는 경우 어떻게 할지?

    private String getString(int resId, Object... params) {
        return mContext.getString(resId, params);
    }

    private void showProgress() {
        if (mProgressDialogFragment == null)
            mProgressDialogFragment = new ProgressDialogFragment();
        mProgressDialogFragment.show(mFragmentManager, ProgressDialogFragment.CLASS_NAME);
    }

    private void dismissProgress() {
        try {
            mProgressDialogFragment.dismiss();
        } catch (NullPointerException ignored) {
        }
    }

    public interface PurchaseCallback {
        public void onSuccess(Program program);

        public void onCancel();
    }
}
