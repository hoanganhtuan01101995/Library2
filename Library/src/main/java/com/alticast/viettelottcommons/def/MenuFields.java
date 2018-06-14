package com.alticast.viettelottcommons.def;

/**
 * Created by mc.kim on 8/8/2016.
 */
public class MenuFields {
    public final static String TYPE_ROOT = "__root";

    public final static String TYPE_MY_ACCOUNT = "user_account";

    public final static String TYPE_CONNECT_TV = "connect_tv";
    /**
     * 일반 메뉴.
     */
    public final static String TYPE_MENU = "__menu";

    /**
     * The Constant TYPE___PROGRAM.
     */
    public final static String TYPE___PROGRAM = "__program";


    /**
     * channel categry
     */
    public final static String TYPE___VOD_CATEGORY = "__vod_category";

    public final static String TYPE_PROGRAM_CATEGORY = "__program_category";
    public final static String TYPE___PROGRAM_CATEGORY_POSTER = "__program_category_poster";
    public final static String TYPE_GENERAL = "general"; // 서버에서 넘겨받은 CATEGORY 목록 중 하위 카테고리가 있는 경우

    public final static String TYPE_PROGRAM = "program";
    public final static String TYPE_PROGRAM_SEARCH = "program_search";
    /**
     * The Constant TYPE___APP.
     */
    public final static String TYPE___APP = "__app";

    /**
     * 사용자 전환.
     */
    public final static String TYPE_H_USER_CHANGE = "h_user_change";

    /**
     * 공지사항.
     */
    public final static String TYPE_VERSIONS = "application_information";

    public final static String TYPE_ACTIVATE = "quick_purchase";

    public final static String TYPE_G_NOTICE = "announcement_general";
    public final static String TYPE_C_NOTICE = "announcement_channel";
    public final static String TYPE_E_NOTICE = "announcement_event";

    public final static String TAG_ROOT_ANNOUNCEMENT = "accouncement_root";
    /**
     * package
     */

    public final static String TYPE_PACKAGE = "__package";

    /**
     * 추천 VOD.
     */
    public final static String TYPE_H_RECOMMEND = "h_recommend";

    /**
     * 선호VOD 메뉴.
     */
    public final static String TYPE_H_FAVORITE = "h_favorite";

    /**
     * 구매목록.
     */
    public final static String TYPE_V_PURCHASED = "vod_purchased";

    public final static String TYPE_NV_PURCHASED = "monthly_purchased";

    public final static String TAG_ROOT_PURCHASED = "purchase_root";




    /**
     * 쿠폰 충전소.
     */
    public final static String TYPE_H_PURCHASE_COUPON = "h_purchase_coupon";

    /**
     * TV 포인트.
     */
    public final static String TYPE_H_POINT = "h_point";

    /**
     * 일반 결제.
     */
    public final static String TYPE_H_CHARGE_NORMAL = "h_charge_normal";

    /**
     * 휴대폰 결제.
     */
    public final static String TYPE_H_CHARGE_PHONE = "h_charge_phone";

    /**
     * 도서문화상품권.
     */
    public final static String TYPE_H_CHARGE_BOOKCOUPON = "h_charge_bookcoupon";

    /**
     * 보유쿠폰 목록.
     */
    public final static String TYPE_H_COUPON_LIST = "h_coupon_list";


    /**
     * 사용자 정보 변경.
     */
    public final static String TYPE_S_U_USERINFO = "s_u_userinfo";

    /**
     * 제한채널 설정.
     */
    public final static String TYPE_S_U_SKIPPED = "s_u_skipped";

    /**
     * 간편구매 설정.
     */
    public final static String TYPE_S_U_PREFER = "s_u_prefer";

    /*
    *  PrePaid
    * */

    public final static String TYPE_TOP_UP = "topUp";

    public final static String TYPE_TOP_UP_HISTORY = "topUp_history";
/*
* Change Language
* */

    public final static String TYPE_LANGUAGE = "change_language";
    /**
     * TV 잠금 설정.
     */
    public final static String TYPE_S_U_TVLOCK = "lock_tv";

    /**
     * 선호채널 설정.
     */
    public final static String TYPE_S_TV_FAVORITE = "favorite_channels";


    public final static String TYPE_RESUME = "resume_viewing";
    /**
     * 반복 설정.
     */
    public final static String TYPE_S_TV_ALARM_REPEAT = "s_tv_alarm_repeat";

    /**
     * 시간 설정.
     */
    public final static String TYPE_S_TV_ALARM_CLOCK = "s_tv_alarm_clock";

    /**
     * 채널 설정.
     */
    public final static String TYPE_S_TV_ALARM_CHANNEL = "s_tv_alarm_channel";

    /**
     * 화면비율 설정.
     */
    public final static String TYPE_S_TV_RESOLUTION = "s_tv_resolution";

    /**
     * VOD 보기설정.
     */
    public final static String TYPE_S_TV_VOD = "s_tv_vod";

    /**
     * MY Contents
     */
    public final static String TYPE_MY_CONTENT = "my_contents";

    /**
     * 미니 가이드.
     */
    public final static String TYPE_S_TV_MINIGUIDE = "s_tv_miniguide";

    /**
     * 메뉴[Menu] 설정.
     */
    public final static String TYPE_S_TV_LANG_MENU = "s_tv_lang_menu";

    /**
     * 음성[Voice] 설정.
     */
    public final static String TYPE_S_TV_LANG_AUDIO = "s_tv_lang_audio";

    /**
     * 자막방송 설정.
     */
    public final static String TYPE_S_TV_ASSIST_CAPTION = "s_tv_assist_caption";

    /**
     * 해설방송 설정.
     */
    public final static String TYPE_S_TV_ASSIST_IMPAIRED = "s_tv_assist_impaired";

    /**
     * 절전대기모드 설정.
     */
    public final static String TYPE_S_TV_SAVE = "s_tv_save";

    /**
     * 스마트 폰 연결.
     */
    public final static String TYPE_S_TV_OTP = "s_tv_otp";

    /**
     * 이용안내.
     */
    public final static String TYPE_USAGE_INFO = "usage_info";

    /**
     * 시스템 정보.
     */
    public final static String TYPE_SYSTEM_INFO = "system_info";

    /**
     * 셋톱박스 정보.
     */
    public final static String TYPE_STB_INFO = "stb_info";

    /**
     * 응용프로그램 정보.
     */
    public final static String TYPE_APPLICATION_INFO = "application_info";

    /**
     * 수신감도 점검.
     */
    public final static String TYPE_CHECK_SIGNAL = "check_signal";

    /**
     * 사용자 설정.
     */
    public final static String TYPE_ADMIN_USER_SETTING = "s_admin_user_setting";

    /**
     * 제한 비밀번호 설정.
     */
    public final static String TYPE_ADMIN_PIN = "s_admin_pin";

    /**
     * 구매 비밀번호 설정.
     */
    public final static String TYPE_ADMIN_PURCHASE_PIN = "s_admin_purchase_pin";

    /**
     * 영화 추천 VOD.
     */
    public final static String TYPE_M_RECOMMEND = "m_recommend";

    /**
     * VOD 추천 VOD.
     */
    public final static String TYPE_V_RECOMMEND = "recommend_vod";

    /**
     * 선호채널.
     */
    public final static String TYPE_E_FAVORITE = "e_favorite";

    /**
     * 프리미엄 채널.
     */
    public final static String TYPE_E_PREMIUM = "e_premium";

    /**
     * 장르 영화.
     */
    public final static String TYPE_E_GENRE_MOVIE = "e_genre_movie";

    /**
     * 스포츠/취미.
     */
    public final static String TYPE_E_GENRE_SPORTS = "e_genre_sports";

    public final static String TYPE_E_GENRE = "e_genre";
    /**
     * 뉴스/다큐.
     */
    public final static String TYPE_E_GENRE_NEWS = "e_genre_news";

    /**
     * 엔터/음악.
     */
    public final static String TYPE_E_GENRE_ENTERTAINMENT = "e_genre_entertainment";
    /**
     * 국내 종합.
     */
    public final static String TYPE_E_GENRE_KIDS = "e_genre_kids";

    public final static String TYPE_E_GENRE_LOCAL_CHANNEL = "e_genre_local_channel";
    public final static String TYPE_E_GENRE_LOCAL = "e_genre_local";


    /**
     * 교육/어린이.
     */
    public final static String TYPE_E_GENRE_EDUCATION = "e_genre_education";

    /**
     * 교양/정보.
     */
    public final static String TYPE_E_GENRE_INFO = "e_genre_info";

    /**
     * 종교/공공.
     */
    public final static String TYPE_E_GENRE_PUBLIC = "e_genre_public";

    /**
     * 해외.
     */
    public final static String TYPE_E_GENRE_GLOBAL = "e_genre_global";

    /**
     * 홈쇼핑.
     */
    public final static String TYPE_E_GENRE_SHOPPING = "e_genre_shopping";

    /**
     * 전체채널.
     */
    public final static String TYPE_E_ALL = "channel_guide";

    /**
     * 예약 프로그램.
     */
    public final static String TYPE_E_RESERVED = "reserved_list";

    /**
     * 오디오 채널.
     */
    public final static String TYPE_E_AUDIO = "e_audio";

    /**
     * 데이터방송 전체보기.
     */
    public final static String TYPE_J_ALL = "j_all";

    /**
     * 데이터 방송 장르.
     */
    public final static String TYPE_J_GENRE = "j_genre";

    /**
     * 마이 앱.
     */
    public final static String TYPE_A_MY = "a_my";

    /**
     * 앱스토어.
     */
    public final static String TYPE_A_STORE = "a_store";

    /**
     * 브라우저.
     */
    public final static String TYPE_A_BROWSER = "a_browser";

    /**
     * TYPE_SEARCH.
     */
    public final static String TYPE_SEARCH = "search";
    public final static String TYPE_SEARCH_RESULT = "search_result";
    /**
     * Bluetooth Device
     */
    public final static String TYPE_BLUETOOTH_DEVICE_MGT = "s_smart_bluetooth";


    /**
     * Data package
     */

    public final static String TYPE_DATA_PACKAGE = "data_package";
    /**
     * CATCH UP
     */
    public final static String TYPE_CATCH_UP = "catch_up";

    /**
     * facebook.
     */

    public final static String TYPE_FACE_BOOK = "facebook";

    /**
     * 마이홈 ROOT.
     */
    public final static String TAG_MYHOME_ROOT = "myhome_root";

    /**
     * 안내/설정 root.
     */
    public final static String TAG_SETTING_ROOT = "setting_root";

    /**
     * 사용자 설정 ROOT.
     */
    public final static String TAG_USER_SETTING_ROOT = "user_setting_root";

    /**
     * 관리자 설정 ROOT.
     */
    public final static String TAG_ADMIN_SETTING_ROOT = "admin_setting_root";

    /**
     * 모닝알람 설정.
     */
    public final static String TAG_MORNING_ALARM = "morning_alarm";

    /**
     * 언어설정.
     */
    public final static String TAG_LANGUAGE_SETTING = "language_setting";

    /**
     * 시청보조 방송.
     */
    public final static String TAG_ASSIST_SETTING = "assist_setting";

    /**
     * 시스템 안내.
     */
    public final static String TAG_SYSTEM_INFO_ROOT = "system_info_root";

    /**
     * 영화 ROOT.
     */
    public final static String TAG_MOVIE_ROOT = "movie_root";

    /**
     * VOD ROOT.
     */
    public final static String TAG_VOD_ROOT = "vod_root";

    /**
     * TAG_EPG_ROOT.
     */
    public final static String TAG_EPG_ROOT = "epg_root";

    /**
     * 편성표 ROOT.
     */
    public final static String TAG_GENRE_EPG_ROOT = "genre_epg_root";

    /**
     * JOY ROOT.
     */
    public final static String TAG_JOY_ROOT = "joy_root";

    /**
     * SMART ROOT.
     */
    public final static String TAG_SMART_ROOT = "smart_root";


    // custom value CONFIG 내의 값을 읽어올 key

    /**
     * Menu Audience.
     */
    public static final String CONF_AUDIENCE = "audience";

    /**
     * sort option.
     */
    public static final String CONF_SORT = "sort"; /* NAA: name ascending, NAD: name descending, IMD: 최신 순, POP: 인기순 */

    /**
     * Hidden.
     */
    public static final String CONF_HIDDEN = "hidden";

    /**
     * VOD category.
     */
    public static final String CONF_CATEGORY = "__category";

    public static final String CONF_VIEW = "view";
    public static final String VIEW_TYPE_POSTER = "poster";

    /**
     * VOD series.
     */
    public static final String CONF_SERIES = "series"; /* 시리즈 유무 (OPTIONAL, 없으면 FALSE) */

    /**
     * VOD series end.
     */
    public static final String CONF_SERIESEND = "seriesend"; /* 시리즈 종료 여부 (OPTIONAL, 없으면 FALSE) */

    /**
     * VOD No pin.
     */
    public static final String CONF_NOPIN = "nopin"; /* NoPin 정책 유무 (optional, 없으면 false) */

    /**
     * 채널 장르 코드 (optional).
     */
    public static final String CONF_CH_GENRE = "chgenre";

    /**
     * category leaf.
     */
    public static final String CONF_LEAF_CATEGORY = "__leaf_category";

    /*
    * TAG SVOD, Package
    * */
    public static final String TAG_SVOD = "svod";


    public static final String TAG_SETTINGS = "tv_setting_root";
    public static final String TAG_ROOT_HOME = "home_root";
}
