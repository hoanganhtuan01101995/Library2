package com.alticast.viettelottcommons.resource;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by duyuno on 10/15/16.
 */
public class CountClass implements Parcelable {
    public int total;
    public IdClass[] data = null;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total);
        if(data == null) {
            data = new IdClass[0];
        }
//        if (this.data != null && this.data.length > 0)
            dest.writeParcelableArray(this.data, 0);
    }


    public CountClass(Parcel in) {
        this.total = in.readInt();
        Parcelable[] parcelables = in.readParcelableArray(IdClass.class.getClassLoader());
        if (parcelables != null && parcelables.length > 0)
            this.data = Arrays.copyOf(parcelables, parcelables.length, IdClass[].class);
    }

    @SuppressWarnings("unused")
    public static final Creator<CountClass> CREATOR = new Creator<CountClass>() {
        @Override
        public CountClass createFromParcel(Parcel in) {
            return new CountClass(in);
        }

        @Override
        public CountClass[] newArray(int size) {
            return new CountClass[size];
        }
    };
}
