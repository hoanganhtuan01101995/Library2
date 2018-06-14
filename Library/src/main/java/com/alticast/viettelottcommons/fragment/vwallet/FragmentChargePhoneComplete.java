package com.alticast.viettelottcommons.fragment.vwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alticast.viettelottcommons.GlobalKey;
import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.fragment.FragmentBase;
import com.alticast.viettelottcommons.resource.ChargeAmountObj;
import com.alticast.viettelottcommons.resource.ChargedResult;
import com.alticast.viettelottcommons.resource.WalletTopupMethod;
import com.alticast.viettelottcommons.util.TextUtils;

import java.util.Date;

/**
 * Created by duyuno on 9/9/17.
 */
public class FragmentChargePhoneComplete extends FragmentBase {
    public static FragmentChargePhoneComplete newInstance() {
        FragmentChargePhoneComplete fragmentFirst = new FragmentChargePhoneComplete();
        return fragmentFirst;
    }

    ChargedResult chargedResult;

    TextView txtVWalletBalance, txtVWalletBonus, txtTopupAmout, txtTopupBonus;
    TextView txtDate, txtHour;

    public void setChargeAmountObj(ChargedResult chargedResult) {
        this.chargedResult = chargedResult;
    }

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_charge_vwallet_complete, container, false);

        initView();

        initData();

        return view;
    }

    public void initView() {
        txtVWalletBalance = (TextView) view.findViewById(R.id.txtVWalletBalance);
        txtVWalletBonus = (TextView) view.findViewById(R.id.txtVWalletBonus);
        txtTopupAmout = (TextView) view.findViewById(R.id.txtTopupAmout);
        txtTopupBonus = (TextView) view.findViewById(R.id.txtTopupBonus);
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtHour = (TextView) view.findViewById(R.id.txtHour);

        view.findViewById(R.id.btnClose).setOnClickListener(onClickListener);
        view.findViewById(R.id.btnCheckTopup).setOnClickListener(onClickListener);
    }

    public void initData() {

        Intent intent = new Intent(GlobalKey.PURCHASE_COMPLETE);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);


        if(chargedResult == null) return;

        if(chargedResult.getWallet() != null) {
            txtVWalletBalance.setText(TextUtils.getNumberString(chargedResult.getWallet().getTopup()));
            txtVWalletBonus.setText(TextUtils.getNumberString(chargedResult.getWallet().getBonus()));
        }
        if(chargedResult.getTopup_wallet() != null) {
            txtTopupAmout.setText(TextUtils.getNumberString(chargedResult.getTopup_wallet().getTopup()));
            txtTopupBonus.setText(TextUtils.getNumberString(chargedResult.getTopup_wallet().getBonus()));
        }

        txtDate.setText(chargedResult.getDateDisplay());
        txtHour.setText(chargedResult.getHourDisplay());
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.btnClose) {
                FragmentChargeManager.closeVWallet(activity);
            } else if (i == R.id.btnCheckTopup) {
                FragmentChargeManager.closeVWallet(activity);
                LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent(GlobalKey.VWALLET_GO_TO_TOPUP_HISTORY));
            }
        }
    };

    @Override
    public void onBackPress() {
        FragmentChargeManager.closeVWallet(activity);
    }
}
