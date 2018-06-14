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

import com.alticast.viettelottcommons.GlobalKey;
import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.App;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.dialog.ConfirmMessageDialog;
import com.alticast.viettelottcommons.dialog.FindPasswordFragment;
import com.alticast.viettelottcommons.dialog.InputBoxDialog;
import com.alticast.viettelottcommons.dialog.PackageOptionDialog;
import com.alticast.viettelottcommons.dialog.PackageOptionDialogPhase2;
import com.alticast.viettelottcommons.dialog.PackagePeriodDialog;
import com.alticast.viettelottcommons.dialog.PackagePeriodDialogPhase2;
import com.alticast.viettelottcommons.dialog.ProgressDialogFragment;
import com.alticast.viettelottcommons.dialog.PurchaseDataPacakgeConfirmDialog;
import com.alticast.viettelottcommons.dialog.PurchaseDataPackageConfirmDialogPhase2;
import com.alticast.viettelottcommons.dialog.PurchaseDataPackageReConfirmDialogPhase2;
import com.alticast.viettelottcommons.dialog.ReConfirmDialog;
import com.alticast.viettelottcommons.loader.FrontEndLoader;
import com.alticast.viettelottcommons.loader.PurchaseLoader;
import com.alticast.viettelottcommons.loader.VWalletLoader;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.ChannelProduct;
import com.alticast.viettelottcommons.resource.Login;
import com.alticast.viettelottcommons.resource.Price;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.Program;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.resource.response.PurchaseResultRes;
import com.alticast.viettelottcommons.resource.wallet.WalletMethod;
import com.alticast.viettelottcommons.util.Logger;

import retrofit2.Call;

/**
 * Created by user on 2016-04-13.
 */
public class RentalPeriodProductPurchaseHelper {
    private static final String TAG = RentalPeriodProductPurchaseHelper.class.getSimpleName();

    public final static String PRICE_DAY = "PRICE_DAY" + RentalPeriodProductPurchaseHelper.class.getSimpleName();
    public final static String PRICE_WEEK = "PRICE_WEEK" + RentalPeriodProductPurchaseHelper.class.getSimpleName();
    public final static String PRICE_MONTH = "PRICE_MONTH" + RentalPeriodProductPurchaseHelper.class.getSimpleName();

    public final static String PARAM_TITLE = "PARAM_TITLE" +"RentalPeriodProductPurchaseHelper";
    public final static String PARAM_DESCRIPTION = "PARAM_DESCRIPTION" + RentalPeriodProductPurchaseHelper.class.getSimpleName();

    public final static String TYPE_PROGRAM = "TYPE_PROGRAM";
    public final static String TYPE_CHANNEL = "TYPE_CHANNEL";
    public final static String TYPE_CATCHUP = "TYPE_CATCHUP";


    public final static int TYPE_DAY = 1;
    public final static int TYPE_WEEK = 2;
    public final static int TYPE_MONTH = 3;

    private Context mContext;
    private FragmentManager mFragmentManager;
//    private ProgressDialogFragment mProgressDialogFragment;
    private PurchaseCallback mPurchaseCallback;
    private Product mProduct;
    private Program mProgram;
    private ChannelProduct mChannel;
    private String mProductType = null;
    private RentalPeriods mRentalPeriod = null;
    private PurchaseLoader.TypeCurrency purchaseType = PurchaseLoader.TypeCurrency.phone;
    private String mProductCategory = null;
    private boolean mIsAutoRenewal = true;

    private Bundle args = null;

    public RentalPeriodProductPurchaseHelper(Context context, FragmentManager fragmentManager, Program program
            , ChannelProduct channel, Product product, String productType, RentalPeriods rentalPeriod, String productCategory, boolean isAutoRenewal) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mProduct = product;
        mProgram = program;
        mChannel = channel;
        mProductType = productType;
        mRentalPeriod = rentalPeriod;
        mProductCategory = productCategory;
        mIsAutoRenewal = isAutoRenewal;
    }

    public void purchase() {
        //TODO 상품
        args = new Bundle();
        showDataPackageConfirmDialog(mRentalPeriod, mIsAutoRenewal);
    }

    public RentalPeriodProductPurchaseHelper setPurchaseCallback(PurchaseCallback purchaseCallback) {
        mPurchaseCallback = purchaseCallback;
        return this;
    }

    public void onPurchaseFinished(Program program) {
        Intent intent = new Intent(GlobalKey.VodController.REFRESH_DATA);
        intent.putExtra(Program.class.getName(), program);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        Intent intent1 = new Intent(GlobalKey.PURCHASE_COMPLETE);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent1);

        Intent intent2 = new Intent(GlobalKey.MainActivityKey.REFRESH_DATA);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent2);
        if (mPurchaseCallback != null)
            mPurchaseCallback.onSuccess(program);
    }

    private void checkPasswordDialog(final RentalPeriods rentalPeriod) {
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

        dialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button1) {
                    showProgress();
                    String inputMsg = dialog.getInputString();
                    //########################################################################################
                    //TODO [수정] match password 수정

                    HandheldAuthorization.LoginUserInfo user = HandheldAuthorization.getInstance().getCurrentUser();

                    FrontEndLoader.getInstance().requestLogin(user.getId(), inputMsg, new WindmillCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            Logger.d(TAG, "called onResult()");
                            final Login login = (Login) obj;
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
//                                    if (mProgressDialogFragment.isCancelled()) return;
                                    dismissProgress();
                                    processPurchase(rentalPeriod);
                                    dialog.dismiss();
                                }
                            });
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
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dialog.show(mFragmentManager, InputBoxDialog.CLASS_NAME);
            }
        });
    }

    private void showFindPasswordDialog() {
        final FindPasswordFragment dialog = new FindPasswordFragment();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dialog.show(mFragmentManager, FindPasswordFragment.CLASS_NAME);
            }
        });
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
//                int i = v.getId();
//                if (i == R.id.btnCancel) {
//                } else if (i == R.id.btnPurchase) {
//                    showDataPackageConfirmDialog(dialog.getSelectedRentalPeriod(), dialog.selectedAutoRenewal());
//
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
        dialog.setCancelable(false);
        dialog.setSrc(null, product);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnConfirm) {
                    Logger.d(TAG, "called onClick");
                    showDataPackageConfirmDialog(dialog.getChoseRental(), true);
                    dialog.dismiss();
                } else if(v.getId() == R.id.btnCancel) {
                    dialog.dismiss();
                    if(mPurchaseCallback != null) mPurchaseCallback.onCancel();
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


    private void showDataPackageConfirmDialog(final RentalPeriods rentalPeriod, boolean isAutoRenewal) {
        DialogFragment oldDialog = (DialogFragment) mFragmentManager.findFragmentByTag(PurchaseDataPackageConfirmDialogPhase2.CLASS_NAME);
        if (oldDialog != null) oldDialog.dismiss();

        final PurchaseDataPackageConfirmDialogPhase2 dialog = new PurchaseDataPackageConfirmDialogPhase2();
        dialog.setSrc(mProduct, rentalPeriod);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (v.getId() == R.id.btnConfirm) {

                    if(HandheldAuthorization.getInstance().isPoorUser() && HandheldAuthorization.getInstance().getPaymentMethod() == AuthManager.ChargeAccount.CHARGE_ACCOUNT_VWALLET) {
                        VWalletLoader.getInstance().checkWalletBalance(rentalPeriod.getPriceValue(), new WindmillCallback() {
                            @Override
                            public void onSuccess(Object obj) {
                                Float result = (Float) obj;
                                if(result >= 0) {
                                    boolean isQuickOption = HandheldAuthorization.getInstance().isQuickOption();
                                    if (!isQuickOption) {
                                        processPurchase(rentalPeriod);
                                    } else {
                                        checkPasswordDialog(rentalPeriod);
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
                                            } else if(v.getId() == R.id.btnCancel) {
                                                dialog.dismiss();
                                                if(mPurchaseCallback != null) mPurchaseCallback.onCancel();
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
                                if(mProduct.getRental_periods() != null && mProduct.getRental_periods().size() > 1) {
                                    showPackagePeriodDialog(mProduct);
                                }
                            }

                            @Override
                            public void onError(ApiError error) {
                                if(mProduct.getRental_periods() != null && mProduct.getRental_periods().size() > 1) {
                                    showPackagePeriodDialog(mProduct);
                                }
                            }
                        });
                    } else {
                        boolean isQuickOption = HandheldAuthorization.getInstance().isQuickOption();
                        if (!isQuickOption) {
                            processPurchase(rentalPeriod);
                        } else {
                            checkPasswordDialog(rentalPeriod);
                        }
                    }




                } else if(v.getId() == R.id.btnCancel) {
                    if(mProduct.getRental_periods() != null && mProduct.getRental_periods().size() > 1) {
                        showPackagePeriodDialog(mProduct);
                    }
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

    private void showPurchaseReConfirmDialog(final RentalPeriods rentalPeriod) {

        DialogFragment oldDialog = (DialogFragment) mFragmentManager.findFragmentByTag(PurchaseDataPackageReConfirmDialogPhase2.CLASS_NAME);
        if (oldDialog != null) oldDialog.dismiss();

        final PurchaseDataPackageReConfirmDialogPhase2 dialog = new PurchaseDataPackageReConfirmDialogPhase2();
        dialog.setSrc(mProduct, rentalPeriod);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (v.getId() == R.id.btnConfirm) {
                    boolean isQuickOption = HandheldAuthorization.getInstance().isQuickOption();
                    if (!isQuickOption) {
                        processPurchase(rentalPeriod);
                    } else {
                        checkPasswordDialog(rentalPeriod);
                    }
                } else if(v.getId() == R.id.btnCancel) {
                    showPackagePeriodDialog(mProduct);
                }

            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dialog.show(mFragmentManager, PurchaseDataPackageReConfirmDialogPhase2.CLASS_NAME);
            }
        });

//        final ReConfirmDialog dialog = new ReConfirmDialog();
//        dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v.getId() == R.id.yes_button) {
//                    boolean isQuickOption = HandheldAuthorization.getInstance().isQuickOption();
//                    if (!isQuickOption) {
//                        processPurchase(rentalPeriod);
//                    } else {
//                        checkPasswordDialog(rentalPeriod);
//                    }
//                }
//                dialog.dismiss();
//            }
//        });
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                dialog.show(mFragmentManager, ReConfirmDialog.CLASS_NAME);
//            }
//        });
    }

    private void processPurchase(RentalPeriods rentalPeriod) {
        Logger.d(TAG, "called processPurchase()- rentalPeriod : " + rentalPeriod.toString());
        if (rentalPeriod != null) {
            String contentId = null;
            if (mProductType == TYPE_PROGRAM)
                contentId = mProgram != null ? mProgram.getId() : "";
            else if (mProductType == TYPE_CHANNEL)
                contentId = mChannel != null ? mChannel.getId() : "";
            else
                contentId = null;//TODO catchup

            final Price price = rentalPeriod.getPrice(PurchaseLoader.currencyMap.get(purchaseType));
//            mLoaderHelper.load(new VipPacakagePurchaseCreateLoader(mContext, mProduct,
//                    purchaseType, mProductCategory, mEntryPath, rentalPeriod, contentId, mIsAutoRenewal), this);
            showProgress();

            PurchaseLoader.getInstance().purchaseCreateRentalPeriod(contentId, mProduct, mProductCategory, rentalPeriod, true, new WindmillCallback() {
                @Override
                public void onSuccess(Object obj) {

                    final PurchaseResultRes purchaseResultRes = (PurchaseResultRes) obj;


                    PurchaseLoader.getInstance().paymentsCreate(purchaseType,mProgram, mProduct, price.getValue(), purchaseResultRes.getId(), mProductCategory, new WindmillCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            dismissProgress();
                            App.getToast(mContext, getString(R.string.vod_noti), mProduct.getName(), false).show();
                            mProduct.setPurchaseId(purchaseResultRes.getPurchaser_id());
//                            onPurchaseFinished(mProgram);

                            FrontEndLoader.getInstance().getMyAccount(new WindmillCallback() {
                                @Override
                                public void onSuccess(Object obj) {
                                    onPurchaseFinished(mProgram);
                                    dismissProgress();
                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    onPurchaseFinished(mProgram);
                                    dismissProgress();
                                }

                                @Override
                                public void onError(ApiError error) {
                                    onPurchaseFinished(mProgram);
                                    dismissProgress();
                                }
                            }, false, (GlobalActivity) mContext, false);

//                            onPurchaseFinished(mProgram);
//                            dismissProgress();
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

        }
        //TODO 타입이 없는 경우 어떻게 할지?
    }

    private String getString(int resId, Object... params) {
        return mContext.getString(resId, params);
    }

    private void showProgress() {
        ((GlobalActivity) mContext).showProgress();
//        if (mProgressDialogFragment == null)
//            mProgressDialogFragment = new ProgressDialogFragment();
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                mProgressDialogFragment.show(mFragmentManager, ProgressDialogFragment.CLASS_NAME);
//            }
//        });
    }

    private void dismissProgress() {
        ((GlobalActivity) mContext).hideProgress();
//        if (mProgressDialogFragment == null) return;
//        try {
//            new Handler().post(new Runnable() {
//                @Override
//                public void run() {
//                    mProgressDialogFragment.dismiss();
//                }
//            });
//        } catch (NullPointerException ignored) {
//        }
    }

//    private void showCannotPurchaseDialog() {
//        final MessageDialog dialog = new MessageDialog();
//        Bundle args = new Bundle();
//        args.putString(MessageDialog.PARAM_TITLE, getString(R.string.notice));
//        args.putString(MessageDialog.PARAM_MESSAGE, getString(R.string.msgCannotPurchase));
//        args.putString(MessageDialog.PARAM_BUTTON_1, getString(R.string.close));
//        dialog.setArguments(args);
//        dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v.getId() == R.id.button1) {
//                }
//                dialog.dismiss();
//
//            }
//        });
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                dialog.show(mFragmentManager, MessageDialog.CLASS_NAME);
//            }
//        });
//        //dialog.show(mFragmentManager, MessageDialog.CLASS_NAME);
//    }

    public interface PurchaseCallback {
        public void onSuccess(Object obj);
        public void onCancel();
    }


//    private String paymentProductType(String mProductType) {
//        Logger.d(TAG, "called paymentProductType() - productType :" + mProductType);
//        String type = "";
//        if (mProductType == TYPE_PROGRAM) {
//            type = "vod";
//        } else if (mProductType == TYPE_CATCHUP) {
//            type = "catchup";
//        } else if (mProductType == TYPE_CHANNEL) {
//            type = "channel";
//        }
//        return type;
//    }
}
