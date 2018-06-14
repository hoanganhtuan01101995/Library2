package com.alticast.viettelottcommons.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.alticast.viettelottcommons.GlobalKey;
import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.activity.App;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.loader.CampaignLoader;
import com.alticast.viettelottcommons.loader.ConfigLoader;
import com.alticast.viettelottcommons.loader.FrontEndLoader;
import com.alticast.viettelottcommons.loader.ProgramLoader;
import com.alticast.viettelottcommons.manager.ChannelManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.manager.MyContentManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.Login;
import com.alticast.viettelottcommons.resource.MyDeviceAccount;
import com.alticast.viettelottcommons.resource.RegistedDevice;
import com.alticast.viettelottcommons.util.NetworkUtil;
import com.alticast.viettelottcommons.widget.FontTextView;

import java.util.ArrayList;

import retrofit2.Call;


public class TabletLoginFragmentPhase2 extends DialogFragment implements OnClickListener {
    //    public static final String IS_AUTO = "isAutoLogin";
//    public static final String ID = "id";
//    public static final String PW = "pw";
    private static final String TAG = TabletLoginFragmentPhase2.class.getSimpleName();
    public static final String CLASS_NAME = TabletLoginFragmentPhase2.class.getName();
    public static final String STATUS_READY = "ready";
    public static final String STATUS_INUSE = "inuse";
    public static final float ALPHA_DIM = 0.3f;
    private ImageButton ib_login_close;
    private ImageButton bt_login, bt_sign_up;
    private FontTextView tv_login_forgot_pw;

    private OnDismissListener mOnDismissListener;
    private EditText mIdInput;
    private EditText mPasswordInput;
    private CheckBox mAutoLoginCheck;
    //  private CheckBox mLiveIpCheck;
    private Integer mFutureAction;
    //    private SharedPreferences mSharedPreferences;
//    private View mPasswordErrorMessage;


    protected ProgressDialogFragment pDialog = null;

    public TabletLoginFragmentPhase2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_tablet, container, false);
        ib_login_close = (ImageButton) v.findViewById(R.id.ib_login_close);
        bt_login = (ImageButton) v.findViewById(R.id.bt_login);
        bt_login.setEnabled(false);
        bt_login.setAlpha(ALPHA_DIM);
        bt_sign_up = (ImageButton) v.findViewById(R.id.bt_sign_up);
        tv_login_forgot_pw = (FontTextView) v.findViewById(R.id.tv_login_forgot_pw);
        RelativeLayout forgotLayout = (RelativeLayout) v.findViewById(R.id.layout_forgot_password);

//        mPasswordErrorMessage = v.findViewById(R.id.password_error_message);
        mIdInput = (EditText) v.findViewById(R.id.id_input);
        mPasswordInput = (EditText) v.findViewById(R.id.password_input);
        mAutoLoginCheck = (CheckBox) v.findViewById(R.id.auto_login_check);
        //    mLiveIpCheck = (CheckBox) v.findViewById(R.id.live_ip_check);s
        mPasswordInput.setTypeface(Typeface.SANS_SERIF);
        mIdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFillBothAccountAndPassWord();
            }
        });
        mPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFillBothAccountAndPassWord();
            }
        });

//        mSharedPreferences = getActivity().getSharedPreferences(CLASS_NAME, Context.MODE_PRIVATE);
        //    mLiveIpCheck.setChecked(mSharedPreferences.getBoolean(IS_LIVE, true));

        ib_login_close.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        bt_sign_up.setOnClickListener(this);
        tv_login_forgot_pw.setOnClickListener(this);
        forgotLayout.setOnClickListener(this);
        //    mLiveIpCheck.setOnClickListener(this);

        // MainApp.selectIp(getActivity());
        v.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) hideKeyboard();
            }
        });


        mIdInput.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard();
                }
            }
        });
        mPasswordInput.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard();
                }
            }
        });
        return v;
    }

    private void checkFillBothAccountAndPassWord() {
        if (bt_login == null) return;
        if (mIdInput == null || mPasswordInput == null || (mIdInput.getText() == null && mPasswordInput.getText() == null) ||
                mIdInput.getText().toString().equalsIgnoreCase("") || mPasswordInput.getText().toString().equalsIgnoreCase("")) {
            bt_login.setEnabled(false);
            bt_login.setAlpha(ALPHA_DIM);
        } else {
            bt_login.setEnabled(true);
            bt_login.setAlpha(1f);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        boolean isAuto = mSharedPreferences.getBoolean(IS_AUTO, false);
//
//        if (isAuto) {
//            String id = mSharedPreferences.getString(ID, "");
//            String pw = mSharedPreferences.getString(PW, "");
//            mIdInput.setText(id);
//            showProgress();
//            FrontEndLoader.getInstance().requestLogin(id, pw, new WindmillCallback() {
//                @Override
//                public void onSuccess(Object obj) {
//                    hideProgress();
//                    Login login = FrontEndLoader.getInstance().getLoginRes();
//                    showLoginMessage(login);
//                }
//
//                @Override
//                public void onFailure(Call call, Throwable t) {
//                    hideProgress();
//                    mPasswordInput.setText("");
//                    mIdInput.setText("");
//                    showKeyboard();
//                    mPasswordErrorMessage.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onError(ApiError error) {
//                    hideProgress();
//                    ApiError.Error errorResult = error.getError();
//                    if (errorResult.getStatus() == 401 && errorResult.getCode().equals("F0102")) {
//                        mPasswordInput.setText("");
//                        mIdInput.setText("");
//                        showKeyboard();
//                        mPasswordErrorMessage.setVisibility(View.VISIBLE);
//                    } else {
//                        MainApp.showAlertDialog(getActivity(), getChildFragmentManager(), error);
//                    }
//                }
//            });
//
//        }
        //Auto Login
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onClick(View v) {
//        mPasswordErrorMessage.setVisibility(View.INVISIBLE);
        if (v.getId() == R.id.ib_login_close) {
            dismiss();
        } else if (v.getId() == R.id.bt_login) {
            attemptLogin();
        } else if (v.getId() == R.id.bt_sign_up) {
            showSignupDialog();
        } else if (v.getId() == R.id.tv_login_forgot_pw) {
            showFindPasswordDialog();
        }
    }

    private void showFindPasswordDialog() {
        FindPasswordFragmentTablet dialog = new FindPasswordFragmentTablet();
        dialog.show(getFragmentManager(), FindPasswordFragmentTablet.CLASS_NAME);
    }

    private void showSignupDialog() {
        String agree = ConfigLoader.getInstance().getAgree();
        final LongMessageDialog dialog = new LongMessageDialog();
        Bundle args = new Bundle();
        args.putString(LongMessageDialog.PARAM_TITLE, getString(R.string.agreement_policy));
        args.putString(LongMessageDialog.PARAM_SUBTITLE, getString(R.string.agreement_policy_desc));
        if (agree != null) {
            args.putString(LongMessageDialog.PARAM_MESSAGE, agree);
        }
        args.putString(LongMessageDialog.PARAM_BUTTON_1, getString(R.string.agree));
        args.putString(LongMessageDialog.PARAM_BUTTON_2, getString(R.string.cancel));
        dialog.setArguments(args);
        dialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button1) {
                    TabletSignupFragmentPhase2 fragment = TabletSignupFragmentPhase2.newInstance(getActivity());
                    fragment.show(getChildFragmentManager(), TabletSignupFragmentPhase2.CLASS_NAME);
                }
                dialog.dismiss();
            }
        });
        dialog.show(getChildFragmentManager(), MessageDialog.CLASS_NAME);
    }


    private void attemptLogin() {
        final String id = mIdInput.getText().toString();
        final String password = mPasswordInput.getText().toString();

        if (id.length() == 0) {
            mIdInput.setError("Please input your id.", null);
            return;
        }
        if (password.length() == 0) {
            mPasswordInput.setError(getString(R.string.myaccount_current_pw_desc), null);
            return;
        }
        if (NetworkUtil.checkNetwork(getActivity()) == NetworkUtil.NetworkType.DISCONNECT) {
            App.showAlertDialogNetwork(getActivity(), getActivity().getSupportFragmentManager(), null);
            return;
        }

        showProgress();

        FrontEndLoader.getInstance().requestLogin(id, password, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {

                hideProgress();
                Login login = FrontEndLoader.getInstance().getLoginRes();
                if (mAutoLoginCheck.isChecked()) {
                    HandheldAuthorization.getInstance().putBoolean(HandheldAuthorization.IS_KEEP_SIGN_IN, mAutoLoginCheck.isChecked());
                    HandheldAuthorization.getInstance().putString(HandheldAuthorization.ID, id);
                    HandheldAuthorization.getInstance().putString(HandheldAuthorization.PW, password);
                }

                ProgramLoader.getInstance().clearData();
                CampaignLoader.getInstance().clearData();
                //Check register device
                if (obj != null && obj instanceof MyDeviceAccount && isLimitedDeviceThroughStatus(login)) {
                    showSwitchConfirmDialog((MyDeviceAccount) obj);
                    dismiss();
                    return;
                } else {
                    showLoginMessage(login);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                hideProgress();
                mPasswordInput.setText("");
                showKeyboard();
//                mPasswordErrorMessage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(ApiError error) {
                hideProgress();

                ApiError.Error errorResult = error.getError();
                if (errorResult.getStatus() == 401 && errorResult.getCode().equals("F0102")) {
                    mPasswordInput.setText("");
                    showKeyboard();
//                    mPasswordErrorMessage.setVisibility(View.VISIBLE);
                } else {
                    App.showAlertDialog(getActivity(), getChildFragmentManager(), error);
                }
            }
        }, (GlobalActivity) getActivity());
        //Login 시도
    }

    private boolean isLimitedDeviceThroughStatus(Login login) {
        if (login != null && login.getStatus().equalsIgnoreCase(TabletLoginFragmentPhase2.STATUS_READY)) {
            return true;
        } else return false;
    }

    private void showSwitchConfirmDialog(final MyDeviceAccount deviceAccount) {
        if (deviceAccount == null) return;
        final DeviceLimitDialog dialog = new DeviceLimitDialog();
        Bundle args = new Bundle();
        args.putString(DeviceLimitDialog.PARAM_TITLE, getString(R.string.msgTitleDeviceLimitDialog));
        args.putString(DeviceLimitDialog.PARAM_SUBTITLE1, getString(R.string.msgsubTitleDeviceLimitDialog1));
        args.putString(DeviceLimitDialog.PARAM_SUBTITLE2, getString(R.string.msgsubTitleDeviceLimitDialog2));

        args.putString(DeviceLimitDialog.PARAM_BUTTON1, getString(R.string.yes));
        args.putString(DeviceLimitDialog.PARAM_BUTTON2, getString(R.string.no));
        args.putString(DeviceLimitDialog.PARAM_ID, deviceAccount.getId());
        args.putInt(DeviceLimitDialog.PARAM_NUM_DEVICE_LIMIT, deviceAccount.getRegistered_device().getTotal());
        args.putBoolean(DeviceLimitDialog.PARAM_CANCEL_ON_TOUCH_OUTSIDE, false);


        dialog.setArguments(args);
        dialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnConfirm) {
                    RegistedDevice registedDevice = deviceAccount.getRegistered_device();
                    if (registedDevice != null && isLimitedDeviceThroughTotal(registedDevice)) {
                        if (registedDevice.getTotal() == 1) {
                            switchSelectRegisterDevice(dialog, registedDevice, switchDeviceCallback);
                        } else {
                            showListDeviceDialog(deviceAccount, switchDeviceCallback);
                        }
                    }
                    dialog.dismiss();
                } else {
                    logout(dialog);
                }
            }
        });
        dialog.setCancelable(false);
        dialog.show(getChildFragmentManager(), DeviceLimitDialog.CLASS_NAME);
    }

    private boolean isLimitedDeviceThroughTotal(RegistedDevice registedDevice) {
        if (registedDevice != null && registedDevice.getRegistered() >= registedDevice.getTotal()) {
            return true;
        } else return false;
    }

    private void logout(final DialogFragment dialog) {
        FrontEndLoader.getInstance().requestLogout(new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                HandheldAuthorization.getInstance().logOut();
                MyContentManager.getInstance().clearData();
                ChannelManager.getInstance().clearData();
                ProgramLoader.getInstance().clearData();
                Intent intent = new Intent(GlobalKey.MainActivityKey.REFRESH_DATA);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                if (dialog != null)
                    dialog.dismiss();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                hideProgress();
                App.showAlertDialogNetwork(getActivity(), getActivity().getSupportFragmentManager(), null);
            }

            @Override
            public void onError(ApiError error) {
                App.showAlertDialog(getActivity(), getActivity().getSupportFragmentManager(), error, null);
            }
        });
    }

    WindmillCallback switchDeviceCallback = new WindmillCallback() {
        @Override
        public void onSuccess(Object obj) {
            hideProgress();

            //Check register device
            if (obj != null) {
                MyDeviceAccount myDeviceAccount = (MyDeviceAccount) obj;
                RegistedDevice registedDevice = myDeviceAccount.getRegistered_device();

                if (registedDevice.getRegistered() > registedDevice.getTotal()) {
                    showListDeviceDialog(myDeviceAccount, switchDeviceCallback);
                }
            }
            showLoginMessage(FrontEndLoader.getInstance().getLoginRes());
        }

        @Override
        public void onFailure(Call call, Throwable t) {
            hideProgress();
            mPasswordInput.setText("");
            showKeyboard();
//            mPasswordErrorMessage.setVisibility(View.VISIBLE);
        }

        @Override
        public void onError(ApiError error) {

        }
    };

    private void showListDeviceDialog(final MyDeviceAccount deviceAccount, final WindmillCallback callback) {
        if (deviceAccount == null) return;

        final RegistedDevice registedDevice = deviceAccount.getRegistered_device();
        if (registedDevice == null) return;
        Bundle args = new Bundle();
        args.putParcelable(ListDeviceLoginDialog.PARAM_PRODUCT, registedDevice);
        args.putString(ListDeviceLoginDialog.DEVICE_ACCOUNT, WindmillConfiguration.deviceId);
        final ListDeviceLoginDialog dialog = new ListDeviceLoginDialog();
        dialog.setArguments(args);
        dialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnCancel) {
                    logout(null);
                    dialog.dismiss();
                } else if (v.getId() == R.id.btnConfirm) {
                    switchSelectRegisterDevice(dialog, registedDevice, callback);
                }

            }
        });
        dialog.setCancelable(false);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dialog.show(getFragmentManager(), ListDeviceLoginDialog.CLASS_NAME);
            }
        });

    }

    private void switchSelectRegisterDevice(final DialogFragment dialog, RegistedDevice registedDevice, final WindmillCallback callback) {
        showProgress();
        ArrayList<MyDeviceAccount.Devices> device = new ArrayList<>();
        // Case 1 : limit =1
        if (dialog != null && dialog instanceof DeviceLimitDialog && registedDevice.getTotal() == 1) {
            device = registedDevice.getDevices();
        }
        // Case 2 : limit >1
        else if (dialog != null && dialog instanceof ListDeviceLoginDialog) {
            if (((ListDeviceLoginDialog) dialog).getSelectedDevice().size() <= 0)
                return;
            else {
                device = ((ListDeviceLoginDialog) dialog).getSelectedDevice();
            }
        }

        OnSelectDeRegisterDevice(device, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                if (obj != null && obj instanceof Login) {
                    HandheldAuthorization.getInstance().loginInfoInitRefreshToken((Login) obj);
                }
                ChannelManager.getInstance().clearData();
                hideProgress();
                dialog.dismiss();
                FrontEndLoader.getInstance().getMyAccount(callback, false, (GlobalActivity) getActivity(), false);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                App.showAlertDialogNetwork(getActivity(), getChildFragmentManager(), null);
                hideProgress();
            }

            @Override
            public void onError(ApiError error) {
                App.showAlertDialog(getActivity(), getChildFragmentManager(), error);
                hideProgress();
            }
        });
    }

    private void OnSelectDeRegisterDevice(ArrayList<MyDeviceAccount.Devices> device, final WindmillCallback callback
    ) {
        FrontEndLoader.getInstance().requestSwitchDevice(device, callback);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null) mOnDismissListener.onDismiss(dialog);
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

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    private void showLoginMessage(Login res) {
        dismiss();
        String message = res.getMessage();
        if (message != null) {
            final MessageDialog dialog = new MessageDialog();
            Bundle args = new Bundle();
            args.putString(MessageDialog.PARAM_TITLE, getString(R.string.notice));
            args.putString(MessageDialog.PARAM_MESSAGE, message);
            args.putString(MessageDialog.PARAM_BUTTON_1, getString(R.string.ok));
            dialog.setArguments(args);
            dialog.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show(getFragmentManager(), MessageDialog.CLASS_NAME);
        }

    }

//    private void showChangePasswordDialog() {
//        final MessageDialog dialog = new MessageDialog();
//        Bundle args = new Bundle();
//        args.putString(MessageDialog.PARAM_TITLE, getString(R.string.pop_change_pw));
//        args.putString(MessageDialog.PARAM_MESSAGE, getString(R.string.pop_change_pw_desc));
//        args.putString(MessageDialog.PARAM_MESSAGE_EMPHASIS, getString(R.string.pop_change_pw_desc2));
//        args.putString(MessageDialog.PARAM_MESSAGE_COMMENT, getString(R.string.pop_change_pw_desc3));
//        args.putString(MessageDialog.PARAM_BUTTON_1, getString(R.string.ok));
//        args.putString(MessageDialog.PARAM_BUTTON_2, getString(R.string.later));
//        dialog.setArguments(args);
//        dialog.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v.getId() == R.id.button1) {
//                    goToMenu("/Setting/User Account");
//                }
//                dismiss();
//            }
//        });
//        dialog.show(getChildFragmentManager(), MessageDialog.CLASS_NAME);
//
//    }

//    private void goToMenu(String menuId) {
//        Menu menu = MenuManager.get().getMenu(menuId);
//        if (menu == null) return;
//        Intent intent = new Intent(getActivity(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        AccountManager.get().setIsJumpPassword(true);
//        intent.putExtra(MainActivity.PARAM_MENU, menu);
//        startActivity(intent);
//    }

    public Integer getFutureAction() {
        return mFutureAction;
    }

    @Override
    public void dismiss() {
        hideKeyboard();
        super.dismiss();
    }

    public void showKeyboard() {
        if (mIdInput == null) {
            return;
        }
        try {
            mIdInput.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIdInput.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mIdInput, 0);
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
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ignored) {
        }
    }

}