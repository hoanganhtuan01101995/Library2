package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;

/**
 * Created by duyuno on 8/18/17.
 */
public class PkgObject {
    private String id;
    private String type;
    private ArrayList<MultiLingualText> name = null;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public ArrayList<MultiLingualText> getName() {
        return name;
    }
}
