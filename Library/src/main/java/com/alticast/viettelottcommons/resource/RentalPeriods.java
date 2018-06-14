package com.alticast.viettelottcommons.resource;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.util.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by mc.kim on 8/8/2016.
 */
public class RentalPeriods implements Parcelable, Comparable{
    private int period = 0;
    private int max_screen = 0;
    private String unit = null;
    private ArrayList<Price> price = null;
    private boolean isPurchased;
    protected String time_renewal;
    protected String subscription_star;
    private String productId;

    public RentalPeriods() {
    }

    public int getMax_screen() {
        return max_screen;
    }

    public int getPeriod() {
        return period;
    }

    public String getUnit() {
        return unit;
    }

    public ArrayList<Price> getPrice() {
        return price;
    }

    public Price getPrice(String type){
        if(price == null || price.isEmpty()) {
            return null;
        }
        int size = price.size();
        for(int i = 0 ; i < size ; i++){
            if(price.get(i).getCurrency().equals(type)){
                return price.get(i);
            }
        }
        return price.get(0);
    }

    public float getPriceValue() {
        Price price = getPrice(Product.CURRENCY_VND);
        if(price == null) {
            return -1;
        }

        return price.getValue();
    }

    public String getPriceString(Product product) {
        return TextUtils.getNumberString(product.getFpackageFinalPrice(this, Product.CURRENCY_VND));
    }

    public String getDurationString(Context context) {
        switch (period) {
            case 0:
                return "N/A";
            case 1:
                return "1 " + context.getResources().getString(R.string.justDay);
            case 7:
                return "1 " + context.getResources().getString(R.string.justWeek);
            case 30:
                return "1 " + context.getResources().getString(R.string.justMothn);
            default:
                return period + " " + context.getResources().getString(R.string.justDay);
//                int month = period / 30;
//                if(month == 0) {
//                    return period + " " + context.getResources().getString(R.string.justDay);
//                }else {
//                    return month + " " + context.getResources().getString(R.string.justMothn);
//                }

        }
    }
    public String getDurationUnitString(Context context) {
        switch (period) {
            case 0:
                return "N/A";
            case 1:
                return " " + context.getResources().getString(R.string.justDay);
            case 7:
                return " " + context.getResources().getString(R.string.justWeek);
            case 30:
                return " " + context.getResources().getString(R.string.justMothn);
            default:
                return period + " " + context.getResources().getString(R.string.justDay);
//                int month = period / 30;
//                if(month == 0) {
//                    return period + " " + context.getResources().getString(R.string.justDay);
//                }else {
//                    return month + " " + context.getResources().getString(R.string.justMothn);
//                }

        }
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTime_renewal() {
        return time_renewal;
    }

    public void setTime_renewal(String time_renewal) {
        this.time_renewal = time_renewal;
    }

    public String getSubscription_star() {
        return subscription_star;
    }

    public void setSubscription_star(String subscription_star) {
        this.subscription_star = subscription_star;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPrice(ArrayList<Price> price) {
        this.price = price;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    protected RentalPeriods(Parcel in) {
        period = in.readInt();
        unit = in.readString();
        Parcelable[] parcelables = in.readParcelableArray(Price.class.getClassLoader());
        price = new ArrayList<>();
        Collections.addAll(price, Arrays.copyOf(parcelables, parcelables.length, Price[].class));
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(period);
        dest.writeString(unit);
        if (price == null)
            price = new ArrayList<>();
        dest.writeParcelableArray(price.toArray(new Price[price.size()]), 0);
    }

    @SuppressWarnings("unused")
    public static final Creator<RentalPeriods> CREATOR = new Creator<RentalPeriods>() {
        @Override
        public RentalPeriods createFromParcel(Parcel in) {
            return new RentalPeriods(in);
        }

        @Override
        public RentalPeriods[] newArray(int size) {
            return new RentalPeriods[size];
        }
    };

    @Override
    public int compareTo(Object another) {

        if(!(another instanceof RentalPeriods)) {
            return 0;
        }

        int rental = ((RentalPeriods) another).getPeriod();

        return period - rental;
    }
}
