package com.alticast.viettelottcommons.resource.ads;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2016-05-17.
 */
public class Tracking implements Parcelable {

    public static final String EVENT_MIDPOINT = "midpoint";
    public static final String EVENT_SKIP = "skip";
    public static final String EVENT_CLOSE = "close";
    public static final String EVENT_CLICK = "click";

    /**가능한 event 목록
     start,  //광고노출 시작
     firstQuartile, // 광고 25% 시청시
     midpoint,   // 광고 50% 시청시
     thirdQuartile,  //광고 75% 시청시
     complete,   //광고 100% 시청시
     skip,   //광고가 skip되었을 때
     close,  //광고를 보다가 나가기를 한 경우
     pause, //광고를 pause 한경우
     resume, //광고를 이어보기 시작한 경우
     click  //광고를 클릭(자세히보기 등)한 경우
     */
    private String event = null;
    /**event에 정의된 상황인 경우, url 호출*/
    private String url = null;


    public String getEvent(){ return event;}
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Tracking{" +
                "event='" + event + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Tracking(Parcel in) {
        event = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(event);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Creator<Tracking> CREATOR = new Creator<Tracking>() {
        @Override
        public Tracking createFromParcel(Parcel in) {
            return new Tracking(in);
        }

        @Override
        public Tracking[] newArray(int size) {
            return new Tracking[size];
        }
    };

}
