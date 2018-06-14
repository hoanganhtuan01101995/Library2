package com.alticast.viettelottcommons.manager;

import com.alticast.viettelottcommons.WindmillConfiguration;

import java.util.HashMap;

/**
 * Created by mc.kim on 7/28/2016.
 */
public class AuthManager {
    private final int REQUEST_UNPAIRING = 0;

    private static HashMap<UserLevel, AUTH> levelMap = new HashMap<>();
    private static UserLevel currentLevel = null;

    public static enum UserLevel {
        LEVEL1,  //Guest
        LEVEL2,  //Stand Alone
        LEVEL3   //Pairing User
    }

    public interface ChargeAccount {
        public static final int CHARGE_ACCOUNT_MOBILE = 0;
        public static final int CHARGE_ACCOUNT_VWALLET = 1;
    }

    static {
        levelMap.put(UserLevel.LEVEL1, new AUTH(false, false, false));
        levelMap.put(UserLevel.LEVEL2, new AUTH(true, false, true));
        levelMap.put(UserLevel.LEVEL3, new AUTH(true, true, true));
    }

    private static AUTH getAuth(UserLevel level) {
        return levelMap.get(level);
    }

    public static AUTH currentUserAuth() {


        boolean isLogin = HandheldAuthorization.getInstance().isLogIn();
        boolean isPairing = HandheldAuthorization.getInstance().isPairing();
        if (isPairing) {
            currentLevel = UserLevel.LEVEL3;
        } else if (!isPairing && isLogin) {
            currentLevel = UserLevel.LEVEL2;
        } else {
            currentLevel = UserLevel.LEVEL1;
        }
        return getAuth(currentLevel);
    }

    public static UserLevel currentLevel() {
        boolean isLogin = HandheldAuthorization.getInstance().isLogIn();
        boolean isPairing = HandheldAuthorization.getInstance().isPairing();
        if (isPairing) {
            currentLevel = UserLevel.LEVEL3;
        } else if (!isPairing && isLogin) {
            currentLevel = UserLevel.LEVEL2;
        } else {
            currentLevel = UserLevel.LEVEL1;
        }
        return currentLevel;
    }

    public static String getLicense() {

        if(!WindmillConfiguration.isUseingDLicense) {
            return "";
        }

        boolean isPairing = HandheldAuthorization.getInstance().isPairing();
        if (isPairing) {
            return "stb,handheld";
        } else {
            return "handheld";
        }
    }


    public static class AUTH {
        private boolean isFree = false;
        private boolean isFee = false;
        private boolean isPrivate = false;

        public AUTH(boolean isFree, boolean isFee, boolean isPrivate) {
            this.isFree = isFree;
            this.isFee = isFee;
            this.isPrivate = isPrivate;
        }

        public boolean isFree() {
            return isFree;
        }

        public boolean isFee() {
            return isFee;
        }

        public boolean isPrivate() {
            return isPrivate;
        }
    }

    public static String getLoginToken() {


        HandheldAuthorization.LoginUserInfo info = HandheldAuthorization.getInstance().getCurrentUser();

        if (info == null) {
            return null;
        }


        return info.getLoginAccessToken();
    }

    public static String getGuestToken() {

        return WindmillConfiguration.guestToken;
    }

    public static String getAccessToken() {

        String token = null;

        HandheldAuthorization.LoginUserInfo info = HandheldAuthorization.getInstance().getCurrentUser();

        switch (currentLevel()) {
            case LEVEL2:
//                token =  WindmillConfiguration.guestToken;
                token = info.getLoginAccessToken();
                break;
            case LEVEL3:
                token = info.getAccessToken().getAccess_token();
                break;

            default:
                token = WindmillConfiguration.guestToken;
                break;
        }

        return token;

    }

    // Only for creating test data
    public static void setUserLevelAndToken(UserLevel ul, String token) {
        currentLevel = ul;
        HandheldAuthorization.getInstance().setCustomLoginAccessToken(token);
    }




}
