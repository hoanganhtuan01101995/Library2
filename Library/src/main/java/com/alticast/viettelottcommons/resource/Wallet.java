package com.alticast.viettelottcommons.resource;


public class Wallet {
    private int topup = 0;
    private int bonus = 0;

    public int getTopup() {
        return topup;
    }

    public void setTopup(int topup) {
        this.topup = topup;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "topup=" + topup +
                ", bonus=" + bonus +
                '}';
    }
}
