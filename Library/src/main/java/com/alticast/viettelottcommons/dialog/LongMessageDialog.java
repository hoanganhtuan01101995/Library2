package com.alticast.viettelottcommons.dialog;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;

public class LongMessageDialog extends DialogFragment {
    public static final String CLASS_NAME = LongMessageDialog.class.getName();

    public static final String PARAM_TITLE = CLASS_NAME + ".PARAM_TITLE";
    public static final String PARAM_SUBTITLE = CLASS_NAME + ".PARAM_SUBTITLE";
    public static final String PARAM_MESSAGE = CLASS_NAME + ".PARAM_MESSAGE";
    public static final String PARAM_BUTTON_1 = CLASS_NAME + ".PARAM_BUTTON_1";
    public static final String PARAM_BUTTON_2 = CLASS_NAME + ".PARAM_BUTTON_2";
    public static final String PARAM_IMAGE = CLASS_NAME + ".PARAM_IMAGE";

    private static final String TAG = LongMessageDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_long_message);
        View v = dialog.getWindow().getDecorView();

        Bundle arguments = getArguments();
        if (arguments != null) {
            String title = arguments.getString(PARAM_TITLE);
            String subtitle = arguments.getString(PARAM_SUBTITLE);
            String message = arguments.getString(PARAM_MESSAGE);
            String button1Text = arguments.getString(PARAM_BUTTON_1);
            String button2Text = arguments.getString(PARAM_BUTTON_2);
            Bitmap image = arguments.getParcelable(PARAM_IMAGE);

            if (title != null) {
                if (title.length() < 24) {
                    TextView titleView = (TextView) v.findViewById(R.id.title);
                    titleView.setVisibility(View.VISIBLE);
                    titleView.setText(title);
                } else {
                    TextView longTitleView = (TextView) v.findViewById(R.id.long_title);
                    longTitleView.setVisibility(View.VISIBLE);
                    longTitleView.setText(title);
                }
            }
            if (message != null) {
                message = message.replace("&lquot;", "\"");
                message = message.replace("&rquot;", "\"");
                ((TextView) v.findViewById(R.id.message)).setText(message);
            }

            ImageView imageView = (ImageView) v.findViewById(R.id.image);
            TextView subtitleView = (TextView) v.findViewById(R.id.subtitle);
            Button button1 = (Button) v.findViewById(R.id.button1);
            Button button2 = (Button) v.findViewById(R.id.button2);
            button1.setOnClickListener(mOnClickListener);
            button2.setOnClickListener(mOnClickListener);

            if (button1Text != null) {
                button1.setText(button1Text);
                button1.setVisibility(View.VISIBLE);
            }
            if (button2Text != null) {
                button2.setText(button2Text);
                button2.setVisibility(View.VISIBLE);
            }
            if (subtitle != null) {
                subtitleView.setText(subtitle);
                subtitleView.setVisibility(View.VISIBLE);
            }
            if (image != null) {
                imageView.setImageBitmap(image);
                imageView.setVisibility(View.VISIBLE);
            }
        }
        return dialog;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
