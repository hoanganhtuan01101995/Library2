package com.alticast.viettelottcommons.resource;

import com.alticast.viettelottcommons.WindmillConfiguration;

import java.util.ArrayList;

public class Trailer {
    private String id = null;
    private String pid = null;
    private String type = null;
    private ArrayList<MultiLingualText> name = null;
    private ArrayList<MultiLingualText> description = null;
    private Location location;
    private Poster poster;
    private String created_time = null;
    private String updated_time = null;

    public Trailer(String id) {
        this.id = id;
    }
    public Location getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public String getName(String lang) {
        String trailerName;
        int size = name.size();

        for (int i = 0; i < size; i++) {
            MultiLingualText multiLingualText = name.get(i);
            if(multiLingualText.getLang().equals(WindmillConfiguration.LANGUAGE)){
                trailerName = multiLingualText.getText();
                break;
            }
        }
        trailerName = name.get(0).getText();

        return trailerName;
    }

    public String getCreated_time() {
        return created_time;
    }

    public String getUpdated_time() {
        return updated_time;
    }
    public Poster getPoster() {
        return poster;
    }

    public void setPoster(Poster poster) {
        this.poster = poster;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDescription() {
        String desc;
        int size = name.size();

        for (int i = 0; i < size; i++) {
            MultiLingualText multiLingualText = description.get(i);
            if(multiLingualText.getLang().equals(WindmillConfiguration.LANGUAGE)){
                desc = multiLingualText.getText();
                break;
            }
        }
        desc = description.get(0).getText();

        return desc;
    }

    public void setDescription(ArrayList<MultiLingualText> description) {
        this.description = description;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setId(String id) {
        this.id = id;
    }
}
