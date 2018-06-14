package com.alticast.viettelottcommons.resource.wallet;

import com.alticast.viettelottcommons.resource.MultiLingualText;

import java.util.ArrayList;

/**
 * Created by duyuno on 9/12/17.
 */
public class Promotion {
    private String id;
    private String license_start;
    private String license_end;
    private ArrayList<MultiLingualText> name;
    private ArrayList<MultiLingualText> description;
    private ArrayList<MultiLingualText> full_description;


    public String getId() {
        return id;
    }

    public ArrayList<MultiLingualText> getName() {
        return name;
    }

    public ArrayList<MultiLingualText> getDescription() {
        return description;
    }

    public ArrayList<MultiLingualText> getFull_description() {
        return full_description;
    }

}
