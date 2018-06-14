package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/10/2016.
 */
public class Campaigns {

    public static final String ACTION_MAIL = "mail";
    public static final String ACTION_CHANNEL = "channel";
    public static final String ACTION_CATEGORY = "category";
    public static final String ACTION_PROGRAM_CATEGORY = "program_category";
    public static final String ACTION_PROGRAM = "program";

    private String id = null;
    private String license_type = null;
    private String license_start = null;
    private String license_end = null;
    private ArrayList<MultiLingualText> name = null;
    private ArrayList<MultiLingualText> description = null;
    private String action = null;
    private String action_url = null;
    private String image = null;
    private String created_time = null;
    private String updated_time = null;

    public String getId() {
        return id;
    }

    public String getLicense_type() {
        return license_type;
    }

    public String getLicense_start() {
        return license_start;
    }

    public String getLicense_end() {
        return license_end;
    }

    public ArrayList<MultiLingualText> getName() {
        return name;
    }


    public String getTitle(String key) {
        int size = name.size();
        String title = null;
        for (int i = 0; i < size; i++) {
            String tmp = name.get(i).getLang();
            if (tmp.equalsIgnoreCase(key)) {
                title = name.get(i).getText();
                break;
            }
        }
        if (title == null) {
            title = name.get(0).getText();
        }

        return title;
    }

    public String getDescription(String key) {
        if (description == null) return "";
        int size = description.size();
        String title = null;
        for (int i = 0; i < size; i++) {
            String tmp = description.get(i).getLang();
            if (tmp.equalsIgnoreCase(key)) {
                title = description.get(i).getText();
                break;
            }
        }
        if (title == null) {
            title = description.get(0).getText();
        }

        return title;
    }

    public String getAction() {
        return action;
    }

    public String getAction_url() {
        return action_url;
    }

    public String getImage() {
        return image;
    }

    public String getCreated_time() {
        return created_time;
    }

    public String getUpdated_time() {
        return updated_time;
    }
}
