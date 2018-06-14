package com.alticast.viettelottcommons.fragment.vwallet;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.App;
import com.alticast.viettelottcommons.adapter.SimpleListAdapter;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.dialog.ProgramPurchaseConfirmDialogPhase2;
import com.alticast.viettelottcommons.dialog.VWalletConfirmChargeDialog;
import com.alticast.viettelottcommons.fragment.FragmentBase;
import com.alticast.viettelottcommons.loader.VWalletLoader;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.ChargeAmountObj;
import com.alticast.viettelottcommons.resource.ChargedResult;
import com.alticast.viettelottcommons.resource.WalletTopupMethod;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by duyuno on 9/9/17.
 */
public class FragmentChargeScratch extends FragmentBase {

    public static FragmentChargeScratch newInstance() {
        FragmentChargeScratch fragmentFirst = new FragmentChargeScratch();
        return fragmentFirst;
    }

    private WalletTopupMethod walletTopupMethod;

    public void setWalletTopupMethod(WalletTopupMethod walletTopupMethod) {
        this.walletTopupMethod = walletTopupMethod;
    }

    private TextView txtPromotion = null;
    private EditText id_input_card_number, id_input_pin_number;
    private Button btnCharge, btnCancel;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_charge_scratch, container, false);

        initView();

//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        return view;
    }

    public void initView() {
        txtPromotion = (TextView) view.findViewById(R.id.txtPromotion);

        txtPromotion.setLineSpacing(1, 1.2f);

        id_input_card_number = (EditText) view.findViewById(R.id.id_input_card_number);
        id_input_pin_number = (EditText) view.findViewById(R.id.id_input_pin_number);

        btnCharge = (Button) view.findViewById(R.id.btnCharge);

        btnCharge.setOnClickListener(onClickListener);
        view.findViewById(R.id.btnCancel).setOnClickListener(onClickListener);

        if (walletTopupMethod.getPromotion() != null) {
            txtPromotion.setVisibility(View.VISIBLE);
            String proMessage = walletTopupMethod.getPromotion().getDescription(activity);
            if (FragmentChargeManager.isTablet) {
                proMessage += "\n";
            } else {
                proMessage += ":";
            }
            proMessage += " (" + walletTopupMethod.getPromotion().getStartDate() + " - " + walletTopupMethod.getPromotion().getEndDate() + ")";
            txtPromotion.setText(proMessage);
        } else {
            txtPromotion.setVisibility(View.GONE);
        }

        AmountTextWatcher amountTextWatcher = new AmountTextWatcher();

        id_input_card_number.addTextChangedListener(amountTextWatcher);
        id_input_pin_number.addTextChangedListener(amountTextWatcher);

        setEnableButtonWithDim(btnCharge, false);

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    activity.hideAllKeyboard();
                }
            }
        });

        id_input_pin_number.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    if(event.getAction() == KeyEvent.ACTION_UP) {
                        checkInput();
                    }

                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.showKeyBoard(id_input_card_number);
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.btnCharge) {
                checkInput();
            } else {
                activity.closeVWallet();
            }
        }
    };

    public void checkInput() {
        final String cardNumber = id_input_card_number.getText().toString().trim();

        if(cardNumber.length() == 0) {
            App.showAlertDialog(activity, activity.getSupportFragmentManager(), "", "Vui lòng nhập số thẻ!", new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    activity.showKeyBoard(id_input_card_number);
                }
            });
            return;
        }
        final String pinNumber = id_input_pin_number.getText().toString().trim();

        if(pinNumber.length() == 0) {
            App.showAlertDialog(activity, activity.getSupportFragmentManager(), "", "Vui lòng nhập mã PIN!", new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    activity.showKeyBoard(id_input_pin_number);
                }
            });
            return;
        }

        DialogFragment oldDialog = (DialogFragment) activity.getSupportFragmentManager().findFragmentByTag(VWalletConfirmChargeDialog.CLASS_NAME);
        if (oldDialog != null) oldDialog.dismiss();

        final VWalletConfirmChargeDialog dialog = new VWalletConfirmChargeDialog();
        dialog.setSrc(null, walletTopupMethod);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (v.getId() == com.alticast.viettelottcommons.R.id.btnConfirm) {
                    charge(cardNumber, pinNumber);
                }

            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dialog.show(activity.getSupportFragmentManager(), VWalletConfirmChargeDialog.CLASS_NAME);
            }
        });


    }

    public void charge(String cardNumber, String pinNumber) {
        activity.showProgress(getChildFragmentManager());
        VWalletLoader.getInstance().requsetTopUpByScratchCard(cardNumber, pinNumber, walletTopupMethod.getPromotionId(), new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                activity.hideProgress();
                FragmentChargeManager.goToChargePhoneComplete(activity, (ChargedResult) obj);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                activity.hideProgress();
                App.showAlertDialogNetwork(getContext(), getChildFragmentManager(), null);
            }

            @Override
            public void onError(ApiError error) {
                activity.hideProgress();
                App.showAlertDialog(getContext(), getChildFragmentManager(), error);
            }
        });
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
            String serial = id_input_card_number.getText().toString().trim();
            String pin = id_input_pin_number.getText().toString().trim();
            setEnableButtonWithDim(btnCharge, serial.length() > 0 && pin.length() > 0);

        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    }

    @Override
    public void onBackPress() {
        activity.closeVWallet();
    }


}
