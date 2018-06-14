package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.Payment;
import com.alticast.viettelottcommons.resource.Price;
import com.alticast.viettelottcommons.resource.Product;

/**
 * Created by mc.kim on 8/5/2016.
 */
public class PurchaseResultRes {
    private String id = null;
    private String purchase_stdt = null;
    private String purchase_endt = null;
    private String reserved_cancel_date = null;
    private String user_id = null;

    private String purchaser_id = null;

    private String alias = null;

    private boolean hidden = false;
    private Payment payment = null;
    private Product product = null;

    private Price total_amount = null;
    private Price price = null;

    private long rental_duration = 0;

    private String source = null;


    public String getId() {
        return id;
    }

    public String getPurchase_stdt() {
        return purchase_stdt;
    }

    public String getPurchase_endt() {
        return purchase_endt;
    }

    public String getReserved_cancel_date() {
        return reserved_cancel_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPurchaser_id() {
        return purchaser_id;
    }

    public String getAlias() {
        return alias;
    }

    public boolean isHidden() {
        return hidden;
    }

    public Payment getPayment() {
        return payment;
    }

    public Product getProduct() {
        return product;
    }

    public Price getTotal_amount() {
        return total_amount;
    }

    public Price getPrice() {
        return price;
    }

    public long getRental_duration() {
        return rental_duration;
    }

    public String getSource() {
        return source;
    }
}
