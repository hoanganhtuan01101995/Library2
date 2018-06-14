package com.alticast.viettelottcommons.resource;

import java.util.Arrays;

/**
 * Created by user on 2016-03-11.
 */
public class AvailableMethod {

    public static final String KEY_VALUE = "VALUE";

    private Method normal = null;
    private Method point = null;
    private Method coupon = null;
    private Method phone = null;
    private Method credit = null;
    private Method hybrid = null;
    private Method wallet = null;

    public class Method {

//        private Balance balance = null;
//        private Balance bonus = null;
        private int vat_rate = 0;
        private boolean subscriber = false;
        private String message = null;
        private Combination [] combination = null;

        public boolean isSubscriber() {
            return subscriber;
        }


        //    protected AvailablePayment(Parcel in) {
//        balance = in.readTypedObject(Balance.class.getClassLoader());
//        subscriber = in.readInt() == 1;
//        vat_rate = in.readInt();
//    }

        public class Balance {
            private String currency = null;
            private int value = 0;

            @Override
            public String toString() {
                return "Balance{" +
                        "currency='" + currency + '\'' +
                        ", value=" + value +
                        '}';
            }
        }

        public class Combination{
            private String [] type;

            @Override
            public String toString() {
                return "Combination{" +
                        "type=" + Arrays.toString(type) +
                        '}';
            }
        }


        @Override
        public String toString() {
            return "Method{" +
//                    "balance=" + balance +
//                    ", bonus=" + bonus +
                    ", vat_rate=" + vat_rate +
                    ", subscriber=" + subscriber +
                    ", message='" + message + '\'' +
                    ", combination=" + Arrays.toString(combination) +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AvailableMethod{" +
                "normal=" + normal +
                ", point=" + point +
                ", coupon=" + coupon +
                ", phone=" + phone +
                ", credit=" + credit +
                ", hybrid=" + hybrid +
                ", wallet=" + wallet +
                '}';
    }

    public Method getPhone() {
        return phone;
    }

    public boolean isPoorUser(){
        return phone != null && !phone.isSubscriber();
    }

    public boolean isPointUser(){
        return point != null && point.isSubscriber();
    }


    //    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeTypedObject(balance);
//        dest.writeInt(subscriber? 1 : 0);
//        dest.writeInt(vat_rate);
//    }

//    @SuppressWarnings("unused")
//    public static final Parcelable.Creator<BasicProduct> CREATOR = new Parcelable.Creator<BasicProduct>() {
//        @Override
//        public BasicProduct createFromParcel(Parcel in) {
//            return new BasicProduct(in);
//        }
//
//        @Override
//        public BasicProduct[] newArray(int size) {
//            return new BasicProduct[size];
//        }
//    };

}
