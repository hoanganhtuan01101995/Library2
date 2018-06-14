package com.alticast.viettelottcommons.resource;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Reservation implements Parcelable {

    private String channelId;
    private String eventId;
    private int serviceId;
    private int rating;
    private ArrayList<MultiLingualText> title = null;
    private String resolution;
    private long startTime;
    private long endTime;


    public Reservation(String channelId, String eventId, int serviceId, int rating, ArrayList<MultiLingualText> title, String resolution, long startTime, long endTime) {
        this.channelId = channelId;
        this.eventId = eventId;
        this.serviceId = serviceId;
        this.rating = rating;
        this.title = title;
        this.resolution = resolution;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    protected Reservation(Parcel in) {
        channelId = in.readString();
        eventId = in.readString();
        serviceId = in.readInt();
        rating = in.readInt();
        Parcelable[] parcelables = in.readParcelableArray(MultiLingualText.class.getClassLoader());
        title = new ArrayList<>();
        Collections.addAll(title, Arrays.copyOf(parcelables, parcelables.length, MultiLingualText[].class));

        resolution = in.readString();
        startTime = in.readLong();
        endTime = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(channelId);
        dest.writeString(eventId);
        dest.writeInt(serviceId);
        dest.writeInt(rating);
        if (title == null)
            title = new ArrayList<>();
        dest.writeParcelableArray(title.toArray(new MultiLingualText[title.size()]), 0);

        dest.writeString(resolution);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
    }

    @SuppressWarnings("unused")
    public static final Creator<Reservation> CREATOR = new Creator<Reservation>() {
        @Override
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        @Override
        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };


    public String getChannelId() {
        return channelId;
    }

    public String getEventId() {
        return eventId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public int getRating() {
        return rating;
    }

    public ArrayList<MultiLingualText> getTitles() {
        return title;
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


    public String getResolution() {
        return resolution;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public JSONArray getTitle() throws JSONException {
        JSONArray jList = new JSONArray();

        for (int i = 0; i < title.size(); i++) {
            JSONObject jLang = new JSONObject();
            jLang.put("lang", title.get(i).getLang());
            jLang.put("text", title.get(i).getText());
            jList.put(jLang);
        }
        return jList;
    }


    public static ArrayList<MultiLingualText> getTitles(JSONObject jItem) throws JSONException {
        ArrayList<MultiLingualText> title = new ArrayList<>();
        JSONArray jName = jItem.getJSONArray("title");
        int length = jName.length();
        for (int i = 0; i < length; i++) {
            JSONObject name = jName.getJSONObject(i);
            String lang = name.getString("lang");
            String text = name.getString("text");
            title.add(new MultiLingualText(lang, text));
        }
        return title;
    }
}
