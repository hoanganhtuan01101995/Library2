package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.util.NetworkUtil;

public class Login3GFailDialog extends DialogFragment {
    public static final String CLASS_NAME = Login3GFailDialog.class.getName();

    private static final String TAG = Login3GFailDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;
    private OnDismissListener mOnDismissListener;
    private NetworkUtil.NetworkType networkType;

    public void setNetworkType(NetworkUtil.NetworkType networkType) {
        this.networkType = networkType;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login_3g_fail);
        View v = dialog.getWindow().getDecorView();

        TextView txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        Button button1 = (Button) v.findViewById(R.id.btnok);
        button1.setOnClickListener(mOnClickListener);

        if(networkType != null && networkType == NetworkUtil.NetworkType.WIFI) {
            txtMessage.setText(getString(R.string.loginNoticeChangeWifiTo3G));
        } else {
            txtMessage.setText(getString(R.string.loginNoticeChangeTo3G));
        }

        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null) mOnDismissListener.onDismiss(dialog);

        final Activity activity = getActivity();
        if (activity != null && activity instanceof GlobalActivity) {
            ((GlobalActivity) activity).onDismisDialog();
        }
    }
}
