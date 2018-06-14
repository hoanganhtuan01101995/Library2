package com.alticast.viettelottcommons.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;

public class PurchaseConfirmDialog extends DialogFragment {
    public static final String CLASS_NAME = PurchaseConfirmDialog.class.getName();

    public static final String PARAM_TITLE = CLASS_NAME + ".PARAM_TITLE";
    public static final String PARAM_CASH_PRICE = CLASS_NAME + ".PARAM_CASH_PRICE";
    public static final String PARAM_POINT_PRICE = CLASS_NAME + ".PARAM_POINT_PRICE";

    private static final String TAG = PurchaseConfirmDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;
    private EditText mInputView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_purchase_confirm);
        View v = dialog.getWindow().getDecorView();

        Bundle arguments = getArguments();
        if (arguments != null) {
            String title = arguments.getString(PARAM_TITLE);
            int price = arguments.getInt(PARAM_CASH_PRICE, -1);
            if (price == -1) {
//                ((TextView) v.findViewById(R.id.currency)).setText(R.string.point);
                price = arguments.getInt(PARAM_POINT_PRICE, -1);
            }

            ((TextView) v.findViewById(R.id.title)).setText(title);
            ((TextView) v.findViewById(R.id.purchase_price)).setText(TextUtils.getNumberString(price));

            mInputView = ((EditText) v.findViewById(R.id.input));
            mInputView.setTypeface(Typeface.SANS_SERIF);
            int padding = getResources().getDimensionPixelSize(R.dimen.list_channel_margin_left);
            mInputView.setPadding(padding, 0, padding, 0);

            v.findViewById(R.id.enter_button).setOnClickListener(mOnClickListener);
            v.findViewById(R.id.cancel_button).setOnClickListener(mOnClickListener);
        }
        return dialog;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public void dismiss() {
        hideKeyboard();
        super.dismiss();
    }

    public String getInputString() {
        return mInputView.getText().toString();
    }


    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mInputView.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ignored) {
        }
    }
}
