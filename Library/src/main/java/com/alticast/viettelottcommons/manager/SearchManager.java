package com.alticast.viettelottcommons.manager;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class SearchManager {
    private static SearchManager ourInstance = new SearchManager();

    public static SearchManager getInstance() {
        return ourInstance;
    }

    private SearchManager() {
    }
}
