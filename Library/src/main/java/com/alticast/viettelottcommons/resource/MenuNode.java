package com.alticast.viettelottcommons.resource;

import java.util.ArrayList;

/**
 * Created by mc.kim on 8/9/2016.
 */
public class MenuNode {
    private ArrayList<Menu> history = null;
    private int focusIndex = 0;
    private String menuType = null;

    public MenuNode(ArrayList<Menu> history, int focusIndex, String menuType) {
        this.history = history;
        this.focusIndex = focusIndex;
        this.menuType = menuType;
    }
}
