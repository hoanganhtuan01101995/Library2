package com.alticast.viettelottcommons.resource;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by mc.kim on 7/28/2016.
 */
public class Program implements Parcelable {
    private static HashSet<String> sLikedSet = new HashSet<>();
    private String id = null;
    private String pid = null;
    private String showtype = null;
    private MultiLingualText[] title = null;
    private MultiLingualText[] synopsis = null;
    private String production_country = null;
    private String production_date = null;
    private MultiLingualText[] directors_text = null;
    private MultiLingualText[] actors_text = null;
    private String[] genres = null;
    private String posterUrl = null;

    public ArrayList<String> getImage() {
        return images;
    }

    public void setImage(ArrayList<String> images) {
        this.images = images;
    }

    private ArrayList<String> images = null;

    private String link = null;

    private Provider provider = null;

    private CountClass likes = null;

    private String created_time = null;
    private String updated_time = null;

    private String[] icons = null; // "new", "hot"
    //
    private Series series = null;
    //
    private String audience = null;

    private String display_runtime = null;
    //
    private CountClass mails = null;

    private Review reviews = null;

    // public directors;
    // public actors;
    // public reviews;
    // public photos;
    // public services;
    // public shops;

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getTitle(String language) {
        String nameStr = null;
        if (title != null && title.length > 0) {
            if (language != null) {
                for (int i = 0; i < title.length; i++) {
                    if (language.equals(title[i].getLang())) {
                        nameStr = title[i].getText();
                    }
                }
            }
            if (nameStr == null) {
                nameStr = title[0].getText();
            }
        }
        return nameStr;
    }

    public String getSynopsis(String lang) {
        String synopStr = null;
        if (synopsis != null && synopsis.length > 0) {
            if (lang != null) {
                for (int i = 0; i < synopsis.length; i++) {
                    if (lang.equals(synopsis[i].getLang())) {
                        synopStr = synopsis[i].getText();
                    }
                }
            }
            if (synopStr == null) {
                synopStr = synopsis[0].getText();
            }
        }
        return synopStr;
    }

    public String getDirectors(String lang) {
        String synopStr = null;
        if (directors_text != null && directors_text.length > 0) {
            if (lang != null) {
                for (int i = 0; i < directors_text.length; i++) {
                    if (lang.equals(directors_text[i].getLang())) {
                        synopStr = directors_text[i].getText();
                    }
                }
            }
            if (synopStr == null) {
                synopStr = directors_text[0].getText();
            }
        }
        return synopStr;
    }

    public String getActors(String lang) {
        String synopStr = null;
        if (actors_text != null && actors_text.length > 0) {
            if (lang != null) {
                for (int i = 0; i < actors_text.length; i++) {
                    if (lang.equals(actors_text[i].getLang())) {
                        synopStr = actors_text[i].getText();
                    }
                }
            }
            if (synopStr == null) {
                synopStr = actors_text[0].getText();
            }
        }
        return synopStr;
    }


    public Review getReviews() {
        return reviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public Program() {

    }

    protected Program(Parcel in) {
        id = in.readString();
        Parcelable[] parcelables = in.readParcelableArray(MultiLingualText.class.getClassLoader());
        title = Arrays.copyOf(parcelables, parcelables.length, MultiLingualText[].class);
        series = in.readParcelable(Series.class.getClassLoader());
        likes = in.readParcelable(CountClass.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelableArray(title, 0);
        dest.writeParcelable(series, 0);
        if (likes != null)
            dest.writeParcelable(likes, 0);
    }

    @SuppressWarnings("unused")
    public static final Creator<Program> CREATOR = new Creator<Program>() {
        @Override
        public Program createFromParcel(Parcel in) {
            return new Program(in);
        }

        @Override
        public Program[] newArray(int size) {
            return new Program[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getPid() {
        return pid;
    }

    public String getShowtype() {
        return showtype;
    }

    public void setTitle(MultiLingualText[] title) {
        this.title = title;
    }

    public MultiLingualText[] getTitle() {
        return title;
    }

    public MultiLingualText[] getSynopsis() {
        return synopsis;
    }

    public String getProduction_country() {
        return production_country;
    }

    public String getProduction_date() {
        return production_date;
    }

    public MultiLingualText[] getDirectors_text() {
        return directors_text;
    }

    public MultiLingualText[] getActors_text() {
        return actors_text;
    }

    public String[] getGenres() {
        return genres;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getLink() {
        return link;
    }

    public Provider getProvider() {
        return provider;
    }

    public CountClass getLikes() {
        return likes;
    }

    public String getCreated_time() {
        return created_time;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public String[] getIcons() {
        return icons;
    }

    public Series getSeries() {
        return series;
    }

    public int getType() {
        if (icons == null) return 0;
        for (String icon : icons) {
            if (icon.equalsIgnoreCase("new")) {
                return 2;
            } else if (icon.equalsIgnoreCase("hot")) {
                return 3;
            }
        }
        return 0;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAudience() {
        return audience;
    }

    public void setDisplay_runtime(String display_runtime) {
        this.display_runtime = display_runtime;
    }

    public String getDisplay_runtime() {
        return display_runtime;
    }

    public CountClass getMails() {
        return mails;
    }

    public boolean isLiked() {
        return (likes != null && likes.data != null && likes.data.length > 0) || (sLikedSet != null && sLikedSet.contains(getId()));
    }

    public void setLiked() {
        sLikedSet.add(getId());
    }

    public void removeLiked() {
        if (likes != null && likes.data != null) {
            likes.data = null;
        }
        if (sLikedSet != null) {
            if (sLikedSet.contains(getId())) {
                sLikedSet.remove(getId());
            }
        }

    }

}
