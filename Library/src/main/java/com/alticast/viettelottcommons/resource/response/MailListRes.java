package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.Mail;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class MailListRes {


    private int total  = 0;
    private ArrayList<Mail> data = null;

    public int getTotal() {
        return total;
    }

    public ArrayList<Mail> getData() {
        return data;
    }
    public void setTotal(int total) {
        this.total = total;
    }

    public void setData(ArrayList<Mail> data) {
        this.data = data;
    }
}
