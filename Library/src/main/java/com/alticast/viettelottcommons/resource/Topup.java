package com.alticast.viettelottcommons.resource;


public class Topup {
    private int topup_amount = 0;
    private int bonus_rate = 0;
    private int bonus_amount = 0;

    public int getTopup_amount() {
        return topup_amount;
    }

    public void setTopup_amount(int topup_amount) {
        this.topup_amount = topup_amount;
    }

    public int getBonus_rate() {
        return bonus_rate;
    }

    public void setBonus_rate(int bonus_rate) {
        this.bonus_rate = bonus_rate;
    }

    public int getBonus_amount() {
        return bonus_amount;
    }

    public void setBonus_amount(int bonus_amount) {
        this.bonus_amount = bonus_amount;
    }
}
