package com.alticast.viettelottcommons.manager;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.def.MenuFields;
import com.alticast.viettelottcommons.def.PurchasePathway;
import com.alticast.viettelottcommons.loader.CategoryLoader;
import com.alticast.viettelottcommons.loader.ProgramLoader;
import com.alticast.viettelottcommons.loader.RecommendLoader;
import com.alticast.viettelottcommons.def.entry.EntryPathLogImpl;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.Category;
import com.alticast.viettelottcommons.resource.Menu;
import com.alticast.viettelottcommons.resource.MultiLingualText;
import com.alticast.viettelottcommons.resource.Path;
import com.alticast.viettelottcommons.resource.Vod;
import com.alticast.viettelottcommons.resource.response.CategoryInfo;
import com.alticast.viettelottcommons.resource.response.CategoryListRes;
import com.alticast.viettelottcommons.resource.response.MenuListRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.acms.category.MenuMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;
import com.alticast.viettelottcommons.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mc.kim on 8/8/2016.
 */
public class MenuManager {

    private static HashMap<String, Boolean> menulevelMap = new HashMap();
    private final int NEWS = 0;
    private final int MOVIE = 1;
    private final int SPORTS = 2;
    private final int ENTERTAINMENT = 3;
    private final int KIDS = 4;
    private final int LOCAL_CHANNEL = 5;
    private final int LOCAL = 6;
    private final int K_ = 7;
    private final int HD1 = 8;
    public static String ROOT = "/";
    public static String HOME_PATH_ID;

    static {
        //Type, Privaate 영역
        menulevelMap.put(MenuFields.TYPE_G_NOTICE, false);
        menulevelMap.put(MenuFields.TYPE_ACTIVATE, true);
        menulevelMap.put(MenuFields.TYPE_VERSIONS, false);
        menulevelMap.put(MenuFields.TYPE_CONNECT_TV, true);
        menulevelMap.put(MenuFields.TYPE_C_NOTICE, false);
        menulevelMap.put(MenuFields.TYPE_E_NOTICE, false);
        menulevelMap.put(MenuFields.TYPE_V_PURCHASED, true);
        menulevelMap.put(MenuFields.TYPE_NV_PURCHASED, true);
        menulevelMap.put(MenuFields.TYPE_S_TV_FAVORITE, true);
        menulevelMap.put(MenuFields.TYPE_H_RECOMMEND, false);
        menulevelMap.put(MenuFields.TYPE_M_RECOMMEND, false);
        menulevelMap.put(MenuFields.TYPE_V_RECOMMEND, false);
        menulevelMap.put(MenuFields.TYPE_E_ALL, false);
        menulevelMap.put(MenuFields.TYPE_E_FAVORITE, true);
        menulevelMap.put(MenuFields.TYPE_E_RESERVED, true);
        menulevelMap.put(MenuFields.TYPE_USAGE_INFO, true);
        menulevelMap.put(MenuFields.TYPE_SEARCH, false);
        menulevelMap.put(MenuFields.TYPE_CATCH_UP, false);
        menulevelMap.put(MenuFields.TYPE_LANGUAGE, false);
        menulevelMap.put(MenuFields.TYPE_MY_CONTENT, true);
        menulevelMap.put(MenuFields.TAG_GENRE_EPG_ROOT, false);
        menulevelMap.put(MenuFields.TYPE_E_GENRE, false);
        menulevelMap.put(MenuFields.TYPE_RESUME, true);
        menulevelMap.put(MenuFields.TYPE___VOD_CATEGORY, false);
        menulevelMap.put(MenuFields.TYPE_TOP_UP, true);
        menulevelMap.put(MenuFields.TYPE_MY_ACCOUNT, true);
        menulevelMap.put(MenuFields.TYPE___PROGRAM, false);
        menulevelMap.put(MenuFields.TYPE_PROGRAM, false);
        menulevelMap.put(MenuFields.TYPE_PROGRAM_SEARCH, false);
        menulevelMap.put(MenuFields.TYPE_FACE_BOOK, false);
        menulevelMap.put(MenuFields.TYPE_TOP_UP_HISTORY, true);

    }

    private ArrayList<Menu> listParentMenu;
    private ArrayList<Menu> menuPath = new ArrayList<>();
    private HashMap<String, ArrayList<Menu>> hashMapMenu;

    private static MenuManager ourInstance = new MenuManager();

    public static MenuManager getInstance() {
        return ourInstance;
    }

    private MenuManager() {
        HOME_PATH_ID = WindmillConfiguration.LIVE ? "/home_" : "/home_";
    }

    private Hashtable<String, Menu> menuTablePath = new Hashtable<>();

    public void initMenus(final WindmillCallback callback) {
        MenuMethod menuMethod = ServiceGenerator.getInstance().createSerive(MenuMethod.class);
        menuTablePath.clear();
        menuList.clear();
        Call<MenuListRes> call = menuMethod.getMenus(AuthManager.getGuestToken(), "all", WindmillConfiguration.MENUS_VERSION);
        call.enqueue(new Callback<MenuListRes>() {
            @Override
            public void onResponse(Call<MenuListRes> call, Response<MenuListRes> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    saveMenuData((MenuListRes) response.body());
                    callback.onSuccess(response.body());
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<MenuListRes> call, Throwable t) {

                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void saveMenuData(MenuListRes menuListRes) {
        menuList = menuListRes.getData();

        int removeIndex = -1;
        for (int i = 0, size = menuList.size(); i < size; i++) {
            Menu menu = menuList.get(i);
            if (menu.getPath_id() != null && menu.getPath_id().contains("SETTING")) {
                removeIndex = i;
                break;
            }
        }

        if (removeIndex >= 0)
            menuList.remove(removeIndex);

        if (menuList == null || menuList.isEmpty()) {
            return;
        }

        if (listParentMenu == null) {
            listParentMenu = new ArrayList<>();
        } else {
            listParentMenu.clear();
        }

        if (hashMapMenu == null) {
            hashMapMenu = new HashMap<>();
        } else {
            hashMapMenu.clear();
        }

        String pathId;

        for (int i = 0, size = menuList.size(); i < size; i++) {
            Menu menu = menuList.get(i);

            Menu item = menuList.get(i);
            pathId = item.getPath_id();
            if (pathId.length() > 1) {
                if (pathId.indexOf(ROOT, 1) == -1) {
                    if (!isHidden(item)) {
                        item.setRoot(true);
                        item.setParentOrgId(item.getId());
                        listParentMenu.add(item);
                    } else {
                        item.setRoot(false);
                    }
                } else {
                    item.setRoot(false);
                }
            } else {
                item.setRoot(false);
            }

            menuTablePath.put(menu.getPath_id(), menu);
        }

        for (int i = 0, size = listParentMenu.size(); i < size; i++) {
            Menu menu = listParentMenu.get(i);

            if ((menu.getTag() != null && menu.getTag().equals("home_root"))
                    || (menu.getPath_id().equals("/home"))
                    || (i == size - 1)) {
                menu.setClickable(false);
            } else {
                menu.setClickable(true);
            }
            ArrayList<Menu> listSub = getSubmenu(menu, i == size - 1);
            hashMapMenu.put(menu.getId(), listSub);
        }

        Menu firstMenu = listParentMenu.get(0);
        if (!firstMenu.getPath_id().equals(HOME_PATH_ID)) {
            Menu homeMenu = new Menu();
            homeMenu.setId("" + System.currentTimeMillis());
            homeMenu.setPath_id(HOME_PATH_ID);
            homeMenu.setParentOrgId(homeMenu.getId());
            homeMenu.setType(MenuFields.TYPE_ROOT);
            homeMenu.setRoot(true);
            homeMenu.setClickable(true);
            homeMenu.setHomeMenu(true);
            ArrayList<MultiLingualText> listName = new ArrayList<>();
            listName.add(new MultiLingualText(WindmillConfiguration.LANGUAGE, "Home"));
            homeMenu.setName(listName);
            listParentMenu.add(0, homeMenu);
        } else {
            firstMenu.setHomeMenu(true);
            firstMenu.setClickable(true);
        }
    }

    public Menu getParentMenu(String id) {
        if (listParentMenu == null) {
            return null;
        }

        for (Menu menu : listParentMenu) {
            if (menu.getId().equals(id)) {
                return menu;
            }
        }

        return null;
    }

    public ArrayList<Menu> getMenuList() {
        return menuList;
    }

    private ArrayList<Menu> menuList = new ArrayList<>();

    public Menu getMenu(String menuPathId) {
        if (menuList == null) {
            return null;
        }

        for (int i = 0; i < menuList.size(); i++) {
            if (menuList.get(i).getPath_id().equals(menuPathId)) {

                return menuList.get(i);
            }
        }

        return null;
    }

    public Menu getCurrentMenu() {
        if (menuPath == null || menuPath.isEmpty()) {
            return null;
        }
        return menuPath.get(menuPath.size() - 1);
    }


    public void getRootMenu(final OnMenuSelectedListener callback) {
        if (menuList.isEmpty()) {
            callback.needLoading(true);

            initMenus(new WindmillCallback() {
                @Override
                public void onSuccess(Object obj) {

//                    ArrayList<Menu> subMenu = getSubmenu(ROOT);
                    callback.onSuccess(listParentMenu);
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    callback.onFail(0);
                }

                @Override
                public void onError(ApiError error) {
                    callback.onError(error);
                }
            });
        } else {
            callback.needLoading(false);
//            ArrayList<Menu> subMenu = getSubmenu(ROOT);
            callback.onSuccess(listParentMenu);
        }
    }

//    public ArrayList<Menu> getRootMenu() {
//        if (menuList != null && menuList.isEmpty()) {
//            return null;
//        } else {
//            ArrayList<Menu> subMenu = getSubmenu(ROOT);
//            return subMenu;
//        }
//    }

    public boolean checkHasData() {
        return menuList != null && !menuList.isEmpty();
    }

    private void getCategories(final Menu selectedMenu, String categoryId, final OnMenuSelectedListener callback) {
        CategoryLoader.getInstance().getCategory(categoryId, 0, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                CategoryListRes categoryListRes = (CategoryListRes) obj;
                ArrayList<Category> categories = categoryListRes.getData();
                int size = categories.size();
                for (int i = 0; i < size; i++) {
                    categories.get(i).setPath_id(subStringCategoryPath(selectedMenu, categories.get(i).getPath_id()));
                    menuList.add(categories.get(i));
                }
                callback.onSuccess(categoryListRes.getData());
            }

            @Override
            public void onFailure(Call call, Throwable t) {

                Logger.print(this, "getCategories onFailure ");
                callback.onFail(0);
            }

            @Override
            public void onError(ApiError error) {
                Logger.print(this, "getCategories onError ");
                callback.onError(error);
            }
        });
    }

    private String subStringCategoryPath(Menu selectedMenu, String pathId) {
        String path = null;

        int idx = pathId.lastIndexOf("/");

        path = selectedMenu.getPath_id() + pathId.substring(idx);

        return path;
    }

    private boolean needToLoad(Menu menu) {
        return getListChilds(menu) == null || getListChilds(menu).size() == 0;
    }


    public void showVodDetail(Vod vod, final OnVodDetailListener callback) {
        final Path path = vod.getPath();
        if (path == null) {
            Logger.print(this, "wrong path");
        }
        final String host = path.getHost();


        if (host.equals(PurchasePathway.ENTRY_RECOMMEND)) {
            String clickLog = path.getClickLogForRecommendation();
            RecommendLoader.getInstance().sendRecommendLog(clickLog);
        }
//        else if (host.equals(PurchasePathway.ENTRY_MENU)) {
//
//        }
        callback.needLoading(true);
        ProgramLoader.getInstance().getProgram(vod.getProgram().getId(), new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                EntryPathLogImpl.getInstance().updateLog(host, path.getEntry());
                callback.onSuccess((Vod) obj);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                callback.onFail(0);
            }

            @Override
            public void onError(ApiError error) {
                callback.onError(error);
            }
        });


    }

    public void showVodDetail(final String programId, final Path entryPath, final OnVodDetailListener callback) {
        if (entryPath != null) {
            final String host = entryPath.getHost();

            if (host != null && host.equals(PurchasePathway.ENTRY_RECOMMEND)) {
                String clickLog = entryPath.getClickLogForRecommendation();
                RecommendLoader.getInstance().sendRecommendLog(clickLog);
            }
        }
//        else if (host.equals(PurchasePathway.ENTRY_MENU)) {
//
//        }
        callback.needLoading(true);
        ProgramLoader.getInstance().getProgram(programId, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                if (entryPath != null) {
                    String host = entryPath.getHost();
                    EntryPathLogImpl.getInstance().updateLog(host, entryPath.getEntry());
                }
//                EntryPathLogImpl.getInstance().updateLog("menu", entryPath.getEntry());
//                EntryPathLogImpl.getInstance().updateLog(PurchasePathway.ENTRY_PROMOTION, programId);
                callback.onSuccess((Vod) obj);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                callback.onFail(0);
            }

            @Override
            public void onError(ApiError error) {
                callback.onError(error);
            }
        });


    }

    //already load not update for Category
    public void menuSelected(final Menu selectedMenu, final int position, final boolean isOnHistory, final OnMenuSelectedListener callback) {

        String type = selectedMenu.getType();

        if (menuList.isEmpty()) {
            getRootMenu(callback);
        } else {

            if (isPrivateMenu(selectedMenu.getType()) && !AuthManager.currentUserAuth().isPrivate()) {
                callback.onFail(401);
            } else {

                if (type.equals(MenuFields.TYPE_MY_ACCOUNT) || type.equals(MenuFields.TYPE_ACTIVATE)) {
//                    if (HandheldAuthorization.getInstance().isQuickOption()) {
////                        HandheldAuthorization.getInstance().changeQuickOption(false);
//                        callback.onLeafMenu(selectedMenu);
//                    } else {
//                        callback.onFail(402);
//                    }
                    callback.onFail(402);
                    return;
                } else if (!selectedMenu.isClickable()) {
                    return;
                }

                if (isCategory(selectedMenu) && needToLoad(selectedMenu)) {
                    String categoryId = selectedMenu.getConfig(MenuFields.CONF_CATEGORY);
                    if (categoryId == null) {
                        categoryId = selectedMenu.getId();
                    }
                    boolean isLeaf = isLeafCategory(selectedMenu);
                    Logger.print(this, "Selected menu  categoryId : " + categoryId);
                    Logger.print(this, "Selected menu  isLeaf : " + isLeaf);
                    Logger.print(this, "Selected menu  type : " + selectedMenu.getType());
                    if (isLeaf) {
                        callback.onLeafMenu(selectedMenu);
                    } else {
                        callback.needLoading(true);
                        CategoryLoader.getInstance().getCategoryDetailInformation(categoryId, new WindmillCallback() {
                            @Override
                            public void onSuccess(Object obj) {
                                updateMenuPathHistory(selectedMenu, position, isOnHistory);

                                Category category = ((CategoryInfo) obj).getCategory();
                                Logger.print(this, "Selected menu onSuccess category : " + category.getName(WindmillConfiguration.LANGUAGE));

                                if (MenuFields.VIEW_TYPE_POSTER.equals(category.getConfig(MenuFields.CONF_VIEW))) {
                                    selectedMenu.setType(MenuFields.TYPE___PROGRAM_CATEGORY_POSTER);
                                    callback.onLeafMenu(selectedMenu);
                                } else {
                                    getCategories(selectedMenu, category.getId(), callback);
                                }
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                Logger.print(this, "Selected menu onFailure category : ");
                                callback.onFail(0);
                            }

                            @Override
                            public void onError(ApiError error) {
                                Logger.print(this, " Selected menu onError category : ");
                                callback.onError(error);
                            }
                        });

                    }

                } else {
                    updateMenuPathHistory(selectedMenu, position, isOnHistory);
                    callback.needLoading(false);
                    ArrayList<Menu> subMenu = getListChilds(selectedMenu);

                    if (subMenu == null || subMenu.size() == 0) {
                        callback.onLeafMenu(selectedMenu);
                    } else {
                        callback.onSuccess(subMenu);
                    }
                }
            }
        }
    }

    private void updateMenuPathHistory(Menu selectedMenu, int position, boolean isOnHistory) {
//        final ArrayList<Menu> currentMenuList = getListChilds(selectedMenu);
        EntryPathLogImpl.getInstance().updateLog(PurchasePathway.ENTRY_MENU, selectedMenu.getPath_id());
//        MenuHistoryManager.getInstance().updateHistory(currentMenuList, position, isOnHistory);
    }


    private boolean isCategory(Menu menu) {

        return menu.getType().equals(MenuFields.TYPE_PROGRAM_CATEGORY) || menu.getType().equals(MenuFields.TYPE_GENERAL) || menu.getType().equals(MenuFields.TYPE___PROGRAM_CATEGORY_POSTER);
    }

    private boolean isPrivateMenu(String menuType) {
        if (menulevelMap.get(menuType) != null) {

            return menulevelMap.get(menuType);

        } else {
            return false;
        }
    }

    private ArrayList<Menu> getSubmenu(Menu menu, boolean isLast) {

        if (menuList == null) {
            return null;
        }

        String parentMenuPathId = menu.getPath_id();

        ArrayList<Menu> tmpList = new ArrayList<Menu>();

        int lenOfPathId;
        String pathId;

        tmpList.clear();
        if (parentMenuPathId.equals(ROOT)) {
            for (int i = 0; i < menuList.size(); i++) {
                Menu item = menuList.get(i);
                pathId = item.getPath_id();
                lenOfPathId = pathId.length();

                if (lenOfPathId > 1) {
                    if (pathId.indexOf(ROOT, 1) == -1) {
                        if (!isHidden(item)) {
                            item.setRoot(isLast || pathId.contains("/home"));
                            item.setParentOrgId(menu.getId());
                            item.setClickable(true);
                            tmpList.add(item);
                        }
                    }
                }

            }

        } else {
            int lenOfParentPathId = parentMenuPathId.length();

            for (int i = 0; i < menuList.size(); i++) {
                Menu item = menuList.get(i);
                pathId = item.getPath_id();
                lenOfPathId = pathId.length();

                if (!pathId.startsWith(parentMenuPathId)) {

                    continue;
                }
                String tmp = pathId.substring(parentMenuPathId.length());
                if (tmp.startsWith("/") && pathId.startsWith(parentMenuPathId) && lenOfPathId > lenOfParentPathId) {
                    int size = getPathSize(tmp);
                    if (size == 1) {
                        if (!isHidden(item)) {
                            item.setRoot(isLast || pathId.contains("/home"));// isLast for settingGroup,
                            item.setParentOrgId(menu.getId());
                            item.setClickable(true);
                            tmpList.add(item);
                        }
                    }
                }
            }
        }


        return tmpList;
    }

    public boolean hasChild(Menu menu) {
        if (hashMapMenu == null) {
            return false;
        }

        ArrayList<Menu> list = hashMapMenu.get(menu.getId());

        return list != null && !list.isEmpty();
    }

    public ArrayList<Menu> getListChilds(Menu menu) {
        if (hashMapMenu == null) {
            return null;
        }

        if (menu == null || menu.getId() == null) {
            return null;
        }

        return hashMapMenu.get(menu.getId());
    }

    public ArrayList<Menu> getListChilds(Menu menu, boolean isLast) {
        ArrayList<Menu> list = getListChilds(menu);

        if (!isLast) {
            return list;
        } else {
            if (list != null && list.size() > 0) {
                Menu menu1 = list.get(list.size() - 1);
                menu1.setLast(true);
            }

            return list;
        }
    }


    public ArrayList<Menu> getSubmenu(String parentMenuPathId, boolean lastIndexCheck) {

        if (menuList == null) {
            return null;
        }

        ArrayList<Menu> tmpList = new ArrayList<Menu>();

        int lenOfPathId;
        String pathId;

        tmpList.clear();
        if (parentMenuPathId.equals(ROOT)) {
            for (int i = 0; i < menuList.size(); i++) {
                Menu item = menuList.get(i);
                pathId = item.getPath_id();
                lenOfPathId = pathId.length();

                if (lenOfPathId > 1) {
                    if (pathId.indexOf(ROOT, 1) == -1) {
                        if (!isHidden(item)) {
                            tmpList.add(item);
                        }
                    }
                }

            }

        } else {
            int lenOfParentPathId = parentMenuPathId.length();

            for (int i = 0; i < menuList.size(); i++) {
                Menu item = menuList.get(i);
                pathId = item.getPath_id();
                lenOfPathId = pathId.length();

                if (!pathId.startsWith(parentMenuPathId)) {

                    continue;
                }
                String tmp = pathId.substring(parentMenuPathId.length());
                if (tmp.startsWith("/") && pathId.startsWith(parentMenuPathId) && lenOfPathId > lenOfParentPathId) {
                    int size = getPathSize(tmp);
                    if (size == 1) {
                        if (!isHidden(item)) {
                            tmpList.add(item);
                        }
                    }
                }
            }
        }

        if (tmpList != null && tmpList.size() > 0) {
            int size = tmpList.size();
            tmpList.get(size - 1).setLast(!lastIndexCheck);
        }

        return tmpList;
    }

    public ArrayList<Menu> getSubCategoryMenu(String parentMenuPathId) {

        if (menuList == null) {
            return null;
        }

        ArrayList<Menu> tmpList = new ArrayList<Menu>();

        int lenOfPathId;
        String pathId;

        tmpList.clear();
        if (parentMenuPathId.equals(ROOT)) {
            for (int i = 0; i < menuList.size(); i++) {
                Menu item = menuList.get(i);
                pathId = item.getPath_id();
                lenOfPathId = pathId.length();

                if (lenOfPathId > 1) {
                    if (pathId.indexOf(ROOT, 1) == -1) {
                        if (!isHidden(item) && item.getType().equalsIgnoreCase(MenuFields.TYPE_PROGRAM_CATEGORY)) {
                            tmpList.add(item);
                        }
                    }
                }

            }

        } else {
            int lenOfParentPathId = parentMenuPathId.length();

            for (int i = 0; i < menuList.size(); i++) {
                Menu item = menuList.get(i);
                pathId = item.getPath_id();
                lenOfPathId = pathId.length();

                if (!pathId.startsWith(parentMenuPathId)) {

                    continue;
                }
                String tmp = pathId.substring(parentMenuPathId.length());
                if (tmp.startsWith("/") && pathId.startsWith(parentMenuPathId) && lenOfPathId > lenOfParentPathId) {
                    int size = getPathSize(tmp);
                    if (size == 1) {
                        if (!isHidden(item) && item.getType().equalsIgnoreCase(MenuFields.TYPE_PROGRAM_CATEGORY)) {
                            tmpList.add(item);
                        }
                    }
                }
            }
        }

        return tmpList;
    }


//    private ArrayList<Menu> getCurrentMenuList(String currentPathId) {
//
//        if (menuList == null) {
//            return null;
//        }
//        int lastNum = currentPathId.lastIndexOf("/");
//
//        String parentPath = currentPathId.substring(0, lastNum).trim();
//
//        if (parentPath.equals("")) {
//            parentPath = ROOT;
//        }
//
//        if (parentPath.equals(ROOT)) {
//            return getRootMenu();
//        }
//
//
//        ArrayList<Menu> tmpList = new ArrayList<Menu>();
//        tmpList.clear();
//        int lenOfParentPathId = parentPath.length();
//
//        for (int i = 0; i < menuList.size(); i++) {
//            Menu item = menuList.get(i);
//            String pathId = item.getPath_id();
//            int lenOfPathId = pathId.length();
//
//            if (!pathId.startsWith(parentPath)) {
//                continue;
//            }
//            String tmp = pathId.substring(parentPath.length());
//            if (tmp.startsWith("/") && pathId.startsWith(parentPath) && lenOfPathId > lenOfParentPathId) {
//                int size = getPathSize(tmp);
//                if (size == 1) {
//                    if (!isHidden(item)) {
//                        tmpList.add(item);
//                    }
//                }
//            }
//        }
//
//        return tmpList;
//    }

    private boolean isHidden(Menu menu) {
        boolean isHidden = false;
        if (menu != null) {

            String value = menu.getConfig(MenuFields.CONF_HIDDEN);
            if (value != null) {
                isHidden = new Boolean(value).booleanValue();
            }
        }
        return isHidden;
    }

    private int getPathSize(String path) {
        String[] paths = path.split("\\/");
        int cnt = 0;

        for (int i = 0; i < paths.length; i++) {
            paths[i] = paths[i].trim();
            if (paths[i] != null && !paths[i].equals("")) {
                cnt++;
            }
        }

        return cnt;
    }


    public boolean isLeafCategory(Menu menu) {
        boolean isLeaf = false;
        if (menu.getType().equals(MenuFields.TYPE_PROGRAM_CATEGORY)) {
            String value = menu.getConfig(MenuFields.CONF_LEAF_CATEGORY);
            if (value != null) {
                isLeaf = Boolean.parseBoolean(value);
            }
        } else if (menu.getType().equals(MenuFields.TYPE_PROGRAM)) {
            isLeaf = true;
        }
        return isLeaf;
    }

    public boolean isSeries(Menu menu) {
        boolean isLeaf = isLeafCategory(menu);
        return !isLeaf && menu.isSeries();
    }

    public ArrayList<Menu> getListParentMenu() {
        return listParentMenu;
    }

    public void setListParentMenu(ArrayList<Menu> listParentMenu) {
        this.listParentMenu = listParentMenu;
    }

    public HashMap<String, ArrayList<Menu>> getHashMapMenu() {
        return hashMapMenu;
    }

    public void setHashMapMenu(HashMap<String, ArrayList<Menu>> hashMapMenu) {
        this.hashMapMenu = hashMapMenu;
    }

    public void pushMenuToPath(Menu menu) {
        menuPath.add(menu);
    }

    public Menu popMenu() {
        if (menuPath.size() == 0) {
            return null;
        }
//        Menu menu = menuPath.get(menuPath.size() - 1);
        menuPath.remove(menuPath.size() - 1);
        return menuPath.get(menuPath.size() - 1);
    }

    public void removeAllLeaf() {
        if (menuPath.isEmpty()) {
            return;
        }

        Menu menu = menuPath.get(menuPath.size() - 1);
        while (!menu.isRoot()) {
            menuPath.remove(menuPath.size() - 1);
            if (menuPath.isEmpty()) {
                break;
            } else {
                menu = menuPath.get(menuPath.size() - 1);
            }
        }
    }

    public void clearPath() {
        menuPath.clear();
    }

    public interface OnMenuSelectedListener {
        public void needLoading(boolean needLoading);

        public void onSuccess(ArrayList menus);

        public void onLeafMenu(Object menu);

        public void onFail(int why);

        public void onError(ApiError error);
    }


    public interface OnVodDetailListener {
        public void needLoading(boolean needLoading);

        public void onSuccess(Vod vod);

        public void onFail(int why);

        public void onError(ApiError error);
    }
}
