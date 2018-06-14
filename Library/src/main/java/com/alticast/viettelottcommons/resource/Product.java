package com.alticast.viettelottcommons.resource;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.resource.response.CheckCouponRes;
import com.alticast.viettelottcommons.resource.response.PurchaseListRes;
import com.alticast.viettelottcommons.util.NetworkUtil;
import com.alticast.viettelottcommons.util.WindDataConverter;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Product implements Parcelable {

    private static final String ONME_PRODUCT_STR = "ONME";

    public static final String NETWORK_TYPE_ALL = "all";
    public static final String NETWORK_TYPE_WIFI = "wifi";
    public static final String NETWORK_TYPE_3G = "third_generation";
    public static final String FREE = "FREE";
    public static final String SINGLE_ONLY = "SINGLE_ONLY";

    private static final int YES = 1;
    private static final int NO = 2;

    public static final String CURRENCY_VND = "VND";
    public static final String CURRENCY_PNT = "PNT";
    public static final String WALLET = "WALLET";
    public static final String PHONE_BALANCE = "PHONE_BALANCE";

    public static final String TYPE_SINGLE = "single";
    public static final String TYPE_SUBSCRIPTION = "subscription";
    public static final String TYPE_PACKAGE = "package";
    public static final String TYPE_FPACKAGE = "fpackage";

    public static final String CHROMECAST_SMALL = "CHROMECAST_SMALL";
    public static final String CHROMECAST_BIG = "CHROMECAST_BIG";
    public static final String ANDROID_STB = "ANDROID_STB";
    public static final String TV_SMART = "TV_SMART";
    public static final String STB = "STB";
    public static final String HANHELD = "HANDHELD";

//    public static final String[] CCLASS_HANDHELD = new String[]{"HANDHELD"};
//    public static final String[] CCLASS_STB = new String[]{"STB"};
//    public static final String[] CCLASS_STB_HANDHELD = new String[]{"STB", "HANDHELD"};
//    public static final String[] CCLASS_HANDHELD_STB = new String[]{"HANDHELD", "STB"};

    public static final String TYPE_PURCHASE = "PURCHASE_TYPE";
    public static final int TYPE_POINT = 0;
    public static final int TYPE_PRICE = 1;
    public static final int TYPE_PREPAID = 2;
    public static final int TYPE_PHONE = 3;

    public static final int NONE_PAYMENT = 0;
    public static final int PREPAID_ONLY = 2;
    public static final int PREPAID_BONUS = 6;
    public static final int POSTPAID_ONLY = 1;
    public static final int AVAILABLE_PREPAID = 2;
    public static final int AVAILABLE_BOUNS = 4;
    public static final int BOTH = 3;

    //###########################################################//TODO add################################
    public static final String TYPE_PAYMENT_POST = "postpaid";
    public static final String TYPE_PAYMENT_PRE = "prepaid";
    public static final String TYPE_PAYMENT_BONUS = "bonus";
    //###########################################################################################


    private int order = 0;
    private boolean purchased = false;
    private Promotion promotion = null;
    private String pclass;
    private String pid;
    private String[] cclass;
    private String cclassString;
    private ArrayList<String> target_devices;
    private ArrayList<String> payment_methods;
    private String id;
    private String type; /* single, subscription, package */
    private String license_start;
    private String license_end;
    private ArrayList<MultiLingualText> name = null;
    private ArrayList<MultiLingualText> description = null;
    private ArrayList<MultiLingualText> short_description = null;
    private String purchase_end;
    private boolean hidden = false;
    private boolean purchasable = false;
    private int preview_period;
    private int rental_period;
    private String currency;
    private ArrayList<Price> price;
    //    private float discount_rate;
    private String content_format; // ex. hd / sd / 3d
    private String parental_rating;
    private Location location;
    private ArrayList<Location> locations;
    private String updated_time;
    private PurchaseInfo purchase;
    private int previewPeriod;
    private String audioType;
    private String title;
    private String category;
    private boolean ispackage;
    private boolean has_subpackages;

    private String locator;
    private String network_type;
    private String parent_id;
    private boolean refund_prorated_daily;
    private String created_time;
    private ArrayList<RentalPeriods> rental_periods = null;

    private int freePriceState;
    private int freeRentalState;
    private int rentalPeriodState;
    private int flagState;
    private int purchasedState;
    private int purchasedDirectState;
    private int paymentOptionChose;
    private boolean exclusive;
    private String purchaseId;
//    private String couponCode;

    private CheckCouponRes checkCouponRes;

    private boolean isPointPurchaseable;

    public int maxScreenCurrent;

    public Product() {
    }

    public int getOrder() {
        return order;
    }

    public boolean isPurchased() {

        if(isOnMeProduct()) return false;

        if (purchasedState > 0) {
            return purchasedState == YES && purchasedDirectState == YES;
        }
        return purchased || (purchase != null && purchase.is_directly() && (isSingleProduct() ? !purchase.isExpired() : true));

//        return purchased || purchase != null;
    }

    public boolean isPointPurchaseable() {
        return isPointPurchaseable;
    }

    public void setPointPurchaseable(boolean pointPurchaseable) {
        isPointPurchaseable = pointPurchaseable;
    }

    public boolean isPurchasedNotDirect() {

        if(isOnMeProduct()) return false;

        if (purchasedState > 0) {
            return purchasedState == YES && purchasedDirectState == NO;
        }
        return purchased || (purchase != null && !purchase.is_directly());
    }

    public boolean isWifiProduct() {

        if(isOnMeProduct()) return false;

        if (isPurchasable()) return false;

        if (isHas_subpackages()) return false;

        if (getPurchase() == null) return false;

        if (getNetwork_type() == null) return false;

        if (!getNetwork_type().equals(Product.NETWORK_TYPE_WIFI)) return false;

        return true;
    }

    public int getPaymentOptionChose() {
        return paymentOptionChose;
    }

    public void setPaymentOptionChose(int paymentOptionChose) {
        this.paymentOptionChose = paymentOptionChose;
    }

    public String getCouponCode() {
        return checkCouponRes != null &&  checkCouponRes.getCode() != null ? checkCouponRes.getCode() : "";
    }
//
//    public void setCouponCode(String couponCode) {
//        this.couponCode = couponCode;
//    }

    public boolean checkSinglePurchased(NetworkUtil.NetworkType networkType) {


        if (!isSingleProduct()) {
            return false;
        }
        if (!WindmillConfiguration.isEnableRVOD) {
            return isPurchased() && AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3;
        }
        if (isPurchased()) {
            if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
                return true;
            } else {
                if ((purchasedDirectState > 0 && purchasedDirectState == YES) || (purchase != null && purchase.is_directly() && !purchase.isExpired())) {
                    return true;
                } else {
                    return networkType == NetworkUtil.NetworkType.WIFI;
                }
            }
        } else {
            return false;
        }
    }

    public int getMaxScreenCurrent() {
        return maxScreenCurrent;
    }

    public void setMaxScreenCurrent(int maxScreenCurrent) {
        this.maxScreenCurrent = maxScreenCurrent;
    }

    public ArrayList<RentalPeriods> getListRentalsUpdate() {

        if (maxScreenCurrent == 0) {
            return getRental_periods();
        }

        ArrayList<RentalPeriods> list = null;
        if (isRentalPeriodProduct()) {
            for (RentalPeriods rentalPeriods : getRental_periods()) {
                if (rentalPeriods.getMax_screen() > maxScreenCurrent) {
                    if (list == null) {
                        list = new ArrayList<>();
                    }

                    list.add(rentalPeriods);
                }
            }
        }

        return list;
    }

    public int getMaxScreenInPeriod() {
        if (isRentalPeriodProduct()) {
            int max = -1;
            for (RentalPeriods rentalPeriods : getRental_periods()) {
                if (rentalPeriods.getMax_screen() > max) {
                    max = rentalPeriods.getMax_screen();
                }
            }

            return max;
        } else {
            return -1;
        }
    }

    public boolean isHasPrice() {
        return price != null && !price.isEmpty();
    }

    public boolean isPurchased(boolean isHasWifiPackage, NetworkUtil.NetworkType networkType, boolean isIgnoreSystemPackage) {

        if(isOnMeProduct()) return false;

        if (isPurchased()) {
            // Neu pairing user, dc play
            if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
                return true;
            }
            // Neu SA user
            // Neu is_directly = true, dc play luon
            if (isPurchasable()) {
                return true;
            } else {
                if (!isIgnoreSystemPackage) {
                    // Wifi ok
                    if (isNetworkSystemPackagePlayable(networkType)) {
                        return isHasWifiPackage;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }

    }

    public boolean isSingleOnly() {

        if(isOnMeProduct()) return false;

        return exclusive && !purchasable;
    }

    public boolean isHas_subpackages() {
        return has_subpackages;
    }

    public void setHas_subpackages(boolean has_subpackages) {
        this.has_subpackages = has_subpackages;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public String getPclass() {
        return pclass;
    }

    public String getPid() {
        return pid;
    }

    public String getCclass() {
        if (cclass == null) {
            return "";
        }
        String cc = "";
        for (int i = 0, length = cclass.length; i < length; i++) {
            if (i == 0) {
                cc += cclass[i];
            } else {
                cc += "_" + cclass[i];
            }
        }
        return cc;
    }

    public ArrayList<String> getTarget_devices() {
        return target_devices;
    }

    public ArrayList<String> getPayment_methods() {
        return payment_methods;
    }

    public int getPaymentMethods() {
        if (payment_methods == null || payment_methods.size() == 0)
            return NONE_PAYMENT;
        int available = 0;
        for (int i = 0; i < payment_methods.size(); i++) {
            if (payment_methods.get(i).equalsIgnoreCase(TYPE_PAYMENT_BONUS)) {
                available = available | AVAILABLE_BOUNS;
            } else if (payment_methods.get(i).equalsIgnoreCase(TYPE_PAYMENT_PRE)) {
                available = available | AVAILABLE_PREPAID;
            } else if (payment_methods.get(i).equalsIgnoreCase(TYPE_PAYMENT_POST)) {
                available = available | POSTPAID_ONLY;
            }
        }
        return available;
    }

    public int getPaymentOptions() {
        if (payment_methods == null || payment_methods.size() == 0)
            return NONE_PAYMENT;
        int available = 0;
        for (int i = 0; i < payment_methods.size(); i++) {
            if (payment_methods.get(i).equalsIgnoreCase(TYPE_PAYMENT_PRE)) {
                available = available | PREPAID_ONLY;
            } else if (payment_methods.get(i).equalsIgnoreCase(TYPE_PAYMENT_POST)) {
                available = available | POSTPAID_ONLY;
            }
        }
        return available;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getLicense_start() {
        return license_start;
    }

    public String getLicense_end() {
        return license_end;
    }


    public String getName() {
        String nameStr = null;
        if (name != null && name.size() > 0) {
            for (int i = 0; i < name.size(); i++) {
                if (WindmillConfiguration.LANGUAGE.equals(name.get(i).getLang())) {
                    nameStr = name.get(i).getText();
                }
            }
            if (nameStr == null) {
                nameStr = name.get(0).getText();
            }
        }
        return nameStr;
    }

    public String getDescription() {
        String nameStr = null;
        if (description != null && description.size() > 0) {
            for (int i = 0; i < description.size(); i++) {
                if (WindmillConfiguration.LANGUAGE.equals(description.get(i).getLang())) {
                    nameStr = description.get(i).getText();
                }
            }
            if (nameStr == null) {
                nameStr = description.get(0).getText();
            }
        }
        return nameStr;
    }

    public boolean isPurchaseable() {

        if(isOnMeProduct()) return false;

        return !isHidden() && purchasable;
    }

    public String getShortDescription() {
        String nameStr = null;
        if (short_description != null && short_description.size() > 0) {
            for (int i = 0; i < short_description.size(); i++) {
                if (WindmillConfiguration.LANGUAGE.equals(short_description.get(i).getLang())) {
                    nameStr = short_description.get(i).getText();
                }
            }
            if (nameStr == null) {
                nameStr = short_description.get(0).getText();
            }
        }
        return nameStr;
    }

    public MultiLingualText getShort_description(String key) {
        return null;
    }

    public String getPurchase_end() {
        return purchase_end;
    }

    public boolean isHidden() {
        return hidden;
    }

    private boolean isPurchasable() {
        return purchasable;
    }

    public int getPreview_period() {
        return preview_period;
    }

    public int getRental_period() {
        return rental_period;
    }

    public String getCurrency() {
        return currency;
    }

    public ArrayList<Price> getPrice() {
        return price;
    }

    public Price getPrice(String type) {

        boolean isSAUser = AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2;

        if (price != null) {
            for (Price p : price) {
                if (isSAUser && !p.isSAPrice()) {
                    continue;
                }
                if (p.getCurrency().equals(type)) {
                    return p;
                }
            }
        }

        if (price != null) {
            for (Price p : price) {
                if (p.getCurrency().equals(type)) {
                    return p;
                }
            }
        }

        if (price != null) {
            for (Price p : price) {
                if (p.getValue() > 0) {
                    return p;
                }
            }
        }


        return null;
    }

    public String getNetwork_type() {
        return network_type;
    }

    public void setNetwork_type(String network_type) {
        this.network_type = network_type;
    }

    public float getPriceValue(String type) {
        Price price = getPrice(type);
        if (price == null) {
            return 0;
        } else {
            return price.getValue();
        }
    }

//    public float getDiscount_rate() {
//        return discount_rate;
//    }

    public String getContent_format() {
        return content_format;
    }

    public String getParental_rating() {
        return parental_rating;
    }

    public Location getLocation() {
        return location;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public PurchaseInfo getPurchase() {
        return purchase;
    }

    public int getPreviewPeriod() {
        return previewPeriod;
    }

    public String getAudioType() {
        return audioType;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public boolean ispackage() {
        return ispackage;
    }

    public String getParent_id() {
        return parent_id;
    }

    public boolean isRefund_prorated_daily() {
        return refund_prorated_daily;
    }

    public String getCreated_time() {
        return created_time;
    }

    public ArrayList<RentalPeriods> getRental_periods() {
        return rental_periods;
    }


    public boolean isHD() {
        return content_format.equalsIgnoreCase("hd");
    }

    private int getRating() {
        return Integer.parseInt(parental_rating);
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public String getPromotionId() {
        return promotion != null ? promotion.getId() : null;
    }


    public boolean isRentalPeriodProduct() {
        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) return false;
        if (isFpackage()) return true;

        if (rentalPeriodState > 0) {
            return rentalPeriodState == YES;
        }

        if (getRental_periods() != null && getRental_periods().size() != 0) return true;
        return false;
    }

    public boolean checkHasUpsellAvai(int maxScreen) {
        if (isRentalPeriodProduct()) {
            for (RentalPeriods rentalPeriods : rental_periods) {
                if (rentalPeriods.getMax_screen() > maxScreen) return true;
            }
        }

        return false;
    }

    public ArrayList<RentalPeriods> getUpSellRental(int maxScreen) {
        if (isRentalPeriodProduct()) {
            ArrayList<RentalPeriods> listResult = null;
            for (RentalPeriods rentalPeriods : rental_periods) {
                if (rentalPeriods.getMax_screen() > maxScreen) {
                    if (listResult == null) {
                        listResult = new ArrayList<>();
                    }

                    listResult.add(rentalPeriods);
                }
            }

            return listResult;
        }

        return null;
    }


    public boolean isFreeRentalPeriod() {

        if(isOnMeProduct()) return false;

        boolean isFree = false;
        if (rental_periods == null) {
            return false;
        }

        if (freeRentalState > 0) {
            return freeRentalState == YES;
        }

        boolean isPairingUer = AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3;
        for (int i = 0; i < rental_periods.size(); i++) {

            Price vndPriceObj = rental_periods.get(i).getPrice(Product.CURRENCY_VND);
            Price pntPriceObj = rental_periods.get(i).getPrice(Product.CURRENCY_PNT);

            float vndPrice = vndPriceObj != null ? vndPriceObj.getValue() : 0;
            float pntPrice = pntPriceObj != null ? pntPriceObj.getValue() : 0;
            if (!isPairingUer) {
                if (vndPrice <= 0) {
                    isFree = true;
                    break;
                }
            } else if (isPairingUer) {
                isFree = true;
                if (pntPrice < 0) {
                    if (vndPrice <= 0) {
                        isFree = true;
                        break;
                    }
                }
            }
        }
        return isFree;
    }

    public boolean isNetworkSystemPackagePlayable(NetworkUtil.NetworkType networkType) {

        if(isOnMeProduct()) return false;

        if (!WindmillConfiguration.is3GQuetoiVersion) {
            return networkType == NetworkUtil.NetworkType.WIFI;
        }
        if (network_type == null || network_type.equals(NETWORK_TYPE_ALL)) {
            return true;
        }
        ;
//        if(network_type == null) {
//            return networkType.equals(NETWORK_TYPE_WIFI);
//        };
//        if(network_type.equals(NETWORK_TYPE_ALL)) {
//            return true;
//        };

        switch (networkType) {
            case WIFI:
                return network_type.equals(NETWORK_TYPE_WIFI);
            case MOBILE:
                return network_type.equals(NETWORK_TYPE_3G);
            default:
                return false;
        }
    }

    public boolean isFreePrice() {

        if(isOnMeProduct()) return false;

        if (freePriceState > 0) {
            return freePriceState == YES;
        }


        if (AuthManager.currentLevel() != AuthManager.UserLevel.LEVEL3) {
            return getPriceValue(Product.CURRENCY_VND) <= 0;
        }
        float vndPrice = getPriceValue(Product.CURRENCY_VND);
        float pntPrice = getPriceValue(Product.CURRENCY_PNT);

//        if (pntPrice == 0 || vndPrice == 0) {
//            return true;
//        }
//        boolean isPointUser = HandheldAuthorization.getInstance().isPointUser();
        if (pntPrice == 0 && isPointPurchaseable) {
            return true;
        }
        if (vndPrice == 0 && !isPointPurchaseable) {
            return true;
        }


        if (pntPrice < 0) return vndPrice <= 0;
        return false;
        // return getPrice(Product.CURRENCY_VND) <= 0 && getPrice(Product.CURRENCY_PNT) <= 0;
    }

    public boolean isFree() {

        if(isOnMeProduct()) return false;

        if(!purchasable) return false;

        if (isRentalPeriodProduct()) {//check Rental Period
            return isFreeRentalPeriod();
        } else {
            return isFreePrice();
        }
    }


    public float getDiscountRatio() {

        if (!isHasPromotion()) {
            return 0f;
        }

        if (promotionState > 0) {
            return discount_rate != 0 ? discount_rate / 100f : 0f;
        }

        return promotion != null ? promotion.getDiscount_rate() / 100f : 0f;

//        return promotion != null ? promotion.getDiscount_rate() / 100f : 0;
    }

    public String getDiscountPercent() {
        return promotion != null ? Integer.toString((int) promotion.getDiscount_rate()) + "%" : null;
    }

    public String getLocator() {
        if (locator == null || locator.length() == 0) {
            if (findLocation() != null && findLocation().getLocator() != null) {
                locator = findLocation().getLocator();
            }
        }
        return locator;
    }

    public String getLocatorChromeCast() {
        if (locator == null || locator.length() == 0) {
            if (findLocationChromecast() != null && findLocationChromecast().getLocator() != null) {
                locator = findLocationChromecast().getLocator();
            }
        }
        return locator;
    }

    public boolean isFpackage() {
        return type.toUpperCase().equals("FPACKAGE");//cclass.startsWith(Product.CCLASS_FPACKAGE);
    }

    public boolean isSubscription() {
        return type.toUpperCase().equals("SUBSCRIPTION");
    }

    public boolean isBundleProduct() {
        return pclass != null && pclass.toLowerCase().startsWith("package");
    }

    public boolean isWifiPackage() {
        return !purchasable && purchase != null;
    }


    public static final int RENTAL_DAY = 1;
    public static final int RENTAL_WEEK = 7;
    public static final int RENTAL_MONTH = 30;

    public static final String UNIT_DAY = "D";
    public static final String UNIT_MONTH = "M";

    public RentalPeriods getRentalInfo(int type) {
        if (rental_periods == null) return null;
        for (RentalPeriods rentalPeriod : rental_periods) {
            if (rentalPeriod.getPeriod() == type && rentalPeriod.getUnit().toUpperCase().equals(UNIT_DAY)) {
                return rentalPeriod;
            }
            if (type == RENTAL_MONTH) {
                if (rentalPeriod.getPeriod() == type / 30 && rentalPeriod.getUnit().equals(UNIT_MONTH)) {
                    return rentalPeriod;
                }
            }
        }
        return null;
    }

    public RentalPeriods getOneMonthRentalInfo() {
        for (RentalPeriods rentalPeriod : rental_periods) {
            if (rentalPeriod.getPeriod() == 30 && rentalPeriod.getUnit().equals("D")) {
                return rentalPeriod;
            }

            if (rentalPeriod.getPeriod() == 1 && rentalPeriod.getUnit().equals("M")) {
                return rentalPeriod;
            }
        }
        return null;
    }

    public HashMap<String, Integer> getRentalDatas() {
        HashMap<String, Integer> map = new HashMap<>();
        for (RentalPeriods rentalPeriod : rental_periods) {
            StringBuffer unit = new StringBuffer();
            unit.append(rentalPeriod.getPeriod());
//            unit.append(rentalPeriod.getUnit() == UNIT_DAY ? R.string.pay_unit_day : R.string.pay_unit_month);
            int normalPrice = getFpackageFinalPrice(rentalPeriod, Product.CURRENCY_VND);
            map.put(unit.toString(), normalPrice);
        }
        return map;
    }

    public int getFinalPrice(String type) {
        Price price = getPrice(type);
//        if (price == null) {
//            return 0;
//        }
//        float originalPrice = price.getValue();
//        String promotionId = getPromotionId();
//        if (promotionId != null && !(originalPrice < 0)) {
//            int minimumCurrency = 500;
//            return (int) Math.ceil(originalPrice * (1f - getDiscountRatio()) / minimumCurrency) * minimumCurrency;
//        }
//        return (int) originalPrice;


        if(price == null) {
            return 0;
        }

        float originalPrice = price.getValue();
        String promotionId = getPromotionId();
        float promoAmount = 0;
        if (promotionId != null && !(originalPrice < 0)) {

            promoAmount = originalPrice * getDiscountRatio();

//            return (int) Math.ceil(originalPrice * (1f - getDiscountRatio()) / minimumCurrency) * minimumCurrency;
//            return (int) Math.ceil(originalPrice * (1f - getDiscountRatio()) / minimumCurrency) * minimumCurrency;
        }

        if(checkCouponRes != null) {
            if(checkCouponRes.getDiscount_method().equals(CheckCouponRes.METHOD_RATE)) {
                promoAmount += originalPrice * (checkCouponRes.getDiscount_value() * 1f / 100);
            } else {
                promoAmount += checkCouponRes.getDiscount_value();
            }
        }

        if(promoAmount <= 0) {
            return (int) originalPrice;
        }

        if(promoAmount >= originalPrice) {
            return 0;
        }

        int promotionPrice = (int)(originalPrice - promoAmount);

//        if(promotionPrice < 500) return 500;

        return promotionPrice;
    }

    public int getFpackageFinalPrice(RentalPeriods rentalPeriod, String type) {

        Price price = rentalPeriod.getPrice(type);

        if (price == null) {
            return 0;
        }

        float originalPrice = price.getValue();
        String promotionId = getPromotionId();
        float promoAmount = 0;
        if (promotionId != null && !(originalPrice < 0)) {

            promoAmount = originalPrice * getDiscountRatio();

//            return (int) Math.ceil(originalPrice * (1f - getDiscountRatio()) / minimumCurrency) * minimumCurrency;
//            return (int) Math.ceil(originalPrice * (1f - getDiscountRatio()) / minimumCurrency) * minimumCurrency;
        }

        if(checkCouponRes != null) {
            if(checkCouponRes.getDiscount_method().equals(CheckCouponRes.METHOD_RATE)) {
                promoAmount += originalPrice * (checkCouponRes.getDiscount_value() * 1f / 100);
            } else {
                promoAmount += checkCouponRes.getDiscount_value();
            }
        }

        if(promoAmount <= 0) {
            return (int) originalPrice;
        }

        if(promoAmount >= originalPrice) {
            return 0;
        }

        int promotionPrice = (int)(originalPrice - promoAmount);

//        if(promotionPrice < 500) return 500;

        return promotionPrice;

//        return (int) originalPrice;
    }

    public boolean isFlag() {

        if(isOnMeProduct()) return false;

        if (AuthManager.currentLevel() != AuthManager.UserLevel.LEVEL2) {
            return false;
        }

        if (flagState > 0) {
            return flagState == YES;
        }

        if (isHandheldCclass()) {
            return location != null && location.isFlag();
        }

        if (getLocations() == null) {
            return false;
        }
        for (Location location : getLocations()) {
            if (location.isFlag()) {
                return true;
            }
        }
//        if (getLocations() != null && getLocations().size() > 0) {
//            Parameter parameter = getLocations().get(0).getParameter(AUDIENCE);
//            return parameter == null ? false : parameter.getValue().equalsIgnoreCase("private:All");
//        }
        return false;

    }

    public boolean isHasPromotion() {
        if (promotionState > 0) {
            return promotionState == YES;
        }

        return promotion != null;
    }

//    public int getDiscoutRate() {
//
//        if(!isHasPromotion()) {
//            return 100;
//        }
//
//        if(promotionState > 0) {
//            return discount_rate != 0 ? discount_rate : 100;
//        }
//
//        return promotion != null ? promotion.getDiscount_rate() : 100;
//    }

    public int discount_rate;
    public int promotionState;

    @Override
    public int describeContents() {
        return 0;
    }

    public void updateLocator() {
        if (findLocation() != null && findLocation().getLocator() != null) {
            this.locator = findLocation().getLocator();
        }
    }

    protected Product(Parcel in) {
        id = in.readString();
        network_type = in.readString();
        hidden = in.readInt() == 1 ? true : false;
        purchasable = in.readInt() == 1 ? true : false;
        purchasedState = in.readInt();
        purchaseId = in.readString();
        flagState = in.readInt();
        rentalPeriodState = in.readInt();
        freePriceState = in.readInt();
        freeRentalState = in.readInt();
        purchasedDirectState = in.readInt();
        promotionState = in.readInt();
        discount_rate = in.readInt();
        type = in.readString();
        locator = in.readString();
        cclass = in.readString().split(Pattern.quote("_"));
        exclusive = in.readInt() == 1 ? true : false;
//        purchase = in.readParcelable(PurchaseInfo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(network_type);
        dest.writeInt(hidden ? 1 : 0);
        dest.writeInt(purchasable ? 1 : 0);
        dest.writeInt(isPurchased() ? YES : NO);
        if (purchaseId != null) {
            dest.writeString(purchaseId);
        } else {
            dest.writeString(isPurchased() && purchase != null ? purchase.getId() : id);
        }

        dest.writeInt(isFlag() ? YES : NO);
        dest.writeInt(isRentalPeriodProduct() ? YES : NO);
        dest.writeInt(isFreePrice() ? YES : NO);
        dest.writeInt(isFreeRentalPeriod() ? YES : NO);
        dest.writeInt(purchase != null && purchase.is_directly() ? YES : NO);
//        dest.writeInt(purchase != null ? YES : NO);
        dest.writeInt(promotion != null ? YES : NO);
        dest.writeInt(discount_rate);
        dest.writeString(type);
        if (findLocation() != null && findLocation().getLocator() != null) {
            dest.writeString(findLocation().getLocator());
        } else {
            dest.writeString("");
        }
        dest.writeString(getCclass());
        dest.writeInt(isExclusive() ? 1 : 0);

//        if(purchase != null) {
//            dest.writeParcelable(purchase, 0);
//        }
//        else {
//            dest.writeParcelable(null, 0);
//        }
    }

    private Location findLocation() {


        if (isHandheldCclass()) {
            return location;
        }

        if (locations == null || locations.isEmpty()) {
            return null;
        }

        for (Location location : locations) {
            if (location.getDevice().contains("handheld")) {
                return location;
            }
        }

        return null;
    }

    private Location findLocationChromecast() {


        if (isHandheldCclass()) {
            return location;
        }

        if (locations == null || locations.isEmpty()) {
            return null;
        }

        for (Location location : locations) {
            if (location.getDevice().contains("chromecast")) {
                return location;
            }
        }

        return null;
    }

    @SuppressWarnings("unused")
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };


    public boolean isSTBCclass() {

        if(isOnMeProduct()) return false;

        if (cclass == null) return false;

        if (cclassString == null || cclassString.isEmpty()) {
            cclassString = Arrays.toString(cclass);
            cclassString = cclassString.substring(1, cclassString.length() - 1);
        }
//        if(!WindmillConfiguration.isBigSmallPackageVersion) {
//            return cclassString.equals(STB);
//        } else {
//            return cclassString.contains(STB) && !cclassString.contains(HANHELD);
//        }
        return cclassString.contains(STB) && !cclassString.contains(HANHELD);
    }

    public boolean isHandheldCclass() {

        if(isOnMeProduct()) return false;

        if (cclass == null) return false;

        if (cclassString == null || cclassString.isEmpty()) {
            cclassString = Arrays.toString(cclass);
            cclassString = cclassString.substring(1, cclassString.length() - 1);
        }
//        if(!WindmillConfiguration.isBigSmallPackageVersion) {
//            return cclassString.equals(HANHELD);
//        } else {
//            return cclassString.contains(HANHELD) && !cclassString.contains(STB);
//        }
        return cclassString.contains(HANHELD) && !cclassString.contains(STB);
    }

    public boolean isHandheldSTBCclass() {

        if(isOnMeProduct()) return false;

        if (cclass == null) return false;

        if (cclassString == null || cclassString.isEmpty()) {
            cclassString = Arrays.toString(cclass);
            cclassString = cclassString.substring(1, cclassString.length() - 1);
        }
        return cclassString.contains(HANHELD) && cclassString.contains(STB);
    }

    public boolean isSmallProduct() {

        if(isOnMeProduct()) return false;

        if (cclass == null) return false;

        if (!isPurchaseable() || !isFpackage()) {
            return false;
        }

        if (isHandheldCclass()) {
            return true;
        }

        if (cclassString.contains(CHROMECAST_SMALL)) {
            return true;
        }


        return false;
    }

    public boolean isOnMeProduct() {
        return pid != null && pid.contains(ONME_PRODUCT_STR);
    }

    public boolean isBigProduct() {

        if(isOnMeProduct()) return false;

        if (cclass == null) return false;

        if (!isPurchaseable() || !isFpackage()) {
            return false;
        }

        if (isHandheldCclass()) {
            return false;
        }

        if (cclassString.contains(CHROMECAST_SMALL)) {
            return false;
        }

        if (cclassString.contains(CHROMECAST_BIG) || cclassString.contains(ANDROID_STB) || cclassString.contains(TV_SMART)) {
            return true;
        }

        return false;
    }


    public boolean isSingleProduct() {

        if(isOnMeProduct()) return false;

        return type != null && type.equals(TYPE_SINGLE);
    }

    public boolean isSubscriptionProduct() {
        return type != null && type.equals(TYPE_SUBSCRIPTION);
    }

    public boolean isPackageProduct() {
        return type != null && type.equals(TYPE_PACKAGE);
    }

    public boolean isFPackageProduct() {
        return type != null && type.equals(TYPE_FPACKAGE);
    }

    public String getPurchaseId() {
        if (purchase != null)
            return purchase.getId();

        return purchaseId != null ? purchaseId : id;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public void setRental_periods(ArrayList<RentalPeriods> rental_periods) {
        this.rental_periods = rental_periods;
    }

    public void checkPurchasedInfo(PurchaseListRes purchaseListRes, boolean isRental) {
        if (isRental && (rental_periods == null || rental_periods.isEmpty())) return;

        if (purchaseListRes.getData() == null) return;

        Purchase purchaseObject = purchaseListRes.getPurchase(id);

        if (purchaseObject == null) return;

        if (isRental) {
            int rentalPeriod = purchaseObject.getProduct().getRental_period();
            for (RentalPeriods rp : rental_periods) {
                if (rp.getPeriod() * 24 * 60 * 60 == rentalPeriod) {
                    rp.setPurchased(true);
                }
            }
        } else {
            if (purchase == null) {
//                SimpleDateFormat sdf = new SimpleDateFormat(WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);
                purchase = new PurchaseInfo();
                purchase.setId(purchaseObject.getId());
                purchase.setDate(purchaseObject.getPurchase_stdt());
                purchase.setIs_directly(true);
            } else {
                purchase.setId(purchaseObject.getId());
                purchase.setDate(purchaseObject.getPurchase_stdt());
                purchase.setIs_directly(true);
            }
        }
    }

    public boolean isHasPriodPurchased() {
        if (rental_periods == null || rental_periods.isEmpty()) return false;

        for (RentalPeriods rp : rental_periods) {
            if (rp.isPurchased()) return true;
        }

        return false;
    }

    public RentalPeriods getPeriodPuchased(PurchaseListRes purchaseListRes) {
        if (rental_periods == null || rental_periods.isEmpty()) return null;

        if (purchaseListRes.getData() == null) return null;

        Purchase purchaseObject = purchaseListRes.getPurchase(id);

        if (purchaseObject == null) return null;

        int rentalPeriod = purchaseObject.getProduct().getRental_period();
        for (RentalPeriods rp : rental_periods) {
            if (rp.getPeriod() * 24 * 60 * 60 == rentalPeriod) {
                return rp;
            }
        }
        return null;
    }

    public String getNextRenewalString(RentalPeriods item) {
        if (purchase == null) {
            return "N/A";
        }
        if (item == null) {
            return "N/A";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);
        try {
            Date date = sdf.parse(purchase.getDate());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, item.getPeriod());

            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");

            return sdf2.format(calendar.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "N/A";
    }

    public String getDurationString(Context context) {
        if (isSingleProduct()) {
            int hour = rental_period / 3600;

            return hour + " " + context.getResources().getString(R.string.justHour);

//            if(hour < 24) {
//                return hour + " " + context.getResources().getString(R.string.justHour);
//            }
//
//            int days = hour / 24;
//            int hourLeft = hour % 24;
//            return days + " " + context.getResources().getString(R.string.justDay) + ( hourLeft > 0 ? " " + hourLeft + " " + context.getResources().getString(R.string.justHour) : "");
        }

        switch (rental_period) {
            case 0:
                return "1 " + context.getResources().getString(R.string.justMothn);
            case 1:
                return "1 " + context.getResources().getString(R.string.justDay);
            case 7:
                return "1 " + context.getResources().getString(R.string.justWeek);
            case 30:
                return "1 " + context.getResources().getString(R.string.justMothn);
            default:
                return rental_period + " " + context.getResources().getString(R.string.justDay);
        }
    }

    public boolean isSVODProduct() {

        if(isOnMeProduct()) return false;

        if (isExclusive() && isPurchaseable()) {
            if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2 && (!isRentalPeriodProduct() || !isHandheldCclass())) {
                return false;
            }
            if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3 && (getPrice() == null || isSTBCclass())) {
                return false;
            }
            return true;
        }

        return false;
    }

    public CheckCouponRes getCheckCouponRes() {
        return checkCouponRes;
    }

    public void setCheckCouponRes(CheckCouponRes checkCouponRes) {
        this.checkCouponRes = checkCouponRes;
    }

    public int getRateResource() {

        int rating = Integer.parseInt(parental_rating);

        if (18 <= rating) {
            return R.drawable.icon_detail_age_18;
        } else if (15 <= rating) {
            return R.drawable.icon_detail_age_16;
        } else if (12 <= rating) {
            return R.drawable.icon_detail_age_13;
        } else if (7 <= rating) {
            return R.drawable.icon_detail_age_7;
        } else if (0 <= rating) {
            return R.drawable.icon_detail_age_all;
        } else {
            return -1;
        }
    }

    public String getImgAgeSourceName() {

        int rating = Integer.parseInt(parental_rating);

        if (18 <= rating) {
            return "icon_detail_age_18";
        } else if (15 <= rating) {
            return "icon_detail_age_16";
        } else if (12 <= rating) {
            return "icon_detail_age_13";
        } else if (7 <= rating) {
            return "icon_detail_age_7";
        } else if (0 <= rating) {
            return "icon_detail_age_all";
        } else {
            return "";
        }
    }

    public void sortRental() {
        if (rental_periods == null || rental_periods.isEmpty()) return;

        if (rental_periods.size() == 1) return;

        Collections.sort(rental_periods);

    }
}