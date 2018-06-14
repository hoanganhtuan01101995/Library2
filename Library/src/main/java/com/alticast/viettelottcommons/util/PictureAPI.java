package com.alticast.viettelottcommons.util;

import com.alticast.viettelottcommons.WindmillConfiguration;

/**
 * Created by mc.kim on 8/25/2016.
 */
public class PictureAPI {
    private static PictureAPI ourInstance = new PictureAPI();

    public static PictureAPI getInstance() {
        return ourInstance;
    }

    private PictureAPI() {
    }

    public String getMenuIcon(String id) {
        String url = null;
        try {
            url = WindmillConfiguration.getBaseUrl() + "api1/contents/pictures/" + id + "?type=original";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public String getBanner(String id) {
        String url = null;
        try {
            url = WindmillConfiguration.getBaseUrl() + "api1/contents/pictures/" + id + "?type=original";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public String getPictureUrl(String id){
        String url = null;
        try {
            url = WindmillConfiguration.getBaseUrl() + "api1/contents/pictures/" + id + "?type=original";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public String getCategoryPictureUrl(String id){
        String url = null;
        try {
            url = WindmillConfiguration.getBaseUrl() + "api1/contents/categories/" + id + "/picture";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
}
