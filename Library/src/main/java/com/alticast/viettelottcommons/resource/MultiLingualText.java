package com.alticast.viettelottcommons.resource;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by mc.kim on 7/28/2016.
 */
public class MultiLingualText implements Parcelable{
    private String text = null;
    private String lang = null;

    public String getText() {
        return text;
    }

    public String getLang() {
        return lang;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(lang);
    }

    @SuppressWarnings("unused")
    public static final Creator<MultiLingualText> CREATOR = new Creator<MultiLingualText>() {
        @Override
        public MultiLingualText createFromParcel(Parcel in) {
            return new MultiLingualText(in);
        }

        @Override
        public MultiLingualText[] newArray(int size) {
            return new MultiLingualText[size];
        }
    };

    protected MultiLingualText(Parcel in) {
        this.text = in.readString();
        this.lang = in.readString();

    }

    public MultiLingualText(String lang, String text) {
        this.text = text;
        this.lang = lang;
    }
}
