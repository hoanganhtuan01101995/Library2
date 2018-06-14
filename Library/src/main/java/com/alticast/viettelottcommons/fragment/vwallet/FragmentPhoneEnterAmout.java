package com.alticast.viettelottcommons.fragment.vwallet;

import android.content.Context;
import android.content.DialogInterface;
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
import com.alticast.viettelottcommons.dialog.PhoneLoginFragmentPhase2;
import com.alticast.viettelottcommons.fragment.FragmentBase;
import com.alticast.viettelottcommons.resource.ChargeAmountObj;
import com.alticast.viettelottcommons.util.TextUtils;

/**
 * Created by duyuno on 9/9/17.
 */
public class FragmentPhoneEnterAmout extends FragmentBase {

    private ChargeAmountObj chargeAmountObj;

    EditText id_input;
    TextView txtDescription;
    TextView txtEnterAmoutDescription;
    Button btnConfirmCustomCharge;
    private int minimumAmout;

    public static FragmentPhoneEnterAmout newInstance() {
        FragmentPhoneEnterAmout fragmentFirst = new FragmentPhoneEnterAmout();
        return fragmentFirst;
    }

    public void setChargeAmountObj(ChargeAmountObj chargeAmountObj) {
        this.chargeAmountObj = chargeAmountObj;
    }

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_phone_custom_amout_charge, container, false);

        btnConfirmCustomCharge = (Button) view.findViewById(R.id.btnConfirmCustomCharge);
        id_input = (EditText) view.findViewById(R.id.id_input);
        txtDescription = (TextView) view.findViewById(R.id.txtDescription);
        txtEnterAmoutDescription = (TextView) view.findViewById(R.id.txtEnterAmoutDescription);
        view.findViewById(R.id.btnConfirmCustomCharge).setOnClickListener(onClickListener);
        view.findViewById(R.id.btnCancelCustomCharge).setOnClickListener(onClickListener);





        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        minimumAmout = FragmentChargeManager.walletTopupMethod.getConfig().getMinimum_amount();

        String money = TextUtils.getNumberString(minimumAmout);

        txtDescription.setText(String.format(getString(R.string.vWalletEnterChargeAmountDes), money));
        txtEnterAmoutDescription.setText(String.format(getString(R.string.enterChargeAmountNote), money));

        id_input.addTextChangedListener(new AmountTextWatcher());

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

        setEnableButtonWithDim(btnConfirmCustomCharge, false);

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    activity.hideAllKeyboard();
                }
            }
        });

        id_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    activity.hideAllKeyboard();
                }
            }
        });
        activity.showKeyBoard(id_input);
    }

    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(id_input.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ignored) {
        }
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
            long money = 0;
            try {
                money = Long.parseLong(s.toString());
                setEnableButtonWithDim(btnConfirmCustomCharge, money >= minimumAmout);
            } catch (Exception e) {

            }

        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.btnConfirmCustomCharge) {
                onClickConfirm();
            } else if (i == R.id.btnCancelCustomCharge) {
//                onBackPress();
                activity.closeVWallet();
            }
        }
    };

    public void onClickConfirm() {
        long amout = validateInput();
        if(amout > 0) {

            if(chargeAmountObj == null) {
                chargeAmountObj = new ChargeAmountObj();
            }

            chargeAmountObj.setAmount(amout);
            FragmentChargeManager.goToChargePhoneGetOtp(activity, chargeAmountObj, 0);
        }
    }

    public long validateInput() {
        long result = -1;
        String moneyText = id_input.getText().toString().trim();
        if(moneyText.length() == 0) {
            App.showAlertDialog(activity, getChildFragmentManager(), null, "Bạn chưa nhập số tiền cần nạp!", onDismissListener);
            return -1;
        }
        try {
            result = Long.parseLong(moneyText);
        } catch (Exception e) {
            App.showAlertDialog(activity, getChildFragmentManager(), null, "Số tiền nhập không hợp lệ!", onDismissListener);
            return -1;
        }

        if(result <= 0) {
            App.showAlertDialog(activity, getChildFragmentManager(), null, "Số tiền nhập không hợp lệ!", onDismissListener);
            return -1;
        }

        return result;


    }

    public DialogInterface.OnDismissListener onDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            activity.showKeyBoard(id_input);
        }
    };

    @Override
    public void onBackPress() {
        FragmentChargeManager.goToChargePhoneMainOptions(activity, 1);
    }
}
