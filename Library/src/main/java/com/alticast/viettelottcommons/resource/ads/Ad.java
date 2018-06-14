package com.alticast.viettelottcommons.resource.ads;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by user on 2016-05-13.
 */
public class Ad implements Parcelable {

    public static final long TIME_MAX_TIMESHIFT_ADS = 2 * 60 * 60;

    private String messageIdRef = null;
    private Result result = null;
    private PlacementDecision[] placementDecision = null;
    private PlacementDecision playerEventDecision;
    private ArrayList<PlacementDecision> imageTimerEventDecisions;
    private ArrayList<PlacementDecision> clipTimerEventDecisions;

    private boolean isShowImageAds;
    private boolean isShowClipAds;

    private int curentImageTimerEventIdx;
    private int curentClipTimerEventIdx;
    private PlacementDecision currentImageTimerEvent;
    private PlacementDecision currentClipTimerEvent;

//    private ArrayList<Integer> arrayMidRollClipTriggers;
//    private ArrayList<Integer> arrayMidRollImageTriggers;

    private HashMap<Integer, PlacementDecision> mapMidrollClip;
    private HashMap<Integer, PlacementDecision> mapMidrollImage;

    public void initTriggers() {

        mapMidrollClip = new HashMap<>();
        mapMidrollImage = new HashMap<>();

//        arrayMidRollClipTriggers = new ArrayList<>();
//        arrayMidRollImageTriggers = new ArrayList<>();
        for (PlacementDecision placementDeci : placementDecision) {
            String opportunityType = placementDeci.getOpportunityType();
            Placement[] placements = placementDeci.getPlacement();
            if (opportunityType != null && placements != null && placements.length > 0) {
                if (opportunityType.toUpperCase().equals(PlacementDecision.TYPE_MIDROLL)) {

                    if (placementDeci.getMidRollEvent() != null) {
                        if(placementDeci.getAdContentType().toUpperCase().equals(PlacementDecision.CONTENT_TYPE_VIDEO)) {
                            mapMidrollClip.put(placementDeci.getMidRollEvent().getTimeShowAds(), placementDeci);
//                            arrayMidRollClipTriggers.add(Integer.valueOf(placementDeci.getMidRollEvent().getTimeShowAds()));
                        } else {
                            Log.e("midroll-image", "" + placementDeci.getMidRollEvent().getTimeShowAds());
                            mapMidrollImage.put(placementDeci.getMidRollEvent().getTimeShowAds(), placementDeci);
//                            arrayMidRollImageTriggers.add(Integer.valueOf(placementDeci.getMidRollEvent().getTimeShowAds()));
                        }
                    }
                } else if(opportunityType.toUpperCase().equals(PlacementDecision.TYPE_TIMER_EVENT)) {
                    if(placementDeci.getTimerEvent() != null) {
                        if(placementDeci.getAdContentType().toUpperCase().equals(PlacementDecision.CONTENT_TYPE_IMAGE)) {
                            if(imageTimerEventDecisions == null) {
                                imageTimerEventDecisions = new ArrayList<>();
                            }
                            imageTimerEventDecisions.add(placementDeci);
                        } else {
                            if(clipTimerEventDecisions == null) {
                                clipTimerEventDecisions = new ArrayList<>();
                            }
                            clipTimerEventDecisions.add(placementDeci);
                        }

                    }
                } else if(opportunityType.toUpperCase().equals(PlacementDecision.TYPE_PLAYER_EVENT)) {
                    if(placementDeci.getPlayerEvent() != null) {
                        playerEventDecision = placementDeci;
                    }
                }
            }
        }

        if(imageTimerEventDecisions != null && !imageTimerEventDecisions.isEmpty()) {
            curentImageTimerEventIdx = 0;
            currentImageTimerEvent = imageTimerEventDecisions.get(0);

            Log.e("uno", "imageTimerEventDecisions size" + imageTimerEventDecisions.size());
        }
        if(clipTimerEventDecisions != null && !clipTimerEventDecisions.isEmpty()) {
            curentClipTimerEventIdx = 0;
            currentClipTimerEvent = clipTimerEventDecisions.get(0);

            Log.e("uno", "clipTimerEventDecisions size" + clipTimerEventDecisions.size());
        }
    }

    public void nextImageTimerEvent() {

        if(imageTimerEventDecisions == null || imageTimerEventDecisions.isEmpty()) {
            return;
        }

        if(imageTimerEventDecisions.size() == 1) {
            return;
        }

        if(curentImageTimerEventIdx == imageTimerEventDecisions.size() - 1) {
            curentImageTimerEventIdx = 0;
        } else {
            curentImageTimerEventIdx++;
        }
        currentImageTimerEvent = imageTimerEventDecisions.get(curentImageTimerEventIdx);
//        Log.e("uno", "nextImageTimerEvent " + curentImageTimerEventIdx + " " + currentImageTimerEvent.getImageBannerUrl());
    }

    public PlacementDecision getCurrentImageTimerEvent() {
        return currentImageTimerEvent;
    }
    public void nextClipTimerEvent() {

        if(clipTimerEventDecisions == null || clipTimerEventDecisions.isEmpty()) {
            return;
        }

        if(clipTimerEventDecisions.size() == 1) {
            return;
        }

        if(curentClipTimerEventIdx == clipTimerEventDecisions.size() - 1) {
            curentClipTimerEventIdx = 0;
        } else {
            curentClipTimerEventIdx++;
        }
        currentClipTimerEvent = clipTimerEventDecisions.get(curentClipTimerEventIdx);
//        Log.e("uno", "nextImageTimerEvent " + curentImageTimerEventIdx + " " + currentImageTimerEvent.getImageBannerUrl());
    }

    public PlacementDecision getCurrentClipTimerEvent() {
        return currentClipTimerEvent;
    }

    public boolean isShowImageAds() {
        return isShowImageAds;
    }

    public void setShowImageAds(boolean showImageAds) {
        isShowImageAds = showImageAds;
    }

    public boolean isShowClipAds() {
        return isShowClipAds;
    }

    public void setShowClipAds(boolean showClipAds) {
        isShowClipAds = showClipAds;
    }

    public boolean checkClipTrigger(int position) {
//        for (Integer integer : arrayMidRollClipTriggers) {
//            if (integer.intValue() * 1000 == position) return true;
//        }
        PlacementDecision placementDecision = mapMidrollClip.get(position / 1000);
        if(placementDecision == null) return false;

        return !placementDecision.isMidrollPlayed();
    }
    public boolean checkImageTrigger(int position) {
        return mapMidrollImage.containsKey(position / 1000);
//        for (Integer integer : arrayMidRollImageTriggers) {
//            if (integer.intValue() * 1000 == position) return true;
//        }
//
//        return false;
    }

    public PlacementDecision getMidrollClip(int position) {
        return mapMidrollClip != null ? mapMidrollClip.get(position / 1000) : null;
    }

    public PlacementDecision getMidrollImage(int position) {
//        Log.e("getMidrollImage", "" + position);
        return mapMidrollImage != null ? mapMidrollImage.get(position / 1000) : null;
    }

    public Ad(String messageIdRef, Result result, PlacementDecision[] placementDecision) {
        this.messageIdRef = messageIdRef;
        this.result = result;
        this.placementDecision = placementDecision;
    }

    public PlacementDecision getPlacements(String adAction, String adContentType, int timeMidRollEvent) {
        PlacementDecision[] placementDecisions = getPlacementDecision();

        if (placementDecisions == null || placementDecisions.length == 0) {
            return null;
        }
        switch (adContentType) {
            case PlacementDecision.CONTENT_TYPE_VIDEO:


                for (PlacementDecision placementDecision : placementDecisions) {
                    Placement[] placements = placementDecision.getPlacement();
                    if (placements == null || placements.length == 0) {
                        continue;
                    }

                    String opportunityType = placementDecision.getOpportunityType();
                    if (opportunityType != null && opportunityType.toUpperCase().equals(adAction)
                            && placementDecision.getAdContentType().toUpperCase().equals(adContentType)) {

                        if (adAction.equals(PlacementDecision.TYPE_MIDROLL)) {
                            if (placementDecision.getMidRollEvent() != null && placementDecision.checkMidRollPlayable(timeMidRollEvent)) {
                                placementDecision.setMidrollPlayed(true);
                                return placementDecision;
                            }
                        } else {
                            return placementDecision;
                        }
                    }
                }
                break;
            case PlacementDecision.CONTENT_TYPE_IMAGE:
                for (PlacementDecision placementDecision : placementDecisions) {
                    Placement[] placements = placementDecision.getPlacement();
                    if (placements == null || placements.length == 0) {
                        continue;
                    }

                    String opportunityType = placementDecision.getOpportunityType();
                    if (opportunityType != null && opportunityType.toUpperCase().equals(adAction)
                            && placementDecision.getAdContentType().toUpperCase().equals(adContentType)
                            ) {

                        if (adAction.equals(PlacementDecision.TYPE_TIMER_EVENT)) {
                            if (placementDecision.getTimerEvent() != null) {
                                return placementDecision;
                            }
                        } else if(adAction.equals(PlacementDecision.TYPE_PLAYER_EVENT)){
                            if (placementDecision.getPlayerEvent() != null) {
                                return placementDecision;
                            }
                        }
                    }
                }
                break;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Ad{" +
                "messageIdRef='" + messageIdRef + '\'' +
                ", result=" + result +
                ", placementDecision=" + Arrays.toString(placementDecision) +
                '}';
    }

    public PlacementDecision[] getPlacementDecision() {
        return placementDecision;
    }

    protected Ad(Parcel in) {
        messageIdRef = in.readString();
        result = in.readParcelable(Result.class.getClassLoader());
        Parcelable[] parcelables = in.readParcelableArray(PlacementDecision.class.getClassLoader());
        placementDecision = Arrays.copyOf(parcelables, parcelables.length, PlacementDecision[].class);
    }


    public Result getResult() {
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(messageIdRef);
        dest.writeParcelable(result, 0);
        dest.writeParcelableArray(placementDecision, 0);
    }

    @SuppressWarnings("unused")
    public static final Creator<Ad> CREATOR = new Creator<Ad>() {
        @Override
        public Ad createFromParcel(Parcel in) {
            return new Ad(in);
        }

        @Override
        public Ad[] newArray(int size) {
            return new Ad[size];
        }
    };
}
