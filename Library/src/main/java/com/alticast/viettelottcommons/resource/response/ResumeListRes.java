package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.Resume;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class ResumeListRes {
    private int total = 0;
    private ArrayList<Resume> data = null;

    public int getTotal() {

        return total;
    }

    public ArrayList<Resume> getData() {
        return data;
    }
}
