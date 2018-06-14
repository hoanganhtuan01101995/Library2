package com.alticast.viettelottcommons.manager;

/**
 * Created by duyuno on 6/22/17.
 */
public class WatchLog {
    public static final String TYPE_CHANNEL = "channel";
    public static final String TYPE_VOD = "VOD";
    public static final String TYPE_MENU = "MENU";
    public static final String TYPE_STALLING_TIME = "Stalling_time";
    public static final String TYPE_STALLING_COUNT = "Stalling_count";


    public String type;
    public String contentId;
    public String accountId;
    public long startTime;
    public long endTime;
    public long stallingDuration;
    public long countStalling;
    public long timeTotal;
}
