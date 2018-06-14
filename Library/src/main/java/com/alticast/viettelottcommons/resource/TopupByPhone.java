package com.alticast.viettelottcommons.resource;

/**
 * Created by user on 2016-03-08.
 */
public class TopupByPhone {
    private String phone_number = null;
    private String otp = null;
    private int topup_amount = 0;
    private int bonus_rate = 0 ;
    private int bonus_amount = 0;
    private String promotion_id = null;

    public TopupByPhone(String phone_number, String otp, int topup_amount, int bonus_rate, int bonus_amount, String promotion_id) {
        this.phone_number = phone_number;
        this.otp = otp;
        this.topup_amount = topup_amount;
        this.bonus_rate = bonus_rate;
        this.bonus_amount = bonus_amount;
        this.promotion_id = promotion_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

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

    public String getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(String promotion_id) {
        this.promotion_id = promotion_id;
    }

    @Override
    public String toString() {
        return "TopupByPhone{" +
                "phone_number='" + phone_number + '\'' +
                ", otp=" + otp +
                ", topup_amount=" + topup_amount +
                ", bonus_rate=" + bonus_rate +
                ", bonus_amount=" + bonus_amount +
                ", promotion_id='" + promotion_id + '\'' +
                '}';
    }
}
