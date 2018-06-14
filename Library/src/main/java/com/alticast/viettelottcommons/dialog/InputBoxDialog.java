package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.util.Logger;


public class InputBoxDialog extends DialogFragment {
    public static final String CLASS_NAME = InputBoxDialog.class.getName();

    public static final String PARAM_TITLE = CLASS_NAME + ".PARAM_TITLE";
    public static final String PARAM_MESSAGE = CLASS_NAME + ".PARAM_MESSAGE";
    public static final String PARAM_BUTTON_1 = CLASS_NAME + ".PARAM_BUTTON_1";
    public static final String PARAM_BUTTON_2 = CLASS_NAME + ".PARAM_BUTTON_2";
    public static final String PARAM_INPUT_TYPE = CLASS_NAME + ".PARAM_INPUT_TYPE";
    public static final String PARAM_INPUT_LENGTH = CLASS_NAME + ".PARAM_INPUT_LENGTH";
    public static final String PARAM_INPUT_TEXT = CLASS_NAME + ".PARAM_INPUT_TEXT";

    private static final String TAG = InputBoxDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;
    private EditText mInputView;
    private boolean mIsKeyBoardUp;
    private InputMethodManager mInputMethodManager;
    private FragmentActivity mActivity;
    private OnDismissListener mOnDismissListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.print(TAG, "onCreateDialog");
        mInputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_input_box);
//        dialog.setCanceledOnTouchOutside(false);
        View v = dialog.getWindow().getDecorView();

        Bundle arguments = getArguments();
        if (arguments != null) {
            String title = arguments.getString(PARAM_TITLE);
            String message = arguments.getString(PARAM_MESSAGE);
            String inputText = arguments.getString(PARAM_INPUT_TEXT);
            String button1Text = arguments.getString(PARAM_BUTTON_1);
            String button2Text = arguments.getString(PARAM_BUTTON_2);

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
                message = message.replace("\n", "<br />");
                ((TextView) v.findViewById(R.id.message)).setText(Html.fromHtml(message));
            }


//            ((TextView) v.findViewById(R.id.message)).setText(message);

            mInputView = ((EditText) v.findViewById(R.id.input));
            mInputView.setInputType(arguments.getInt(PARAM_INPUT_TYPE, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_TEXT_VARIATION_PASSWORD));
            int inputLength = arguments.getInt(PARAM_INPUT_LENGTH, -1);
            if (inputLength >= 0) {
//                mInputView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                InputFilter[] filterArray = new InputFilter[1];
                filterArray[0] = new InputFilter.LengthFilter(6);
                mInputView.setFilters(filterArray);
            }

            final Button button1 = (Button) v.findViewById(R.id.button1);
            Button button2 = (Button) v.findViewById(R.id.button2);
            TextView forgotPwBtn = (TextView) v.findViewById(R.id.tv_login_forgot_pw);
            //#######################################################
            //TODO [확인] 비밀번호 찾기
            //forgotPwBtn.setVisibility(View.GONE);
            if (mInputView.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                forgotPwBtn.setVisibility(View.VISIBLE);
                forgotPwBtn.setOnClickListener(mOnClickListener);
            } else {
                forgotPwBtn.setVisibility(View.GONE);
            }
            //#######################################################
            button1.setOnClickListener(mOnClickListener);
            button2.setOnClickListener(mOnClickListener);
            button1.setText(button1Text);
            button2.setText(button2Text);

            button1.setEnabled(mInputView.length() > 0);
            mInputView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    button1.setEnabled(mInputView.length() > 0);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            if (inputText != null) {
                mInputView.setText(inputText);
            }
        }

        Logger.print(TAG, "onCreateDialog " + mInputView.getWindowToken());

        mInputView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) mInputView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mInputView, 0);
                    mIsKeyBoardUp = true;
                } else {
                    boolean hide = hide();
                    if (hide) mIsKeyBoardUp = false;
                }
            }
        });

        return dialog;
    }


    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }


    @Override
    public void dismiss() {
        Logger.print(TAG, "dismiss");
        boolean hide = hide();
        if (hide) mIsKeyBoardUp = false;
        super.dismiss();
    }


    @Override
    public void onPause() {
        Logger.print(TAG, "onPause");

        boolean hide = hide();
        if (hide) mIsKeyBoardUp = false;
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        Logger.print(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Logger.print(TAG, "onDismiss");
        boolean hide = hide();
        if (hide) mIsKeyBoardUp = false;
        super.onDismiss(dialog);
        if (mOnDismissListener != null) mOnDismissListener.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.print(TAG, "onDismiss mIsKeyBoardUp:" + mIsKeyBoardUp);

    }

    public void setOnDismissListener(OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    public String getInputString() {
        return mInputView.getText().toString();
    }

    public void setEmphasisText(String text) {
        View v = getDialog().getWindow().getDecorView();
        TextView emphasisText = (TextView) v.findViewById(R.id.message_emphasis);
        emphasisText.setText(text);
        emphasisText.setVisibility(View.VISIBLE);
        mInputView.setText("");
    }

    public EditText getInputView() {
        try {
            return (EditText) getDialog().getWindow().getDecorView().findViewById(R.id.input);
        } catch (NullPointerException e) {
            return null;
        }
    }

    private boolean hide() {
        boolean isSucceed = false;
        if (mInputMethodManager.isActive()) {
            IBinder windowToken = mInputView.getWindowToken();
            if (windowToken == null) {
                windowToken = mActivity.getWindow().getDecorView().getWindowToken();
            }
            if (windowToken != null)
                isSucceed = mInputMethodManager.hideSoftInputFromWindow(windowToken, 0);
        } else if (mInputMethodManager.isAcceptingText()) {
            mInputMethodManager.toggleSoftInput(0, 0);
            isSucceed = true;
        }
        return isSucceed;
    }


    public void hideKeyboard() {
    }

    public interface PasswordListener {
        public void isSuccess(boolean state);
    }
}
