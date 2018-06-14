package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;

public class ChargingMethod {
    private int wallet_limit = 0;
    private ArrayList<com.alticast.viettelottcommons.resource.WalletTopupMethod> wallet_topup_methods = null;

    public int getWallet_limit() {
        return wallet_limit;
    }

    public void setWallet_limit(int wallet_limit) {
        this.wallet_limit = wallet_limit;
    }

    public ArrayList<com.alticast.viettelottcommons.resource.WalletTopupMethod> getWallet_topup_methods() {
        return wallet_topup_methods;
    }

    public void processData() {
        if(wallet_topup_methods == null) return;
        for(WalletTopupMethod walletTopupMethod : wallet_topup_methods) {
            walletTopupMethod.processData();
        }
    }

    public WalletTopupMethod getMethod(String type) {
        if(wallet_topup_methods == null) return null;
        for(WalletTopupMethod walletTopupMethod : wallet_topup_methods) {
            if(walletTopupMethod.getMethod().equals(type)) {
                return walletTopupMethod;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "ChargingMethod{" +
                "wallet_limit=" + wallet_limit +
                ", wallet_topup_methods=" + wallet_topup_methods +
                '}';
    }
}
