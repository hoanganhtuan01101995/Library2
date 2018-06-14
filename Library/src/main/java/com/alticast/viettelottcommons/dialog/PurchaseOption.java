package com.alticast.viettelottcommons.dialog;

/**
 * Created by duyuno on 3/13/17.
 */
public class PurchaseOption {
    private String name;
    private Object object;


    public PurchaseOption(String name, Object object) {
        this.name = name;
        this.object = object;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
