package com.alticast.viettelottcommons.resource;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.NetworkUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by mc.kim on 7/28/2016.
 */
public class Vod implements Parcelable {
    private long timeOffset = 0;

    public static final String TYPE_SINGLE = "single";
    private int purchases = 0;
    private String id = null;
    private ArrayList<Product> product = null;
    private Program program = null;
    private Path path = null;
    private String purchaseId;

    private String menuPath;

    private boolean isExpand;


    private String watchedID;

    public boolean isResumeVod;
    public boolean isRelatedVod;
    public boolean isSearchVod;
    public boolean isPurchased;

    public boolean isFromResumingFragment;

    public boolean isRelatedVod() {
        return isRelatedVod;
    }

    public boolean isSearchVod() {
        return isSearchVod;
    }

    public void setSearchVod(boolean searchVod) {
        isSearchVod = searchVod;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public long getTimeOffset() {
        return timeOffset;
    }

    public int getPurchases() {
        return purchases;
    }

    public String getId() {
        return id;
    }

    public void setTimeOffset(long timeOffset) {
        this.timeOffset = timeOffset;
    }

    public ArrayList<Product> getProduct() {
        return product;
    }

    public Product getProduct(String type) {
        int size = product.size();
        for (int i = 0; i < size; i++) {
            if (product.get(i).getType().equals(type)) {
                return product.get(i);
            }
        }
        return null;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    public Product getSingleProduct() {

        for (Product p : product) {
            if (p.getType().equals(TYPE_SINGLE)) {
//                if(p.isHandheldCclass() || p.isHandheldSTBCclass()) {
                return p;
//                }

            }
        }

        return null;
    }

    public boolean isFromResumingFragment() {
        return isFromResumingFragment;
    }

    public void setFromResumingFragment(boolean fromResumingFragment) {
        isFromResumingFragment = fromResumingFragment;
    }

    public boolean isSingleOnly() {
        if (product == null) {
            return false;
        }
        for (Product product : this.product) {
            if(product.isSingleOnly()) {
                return true;
            }
        }

        return false;
    }

    public void setRelatedVod(boolean relatedVod) {
        isRelatedVod = relatedVod;
    }

    public Program getProgram() {
        return program;
    }

    public ArrayList<Product> getHandheldAvailableProducts() {
        if (product == null) {
            return null;
        }
        ArrayList<Product> list = new ArrayList<>();
        for (Product product : this.product) {
            if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
                if ((product.isHandheldCclass() && !product.isSingleProduct()) || (product.isBigProduct())) {
                    list.add(product);
                }
            } else if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
                if (product.isHandheldSTBCclass() && !product.isSingleProduct() && product.isHasPrice()) {
                    list.add(product);
                }
//                if((product.isHandheldCclass() || product.isHandheldSTBCclass()) && !product.isSingleProduct() && product.isHasPrice()) {
//                    list.add(product);
//                }
            }

        }

        return list;
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

    public boolean isPackageProductFree(/*Product[] products*/) {
        if (getProduct() == null || getProduct().size() == 0) return false;
        for (int i = 0; i < getProduct().size(); i++) {
            Product product = getProduct().get(i);
            if (product.isFree()) {
                return true;
            }
        }
        return false;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public boolean isPackageProductPurchased(/*Product[] products*/) {
        if (getProduct() == null || getProduct().size() == 0) return false;


        for (int i = 0; i < getProduct().size(); i++) {
            Product product = getProduct().get(i);
            if (/*product.isPurchasable() && */product.isPurchased()) {
                return true;
            }
        }


        return false;
    }

    public boolean isSeries() {
        if (program == null) {
            return false;
        }
        return program.getSeries() != null;
    }

    public boolean isPackageProductPurchased(Context context) {
//        if (products == null || products.size() == 0) return false;
//
//        boolean isPurchased = false;
//
//        int size = products.size();
//
//        for (int i = 0; i < size; i++) {
//            Product product = products.get(i);
//            if (product.isFpackage()) {
//
//                if (NetworkUtil.checkMobileNetwork(context)) {// connected mobile network
//                    if (product.isPurchasable() && product.isPurchased()) {
//                        Logger.d("program", "isPackageProductPurchased-----1");
//                        isPurchased = true;
//                        break;
//                    }
//                } else {
//
//                    if (product.isPurchased()) {
//                        Logger.d("program", "isPackageProductPurchased-----2");
//                        isPurchased = true;
//                        break;
//                    }
//                }
//
//
//            } else {
//                if (product.isPurchased()) {
//                    Logger.d("program", "isPackageProductPurchased-----3");
//                    isPurchased = true;
//                    break;
//                }
//            }
//        }
//
//
//        return isPurchased;

        if (getProduct() == null || getProduct().size() == 0) return false;


        for (int i = 0; i < getProduct().size(); i++) {
            Product product = getProduct().get(i);
            if (/*product.isPurchasable() && */product.isPurchased()) {
                return true;
            }
        }


        return false;
    }


    public boolean isFlagProgram() {

        Product product = getSingleProduct();

        return product != null && product.isFlag();
//
//        ArrayList<Product> listHandhelAvai = getHandheldAvailableProducts();
//        if(listHandhelAvai == null) {
//            return false;
//        }
//
//        for(Product product : listHandhelAvai) {
//            if(product.isFlag()) {
//                return true;
//            }
//        }

//        return false;
    }

    public boolean isFreeProgram() {

        Product product = getSingleProduct();

        return product != null && product.isFree();

//        ArrayList<Product> listHandheldAvai = getHandheldAvailableProducts();
//        if(listHandheldAvai == null) {
//            return false;
//        }
//
//        for(Product product : listHandheldAvai) {
//            if(!product.isHidden() && product.isFree()) {
//                return true;
//            }
//        }
//        return false;
    }

    public ArrayList<Product> getPackageProducts() {
        if (product == null) return null;
        ArrayList<Product> packageProduct = new ArrayList<>();

        for (int i = 0; i < product.size(); i++) {
            Product tmPro = product.get(i);

            if (!tmPro.getType().toLowerCase().equals("single")) {
                if (tmPro.isFpackage()) {
                    tmPro = checkAvailableFpackage(tmPro);
                } else {
                    tmPro = checkAvailable(tmPro);
                }
                if (tmPro != null)
                    packageProduct.add(tmPro);
            }
        }
//        return packageProduct.size() == 0 ? null : packageProduct.toArray(new Product[packageProduct.size()]);
        return packageProduct.size() == 0 ? null : packageProduct;
    }

    public ArrayList<Product> checkSystemProduct() {
        if (product == null) return null;

        ArrayList<Product> list = null;

        Product single = getSingleProduct();
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

                if (!WindmillConfiguration.is3GQuetoiVersion) {
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(p);
                    continue;
                }

                if (!p.isHas_subpackages() || p.getNetwork_type() == null || p.getNetwork_type().equals("all")) {
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(p);
                } else {
                    if (single.isPurchasedNotDirect()) {
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        list.add(p);
                    }
                }
            }
        }

        return list;
    }

    public static boolean isNetworkSystemPackagePlayable(ArrayList<Product> listProduct, NetworkUtil.NetworkType networkType) {
        if (listProduct == null || listProduct.isEmpty()) return false;

        for (Product product : listProduct) {
            if (product.isNetworkSystemPackagePlayable(networkType)) {
                return true;
            }
        }

        return false;
    }

    public Product getWatchableProduct() {//watchable 체크할때 purchasable 체크 하지 않음.
        Product product = null; //getAvailableBundleProduct();
        ArrayList<Product> products = getHandheldAvailableProducts();


        if (products != null && isPackageProductFree() || isPackageProductPurchased()) {
            return getSingleProduct();
        }
        product = getAvailableSingleProduct();
        if (product != null && (product.isFree() || product.isPurchased() ||
                product.isFlag() && (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2)))
            return product;

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
            if (product.isPurchaseable()) return product;
        }

        if (product.isSubscriptionProduct() && product.isHandheldCclass()) {
            if (product.isPurchaseable()) return product;
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
            if (product.isPurchaseable()) return product;
        }

        if (product.isPackageProduct() && product.isHandheldCclass()) {
            if (product.isPurchaseable()) return product;
        }
        return null;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    public Product getAvailableSingleProduct() {

        ArrayList<Product> list = getHandheldAvailableProducts();
        for (Product product : list) {
            if (product.isSingleProduct()) {
                if (product.isFlag() || product.isFree() || product.isPurchased()) {
                    return product;
                }
            }
        }

        return getSingleProduct();
    }

    public void setProduct(ArrayList<Product> product) {
        this.product = product;
    }

    public void setProgram(Program program) {
        this.program = program;
    }


    //TODO standalone 상품 ###########################################################################
//    public Product getAvailableFpackageProduct() {
//        Product product = getProduct(Product.CCLASS_FPACKAGE_STB_HANDHELD);
//        if (product != null) {
////            if (product.getPurchasable().equals("true"))
//            if (product.isPurchased() || product.isFree/*isFpackageFree*/() || product.getPurchasable().equals("true"))
//                return product;
//        }
//        product = getProduct(Product.CCLASS_FPACKAGE_HANDHELD);
//        if (product != null) {
////            if (product.getPurchasable().equals("true"))
//            if (product.isPurchased() || product.isFree/*isFpackageFree*/() || product.getPurchasable().equals("true"))
//                return product;
//        }
//        return null;
//    }
    //#######################################################################################################################

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(isPurchased ? 1 : 0);
        dest.writeInt(isRelatedVod ? 1 : 0);
        dest.writeInt(isResumeVod ? 1 : 0);
        dest.writeInt(isSearchVod ? 1 : 0);
        dest.writeString(purchaseId);
        dest.writeString(menuPath);
        dest.writeParcelable(program, 0);
        if (product == null)
            product = new ArrayList<>();
        dest.writeParcelableArray(product.toArray(new Product[product.size()]), 0);
    }

    @SuppressWarnings("unused")
    public static final Creator<Vod> CREATOR = new Creator<Vod>() {
        @Override
        public Vod createFromParcel(Parcel in) {
            return new Vod(in);
        }

        @Override
        public Vod[] newArray(int size) {
            return new Vod[size];
        }
    };

    public Vod() {
    }

    protected Vod(Parcel in) {
        this.id = in.readString();
        this.isPurchased = in.readInt() == 1;
        this.isRelatedVod = in.readInt() == 1;
        this.isResumeVod = in.readInt() == 1;
        this.isSearchVod = in.readInt() == 1;
        this.purchaseId = in.readString();
        this.menuPath = in.readString();
        this.program = in.readParcelable(Program.class.getClassLoader());
        Parcelable[] parcelables = in.readParcelableArray(Product.class.getClassLoader());
        product = new ArrayList<>();
        Collections.addAll(product, Arrays.copyOf(parcelables, parcelables.length, Product[].class));
    }

    public static final int TYPE_NOMAL = 0;
    public static final int TYPE_RESUME = 1;

    private int type = TYPE_NOMAL;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isResumeVod() {
        return isResumeVod;
    }

    public void setResumeVod(boolean resumeVod) {
        isResumeVod = resumeVod;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getWatchedID() {
        return watchedID;
    }

    public void setWatchedID(String watchedID) {
        this.watchedID = watchedID;
    }

}
