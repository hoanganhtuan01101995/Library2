package com.alticast.viettelottcommons.resource.ads;

import java.text.SimpleDateFormat;

/**
 * Created by duyuno on 7/7/17.
 */
public class PlayerEvent {

    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

    public String event;
    public String insertionType;
    public String frequencyType;
    public  int count;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getInsertionType() {
        return insertionType;
    }

    public void setInsertionType(String insertionType) {
        this.insertionType = insertionType;
    }

    public String getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
