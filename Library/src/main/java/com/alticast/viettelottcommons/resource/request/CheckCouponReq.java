package com.alticast.viettelottcommons.resource.request;

/**
 * Created by duyuno on 2/23/18.
 */
public class CheckCouponReq {
    private String product_id;
    private String coupon_tx_id;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCoupon_tx_id() {
        return coupon_tx_id;
    }

    public void setCoupon_tx_id(String coupon_tx_id) {
        this.coupon_tx_id = coupon_tx_id;
    }
}
