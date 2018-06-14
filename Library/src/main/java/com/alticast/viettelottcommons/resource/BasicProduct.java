package com.alticast.viettelottcommons.resource;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;


public class BasicProduct implements Parcelable {
    public static final String TYPE_FLEXI = "FLEXI";
    public static final String TYPE_FLEXI_PLUS = "FLEXI+";

    private String id;
    //private String name;
    private Bundle name;
    private Bundle price;
    private Bundle purchasableProduct;

    public BasicProduct(String id, Bundle/*String*/ name, Bundle price, Bundle purchasableProduct) {
        this.id = id;
//        this.name = name;
        this.name = name;
        this.price = price;
        this.purchasableProduct = purchasableProduct;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return TextUtils.getLocaleString(name);
    }

    public Bundle getPrice() {
        return price;
    }

    public int getPrice(String type) {
        try {
            return price.getInt(type, -1);
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public String getPurchasableProduct(String type) {
        return purchasableProduct.getString(type);
    }

    //##############################################################
    public Object getPaymentMethods(){
        //TODO
        return null;
    }
    //#############################################################

    protected BasicProduct(Parcel in) {
        id = in.readString();
//        name = in.readString();
        name = in.readBundle();
        price = in.readBundle();
        purchasableProduct = in.readBundle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
//        dest.writeString(name);
        dest.writeBundle(name);
        dest.writeBundle(price);
        dest.writeBundle(purchasableProduct);
    }

    @SuppressWarnings("unused")
    public static final Creator<BasicProduct> CREATOR = new Creator<BasicProduct>() {
        @Override
        public BasicProduct createFromParcel(Parcel in) {
            return new BasicProduct(in);
        }

        @Override
        public BasicProduct[] newArray(int size) {
            return new BasicProduct[size];
        }
    };
}


