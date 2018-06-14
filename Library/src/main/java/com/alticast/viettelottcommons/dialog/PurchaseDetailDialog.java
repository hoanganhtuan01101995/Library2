package com.alticast.viettelottcommons.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.activity.App;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.loader.PurchaseLoader;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.Purchase;
import com.alticast.viettelottcommons.resource.Vod;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;
import com.alticast.viettelottcommons.util.TimeUtil;
import com.alticast.viettelottcommons.widget.FontCheckBox;

import retrofit2.Call;


public class PurchaseDetailDialog extends DialogFragment implements CompoundButton.OnCheckedChangeListener {
    public static final String CLASS_NAME = PurchaseDetailDialog.class.getName();

    public static final String PARAM_PURCHASE = CLASS_NAME + ".PARAM_TITLE";
    public static final String PARAM_PROGRAM = CLASS_NAME + ".PARAM_PURCHASED_DATE";

    private static final String TAG = PurchaseDetailDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;
    //TODO [확인] pre-payment
    private static final String FORMAT_DATE = "EEE, dd/MM";

    private FontCheckBox checkAutoRenewal = null;
//    private FontButtonView stop_subscription_button = null;
    private ProgressDialogFragment mProgressDialogFragment;
    private DialogInterface.OnDismissListener mOnDismissListener;
    private boolean isStateChagnegd = false;
    private Purchase mPurchase = null;
    private Vod mVod = null;
    public void setSrc(Purchase purchase){
        this.mPurchase = purchase;
    }

    public void setSrc(Purchase purchase,Vod vod){
        this.mPurchase = purchase;
        this.mVod = vod;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_purchase_detail);
        View v = dialog.getWindow().getDecorView();

        isStateChagnegd = false;
        checkAutoRenewal = (FontCheckBox) v.findViewById(R.id.checkAutoRenewal);
//        stop_subscription_button = (FontButtonView) v.findViewById(R.id.stop_subscription_button);
        mProgressDialogFragment = new ProgressDialogFragment();
        if (mPurchase != null) {

            String productType = mPurchase.getProductType();
            long purchasedDate = mPurchase.getPurchasedDate();
            long availableDate = mPurchase.getExpireDate();
            int cclass = mPurchase.getCClass();


            //TODO add
            v.findViewById(R.id.txtWalletMsg).setVisibility(mPurchase.isPaymentTypeWallet() ? View.VISIBLE : View.GONE);

            String title;
//            ((TextView) v.findViewById(R.id.purchase_type)).setText(getResources().getText(mPurchase.getPaymentMethod().equals(Product.CURRENCY_PNT) ? R.string.point : R.string.price));
            if ("subscription".equals(productType) || "fpackage".equals(productType)) {
                title = mPurchase.getProductName();
                String canceledDate = mPurchase.getCanceledDate();
                boolean isCanceled = canceledDate != null && canceledDate.length() > 0 && !canceledDate.equals("null");

                //((TextView) v.findViewById(R.id.payment_method)).append(" (" + getString(R.string.per_monthly) + ")");
                TextView txtPeriod = ((TextView) v.findViewById(R.id.txtPeriod));
                txtPeriod.setVisibility(View.VISIBLE);
                int rentalPeriod = TimeUtil.secToDay(mPurchase.getRentalPeriod());
                switch (rentalPeriod) {
                    case 0:
                        if(AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
                            txtPeriod.setVisibility(View.GONE);
                        } else {
                            txtPeriod.setText(getString(R.string.purchaseDialogPerMonth));
                        }
                        break;
                    case 1:
                        txtPeriod.setText(getString(R.string.purchaseDialogPerDay));
                        break;
                    case 7:
                        txtPeriod.setText(getString(R.string.purchaseDialogPerWeek));
                        break;
                    default:
                        int month = rentalPeriod / 30;
                        if(month == 0) {
                            txtPeriod.setText("(" + rentalPeriod + " " + getString(R.string.justDay) + ")");
                        }
                        else if(month == 1) {
                            txtPeriod.setText(getString(R.string.purchaseDialogPerMonth));
                        } else {
                            txtPeriod.setText(" (" + month + " " + getString(R.string.justMothn) + ")");
                        }

                }


                //###################################################################################################
                //TODO [확인] cancel btn 삭제

                Logger.d(TAG, "Test _____ PurchaseList : " + mPurchase.isCanCelable());


                boolean isCancelable = (cclass == Purchase.CCLASS_HANDHELD && mPurchase.isCanCelable());

//                stop_subscription_button.setOnClickListener();

                if (isCancelable) {
                    checkAutoRenewal.setVisibility(View.VISIBLE);
//                    checkAutoRenewal.setTag(mPurchase.getId());
                    checkAutoRenewal.setChecked(!mPurchase.isCanceled());
//                    checkAutoRenewal.setOnCheckedChangeListener(this);
//                    stop_subscription_button.setVisibility(View.GONE);

//                    stop_subscription_button.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mProgressDialogFragment.show(getFragmentManager(), null);
//                            PurchaseLoader.getInstance().automaticRenewal(mPurchase.getId(), mPurchase.getChangeStatus(), new WindmillCallback() {
//                                @Override
//                                public void onSuccess(Object obj) {
////                                    checkAutoRenewal.setChecked(!checkAutoRenewal.isChecked());
//                                    isStateChagnegd = true;
//                                    mProgressDialogFragment.dismiss();
//                                    dismiss();
//                                }
//
//                                @Override
//                                public void onFailure(Call call, Throwable t) {
//                                    App.showAlertDialogNetwork(getActivity(), getChildFragmentManager(), null);
//                                    mProgressDialogFragment.dismiss();
//                                }
//
//                                @Override
//                                public void onError(ApiError error) {
//                                    App.showAlertDialog(getActivity(), getChildFragmentManager(), error, null);
//                                    mProgressDialogFragment.dismiss();
//                                }
//                            });
//                        }
//                    });


                    checkAutoRenewal.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if(event.getAction() == MotionEvent.ACTION_UP) {
                                mProgressDialogFragment.show(getFragmentManager(), null);
                                PurchaseLoader.getInstance().automaticRenewal(mPurchase.getId(), mPurchase.getChangeStatus(), new WindmillCallback() {
                                    @Override
                                    public void onSuccess(Object obj) {
                                        checkAutoRenewal.setChecked(!checkAutoRenewal.isChecked());
                                        isStateChagnegd = true;
                                        mProgressDialogFragment.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call call, Throwable t) {
                                        App.showAlertDialogNetwork(getActivity(), getChildFragmentManager(), null);
                                        mProgressDialogFragment.dismiss();
                                    }

                                    @Override
                                    public void onError(ApiError error) {
                                        App.showAlertDialog(getActivity(), getChildFragmentManager(), error, null);
                                        mProgressDialogFragment.dismiss();
                                    }
                                });
                            }
                            return true;
                        }
                    });

                } else {
                    checkAutoRenewal.setVisibility(View.GONE);
//                    stop_subscription_button.setVisibility(View.GONE);
                }


//                  boolean isCancelable = (cclass == Purchase.CCLASS_HANDHELD || cclass == Purchase.CCLASS_STB_HANDHELD) && !isCanceled ;
//                  v.findViewById(R.id.stop_subscription_button).setVisibility(isCancelable ? View.VISIBLE : View.GONE);
//                  v.findViewById(R.id.message).setVisibility(isCanceled ? View.VISIBLE : View.GONE);
                //###################################################################################################
            } else {
                title = mVod != null ? mVod.getProgram().getTitle(WindmillConfiguration.LANGUAGE) : mPurchase.getProductName();


                v.findViewById(R.id.available_date_views).setVisibility(View.VISIBLE);
                ((TextView) v.findViewById(R.id.available_date)).setText(TextUtils.getDateString(availableDate, FORMAT_DATE).toUpperCase());

                v.findViewById(R.id.detail_button).setVisibility(mVod != null ? View.VISIBLE : View.GONE);
//                v.findViewById(R.id.delete_button).setVisibility(cclass == Purchase.CCLASS_HANDHELD ? View.VISIBLE : View.GONE);
                v.findViewById(R.id.delete_button).setVisibility(View.GONE);
                checkAutoRenewal.setVisibility(View.GONE);
            }
            ((TextView) v.findViewById(R.id.title)).setText(title);
            if (mPurchase.getPaymentValue() == 0) {
                ((TextView) v.findViewById(R.id.price)).setText(getString(R.string.lock_free));
                ((TextView) v.findViewById(R.id.payment_method)).setVisibility(View.GONE);
                ((TextView) v.findViewById(R.id.txtPeriod)).setVisibility(View.GONE);
            } else {
                ((TextView) v.findViewById(R.id.price)).setText(TextUtils.getNumberString(mPurchase.getPaymentValue()));
                ((TextView) v.findViewById(R.id.payment_method)).setVisibility(View.VISIBLE);
            }


            v.findViewById(R.id.purchased_date_views).setVisibility(View.VISIBLE);
            ((TextView) v.findViewById(R.id.purchased_date)).setText(TextUtils.getDateString(purchasedDate, FORMAT_DATE).toUpperCase());

            v.findViewById(R.id.stop_subscription_button).setOnClickListener(mOnClickListener);
            v.findViewById(R.id.detail_button).setOnClickListener(mOnClickListener);
            v.findViewById(R.id.close_button).setOnClickListener(mOnClickListener);
            v.findViewById(R.id.delete_button).setOnClickListener(mOnClickListener);
        }
        return dialog;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //TODO AUTO RENEWAL FUNCTION
        mProgressDialogFragment.show(getChildFragmentManager(), ProgressDialogFragment.CLASS_NAME);
    }



//    @Override
//    public void onResult(BaseLoader loader) {
//        mProgressDialogFragment.dismiss();
//        if (loader.getData() == null) {
//
//            checkAutoRenewal.setOnCheckedChangeListener(null);
//            checkAutoRenewal.setChecked(!checkAutoRenewal.isChecked());
//            checkAutoRenewal.setOnCheckedChangeListener(this);
//            showMessageDialog(getString(R.string.notice), loader.getError().getMessage());
//
//            Logger.d("AutomaticRenewalLoader", "result : " + false);
//        } else {
//
//            isStateChagnegd = true;
//            boolean result = ((AutomaticRenewalLoader) loader).getData();
//            Logger.d("AutomaticRenewalLoader", "result : " + result);
//        }
//
//
//    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }


//    private void showMessageDialog(String title, String message) {
//        final MessageDialog dialog = new MessageDialog();
//        Bundle args = new Bundle();
//        args.putString(MessageDialog.PARAM_TITLE, title);
//        args.putString(MessageDialog.PARAM_MESSAGE, message);
//        args.putString(MessageDialog.PARAM_BUTTON_1, getString(R.string.ok));
//        dialog.setArguments(args);
//        dialog.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show(getChildFragmentManager(), MessageDialog.CLASS_NAME);
//    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null && isStateChagnegd) mOnDismissListener.onDismiss(dialog);
    }
}
