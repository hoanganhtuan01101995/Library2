package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;

public class PurchaseDataPackageReConfirmDialogPhase2 extends DialogFragment {
    private static final String TAG = PurchaseDataPackageReConfirmDialogPhase2.class.getSimpleName();
    public static final String CLASS_NAME = PurchaseDataPackageReConfirmDialogPhase2.class.getName();

    public static final String PARAM_IS_POINT_USER = "PARAM_IS_POINT_USER";
    public static final String PARAM_PACKAGES = "PARAM_PACKAGES";

    private View.OnClickListener mOnClickListener = null;
    private RentalPeriods rentalPeriods;
    private Product product;

    public void setSrc(Product product, RentalPeriods rentalPeriods){
        this.product = product;
        this.rentalPeriods = rentalPeriods;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_confirm_data_package_p2);
        View v = dialog.getWindow().getDecorView();
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setText(getString(R.string.confirmPurchaseQuest));

        TextView packageName = (TextView) v.findViewById(R.id.packageName);
        TextView originalPrice = (TextView) v.findViewById(R.id.originalPrice);
        TextView totalPrice = (TextView) v.findViewById(R.id.totalPrice);
        TextView duration = (TextView) v.findViewById(R.id.duration);
        TextView description = (TextView) v.findViewById(R.id.description);
        description.setVisibility(View.GONE);

        LinearLayout layoutPromotion = (LinearLayout) v.findViewById(R.id.layoutPromotion);
        if(product.getDiscountRatio() > 0) {
            layoutPromotion.setVisibility(View.VISIBLE);
        } else {
            layoutPromotion.setVisibility(View.GONE);
        }

        String name = getString(R.string.titleViettel3Gpackage);
        packageName.setText(product.getName() != null ? product.getName() : name);
        originalPrice.setText(TextUtils.getNumberString(rentalPeriods.getPriceValue()) + " VNĐ");
        totalPrice.setText(rentalPeriods.getPriceString(product) + " VNĐ");
        duration.setText(rentalPeriods.getDurationString(getContext()));

        v.findViewById(R.id.btnConfirm).setOnClickListener(mOnClickListener);
        v.findViewById(R.id.btnCancel).setOnClickListener(mOnClickListener);


        return dialog;
    }

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
