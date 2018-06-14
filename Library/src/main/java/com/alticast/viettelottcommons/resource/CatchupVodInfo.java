package com.alticast.viettelottcommons.resource;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.loader.PlaybackLoader;
import com.alticast.viettelottcommons.manager.ChannelManager;
import com.alticast.viettelottcommons.util.DetailUtil;
import com.alticast.viettelottcommons.util.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;

public class CatchupVodInfo implements VodInfo {
    private static final String TAG = CatchupVodInfo.class.getSimpleName();

    private Schedule mSchedule;
    private String mCatchupId;
//    private String mEntryPath;

    public CatchupVodInfo(Schedule schedule) {
        Logger.d(TAG, "CatchupVodInfo schedule: " + schedule);
        mSchedule = schedule;

        ChannelProduct channel = ChannelManager.getInstance().getChannel(schedule.getChannel().getId());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        if (timeZone.getRawOffset() == 0) timeZone = TimeZone.getTimeZone("GMT+07");
        dateFormat.setTimeZone(timeZone);
        mCatchupId = dateFormat.format(new Date(mSchedule.getStart())) + "_" + channel.getService_id() + "_" + mSchedule.getPid();
    }

    public String getTitle() {
        return mSchedule.getTitle(WindmillConfiguration.LANGUAGE);

    }

    @Override
    public String getLogoUrl(Activity activity) {
        return DetailUtil.getPosterUrl(getSchedule().getChannel().getId(), DetailUtil.TYPE_LOGO);
    }

//    @Override
//    public String getEntryPath() {
//        return mEntryPath;
//    }
//
//    @Override
//    public String getMenuPathId() {
//        return "/catchup";
//    }

    @Override
    public String getLocator() {
        return mCatchupId + ".m3u8";
    }

    @Override
    public String getLocatorChromeCast() {
        return null;
    }

    @Override
    public void setData(Object object) {
        if(object instanceof Schedule){
            mSchedule = (Schedule) object;

            ChannelProduct channel = ChannelManager.getInstance().getChannel(mSchedule.getChannel().getId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            TimeZone timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
            if (timeZone.getRawOffset() == 0) timeZone = TimeZone.getTimeZone("GMT+07");
            dateFormat.setTimeZone(timeZone);
            mCatchupId = dateFormat.format(new Date(mSchedule.getStart())) + "_" + channel.getService_id() + "_" + mSchedule.getPid();
        } else {
            mSchedule = null;
            mCatchupId = null;
        }
    }

    @Override
    public Vod getVod() {
        return null;
    }

    @Override
    public Schedule getSchedule() {
        return mSchedule;
    }

    @Override
    public boolean isPurchased() {
        return mSchedule != null ? mSchedule.isPurchased() : false;
    }

    @Override
    public void setPurchase(boolean isPurchase) {
        if(mSchedule == null) return;
        mSchedule.setPurchased(isPurchase);
    }

    public String getId() {
        return mCatchupId;
    }

    public Schedule getmSchedule() {
        return mSchedule;
    }

    public void leaveCheckPoint(long startTime, int currentPosition, int duration) {
//        WatchApi api = ApiPool.get().getApi(WatchApi.class);
//        try {
//            api.pauseCatchup(StringConverter.class, mCatchupId, mSchedule.getTitle(), startTime, currentPosition, duration, mEntryPath);
//        } catch (Api.ApiException ignored) {
//        }
        PlaybackLoader.getInstance().changeCatchUpStatus(PlaybackLoader.PAUSE_CATCHUP, mCatchupId, mSchedule.getPurchaseId(), mSchedule, startTime, currentPosition, duration, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                Log.e("leaveCheckPoint", "onSuccess");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("leaveCheckPoint", "onFailure");
            }

            @Override
            public void onError(ApiError error) {
                Log.e("leaveCheckPoint", "onError");
            }
        });
    }

    public void removeCheckPoint(long startTime, int duration) {

        PlaybackLoader.getInstance().changeCatchUpStatus(PlaybackLoader.FINISH_CATCHUP, mCatchupId, mSchedule.getPurchaseId(), mSchedule, startTime, 0, duration, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                Log.e("removeCheckPoint", "onSuccess");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("removeCheckPoint", "onFailure");
            }

            @Override
            public void onError(ApiError error) {
                Log.e("removeCheckPoint", "onError");
            }
        });

//        WatchApi api = ApiPool.get().getApi(WatchApi.class);
//        try {
//            api.finishCatchup(StringConverter.class, mCatchupId, mSchedule.getTitle(), startTime, duration, mEntryPath);
//        } catch (Api.ApiException ignored) {
//        }
    }

    public void leaveLike(Context context) {
    }

    //NOT called function
    public void showInfo(Context context) {
    }

    protected CatchupVodInfo(Parcel in) {
        mCatchupId = in.readString();
        mSchedule = in.readParcelable(Schedule.class.getClassLoader());
//        mEntryPath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCatchupId);
        dest.writeParcelable(mSchedule, 0);
//        dest.writeString(mEntryPath);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CatchupVodInfo> CREATOR = new Parcelable.Creator<CatchupVodInfo>() {
        @Override
        public CatchupVodInfo createFromParcel(Parcel in) {
            return new CatchupVodInfo(in);
        }

        @Override
        public CatchupVodInfo[] newArray(int size) {
            return new CatchupVodInfo[size];
        }
    };
}
