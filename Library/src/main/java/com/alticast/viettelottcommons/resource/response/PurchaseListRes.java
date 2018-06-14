package com.alticast.viettelottcommons.resource.response;

import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.loader.PurchaseCheckLoader;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.PriceSummery;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.Purchase;
import com.alticast.viettelottcommons.resource.RentalPeriods;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by mc.kim on 8/8/2016.
 */
public class PurchaseListRes {

    private HashMap<String, Purchase> hashPurchase;

    private int total = 0;
    private ArrayList<Purchase> data = null;
    private PriceSummery summary = null;
    private ArrayList<String> ids = null;

    private ProgramList programList = null;
    private int offset = 0;

    public void initHashPurchase() {

        if(data == null || data.isEmpty()) return;

        hashPurchase = new HashMap<>();

        for(Purchase purchase : data) {
            if(purchase.getProduct() != null) {
                hashPurchase.put(purchase.getProduct().getId(), purchase);
            }
        }
    }

//    public int getScreenMax() {
//        if(data == null || data.isEmpty()) return 0;
//        int max = 0;
//
//        for(Purchase purchase : data) {
//            int newMax = purchase.getScreenMax();
//
//            if(max < newMax) {
//                max = newMax;
//            }
//        }
//
//        return max;
//    }
    public int getScreenMax(ArrayList<Product> listSource) {
        if(data == null || data.isEmpty()) return 0;
        int max = 0;

        for(Product product : listSource) {
            if(!product.isOnMeProduct()) {
                Purchase purchase = getPurchase(product.getId());
                if(purchase != null) {
                    if (purchase.getProductType() == null || purchase.getProductType().equals("single")) {
                        continue;
                    }
                    if (purchase.getExpireDate() > 0) {
                        if (purchase.getExpireDate() > Calendar.getInstance().getTimeInMillis()) {
                            int newMax = purchase.getScreenMax();

                            if(max < newMax) {
                                max = newMax;
                            }
                        }
                    } else {
                        int newMax = purchase.getScreenMax();

                        if(max < newMax) {
                            max = newMax;
                        }
                    }
                }
            }
        }

//        for(Purchase purchase : data) {
//            int newMax = purchase.getScreenMax();
//
//            if(max < newMax) {
//                max = newMax;
//            }
//        }

        return max;
    }

    public boolean checkAvaiBigPackage() {
        if(data == null || data.isEmpty()) return false;

        for(Purchase purchase : data) {
            if(purchase.getProduct() == null || purchase.getProduct().getScreen_type() == null) {
                continue;
            }
            if(purchase.getProduct().getScreen_type().equals("big")) {
                return true;
            }
        }

        return false;
    }

    public boolean checkAvaiProduct(String productId) {
        return hashPurchase != null && hashPurchase.containsKey(productId);
    }

    public Purchase getPurchase(String productId) {
        if(hashPurchase == null) return null;
        return hashPurchase.get(productId);
    }

    public boolean checkPurchased(ArrayList<Product> listProduct) {
        if(hashPurchase == null) return false;
        if(listProduct == null) return false;
        for(Product product : listProduct) {
            if(hashPurchase.containsKey(product.getId())) {
                return true;
            }
        }

        return false;
    }
    public RentalPeriods getPurchasedRental(ArrayList<Product> listProduct) {
        if(hashPurchase == null) return null;
        if(listProduct == null) return null;
        for(Product product : listProduct) {
            if(hashPurchase.containsKey(product.getId())) {
                if(!product.isRentalPeriodProduct()) {
                    continue;
                }
                for(RentalPeriods rentalPeriods : product.getRental_periods()) {
                    if(rentalPeriods.isPurchased()) {
                        return rentalPeriods;
                    }
                }
            }
        }

        return null;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotal() {
        return total;
    }

    public ArrayList<Purchase> getData() {
        return data;
    }

    public PriceSummery getSummary() {
        return summary;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public ProgramList getProgramList() {
        return programList;
    }

    public void setProgramList(ProgramList programList) {
        this.programList = programList;
    }

    public void filterPurchaseList(final WindmillCallback windmillCallback) {

        if(data == null || data.isEmpty()) windmillCallback.onSuccess(null);

        initHashPurchase();

        PurchaseCheckLoader.getInstance().getFullpackages(new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                WalletRes walletRes = (WalletRes) obj;

                ArrayList<Purchase> listPurchase = null;

                for(Product product : walletRes.getData()) {
                    if(!product.isOnMeProduct()) {
                        Purchase purchase = getPurchase(product.getId());
                        if(purchase != null) {
                            if (purchase.getProductType() == null || purchase.getProductType().equals("single")) {
                                continue;
                            }
                            if (purchase.getExpireDate() > 0) {
                                if (purchase.getExpireDate() > Calendar.getInstance().getTimeInMillis()) {
                                    if(listPurchase == null) {
                                        listPurchase = new ArrayList<>();
                                    }
                                    listPurchase.add(purchase);
                                }
                            } else {
                                if(listPurchase == null) {
                                    listPurchase = new ArrayList<>();
                                }
                                listPurchase.add(purchase);
                            }
                        }
                    }
                }

                windmillCallback.onSuccess(listPurchase);

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                windmillCallback.onSuccess(null);
            }

            @Override
            public void onError(ApiError error) {
                windmillCallback.onSuccess(null);
            }
        });

//        for (Purchase purchase : data) {
//            if(purchase.getProductType() == null) {
//                continue;
//            }
//            if (purchase.getProductType().equals("subscription") || purchase.getProductType().equals("fpackage")) {
//
//                if(purchase.getExpireDate() > 0) {
//                    if (purchase.getExpireDate() > Calendar.getInstance().getTimeInMillis()) {
//                        if(listPurchase == null) {
//                            listPurchase = new ArrayList<>();
//                        }
//                        listPurchase.add(purchase);
//                    }
//                } else {
//                    if(listPurchase == null) {
//                        listPurchase = new ArrayList<>();
//                    }
//                    listPurchase.add(purchase);
//                }
//            }
//        }
//
//        windmillCallback.onSuccess(listPurchase);
    }

//    public void processData(PurchaseListRes mPurchaseListRes, WalletRes walletRes) {
//        ArrayList<Purchase> purchases = mPurchaseListRes.getData();
//        if (purchases == null) return;
//
//        mPurchaseListRes.initHashPurchase();
//
//        ArrayList<Purchase> mList = new ArrayList<Purchase>();
//
//        for(Product product : walletRes.getData()) {
//            if(product.isOnMePackage()) {
//                Purchase purchase = mPurchaseListRes.getPurchase(product.getId());
//                if(purchase != null) {
//                    if (purchase.getProductType() == null) {
//                        continue;
//                    }
//                    if (purchase.getExpireDate() > 0) {
//                        if (purchase.getExpireDate() > Calendar.getInstance().getTimeInMillis()) {
//                            mList.add(purchase);
//                        }
//                    } else {
//                        mList.add(purchase);
//                    }
//                }
//            }
//        }
//
//    }
}
