package com.alticast.viettelottcommons.util;

import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.response.PurchaseListRes;

import java.util.ArrayList;

/**
 * Created by duyuno on 10/4/17.
 */
public class ProductUtils {

    public static ArrayList<Product> filterPurchasedAndSVOD(ArrayList<Product> listSource) {

        if(listSource == null || listSource.isEmpty()) return null;

        ArrayList<Product> listResults = null;

        for(Product product : listSource) {
            if (product.isSVODProduct()) {
                if (listResults == null) {
                    listResults = new ArrayList<>();
                }
                listResults.add(product);
            } else {
                if(product.getPurchase() != null) {
                    if (listResults == null) {
                        listResults = new ArrayList<>();
                    }
                    listResults.add(product);
                }
            }
        }

        return listResults;
    }

    public static ArrayList<Product> filterSmallProduct(ArrayList<Product> listSource) {
        if(listSource == null || listSource.isEmpty()) return null;
        ArrayList<Product> listResults = null;

        for(Product product : listSource) {
            if (product.isSmallProduct()) {
                if (listResults == null) {
                    listResults = new ArrayList<>();
                }
                listResults.add(product);
            }
        }

        return listResults;
    }


    public static ArrayList<Product> filterPurchaseableProduct(ArrayList<Product> listSource) {
        if(listSource == null || listSource.isEmpty()) return null;
        ArrayList<Product> listResults = null;

        for(Product product : listSource) {
            if(product.isPurchaseable() && product.isFpackage()) {
                if (listResults == null) {
                    listResults = new ArrayList<>();
                }
                listResults.add(product);
            }
        }

        return listResults;
    }
    public static ArrayList<Product> filterFullPurchaseableProduct(ArrayList<Product> listSource) {

        if(AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
            return filterAllHandheldPurchaseableProduct(listSource);
        }

        if(listSource == null || listSource.isEmpty()) return null;
        ArrayList<Product> listResults = null;

        for(Product product : listSource) {
            if(product.isPurchaseable() && !product.isSingleProduct()) {
                if (listResults == null) {
                    listResults = new ArrayList<>();
                }
                listResults.add(product);
            }
        }

        return listResults;
    }
    public static ArrayList<Product> filterAllHandheldPurchaseableProduct(ArrayList<Product> listSource) {
        if(listSource == null || listSource.isEmpty()) return null;
        ArrayList<Product> listResults = null;

        for(Product product : listSource) {
            if(product.isOnMeProduct()) continue;
            if(product.isPurchaseable()
                    && ((product.isHandheldCclass() && !product.isSingleProduct()) || product.isBigProduct())) {
                if (listResults == null) {
                    listResults = new ArrayList<>();
                }
                listResults.add(product);
            }
        }

        return listResults;
    }
    public static ArrayList<Product> filterAllHandheldPurchaseableProduct(ArrayList<Product> listSource, PurchaseListRes purchaseListRes) {
        if(listSource == null || listSource.isEmpty()) return null;
        ArrayList<Product> listResults = null;

        for(Product product : listSource) {

            if(product.isOnMeProduct()) continue;

            if(product.isPurchaseable()
                    && ((product.isHandheldCclass() && !product.isSingleProduct()) || product.isBigProduct())) {
                if (listResults == null) {
                    listResults = new ArrayList<>();
                }

                if(purchaseListRes.getScreenMax(listSource) <= product.getMaxScreenInPeriod()) {
                    listResults.add(product);
                }

            }
        }

        return listResults;
    }
    public static ArrayList<Product> filterBasicProduct(ArrayList<Product> listSource) {
        if(listSource == null || listSource.isEmpty()) return null;
        ArrayList<Product> listResults = null;

        for(Product product : listSource) {
            if(product.isPurchaseable() && product.isFpackage() && product.getPid().equals("VIP2")) {
                if (listResults == null) {
                    listResults = new ArrayList<>();
                }
                listResults.add(product);
            }
        }

        return listResults;
    }
    public static ArrayList<Product> filterBigProduct(ArrayList<Product> listSource) {
        if(listSource == null || listSource.isEmpty()) return null;
        ArrayList<Product> listResults = null;

        for(Product product : listSource) {
            if (product.isBigProduct()) {
                if (listResults == null) {
                    listResults = new ArrayList<>();
                }
                listResults.add(product);
            }
        }

        return listResults;
    }

    public static ArrayList<Product> filterUpSellProduct(ArrayList<Product> listSource, int maxScreen) {
        if(listSource == null || listSource.isEmpty()) return null;

        ArrayList<Product> listResults = null;

        for(Product product : listSource) {
            if (product.checkHasUpsellAvai(maxScreen)) {
                if (listResults == null) {
                    listResults = new ArrayList<>();
                }
                listResults.add(product);
            }
        }

        return listResults;
    }
}
