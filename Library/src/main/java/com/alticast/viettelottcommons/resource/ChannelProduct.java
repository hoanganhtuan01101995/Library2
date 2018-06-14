package com.alticast.viettelottcommons.resource;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.ChannelManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.NetworkUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by mc.kim on 8/10/2016.
 */
public class ChannelProduct implements Parcelable {
    public static final int TYPE_NORMAL = 0x01;
    public static final int TYPE_GROUP_PROMOTION = 0x90;
    public static final int TYPE_SINGLE_PROMOTION = 0x91;
    public static final int TYPE_PPV = 0x92;
    public static final int TYPE_PPD = 0x93;
    public static final int TYPE_UNSUBSCRIBE = 0x94;

    public static final String PRODUCT_TYPE_SINGLE = "single";
    public static final String PRODUCT_TYPE_PACKAGE = "package";
    public static final String PRODUCT_TYPE_SUBSCIPTION = "subscription";

    //TODO [StandAlone add]###################################################################################
    public static final String PRODUCT_TYPE_FPACKAGE = "fpackage";
    //########################################################################################################


    private String pid = null;
    private String id = null;
    private String purchaseId = null;
    private int type;
    private int number = 0;
    private String service_id = null;
    private String service_type = null;
    private ArrayList<Product> product = null;
    private Channel channel = null;
    private boolean encryption = false;
    private boolean visible = false;
    private ArrayList<Config> config = null;

    public ChannelProduct() {

    }


    public void setProduct(ArrayList<Product> product) {
        this.product = product;
    }

    public String getPid() {
        return pid;
    }

    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    public String getService_id() {
        return service_id;
    }

    public String getService_type() {
        return service_type;
    }

    public ArrayList<Product> getProduct() {

        return product;
    }

    public Channel getChannel() {
        return channel;
    }

    public boolean isEncryption() {
        return encryption;
    }

    public ArrayList<Config> getConfig() {
        return config;
    }


    public boolean isFree() {
        if (encryption) {
            return false;
        }

        if (config == null) {
            return false;
        }

        for (Config cf : config) {
            if (cf.getName().equals("Public")) {
                if (cf.getValue().equals("1")) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getChannelNumber() {
        String numberStr = null;
        StringBuffer buffer = new StringBuffer();
        if (number >= 0) {
            if (number < 10) {
                buffer.append("00").append(number);
            } else if (number < 100) {
                buffer.append("0").append(number);
            } else {
                buffer.append(number);
            }
        }
        numberStr = buffer.toString();
        return numberStr;
    }

    public boolean isFavorite() {
        if (HandheldAuthorization.getInstance().isLogIn()) {
            return ChannelManager.getInstance().contains(id);
        }
        return false;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean shouldPurchase() {
        if (!hasProduct())
            return true;

        Product singleProduct = getProduct(PRODUCT_TYPE_SINGLE);
        if (singleProduct != null && (singleProduct.isPurchased() || singleProduct.isFree()))
            return false;

        Product packageProduct = getProduct(PRODUCT_TYPE_PACKAGE);
        if (packageProduct != null && (packageProduct.isPurchased() || packageProduct.isFree()))
            return false;

        Product subscriptionProduct = getProduct(PRODUCT_TYPE_SUBSCIPTION);
        if (subscriptionProduct != null && (subscriptionProduct.isPurchased() || subscriptionProduct.isFree()))
            return false;
        return true;
    }

    public boolean hasProduct() {
        if (product == null)
            return false;
        return getProduct(PRODUCT_TYPE_SINGLE) != null || getProduct(PRODUCT_TYPE_PACKAGE) != null || getProduct(PRODUCT_TYPE_SUBSCIPTION) != null;
    }

    public Product getProduct(String type) {
        if (product == null)
            return null;

        for (Product mProduct : product) {
            if (mProduct.getType().toLowerCase().equals(type))
                return mProduct;
        }
        return null;
    }

    public ArrayList<Product> checkSystemProduct() {
        if (product == null) return null;
        ArrayList<Product> list = null;
        for (Product p : product) {

//            if(p.isHas_subpackages()) {
//                if(list == null) {
//                    list = new ArrayList<>();
//                }
//                list.add(p);
//            } else {
//                if(!p.isPurchaseable() && p.isPurchased()) {
//                    if(list == null) {
//                        list = new ArrayList<>();
//                    }
//                    list.add(p);
//                }
//            }

            if (!p.isPurchaseable() && p.isPurchased()) {
                if(list == null) {
                    list = new ArrayList<>();
                }
                list.add(p);
            }
        }

        return list;
    }

//    public static boolean isNetworkSystemPackagePlayable(ArrayList<Product> listProduct, NetworkUtil.NetworkType networkType) {
//        if(listProduct == null || listProduct.isEmpty()) return false;
//
//        for(Product product : listProduct) {
//            if(product.isNetworkSystemPackagePlayable(networkType)) {
//                return true;
//            }
//        }
//
//        return  false;
//    }
    public static boolean isNetworkSystemPackagePlayable(ArrayList<Product> listProduct, NetworkUtil.NetworkType networkType, boolean isVtvCab) {
        if(listProduct == null || listProduct.isEmpty()) return false;

        for(Product product : listProduct) {
            if(product.isNetworkSystemPackagePlayable(networkType)) {
                if(!isVtvCab) {
                    return true;
                } else {
                    if(product.getNetwork_type() == null || product.getNetwork_type().equals("all")) {
                        return true;
                    }
                }
            }
        }

        return  false;
    }

    public ArrayList<Product> getHandheldAvailableProducts() {
        if (product == null) {
            return null;
        }
        ArrayList<Product> list = new ArrayList<>();
        for (Product product : this.product) {
            if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
                if (product.isHandheldCclass() && !product.isSingleProduct() || (product.isBigProduct())) {
                    list.add(product);
                }
            } else if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
                if (product.isHandheldSTBCclass() && !product.isSingleProduct()) {
                    list.add(product);
                }
//                if ((product.isHandheldCclass() || product.isHandheldSTBCclass()) && !product.isSingleProduct()) {
//                    list.add(product);
//                }
            }

        }

        return list.size() > 0 ? list : null;
    }

    public ArrayList<Product> getPurchaseableProducts(ArrayList<Product> listSource) {
        if (listSource == null) {
            return null;
        }
        ArrayList<Product> list = new ArrayList<>();
        for (Product product : listSource) {
            if (product.isPurchaseable()) {
                list.add(product);
            }
        }

        return list;
    }


    public ArrayList<Product> getPackageProducts() {
        Logger.print("program", "called getPackageProducts()");
        if (product == null) return null;
        ArrayList<Product> packageProduct = new ArrayList<>();
        for (Product mProduct : product) {
            Logger.print("program", "########################################");
            if (!mProduct.getType().toLowerCase().equals("single")/*product.getType().toLowerCase().equals("subscription") || product.getType().toLowerCase().equals("package")*/
                    && mProduct.isPurchaseable()) {
                if (mProduct.isFpackage()) {
                    mProduct = checkAvailableFpackage(mProduct);
                } else {
                    mProduct = checkAvailable(mProduct);
                }
                if (mProduct != null)
                    packageProduct.add(mProduct);
            }
        }
        Logger.print("program", "called getPackageProducts() : " + packageProduct.size());
        return packageProduct.size() == 0 ? null : packageProduct;
    }


    public boolean isPackageProductFree(/*Product[] products*/) {
        if (product == null || product.size() == 0) return false;
        for (int i = 0; i < product.size(); i++) {
            Product mProduct = product.get(i);
            if (mProduct.isFree()) return true;
        }
        return false;
    }

    public boolean isPackageProductPurchased(/*Product[] products*/) {
        if (product == null || product.size() == 0) return false;
        for (int i = 0; i < product.size(); i++) {
            Product mProduct = product.get(i);
            if (mProduct.isPurchased()) {
                return true;
            }
        }
        return false;
    }
//
//    public ArrayList<Product> getPurchaseableProducts(ArrayList<Product> listSource) {
//        if(listSource == null) {
//            return null;
//        }
//        ArrayList<Product> list = new ArrayList<>();
//        for(Product product : listSource) {
//            if(product.isPurchaseable()) {
//                list.add(product);
//            }
//        }
//
//        return list;
//    }


    public int getCatchUpDuration() {
        String value = null;
        ArrayList<CustomValue> channelConfig = channel.getConfig();

        int size = channelConfig.size();
        for (int i = 0; i < size; i++) {
            if (channelConfig.get(i).getName().equals("IsCatchUp")) {
                value = channelConfig.get(i).getValue();
            }
        }
        if (value != null) {

            if (String.valueOf(Boolean.TRUE).equalsIgnoreCase(value)) {
                return 7;
            } else if (String.valueOf(Boolean.FALSE).equalsIgnoreCase(value)) {
                return 0;
            } else {
                return Integer.parseInt(value);
            }

        } else {
            return 0;
        }
    }


    public boolean isTimeShift() {
        ArrayList<CustomValue> channelConfig = channel.getConfig();
        if (channelConfig == null) {
            return false;
        }

        int size = channelConfig.size();


        for (int i = 0; i < size; i++) {
            if (channelConfig.get(i).getName().equalsIgnoreCase("IsTimeShift")) {
                return channelConfig.get(i).getValue().equalsIgnoreCase("true");
            }
        }


        return false;
    }


    public boolean isIsLiveRecord() {
        ArrayList<CustomValue> channelConfig = channel.getConfig();
        if (channelConfig == null) {
            return false;
        }

        int size = channelConfig.size();


        for (int i = 0; i < size; i++) {
            if (channelConfig.get(i).getName().equalsIgnoreCase("IsLiveRecord")) {
                return channelConfig.get(i).getValue().equalsIgnoreCase("true");
            }
        }


        return false;
    }

    public Product checkAvailableFpackage(Product product) {
        if (product.isFPackageProduct() && product.isHandheldSTBCclass()) {
            if (product != null) {
                if (product.isPurchased() || product.isFree() || product.isPurchaseable())
                    return product;
            }
        }
        if (product.isFPackageProduct() && product.isHandheldCclass()) {
            if (product != null) {
                //if (product.getPurchasable().equals("true"))
                if (product.isPurchased() || product.isFree() || product.isPurchaseable())
                    return product;
            }
        }
        return null;
    }


    /**
     * purchasable T/F 와 상관없이 구매하거나 , 무료인 경우
     * purchasable T 인 경우.
     */
    public Product checkAvailable(Product product) {
        AuthManager.UserLevel level = AuthManager.currentLevel();
        boolean isStandAloneUser = level == AuthManager.UserLevel.LEVEL3 ? false : true;
        /**subscription 상품 구매하거나 free인 경우*/
        if (!isStandAloneUser && product.isSubscriptionProduct() && product.isHandheldSTBCclass()) {
            if (product.isPurchased())
                return product;
//            if ("true".equals(product.getPurchasable()) && product.isFree())
            if (product.isFree() /*|| "true".equals(product.getPurchasable())*/)
                return product;
        }

        if (product.isSubscriptionProduct() && product.isHandheldCclass()) {
            if (product.isPurchased()) return product;
//            if ("true".equals(product.getPurchasable()) && product.isFree())
            if (product.isFree()/* || "true".equals(product.getPurchasable())*/)
                return product;
        }

        if (!isStandAloneUser && product.isSubscriptionProduct() && product.isHandheldSTBCclass()) {
            if (product.isPurchaseable()) {
                return product;
            }
        }

        if (product.isSubscriptionProduct() && product.isHandheldCclass()) {
            if (product.isPurchaseable()) {
                return product;
            }
        }


        if (!isStandAloneUser && product.isPackageProduct() && product.isHandheldSTBCclass()) {
            if (product.isPurchased()) return product;
            if (/*"true".equals(product.getPurchasable()) &&*/ product.isFree()) return product;
        }

        if (product.isPackageProduct() && product.isHandheldCclass()) {
            if (product.isPurchased()) return product;
            if (/*"true".equals(product.getPurchasable()) && */product.isFree()) return product;
        }

        if (!isStandAloneUser && product.isPackageProduct() && product.isHandheldSTBCclass()) {
            if (product.isPurchaseable()) {
                return product;
            }
        }

        if (product.isPackageProduct() && product.isHandheldCclass()) {
            if (product.isPurchaseable()) {
                return product;
            }
        }
        return null;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    protected ChannelProduct(Parcel in) {
        id = in.readString();
        purchaseId = in.readString();
        type = in.readInt();
        service_id = in.readString();
        number = in.readInt();
        Parcelable[] parcelables = in.readParcelableArray(Product.class.getClassLoader());
        product = new ArrayList<>();
        Collections.addAll(product, Arrays.copyOf(parcelables, parcelables.length, Product[].class));
        encryption = in.readInt() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(purchaseId);
        dest.writeInt(type);
        dest.writeString(service_id);
        dest.writeInt(number);
        if (product == null)
            product = new ArrayList<>();
        dest.writeParcelableArray(product.toArray(new Product[product.size()]), 0);
        dest.writeInt(encryption ? 1 : 0);
    }

    @SuppressWarnings("unused")
    public static final Creator<ChannelProduct> CREATOR = new Creator<ChannelProduct>() {
        @Override
        public ChannelProduct createFromParcel(Parcel in) {
            return new ChannelProduct(in);
        }

        @Override
        public ChannelProduct[] newArray(int size) {
            return new ChannelProduct[size];
        }
    };

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }
}
