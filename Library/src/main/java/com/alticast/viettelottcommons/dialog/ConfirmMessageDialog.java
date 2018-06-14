package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.util.Logger;

public class ConfirmMessageDialog extends DialogFragment {
    private static final String TAG = ConfirmMessageDialog.class.getSimpleName();
    public static final String CLASS_NAME = ConfirmMessageDialog.class.getName();

    public static final String PARAM_IS_POINT_USER = "PARAM_IS_POINT_USER";
    public static final String PARAM_PACKAGES = "PARAM_PACKAGES";

    private View.OnClickListener mOnClickListener = null;

    private String title, message;
    private String confirmTitle;

    public void setSrc(String title, String message, String confirmTitle) {
        this.title = title;
        this.message = message;
        this.confirmTitle = confirmTitle;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_confirm_message);



        View v = dialog.getWindow().getDecorView();

        TextView txtTitle = (TextView) v.findViewById(R.id.title);
        TextView txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        Button btnConfirm = (Button) v.findViewById(R.id.btnConfirm);

        txtTitle.setText(title);
        txtMessage.setText(message);
        btnConfirm.setText(confirmTitle);

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
