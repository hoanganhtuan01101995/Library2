package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.widget.FontTextView;
import com.alticast.viettelottcommons.widget.wheel.blur.picker.PickerUI;
import com.alticast.viettelottcommons.widget.wheel.blur.picker.PickerUISettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DateWheelDialog extends DialogFragment {
    public static final String CLASS_NAME = DateWheelDialog.class.getName();

    public static final String PARAM_TITLE = CLASS_NAME + ".PARAM_TITLE";
    private List<String> options;

    public int width;
    public int gravity;

    public void setWidth(int width) {
        this.width = width;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void setPickerClickListener(onPickerClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    private onPickerClickListener mOnClickListener;

    public interface onPickerClickListener {
        void onPickerClickListener(int number);
    }

    private OnDismissListener mOnDismissListener;

    private int currentPosition = -1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.date_wheel_dialog);
        dialog.setCancelable(true);

        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
//        attributes.width = getResources().getDimensionPixelSize(R.dimen.channel_list_width);
        if (WindmillConfiguration.scrWidth < getResources().getDimensionPixelSize(R.dimen.date_wheel_dialog_width)) {
            attributes.width = WindmillConfiguration.scrWidth;
        } else {
            attributes.width = width > 0 ? width : getResources().getDimensionPixelSize(R.dimen.date_wheel_dialog_width);
        }
        attributes.gravity = gravity > 0 ? gravity : Gravity.RIGHT;


        View v = dialog.getWindow().getDecorView();
        currentPosition = 1;
        Bundle arguments = getArguments();
        if (arguments != null) {
            options = arguments.getStringArrayList(PARAM_TITLE);
        }

        final PickerUI mPickerUI = (PickerUI) v.findViewById(R.id.picker);
        mPickerUI.setItems(getContext(), options);
        mPickerUI.setColorTextCenter(0xFFFFFFFF);
        mPickerUI.setColorTextNoCenter(0x66FFFFFF);
        mPickerUI.setBackgroundColorPanel(0xFF293241);
        mPickerUI.setLinesColor(0xFF00f3ed);
        mPickerUI.setItemsClickables(true);
        mPickerUI.setAutoDismiss(false);
        mPickerUI.setOnClickItemPickerUIListener(
                new PickerUI.PickerUIItemClickListener() {
                    @Override
                    public void onItemClickPickerUI(int which, int position, String valueResult) {
                        if (position == currentPosition) {
                            mOnClickListener.onPickerClickListener(position);
                            dismiss();
                        } else {
                            currentPosition = position;
                        }
                    }
                });

        currentPosition = getCurrentPos();
        mPickerUI.slide(currentPosition);
        return dialog;
    }

    private int getCurrentPos() {
        int pos = -1;
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).contains(getContext().getResources().getString(R.string.today))) {
                return i;
            }
        }
        return pos;
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
