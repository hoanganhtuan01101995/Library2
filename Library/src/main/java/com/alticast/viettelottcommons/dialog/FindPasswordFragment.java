package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alticast.viettelottcommons.GlobalKey;
import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.loader.FrontEndLoader;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.AuthCodeRes;
import com.alticast.viettelottcommons.resource.ResultRes;

import retrofit2.Call;

public class FindPasswordFragment extends DialogFragment implements View.OnClickListener {
    public static final String CLASS_NAME = FindPasswordFragment.class.getName();
    public static final String TAG = FindPasswordFragment.class.getSimpleName();
    private ProgressDialogFragment pDialog;
    private CntHandler cntHandler = null;

    private EditText mPhoneNumberEdt;
    private EditText mVerifyCodeEdt;
    private EditText mNewPwdEdt;
    private EditText mReNewPwdEdt;
    private Button mSendBtn;
    private Button mConfirmBtn;
    private Button mCloseBtn;
    private TextView mAuthCountTv;
    private TextView mEnterPasswordTv;
    private TextView titleStep1, titleStep2, titleStep3, password_check_message;

    public FindPasswordFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        mCloseBtn = (Button) rootView.findViewById(R.id.btn_forgot_pwd_x);
        mSendBtn = (Button) rootView.findViewById(R.id.btn_forgot_pwd_send);
        mConfirmBtn = (Button) rootView.findViewById(R.id.btn_forgot_pwd_confirm);

        mPhoneNumberEdt = (EditText) rootView.findViewById(R.id.edt_forgot_pwd_phone_number);
        mVerifyCodeEdt = (EditText) rootView.findViewById(R.id.edt_forgot_pwd_received_code);
        mNewPwdEdt = (EditText) rootView.findViewById(R.id.edt_forgot_pwd_enter_pwd);
        mReNewPwdEdt = (EditText) rootView.findViewById(R.id.edt_forgot_pwd_confirm_pwd);
        titleStep1 = (TextView) rootView.findViewById(R.id.titleStep1);
        titleStep2 = (TextView) rootView.findViewById(R.id.titleStep2);
        titleStep3 = (TextView) rootView.findViewById(R.id.titleStep3);
        password_check_message = (TextView) rootView.findViewById(R.id.password_check_message);

        String title1 = getString(R.string.forgot_pwd_step1);
        String message1 = getString(R.string.forgot_pwd_step1_msg);
        String fullTitle1 = "<b>" + title1 + " </b>" + message1;
        titleStep1.setText(Html.fromHtml(fullTitle1));

        String title2 = getString(R.string.forgot_pwd_step2);
        String message2 = getString(R.string.forgot_pwd_step2_msg);
        String fullTitle2 = "<b>" + title2 + " </b>" + message2;
        titleStep2.setText(Html.fromHtml(fullTitle2));

        String title3 = getString(R.string.forgot_pwd_step3);
        String message3 = getString(R.string.forgot_pwd_step3_msg);
        String fullTitle3 = "<b>" + title3 + " </b>" + message3;
        titleStep3.setText(Html.fromHtml(fullTitle3));

//        tt.setText(Html.fromHtml("<b>"+o.content+"</b>"));
//
//        SpannableString spannableString1 = new SpannableString(getString(R.string.forgot_pwd_step1_msg));
//        spannableString1.setSpan(new StyleSpan(Rs.getFont(getString(R.string.font_B))), 0, title1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString1.setSpan(Rs.getFont(getString(R.string.font_M)), title1.length() - 1, spannableString1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        titleStep1.setText(spannableString1);
//
//        SpannableString spannableString2 = new SpannableString(getString(R.string.forgot_pwd_step2_msg));
//        String title2 = getString(R.string.forgot_pwd_step2);
//        spannableString2.setSpan(Rs.getFont(getString(R.string.font_B)), 0, title2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString2.setSpan(Rs.getFont(getString(R.string.font_M)), title2.length() - 1, spannableString2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        titleStep2.setText(spannableString2);
//
//        SpannableString spannableString3 = new SpannableString(getString(R.string.forgot_pwd_step3_msg));
//        String title3 = getString(R.string.forgot_pwd_step3);
//        spannableString3.setSpan(Rs.getFont(getString(R.string.font_B)), 0, title3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString3.setSpan(Rs.getFont(getString(R.string.font_M)), title3.length() - 1, spannableString3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        titleStep3.setText(spannableString3);

        mNewPwdEdt.setEnabled(false);
        mReNewPwdEdt.setEnabled(false);

        mAuthCountTv = (TextView) rootView.findViewById(R.id.tv_forgot_pwd_authCount);
        //    mEnterPasswordTv = (TextView) rootView.findViewById(R.id.tv_forgot_pwd_enter_pwd);

        mCloseBtn.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);

        mPhoneNumberEdt.addTextChangedListener(new FindPasswordTextWatcher(mPhoneNumberEdt));
        mVerifyCodeEdt.addTextChangedListener(new FindPasswordTextWatcher(mVerifyCodeEdt));
        mNewPwdEdt.addTextChangedListener(new FindPasswordTextWatcher(mNewPwdEdt));
        mReNewPwdEdt.addTextChangedListener(new FindPasswordTextWatcher(mReNewPwdEdt));


        if (HandheldAuthorization.getInstance().isLogIn()) {
            mPhoneNumberEdt.setText(HandheldAuthorization.getInstance().getCurrentId());
        }
        setupUI(rootView);
        //##############################################
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cntHandler == null) {
            return;
        }
        cntHandler.sendEmptyMessage(CntHandler.STOP);
    }

    private class FindPasswordTextWatcher implements TextWatcher {

        private EditText targetEdit = null;

        public FindPasswordTextWatcher(EditText targetEdit) {
            this.targetEdit = targetEdit;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //HuyLV [06/28/2016]############################
            targetEdit.requestFocus();
            targetEdit.setSelection(start);
            //##############################################
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int i = targetEdit.getId();
            if (i == R.id.edt_forgot_pwd_phone_number) {
                mSendBtn.setEnabled(mPhoneNumberEdt.length() > 0);
                if (cntHandler != null && !cntHandler.isStop) {
                    mSendBtn.setText(getString(R.string.forgot_pwd_resend));
                } else {
                    mSendBtn.setText(getString(R.string.forgot_pwd_send));
                }

            } else if (i == R.id.edt_forgot_pwd_received_code) {
                //##############################################
            } else {
            }
            mNewPwdEdt.setEnabled(mPhoneNumberEdt.length() * mVerifyCodeEdt.length() > 0);
            mReNewPwdEdt.setEnabled(mNewPwdEdt.length() > 0);
        }

        @Override
        public void afterTextChanged(Editable s) {
            //HuyLV [06/28/2016]############################
            if (TextUtils.isEmpty(mPhoneNumberEdt.getText().toString().toLowerCase()) || TextUtils.isEmpty(mVerifyCodeEdt.getText().toString().trim())
                    || TextUtils.isEmpty(mNewPwdEdt.getText().toString().trim()) || TextUtils.isEmpty(mReNewPwdEdt.getText().toString().trim())) {
                mConfirmBtn.setEnabled(false);
            } else {
                mConfirmBtn.setEnabled(true);
            }
            //##############################################
        }
    }


    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.btn_forgot_pwd_send) {
            verifyEmail();

        } else if (i == R.id.btn_forgot_pwd_confirm) {
            String newPass = mNewPwdEdt.getText().toString().trim();
            String reNewPass = mReNewPwdEdt.getText().toString().trim();
            if (!TextUtils.isEmpty(newPass) && !TextUtils.isEmpty(reNewPass)) {
                if (newPass.equals(reNewPass)) {
                    resetPassword();
                } else {
                    password_check_message.setText(getString(R.string.forgot_pwd_wrong_pwd));
                    mNewPwdEdt.setText("");
                    mReNewPwdEdt.setText("");
//                        mReNewPwdEdt.setHintTextColor(Color.parseColor("#FFFF4444"));
//                        mReNewPwdEdt.setHint(getString(R.string.forgot_pwd_wrong_pwd));
                    showKeyboard(mReNewPwdEdt);
                }
            }

        } else if (i == R.id.btn_forgot_pwd_x) {
            dismiss();

            //##############################################
        } else {
        }
    }

    private void resetPassword() {
        //HuyLV [06/28/2016]############################
        String email = mPhoneNumberEdt.getText().toString().trim();
        String code = mVerifyCodeEdt.getText().toString().trim();
        String password = mReNewPwdEdt.getText().toString().trim();
        //##############################################

        showProgress();
        FrontEndLoader.getInstance().resetPassword(email, password, code, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                hideProgress();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mVerifyCodeEdt.getWindowToken(), 0);
                //##############################################
                if (cntHandler != null) {
                    cntHandler.sendEmptyMessage(CntHandler.STOP);
                }
                showConfirmDialog();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                hideProgress();
            }

            @Override
            public void onError(ApiError error) {
                hideProgress();
                showMessageDialog(getString(R.string.notice), error.getError().getMessage());
            }
        });


    }

    private void verifyEmail() {
        //HuyLV [06/28/2016]############################
        String email = mPhoneNumberEdt.getText().toString().trim();
        //##############################################
        showProgress();
        FrontEndLoader.getInstance().checkID(email, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {

                ResultRes resultRes = (ResultRes) obj;
                if (resultRes.isResult()) {
                    mSendBtn.setEnabled(false);
                    mVerifyCodeEdt.setEnabled(true);
//                    mNewPwdEdt.setEnabled(true);
//                    mReNewPwdEdt.setEnabled(true);
                    requestAuthCode(mPhoneNumberEdt.getText().toString().trim());
                } else {
                    hideProgress();
                    showMessageDialog(getString(R.string.create_account_id_invalide_title), getString(R.string.create_account_id_invalide_sub) + "\n"
                            + getString(R.string.create_account_id_invalide_des));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                hideProgress();
            }

            @Override
            public void onError(ApiError error) {
                hideProgress();
                showMessageDialog(getString(R.string.notice), error.getError().getMessage());
            }
        });
    }

    private void showConfirmDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_forgot_pwd_popup);
        dialog.setCanceledOnTouchOutside(false);
        View view = dialog.getWindow().getDecorView();
        Button oK = (Button) view.findViewById(R.id.btn_forgot_pwd_ok);
        dialog.show();
        oK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (HandheldAuthorization.getInstance().isLogIn()) {
                    String id = mPhoneNumberEdt.getText().toString();
                    String pw = mNewPwdEdt.getText().toString();
//                    SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(PhoneLoginFragment.CLASS_NAME, Context.MODE_PRIVATE);

                    if (HandheldAuthorization.getInstance().getBoolean(HandheldAuthorization.IS_KEEP_SIGN_IN)) {
                        HandheldAuthorization.getInstance().putString(HandheldAuthorization.PW, pw);
                    }


                    FrontEndLoader.getInstance().requestLogin(id, pw, new WindmillCallback() {
                        @Override
                        public void onSuccess(Object obj) {

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                        }

                        @Override
                        public void onError(ApiError error) {
                            Intent intent = new Intent(GlobalKey.MainActivityKey.REFRESH_DATA);
                            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                        }
                    }, (GlobalActivity) getActivity());
                }
            }
        });
    }
    //##############################################

    private void requestAuthCode(String mCheckedEmail) {

        FrontEndLoader.getInstance().requestAuthenticationCode(mCheckedEmail, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                hideProgress();
                AuthCodeRes codeRes = (AuthCodeRes) obj;

                if (cntHandler != null) {
                    cntHandler.sendEmptyMessage(CntHandler.STOP);
                    cntHandler = null;
                }
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
        dialog.setOnClickListener(new View.OnClickListener() {
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
                //HuyLV [06/29/2016]############################
                return "(" + sec + " " + getString(R.string.sec) + ")";
            } else {
                return "(" + min + " " + getString(R.string.min) + " " + sec + " " + getString(R.string.sec) + ")";
            }
            //##############################################

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
                        mAuthCountTv.setText(changeTime(cnt--));
                        mVerifyCodeEdt.setEnabled(true);
                        //##############################################
                        sendEmptyMessageDelayed(RUN, timeOffset);
                    } else {
                        mVerifyCodeEdt.setEnabled(false);
                        mSendBtn.setEnabled(true);
                        mAuthCountTv.setText("");
                        //##############################################
                        sendEmptyMessage(OVER);
                    }
                    break;
                case STOP:
                    isStop = true;
                    mAuthCountTv.setText("");
                    break;
                default:
                    break;
            }
        }
    }

    protected void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(v);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInputFromInputMethod(editText.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
        editText.requestFocus();
    }


    protected void showProgress() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        } else {
            pDialog = new ProgressDialogFragment();
        }


        pDialog.show(getFragmentManager(), "");
    }

    protected void hideProgress() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}
