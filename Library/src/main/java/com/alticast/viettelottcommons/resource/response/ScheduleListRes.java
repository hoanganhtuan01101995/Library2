package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.Schedule;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class ScheduleListRes {
    private int total = 0;
    private ArrayList<Schedule> data = null;

    public int getTotal() {
        return total;
    }

    public ArrayList<Schedule> getData() {
        return data;
    }

    public void setData(ArrayList<Schedule> data) {
        this.data = data;
    }
}
