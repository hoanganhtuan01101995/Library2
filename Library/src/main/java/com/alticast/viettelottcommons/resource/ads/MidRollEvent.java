package com.alticast.viettelottcommons.resource.ads;

import com.alticast.viettelottcommons.WindmillConfiguration;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * Created by duyuno on 6/5/17.
 */
public class MidRollEvent {

    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

    public String insertionOffset;
    public String mainContentResumingOffset;
    public String exposureDuration;
    public boolean ignoredIfTrickplay;
    private int timeShowAds = -1;
    private int exposureDurationTime = 0;

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

    public int getExposureDurationTime() {

//        if(WindmillConfiguration.isFakeTimerEvent) {
//            return 5;
//        }

        if(exposureDuration == null || exposureDuration.isEmpty()) {
            return -1;
        }

        if(exposureDurationTime > 0) {
            return exposureDurationTime;
        }

        String timeStr[] = exposureDuration.split(Pattern.quote(":"));

        if(timeStr.length == 3) {
            exposureDurationTime += Integer.parseInt(timeStr[2]) + Integer.parseInt(timeStr[1]) * 60 + Integer.parseInt(timeStr[0]) * 60 * 60;
        } else if (timeStr.length == 2) {
            exposureDurationTime += Integer.parseInt(timeStr[1]) + Integer.parseInt(timeStr[0]) * 60;
        } else if (timeStr.length == 1) {
            exposureDurationTime += Integer.parseInt(timeStr[0]);
        } else {
            return -1;
        }

        return exposureDurationTime;
    }

    public boolean isIgnoredIfTrickplay() {
        return ignoredIfTrickplay;
    }

    public void setIgnoredIfTrickplay(boolean ignoredIfTrickplay) {
        this.ignoredIfTrickplay = ignoredIfTrickplay;
    }

    public int getTimeShowAds() {
        if(insertionOffset == null || insertionOffset.isEmpty()) {
            return -1;
        }

        if(timeShowAds > 0) {
            return timeShowAds;
        }

        String timeStr[] = insertionOffset.split(Pattern.quote(":"));

        if(timeStr.length == 3) {
            timeShowAds += Integer.parseInt(timeStr[2]) + Integer.parseInt(timeStr[1]) * 60 + Integer.parseInt(timeStr[0]) * 60 * 60;
        } else if (timeStr.length == 2) {
            timeShowAds += Integer.parseInt(timeStr[1]) + Integer.parseInt(timeStr[0]) * 60;
        } else if (timeStr.length == 1) {
            timeShowAds += Integer.parseInt(timeStr[0]);
        } else {
            return -1;
        }

        return timeShowAds;
    }

    @Override
    public String toString() {
        return "MidRollEvent {" +
                "insertionOffset=" + insertionOffset +
                "mainContentResumingOffset=" + mainContentResumingOffset +
                "ignoredIfTrickplay=" + ignoredIfTrickplay +
                "}";
    }
}
