package com.alticast.viettelottcommons.resource;

/**
 * Created by mc.kim on 8/5/2016.
 */
public class Payment {
    private int amount = 0;
    private String date = null;
    private String type = null;
    private Price total_amount = null;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public Price getTotal_amount() {
        return total_amount;
    }
}
