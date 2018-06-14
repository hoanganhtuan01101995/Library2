package com.alticast.viettelottcommons.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.alticast.viettelottcommons.resource.AccessToken;
import com.alticast.viettelottcommons.resource.Login;
import com.alticast.viettelottcommons.resource.MyDeviceAccount;
import com.alticast.viettelottcommons.resource.PairingRes;
import com.alticast.viettelottcommons.util.Logger;

/**
 * Created by mc.kim on 7/28/2016.
 */
public class HandheldAuthorization {

    public static final String LOGIN_TOKEN = "LOGIN_TOKEN";
    public static final String LOGIN_TOKEN_SECRET = "LOGIN_TOKEN_SECRET";
    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    public static final String SECRET_TOKEN = "SECRET_TOKEN";
    public static final String STATUS = "STATUS";
    public static final String IS_POOR_USER = "IS_POOR_USER";
    public static final String PAIR_ACCESS_TOKEN = "PAIR_ACCESS_TOKEN";
    public static final String PAIR_TOKEN_SECRET = "PAIR_TOKEN_SECRET";
    public static final String PAIR_FAMILY_NAME = "PAIR_FAMILY_NAME";
    public static final String PAIR_SO_ID = "PAIR_SO_ID";
    public static final String PAIR_CUST_ID = "PAIR_CUST_ID";
    public static final String ID = "ID";
    public static final String PW = "PW";
    public static final String UDID = "UDID";
    public static final String DEVICE_ID = "DEVICE_ID";
    public static final String IS_KEEP_SIGN_IN = "IS_KEEP_SIGN_IN";
    public static final String CHARGE_ACCOUNT = "CHARGE_ACCOUNT";
    public static final String SALE_CODE = "SALE_CODE";
    public static final String WALLET_CHARGE_PHONE = "WALLET_CHARGE_PHONE";
    public static final String WALLET_CHARGE_PHONE_REMEMVER = "WALLET_CHARGE_PHONE_REMEMVER";

    public static final String IS_AUTO_LOGIN = "IS_AUTO_LOGIN";
    public static final String IS_ERROR_U0124 = "U0124";

    // check when account is login success or not
    public static final String IS_LOGIN_SUCCES = "IS_LOGIN_SUCCES";

    public static final String WARNING_MONTH = "WARNING_MONTH";
    public static final String WARNING_STATE = "WARNING_STATE";

    public static final String LAST_TIME_CRASH = "LAST_TIME_CRASH";

    private static HandheldAuthorization ourInstance = null;
    private boolean initialized = false;


    private boolean isPairedHeader = false;

    private HandheldAuthorization() {
        currentUser = null;
    }


    public synchronized static HandheldAuthorization getInstance() {
        if (ourInstance == null) {
            ourInstance = new HandheldAuthorization();
        }
        return ourInstance;
    }

    public void initSharePreference(Context c) {
        mSharedPreferences = c.getSharedPreferences("HandheldAuthorization", Context.MODE_PRIVATE);
    }

    private LoginUserInfo currentUser = null;
    private Context context = null;
    private SharedPreferences mSharedPreferences;

    public void initialize(Context c) {

        currentUser = null;
        context = c;
        initialized = true;
        isPairedHeader = false;
        mSharedPreferences = c.getSharedPreferences("HandheldAuthorization", Context.MODE_PRIVATE);

        String loginToken = mSharedPreferences.getString(LOGIN_TOKEN, "");
        String id = mSharedPreferences.getString(ID, "");
        if (loginToken != null && !loginToken.isEmpty() && id != null && !id.isEmpty()) {
            currentUser = new LoginUserInfo();
            currentUser.setLoginAccessToken(loginToken);
            currentUser.setId(id);
            currentUser.setLoginTokenSecret(mSharedPreferences.getString(SECRET_TOKEN, ""));
//            currentUser.setDeviceUDID(mSharedPreferences.getString(UDID, ""));
            currentUser.setPassword(mSharedPreferences.getString(PW, ""));
            currentUser.setRefresh_token(mSharedPreferences.getString(REFRESH_TOKEN, ""));
            currentUser.setStatus(mSharedPreferences.getString(STATUS, ""));
            currentUser.setWarningOptionMonth(mSharedPreferences.getInt(WARNING_MONTH, 0));
            currentUser.setWarningOptionState(mSharedPreferences.getBoolean(WARNING_STATE, false));

            String pairAccessToken = mSharedPreferences.getString(PAIR_ACCESS_TOKEN, "");
            if (pairAccessToken != null && !pairAccessToken.isEmpty()) {
                AccessToken accessToken = new AccessToken();
                accessToken.setAccess_token(pairAccessToken);
                accessToken.setToken_secret(mSharedPreferences.getString(PAIR_TOKEN_SECRET, ""));
                accessToken.setFamilyName(mSharedPreferences.getString(PAIR_FAMILY_NAME, ""));

                currentUser.setAccessToken(accessToken);
                String soID = mSharedPreferences.getString(PAIR_SO_ID, "");
                String custID = mSharedPreferences.getString(PAIR_CUST_ID, "");
                currentUser.setSo_id(soID);
                currentUser.setCust_id(custID);
            }
        }
    }

    public boolean isAuthened() {
        return currentUser != null;
    }

    public void putString(String field, String value) {
        if (mSharedPreferences != null) {
            mSharedPreferences.edit().putString(field, value).commit();
        }
    }

    public void putLong(String field, long value) {
        if (mSharedPreferences != null) {
            mSharedPreferences.edit().putLong(field, value).commit();
        }
    }

    public void putInt(String field, int value) {
        if (mSharedPreferences != null) {
            mSharedPreferences.edit().putInt(field, value).commit();
        }
    }

    public void putBoolean(String field, boolean value) {
        if (mSharedPreferences != null) {
            mSharedPreferences.edit().putBoolean(field, value).commit();
        }
    }

    public String getString(String key) {
        if (mSharedPreferences != null) {
            return mSharedPreferences.getString(key, "");
        }

        return "";
    }

    public long getLong(String key) {
        if (mSharedPreferences != null) {
            return mSharedPreferences.getLong(key, 0);
        }

        return 0;
    }

    public int getPaymentMethod() {
        if (mSharedPreferences != null) {
            return mSharedPreferences.getInt(CHARGE_ACCOUNT, 0);
        }
        return 0;
    }

    public void setPaymentMethod(int method) {
        if (mSharedPreferences != null) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putInt(CHARGE_ACCOUNT, method).commit();
            editor.commit();
        }
    }

    public int getInt(String key) {
        if (mSharedPreferences != null) {
            return mSharedPreferences.getInt(key, 0);
        }

        return 0;
    }

    public boolean getBoolean(String key) {
        if (mSharedPreferences != null) {
            return mSharedPreferences.getBoolean(key, false);
        }

        return false;
    }

    public boolean isPairing() {
        if (currentUser == null) {
            return false;
        }
        return currentUser.getAccessToken() != null;
    }

    public String getCurrentId() {
        if (currentUser == null) {
            return null;
        }
        return currentUser.id;
    }

    public boolean isLogIn() {
        if (currentUser == null) {
            return false;
        }
        return currentUser.getLoginAccessToken() != null;
    }

    public String getTokenSecret() {
        if (isPairing()) {
            return currentUser.getAccessToken().getToken_secret();
        } else {
            return currentUser.getLoginTokenSecret();
        }
    }


    public void logOut() {
        if (mSharedPreferences != null) {
            mSharedPreferences.edit().clear().commit();
        }
        currentUser = null;
    }

    public void setWarningState(boolean state, int month) {
        currentUser.setIsWarningOptionState(state);
        currentUser.setWarningOptionMonth(month);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(WARNING_MONTH, month).commit();
        editor.putBoolean(WARNING_STATE, state).commit();
        editor.commit();
    }

    public void saveDeviceId(String deviceId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(DEVICE_ID, deviceId).commit();
        editor.commit();
    }

    public String getDeviceId() {
        if (mSharedPreferences != null) {
            return mSharedPreferences.getString(DEVICE_ID, null);
        }

        return null;
    }

    public boolean getWaringState(int month) {
        int currentMonth = currentUser.getWarningOptionMonth();
        if (currentMonth == month) {
            return currentUser.isWarningOptionState();
        } else {
            return false;
        }
    }


    public void changePassword(String password) {
        if (currentUser != null)
            currentUser.setPassword(password);
    }

    public void loginInfoInit(String id, String password, String udid, Login loginRes) {

        if (currentUser == null) {
            currentUser = new LoginUserInfo();
        }

        String loginAccessToken = loginRes.getAccess_token();
        currentUser.setLoginTokenSecret(loginRes.getToken_secret());
        currentUser.setDeviceUDID(udid);
        currentUser.setId(id);
        currentUser.setPassword(password);
        currentUser.setLoginAccessToken(loginAccessToken);
        currentUser.setRefresh_token(loginRes.getRefresh_token());
        currentUser.setStatus(loginRes.getStatus());

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LOGIN_TOKEN, loginAccessToken).commit();
        editor.putString(ID, id).commit();
        editor.putString(PW, password).commit();
        editor.putString(REFRESH_TOKEN, loginRes.getRefresh_token()).commit();
        editor.putString(UDID, udid).commit();
        editor.putString(SECRET_TOKEN, loginRes.getToken_secret()).commit();
        editor.putString(STATUS, loginRes.getStatus()).commit();
//        editor.commit();

    }

    public void loginInfoInitRefreshToken(Login loginRes) {

        if (currentUser == null) {
            currentUser = new LoginUserInfo();
        }

        String loginAccessToken = loginRes.getAccess_token();
        currentUser.setLoginTokenSecret(loginRes.getToken_secret());
        currentUser.setLoginAccessToken(loginAccessToken);
        currentUser.setRefresh_token(loginRes.getRefresh_token());
        currentUser.setStatus(loginRes.getStatus());

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LOGIN_TOKEN, loginAccessToken).commit();
        editor.putString(REFRESH_TOKEN, loginRes.getRefresh_token()).commit();
        editor.putString(SECRET_TOKEN, loginRes.getToken_secret()).commit();
        editor.putString(STATUS, loginRes.getStatus()).commit();

        HandheldAuthorization.getInstance().setLoginInfoSuccessState(true);
    }

    public void setLoginInfoSuccessState(boolean isSuccess) {
        putBoolean(IS_LOGIN_SUCCES, isSuccess);
    }

    public boolean isPoorUser() {
        if (mSharedPreferences != null) {
            return mSharedPreferences.getBoolean(IS_POOR_USER, false);
        }

        return false;
    }

    public void setIsPoorUser(boolean isPoorUser) {
        putBoolean(IS_POOR_USER, isPoorUser);
    }

    public boolean getLoginInfoSuccessState() {
        return getBoolean(IS_LOGIN_SUCCES);
    }

    public boolean isQuickOption() {
        return currentUser.isQuickPurchase();
    }

    public void changeQuickOption(boolean isOn) {
        currentUser.setQuickPurchase(isOn);
    }

    public void setLoginToken(Login loginRes) {

        if (currentUser == null) {
            currentUser = new LoginUserInfo();
        }

        String loginAccessToken = loginRes.getAccess_token();
        currentUser.setLoginTokenSecret(loginRes.getToken_secret());
        currentUser.setLoginAccessToken(loginAccessToken);
        currentUser.setRefresh_token(loginRes.getRefresh_token());
        currentUser.setStatus(loginRes.getStatus());

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LOGIN_TOKEN, loginAccessToken).commit();
        editor.putString(REFRESH_TOKEN, loginRes.getRefresh_token()).commit();
        editor.putString(SECRET_TOKEN, loginRes.getToken_secret()).commit();
        editor.putString(STATUS, loginRes.getStatus()).commit();

    }

    // Only for creating test data
    public void setCustomLoginAccessToken(String loginAccessToken) {
        if (currentUser == null) {
            currentUser = new LoginUserInfo();
        }
        currentUser.setLoginAccessToken(loginAccessToken);
    }

    public void setDeviceAccount(MyDeviceAccount myDeviceAccount) {
        if (currentUser == null) {
            return;
        }
        getCurrentUser().setAccount(myDeviceAccount);
    }

    public void setPairingAccessToken(AccessToken accessToken) {
        currentUser.setAccessToken(accessToken);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (accessToken == null) {
            editor.putString(PAIR_ACCESS_TOKEN, null);
            editor.putString(PAIR_TOKEN_SECRET, null);
            editor.putString(PAIR_FAMILY_NAME, null);
            editor.commit();
            return;
        }
        editor.putString(PAIR_ACCESS_TOKEN, accessToken.getAccess_token());
        editor.putString(PAIR_TOKEN_SECRET, accessToken.getToken_secret());
        editor.putString(PAIR_FAMILY_NAME, accessToken.getFamilyName());
        editor.commit();
    }

    public void setSoId(String soId) {
        currentUser.setSo_id(soId);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(PAIR_SO_ID, soId);
        editor.commit();
    }

    public void setCust_id(String cust_id) {
        currentUser.cust_id = cust_id;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(PAIR_CUST_ID, cust_id);
        editor.commit();
    }

    public String getSTBUdid() {
        if (currentUser == null || currentUser.getAccount() == null || currentUser.getAccount().getPairing() == null) {
            return null;
        }

        return currentUser.getAccount().getPairing().getDevice_udid();

    }

    public void setIsPointUser(boolean state) {
        currentUser.setmIsPointUser(state);
    }

    public boolean isPointUser() {
        return currentUser.ismIsPointUser();
    }

    public LoginUserInfo getCurrentUser() {
        return currentUser;
    }

    public String getPairedSTBName() {
        if (getCurrentUser() == null) {
            return null;
        }
        if (getCurrentUser().getAccount() == null) {
            return null;
        }
        if (getCurrentUser().getAccount().getPairing() == null) {
            return null;
        }
        if (getCurrentUser().getAccount().getPairing().getPaired_alias() == null) {
            return null;
        }


        return getCurrentUser().getAccount().getPairing().getPaired_alias();
    }

    public void setPairingData(PairingRes pairingData) {
        currentUser.setPairingData(pairingData);
    }

    public class LoginUserInfo {
        private String id = null;
        private String password = null;
        private String loginAccessToken = null;
        private String loginTokenSecret = null;
        private String deviceUDID = null;
        private String status = null;

        private AccessToken accessToken = null;
        private PairingRes pairingData = null;
        private int warningOptionMonth = 0;
        private boolean isWarningOptionState = false;

        private boolean quickPurchase;
        private String so_id;

        private String cust_id;

        private MyDeviceAccount account = null;
        private boolean mIsPointUser;
        private boolean mIsPoorUser;

        public String getPaymentOption() {
            return account != null ? account.getPaymentOption() : null;
        }

        public void setPaymentOption(String method) {
            if (account != null) {
                account.setPaymentOption(method);
            }
        }

        public MyDeviceAccount getAccount() {
            return account;
        }

        public void setAccount(MyDeviceAccount account) {
            this.account = account;
            if (account.getPaymentOption().equals("MB")) {
                HandheldAuthorization.getInstance().putInt(HandheldAuthorization.CHARGE_ACCOUNT, 0);
            } else {
                HandheldAuthorization.getInstance().putInt(HandheldAuthorization.CHARGE_ACCOUNT, 1);
            }
            this.id = account.getId();
        }

        private String refresh_token = null;

        public String getLoginTokenSecret() {
            return loginTokenSecret;
        }

        public void setLoginTokenSecret(String loginTokenSecret) {
            this.loginTokenSecret = loginTokenSecret;
        }

        public void setWarningOptionState(boolean warningOptionState) {
            isWarningOptionState = warningOptionState;
        }

        public boolean ismIsPointUser() {
            return mIsPointUser;
        }

        public void setmIsPointUser(boolean mIsPointUser) {
            this.mIsPointUser = mIsPointUser;
        }

        public boolean ismIsPoorUser() {
            return mIsPoorUser;
        }

        public void setmIsPoorUser(boolean mIsPoorUser) {
            this.mIsPoorUser = mIsPoorUser;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }


        public int getWarningOptionMonth() {
            return warningOptionMonth;
        }

        public void setWarningOptionMonth(int warningOptionMonth) {
            this.warningOptionMonth = warningOptionMonth;
        }

        public boolean isWarningOptionState() {
            return isWarningOptionState;
        }

        public void setIsWarningOptionState(boolean isWarningOptionState) {
            this.isWarningOptionState = isWarningOptionState;
        }

        public PairingRes getPairingData() {
            return pairingData;
        }

        public void setPairingData(PairingRes pairingData) {
            this.pairingData = pairingData;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getLoginAccessToken() {
            return loginAccessToken;
        }

        public void setLoginAccessToken(String loginAccessToken) {
            this.loginAccessToken = loginAccessToken;
        }

        public AccessToken getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(AccessToken accessToken) {
            this.accessToken = accessToken;
        }

        public String getDeviceUDID() {
            return deviceUDID;
        }

        public void setDeviceUDID(String deviceUDID) {
            this.deviceUDID = deviceUDID;
        }

        public boolean isPaired() {
            return pairingData != null;
        }

        public boolean isViettelMobileBalanceUser() {

            if (account == null) {
                Logger.print("HandheldAuthorization ", "isViettelMobileBalanceUser account is null ");
                return false;
            }

            if (account.getConfig() == null) {
                Logger.print("HandheldAuthorization ", "isViettelMobileBalanceUser account.getConfig() is null ");
                return false;
            }

            return account.getConfig().isViettelMobileBalanceUser();
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSo_id() {
            return so_id;
        }

        public void setSo_id(String so_id) {
            this.so_id = so_id;
        }

        public String getCust_id() {
            return cust_id;
        }

        public void setCust_id(String cust_id) {
            this.cust_id = cust_id;
        }

        public boolean isQuickPurchase() {
            return quickPurchase;
        }

        public void setQuickPurchase(boolean quickPurchase) {
            this.quickPurchase = quickPurchase;
        }
    }


}
