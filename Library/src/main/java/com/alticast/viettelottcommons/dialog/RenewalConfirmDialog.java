package com.alticast.viettelottcommons.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.util.Logger;

public class RenewalConfirmDialog extends DialogFragment {
    public static final String CLASS_NAME = RenewalConfirmDialog.class.getName();
    private static final String TAG = RenewalConfirmDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;
    private String pkgName;
    private boolean isCancelSubscription;

    public void setSrc(String pkgName, boolean isCancelSubscription) {
        this.pkgName = pkgName;
        this.isCancelSubscription = isCancelSubscription;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_revewal_confirm);
        View v = dialog.getWindow().getDecorView();
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView packageName = (TextView) v.findViewById(R.id.packageName);
        TextView txtDescription = (TextView) v.findViewById(R.id.txtDescription);

        packageName.setText("" + pkgName);

        if(isCancelSubscription) {
            title.setText(getString(R.string.cancelSubcriptionTitle));
            txtDescription.setText(getString(R.string.cancelSubcriptionMessage));
        } else {
            title.setText(getString(R.string.subcriptionTitle));
            txtDescription.setText(getString(R.string.subcriptionMessage));
        }

        v.findViewById(R.id.btnConfirm).setOnClickListener(mOnClickListener);
        v.findViewById(R.id.btnCancel).setOnClickListener(mOnClickListener);
        return dialog;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
