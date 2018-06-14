package com.alticast.viettelottcommons.fragment.vwallet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.App;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.fragment.FragmentBase;
import com.alticast.viettelottcommons.loader.VWalletLoader;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.ChargeAmountObj;
import com.alticast.viettelottcommons.resource.OTPresult;
import com.alticast.viettelottcommons.util.TextUtils;
import com.alticast.viettelottcommons.widget.FontCheckBox;

import retrofit2.Call;

/**
 * Created by duyuno on 9/9/17.
 */
public class FragmentChargePhoneGetOTP extends FragmentBase {
    public static FragmentChargePhoneGetOTP newInstance() {
        FragmentChargePhoneGetOTP fragmentFirst = new FragmentChargePhoneGetOTP();
        return fragmentFirst;
    }

    private ChargeAmountObj chargeAmountObj;

    EditText id_input;
    LinearLayout layoutRememberPhone;
    FontCheckBox checkIdPhone;
    Button btnSendPassword;
    boolean isRememberPhone;

    public void setChargeAmountObj(ChargeAmountObj chargeAmountObj) {
        this.chargeAmountObj = chargeAmountObj;
    }

    TextView txtVWalletBalance, txtBonus;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_charge_get_otp, container, false);

        initView();
        initData();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.showKeyBoard(id_input);
    }

    public void initView() {
        txtVWalletBalance = (TextView) view.findViewById(R.id.txtVWalletBalance);
        txtBonus = (TextView) view.findViewById(R.id.txtBonus);
        id_input = (EditText) view.findViewById(R.id.id_input);
        checkIdPhone = (FontCheckBox) view.findViewById(R.id.checkIdPhone);
        btnSendPassword = (Button) view.findViewById(R.id.btnSendPassword);

        view.findViewById(R.id.layoutRememberPhone).setOnClickListener(onClickListener);
        btnSendPassword.setOnClickListener(onClickListener);
        view.findViewById(R.id.btnCancel).setOnClickListener(onClickListener);


        id_input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    if(event.getAction() == KeyEvent.ACTION_UP) {
                        onClickConfirm();
                    }
                    return true;
                }
                return false;
            }
        });

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    activity.hideAllKeyboard();
                }
            }
        });

        setEnableButtonWithDim(btnSendPassword, false);
        id_input.addTextChangedListener(new AmountTextWatcher());
    }

    private class AmountTextWatcher implements TextWatcher {

        public AmountTextWatcher() {
            super();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setEnableButtonWithDim(btnSendPassword, s.length() > 0);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    }

    public void initData() {
        txtVWalletBalance.setText(TextUtils.getNumberString(chargeAmountObj.getAmount()));
        txtBonus.setText(chargeAmountObj.getBonusDisplay(FragmentChargeManager.walletTopupMethod));

        isRememberPhone = HandheldAuthorization.getInstance().getBoolean(HandheldAuthorization.WALLET_CHARGE_PHONE_REMEMVER);
        if(isRememberPhone) {
            checkIdPhone.setChecked(true);
            String phoneNumber = HandheldAuthorization.getInstance().getString(HandheldAuthorization.WALLET_CHARGE_PHONE);
            if(phoneNumber != null) {
                id_input.setText(phoneNumber);
            }
        } else {
            checkIdPhone.setChecked(false);
        }


    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.btnSendPassword) {
                onClickConfirm();
            }
            else if (i == R.id.btnCancel) {
//                onBackPress();
                activity.closeVWallet();
            }
            else if (i == R.id.layoutRememberPhone) {
                isRememberPhone = !isRememberPhone;
                HandheldAuthorization.getInstance().putBoolean(HandheldAuthorization.WALLET_CHARGE_PHONE_REMEMVER, isRememberPhone);
                checkIdPhone.setChecked(isRememberPhone);
            }
        }
    };

    public void onClickConfirm() {
        String phoneNumber = validateInput();
        if(phoneNumber != null) {
            doGetOtp(phoneNumber);
        }
    }

    public void doGetOtp(final String phoneNumber) {
        activity.showProgress(getChildFragmentManager());

        VWalletLoader.getInstance().requestOtp(phoneNumber, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {

                activity.hideProgress();

                if(isRememberPhone) {
                    HandheldAuthorization.getInstance().putString(HandheldAuthorization.WALLET_CHARGE_PHONE, phoneNumber);
                }
                FragmentChargeManager.goToChargePhoneAction(activity, chargeAmountObj, phoneNumber, 0);

            }

            @Override
            public void onFailure(Call call, Throwable t) {

                activity.hideProgress();
                App.showAlertDialogNetwork(activity, getChildFragmentManager(), null);
            }

            @Override
            public void onError(ApiError error) {
                activity.hideProgress();
                App.showAlertDialog(activity, getChildFragmentManager(), error);
            }
        });
    }

    public String validateInput() {
        String phone = id_input.getText().toString().trim();
        if(phone.length() == 0) {
            App.showAlertDialog(activity, activity.getSupportFragmentManager(), null, "Bạn chưa nhập số điện thoại!", null);
            return null;
        }
        try {
            Long.parseLong(phone);
        } catch (Exception e) {
            App.showAlertDialog(activity, activity.getSupportFragmentManager(), null, "Số điện thoại không hợp lệ!", null);
            return null;
        }
        return phone;


    }


    @Override
    public void onBackPress() {
        FragmentChargeManager.goToChargePhoneMainOptions(activity, 1);
    }
}
