package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/22/2016.
 */
public class Package {

    private String id = null;

    /**
     * The type.
     */
    private String type = null;
    /**
     * The name.
     */
    private ArrayList<MultiLingualText> name = null;

    /**
     * The description.
     */
    private ArrayList<MultiLingualText> description = null;

    /**
     * The categories.
     */
    private ArrayList<Category> categories = null;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public ArrayList<MultiLingualText> getName() {
        return name;
    }

    public ArrayList<MultiLingualText> getDescription() {
        return description;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }
}
