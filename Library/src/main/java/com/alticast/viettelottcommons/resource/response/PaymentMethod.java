package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.Payment;

/**
 * Created by mc.kim on 8/5/2016.
 */
public class PaymentMethod {
    private Payment normal = null;
    private Payment point = null;
    private Payment coupon = null;
    private Payment phone = null;
    private Payment credit = null;
    private Payment hybrid = null;
    private Payment wallet = null;

    public Payment getNormal() {
        return normal;
    }

    public Payment getPoint() {
        return point;
    }

    public Payment getCoupon() {
        return coupon;
    }

    public Payment getPhone() {
        return phone;
    }

    public Payment getCredit() {
        return credit;
    }

    public Payment getHybrid() {
        return hybrid;
    }

    public Payment getWallet() {
        return wallet;
    }
}
