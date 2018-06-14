package com.alticast.viettelottcommons.resource.ads;

import com.alticast.viettelottcommons.WindmillConfiguration;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * Created by duyuno on 7/7/17.
 */
public class TimerEvent {

    public static final String FREQUENCE_ONCE = "once";
    public static final String FREQUENCE_EVERY = "every";

    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

    public String frequencyType; // once, every
    public String frequencyTerm;
    public String frequencyStartTime;
    public String exposureDuration;

    private int frequencyTermTime = 0;
    private int exposureDurationTime = 0;
    private int frequencyStart = 0;
    private int countLimit = 0;

//    private boolean isFake = false;

    public int getFrequencyTermTime() {
        if(WindmillConfiguration.isFakeTimerEvent) {
            return 10;
        }


        if(frequencyTerm == null || frequencyTerm.isEmpty()) {
            return -1;
        }

        if(frequencyTermTime > 0) {
            return frequencyTermTime;
        }

        String timeStr[] = frequencyTerm.split(Pattern.quote(":"));

        if(timeStr.length == 3) {
            frequencyTermTime += Integer.parseInt(timeStr[2]) + Integer.parseInt(timeStr[1]) * 60 + Integer.parseInt(timeStr[0]) * 60 * 60;
        } else if (timeStr.length == 2) {
            frequencyTermTime += Integer.parseInt(timeStr[1]) + Integer.parseInt(timeStr[0]) * 60;
        } else if (timeStr.length == 1) {
            frequencyTermTime += Integer.parseInt(timeStr[0]);
        } else {
            return -1;
        }

        return frequencyTermTime;
    }

    public int getCountLimit() {
        return countLimit;
    }

    public int getFrequencyStart() {
        if(WindmillConfiguration.isFakeTimerEvent) {
            return 10;
        }


        if(frequencyStartTime == null || frequencyStartTime.isEmpty()) {
            return -1;
        }

        if(frequencyStart > 0) {
            return frequencyStart;
        }

        String timeStr[] = frequencyStartTime.split(Pattern.quote(":"));

        if(timeStr.length == 3) {
            frequencyStart += Integer.parseInt(timeStr[2]) + Integer.parseInt(timeStr[1]) * 60 + Integer.parseInt(timeStr[0]) * 60 * 60;
        } else if (timeStr.length == 2) {
            frequencyStart += Integer.parseInt(timeStr[1]) + Integer.parseInt(timeStr[0]) * 60;
        } else if (timeStr.length == 1) {
            frequencyStart += Integer.parseInt(timeStr[0]);
        } else {
            return 0;
        }

        return frequencyStart;
    }

    public int getExposureDurationTime() {

        if(WindmillConfiguration.isFakeTimerEvent) {
            return 5;
        }

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

    public String getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
    }

    public String getFrequencyTerm() {
        return frequencyTerm;
    }

    public void setFrequencyTerm(String frequencyTerm) {
        this.frequencyTerm = frequencyTerm;
    }

    public String getExposureDuration() {
        return exposureDuration;
    }

    public void setExposureDuration(String exposureDuration) {
        this.exposureDuration = exposureDuration;
    }
}
