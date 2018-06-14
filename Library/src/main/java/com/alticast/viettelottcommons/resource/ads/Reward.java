package com.alticast.viettelottcommons.resource.ads;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2016-05-17.
 */
public class Reward implements Parcelable{
    /**가능한 event목록은 tracking의 event와 동일*/
    private String event = null;
    /**event에 정의된 상황인 경우, url 호출*/
    private String url = null;

    @Override
    public String toString() {
        return "Reward{" +
                "event='" + event + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Reward(Parcel in) {
        event = in.readString();
        url  = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(event);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Creator<Reward> CREATOR = new Creator<Reward>() {
        @Override
        public Reward createFromParcel(Parcel in) {
            return new Reward(in);
        }

        @Override
        public Reward[] newArray(int size) {
            return new Reward[size];
        }
    };
}
