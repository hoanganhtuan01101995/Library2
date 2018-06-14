package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.Program;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;

public class ProgramPurchaseDialogPhase2 extends DialogFragment {
    private static final String TAG = ProgramPurchaseDialogPhase2.class.getSimpleName();
    public static final String CLASS_NAME = ProgramPurchaseDialogPhase2.class.getName();

    public static final String PARAM_IS_POINT_USER = "PARAM_IS_POINT_USER";
    public static final String PARAM_PACKAGES = "PARAM_PACKAGES";

    private View.OnClickListener mOnClickListener = null;
    private Program program;
    private Product product;
    private boolean isPointUser;

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

        dialog.setContentView(R.layout.dialog_program_purchase_phase2);
        View v = dialog.getWindow().getDecorView();
        TextView packageName = (TextView) v.findViewById(R.id.packageName);
        TextView originalPrice = (TextView) v.findViewById(R.id.originalPrice);
        TextView totalPrice = (TextView) v.findViewById(R.id.totalPrice);
        TextView description = (TextView) v.findViewById(R.id.description);
        TextView txtAccountType = (TextView) v.findViewById(R.id.txtAccountType);

        txtAccountType.setText(isPointUser ? R.string.purchase_flexiplus_user_message : R.string.purchase_flexi_user_message);

        String name = product.isSingleProduct() && program != null ? program.getTitle(WindmillConfiguration.LANGUAGE) : product.getName();
        packageName.setText(name);
        if(isPointUser){
            originalPrice.setText(TextUtils.getNumberString(product.getPriceValue(Product.CURRENCY_PNT)) + " VNĐ");
            totalPrice.setText(TextUtils.getNumberString(product.getFinalPrice(Product.CURRENCY_PNT)) + " VNĐ");
        } else {
            originalPrice.setText(TextUtils.getNumberString(product.getPriceValue(Product.CURRENCY_VND)) + " VNĐ");
            totalPrice.setText(TextUtils.getNumberString(product.getFinalPrice(Product.CURRENCY_VND)) + " VNĐ");
        }
        if(product.getDescription() != null && !product.getDescription().isEmpty()) {
            description.setText(product.getDescription());
        } else {
            description.setVisibility(View.GONE);
        }


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
