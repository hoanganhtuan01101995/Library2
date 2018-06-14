package com.alticast.viettelottcommons.api;

import com.alticast.viettelottcommons.resource.Series;
import com.alticast.viettelottcommons.resource.Vod;

import java.util.ArrayList;

/**
 * Created by duyuno on 5/23/17.
 */
public interface GetSeriesInfoListener {
    public void onComplete(Series lastestSeries, ArrayList<Vod> listEpisodes);
}
