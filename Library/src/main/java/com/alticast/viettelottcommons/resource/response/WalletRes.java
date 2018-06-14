package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.Purchase;

import java.util.ArrayList;

/**
 * Created by duyuno on 8/17/17.
 */
public class WalletRes {
    private int total = 0;
    private ArrayList<Product> data = null;

    public int getTotal() {
        return total;
    }

    public ArrayList<Product> getData() {
        return data;
    }


}
