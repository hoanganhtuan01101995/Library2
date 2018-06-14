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

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;

public class LoginConfirmDialog extends DialogFragment {
    public static final String CLASS_NAME = LoginConfirmDialog.class.getName();

    private static final String TAG = LoginConfirmDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;
    private OnDismissListener mOnDismissListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login_confirm);
        View v = dialog.getWindow().getDecorView();

        Button button1 = (Button) v.findViewById(R.id.btnlogin);
        Button button2 = (Button) v.findViewById(R.id.btnlogin3G);
        Button button3 = (Button) v.findViewById(R.id.btncancel);
        button1.setOnClickListener(mOnClickListener);
        button2.setOnClickListener(mOnClickListener);
        button3.setOnClickListener(mOnClickListener);

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
