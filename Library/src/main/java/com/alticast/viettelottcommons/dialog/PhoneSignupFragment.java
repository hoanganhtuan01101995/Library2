package com.alticast.viettelottcommons.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.App;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.loader.FrontEndLoader;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.AuthCodeRes;
import com.alticast.viettelottcommons.resource.ResultRes;

import retrofit2.Call;

public class PhoneSignupFragment extends DialogFragment implements OnClickListener {
    public static final String CLASS_NAME = PhoneSignupFragment.class.getName();
    private static final String TAG = PhoneSignupFragment.class.getSimpleName();

    private OnDismissListener mOnDismissListener;

    private TextView mEmailCheckMessageView;
    private TextView mPasswordCheckMessageView;
    private EditText mIdInput;
    private EditText mSmsInput;
    private EditText mPasswordInput;
    private EditText mPasswordConfirmInput;
    private Button mVerifyButton;
    private Button mCreateButton;

    private String mCheckedEmail;
    private TextView authInvalideCnt = null;
    private TextView auchCnt = null;
    private LinearLayout authCntLayout = null;
    private ProgressDialogFragment mProgressDialogFragment;
    private CntHandler cntHandler = null;

    public static PhoneSignupFragment newInstance(Context context) {
        PhoneSignupFragment fragment = new PhoneSignupFragment();
        return fragment;
    }

    public PhoneSignupFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup_phone_num, container, false);
        v.findViewById(R.id.close_button).setOnClickListener(this);

        mEmailCheckMessageView = (TextView) v.findViewById(R.id.email_check_message);
        mPasswordCheckMessageView = (TextView) v.findViewById(R.id.password_check_message);

        mIdInput = (EditText) v.findViewById(R.id.id_input);
        mSmsInput = (EditText) v.findViewById(R.id.sms_input);

        mPasswordInput = (EditText) v.findViewById(R.id.password_input);
        mPasswordConfirmInput = (EditText) v.findViewById(R.id.password_confirm_input);
        mVerifyButton = (Button) v.findViewById(R.id.verify_button);

        mCreateButton = (Button) v.findViewById(R.id.create_button);

        authInvalideCnt = (TextView) v.findViewById(R.id.authInvalideCnt);
        auchCnt = (TextView) v.findViewById(R.id.auchCnt);
        authCntLayout = (LinearLayout) v.findViewById(R.id.authCntLayout);

        authCntLayout.setVisibility(View.INVISIBLE);

        mPasswordInput.setTypeface(Typeface.SANS_SERIF);
        mPasswordConfirmInput.setTypeface(Typeface.SANS_SERIF);

        mVerifyButton.setOnClickListener(this);
        mCreateButton.setOnClickListener(this);
        mIdInput.addTextChangedListener(new SignUpTextWatcher(mIdInput));
        mSmsInput.addTextChangedListener(new SignUpTextWatcher(mSmsInput));
        mSmsInput.setEnabled(false);

        mPasswordInput.setEnabled(false);
        mPasswordConfirmInput.setEnabled(false);

        mPasswordInput.addTextChangedListener(new SignUpTextWatcher(mPasswordInput));
        mPasswordConfirmInput.addTextChangedListener(new SignUpTextWatcher(mPasswordConfirmInput));

        v.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    hideKeyboard();
            }
        });
        v.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    showExitDialog();
                    return true;
                }
                return false;
            }
        });

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cntHandler == null) {
            return;
        }
        cntHandler.sendEmptyMessage(CntHandler.STOP);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null)
            mOnDismissListener.onDismiss(dialog);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.close_button) {
            showExitDialog();

        } else if (i == R.id.verify_button) {
            verifyPhoneNumber();

        } else if (i == R.id.create_button) {
            createAccount();

        }
    }

    public void showExitDialog() {
        String title = getString(R.string.pop_quit_create_account_title);
        String message = getString(R.string.pop_quit_create_account_desc);
        showMessageDialog(title, message, new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        });
    }

    private void verifyPhoneNumber() {
        mEmailCheckMessageView.setText("");
        String email = mIdInput.getText().toString();
        showProgress();


        FrontEndLoader.getInstance().checkID(email, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {

                mVerifyButton.setEnabled(false);
                ResultRes result = (ResultRes) obj;
                if (result.isResult()) {
                    mIdInput.setText("");
                    showMessageDialog(getString(R.string.create_account_already_title), getString(R.string.create_account_already_sub) + "\n"
                            + getString(R.string.create_account_already_des));
                } else {
                    mCheckedEmail = mIdInput.getText().toString();
                    requestAuthCode();
                }

                hideProgress();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                hideProgress();
            }

            @Override
            public void onError(ApiError error) {
                hideProgress();
                mIdInput.setText("");
                showMessageDialog(getString(R.string.notice), error.getError().getMessage());
            }
        });

    }

    private void createAccount() {
        final String email = mIdInput.getText().toString();
        String password = mPasswordInput.getText().toString();
        String passwordConfirm = mPasswordConfirmInput.getText().toString();
        if (!password.equals(passwordConfirm)) {
            mPasswordConfirmInput.getText().clear();
            mPasswordInput.getText().clear();
            mPasswordCheckMessageView.setText(R.string.myaccount_wrongpassword);
            // showKeyboard();
            return;
        }
        //###########################################################
        //cntHandler.sendEmptyMessage(CntHandler.STOP);
        //###########################################################
        String otp = mSmsInput.getText().toString().trim();

        showProgress();

//        FrontEndLoader.getInstance().checkID();

        FrontEndLoader.getInstance().createAccount(email, password, otp, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                hideProgress();
                App.getToast(getActivity(), getString(R.string.create_account_title), getString(R.string.create_account_noti, email), false).show();
                cntHandler.sendEmptyMessage(CntHandler.STOP);
                dismiss();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                hideProgress();
            }

            @Override
            public void onError(ApiError error) {
                hideProgress();

                mSmsInput.setText("");
                final MessageDialog dialog = new MessageDialog();
                Bundle args = new Bundle();
                args.putString(MessageDialog.PARAM_TITLE, getString(R.string.findPassword_invalid_sms_title));
                args.putString(MessageDialog.PARAM_COLOR_MESSAGE, getString(R.string.findPassword_invalid_sms_msg));
                args.putString(MessageDialog.PARAM_BUTTON_1, getString(R.string.ok));
                dialog.setArguments(args);
                dialog.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show(getChildFragmentManager(), MessageDialog.CLASS_NAME);


            }
        });


    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    private void requestAuthCode() {

        FrontEndLoader.getInstance().requestAuthenticationCode(mCheckedEmail, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                hideProgress();
                AuthCodeRes codeRes = (AuthCodeRes) obj;
                if (cntHandler != null) {
                    cntHandler.sendEmptyMessage(CntHandler.STOP);
                    cntHandler = null;
                }
                mVerifyButton.setEnabled(false);
                cntHandler = new CntHandler(codeRes.getTimeout());
                cntHandler.sendEmptyMessage(CntHandler.RUN);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                hideProgress();
            }

            @Override
            public void onError(ApiError error) {
                hideProgress();
                mIdInput.setText("");
                showMessageDialog(getString(R.string.notice), error.getError().getMessage());
            }
        });

    }


    private void showMessageDialog(String title, String message) {
        final MessageDialog dialog = new MessageDialog();
        Bundle args = new Bundle();
        args.putString(MessageDialog.PARAM_TITLE, title);
        args.putString(MessageDialog.PARAM_MESSAGE, message);
        args.putString(MessageDialog.PARAM_BUTTON_1, getString(R.string.ok));
        dialog.setArguments(args);
        dialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show(getChildFragmentManager(), MessageDialog.CLASS_NAME);
    }


    private class CntHandler extends Handler {
        private boolean isStop = false;
        private final String TAG = CntHandler.class.getSimpleName();

        private int cnt = 0;
        public static final int RUN = 0;
        public static final int STOP = 1;
        public static final int OVER = 2;
        private final int timeOffset = 1000;

        public CntHandler(int cnt) {
            this.cnt = cnt / 1000;
        }

        public boolean isStop() {
            return isStop;
        }

        private String changeTime(int time) {
            int min = time / 60;
            int tmpS = time % 60;
            String sec = null;
            if (tmpS < 10) {
                sec = "0" + tmpS;
            } else {
                sec = String.valueOf(tmpS);
            }
            if (min == 0) {
                return sec + " " + getString(R.string.sec);
            } else {
                return min + " " + getString(R.string.min) + " " + sec + " " + getString(R.string.sec);
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RUN:
                    if (isStop) {
                        return;
                    }
                    if (cnt > 0) {
                        authCntLayout.setVisibility(View.VISIBLE);
                        authInvalideCnt.setText(getString(R.string.create_account_auth_cnt_title));
                        auchCnt.setText(changeTime(cnt--));
                        mSmsInput.setEnabled(true);
                        sendEmptyMessageDelayed(RUN, timeOffset);
                    } else {
                        mVerifyButton.setText(getString(R.string.create_account_id_re_button_phone));
                        //#########################################
                        mVerifyButton.setEnabled(true);
                        //#########################################
                        authCntLayout.setVisibility(View.VISIBLE);
                        mSmsInput.setEnabled(false);
                        authInvalideCnt.setText(getString(R.string.create_account_auth_cnt_over));
                        auchCnt.setText("");
                        sendEmptyMessage(OVER);
                    }
                    break;
                case STOP:
                    isStop = true;
                    authCntLayout.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    private class SignUpTextWatcher implements TextWatcher {

        private View targetView = null;

        public SignUpTextWatcher(View targetView) {
            super();
            this.targetView = targetView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int i = targetView.getId();
            if (i == R.id.id_input) {
                if (cntHandler != null && !cntHandler.isStop) {
                    mVerifyButton.setText(getString(R.string.create_account_id_re_button_phone));
                }
                mVerifyButton.setEnabled(mIdInput.length() > 0 && !mIdInput.getText().toString().equals(mCheckedEmail));

            } else if (i == R.id.sms_input || i == R.id.password_input || i == R.id.password_confirm_input) {
            } else {
            }
            mPasswordInput.setEnabled(mSmsInput.length() * mIdInput.length() > 0);
            mPasswordConfirmInput.setEnabled(mPasswordInput.length() > 0);
            mPasswordCheckMessageView.setText("");
            mCreateButton.setEnabled(mIdInput.length() * mSmsInput.length() *
                    mPasswordInput.length() * mPasswordConfirmInput.length() != 0);

        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    }


    private void showMessageDialog(String title, String message, final Runnable onOk) {
        final MessageDialog dialog = new MessageDialog();
        Bundle args = new Bundle();
        args.putString(MessageDialog.PARAM_TITLE, title);
        args.putString(MessageDialog.PARAM_MESSAGE, message);
        args.putString(MessageDialog.PARAM_BUTTON_1, getString(R.string.yes));
        args.putString(MessageDialog.PARAM_BUTTON_2, getString(R.string.no));
        dialog.setArguments(args);
        dialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button1) {
                    onOk.run();
                }
                dialog.dismiss();
            }
        });
        dialog.show(getChildFragmentManager(), MessageDialog.CLASS_NAME);
    }

    @Override
    public void dismiss() {
        hideKeyboard();
        super.dismiss();
    }

    public void showKeyboard() {
        try {
            mPasswordInput.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPasswordInput.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mPasswordInput, 0);
                }
            }, 100);
        } catch (Exception ignored) {
        }
    }

    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mIdInput.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(mPasswordInput.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(mPasswordConfirmInput.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ignored) {
        }
    }

    protected void showProgress() {
        if (mProgressDialogFragment != null && mProgressDialogFragment.isShowing()) {
            mProgressDialogFragment.dismiss();
        } else {
            mProgressDialogFragment = new ProgressDialogFragment();
        }


        mProgressDialogFragment.show(getFragmentManager(), "");
    }

    protected void hideProgress() {
        if (mProgressDialogFragment != null && mProgressDialogFragment.isShowing()) {
            mProgressDialogFragment.dismiss();
        }
    }
}