package com.alticast.viettelottcommons.resource;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by duyuno on 1/13/17.
 */
public class RegistedDevice implements Parcelable {
    private int total;
    private int registered;
    private ArrayList<MyDeviceAccount.Devices> devices;

    protected RegistedDevice(Parcel in) {
        total = in.readInt();
        registered = in.readInt();
        devices = in.createTypedArrayList(MyDeviceAccount.Devices.CREATOR);
    }

    public static final Creator<RegistedDevice> CREATOR = new Creator<RegistedDevice>() {
        @Override
        public RegistedDevice createFromParcel(Parcel in) {
            return new RegistedDevice(in);
        }

        @Override
        public RegistedDevice[] newArray(int size) {
            return new RegistedDevice[size];
        }
    };

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRegistered() {
        return registered;
    }

    public void setRegistered(int registered) {
        this.registered = registered;
    }

    public ArrayList<MyDeviceAccount.Devices> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<MyDeviceAccount.Devices> devices) {
        this.devices = devices;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(total);
        dest.writeInt(registered);
        dest.writeTypedList(devices);
    }
}
