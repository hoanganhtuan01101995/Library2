package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.*;
import com.alticast.viettelottcommons.resource.Package;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/22/2016.
 */
public class PackageDetailInfoRes {
    private String id = null;
    private ArrayList<Product> product = null;
    private com.alticast.viettelottcommons.resource.Package pkg = null;
    private String created_time = null;
    private String updated_time = null;
    private String backGround = null;


    public String getId() {
        return id;
    }

    public ArrayList<Product> getProduct() {
        return product;
    }

    public Package getPkg() {
        return pkg;
    }

    public String getCreated_time() {
        return created_time;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public String getBackGround() {
        return backGround;
    }
}
