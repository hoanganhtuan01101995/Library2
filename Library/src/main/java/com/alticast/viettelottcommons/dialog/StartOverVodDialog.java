package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.widget.FontTextView;

public class StartOverVodDialog extends DialogFragment {
    public static final String CLASS_NAME = StartOverVodDialog.class.getName();

    public static final String PARAM_TITLE = CLASS_NAME + ".PARAM_TITLE";
    public static final String PARAM_SUBTITLE1 = CLASS_NAME + ".PARAM_SUBTITLE1";
    public static final String PARAM_ID = CLASS_NAME + ".PARAM_ID";
    public static final String PARAM_PERCENT = CLASS_NAME + ".PERCENT";
    public static final String PARAM_INSTRUCTION = CLASS_NAME + ".PARAM_INSTRUCTION";
    public static final String PARAM_POSTER = CLASS_NAME + ".PARAM_POSTER";

    public static final String PARAM_BUTTON1 = CLASS_NAME + ".PARAM_BUTTON1";
    public static final String PARAM_BUTTON2 = CLASS_NAME + ".PARAM_BUTTON2";

    public static final String PARAM_CANCEL_ON_TOUCH_OUTSIDE = CLASS_NAME + ".PARAM_CANCEL_ON_TOUCH_OUTSIDE";

    private static final String TAG = StartOverVodDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;
    private OnDismissListener mOnDismissListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_device_limit);
        dialog.setCancelable(false);
        View v = dialog.getWindow().getDecorView();

        Bundle arguments = getArguments();
        if (arguments != null) {

            String title = arguments.getString(PARAM_TITLE);
            String subTitle1 = arguments.getString(PARAM_SUBTITLE1);
            int percent = arguments.getInt(PARAM_PERCENT);
            String instruction = arguments.getString(PARAM_INSTRUCTION);
            Bitmap poster = arguments.getParcelable("BitmapImage");

            Button button1 = (Button) v.findViewById(R.id.btnStartOver);
            Button button2 = (Button) v.findViewById(R.id.btnContinue);
            button1.setOnClickListener(mOnClickListener);
            button2.setOnClickListener(mOnClickListener);

            FontTextView txt_title = (FontTextView) v.findViewById(R.id.title);
            FontTextView txt_subTitle1 = (FontTextView) v.findViewById(R.id.subtitle1);
            FontTextView txt_introduction = (FontTextView) v.findViewById(R.id.introduction);
            SeekBar seekBar = (SeekBar) v.findViewById(R.id.seekbar);
            ImageView imvPoster = (ImageView) v.findViewById(R.id.poster);


            txt_title.setText(title);
            txt_subTitle1.setText(subTitle1);
            txt_introduction.setText(instruction);
            imvPoster.setImageBitmap(poster);
            seekBar.setProgress(percent);

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
