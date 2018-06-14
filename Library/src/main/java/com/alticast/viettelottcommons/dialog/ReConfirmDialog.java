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

public class ReConfirmDialog extends DialogFragment {
    public static final String CLASS_NAME = ReConfirmDialog.class.getName();
    private static final String TAG = ReConfirmDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_re_confirm);
        View v = dialog.getWindow().getDecorView();
        TextView hotLineView = (TextView) v.findViewById(R.id.hot_line);
        v.findViewById(R.id.yes_button).setOnClickListener(mOnClickListener);
        v.findViewById(R.id.cancel_button).setOnClickListener(mOnClickListener);
        hotLineView.setVisibility(View.GONE);
        return dialog;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
