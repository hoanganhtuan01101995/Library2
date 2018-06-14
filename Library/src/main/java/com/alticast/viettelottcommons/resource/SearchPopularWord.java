package com.alticast.viettelottcommons.resource;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class SearchPopularWord {
    private String query = null;
    private int ranking = 0;
    private int prior_ranking = 0;

    public String getQuery() {
        return query;
    }

    public int getRanking() {
        return ranking;
    }

    public int getPrior_ranking() {
        return prior_ranking;
    }
}
