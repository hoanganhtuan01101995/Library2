package com.alticast.viettelottcommons;

import com.alticast.viettelottcommons.resource.Schedule;

import java.util.ArrayList;

/**
 * Created by duyuno on 4/23/17.
 */
public class GlobalInfo {

    public static ArrayList<Schedule> listSchedule;

    public static ArrayList<Schedule> getListSchedule() {
        return listSchedule;
    }

    public static void setListSchedule(ArrayList<Schedule> listSchedule) {
        GlobalInfo.listSchedule = listSchedule;
    }

    public static boolean checkScheduleInChannel(ArrayList<Schedule> listSchedule, String channelId) {
        if(listSchedule == null || listSchedule.isEmpty() || channelId == null || channelId.isEmpty()) return false;

        Schedule schedule = listSchedule.get(0);
        if(schedule.getChannel() == null) return false;

        return schedule.getChannel().getId().equals(channelId);
    }
}
