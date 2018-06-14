package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.Campaigns;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/11/2016.
 */
public class CampaignsRes {
    private int total = 0;
    private ArrayList<Campaigns> data = null;

    public int getTotal() {
        return total;
    }

    public ArrayList<Campaigns> getData() {
        return data;
    }

    public Campaigns getData(String action) {
        int size = data.size();
        for (int i = 0; i < size; i++) {
            Campaigns campaigns = data.get(i);
            if (campaigns.getAction().equals(action)) {
                return campaigns;
            }
        }

        return null;

    }
}
