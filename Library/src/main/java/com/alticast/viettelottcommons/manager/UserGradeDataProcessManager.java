package com.alticast.viettelottcommons.manager;

import java.util.ArrayList;

/**
 * Created by mc.kim on 7/28/2016.
 */
public class UserGradeDataProcessManager {

    private static UserGradeDataProcessManager instance = null;
    private ArrayList<String> include = null;

    public UserGradeDataProcessManager() {
        if (include == null) {
            include = new ArrayList<String>();
        }

    }

    public static synchronized UserGradeDataProcessManager getInstacne() {
        if (instance == null) {
            instance = new UserGradeDataProcessManager();
        }
        return instance;
    }

    public ArrayList<String> getInclude() {
        if (include != null) {
            include.clear();
        } else {
            include = new ArrayList<String>();
        }

        include.add("product");
        include.add("multilang");
        if (AuthManager.currentUserAuth().isPrivate()) {
            include.add("purchase");
        }

        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2 || AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL1) {
            include.add("fpackage");
        }

        return include;
    }

    public ArrayList<String> getIncludeProduct() {
        if (include != null) {
            include.clear();
        } else {
            include = new ArrayList<String>();
        }
        include.add("product");
        include.add("multilang");
        return include;
    }


}
