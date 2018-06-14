package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.adapter.SimpleListAdapter;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;

import java.util.ArrayList;

public class PurchaseDataPackageConfirmDialogPhase2 extends DialogFragment {
    private static final String TAG = PurchaseDataPackageConfirmDialogPhase2.class.getSimpleName();
    public static final String CLASS_NAME = PurchaseDataPackageConfirmDialogPhase2.class.getName();

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
        TextView packageName = (TextView) v.findViewById(R.id.packageName);
        TextView originalPrice = (TextView) v.findViewById(R.id.originalPrice);
        TextView totalPrice = (TextView) v.findViewById(R.id.totalPrice);
        TextView duration = (TextView) v.findViewById(R.id.duration);
        TextView description = (TextView) v.findViewById(R.id.description);
//        TextView txtPromotionNote = (TextView) v.findViewById(R.id.txtPromotionNote);

        LinearLayout layoutPromotion = (LinearLayout) v.findViewById(R.id.layoutPromotion);


        description.setVisibility(View.GONE);
//        if(product.getDescription() != null && !product.getDescription().isEmpty()) {
//            description.setText(product.getDescription());
//        } else {
//            description.setVisibility(View.GONE);
//        }

        if(product.getDiscountRatio() > 0 || product.getCheckCouponRes() != null) {
            layoutPromotion.setVisibility(View.VISIBLE);
//            txtPromotionNote.setVisibility(View.VISIBLE);
//            txtPromotionNote.setText(String.format(getContext().getString(R.string.promotionNote), "" + (int)(product.getDiscountRatio() * 100) + " %"));
        } else {
            layoutPromotion.setVisibility(View.GONE);
//            txtPromotionNote.setVisibility(View.GONE);
        }

        String name = getString(R.string.titleViettel3Gpackage);
        packageName.setText(product.getName() != null ? product.getName() : name);
        originalPrice.setText(TextUtils.getNumberString(rentalPeriods.getPriceValue()) + " VNĐ");
        totalPrice.setText(rentalPeriods.getPriceString(product) + " VNĐ");
        duration.setText(rentalPeriods.getDurationString(getContext()));

        v.findViewById(R.id.btnConfirm).setOnClickListener(onClickListener);
        v.findViewById(R.id.btnCancel).setOnClickListener(onClickListener);


        return dialog;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btnCancel) {
                product.setCheckCouponRes(null);
            }
            mOnClickListener.onClick(v);
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
