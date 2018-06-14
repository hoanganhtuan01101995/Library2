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
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.App;
import com.alticast.viettelottcommons.fragment.FragmentBase;
import com.alticast.viettelottcommons.resource.ChargeAmountObj;
import com.alticast.viettelottcommons.util.TextUtils;
import com.alticast.viettelottcommons.widget.FontCheckBox;

/**
 * Created by duyuno on 9/9/17.
 */
public class FragmentChargePhoneAction extends FragmentBase {
    public static FragmentChargePhoneAction newInstance() {
        FragmentChargePhoneAction fragmentFirst = new FragmentChargePhoneAction();
        return fragmentFirst;
    }

    private ChargeAmountObj chargeAmountObj;
    private String phoneNumber;
    EditText id_input;
    Button btnConfirm;

    public void setChargeAmountObj(ChargeAmountObj chargeAmountObj, String phoneNumber) {
        this.chargeAmountObj = chargeAmountObj;
        this.phoneNumber = phoneNumber;
    }

    TextView txtVWalletBalance, txtBonus;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_charge_action, container, false);

        initView();
        initData();

        return view;
    }

    public void initView() {
        txtVWalletBalance = (TextView) view.findViewById(R.id.txtVWalletBalance);
        txtBonus = (TextView) view.findViewById(R.id.txtBonus);
        id_input = (EditText) view.findViewById(R.id.id_input);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(onClickListener);
        view.findViewById(R.id.btnCancel).setOnClickListener(onClickListener);

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    activity.hideAllKeyboard();
                }
            }
        });

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
        id_input.addTextChangedListener(new AmountTextWatcher());
        setEnableButtonWithDim(btnConfirm, false);
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
            setEnableButtonWithDim(btnConfirm, s.length() > 0);

        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.showKeyBoard(id_input);
    }

    public void initData() {
        txtVWalletBalance.setText(TextUtils.getNumberString(chargeAmountObj.getAmount()));
        txtBonus.setText(chargeAmountObj.getBonusDisplay(FragmentChargeManager.walletTopupMethod));
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.btnConfirm) {
                onClickConfirm();
            }
            else if (i == R.id.btnCancel) {
//                onBackPress();
                activity.closeVWallet();
            }
        }
    };

    public void onClickConfirm() {
        String otp = validateInput();
        if(otp != null) {
            activity.hideAllKeyboard();
            FragmentChargeManager.goToChargePhoneConfirm(activity, chargeAmountObj, otp, phoneNumber, 0);
        }
    }

    public String validateInput() {
        String otp = id_input.getText().toString().trim();
        if(otp.length() == 0) {
            App.showAlertDialog(activity, activity.getSupportFragmentManager(), null, "Bạn chưa nhập mã OTP!", null);
            return null;
        }
        return otp;
    }

    @Override
    public void onBackPress() {
        FragmentChargeManager.goToChargePhoneMainOptions(activity, 1);
    }
}
