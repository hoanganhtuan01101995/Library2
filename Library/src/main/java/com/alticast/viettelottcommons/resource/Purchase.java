package com.alticast.viettelottcommons.resource;


import com.alticast.viettelottcommons.util.TimeUtil;
import com.alticast.viettelottcommons.util.WindDataConverter;

import java.util.ArrayList;
import java.util.Calendar;

public class Purchase {
    public static final int CCLASS_STB = 0;
    public static final int CCLASS_HANDHELD = 1;
    public static final int CCLASS_STB_HANDHELD = 2;


    public static final String SOURCE_SMS = "sms";
    public static final String SOURCE_STB = "stb";
    public static final String SOURCE_HANDHELD = "handheld";


    private String id = null;
    private String purchase_stdt = null;
    private String purchase_endt = null;
    private String reserved_cancel_date = null;
    private String user_id = null;
    private String purchaser_id = null;
    private String alias = null;
    private String hidden = null;
    private boolean bundle = false;
    private String source = null;
    private String auto_renewal = null;

    private PurchaseProduct product = null;


    private Payment payment = null;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setAuto_renewal(String auto_renewal) {
        this.auto_renewal = auto_renewal;
    }

    public class PurchaseProduct {
        private String id = null;
        private String title = null;
        private String category = null;
        private int rental_period = 0;
        private String type = null;
        private int screen_max;
        private String screen_type = null;
        private boolean ispackage = false;
        private int parental_rating = 0;
        private String parent_id = null;
        private boolean refund_prorated_daily = false;
        private ArrayList<String> target_devices = null;

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getCategory() {
            return category;
        }

        public int getRental_period() {
            return rental_period;
        }

        public String getType() {
            return type;
        }

        public boolean ispackage() {
            return ispackage;
        }

        public int getParental_rating() {
            return parental_rating;
        }

        public String getParent_id() {
            return parent_id;
        }

        public boolean isRefund_prorated_daily() {
            return refund_prorated_daily;
        }

        public ArrayList<String> getTarget_devices() {
            return target_devices;
        }

        public int getScreen_max() {
            return screen_max;
        }

        public String getScreen_type() {
            return screen_type;
        }
    }

    public int getScreenMax() {
        return product != null ? product.getScreen_max() : 0;
    }
    public String getScreenType() {
        return product != null ? product.getScreen_type() : null;
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

    public String getHidden() {
        return hidden;
    }

    public boolean isBundle() {
        return bundle;
    }

    public String getSource() {
        return source;
    }

    public String getAuto_renewal() {
        return auto_renewal;
    }

    public PurchaseProduct getProduct() {
        return product;
    }

    public String getProductType() {
        return product.getType();
    }

    public long getPurchasedDate() {
        return TimeUtil.getLongTime(purchase_stdt, WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);
    }

    public long getExpireDate() {
        return TimeUtil.getLongTime(purchase_endt, WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);
    }

    public int getRemainDay() {
        return TimeUtil.secToDay((getExpireDate() - System.currentTimeMillis()) / 1000);
    }

    public long getNextRenewalDate() {

        long purchasedTime = TimeUtil.getLongTime(purchase_stdt, WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(purchasedTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Calendar currentDay = Calendar.getInstance();
        currentDay.set(Calendar.HOUR_OF_DAY, 0);
        currentDay.set(Calendar.MINUTE, 0);
        currentDay.set(Calendar.SECOND, 0);


        long offsetTime = currentDay.getTimeInMillis() - calendar.getTimeInMillis();
        int rental = getRentalPeriod();
        long periods = (long)(offsetTime * 1d / (rental * 1d * 1000));
        return calendar.getTimeInMillis() + (periods + 1) * rental * 1000;
    }

    public int getCClass() {
        ArrayList<String> cclass = product.getTarget_devices();
        if (cclass.get(0).toUpperCase().equals("HANDHELD")) {
            return CCLASS_HANDHELD;
        }

        if (cclass.size() > 1) {
            return CCLASS_STB_HANDHELD;
        }

        return CCLASS_STB;
    }

    public boolean isPaymentTypeWallet() {
        return payment.getType().equals("wallet");
    }

    public String getPaymentMethod() {
        return payment.getTotal_amount().getCurrency();
    }

    public float getPaymentValue() {
        return payment.getTotal_amount().getValue();
    }

    public String getProductName() {
        return product.getTitle();
    }

    public String getCanceledDate() {
        return reserved_cancel_date;
    }

    public boolean isCanCelable() {
        if (auto_renewal == null) {
            return false;
        }


        return !auto_renewal.equalsIgnoreCase("N");
    }

    public boolean isCanceled() {
        if (auto_renewal == null) {
            return false;
        }
        return auto_renewal.equalsIgnoreCase("F");
    }

    public String getChangeStatus() {
        if(isCanceled()) {
            return "T";
        } else {
            return "F";
        }
    }

    public String getProductId() {
        return product.getId();
    }

    public int getRentalPeriod() {
        if (product != null) {
            return product.getRental_period();
        }

        return -1;
    }
}

