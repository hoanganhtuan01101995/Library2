package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.resource.Price;

import java.text.DecimalFormat;

/**
 * Created by mc.kim on 8/5/2016.
 */
public class PaidAmountRes {
    public static int TYPE_TOTAL = 0;
    public static int TYPE_DISCOUNT = 1;
    public static int TYPE_PRE = 2;
    public static int TYPE_POINT = 3;
    public static int TYPE_NORMAL = 4;

    private int month = 0;
    private int limit = 0;
    private Payments payment = null;

    public Payments getPayment() {
        return payment;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
    public int getLimitInteger(){
        return limit;
    }
    public String getLimit() {
        return currency(limit);
    }

    public int getLimitValue() {
        return limit;
    }

    private String currency(int nameStr) {
        DecimalFormat df = new DecimalFormat("#,###");

        return df.format(nameStr);
    }

    public float getPriceValue(int type) {

        float nameStr = 0;
        Payments payment = getPayment();
        Price price = null;
        if (type == TYPE_TOTAL) {
            price = payment.total;
        } else if (type == TYPE_DISCOUNT) {
            price = payment.discount;
        } else if (type == TYPE_PRE) {
            price = payment.prepayment;
        } else if (type == TYPE_POINT) {
            price = payment.point;
        } else if (type == TYPE_NORMAL) {
            price = payment.normal;
        }

        if (price != null) {
            nameStr = price.getValue();
        }
        return nameStr;
    }

    public class Payments {
        private Price total = null;
        private Price discount = null;
        private Price prepayment = null;
        private Price point = null;
        private Price normal = null;

        public Price getTotal() {
            return total;
        }

        public Price getDiscount() {
            return discount;
        }

        public Price getPrepayment() {
            return prepayment;
        }

        public Price getPoint() {
            return point;
        }

        public Price getNormal() {
            return normal;
        }

    }
}
