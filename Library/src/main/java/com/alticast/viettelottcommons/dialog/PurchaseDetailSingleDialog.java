package com.alticast.viettelottcommons.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.Purchase;
import com.alticast.viettelottcommons.resource.Vod;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.StringUtil;
import com.alticast.viettelottcommons.util.TextUtils;
import com.alticast.viettelottcommons.util.TimeUtil;


public class PurchaseDetailSingleDialog extends DialogFragment implements CompoundButton.OnCheckedChangeListener {
    public static final String CLASS_NAME = PurchaseDetailSingleDialog.class.getName();

    public static final String PARAM_PURCHASE = CLASS_NAME + ".PARAM_TITLE";
    public static final String PARAM_PROGRAM = CLASS_NAME + ".PARAM_PURCHASED_DATE";

    private static final String TAG = PurchaseDetailSingleDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;
    //TODO [확인] pre-payment
    private static final String FORMAT_DATE = "HH:mm '-' dd/MM/yyyy";

//    private FontButtonView stop_subscription_button = null;
    private ProgressDialogFragment mProgressDialogFragment;
    private DialogInterface.OnDismissListener mOnDismissListener;
    private boolean isStateChagnegd = false;
    private Purchase mPurchase = null;
    private Vod vod = null;

    private long currentTime;

    public void setSrc(Purchase purchase, Vod vod){
        this.mPurchase = purchase;
        this.vod = vod;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        currentTime = System.currentTimeMillis();

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_purchase_detail_single);
        View v = dialog.getWindow().getDecorView();

        TextView title = (TextView) v.findViewById(R.id.title);
        TextView originalPrice = (TextView) v.findViewById(R.id.originalPrice);
        TextView startTime = (TextView) v.findViewById(R.id.startTime);
        TextView endTime = (TextView) v.findViewById(R.id.endTime);
        TextView status = (TextView) v.findViewById(R.id.status);



        isStateChagnegd = false;
        mProgressDialogFragment = new ProgressDialogFragment();
        if (mPurchase != null) {

            title.setText(mPurchase.getProductName());

            if (mPurchase.getPaymentValue() == 0) {
                originalPrice.setText(getString(R.string.lock_free));
                (v.findViewById(R.id.layoutDuration)).setVisibility(View.GONE);
            } else {
                originalPrice.setText(TextUtils.getNumberString(mPurchase.getPaymentValue()) + " VNĐ");
            }
            startTime.setText(TextUtils.getDateString(mPurchase.getPurchasedDate(), FORMAT_DATE).toUpperCase());
            endTime.setText(TextUtils.getDateString(mPurchase.getExpireDate(), FORMAT_DATE).toUpperCase());

            if(mPurchase.getExpireDate() < currentTime) {
                status.setText(getString(R.string.txtExpired));
            } else {
                long timeRamain = currentTime - mPurchase.getExpireDate();
                int rentalPeriod = TimeUtil.secToDay(timeRamain / 1000);
//                status.setText(getString(R.string.availabletowatch) + "(" + StringUtil.getPeriodTimeString(getContext(), rentalPeriod) +")");
                status.setText(getString(R.string.stillCanWatch));
            }

            v.findViewById(R.id.btnClose).setOnClickListener(mOnClickListener);
            v.findViewById(R.id.btnMoreDetail).setOnClickListener(mOnClickListener);
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
