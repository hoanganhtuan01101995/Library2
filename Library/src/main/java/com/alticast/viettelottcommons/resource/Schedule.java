package com.alticast.viettelottcommons.resource;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.alticast.viettelottcommons.util.TimeUtil;
import com.alticast.viettelottcommons.util.WindDataConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class Schedule implements Parcelable {
    private String id = null;
    private String pid = null;
    private String start_time = null;
    private String end_time = null;
    private String purchaseId;

    private int type;
    private String header = null;
    public static int ITEM = 1;
    public static int SECTION = 0;

    private ArrayList<MultiLingualText> title = null;
    private ArrayList<MultiLingualText> synopsis = null;
    private ArrayList<MultiLingualText> directors_text = null;
    private ArrayList<MultiLingualText> actors_text = null;
    private ArrayList<Config> custom = null;

    private boolean isPurchased;

    public Schedule() {
    }

    public Schedule(String _header) {
        header = _header;
    }

    private Channel channel = null;

    public String getId() {
        return id;
    }

    public String getPid() {
        return pid;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public ArrayList<MultiLingualText> getTitle() {
        return title;
    }

    public ArrayList<MultiLingualText> getSynopsis() {
        return synopsis;
    }

    public ArrayList<MultiLingualText> getDirectors_text() {
        return directors_text;
    }

    public ArrayList<MultiLingualText> getActors_text() {
        return actors_text;
    }

    public ArrayList<Config> getCustom() {
        return custom;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getTitle(String language) {

        if (title == null) {
            return "No tittle";
        }

        String nameStr = null;
        if (title != null && title.size() > 0) {
            if (language != null) {
                for (int i = 0; i < title.size(); i++) {
                    if (language.equals(title.get(i).getLang())) {
                        nameStr = title.get(i).getText();
                    }
                }
            }
            if (nameStr == null) {
                nameStr = title.get(0).getText();
            }
        }
        return nameStr;
    }

    public void setType(int _type) {
        type = _type;
    }

    public int getType() {
        return type;
    }

    public String getHeader() {
        return header;
    }

    public long getStart() {
        return TimeUtil.getLongTime(start_time, WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);
    }

    public long getEnd() {
        return TimeUtil.getLongTime(end_time, WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    public void setEndTime(long endTime) {
        this.end_time = TimeUtil.getTime(endTime, WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);
    }
    public void setStartTime(long startTime) {
        this.start_time = TimeUtil.getTime(startTime, WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setTitle(ArrayList<MultiLingualText> title) {
        this.title = title;
    }

    public boolean isHd() {
        if (custom == null)
            return false;
        int size = custom.size();

        for (int i = 0; i < size; i++) {
            if (custom.get(i).getName().equalsIgnoreCase("VideoType")) {
                return custom.get(i).getValue().equalsIgnoreCase("HD");
            }
        }
        return false;
    }

    private int rating = 0;

    public int getRating() {
        return rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(isPurchased ? 1 : 0);
        dest.writeString(purchaseId);
        dest.writeString(start_time);
        dest.writeString(end_time);
        dest.writeInt(type);
        if (title == null)
            title = new ArrayList<>();
        dest.writeParcelableArray(title.toArray(new MultiLingualText[title.size()]), 0);
        dest.writeParcelable(channel, 0);
    }

    @SuppressWarnings("unused")
    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    protected Schedule(Parcel in) {
        this.id = in.readString();
        this.isPurchased = in.readInt() == 1;
        this.purchaseId = in.readString();
        this.start_time = in.readString();
        this.end_time = in.readString();
        this.type = in.readInt();
        Parcelable[] parcelables = in.readParcelableArray(MultiLingualText.class.getClassLoader());
        title = new ArrayList<>();
        Collections.addAll(title, Arrays.copyOf(parcelables, parcelables.length, MultiLingualText[].class));
        this.channel = in.readParcelable(Channel.class.getClassLoader());
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setSynopsis(ArrayList<MultiLingualText> synopsis) {
        this.synopsis = synopsis;
    }

    public void setDirectors_text(ArrayList<MultiLingualText> directors_text) {
        this.directors_text = directors_text;
    }

    public void setActors_text(ArrayList<MultiLingualText> actors_text) {
        this.actors_text = actors_text;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

}
