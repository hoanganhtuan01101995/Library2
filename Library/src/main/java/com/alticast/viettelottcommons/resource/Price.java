package com.alticast.viettelottcommons.resource;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by mc.kim on 8/5/2016.
 */
public class Price implements Parcelable{

    public static final String DEVICE_HANDHELD = "HANDHELD";

    private String currency = null;
    private String device = null;
    private float value = 0;
    private float vat = 0;

    public Price() {
    }

    public String getCurrency() {
        return currency;
    }

    public float getValue() {
        return value;
    }

    public float getVat() {
        return vat;
    }

    public void setVat(float vat) {
        this.vat = vat;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isSAPrice() {
        return device != null && device.equals(DEVICE_HANDHELD);
    }

    public boolean hasDeviceValue() {
        return device != null && !device.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    protected Price(Parcel in) {
        currency = in.readString();
        device = in.readString();
        value = in.readFloat();
        vat = in.readFloat();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currency);
        dest.writeString(device != null ? device : "");
        dest.writeFloat(value);
        dest.writeFloat(vat);
    }

    @SuppressWarnings("unused")
    public static final Creator<Price> CREATOR = new Creator<Price>() {
        @Override
        public Price createFromParcel(Parcel in) {
            return new Price(in);
        }

        @Override
        public Price[] newArray(int size) {
            return new Price[size];
        }
    };
}
