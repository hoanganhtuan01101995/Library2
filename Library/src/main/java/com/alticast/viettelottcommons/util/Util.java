package com.alticast.viettelottcommons.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.manager.TimeManager;
import com.alticast.viettelottcommons.resource.request.CreateAccountReq;
import com.alticast.viettelottcommons.resource.request.MyDeviceInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by mc.kim on 8/3/2016.
 */
public class Util {
    private static String HMAC_SHA1 = "HmacSHA1";

    public static MyDeviceInfo generateMyDeviceInfo(String id) {
        String model_no = android.os.Build.MODEL;
        String os_name = android.os.Build.ID;
        String os_version = android.os.Build.VERSION.RELEASE;
        String manufacturer = Build.MANUFACTURER;
        MyDeviceInfo info = new MyDeviceInfo(id, model_no, os_name, os_version, manufacturer);
        return info;
    }

    public static String getRandomHexString(int numchars) {
        char[] CHARSET_19 = "123456789".toCharArray();
        int length = numchars;
        Random random = new Random();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(CHARSET_19.length);
            result[i] = CHARSET_19[randomCharIndex];
        }
        return new String(result);
    }

    public static CreateAccountReq generateAccountInfo(String id, String password, String otp, String udid) {
        CreateAccountReq account = new CreateAccountReq();
        password = AsSHA1.getInstance().encryptHmacSHA1(id, password);
        account.setId(id);
        account.setPassword(password);
        account.setCode(otp);
        account.setDevice(generateMyDeviceInfo(udid));
        account.setClient_id(WindmillConfiguration.clientId);
        account.setHash(getScretKey(id + password + otp + WindmillConfiguration.clientId, WindmillConfiguration.secretKey));
        account.setCellphone(id);
        return account;
    }

    public static String generateSignature(String data, String key) throws SignatureException {

        String result;
        try {
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1);
            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());
            char[] encodedBytes = Base64Coder.encode(rawHmac);
            result = new String(encodedBytes);
        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

    public static String getScretKey(String data, String key) {
        String secret = null;
        try {
            secret = generateSignature(data, key);
        } catch (SignatureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return secret;
    }

    public static synchronized File makeDirectory(String dir_path) {
        File dir = new File(dir_path);
        if (!dir.exists()) {
            dir.mkdirs();
        } else {
        }

        return dir;
    }

    public static File makeFile(File dir, String file_path) {
        File file = null;
        boolean isSuccess = false;
        if (dir.isDirectory()) {

            file = new File(file_path);
            if (file != null && !file.exists()) {
                try {
                    isSuccess = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
            } else {
            }
        }
        return file;
    }

    public static String getCurrentTimeToFileFormat() {

        long now = TimeManager.getInstance().getServerCurrentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String strCurDate = CurDateFormat.format(date);
        return strCurDate + ".CFG";
    }

    public static boolean writeFile(File file, byte[] file_content) {
        boolean result;
        FileOutputStream fos;
        if (file != null && file.exists() && file_content != null) {
            try {
                fos = new FileOutputStream(file);
                try {
                    fos.write(file_content);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            result = true;
        } else {
            result = false;
        }
        return result;
    }


    public static void setupFontUI(final View view, final Typeface tf) {

        if (view instanceof TextView) {

            ((TextView) view).setTypeface(tf);
        }

        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupFontUI(innerView, tf);
            }
        }
    }

    public static boolean isLatestVersion(Context context, String serverVersion) {


        try {
            String packageName = context.getPackageName();
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            String[] serverVersions = serverVersion.split("\\.");
            String[] appVersions = packageInfo.versionName.split("\\.");
            for (int i = 0; i < Math.min(serverVersions.length, appVersions.length); i++) {
                if (Integer.parseInt(serverVersions[i]) > Integer.parseInt(appVersions[i])) {
                    return false;
                } else if (Integer.parseInt(serverVersions[i]) < Integer.parseInt(appVersions[i])) {
                    break;
                }
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return true;
    }

    public static boolean isTablet(Context context) {

        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public static String md5(String s) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            BigInteger i = new BigInteger(1,m.digest());
            return String.format("%1$032x", i);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
