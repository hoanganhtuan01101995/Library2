package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.loader.SearchLoader;
import com.alticast.viettelottcommons.resource.Category;
import com.alticast.viettelottcommons.resource.Hits;
import com.alticast.viettelottcommons.resource.Param;
import com.alticast.viettelottcommons.resource.SearchCategory;
import com.alticast.viettelottcommons.resource.Suggestion;
import com.alticast.viettelottcommons.util.Logger;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class SearchResultRes {

    public int took = 0;

    public Hits hits = null;

    public SearchCategory best = null;

    public ArrayList<Category> categories = null;

    public Suggestion suggestion = null;

    public SearchCategory schedule = null;
    public SearchCategory tvshow = null;
    public SearchCategory movie = null;
    public SearchCategory channel = null;
    public SearchCategory vgroup = null;
    public SearchCategory catchup = null;

    public SearchCategory vseries = null;
    public SearchCategory vod = null;

    public SearchCategory getVseries() {
        return vseries;
    }

    public void setVseries(SearchCategory vseries) {
        this.vseries = vseries;
    }

    public SearchCategory getVod() {
        return vod;
    }

    public void setVod(SearchCategory vod) {
        this.vod = vod;
    }


    public Param _params = null;

    public int getTook() {
        return took;
    }

    public Hits getHits() {
        return hits;
    }

    public SearchCategory getBest() {
        return best;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }

    public SearchCategory getSchedule() {
        return schedule;
    }

    public SearchCategory getTvshow() {
        return tvshow;
    }

    public SearchCategory getMovie() {
        return movie;
    }

    public SearchCategory getChannel() {
        return channel;
    }

    public SearchCategory getVgroup() {
        return vgroup;
    }

    public SearchCategory getCatchup() {
        return catchup;
    }

    public Param getParams() {
        return _params;
    }

    public boolean isHasSearchResult() {
        if (getSearchResultSize(SearchLoader.CATEGORY_TVSHOW) > 0 ||
                getSearchResultSize(SearchLoader.CATEGORY_CHANNEL) > 0 ||
                getSearchResultSize(SearchLoader.CATEGORY_PROGRAM) > 0 ||
                getSearchResultSize(SearchLoader.CATEGORY_CATCHUP) > 0 ||
                getSearchResultSize(SearchLoader.CATEGORY_MOVIE) > 0 ||
                getSearchResultSize(SearchLoader.CATEGORY_SERIES) > 0) {
            return true;
        }
        return false;
    }

    public SearchCategory getSearchResult(String type) {
        SearchCategory searchCategory = null;
        switch (type) {
            case SearchLoader.CATEGORY_MOVIE:
                searchCategory = getMovie();
                break;
            case SearchLoader.CATEGORY_TVSHOW:
                searchCategory = getTvshow();
                break;
            case SearchLoader.CATEGORY_SERIES:
                searchCategory = getVgroup();
                break;
            case SearchLoader.CATEGORY_CHANNEL:
                searchCategory = getChannel();
                break;
            case SearchLoader.CATEGORY_PROGRAM:
                searchCategory = getSchedule();
                break;
            case SearchLoader.CATEGORY_CATCHUP:
                searchCategory = getCatchup();
                break;
            case SearchLoader.CATEGORY_VSERIES:
                searchCategory = getVseries();
                break;
            case SearchLoader.CATEGORY_VOD:
                searchCategory = getVod();
                break;
        }

        return searchCategory;

    }

    public int getSearchResultSize(String type) {
        SearchCategory searchCategory = getSearchResult(type);
        if (searchCategory == null) {
            Logger.print("TAG ", " getSearchResultSize  type " + type + "total " + "null : 0 ");
            return 0;
        } else {
            Logger.print("TAG ", " getSearchResultSize  type " + type + "total " + searchCategory.getTotal());
            return searchCategory.getTotal();
        }
    }
}
