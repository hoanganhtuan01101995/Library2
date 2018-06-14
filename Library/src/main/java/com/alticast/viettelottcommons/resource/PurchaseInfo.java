package com.alticast.viettelottcommons.resource;

import android.os.Parcel;
import android.os.Parcelable;

import com.alticast.viettelottcommons.manager.TimeManager;
import com.alticast.viettelottcommons.util.TimeUtil;
import com.alticast.viettelottcommons.util.WindDataConverter;

/**
 * Created by mc.kim on 8/8/2016.
 */
public class PurchaseInfo implements Parcelable{
    private String id = null;
    private String date = null;
    private String expired_date = null;
    private boolean is_directly;

    public PurchaseInfo() {

    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getExpired_date() {
        return expired_date;
    }

    public boolean is_directly() {
        return is_directly;
    }

    public boolean isExpired() {
        long expiredDate = TimeUtil.getLongTime(getExpired_date(), WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);
//        expiredDate = TimeManager.getInstance().getCurrentTime(expiredDate);
        long currentTime = System.currentTimeMillis();
        return expiredDate < currentTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setExpired_date(String expired_date) {
        this.expired_date = expired_date;
    }

    public void setIs_directly(boolean is_directly) {
        this.is_directly = is_directly;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    protected PurchaseInfo(Parcel in) {
        id = in.readString();
//        date = in.readString();
//        expired_date = in.readString();
        is_directly = in.readInt() == 1 ? true : false;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
//        dest.writeString(date != null ? date : "");
//        dest.writeString(expired_date != null ? expired_date : "");
        dest.writeInt(is_directly ? 1 : 0);
    }

    @SuppressWarnings("unused")
    public static final Creator<PurchaseInfo> CREATOR = new Creator<PurchaseInfo>() {
        @Override
        public PurchaseInfo createFromParcel(Parcel in) {
            return new PurchaseInfo(in);
        }

        @Override
        public PurchaseInfo[] newArray(int size) {
            return new PurchaseInfo[size];
        }
    };
}
