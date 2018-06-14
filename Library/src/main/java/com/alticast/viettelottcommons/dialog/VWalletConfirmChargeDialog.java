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
import com.alticast.viettelottcommons.fragment.vwallet.FragmentChargeManager;
import com.alticast.viettelottcommons.resource.ChargeAmountObj;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.Program;
import com.alticast.viettelottcommons.resource.WalletTopupMethod;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;

public class VWalletConfirmChargeDialog extends DialogFragment {
    private static final String TAG = VWalletConfirmChargeDialog.class.getSimpleName();
    public static final String CLASS_NAME = VWalletConfirmChargeDialog.class.getName();

    public static final String PARAM_IS_POINT_USER = "PARAM_IS_POINT_USER";
    public static final String PARAM_PACKAGES = "PARAM_PACKAGES";

    private View.OnClickListener mOnClickListener = null;
    private ChargeAmountObj chargeAmountObj;
    private WalletTopupMethod walletTopupMethod;

    public void setSrc(ChargeAmountObj chargeAmountObj, WalletTopupMethod walletTopupMethod){
        this.chargeAmountObj = chargeAmountObj;
        this.walletTopupMethod = walletTopupMethod;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_vwallet_confirm);
        View v = dialog.getWindow().getDecorView();

        if(walletTopupMethod.getMethod().equals("phone")) {
            TextView txtTopupAmout = (TextView) v.findViewById(R.id.txtTopupAmout);
            TextView txtTopupBonus = (TextView) v.findViewById(R.id.txtTopupBonus);
            TextView txtBonus = (TextView) v.findViewById(R.id.txtBonus);

            txtTopupAmout.setText(TextUtils.getNumberString(chargeAmountObj.getAmount()));
            txtTopupBonus.setText(TextUtils.getNumberString(chargeAmountObj.getBonusMoney(walletTopupMethod)));
            txtBonus.setText(chargeAmountObj.getBonusDisplay(walletTopupMethod));
        } else {
            v.findViewById(R.id.layoutInfo).setVisibility(View.GONE);
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
