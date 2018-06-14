package com.alticast.viettelottcommons.resource;

/**
 * Created by mc.kim on 8/8/2016.
 */
public class PriceSummery {
    private Price point = null;
    private Price amount = null;
    private Price phone = null;
    private Price wallet = null;

    public Price getPoint() {
        return point;
    }

    public void setPoint(Price point) {
        this.point = point;
    }

    public Price getAmount() {
        return amount;
    }

    public void setAmount(Price amount) {
        this.amount = amount;
    }

    public Price getPhone() {
        return phone;
    }

    public void setPhone(Price phone) {
        this.phone = phone;
    }

    public Price getWallet() {
        return wallet;
    }

    public void setWallet(Price wallet) {
        this.wallet = wallet;
    }
}
