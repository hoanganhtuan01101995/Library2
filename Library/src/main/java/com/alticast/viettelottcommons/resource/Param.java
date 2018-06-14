package com.alticast.viettelottcommons.resource;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class Param {
    private String access_token = null;
    private String q = null;
    private String cat = null;
    private String m_field = null;
    private String p_rating = null;
    private String vsort = null;
    private int limit = 0;
    private int offset = 0;
    private String lang = null;
    private String input_route = null;

    public String getAccess_token() {
        return access_token;
    }

    public String getQ() {
        return q;
    }

    public String getCat() {
        return cat;
    }

    public String getM_field() {
        return m_field;
    }

    public String getP_rating() {
        return p_rating;
    }

    public String getVsort() {
        return vsort;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public String getLang() {
        return lang;
    }

    public String getInput_route() {
        return input_route;
    }
}
