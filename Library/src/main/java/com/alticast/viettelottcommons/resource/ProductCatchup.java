package com.alticast.viettelottcommons.resource;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by dUONG on 11/30/2016.
 */

public class ProductCatchup {
    boolean bundle;
    private String id;
    ArrayList<Product> purchasable_products;
    Product product;

    protected ProductCatchup() {
        super();
    }

    public boolean isBundle() {
        return bundle;
    }

    public void setBundle(boolean bundle) {
        this.bundle = bundle;
    }

    public ArrayList<Product> getPurchasable_products() {
        return purchasable_products;
    }

    public void setPurchasable_products(ArrayList<Product> purchasable_products) {
        this.purchasable_products = purchasable_products;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


