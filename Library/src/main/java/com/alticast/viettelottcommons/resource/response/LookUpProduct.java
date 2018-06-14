package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.Price;
import com.alticast.viettelottcommons.resource.Product;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/5/2016.
 */
public class LookUpProduct {
    private String id = null;
    private String purchase_stdt = null;
    private String purchase_endt = null;
    private Price purchase_amount = null;
    private Product product = null;
    private ArrayList<Product> purchasable_products = null;


    public String getId() {
        return id;
    }

    public String getPurchase_stdt() {
        return purchase_stdt;
    }

    public String getPurchase_endt() {
        return purchase_endt;
    }

    public Price getPurchase_amount() {
        return purchase_amount;
    }

    public Product getProduct() {
        return product;
    }

    public ArrayList<Product> getPurchasable_products() {
        return purchasable_products;
    }
}
