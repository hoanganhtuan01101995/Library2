package com.alticast.viettelottcommons;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.alticast.android.util.Log;
import com.alticast.media.AltiPlayer;
import com.alticast.viettelottcommons.loader.CampaignLoader;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.Rs;
import com.alticast.viettelottcommons.util.Util;

import java.io.File;

/**
 * Created by mc.kim on 7/28/2016.
 */
public class WindmillConfiguration {

    private static final int LIVE_TYPE = 0;
    private static final int KR_TEST = 1;
    private static final int VT_TEST = 2;

    // Server config
    public static boolean isAdVersion = true;
    //        public static boolean isAdVersion = false;
    public static boolean isAdPostEnable = true;
    public static boolean isAdImageEnable = false;
    //        public static boolean isAdPostEnable = false;
    public static boolean isNotUseLock = false;
    public static int version = 1;

    public static boolean NETWORK_FAKE = true;
    //    public static boolean NETWORK_FAKE = false;
    public static boolean is3gMode = false;
    //    public static boolean isSendHeartBeat = false;
    public static boolean isSendHeartBeat = false;

    //    public static boolean isUseNewPrepareApi = true;
    public static boolean isUseNewPrepareApi = false;

    //    public static boolean isAnnoucement = false;
    public static boolean isAnnoucement = true;

    //        public static boolean isTrailerPhotoEnable= true;
    public static boolean isTrailerPhotoEnable = false;

    //    public static boolean is
// AutoReloadLiveChannel = false;
    public static boolean isAutoReloadLiveChannel = true;

    //    public static boolean isAlacaterVersion = false;
    public static boolean isAlacaterVersion = true;
    public static boolean is3GQuetoiVersion = true;
    //    public static boolean isBigSmallPackageVersion = false;
    public static boolean isBigSmallPackageVersion = true;

    public static boolean isEnableCatchingException = true;
    //    public static boolean isEnableCatchingException = false;
    public static boolean isEnableRVOD = false;
    public static boolean isEnableChromeCast = true;
    public static boolean isEnableChromeCastDEVICE = true;
//    public static boolean isEnableChromeCastDEVICE = false;

    public static boolean isOnAirEnable = true;
    //    public static boolean isOnAirEnable = false;
    public static boolean isVodDetailTypeB = true;
//    public static boolean isVodDetailTypeB = false;

    public static boolean isEnablePromotionBanner = true;
    //    public static boolean isEnablePromotionBanner = false;
    public static boolean isEnablePromotionSubMenuBanner = true;
//    public static boolean isEnablePromotionSubMenuBanner = false;

    public static boolean isEnableRotationDevice = true;
    public static boolean isEnableCoupon = true;
//    public static boolean isEnableRotationDevice = false;

    //    public static boolean isTestBandWidth= true;
    public static boolean isTestBandWidth = false;
    public static boolean isUseingDLicense = false;

    //    public static final String MENUS_VERSION = "OTT_Live";
    public static final String MENUS_VERSION = "OTT_NewUI";

    public static String clientId = null;//v1.1.4
    public static String secretKey = null;//v 1.1.4
    public static String proxyIp = null;
    public static String baseADDS = null;
    public static String baseUrl = null;

    public static String baseHttpsUrl = null;

    public static boolean isFakeTimerEvent = false;

    //        public static int LOG_LEVEL = Log.LEVEL_NONE;
//    public static final int LOG_LEVEL = Log.LEVEL_MSG;
    public static int LOG_LEVEL = Log.LEVEL_DBG;
    public static String model = "ANDROID_PHONE";
    public static String type = "phone";

    public static boolean isUseCastisPromotion = true;
//    public static boolean isUseCastisPromotion = false;

    public static boolean isNewIFrame = true;
//    public static boolean isNewIFrame = false;

    public static String testPushWoodString = "12B82-C2269";

    //    public static boolean PLAYER_LOG_ON = true;
    public static boolean PLAYER_LOG_ON = false;

    public static boolean isNewUIPhase2 = false;
    //    public static int serverType = LIVE_TYPE;
    public static String promotionCampain = CampaignLoader.PHONE_PROMO;
    //    public static boolean isTrailerPhotoEnable= true;
//    public static boolean isTrailerPhotoEnable= false;
//    public static int serverType = KR_TEST;
    public static int serverType = VT_TEST;


    // Check version in manifest
    public static final String chromeUrl = "http://110.35.173.30:18080";
    public static final String SENDLOG_URL = "http://logger-20170212.vietteltv.com.vn";
//    public static final String SENDLOG_URL = "http:/";

    public static boolean LIVE = false;
    public static boolean isIgnoreCheckPayment = true;


    static {
        switch (serverType) {
            case LIVE_TYPE:
//                isUseNewPrepareApi = false;
                baseUrl = "http://otttv.viettel.com.vn/";
//                baseHttpsUrl = "https://otttv.viettel.com.vn/";
                baseHttpsUrl = "http://otttv.viettel.com.vn/";
                baseADDS = "https://ottad.viettel.com.vn/";
//                clientId = "ff80808155ff208c0156c5fa75a60002";
//                secretKey = "7a8b0aeb92974e0bb2a9490dcf7a6332";

//                clientId = "ff80808155ff208c01586ce233910007";
//                secretKey = "c21b0057a1ce4d219d732895226c7a56";
                clientId = "ff8080815a2436fc015a5e4879ae0002";
                secretKey = "44a03c2559d74e9b9acc4378c3683d5b";

//                clientId = "ff808081542e0e290154c33074f00012";
//                secretKey = "c3c07de6548343d5847630ce73849af6";
                proxyIp = "otttv.viettel.com.vn";
                LIVE = true;
                NETWORK_FAKE = false;
                break;
            case KR_TEST:
                baseUrl = "http://58.140.89.35:18080/";
                baseADDS = "http://58.140.89.35:18080/";
                clientId = "ff808081542e0e290154c33074f00012";
                secretKey = "c3c07de6548343d5847630ce73849af6";
                proxyIp = "58.140.89.35";
                LIVE = false;
                break;
            case VT_TEST:
                baseUrl = "http://10.60.70.209:18080/";
                baseHttpsUrl = "https://10.60.70.209:443/";
//                baseHttpsUrl = "http://10.60.70.209:18080/";
                baseADDS = "http://10.60.70.228:18080/";
                clientId = "viettelSdpClient2";
                secretKey = "viettelSdpUserprofile1";
                proxyIp = "10.60.70.209";
                LIVE = false;
//                isAdVersion = false;
//                isAdPostEnable = false;
                break;
        }

    }

    // Device config

    public static int scrWidth, scrHeight;
    public static int posterWidth, posterHeight;
    public static int topairWidth, topairHeight;
    public static int topairTabletWidth, topairTabletHeight;
    public static int posterWidthTablet, posterHeightTablet;
    public static int posterWidthTabletRelated, posterHeightTabletRelated;
    public static int promotionWidth, promotionHeight;
    public static int promotionMIDHeight;
    public static int bottomWidth;
    public static int statusbarHeight;
    public static int navigationHeight;
    public static int promotionWidthTablet, promotionHeightTablet;
    public static float scale;
    public static float density;
    public static boolean isHasNavBar;

    // Application config
    public static final boolean MULTI_LANGUAGE = true;
    public static final String GUEST_REGION = "GUEST";
    public static String COUNTRY = "KR";
    public static String LANGUAGE = "eng";
    public static String LANGUAGE_ENG = "eng";
    public static String LANGUAGE_VIE = "vie";
    public static String defaultPassword = "0000";
    public static String guestToken = "00536aefb1f78bca51f8b3fde6f643c5";
    //     public static String guestToken = "__touchme__";
    public static final String CURRENCY_VND = "VND";
    public static final String CURRENCY_POINT = "PNT";
    public static String applicationVersion = null;
    public static String deviceId;

    /**
     * Instantiates a new windmill configuration.
     */

    public static void init(Context context, String language) {


//        LANGUAGE = LANGUAGE_VIE;
//
//        Locale locale = new Locale("vi");
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

//        LANGUAGE = language;

//        model = Util.isTablet(context) ? "ANDROID_PAD" : "ANDROID_PHONE";
//        type = Util.isTablet(context) ? "pad" : "phone";

        applicationVersion = getVersion(context);

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        scrHeight = metrics.heightPixels;
        scrWidth = metrics.widthPixels;

        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusbarHeight = (int) context.getResources().getDimension(resourceId);
        }
        android.util.Log.e("statusbarHeight", "" + statusbarHeight);

        isHasNavBar = hasNavBar(context);

        if (isHasNavBar) {
            int resourceIdNavi = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceIdNavi > 0) {
                navigationHeight = (int) context.getResources().getDimension(resourceIdNavi);
            }
        }
        android.util.Log.e("navigationHeight", "" + navigationHeight);

        scale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        density = context.getApplicationContext().getResources().getDisplayMetrics().density;
        posterWidth = (scrWidth - (int) (context.getResources().getDimension(R.dimen.posterMarginBorder) * 2) - (int) (context.getResources().getDimension(R.dimen.smallMargin) * 3)) / 3;
        posterHeight = posterWidth * 296 / 212; // rate from design
        topairWidth = (scrWidth - (int) (context.getResources().getDimension(R.dimen.posterMarginBorder) * 2) - (int) (context.getResources().getDimension(R.dimen.smallMargin) * 3)) / 2;
        topairHeight = topairWidth * 176 / 308;
        topairTabletWidth = (scrWidth - (int) (context.getResources().getDimension(R.dimen.posterMarginBorder) * 2) - (int) (context.getResources().getDimension(R.dimen.smallMargin) * 5)) / 4;
        topairTabletHeight = topairTabletWidth * 176 / 308;

        posterWidthTablet = (scrWidth - (int) (context.getResources().getDimension(R.dimen.posterMarginBorder) * 2) - (int) (context.getResources().getDimension(R.dimen.smallMargin) * 6)) / 6;
        posterHeightTablet = posterWidthTablet * 296 / 212; // rate from design

        bottomWidth = scrWidth / 5;

//        posterWidthTabletRelated = (scrWidth- (int) (context.getResources().getDimension(R.dimen.player_width) )
//                - (int) (context.getResources().getDimension(R.dimen.posterMarginBorder) * 2) - (int) (context.getResources().getDimension(R.dimen.smallMargin) * 6)) / 6;
        posterWidthTabletRelated = (scrWidth - (int) (580 * density)
                - (int) (context.getResources().getDimension(R.dimen.posterMarginBorder) * 2) - (int) (context.getResources().getDimension(R.dimen.smallMargin) * 2)) / 3;
        posterHeightTabletRelated = posterWidthTabletRelated * 296 / 212; // rate from design

        promotionWidth = scrWidth;
        promotionHeight = promotionWidth * 218 / 360;
        promotionWidthTablet = scrWidth;
        promotionHeightTablet = scrWidth * 392 / 1010;

        promotionMIDHeight = (int) (86 * density);
//        promotionHeightTablet = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 392, context.getResources().getDisplayMetrics());

        initDeviceId(context);
////
////        // This is for running on virtual device, please don't remove this
        if (deviceId != null && !deviceId.equals("000000000000000")) {
//            HandheldAuthorization.getInstance().saveDeviceId(deviceId);
            initPlayer(context);
        }
//        initPlayer(context);
//        String id = AltiPlayer.GetDeviceUniqueID();
//        deviceId = id;
//        initPlayer(context);
        Rs.init(context);
    }

    public static void initDeviceId(Context context) {

        if (deviceId == null) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();
        }

        if (deviceId == null) {
            deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }

        if (deviceId != null && !deviceId.contains("ViettelTV_")) {
            deviceId = "ViettelTV_" + deviceId;
        }
    }

    public static boolean hasNavBar(Context context) {
        if (Build.VERSION.SDK_INT <= 19) {
            return false;
        }
        Point realSize = new Point();
        Point screenSize = new Point();
        boolean hasNavBar = false;
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        realSize.x = metrics.widthPixels;
        realSize.y = metrics.heightPixels;
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(screenSize);
        if (realSize.y != screenSize.y) {
            int difference = realSize.y - screenSize.y;
            int navBarHeight = 0;
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                navBarHeight = resources.getDimensionPixelSize(resourceId);
            }
            if (navBarHeight != 0) {
                if (difference == navBarHeight) {
                    hasNavBar = true;
                }
            }

        }
        return hasNavBar;
    }

    public static boolean hasStatusBar(Context context) {
        Point realSize = new Point();
        Point screenSize = new Point();
        boolean hasNavBar = false;
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        realSize.x = metrics.widthPixels;
        realSize.y = metrics.heightPixels;
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(screenSize);
        if (realSize.y != screenSize.y) {
            int difference = realSize.y - screenSize.y;
            int navBarHeight = 0;
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                navBarHeight = resources.getDimensionPixelSize(resourceId);
            }
            if (navBarHeight != 0) {
                if (difference == navBarHeight) {
                    hasNavBar = true;
                }
            }

        }
        return hasNavBar;
    }

    public static void reconfig(Context context, boolean isFull) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        if (isFull) {
            scrHeight = metrics.widthPixels + navigationHeight;
            scrWidth = metrics.heightPixels + statusbarHeight;
        } else {
            scrHeight = metrics.heightPixels;
            scrHeight = metrics.widthPixels;
        }

        android.util.Log.e("reconfig", "Width " + scrWidth + " | Height " + scrHeight);
    }

    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static String getBaseHttpsUrl() {
        return baseHttpsUrl;
    }

    public static String getProxyIp() {
        return proxyIp;
    }


    private static void initPlayer(Context context) {


        String rootDir = context.getApplicationInfo().dataDir;

//        File sdCard = Environment.getExternalStorageDirectory();
//        File dirLog = new File (sdCard.getAbsolutePath() + "/logfolder");
//        dirLog.mkdirs();
//        String rootDir = dirLog.getAbsolutePath();

        final String dir = context.getApplicationInfo().dataDir + "/lib/";
        final String configDirLocation = rootDir + "/files/player/";
        final String drmDirLocation = rootDir + "/Viettel/DRM";
        final String fileName = Util.getCurrentTimeToFileFormat();

        Logger.d("DRM folder", "" + drmDirLocation);

        File drmDir = Util.makeDirectory(drmDirLocation);
        File configDir = Util.makeDirectory(configDirLocation);
        File file = Util.makeFile(configDir, (configDirLocation + fileName));

        String content = new String("TT120430506|812c50b7b11310ce6a1ffcf0b158917c|27.67.49.248|80|ffffc4281ddbdad6f76c0033c583|SHV-E120K|"
                + drmDir.getAbsolutePath() + "|Viettel|");
        Util.writeFile(file, content.getBytes());

        AltiPlayer.init(dir, configDirLocation, context);
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getBaseADDS() {
        return baseADDS;
    }

    private static String getVersion(Context context) {
        PackageInfo i = null;
        try {
            i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String version = i.versionName;

        return version;
    }

}