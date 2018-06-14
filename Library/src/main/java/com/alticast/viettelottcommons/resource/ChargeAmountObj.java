package com.alticast.viettelottcommons.resource;

import com.alticast.viettelottcommons.util.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by duyuno on 9/8/17.
 */
public class ChargeAmountObj {

    public long amount;
    public WalletTopupMethod.Promotion.Item  item;

    public void setItem(WalletTopupMethod.Promotion.Item item) {
        this.item = item;
    }

    public void setItem(WalletTopupMethod.Promotion.Item[] items) {
        if(items == null) {
            this.item = null;
            return;
        }

        for(WalletTopupMethod.Promotion.Item item : items) {
            if(amount >= item.getStart_amount() && amount <= item.getEnd_amont()) {
                this.item = item;
                return;
            }
        }
    }

    public String getAmountDisplay() {
        return amount > 0 ? TextUtils.getNumberString(amount) + " VNĐ" : "Nhập số tiền cần nạp";
    }

    public String getBonusDisplay(WalletTopupMethod walletTopupMethod) {
        if(item == null) {
            if(walletTopupMethod == null || walletTopupMethod.getPromotion() == null
                    || walletTopupMethod.getPromotion().getItems() == null
                    || walletTopupMethod.getPredefined_amount() == null) {
                return "";
            }
            Integer[] preAmount = walletTopupMethod.getPredefined_amount().toArray(new Integer[walletTopupMethod.getPredefined_amount().size()]);
            Arrays.sort(preAmount);
            for(int i = 0, length = preAmount.length; i < length; i++) {
                if(i >= walletTopupMethod.getPromotion().getItems().length) return "";
                if(amount < preAmount[i]) {
                    item = walletTopupMethod.getPromotion().getItems()[i];
                    break;
                }
            }
        }
        return getBonusDisplay(item);

    }

    public int getBonusRate(WalletTopupMethod walletTopupMethod) {
        if(item == null) {
            if(walletTopupMethod == null || walletTopupMethod.getPromotion() == null || walletTopupMethod.getPromotion().getItems() == null || walletTopupMethod.getPredefined_amount() == null) return 0;
            Integer[] preAmount = walletTopupMethod.getPredefined_amount().toArray(new Integer[walletTopupMethod.getPredefined_amount().size()]);
            Arrays.sort(preAmount);
            for(int i = 0, length = preAmount.length; i < length; i++) {
                if(i >= walletTopupMethod.getPromotion().getItems().length) return 0;
                if(amount < preAmount[i]) {
                    item = walletTopupMethod.getPromotion().getItems()[i];
                    break;
                }
            }
        }
        return getBonusRate(item);
    }



    public int getBonusAmount(WalletTopupMethod walletTopupMethod) {
        if(item == null) {
            if(walletTopupMethod == null || walletTopupMethod.getPromotion() == null || walletTopupMethod.getPromotion().getItems() == null || walletTopupMethod.getPredefined_amount() == null) return 0;
            Integer[] preAmount = walletTopupMethod.getPredefined_amount().toArray(new Integer[walletTopupMethod.getPredefined_amount().size()]);
            Arrays.sort(preAmount);
            for(int i = 0, length = preAmount.length; i < length; i++) {
                if(i >= walletTopupMethod.getPromotion().getItems().length) return 0;
                if(amount < preAmount[i]) {
                    item = walletTopupMethod.getPromotion().getItems()[i];
                    break;
                }
            }
        }
        return getBonusAmount(item);
    }

    public int getBonusRate(WalletTopupMethod.Promotion.Item item) {
        if(item == null) return 0;
        return item.getBonus_rate();
    }
    public int getBonusAmount(WalletTopupMethod.Promotion.Item item) {
        if(item == null) return 0;
        return item.getBonus_amount();
    }

    public String getBonusDisplay(WalletTopupMethod.Promotion.Item item) {
        if(item == null) {
            return "";
        }
        if(item.getBonus_rate() > 0) {
            return "(Khuyến mại " + item.getBonus_rate() + "%)";
        } else {
            return "(Khuyến mại " + TextUtils.getNumberString(item.getBonus_amount()) + " VNĐ)";
        }

    }

    public long getAmount() {
        return amount;
    }

    public int getBonusMoney(WalletTopupMethod walletTopupMethod) {
        if(item == null) {
            if(walletTopupMethod == null || walletTopupMethod.getPromotion() == null || walletTopupMethod.getPromotion().getItems() == null || walletTopupMethod.getPredefined_amount() == null) return 0;
            Integer[] preAmount = walletTopupMethod.getPredefined_amount().toArray(new Integer[walletTopupMethod.getPredefined_amount().size()]);
            Arrays.sort(preAmount);
            for(int i = 0, length = preAmount.length; i < length; i++) {
                if(i >= walletTopupMethod.getPromotion().getItems().length) return 0;
                if(amount < preAmount[i]) {
                    item = walletTopupMethod.getPromotion().getItems()[i];
                    break;
                }
            }
        }
        return getBonusMoney(item);
    }

    private int getBonusMoney(WalletTopupMethod.Promotion.Item item) {

        if(item == null) return 0;

        if(item.getBonus_rate() > 0) {
            return (int)(amount * ((1f * item.getBonus_rate()) / 100));
        } else {
            return item.getBonus_amount();
        }
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public WalletTopupMethod.Promotion.Item getItem() {
        return item;
    }
}
