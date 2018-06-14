package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/10/2016.
 */
public class CampaignData {


    private String id = null;
    private String pid = null;
    private ArrayList<MultiLingualText> name = null;
    private ArrayList<Campaigns> campaigns = null;

    private String action = null;
    private String action_url = null;

    public String getAction() {
        return action;
    }

    public String getAction_url() {
        return action_url;
    }

    public String getId() {
        return id;
    }

    public String getPid() {
        return pid;
    }

    public ArrayList<MultiLingualText> getName() {
        return name;
    }

    public ArrayList<Campaigns> getCampaigns() {
        return campaigns;
    }

    public String getPictureId(){
        if(campaigns==null){
            return null;
        }else{
            return campaigns.get(0).getId();
        }
    }

    public String getTitle(String key) {
        int size = name.size();
        String title = null;
        for(int i = 0 ; i<size ; i++){
            String tmp = name.get(i).getLang();
            if(tmp.equalsIgnoreCase(key)){
                title = name.get(i).getText();
                break;
            }
        }
        if(title == null){
            title = name.get(0).getText();
        }

        return title;
    }
}
