//package com.alticast.viettelottcommons.manager;
//
//import java.util.LinkedList;
//import java.util.List;
//
//public class ChannelLogManager {
//    private static final String TAG = ChannelLogManager.class.getSimpleName();
//    public static final int MINIMUM_WATCH_TIME = 10000;
//
//    private static ChannelLogManager sSelf = new ChannelLogManager();
//    private List<WatchLog> mWatchLogs = new LinkedList<>();
//    private WatchLog mStagedWatchLog;
//
//    public static ChannelLogManager get() {
//        return sSelf;
//    }
//
//    private ChannelLogManager() {
//    }
//
//    public void startWatch(String channelId) {
//        stopWatch();
//        mStagedWatchLog = new WatchLog();
//        mStagedWatchLog.channelId = channelId;
//        mStagedWatchLog.startTime = TimeManager.getInstance().getServerCurrentTimeMillis();
//    }
//
//    public void stopWatch() {
//        if (mStagedWatchLog != null) {
//            mStagedWatchLog.endTime = TimeManager.getInstance().getServerCurrentTimeMillis();
//            if (mStagedWatchLog.endTime > mStagedWatchLog.startTime) {
//                mWatchLogs.add(mStagedWatchLog);
//            }
////            if (mStagedWatchLog.endTime >= mStagedWatchLog.startTime + MINIMUM_WATCH_TIME) {
////                mWatchLogs.add(mStagedWatchLog);
////            }
//            mStagedWatchLog = null;
//        }
//    }
//
//    public String flush() {
//        StringBuilder sb = new StringBuilder();
//        String chId = null;
//        for (WatchLog log : mWatchLogs) {
//            if(chId == null) {
//                chId = log.channelId;
//            }
//            sb.append("id=").append(log.channelId).append(";");
//            sb.append("lt=").append(log.startTime).append(";");
//            sb.append("ot=").append(log.endTime).append(";");
//        }
//        mWatchLogs.clear();
//        if (sb.length() > 0) {
//            sb.insert(0, "type=channel;");
////            if(chId != null) {
////                startWatch(chId);
////            }
//            return sb.toString();
//        } else {
//            return null;
//        }
//
//    }
//
//
//    public class WatchLog {
//        String channelId;
//        long startTime;
//        long endTime;
//    }
//
//
//}
