package com.alticast.viettelottcommons.resource.ads;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2016-05-17.
 */
public class AdInsertion implements Parcelable{
    /**가능한 event type : play,ff,rw,pause 이벤트와 무관하게 동작*/
    private String event = null;
    /**pre/post */
    private String insertionType = null;
    /**every, once */
    private String frequencyType =null;
    /**frequencyType이 once인 경우, 설정된 count 번째 event 인 경우 광고노출
     * frequencyType이 every인 경우, 설정된 횟수의 event 마다 광고노출출*/
    private int count = 0;
    /**frequencyType이 once인 경우, 설정된 duration이 되었을때 광고를 노출
     * ex) 00:10:00.000 서비스 이용시간이 10분이 되었을때 광고 노출
     * frequencyType이 every인 경우, 설정된 duration 마다 광고를 노출
     * ex) 00:10:00.000 서비스 이용시간 10분 마다 광고 노출*/
    private String duration = null;

    @Override
    public String toString() {
        return "AdInsertion{" +
                "event='" + event + '\'' +
                ", insertionType='" + insertionType + '\'' +
                ", frequencyType='" + frequencyType + '\'' +
                ", count=" + count +
                ", duration='" + duration + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    protected AdInsertion(Parcel in) {
        event = in.readString();
        insertionType = in.readString();
        frequencyType= in.readString();
        count = in.readInt();
        duration = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(event);
        dest.writeString(insertionType);
        dest.writeString(frequencyType);
        dest.writeInt(count);
        dest.writeString(duration);
    }

    @SuppressWarnings("unused")
    public static final Creator<AdInsertion> CREATOR = new Creator<AdInsertion>() {
        @Override
        public AdInsertion createFromParcel(Parcel in) {
            return new AdInsertion(in);
        }

        @Override
        public AdInsertion[] newArray(int size) {
            return new AdInsertion[size];
        }
    };
}
