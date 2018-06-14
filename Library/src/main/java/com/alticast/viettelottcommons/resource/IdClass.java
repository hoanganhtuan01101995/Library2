package com.alticast.viettelottcommons.resource;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by duyuno on 10/15/16.
 */
public class IdClass implements Parcelable {
    public String id = null;
    public String name = null;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }


    protected IdClass(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    @SuppressWarnings("unused")
    public static final Creator<IdClass> CREATOR = new Creator<IdClass>() {
        @Override
        public IdClass createFromParcel(Parcel in) {
            return new IdClass(in);
        }

        @Override
        public IdClass[] newArray(int size) {
            return new IdClass[size];
        }
    };
}
