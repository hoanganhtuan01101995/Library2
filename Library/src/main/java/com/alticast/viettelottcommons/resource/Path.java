package com.alticast.viettelottcommons.resource;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.regex.Pattern;

/**
 * Created by mc.kim on 8/10/2016.
 */
public class Path implements Parcelable {
    private String host = null;
    private String entry = null;
    private String pathId = null;
    private String clickLogForRecommendation = null;

    public Path(String host, String entry, String pathId, String clickLogForRecommendation) {
        this.host = host;
        this.entry = entry;
        this.pathId = pathId;
        this.clickLogForRecommendation = clickLogForRecommendation;
    }
    public Path(String host, String entry, String clickLogForRecommendation) {
        this.host = host;
        this.entry = entry;
        this.clickLogForRecommendation = clickLogForRecommendation;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public void setClickLogForRecommendation(String clickLogForRecommendation) {
        this.clickLogForRecommendation = clickLogForRecommendation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(host);
        dest.writeString(entry);
        dest.writeString(pathId);
        dest.writeString(clickLogForRecommendation);
    }

    public Path(Parcel in) {
        host = in.readString();
        entry = in.readString();
        pathId = in.readString();
        clickLogForRecommendation = in.readString();

    }

    public static final Creator<Path> CREATOR = new Creator<Path>() {
        @Override
        public Path createFromParcel(Parcel in) {
            return new Path(in);
        }

        @Override
        public Path[] newArray(int size) {
            return new Path[0];
        }

    };

    public String getHost() {
        return host;
    }

    public String getClickLogForRecommendation() {
        return clickLogForRecommendation;
    }

    public String getEntry() {
        return entry;
    }

    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }
}
