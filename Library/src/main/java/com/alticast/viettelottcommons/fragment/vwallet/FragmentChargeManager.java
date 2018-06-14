package com.alticast.viettelottcommons.fragment.vwallet;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.fragment.FragmentBase;
import com.alticast.viettelottcommons.resource.ChargeAmountObj;
import com.alticast.viettelottcommons.resource.ChargedResult;
import com.alticast.viettelottcommons.resource.WalletTopupMethod;

/**
 * Created by duyuno on 9/9/17.
 */
public class FragmentChargeManager {

    public static WalletTopupMethod walletTopupMethod;
    public static boolean isTablet;


    public static void goToChargePhoneMainOptions(GlobalActivity activity, WalletTopupMethod walletTopupMethod, boolean isTablet, int way) {
        FragmentPhoneMain fragmentChargePhoneMain = FragmentPhoneMain.newInstance();
        fragmentChargePhoneMain.setWalletTopupMethod(walletTopupMethod, isTablet);
        FragmentChargeManager.walletTopupMethod =walletTopupMethod;
        FragmentChargeManager.isTablet = isTablet;

        if(!isTablet) {
            activity.setVWalletChargeTitle(activity.getString(R.string.vWallet_select_topup_title));
        }

        replaceFragment(activity, fragmentChargePhoneMain, way);
    }
    public static void goToChargeScratch(GlobalActivity activity, WalletTopupMethod walletTopupMethod, boolean isTablet) {
        FragmentChargeScratch fragmentChargePhoneMain = FragmentChargeScratch.newInstance();
        fragmentChargePhoneMain.setWalletTopupMethod(walletTopupMethod);
        FragmentChargeManager.walletTopupMethod =walletTopupMethod;
        FragmentChargeManager.isTablet = isTablet;

        if(!isTablet) {
            activity.setVWalletChargeTitle(activity.getString(R.string.vWallet_topup_via_scard_title));
        }

        replaceFragment(activity, fragmentChargePhoneMain, -1);
    }
    public static void goToChargePhoneMainOptions(GlobalActivity activity, int way) {
        FragmentPhoneMain fragmentChargePhoneMain = FragmentPhoneMain.newInstance();
        fragmentChargePhoneMain.setWalletTopupMethod(walletTopupMethod, isTablet);
        FragmentChargeManager.walletTopupMethod =walletTopupMethod;
        replaceFragment(activity, fragmentChargePhoneMain, way);
        if(!isTablet) {
            activity.setVWalletChargeTitle(activity.getString(R.string.vWallet_select_topup_title));
        }
    }
    public static void goToChargePhoneEnterAmount(GlobalActivity activity, int way) {
        FragmentPhoneEnterAmout fragmentChargePhoneMain = FragmentPhoneEnterAmout.newInstance();
        replaceFragment(activity, fragmentChargePhoneMain, way);

        if(!isTablet) {
            activity.setVWalletChargeTitle(activity.getString(R.string.enterChargeAmountTitle));
        }
    }
    public static void goToChargePhoneGetOtp(GlobalActivity activity, ChargeAmountObj chargeAmountObj, int way) {
        FragmentChargePhoneGetOTP fragmentChargePhoneMain = FragmentChargePhoneGetOTP.newInstance();
        fragmentChargePhoneMain.setChargeAmountObj(chargeAmountObj);
        if(!isTablet) {
            activity.setVWalletChargeTitle(activity.getString(R.string.titleEnterMobilePhoneNumber));
        }
        replaceFragment(activity, fragmentChargePhoneMain, way);
    }
    public static void goToChargePhoneAction(GlobalActivity activity, ChargeAmountObj chargeAmountObj, String phoneNumber, int way) {
        FragmentChargePhoneAction fragmentChargePhoneMain = FragmentChargePhoneAction.newInstance();
        fragmentChargePhoneMain.setChargeAmountObj(chargeAmountObj, phoneNumber);
        if(!isTablet) {
            activity.setVWalletChargeTitle(activity.getString(R.string.vWalletEnterOTPTitle));
        }
        replaceFragment(activity, fragmentChargePhoneMain, way);
    }
    public static void goToChargePhoneConfirm(GlobalActivity activity,ChargeAmountObj chargeAmountObj, String otp, String phoneNumber, int way) {
        FragmentChargePhoneConfirm fragmentChargePhoneMain = FragmentChargePhoneConfirm.newInstance();
        fragmentChargePhoneMain.setChargeAmountObj(chargeAmountObj, otp, phoneNumber);

        if(!isTablet) {
            activity.setVWalletChargeTitle(activity.getString(R.string.vWalletChargeConfirmTitle));
        }

        replaceFragment(activity, fragmentChargePhoneMain, way);
    }
    public static void goToChargePhoneComplete(GlobalActivity activity,ChargedResult chargedResult) {
//        FragmentChargePhoneComplete fragmentChargePhoneMain = FragmentChargePhoneComplete.newInstance();
//        fragmentChargePhoneMain.setChargeAmountObj(chargeAmountObj);
//        activity.replaceFragment(activity, fragmentChargePhoneMain,  way);
        activity.replaceVWalletComplete(chargedResult);
    }

    public static void closeVWallet(GlobalActivity globalActivity) {
        globalActivity.closeVWallet();
    }

    public static void replaceFragment(GlobalActivity activity, Fragment fragment, int way) {
        activity.replaceFragment(fragment,com.alticast.viettelottcommons.R.id.contentWallet, way);
    }

    public static FragmentBase getCurrentFragment(AppCompatActivity activity) {
        return (FragmentBase) (activity.getSupportFragmentManager().findFragmentById(R.id.contentWallet));
    }
}
