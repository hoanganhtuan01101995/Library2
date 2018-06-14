package com.alticast.viettelottcommons.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;


/**
 * Created by mc.kim on 2015-11-10.
 */
public class MessageScrolledDialog extends DialogFragment {
    public static final String CLASS_NAME = MessageScrolledDialog.class.getName();
    public static final String PARAM_MESSAGE = CLASS_NAME + ".PARAM_MESSAGE";

    private View.OnClickListener mOnClickListener = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_scroll_message);
        View v = dialog.getWindow().getDecorView();
        Bundle arguments = getArguments();
        if (arguments != null) {
            String message = arguments.getString(PARAM_MESSAGE);
            TextView messageText = (TextView)v.findViewById(R.id.message);
            messageText.setText(message);
            v.findViewById(R.id.button1).setOnClickListener(mOnClickListener);
            v.findViewById(R.id.button2).setOnClickListener(mOnClickListener);

            messageText.setMovementMethod(new ScrollingMovementMethod());
        }
        return dialog;
    }
    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

}
