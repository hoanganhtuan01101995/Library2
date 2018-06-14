package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.Program;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;
import com.alticast.viettelottcommons.widget.FontCheckBox;

public class PaymentOptionDialogPhase extends DialogFragment {
    private static final String TAG = PaymentOptionDialogPhase.class.getSimpleName();
    public static final String CLASS_NAME = PaymentOptionDialogPhase.class.getName();

    public static final String PARAM_IS_POINT_USER = "PARAM_IS_POINT_USER";
    public static final String PARAM_PACKAGES = "PARAM_PACKAGES";

    private View.OnClickListener mOnClickListener = null;
    private Product product;
    private Program program;
    private boolean isPointUser;

    FontCheckBox checkIdPostPaid;
    FontCheckBox checkIdPrePaid;

    private int paymentOption;

    public void setSrc(Product product, Program program, boolean isPointUser){
        this.product = product;
        this.program = program;
        this.isPointUser = isPointUser;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_payment_option);
        View v = dialog.getWindow().getDecorView();

        TextView originalPrice = (TextView) v.findViewById(R.id.originalPrice);
        TextView totalPrice = (TextView) v.findViewById(R.id.totalPrice);
        TextView duration = (TextView) v.findViewById(R.id.duration);
        checkIdPostPaid = (FontCheckBox) v.findViewById(R.id.checkIdPostPaid);
        checkIdPrePaid = (FontCheckBox) v.findViewById(R.id.checkIdPrePaid);

//        LinearLayout layoutPromotion = (LinearLayout) v.findViewById(R.id.layoutPromotion);
        RelativeLayout postPaidLayout = (RelativeLayout) v.findViewById(R.id.postPaidLayout);
        RelativeLayout prepaidLayout = (RelativeLayout) v.findViewById(R.id.prepaidLayout);
//        if(product.getDiscountRatio() > 0) {
//            layoutPromotion.setVisibility(View.VISIBLE);
//        } else {
//            layoutPromotion.setVisibility(View.GONE);
//        }

        int paymentOptions = product.getPaymentOptions();
        switch (paymentOptions) {
            case Product.PREPAID_ONLY:
                paymentOption = paymentOptions;
                setEnableButtonWithDim(postPaidLayout, false);
                break;
            case Product.POSTPAID_ONLY:
                paymentOption = paymentOptions;
                setEnableButtonWithDim(prepaidLayout, false);
                break;
            case Product.BOTH:
                paymentOption = Product.POSTPAID_ONLY;
                break;
        }
        setPaymentOption(paymentOption);

//        originalPrice.setText(TextUtils.getNumberString(rentalPeriods.getPriceValue()) + " VNĐ");
//        totalPrice.setText(rentalPeriods.getPriceString(product) + " VNĐ");

        if(isPointUser){
            originalPrice.setText(TextUtils.getNumberString(product.getPriceValue(Product.CURRENCY_PNT)) + " VNĐ");
            totalPrice.setText(TextUtils.getNumberString(product.getFinalPrice(Product.CURRENCY_PNT)) + " VNĐ");
        } else {
            originalPrice.setText(TextUtils.getNumberString(product.getPriceValue(Product.CURRENCY_VND)) + " VNĐ");
            totalPrice.setText(TextUtils.getNumberString(product.getFinalPrice(Product.CURRENCY_VND)) + " VNĐ");
        }

        duration.setText(product.getDurationString(getContext()));

        v.findViewById(R.id.btnConfirm).setOnClickListener(onClickListener);
        v.findViewById(R.id.btnCancel).setOnClickListener(onClickListener);
        v.findViewById(R.id.postPaidLayout).setOnClickListener(onClickListener);
        v.findViewById(R.id.prepaidLayout).setOnClickListener(onClickListener);


        return dialog;
    }

    public void setEnableButtonWithDim(View button, boolean enable) {
        button.setEnabled(enable);
        if (enable) {
            button.setAlpha(1);
        } else {
            button.setAlpha(PhoneLoginFragmentPhase2.ALPHA_DIM);
        }
    }

    public void setPaymentOption(int paymentOption) {
        this.paymentOption = paymentOption;
        if(paymentOption == Product.PREPAID_ONLY) {
            checkIdPrePaid.setChecked(true);
            checkIdPostPaid.setChecked(false);
        } else {
            checkIdPrePaid.setChecked(false);
            checkIdPostPaid.setChecked(true);
        }
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.btnConfirm) {
                product.setPaymentOptionChose(paymentOption);
                mOnClickListener.onClick(v);
            } else if(i == R.id.btnCancel) {
                mOnClickListener.onClick(v);
            } else if(i == R.id.postPaidLayout) {
                setPaymentOption(Product.POSTPAID_ONLY);
            } else if(i == R.id.prepaidLayout) {
                setPaymentOption(Product.PREPAID_ONLY);
            }

        }
    };

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity != null && activity instanceof GlobalActivity) {
            ((GlobalActivity) activity).onDismisDialog();
        }
    }
}
