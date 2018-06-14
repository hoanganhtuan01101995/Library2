package com.alticast.viettelottcommons.resource;

import android.os.Parcel;
import android.os.Parcelable;

import com.alticast.viettelottcommons.WindmillConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by mc.kim on 8/10/2016.
 */
public class Channel implements Parcelable {

    private String id = null;
    private int pid = 0;

    private ArrayList<MultiLingualText> name = null;
    private String created_time = null;
    private String updated_time = null;
    private ArrayList<String> genres = null;
    private ArrayList<CustomValue> config = null;

    private String channelName;


    public Channel(String id) {
        this.id = id;
    }

    private boolean isFavorite;
    private int type;
    private String serviceId;

    public String getId() {
        return id;
    }

    public int getPid() {
        return pid;
    }

    public String getName(String lang) {
        if(channelName != null && channelName.length() > 0) {
            return channelName;
        }

        if(name == null || name.isEmpty()) {
            channelName = "N/A";
        }
        int size = name.size();

        for (int i = 0; i < size; i++) {
            MultiLingualText multiLingualText = name.get(i);
            if(multiLingualText.getLang().equals(WindmillConfiguration.LANGUAGE)){
                channelName = multiLingualText.getText();
                break;
            }
        }
        channelName = name.get(0).getText();

        return channelName;
    }

    public String getChannelName() {
        if(channelName != null && channelName.length() > 0) {
            return channelName;
        }

        if(name == null || name.isEmpty()) {
            channelName = "";
            return channelName;
        }
        int size = name.size();

        for (int i = 0; i < size; i++) {
            MultiLingualText multiLingualText = name.get(i);
            if(multiLingualText.getLang().equals(WindmillConfiguration.LANGUAGE)){
                channelName = multiLingualText.getText();
                break;
            }
        }
        channelName = name.get(0).getText();

        return channelName;
    }

    public String getCreated_time() {
        return created_time;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public ArrayList<CustomValue> getConfig() {
        return config;
    }

    public boolean getCatchUpState(){
        int size = config.size();

        for (int i = 0; i < size; i++) {
            if (config.get(i).getName().equalsIgnoreCase("IsCatchUp")) {
                return config.get(i).getValue().equalsIgnoreCase("1");
            }
        }
        return false;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public boolean isVTVCabChannel() {

        if(!WindmillConfiguration.isAlacaterVersion) {
            return false;
        }

        if(genres == null || genres.isEmpty()) {
            return false;
        }

        for(String genre : genres) {
            if(genre.equals("1:11:0:0")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(pid);
        dest.writeString(getChannelName());
        if (name == null)
            name = new ArrayList<>();
        dest.writeParcelableArray(name.toArray(new MultiLingualText[name.size()]), 0);
        if (genres == null)
            genres = new ArrayList<>();
        dest.writeStringList(genres);
//        dest.writeParcelableArray(genres.toArray(new String[genres.size()]), 0);
    }

    @SuppressWarnings("unused")
    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel in) {
            return new Channel(in);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };

    protected Channel(Parcel in) {
        this.id = in.readString();
        this.pid = in.readInt();
        this.channelName = in.readString();
        Parcelable[] parcelablesName = in.readParcelableArray(MultiLingualText.class.getClassLoader());
        name = new ArrayList<>();
        Collections.addAll(name, Arrays.copyOf(parcelablesName, parcelablesName.length, MultiLingualText[].class));

        Parcelable[] parcelablesGenres = in.readParcelableArray(String.class.getClassLoader());
        genres = new ArrayList<>();
        Collections.addAll(genres, Arrays.copyOf(parcelablesGenres, parcelablesGenres.length, String[].class));

    }
}
