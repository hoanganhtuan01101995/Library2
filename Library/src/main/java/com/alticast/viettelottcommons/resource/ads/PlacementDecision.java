package com.alticast.viettelottcommons.resource.ads;

import android.os.Parcel;
import android.os.Parcelable;

import com.alticast.viettelottcommons.WindmillConfiguration;

import java.util.Arrays;

/**
 * Created by user on 2016-05-17.
 */
public class PlacementDecision implements Parcelable{

    public static final String TYPE_PREROLL = "PRE-ROLL";
    public static final String TYPE_POSTROLL = "POST-ROLL";
    public static final String TYPE_MIDROLL = "MID-ROLL";
    public static final String TYPE_TIMER_EVENT = "TIMER-EVENT";
    public static final String TYPE_PLAYER_EVENT = "PLAYER-EVENT";

    public static final String PLAY_CONTROL_TYPE = "PLAY_CONTROL_TYPE";
    public static final String TYPE_POLICY_EACH = "EACH";
    public static final String TYPE_POLICY_ALL = "ALL";

    public static final String CONTENT_TYPE_VIDEO = "VIDEO";
    public static final String CONTENT_TYPE_IMAGE = "IMAGE";

    private String id = null;
    /***
     * 광고가 나갈수 있는 기회의 종류
     * Pre/Mid/Post roll
     * Play-Event , Trick-Play
     * EPG-AD, Home, Menu-Nav
     */
    private String opportunityType = null;
    /***
     * Image 광고의 경우 이미지 자리 id
     */
    private String opportunityId = null;
    /**광고 컨텐츠 타입 VIDEO, IMAGE*/
    private String adContentType = null;
    /**0: 가능한 control 없음.
     * 1: FF가능
     * 2: RW가능
     * 4: PAUSE 가능
     * 8: 미세배속 가능
     */
    private int enablePlayControl =0;
    /**광고 노출 시간 지점*/
    private String insertionOffset = null;
    /**opportunityType 이 Play-Event인 경우의 광고 삽입 정보,*/
    private String mainContentResumingOffset = null;

    private AdInsertion adInsertion = null;
    private Result result = null;
    /***/
    private Placement [] placement = null;

    private String skipPolicy;

    private MidRollEvent midRollEvent;
    private TimerEvent timerEvent;
    private PlayerEvent playerEvent;

    private boolean isMidrollPlayed;

    public String getImageBannerUrl() {
        if(placement == null || placement.length == 0) return null;

        return placement[0].getAdContentUrl();
    }

    public String getImpression() {
        if(placement == null || placement.length == 0) return null;

        return placement[0].getImpression();
    }

    public String getTrackingUrl(String event) {
        if(placement == null || placement.length == 0) return null;

        for(Placement pl : placement) {
            String url = pl.getTrackingUrl(event);

            if(url != null) return url;
        }

        return null;
    }

    public String getImageBannerImpression() {
        if(placement == null || placement.length == 0) return null;

        return placement[0].getImpression();
    }

    public String getClickThroughtUrl() {
        if(placement == null || placement.length == 0) return null;
        if(WindmillConfiguration.isFakeTimerEvent) {
            return placement[0].getAdContentUrl();
        }
//        return placement[0].getAdContentUrl();
        return placement[0].getClickThrough();
    }

    public String getClickThroughType() {
        if(placement == null || placement.length == 0) return null;

        if(WindmillConfiguration.isFakeTimerEvent) {
            return Placement.CLICK_THROUGH_TYPE_IMAGE;
        }
        return placement[0].getClickThroughType();
    }

    @Override
    public String toString() {
        return "PlacementDecision{" +
                "id='" + id + '\'' +
                ", opportunityType='" + opportunityType + '\'' +
                ", opportunityId='" + opportunityId + '\'' +
                ", adContentType='" + adContentType + '\'' +
                ", enablePlayControl=" + enablePlayControl +
                ", skipPolicy='" + skipPolicy + '\'' +
                ", insertionOffset='" + insertionOffset + '\'' +
                ", mainContentResumingOffset='" + mainContentResumingOffset + '\'' +
                ", adInsertion=" + adInsertion +
                ", result=" + result +
                ", placement=" + Arrays.toString(placement) +
                ", midRollEvent=" + midRollEvent +
                '}';
    }

    public boolean isMidrollPlayed() {
        return isMidrollPlayed;
    }

    public void setMidrollPlayed(boolean midrollPlayed) {
        isMidrollPlayed = midrollPlayed;
    }

    public TimerEvent getTimerEvent() {
        return timerEvent;
    }

    public void setTimerEvent(TimerEvent timerEvent) {
        this.timerEvent = timerEvent;
    }

    public PlayerEvent getPlayerEvent() {
        return playerEvent;
    }

    public void setPlayerEvent(PlayerEvent playerEvent) {
        this.playerEvent = playerEvent;
    }

    public boolean checkMidRollPlayable(int midrollTriggerTime) {
        if(isMidrollPlayed) return  false;


        if(midRollEvent != null) {
            if(midRollEvent.ignoredIfTrickplay) {
                return midrollTriggerTime == midRollEvent.getTimeShowAds();
            } else {
                return midrollTriggerTime >= midRollEvent.getTimeShowAds();
            }
        }

        return false;
    }

    public String getId() {
        return id;
    }

    public String getOpportunityType() {
        return opportunityType;
    }

    public void setOpportunityType(String opportunityType) {
        this.opportunityType = opportunityType;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getAdContentType() {
        return adContentType;
    }

    public void setAdContentType(String adContentType) {
        this.adContentType = adContentType;
    }

    public boolean getEnablePlayControl() {
        return (enablePlayControl & 4) == 4;
    }

    public void setEnablePlayControl(int enablePlayControl) {
        this.enablePlayControl = enablePlayControl;
    }

    public String getInsertionOffset() {
        return insertionOffset;
    }

    public void setInsertionOffset(String insertionOffset) {
        this.insertionOffset = insertionOffset;
    }

    public String getMainContentResumingOffset() {
        return mainContentResumingOffset;
    }

    public void setMainContentResumingOffset(String mainContentResumingOffset) {
        this.mainContentResumingOffset = mainContentResumingOffset;
    }

    public AdInsertion getAdInsertion() {
        return adInsertion;
    }

    public void setAdInsertion(AdInsertion adInsertion) {
        this.adInsertion = adInsertion;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Placement[] getPlacement() {
        return placement;
    }

    public void setPlacement(Placement[] placement) {
        this.placement = placement;
    }

    public String getSkipPolicy() {
        return skipPolicy;
    }

    public MidRollEvent getMidRollEvent() {
        return midRollEvent;
    }

    public void setMidRollEvent(MidRollEvent midRollEvent) {
        this.midRollEvent = midRollEvent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected PlacementDecision(Parcel in) {
        opportunityType = in.readString();
        opportunityId = in.readString();
        adContentType = in.readString();
        enablePlayControl = in.readInt();
        skipPolicy = in.readString();
        insertionOffset = in.readString();
        mainContentResumingOffset = in.readString();
        adInsertion = in.readParcelable(AdInsertion.class.getClassLoader());
        result = in.readParcelable(Result.class.getClassLoader());

        Parcelable[] parcelables = in.readParcelableArray(Placement.class.getClassLoader());
        placement = Arrays.copyOf(parcelables, parcelables.length, Placement [].class);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(opportunityType);
        dest.writeString(opportunityId);
        dest.writeString(adContentType);
        dest.writeInt(enablePlayControl);
        dest.writeString(skipPolicy);
        dest.writeString(insertionOffset);
        dest.writeString(mainContentResumingOffset);

        dest.writeParcelable(adInsertion, 0);
        dest.writeParcelable(result, 0);
        dest.writeParcelableArray(placement, 0);

    }

    @SuppressWarnings("unused")
    public static final Creator<PlacementDecision> CREATOR = new Creator<PlacementDecision>() {
        @Override
        public PlacementDecision createFromParcel(Parcel in) {
            return new PlacementDecision(in);
        }

        @Override
        public PlacementDecision[] newArray(int size) {
            return new PlacementDecision[size];
        }
    };
}