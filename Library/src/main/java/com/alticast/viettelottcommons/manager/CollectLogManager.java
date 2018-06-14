package com.alticast.viettelottcommons.manager;

import java.util.LinkedList;
import java.util.List;

public class CollectLogManager {
    private static final String TAG = CollectLogManager.class.getSimpleName();
    public static final int MINIMUM_WATCH_TIME = 10000;

    private static CollectLogManager sSelf = new CollectLogManager();
    private List<WatchLog> mWatchLogs = new LinkedList<>();
    private WatchLog mStagedWatchLog;

    public static CollectLogManager get() {
        return sSelf;
    }

    private CollectLogManager() {
    }

    public void startWatch(String type, String contentId) {
        if (type.equals(WatchLog.TYPE_CHANNEL)) {
            if (mStagedWatchLog != null && mStagedWatchLog.type.equals(WatchLog.TYPE_CHANNEL)) {
                stopWatch();
            }
        }
        mStagedWatchLog = new WatchLog();
        mStagedWatchLog.type = type;
        mStagedWatchLog.contentId = contentId;
        mStagedWatchLog.accountId = HandheldAuthorization.getInstance().getCurrentId();
        mStagedWatchLog.startTime = TimeManager.getInstance().getServerCurrentTimeMillis();
    }

    public void saveWatchStallingTime(String contentId, long timeStalling, long totalTime) {

        if (timeStalling <= 0 || totalTime <= 0) return;

        WatchLog mStagedWatchLog = new WatchLog();
        mStagedWatchLog.type = WatchLog.TYPE_STALLING_TIME;
        mStagedWatchLog.contentId = contentId;
        mStagedWatchLog.accountId = HandheldAuthorization.getInstance().getCurrentId();
        mStagedWatchLog.stallingDuration = timeStalling;
        mStagedWatchLog.timeTotal = totalTime;

        mWatchLogs.add(mStagedWatchLog);
    }

    public void saveWatchStallingCount(String contentId, long countStalling, long totalTime) {

        if (countStalling <= 0 || totalTime <= 0) return;

        WatchLog mStagedWatchLog = new WatchLog();
        mStagedWatchLog.type = WatchLog.TYPE_STALLING_COUNT;
        mStagedWatchLog.contentId = contentId;
        mStagedWatchLog.accountId = HandheldAuthorization.getInstance().getCurrentId();
        mStagedWatchLog.countStalling = countStalling;
        mStagedWatchLog.timeTotal = totalTime;

        mWatchLogs.add(mStagedWatchLog);
    }

    public void stopWatch() {
        if (mStagedWatchLog != null) {
            mStagedWatchLog.accountId = HandheldAuthorization.getInstance().getCurrentId();
            mStagedWatchLog.endTime = TimeManager.getInstance().getServerCurrentTimeMillis();

            if (mStagedWatchLog.endTime < mStagedWatchLog.startTime) {
                mStagedWatchLog.endTime = mStagedWatchLog.startTime + 1;
            }

            mWatchLogs.add(mStagedWatchLog);
            mStagedWatchLog = null;
        }
    }

    public boolean isHasLog() {

        if (mWatchLogs == null || mWatchLogs.isEmpty()) {
            return false;
        }

        for (WatchLog log : mWatchLogs) {
            switch (log.type) {
                case WatchLog.TYPE_CHANNEL:
                case WatchLog.TYPE_STALLING_TIME:
                case WatchLog.TYPE_STALLING_COUNT:
                    return true;
                default:
                    if (log.endTime > 0) {
                        return true;
                    }
            }
        }

        return false;
    }

    public String flush() {

        if (mWatchLogs == null || mWatchLogs.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        WatchLog log = mWatchLogs.get(0);

        sb.append("type=").append(log.type).append(";");
        sb.append("id=").append(log.contentId != null ? log.contentId : "N/A").append(";");
        if (log.startTime > 0) {
            sb.append("lt=").append(log.startTime).append(";");
        }
        if (log.endTime > 0) {
            sb.append("ot=").append(log.endTime).append(";");
        }

        if (log.countStalling > 0) {
            sb.append("count_stalling=").append(log.countStalling).append(";");
        }

        if (log.stallingDuration > 0) {
            sb.append("time_stalling=").append(log.stallingDuration).append(";");
        }

        if (log.timeTotal > 0) {
            sb.append("time_total=").append(log.timeTotal).append(";");
        }


        sb.append("OTT=").append(log.accountId).append(";");
        sb.append("agents=").append("ViettelTV_OTT").append(";");

        if (mWatchLogs != null && !mWatchLogs.isEmpty()) {
            mWatchLogs.remove(0);
        }
        return sb.toString();

//        for (WatchLog log : mWatchLogs) {
//
//            sb.append("type=").append(log.type).append(";");
//            sb.append("Id=").append(log.contentId != null ? log.contentId : "N/A").append(";");
//            if(log.startTime > 0) {
//                sb.append("lt=").append(log.startTime).append(";");
//            }
//            if(log.endTime > 0) {
//                sb.append("ot=").append(log.endTime).append(";");
//            }
//
//            if(log.countStalling > 0) {
//                sb.append("Count_stalling=").append(log.countStalling).append(";");
//            }
//            if(log.timeTotal > 0) {
//                sb.append("Time_total=").append(log.timeTotal).append(";");
//            }
//            if(log.stallingDuration > 0) {
//                sb.append("Time_stalling=").append(log.stallingDuration).append(";");
//            }
//
//            sb.append("OTT=").append(log.accountId).append(";");
//        }
//          mWatchLogs.clear();
    }

}
