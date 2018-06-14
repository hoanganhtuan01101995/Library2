package com.alticast.viettelottcommons.resource;

import com.alticast.viettelottcommons.util.TextUtils;
import com.alticast.viettelottcommons.util.TimeUtil;
import com.alticast.viettelottcommons.util.WindDataConverter;

public class ChargedResult {
    private String date = null;
    private String tx_id = null;
    private com.alticast.viettelottcommons.resource.Wallet topup_wallet = null;
    private com.alticast.viettelottcommons.resource.Wallet wallet = null;

    public String getTx_id() {
        return tx_id;
    }

    public void setTx_id(String tx_id) {
        this.tx_id = tx_id;
    }

    public com.alticast.viettelottcommons.resource.Wallet getTopup_wallet() {
        return topup_wallet;
    }

    public void setTopup_wallet(com.alticast.viettelottcommons.resource.Wallet topup_wallet) {
        this.topup_wallet = topup_wallet;
    }

    public com.alticast.viettelottcommons.resource.Wallet getWallet() {
        return wallet;
    }

    public void setWallet(com.alticast.viettelottcommons.resource.Wallet wallet) {
        this.wallet = wallet;
    }

    public String getDateDisplay() {
        long time = TimeUtil.getLongTime(date, WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);
        return TextUtils.getDateString(time, WindDataConverter.WINDMIL_PURCHASE_DETAIL_DATE_FORMAT).toUpperCase();
    }
    public String getHourDisplay() {
        long time = TimeUtil.getLongTime(date, WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);
        return TextUtils.getDateString(time, WindDataConverter.WINDMIL_PURCHAE_TIME).toUpperCase();
    }


    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "ChargedResult{" +
                "tx_id='" + tx_id + '\'' +
                ", topup_wallet=" + topup_wallet +
                ", wallet=" + wallet +
                '}';
    }
}
