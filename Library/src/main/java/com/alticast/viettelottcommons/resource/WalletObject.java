package com.alticast.viettelottcommons.resource;

import android.content.Context;
import android.view.View;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.util.TextUtils;
import com.alticast.viettelottcommons.util.TimeUtil;
import com.alticast.viettelottcommons.util.WindDataConverter;

/**
 * Created by duyuno on 2/25/17.
 */
public class WalletObject {
    protected int priceValue;
    protected long rentalPeridod;
    protected boolean isPurchased;
    protected String time_renewal;
    protected String subscription_star;

    public int getPriceValue(String type) {
        return priceValue;
    }

    public void setPrice(int price) {
        this.priceValue = price;
    }

    public long getRentalPeridod() {
        return rentalPeridod;
    }

    public void setRentalPeridod(long rentalPeridod) {
        this.rentalPeridod = rentalPeridod;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    public String getTime_renewal() {
        return time_renewal;
    }

    public void setTime_renewal(String time_renewal) {
        this.time_renewal = time_renewal;
    }

    public String getPriceString() {
        return TextUtils.getNumberString(priceValue);
    }

    public long getSubscription_star() {
        return TimeUtil.getLongTime(subscription_star, WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);
    }

    public String getDurationString(Context context) {
        int period = TimeUtil.secToDay(rentalPeridod);
        if(period == 0) {
            return rentalPeridod / 3600 + " " + context.getString(R.string.justHour);
        } else {
            return period + " " + context.getString(R.string.justDay);
        }
    }

    public void setSubscription_star(String subscription_star) {
        this.subscription_star = subscription_star;
    }
}
