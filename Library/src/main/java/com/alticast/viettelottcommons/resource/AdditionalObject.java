package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;

/**
 * Created by duyuno on 8/18/17.
 */
public class AdditionalObject {
    private String id;
    private PkgObject pkg;
    private ArrayList<Product> product;

    public String getId() {
        return id;
    }

    public PkgObject getPkg() {
        return pkg;
    }

    public ArrayList<Product> getProduct() {
        return product;
    }
}
