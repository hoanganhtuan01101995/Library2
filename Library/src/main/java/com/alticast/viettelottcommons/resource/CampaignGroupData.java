package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/10/2016.
 */
public class CampaignGroupData {

    public static final String ACTION_CATEGORY = "category";
    public static final String ACTION_CUSTOM = "custom";
    public static final String ACTION_URL_RESUME = "resume";

    private String id = null;
    private String pid = null;
    private ArrayList<MultiLingualText> name = null;
    private ArrayList<Config> config = null;
    private ArrayList<CampaignData> data = null;

    public String getId() {
        return id;
    }

    public String getPid() {
        return pid;
    }

    public ArrayList<MultiLingualText> getName() {
        return name;
    }

    public ArrayList<Config> getConfig() {
        return config;
    }

    public ArrayList<CampaignData> getData() {
        return data;
    }

    public void removeResume() {
        int size = data.size();
        int position = -1;
        for (int i = 0; i < size; i++) {
            CampaignData campaignData = data.get(i);
            if (campaignData.getAction().equals(ACTION_CUSTOM) && campaignData.getAction_url().equals(ACTION_URL_RESUME)) {
                position = i;
                break;
            }
        }

        if (position >= 0) {
            data.remove(position);
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setName(ArrayList<MultiLingualText> name) {
        this.name = name;
    }

    public void setConfig(ArrayList<Config> config) {
        this.config = config;
    }

    public void setData(ArrayList<CampaignData> data) {
        this.data = data;
    }
}
