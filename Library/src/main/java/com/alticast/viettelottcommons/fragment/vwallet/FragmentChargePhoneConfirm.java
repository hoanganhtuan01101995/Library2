package com.alticast.viettelottcommons.fragment.vwallet;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.App;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.dialog.ProgramPurchaseConfirmDialogPhase2;
import com.alticast.viettelottcommons.dialog.VWalletConfirmChargeDialog;
import com.alticast.viettelottcommons.fragment.FragmentBase;
import com.alticast.viettelottcommons.loader.VWalletLoader;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.ChargeAmountObj;
import com.alticast.viettelottcommons.resource.ChargedResult;
import com.alticast.viettelottcommons.util.TextUtils;

import retrofit2.Call;

/**
 * Created by duyuno on 9/9/17.
 */
public class FragmentChargePhoneConfirm extends FragmentBase {
    public static FragmentChargePhoneConfirm newInstance() {
        FragmentChargePhoneConfirm fragmentFirst = new FragmentChargePhoneConfirm();
        return fragmentFirst;
    }

    private ChargeAmountObj chargeAmountObj;
    private String otp;
    private String phoneNumber;

    public void setChargeAmountObj(ChargeAmountObj chargeAmountObj, String otp, String phoneNumber) {
        this.chargeAmountObj = chargeAmountObj;
        this.otp = otp;
        this.phoneNumber = phoneNumber;
    }

    private View view;
    private TextView txtVWalletBalance, txtBonus, txtVWalletBonus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_charge_phone_confirm, container, false);

        initView();

        initData();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.hideAllKeyboard();
    }

    public void initView() {
        txtVWalletBalance = (TextView) view.findViewById(R.id.txtVWalletBalance);
        txtBonus = (TextView) view.findViewById(R.id.txtBonus);
        txtVWalletBonus = (TextView) view.findViewById(R.id.txtVWalletBonus);

        view.findViewById(R.id.btnConfirmCustomCharge).setOnClickListener(onClickListener);
        view.findViewById(R.id.btnCancelCustomCharge).setOnClickListener(onClickListener);

    }

    public void initData() {
        txtVWalletBalance.setText(TextUtils.getNumberString(chargeAmountObj.getAmount()));
        txtVWalletBonus.setText(TextUtils.getNumberString(chargeAmountObj.getBonusMoney(FragmentChargeManager.walletTopupMethod)));
        txtBonus.setText(chargeAmountObj.getBonusDisplay(FragmentChargeManager.walletTopupMethod));
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.btnConfirmCustomCharge) {
                DialogFragment oldDialog = (DialogFragment) activity.getSupportFragmentManager().findFragmentByTag(VWalletConfirmChargeDialog.CLASS_NAME);
                if (oldDialog != null) oldDialog.dismiss();

                final VWalletConfirmChargeDialog dialog = new VWalletConfirmChargeDialog();
                dialog.setSrc(chargeAmountObj, FragmentChargeManager.walletTopupMethod);
                dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (v.getId() == com.alticast.viettelottcommons.R.id.btnConfirm) {
                            doChargeAction();
                        }

                    }
                });

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show(activity.getSupportFragmentManager(), VWalletConfirmChargeDialog.CLASS_NAME);
                    }
                });

            } else if (i == R.id.btnCancelCustomCharge) {
//                onBackPress();
                activity.closeVWallet();
            }
        }
    };

    public void doChargeAction() {
        activity.showProgress(getChildFragmentManager());
        VWalletLoader.getInstance().requestTopupByMobilePhone(phoneNumber, otp, chargeAmountObj, FragmentChargeManager.walletTopupMethod, new WindmillCallback() {
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

    @Override
    public void onBackPress() {
        FragmentChargeManager.goToChargePhoneMainOptions(activity, 1);
    }
}
