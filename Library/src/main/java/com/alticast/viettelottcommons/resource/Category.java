package com.alticast.viettelottcommons.resource;

import com.alticast.viettelottcommons.util.PictureAPI;

/**
 * Created by mc.kim on 8/9/2016.
 */
public class Category extends Menu {
    private String pid = null;
    private String categoryPosterUrl = null;
    private boolean isSearch;

    private String menuPath;

    public boolean isSearch() {
        return isSearch;
    }

    public void setSearch(boolean search) {
        isSearch = search;
    }

    public String getPid() {
        return pid;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    public String getCategoryPosterUrl() {
        return PictureAPI.getInstance().getCategoryPictureUrl(getId());
    }
}
