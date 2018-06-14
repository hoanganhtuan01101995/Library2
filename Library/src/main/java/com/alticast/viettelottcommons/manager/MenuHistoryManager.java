package com.alticast.viettelottcommons.manager;

import com.alticast.viettelottcommons.resource.Menu;
import com.alticast.viettelottcommons.resource.MenuNode;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by mc.kim on 8/9/2016.
 */
public class MenuHistoryManager {

    private Stack<MenuNode> menuNodeStack = new Stack<>();
    private static MenuHistoryManager ourInstance = new MenuHistoryManager();

    public static MenuHistoryManager getInstance() {
        return ourInstance;
    }

    private MenuHistoryManager() {

    }

    public void updateHistory(ArrayList<Menu> currentMenuList, int position, boolean isOnHistory) {
        Menu currentMenu = currentMenuList.get(position);
        MenuNode node = new MenuNode(currentMenuList, position, currentMenu.getType());
        if (isOnHistory) {
            if (!menuNodeStack.empty()) {
                menuNodeStack.pop();
            }
            menuNodeStack.add(node);
        } else {
            menuNodeStack.add(node);
        }
    }

    public MenuNode popHistory() {
        if (menuNodeStack.empty()) {
            return null;
        }
        return menuNodeStack.pop();
    }



}
