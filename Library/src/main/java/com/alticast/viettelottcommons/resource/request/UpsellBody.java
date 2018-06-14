package com.alticast.viettelottcommons.resource.request;

/**
 * Created by duyuno on 10/25/16.
 */
public class UpsellBody {
    public String product_id;
    public int rental_period;
    public String unit;
    public String sale_code;
    public String coupon_tx_id;

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setRental_period(int rental_period) {
        this.rental_period = rental_period;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setSale_code(String sale_code) {
        this.sale_code = sale_code;
    }

    public void setCoupon_tx_id(String coupon_tx_id) {
        this.coupon_tx_id = coupon_tx_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public int getRental_period() {
        return rental_period;
    }

    public String getUnit() {
        return unit;
    }

    public String getSale_code() {
        return sale_code;
    }

    public String getCoupon_tx_id() {
        return coupon_tx_id;
    }
}
