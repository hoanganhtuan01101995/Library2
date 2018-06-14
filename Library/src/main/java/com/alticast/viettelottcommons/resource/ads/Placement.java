package com.alticast.viettelottcommons.resource.ads;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by user on 2016-05-17.
 */
public class Placement implements Parcelable {

    public static final String CLICK_THROUGH_TYPE_IMAGE = "image";
    public static final String TYPE_channel = "channel";
    public static final String TYPE_menu = "menu";
    public static final String TYPE_image = "image";
    public static final String TYPE_url = "url";
    public static final String TYPE_contentDetail = "contentDetail";
    public static final String
            CLICK_THROUGH_TYPE_URL = "url";

    public static final String Mobile_HOME = "HOME";

    public static final String Mobile_HOME_BOTTOM = "HOME.BOTTOM";
    public static final String Mobile_HOME_MID = "HOME.MID";
    public static final String Mobile_HOME_TOP = "HOME.TOP";

    public static final String Mobile_SUB_BOTTOM = "SUB.BOTTOM";
    public static final String Mobile_SUB_MID = "SUB.MID";
    public static final String Mobile_SUB_TOP = "SUB.TOP";

    public static final String Mobile_IFRAME = "IFRAME";

    /**
     * HH:mm:ss
     * 영상광고의 경우, 영상광고의 시간길이
     * 이미지 광고의 경우, 이미지를 노출해야할 시간길이,
     * 값이 없는 경우 이미지를 계속해서 노출
     */
    private String duration = null;

    public String getAdSpaceId() {
        return adSpaceId;
    }

    public void setAdSpaceId(String adSpaceId) {
        this.adSpaceId = adSpaceId;
    }

    private String adSpaceId = null;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description = null;

    /**
     * video광고의 경우 slot 번호, 연속되지 않을 수 있음.
     * 1부터 시작(0번 슬롯은 없음)
     */
    private int slotNumber = 0;
    /**
     * 광고 파일명 또는 광고 소재 url
     */
    private String adContentUrl = null;
    /**
     * 광고의 부가 url 정보(링크 정보)
     */
    private String checkThrough = null;
    private String clickThrough = null;
    private String clickThroughType = null;
    /**
     * adContentType가 image일 떄 광고 노출 위치
     * Left-Top
     * Left-Middle
     * Left-Bottom-p=ô0pihgfcx     Center-Top
     * Center-Bottom
     * Right-Top
     * Right-Middle
     * Right-Bottom
     * <p>
     * opportunityId 가 지정되어 있지 않은 이미지 광고의 경우, 필수
     */
    private String position = null;
    /**
     * 단위 : 초 해당 시간 이후 광고 skip 가능 -1인 경우, skip 불가
     */
    private int skipOffset = -1;
    /**
     * 광고 노출수 report를 위한 url
     */
    private String impression = null;
    /**
     * 광고 event tracking을 위한 url 정보
     */
    private Tracking[] tracking = null;
    /**
     * 광고 reward 요청을 위한 url 정보
     */
    private Reward reward = null;

    @Override
    public String toString() {
        return "Placement{" +
                "duration='" + duration + '\'' +
                ", slotNumber=" + slotNumber +
                ", adContentUrl='" + adContentUrl + '\'' +
                ", checkThrough='" + checkThrough + '\'' +
                ", position='" + position + '\'' +
                ", skipOffset=" + skipOffset +
                ", impression='" + impression + '\'' +
                ", trackings=" + Arrays.toString(tracking) +
                ", reward=" + reward +
                '}';
    }

    public String/*Uri*/ getAdContentUrl(String gldAddress, String requestId) {
//        Uri uri = new Uri.Builder()
//                .scheme("http")
//                .encodedAuthority(gldAddress)
//                .appendPath(adContentUrl)
//                .appendQueryParameter("VOD_RequestID", requestId)
//                .appendQueryParameter("AdaptiveType", "HLS")
//                .build();

//        Uri uri = new Uri.Builder()
//                .scheme(Api.PUBLIC_SCHME)
//                .encodedAuthority(gldAddress)
//                .appendEncodedPath(adContentUrl)
//                .appendQueryParameter("VOD_RequestID", requestId)//TODO check hardcoding
//                .appendQueryParameter("AdaptiveType", "HLS")//TODO check hardcoding
//                .build();
//
//        //String str="http://"+gldAddress.trim()+"/"+adContentUrl+"?VOD_RequestID="+requestId.trim()+"&AdaptiveType=HLS";
//        Logger.d("ad uri", uri.toString());
//        return uri.toString();
        return null;
    }

    public int getSkipOffset() {
        return skipOffset;
    }

    public String getAdContentUrl() {
        return adContentUrl;
    }

    public void setAdContentUrl(String adContentUrl) {
        this.adContentUrl = adContentUrl;
    }

    public String getClickThrough() {
        return clickThrough;
    }

    public String getClickThroughType() {
        return clickThroughType;
    }

    public int getDurationSecond() {
        int sec = 0;

        String[] times = duration.split(":");
        int x = 60;
        for (int j = times.length; j > 0; j--) {
            sec = Integer.parseInt(times[j]) * x;
            x = x * x;
        }

        return sec;
    }

    public String getImpression() {
        return impression;
    }

    public String getTrackingUrl(String trackingEvent) {
        if (tracking == null)
            return null;
        for (int i = 0; i < tracking.length; i++) {
            if (tracking[i].getEvent().toLowerCase().equals(trackingEvent))
                return tracking[i].getUrl();
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Placement(Parcel in) {
        duration = in.readString();
        slotNumber = in.readInt();
        adContentUrl = in.readString();
        checkThrough = in.readString();
        position = in.readString();
        skipOffset = in.readInt();
        impression = in.readString();
        Parcelable[] parcelables = in.readParcelableArray(Tracking.class.getClassLoader());
        tracking = Arrays.copyOf(parcelables, parcelables.length, Tracking[].class);
        reward = in.readParcelable(Reward.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(duration);
        dest.writeInt(slotNumber);
        dest.writeString(adContentUrl);
        dest.writeString(checkThrough);
        dest.writeString(position);
        dest.writeInt(skipOffset);
        dest.writeString(impression);
        dest.writeParcelableArray(tracking, 0);
        dest.writeParcelable(reward, 0);

    }

    @SuppressWarnings("unused")
    public static final Creator<Placement> CREATOR = new Creator<Placement>() {
        @Override
        public Placement createFromParcel(Parcel in) {
            return new Placement(in);
        }

        @Override
        public Placement[] newArray(int size) {
            return new Placement[size];
        }
    };
}
