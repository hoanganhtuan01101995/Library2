package com.alticast.viettelottcommons.def.entry;

import com.alticast.android.util.Log;
import com.alticast.viettelottcommons.manager.MenuManager;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by mc.kim on 8/8/2016.
 */
public class EntryPathLogImpl {

    private static final Log LOG = Log.createLog("EntryPathLogImpl");

    private final String scheme = "windmill";
    private String hostName;
    private ArrayList<String> pathList = new ArrayList<String>();
    private ArrayList<String> menuPathList = new ArrayList<>();

    private boolean isRelateContentMode;

    private static EntryPathLogImpl ourInstance = new EntryPathLogImpl();

    public static EntryPathLogImpl getInstance() {
        return ourInstance;
    }

    private String subPath;

    private EntryPathLogImpl() {
    }

    public boolean updateLog(String host, String path) {
        if (host == null) {
            return false;
        }
        pathList.clear();
        menuPathList.clear();
        hostName = host;
        if (path != null) {
            LOG.printDbg("test updateLog path :  " + path);
            pathList.add(path);
            menuPathList.add(path);
        }
        return true;
    }

    public void popLog() {
        if (pathList.size() > 0) {
            pathList.remove(pathList.size() - 1);
        }

    }

    public String getSubPath() {
        return subPath;
    }

    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }

    public boolean isHasSubPath() {
        return subPath != null;
    }

    //    public boolean updateCategoryLog(String host, String path) {
//        if (host == null) {
//            return false;
//        }
//        hostName = host;
//        if (path != null) {
//            String tm = pathList.get(pathList.size() - 1);
//            if (path.contains(tm) || tm.contains(path)) {
//
//
//                pathList.clear();
//                if (path.contains(tm)) {
//                    pathList.add(path);
//                } else {
//                    pathList.add(tm);
//                }
//
//
//            } else {
//                pathList.clear();
//                pathList.add(tm + "/" + path);
//            }
//
//
//        }
//        return true;
//    }


    public URI getPurchasePathIDUri() {
        URI uri = null;
        String host = hostName;
        String delim = "/";
        String path = "";

        if (host == null || host.length() == 0) {
            return null;
        }

        int count = menuPathList.size();
        for (int i = 0; i < count; i++) {
            path += delim + menuPathList.get(i);
        }
        try {
            uri = new URI(scheme, host, path, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }


    public String getPurchaseMenuString() {
        String pathway = null;
        URI uri = getPurchasePathIDUri();
        if (uri != null) {
            try {
                pathway = URLDecoder.decode(uri.toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return pathway;
    }


    public void clearPurchasePath() {
        hostName = null;
        pathList.clear();
    }

    public boolean isRelateContentMode() {
        return isRelateContentMode;
    }

    public void setRelateContentMode(boolean relateContentMode) {
        isRelateContentMode = relateContentMode;
    }

    public static final String GENERAL_PATH = "VC_OTT";

    public String getWatchMenuId() {

//        if(isRelateContentMode) {
//            return GENERAL_PATH;
//        }

        if(MenuManager.getInstance().getCurrentMenu() == null) {
            return GENERAL_PATH;
        }
        String currentMenu = MenuManager.getInstance().getCurrentMenu().getPath_id();
        if(currentMenu != null && !currentMenu.isEmpty() && !currentMenu.contains("home")) {
            return subPath != null ? subPath : currentMenu;
        } else {
            return GENERAL_PATH;
        }

    }
}
