package com.alticast.viettelottcommons.resource;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class Resume {

    private String id = null;

    private String program_id  = null;
    private long time_offset = 0;
    private String type = null;
    private String title = null;
    private String series = null;
    private int season = 0;
    private int episode = 0;

    private Vod vod = null;

    public String getId() {
        return id;
    }

    public long getTime_offset() {
        return time_offset;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }


    public Vod getVod() {
        return vod;
    }

    public void setVod(Vod vod) {
        this.vod = vod;
    }

    public String getProgram_id() {
        return program_id;
    }

    public void setProgram_id(String program_id) {
        this.program_id = program_id;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

}
