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
import com.alticast.viettelottcommons.widget.FontTextView;

public class CommonMessageDialog extends DialogFragment {
    public static final String CLASS_NAME = CommonMessageDialog.class.getName();

    public static final String PARAM_TITLE = CLASS_NAME + ".PARAM_TITLE";
    public static final String PARAM_SUBTITLE = CLASS_NAME + ".PARAM_SUBTITLE";
    public static final String PARAM_ID = CLASS_NAME + ".PARAM_ID";
    public static final String PARAM_NUM_DEVICE_LIMIT = CLASS_NAME + ".PARAM_NUM_DEVICE_LIMIT";

    public static final String PARAM_BUTTON_1 = CLASS_NAME + ".PARAM_BUTTON_1";
    public static final String PARAM_BUTTON_2 = CLASS_NAME + ".PARAM_BUTTON_2";
    public static final String PARAM_BUTTON_3 = CLASS_NAME + ".PARAM_BUTTON_3";

    public static final String PARAM_CANCEL_ON_TOUCH_OUTSIDE = CLASS_NAME + ".PARAM_CANCEL_ON_TOUCH_OUTSIDE";

    private static final String TAG = CommonMessageDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;
    private OnDismissListener mOnDismissListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_common_message);
        dialog.setCancelable(false);
        View v = dialog.getWindow().getDecorView();

        Bundle arguments = getArguments();
        if (arguments != null) {

            String title = arguments.getString(PARAM_TITLE);
            String subTitle = arguments.getString(PARAM_SUBTITLE);
            String ID = arguments.getString(PARAM_ID);
            int registerDeviceTotal = arguments.getInt(PARAM_NUM_DEVICE_LIMIT);

            String button1Text = arguments.getString(PARAM_BUTTON_1);
            String button2Text = arguments.getString(PARAM_BUTTON_2);
            String button3Text = arguments.getString(PARAM_BUTTON_3);


            Button button1 = (Button) v.findViewById(R.id.btn1);
            Button button2 = (Button) v.findViewById(R.id.btn2);
            Button button3 = (Button) v.findViewById(R.id.btn3);

            if (button1Text != null) {
                button1.setText(button1Text);
                button1.setVisibility(View.VISIBLE);
            } else {
                button1.setVisibility(View.GONE);
            }

            if (button2Text != null) {
                button2.setText(button2Text);
                button2.setVisibility(View.VISIBLE);
            } else {
                button2.setVisibility(View.GONE);
            }

            if (button3Text != null) {
                button3.setText(button3Text);
                button3.setVisibility(View.VISIBLE);
            } else {
                button3.setVisibility(View.GONE);
            }

            button1.setOnClickListener(mOnClickListener);
            button2.setOnClickListener(mOnClickListener);
            button3.setOnClickListener(mOnClickListener);

            FontTextView txt_title = (FontTextView) v.findViewById(R.id.title);
            FontTextView txt_subTitle = (FontTextView) v.findViewById(R.id.subtitle);

            txt_title.setText(title);

            if (subTitle != null) {
                txt_subTitle.setVisibility(View.VISIBLE);
                txt_subTitle.setText(subTitle);
            } else {
                txt_subTitle.setVisibility(View.GONE);
            }

            boolean cancelOnTouchOutside = arguments.getBoolean(PARAM_CANCEL_ON_TOUCH_OUTSIDE, true);
            dialog.setCanceledOnTouchOutside(cancelOnTouchOutside);

        }
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
