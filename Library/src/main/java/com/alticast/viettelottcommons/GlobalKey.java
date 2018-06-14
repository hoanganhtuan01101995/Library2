package com.alticast.viettelottcommons;

import com.alticast.viettelottcommons.activity.GlobalActivity;

/**
 * Created by duyuno on 1/12/17.
 */
public class GlobalKey {
    public static interface MainActivityKey {
        public static final String LOGOUT_DATA = "MainActivityKey.LOGOUT_DATA";
        public static final String REFRESH_DATA = "MainActivityKey.REFRESH_DATA";
        public static final String REFRESH_BOTTOM_BAR = "NavigationActivity.REFRESH_BOTTOM_BAR";
        public static final String REFRESH_ACCOUNT = "NavigationActivity.REFRESH_ACCOUNT";
        public static final String IS_CHANNEL_ACTIVITY = "ChannelActivity.CHANNEL_ACTIVITY";
        public static final String IS_CHANNEL_ACTIVITY_AUTHOR = "ChannelActivity.IS_CHANNEL_ACTIVITY_AUTHOR";
        public static final String ACCESS_TOKEN_FAIL = "ACCESS_TOKEN_FAIL";
    }
    public static interface VodController {
        public static final String REFRESH_DATA = "VodControllerFragment.REFRESH_DATA";
        public static final String ACTION_INFO_LOADED = "VodControllerFragment.ACTION_INFO_LOADED";
        public static final String PARAM_START_OFFSET = "VodControllerFragment.PARAM_START_OFFSET";
        public static final String PARAM_SCROLL_DISTANCE = "VodControllerFragment.PARAM_SCROLL_DISTANCE";
        public static final String ACTION_REMOVE_CHECKPOINT = "VodControllerFragment.ACTION_REMOVE_CHECKPOINT";
    }

    public static interface PlayBackKey {
        public static final String CLASS_NAME = GlobalActivity.class.getName();
        public static final String ACTION_MEDIA_FINISHED = CLASS_NAME + ".ACTION_MEDIA_FINISHED";
        public static final String ACTION_MEDIA_RESET = CLASS_NAME + ".ACTION_MEDIA_RESET";
        public static final String ACTION_MEDIA_PREPARED = CLASS_NAME + ".ACTION_MEDIA_PREPARED";
        public static final String ACTION_MEDIA_PAUSED = CLASS_NAME + ".ACTION_MEDIA_PAUSED";
        public static final String ACTION_MEDIA_RESUMED = CLASS_NAME + ".ACTION_MEDIA_RESUMED";
        public static final String ACTION_MEDIA_CHROME_FINISH = CLASS_NAME + ".ACTION_MEDIA_CHROME_FINISH";
        public static final String ACTION_MEDIA_PROGRESS = CLASS_NAME + ".ACTION_MEDIA_PROGRESS";
        public static final String PARAM_MEDIA_DURATION = CLASS_NAME + ".PARAM_MEDIA_DURATION";
        public static final String PARAM_MEDIA_PROGRESS = CLASS_NAME + ".PARAM_MEDIA_PROGRESS";
        public static final String PARAM_PREPARED = CLASS_NAME + ".PARAM_PREPARED";
        public static final String ACTION_SHOW_NORMAL_PROGRESS = CLASS_NAME + ".ACTION_SHOW_NORMAL_PROGRESS";
        public static final String ACTION_PLAY_AD = CLASS_NAME + ".ACTION_PLAY_AD";
        public static final String ACTION_NO_PLAY_AD = CLASS_NAME + ".ACTION_NO_PLAY_AD";
    }
    public static interface NotifiKey {
        public static final String KEY = "key";
        public static final String ID = "id";
        public static final String VALUE = "value";
        public static final String NAME = "name";
        public static final String EPID = "epid";
        public static final String EPNAME = "epname";

        public static final String NOTIFICATION = "NOTIFICATION";
    }

    public static final String LANDSCAPE_MODE = "LANDSCAPE_MODE";
    public static final String PURCHASE_COMPLETE = "PURCHASE_COMPLETE";
    public static final String VWALLET_GO_TO_TOPUP_HISTORY = "VWALLET_GO_TO_TOPUP_HISTORY";

    public static final int REQUEST_CODE_ADS = 9669;
}
