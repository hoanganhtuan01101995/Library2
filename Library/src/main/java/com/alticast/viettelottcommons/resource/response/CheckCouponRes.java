package com.alticast.viettelottcommons.resource.response;

/**
 * Created by duyuno on 2/23/18.
 */
public class CheckCouponRes {

    public static final String METHOD_RATE = "rate";
    public static final String METHOD_PRICE = "price";

    private String discount_method;
    private int discount_value;
    private String code;

    public String getDiscount_method() {
        return discount_method != null ? discount_method : METHOD_PRICE;
    }

    public void setDiscount_method(String discount_method) {
        this.discount_method = discount_method;
    }

    public int getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(int discount_value) {
        this.discount_value = discount_value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
