package com.alticast.viettelottcommons.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.StringUtil;
import com.alticast.viettelottcommons.util.TextUtils;


/**
 * Created by user on 2016-04-18.
 */
public class PurchaseDataPacakgeConfirmDialog extends DialogFragment {
    private static final String TAG = PurchaseDataPacakgeConfirmDialog.class.getSimpleName();
    public static final String CLASS_NAME = PurchaseDataPacakgeConfirmDialog.class.getName();
    public static final String PARAM_TYPE = "PARAM_TYPE";
    public static final String PARAM_RENTALPERIOD = "PARAM_RENTALPERIOD";
    public static final String PARAM_RENTALPERIOD_PRICE = "PARAM_RENTALPERIOD_PRICE";

    private View.OnClickListener mOnClickListener = null;
    private DialogInterface.OnDismissListener mOnDismissListener = null;
    private float totalPrice = 0f;
    public final static String PARAM_TITLE = "PARAM_TITLE" + "RentalPeriodProductPurchaseHelper";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_data_package);
        View v = dialog.getWindow().getDecorView();

        Bundle arg = getArguments();
        int type = -1;

        RentalPeriods rentalPeriod = null;

        String name = getString(R.string.titleViettel3Gpackage);
        if (arg != null) {
            type = arg.getInt(PARAM_TYPE);
            name = arg.getString(PARAM_TITLE, name);
            rentalPeriod = arg.getParcelable(PARAM_RENTALPERIOD);
            totalPrice = arg.getFloat(PARAM_RENTALPERIOD_PRICE);
        }

        TextView txtPackageName = (TextView) v.findViewById(R.id.txtPackageName);
        TextView txtPeriod = (TextView) v.findViewById(R.id.txtPeriod);
        TextView txtPrice = (TextView) v.findViewById(R.id.txtPrice);

        Button btnPurchase = (Button) v.findViewById(R.id.btnPurchase);
        Button btnCancel = (Button) v.findViewById(R.id.btnCancel);

        btnPurchase.setOnClickListener(mOnClickListener);
        btnCancel.setOnClickListener(mOnClickListener);

        txtPackageName.setText(name);

        String price = getString(R.string.priceOnlyOneDay, TextUtils.getNumberString(totalPrice));
        Logger.d(TAG, "price  : " + price);
        if(rentalPeriod.getPeriod() != 0) {
            txtPrice.setText(price + "/" + StringUtil.getPeriodString(getContext(), rentalPeriod.getPeriod()));
        } else {
            txtPrice.setText(price);
        }

        return dialog;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if(!this.isDetached()) {
//            super.dismiss();
//            if (mOnDismissListener != null) mOnDismissListener.onDismiss(getDialog());
//        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Logger.d(TAG, "called onDissmiss()");
        super.onDismiss(dialog);
        if (mOnDismissListener != null) mOnDismissListener.onDismiss(dialog);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

}
