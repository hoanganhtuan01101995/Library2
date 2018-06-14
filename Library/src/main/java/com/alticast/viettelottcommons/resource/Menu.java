package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/8/2016.
 */
public class Menu {

    private static final String SHORT_CUT_CONFIG = "shortcut";

    private boolean isLast = false;
    private boolean expanded = false;
    private String[] path = null;

    private String id = null;

    private String path_id = null;

    private String type = null;

    private String tag = null;

    private String updated_time = null;

    private String[] device = null;

    private String[] regional = null;

    private ArrayList<MultiLingualText> name = null;

    private String license_start = null;

    private String license_end = null;

    private ArrayList<CustomValue> config = null;

    private String parentOrgId = null;

    private String[] images = null;

    public String[] getPath() {
        return path;
    }

    public String getId() {
        return id;
    }

    public String getPath_id() {
        return path_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public String[] getDevice() {
        return device;
    }

    public String[] getRegional() {
        return regional;
    }

    public boolean isHomeMenu;

    private boolean isRoot;

    private boolean isClickable;

    private boolean isDisplay;

    public boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(boolean display) {
        isDisplay = display;
    }

    public boolean isLogout;

    public boolean isLogout() {
        return isLogout;
    }

    public void setLogout(boolean logout) {
        isLogout = logout;
    }

    public String getName(String key) {
        if(isLogout()) {
            return "";
        }
        if (name == null) return null;
        int size = name.size();

        for (int i = 0; i < size; i++) {
            if (name.get(i).getLang().equals(key)) {
                return name.get(i).getText();
            }
        }
        return name.get(0).getText();
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLicense_start() {
        return license_start;
    }

    public String getLicense_end() {
        return license_end;
    }

    public String getConfig(String key) {
        if(config == null) {
            return null;
        }
        int size = config.size();
        for (int i = 0; i < size; i++) {
            if (config.get(i).getName().equals(key)) {
                return config.get(i).getValue();
            }
        }
        return null;
    }

    public boolean isLeafCategories() {
        if(config == null) {
            return false;
        }
        int size = config.size();
        for (int i = 0; i < size; i++) {
            if (config.get(i).getName().equals("__leaf_category")) {
                return Boolean.parseBoolean(config.get(i).getValue());
            }
        }
        return false;
    }

    public boolean isSeries() {
        if(config == null) {
            return false;
        }
        int size = config.size();
        for (int i = 0; i < size; i++) {
            if (config.get(i).getName().equals("series")) {
                return true;
            }
        }
        return false;
    }

    public CustomValue checkExistedShortCut() {
        if (config == null) {
            return null;
        }

        for (CustomValue customValue : config) {
            if(customValue.getName() == null) {
                continue;
            }
            if (customValue.getName().equals(SHORT_CUT_CONFIG) && customValue.getValue().length() != 0) {
                return customValue;
            }
        }

        return null;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public String[] getImages() {
        return images;
    }

    public void setPath_id(String path_id) {
        this.path_id = path_id;
    }


    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }


    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(ArrayList<MultiLingualText> name) {
        this.name = name;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    public boolean isHomeMenu() {
        return isHomeMenu;
    }

    public void setHomeMenu(boolean homeMenu) {
        isHomeMenu = homeMenu;
    }
}
