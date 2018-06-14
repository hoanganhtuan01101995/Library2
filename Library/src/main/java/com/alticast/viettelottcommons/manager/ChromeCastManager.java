package com.alticast.viettelottcommons.manager;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.loader.ChannelLoader;
import com.alticast.viettelottcommons.loader.PlaybackLoader;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.ProgramVodInfo;
import com.alticast.viettelottcommons.resource.Schedule;
import com.alticast.viettelottcommons.resource.Vod;
import com.alticast.viettelottcommons.resource.response.ScheduleListRes;
import com.alticast.viettelottcommons.util.Logger;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;

public class ChromeCastManager {

    public static final int SEEK_ALLOWABLE = 2 * 60 * 60 * 1000;

    private static ChromeCastManager ourInstance = new ChromeCastManager();
    public static int TYPE_VOD_CAST = 10;
    public static int TYPE_VOD_SERIES_CAST = 13;
    public static int TYPE_CHANNEL_CAST = 11;
    public static int NONE_TYPE = 12;
    public static int TYPE_CATCHUP_CAST = 14;
    public static String CHROME_CAST_VOLUME = "CHROME_CAST_VOLUME";

    public static String CHROME_MEDIA_TYPE = "CHROME_MEDIA_TYPE";
    public static String CHROME_MEDIA_ID = "CHROME_MEDIA_ID";
    public static String CHROME_MEDIA_START = "CHROME_MEDIA_START";
    public static String CHROME_MEDIA_END = "CHROME_MEDIA_END";
    public static String CAST_INFO_DIRECTOR = "CAST_INFO_DIRECTOR";
    public static String CAST_INFO_STAR = "CAST_INFO_STAR";
    public static String CAST_INFO_LIKES = "CAST_INFO_LIKES";
    public static String CAST_INFO_PRODUCTION_YEAR = "CAST_INFO_PRODUCTION_YEAR";
    public static String CAST_INFO_GENRES = "CAST_INFO_GENRES";
    public static String CAST_INFO_HOT = "CAST_INFO_HOT";

    public static String CHROME_SERIES_NEXT_PLAY = "CHROME_SERIES_NEXT_PLAY";

    public static String RELOAD_CONTROL = "RELOAD_CONTROL";

    public static final String CAST_VOD = "CAST_VOD";
    public static final String CAST_VOD_TITLE = "CAST_VOD_TITLE";
    public static final String CAST_VOD_DURATION = "CAST_VOD_DURATION";
    public static final String CAST_VOD_PROGRAM_ID = "CAST_VOD_PROGRAM_ID";
    public static final String CAST_VOD_SERIES_NUMBER = "CAST_VOD_SERIES_NUMBER";

    public static final String CAST_CATCHUP = "CAST_CATCHUP";
    public static final String CAST_CATCHUP_ID = "CAST_CATCHUP_ID";
    public static final String CAST_CATCHUP_TITLE = "CAST_CATCHUP_TITLE";
    public static final String CAST_CATCHUP_START = "CAST_CATCHUP_START";
    public static final String CAST_CATCHUP_END = "CAST_CATCHUP_END";

    public static final String CAST_VOD_SERIES = "CAST_VOD_SERIES";

    public static final String CAST_CHANNEL = "CAST_CHANNEL";
    public static final String CAST_CHANNEL_ID = "CAST_CHANNEL_ID";
    public static final String CAST_CHANNEL_IS_TIMESHIFT = "CAST_CHANNEL_IS_TIMESHIFT";

    public static final String CAST_CHANNEL_RELOAD = "CAST_CHANNEL_RELOAD";
    public static final String CAST_DISCONNECT = "CAST_DISCONNECT";
    public static final String CAST_CONNECT = "CAST_CONNECT";

    public static final String CAST_CHANGE_METADATA = "CAST_CHANGE_METADATA";
    public static final String SHOW_CHECK_POINT = "SHOW_CHECK_POINT";
    public static final String ACTION_PROGRESS_REMOTE = "ACTION_PROGRESS_REMOTE";

    public static final String CAST_TIMESHIFT = "CAST_TIMESHIFT";

    public static final String CHANGE_PLAY_PAUSE = "CHANGE_PLAY_PAUSE";

    public static final String VOD_DETAIL_POPUP = "VOD_DETAIL_POPUP";

    public static final String ACTION_MEDIA_CHROME_FINISH = "ACTION_MEDIA_CHROME_FINISH";

    public static final String CAST_CONTROL_VISIBLE = "CAST_CONTROL_VISIBLE";
    public static final String CAST_CONTROL_GONE = "CAST_CONTROL_GONE";

    public static final String SHOW_CAST_SERIES_EXIT = "SHOW_CAST_SERIES_EXIT";

    public static final String CHROME_CAST_VOLUME_CHANGE = "CHROME_CAST_VOLUME_CHANGE";
    public static final String CHROME_NONE_CAST_DEVICE = "CHROME_NONE_CAST_DEVICE";

    public static final String CHROME_RELOAD_CHANNEL = "CHROME_RELOAD_CHANNEL";

    public static final int CHROME_PROGRESS_NEGATIVE = -1;

    public static final int CAST_SERIES_PROGRESS_NEXT = -1;

    public static final int CAST_SERIES_NORMAL_VOD = 0;
    public static final int CAST_SERIES_USE_THIS_VOD = 1;
    public static final int CAST_SERIES_LAST_SEEN_VOD = 2;

    public static ChromeCastManager getInstance() {
        return ourInstance;
    }

    private ChromeCastManager() {
    }

    public boolean isChromeCastState() {
        return mChromeCastState;
    }

    public void setChromeCastState(boolean mChromeCastState) {
        this.mChromeCastState = mChromeCastState;
    }

    private boolean mChromeCastState;

    public Vod getCastVod() {
        return mCastVod;
    }

    public void setCastVod(Vod mCastVod, long progress, boolean isPlayListMode) {
        ChromeCastManager.getInstance().setPlayListMode(isPlayListMode);
        setCastType(TYPE_VOD_CAST);
        mVodProgress = progress;
        this.mCastVod = mCastVod;
    }

    public void setCastVod(Vod mCastVod, long progress) {
        resetAll();
        ChromeCastManager.getInstance().setPlayListMode(false);
        setCastType(TYPE_VOD_CAST);
        mVodProgress = progress;
        this.mCastVod = mCastVod;
    }

    public Schedule getCatchUpSchedule() {
        return mCatchUpSchedule;
    }

    public void getAutoNextCatchUpSchedule(Schedule currentSchedule, WindmillCallback callback) {
        if (currentSchedule == null) return;
        String channelId = currentSchedule.getChannel().getId();
        if (channelId == null) return;
        ChannelManager.getInstance().getChannelSchedule(channelId, callback);
    }

    public void setCatchUpSchedule(Schedule mCatchUpSchedule, long mCatchUpProgress) {
        resetAll();
        this.mCatchUpSchedule = mCatchUpSchedule;
        this.mCatchUpProgress = mCatchUpProgress;
        setCastType(TYPE_CATCHUP_CAST);
    }

    private Schedule mCatchUpSchedule;

    public long getCatchUpProgress() {
        return mCatchUpProgress;
    }

    public void setCatchUpProgress(long mCatchUpProgress) {
        this.mCatchUpProgress = mCatchUpProgress;
    }

    private long mCatchUpProgress;

    private Vod mCastVod;

    public long getVodProgress() {
        return mVodProgress;
    }

    public void setVodProgress(long mVodProgress) {
        this.mVodProgress = mVodProgress;
    }

    private long mVodProgress; // millisecond

    public Schedule getCastChannel() {
        return mCastChannel;
    }

    public void setCastChannel(Schedule mCastChannel) {
        resetAll();
        setCastType(TYPE_CHANNEL_CAST);
        this.mCastChannel = mCastChannel;
        this.timeShiftProgress = 0;
    }

    public void setCastChannel(Schedule mCastChannel, long timeShiftProgress) {
        resetAll();
        setCastType(TYPE_CHANNEL_CAST);
        this.mCastChannel = mCastChannel;
        this.timeShiftProgress = timeShiftProgress;
    }


    public long getTimeShiftProgress() {
        return timeShiftProgress;
    }

    private long timeShiftProgress = 0;

    private Schedule mCastChannel;

    public int getCastType() {
        return castType;
    }

    public void setCastType(int castType) {
        this.castType = castType;
        if (castType == NONE_TYPE) {
            resetAll();
        }
    }

    private void resetAll() {
        mCastChannel = null;
        mCastVod = null;
        mCatchUpSchedule = null;
        mNextSeriesProgramInfo = null;
        isPlayListMode = false;
        timeShiftProgress = 0;
    }

    private int castType = NONE_TYPE;


    public int getCurrentProgress() {
        return mCurrentProgress;
    }

    public void setCurrentProgress(int mCurrentProgress) {
        this.mCurrentProgress = mCurrentProgress;
    }

    private int mCurrentProgress;

    public long getPauseTime() {
        return mPauseTime;
    }

    public void setPauseTime(long mPauseTime) {
        this.mPauseTime = mPauseTime;
    }

    private long mPauseTime;


    public long getTotalPauseTime() {
        return mTotalPauseTime;
    }

    public void setTotalPauseTime(long mTotalPauseTime) {
        this.mTotalPauseTime = mTotalPauseTime;
    }

    private long mTotalPauseTime;

    public long getLiveStartTime() {
        return liveStartTime;
    }

    public void setLiveStartTime(long liveStartTime) {
        this.liveStartTime = liveStartTime;
    }

    public boolean isTimeShift() {
        return isTimeShift;
    }

    public long getTimeDistance() {
        return timeDistance;
    }

    public void setTimeDistance(long timeDistance) {
        this.timeDistance = timeDistance;
    }

    private long timeDistance;

    public void setTimeShift(boolean timeShift) {
        if (!timeShift) {
            setTotalPauseTime(0);
            setPauseTime(0);
        }
        isTimeShift = timeShift;
    }

    private boolean isTimeShift;

    private long liveStartTime;

    public boolean isCastFromPlayBack() {
        return isCastFromPlayBack;
    }

    public void setCastFromPlayBack(boolean castFromPlayBack) {
        isCastFromPlayBack = castFromPlayBack;
    }

    private boolean isCastFromPlayBack;

    public double getVolumeChromeCast() {
        return volumeChromeCast;
    }

    public void setVolumeChromeCast(double volumeChromeCast) {
        this.volumeChromeCast = volumeChromeCast;
    }

    private double volumeChromeCast;

    public ArrayList<Schedule> getSchedulesLive() {
        return mSchedules;
    }

    public void setSchedulesLive(ArrayList<Schedule> mSchedules) {
        this.mSchedules = mSchedules;
    }


    private ArrayList<Schedule> mSchedules = new ArrayList<>();

    public int getCurrentScheduleIndex() {
        return mCurrentScheduleIndex;
    }

    public void setCurrentScheduleIndex(int mCurrentScheduleIndex) {
        this.mCurrentScheduleIndex = mCurrentScheduleIndex;
    }

    private int mCurrentScheduleIndex;

    public void loadSchedule(final String channelId, final WindmillCallback callback) {

        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.DAY_OF_YEAR, -4);
        long start = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, 10);
        long end = cal.getTimeInMillis();

        ChannelLoader.getInstance().getScheduleList(channelId, start, end, 10000, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                ArrayList<Schedule> schedules = ((ScheduleListRes) obj).getData();
                calculateScheduleIndex(schedules, callback);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }

            @Override
            public void onError(ApiError error) {
            }
        });
    }

    public void loadCatchUpSchedule(final String channelId, final WindmillCallback callback, final double startSchedule, final double endSchedule) {

        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.DAY_OF_YEAR, -4);
        long start = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, 10);
        long end = cal.getTimeInMillis();

        ChannelLoader.getInstance().getScheduleList(channelId, start, end, 10000, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                ArrayList<Schedule> schedules = ((ScheduleListRes) obj).getData();
                calculateCatchUpScheduleIndex(schedules, callback, startSchedule, endSchedule);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }

            @Override
            public void onError(ApiError error) {
            }
        });
    }

    public void calculateScheduleIndex(ArrayList<Schedule> schedules, WindmillCallback callback) {
        if (schedules == null) {
            return;
        }
        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            long now = TimeManager.getInstance().getServerCurrentTimeMillis();
            if (schedule.getStart() <= now && now <= schedule.getEnd()) {
                callback.onSuccess(schedule);
                break;
            }
        }
    }

    public void calculateCatchUpScheduleIndex(ArrayList<Schedule> schedules, WindmillCallback callback, double start, double end) {
        if (schedules == null) {
            return;
        }
        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            long now = TimeManager.getInstance().getServerCurrentTimeMillis();
            if (schedule.getStart() == start && end == schedule.getEnd()) {
                callback.onSuccess(schedule);
                break;
            }
        }
    }

    public boolean isCastingContent() {
        return isCastingContent;
    }

    public void setCastingContent(boolean castingContent) {
        isCastingContent = castingContent;
    }

    // check when content need to cast continue
    private boolean isCastingContent;

    public String getCastedDevice() {
        return castedDevice;
    }

    public void setCastedDevice(String castedDevice) {
        this.castedDevice = castedDevice;
    }

    private String castedDevice;


    public void checkEndMedia(WindmillCallback callback) {
        if (ChromeCastManager.getInstance().getCastType() == ChromeCastManager.TYPE_VOD_CAST) {
            Vod vod = ChromeCastManager.getInstance().getCastVod();
            if (vod != null) {
                if (vod.isSeries()) {
                    Logger.print("Tag", "checkEndMedia vod.isSeries");
                    findNextepisode(ChromeCastManager.getInstance().getCastVod().getProgram().getId(), callback);
                } else {
                    callback.onError(null);
                }
            }
        } else if (ChromeCastManager.getInstance().getCastType() == ChromeCastManager.TYPE_CATCHUP_CAST) {
            callback.onError(null);
        }
    }

    public ProgramVodInfo getNextSeriesProgramInfo() {
        return mNextSeriesProgramInfo;
    }

    public void setNextSeriesProgramInfo(ProgramVodInfo mNextSeriesProgramInfo) {
        this.mNextSeriesProgramInfo = mNextSeriesProgramInfo;
    }

    private ProgramVodInfo mNextSeriesProgramInfo;

    private void findNextepisode(String vodProgramInfo, final WindmillCallback callback) {
        PlaybackLoader.getInstance().findNextEpisode(vodProgramInfo, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                Logger.print("Tag", "checkEndMedia findNextepisode onSuccess" +
                        ((Vod) obj).getProgram().getTitle(WindmillConfiguration.LANGUAGE));
                setNextSeriesProgramInfo(new ProgramVodInfo((Vod) obj));
                callback.onSuccess(obj);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                callback.onFailure(call, t);
            }

            @Override
            public void onError(ApiError error) {
                callback.onError(null);
            }
        });
    }

    public boolean isPlayListMode() {
        return isPlayListMode;
    }

    public void setPlayListMode(boolean playListMode) {
        isPlayListMode = playListMode;
    }

    private boolean isPlayListMode;

    public int getCheckPoint() {
        return mCheckPoint;
    }

    public void setCheckPoint(int mCheckPoint) {
        this.mCheckPoint = mCheckPoint;
    }

    private int mCheckPoint;


}
