package com.alticast.viettelottcommons.resource;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class Mail implements Parcelable {

    public Mail() {

    }
    private String id = null;

    private String thread_id = null;

    private String license_start = null;

    private String license_end = null;

    private String from = null;

    private String type = null;
    private int priority = 0;

    private ArrayList<MultiLingualText> name = null;

    private ArrayList<MultiLingualText> description = null;

    private Config config = null;

    private ArrayList<Target> target = null;

    private Action action = null;

    private String image = null;

    private String callback = null;

    private String updated_time = null;


    public String getId() {
        return id;
    }

    public String getThread_id() {
        return thread_id;
    }

    public String getLicense_start() {
        return license_start;
    }

    public String getLicense_end() {
        return license_end;
    }

    public String getFrom() {
        return from;
    }

    public String getType() {
        return type;
    }

    public ArrayList<MultiLingualText> getName() {
        return name;
    }



    public Mail(Parcel in) {
        id = in.readString();
        thread_id = in.readString();
        license_start = in.readString();
        license_end = in.readString();
        from = in.readString();
        type = in.readString();
        priority = in.readInt();
        name = in.createTypedArrayList(MultiLingualText.CREATOR);
        description = in.createTypedArrayList(MultiLingualText.CREATOR);
        image = in.readString();
        callback = in.readString();
        updated_time = in.readString();
    }

    public static final Creator<Mail> CREATOR = new Creator<Mail>() {
        @Override
        public Mail createFromParcel(Parcel in) {
            return new Mail(in);
        }

        @Override
        public Mail[] newArray(int size) {
            return new Mail[size];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public void setLicense_start(String license_start) {
        this.license_start = license_start;
    }

    public void setName(ArrayList<MultiLingualText> name) {
        this.name = name;
    }

    public void setDescription(ArrayList<MultiLingualText> description) {
        this.description = description;
    }

    public String getName(String type) {
        int size = name.size();

        for (int i = 0; i < size; i++) {
            if (name.get(i).getLang().equalsIgnoreCase(type)) {
                return name.get(i).getText();
            }
        }


        return name.get(0).getText();
    }

    public ArrayList<MultiLingualText> getDescription() {
        return description;
    }

    public String getDescription(String type) {
        int size = description.size();

        for (int i = 0; i < size; i++) {
            if (description.get(i).getLang().equalsIgnoreCase(type)) {
                return description.get(i).getText();
            }
        }


        return description.get(0).getText();
    }


    public Config getConfig() {
        return config;
    }


    public Action getAction() {
        return action;
    }

    public String getImage() {
        return image;
    }

    public String getCallback() {
        return callback;
    }

    public String getUpdated_time() {
        return updated_time;
    }


//    public String getActionType() {
//        return target.getType();
//    }
//
//    public String getActionId() {
//        return target.getId();
//    }

    public ArrayList<Target> getTarget() {
        return target;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(thread_id);
        dest.writeString(license_start);
        dest.writeString(license_end);
        dest.writeString(from);
        dest.writeString(type);
        dest.writeInt(priority);
        dest.writeTypedList(name);
        dest.writeTypedList(description);
        dest.writeString(image);
        dest.writeString(callback);
        dest.writeString(updated_time);
    }
}
