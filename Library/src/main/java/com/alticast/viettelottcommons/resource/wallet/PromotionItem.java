package com.alticast.viettelottcommons.resource.wallet;

/**
 * Created by duyuno on 9/12/17.
 */
public class PromotionItem {
    private boolean input;
    private int start_amount;
    private int end_amount;
    private int bonus_rate;
    private int bonus_amount;

    public boolean isInput() {
        return input;
    }

    public int getStart_amount() {
        return start_amount;
    }

    public int getEnd_amount() {
        return end_amount;
    }

    public int getBonus_rate() {
        return bonus_rate;
    }

    public int getBonus_amount() {
        return bonus_amount;
    }
}
