package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.CampaignGroupData;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/10/2016.
 */
public class CampaignGroupRes {
    private int total = 0;
    private ArrayList<CampaignGroupData> data = null;

    public int getTotal() {
        return total;
    }

    public ArrayList<CampaignGroupData> getData() {
        return data;
    }

    public CampaignGroupData getData(String pid) {
        int size = data.size();
        for (int i = 0; i < size; i++) {
            if (data.get(i).getPid().equals(pid)) {
                return data.get(i);
            }
        }
        return null;
    }

    public CampaignGroupRes() {

    }

    public CampaignGroupRes(CampaignGroupRes campaignGroupRes) {
        total = campaignGroupRes.total;

        if (campaignGroupRes.getData() != null) {
            data = new ArrayList<>();
            data.addAll(campaignGroupRes.getData());
        }
    }

}
