package com.alticast.viettelottcommons.resource;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mc.kim on 7/28/2016.
 */
public class Series implements Parcelable {
    private String id = null;
    private String season = null;
    private String episode = null;
    private boolean episode_end = false;
    private MultiLingualText[] episode_titile = null;

    protected Series(Parcel in) {
        id = in.readString();
        season = in.readString();
        episode = in.readString();
        episode_end = in.readByte() != 0;
        episode_titile = in.createTypedArray(MultiLingualText.CREATOR);
    }

    public static final Creator<Series> CREATOR = new Creator<Series>() {
        @Override
        public Series createFromParcel(Parcel in) {
            return new Series(in);
        }

        @Override
        public Series[] newArray(int size) {
            return new Series[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getSeason() {
        return season;
    }

    public String getEpisode() {
        return episode;
    }

    public boolean isEpisode_end() {
        return episode_end;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public MultiLingualText[] getEpisode_titile() {
        return episode_titile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(season);
        dest.writeString(episode);
        dest.writeByte((byte) (episode_end ? 1 : 0));
        dest.writeTypedArray(episode_titile, flags);
    }
}
