package com.alticast.viettelottcommons.resource;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;


public interface VodInfo extends Parcelable {
    public String getTitle();

    public String getLogoUrl(Activity activity);

    public void leaveCheckPoint(long startTime, int currentPosition, int duration);

    public void removeCheckPoint(long startTime, int duration);

    public void leaveLike(Context context);

//    public void showInfo(Context context);

//    public String getEntryPath();

//    public String getMenuPathId();

    public String getLocator();

    public String getLocatorChromeCast();

    public void setData(Object object);

    public Vod getVod();

    public Schedule getSchedule();

    public String getId();

    public boolean isPurchased();

    public void setPurchase(boolean isPurchase);

}
