package com.alticast.viettelottcommons.resource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.loader.PlaybackLoader;
import com.alticast.viettelottcommons.util.DetailUtil;
import com.alticast.viettelottcommons.util.Logger;

import retrofit2.Call;

public class ProgramVodInfo implements VodInfo {
    private static final String TAG = ProgramVodInfo.class.getSimpleName();

    //    private Program mProgram;
//    private Product mProduct;
    private Vod vod;
//    private Path mPath;

//    public ProgramVodInfo(Program program, Product product) {
//        mProgram = program;
//        mProduct = product;
////        mPath = path;
//    }

    public ProgramVodInfo(Vod vod) {
        this.vod = vod;
    }

    public Vod getVod() {
        return vod;
    }

    @Override
    public Schedule getSchedule() {
        return null;
    }

    @Override
    public String getId() {
        return vod.getId();
    }

    @Override
    public boolean isPurchased() {
        return vod != null ? vod.isPurchased() : false;
    }

    @Override
    public void setPurchase(boolean isPurchase) {
        if (vod == null) return;
        vod.setPurchased(isPurchase);
    }

    public void setVod(Vod vod) {
        this.vod = vod;
    }

    public String getTitle() {
//        return mProgram.getTitle(WindmillConfiguration.LANGUAGE);
        return vod.getProgram().getTitle(WindmillConfiguration.LANGUAGE);
    }

    @Override
    public String getLogoUrl(Activity activity) {
        return DetailUtil.getPosterUrl(vod.getProgram().getId(), DetailUtil.TYPE_POSTER,
                (int) activity.getResources().getDimension(R.dimen.bookmark_image_width),
                (int) activity.getResources().getDimension(R.dimen.bookmark_image_height));
    }


//    @Override
//    public String getEntryPath() {
//        return mPath.getEntry();
//    }
//
//    @Override
//    public String getMenuPathId() {
//        return EntryPathLogImpl.getInstance().getWatchMenuId();
//    }

    @Override
    public String getLocator() {
//        return mProduct.getLocator();
        return vod.getSingleProduct().getLocator();
    }

    @Override
    public String getLocatorChromeCast() {
        return vod.getSingleProduct().getLocatorChromeCast();
    }

    @Override
    public void setData(Object object) {
        if (object instanceof Vod) {
            vod = (Vod) object;
        } else {
            vod = null;
        }
    }

    public void leaveCheckPoint(long startTime, int currentPosition, int duration) {
//        WatchApi api = ApiPool.get().getApi(WatchApi.class);
//        try {
//            api.pauseVod(StringConverter.class, mProgram, mProduct, startTime, currentPosition, duration, getEntryPath(), getMenuPathId());
//        } catch (Api.ApiException ignored) {
//        }

        PlaybackLoader.getInstance().changeVodStatus(PlaybackLoader.PAUSE_VOD, this.getProgram(), this.getProduct(), vod.getPurchaseId(), startTime, currentPosition, duration, new WindmillCallback() {
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
//        WatchApi api = ApiPool.get().getApi(WatchApi.class);
//        try {
//            api.finishVod(StringConverter.class, mProgram.getId(), mProduct.getId(), mProduct.getPid(), mProgram.getTitle(WindmillConfiguration.LANGUAGE), startTime, duration, getEntryPath(), getMenuPathId());
//        } catch (Api.ApiException ignored) {
//        }

        PlaybackLoader.getInstance().changeVodStatus(PlaybackLoader.FINISH_VOD, this.getProgram(), vod.getSingleProduct(), vod.getPurchaseId(), startTime, 0, duration, new WindmillCallback() {
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
    }

    public void leaveLike(final Context context) {
//        ProgramApi api = ApiPool.get().getApi(ProgramApi.class);
//        try {
//            api.like(StringConverter.class, mProgram.getId()).convert();
//            mProgram.setLiked(true);
//        } catch (Api.ApiException ignored) {
//        }

        PlaybackLoader.getInstance().likeVod(vod.getProgram().getId(), new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                vod.getProgram().setLiked();
//                Intent intent = new Intent(VodDetailActivity.REFRESH_DATA);
//                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }

            @Override
            public void onError(ApiError error) {

            }
        });
    }

    public boolean isLiked() {
        return vod.getProgram().isLiked();
    }

    public Program getProgram() {
        return vod.getProgram();
    }

    public Product getProduct() {
        return vod.getSingleProduct();
    }

    protected ProgramVodInfo(Parcel in) {
//        mProgram = in.readParcelable(Program.class.getClassLoader());
//        mProduct = in.readParcelable(Product.class.getClassLoader());
//        mPath = in.readParcelable(Path.class.getClassLoader());

        vod = in.readParcelable(Vod.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        if (mProgram != null)
//            dest.writeParcelable(mProgram, 0);
//        if (mProduct != null)
//            dest.writeParcelable(mProduct, 0);
        if (vod != null)
            dest.writeParcelable(vod, 0);
//        if (mPath != null)
//            dest.writeParcelable(mPath, 0);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProgramVodInfo> CREATOR = new Parcelable.Creator<ProgramVodInfo>() {
        @Override
        public ProgramVodInfo createFromParcel(Parcel in) {
            return new ProgramVodInfo(in);
        }

        @Override
        public ProgramVodInfo[] newArray(int size) {
            return new ProgramVodInfo[size];
        }
    };
}
