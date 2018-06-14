package com.alticast.viettelottcommons.util;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AsSHA1 {
	private static AsSHA1 instance = null;
	
	private AsSHA1() {
    }

    public static AsSHA1 getInstance() {

        if (instance == null) {
            instance = new AsSHA1();
        }
        return instance;
    }
	
	
    /**
     * @param userId : login id
     * @param password : login password
     * @return sha1 key
     */
    public String encryptHmacSHA1(String userId, String password) {
    	Logger.print("String", "encryptHmacSHA1 userId   :  "+userId);
    	Logger.print("String", "encryptHmacSHA1 password   :  "+password);
    	String initKey = password;
    	String baseString =userId+password;
        String signature = null;
        try {
            SecretKey key = new SecretKeySpec(initKey.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(key);
            byte[] doFinal = mac.doFinal(baseString.getBytes());
            signature = Base64.encodeToString(doFinal, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException ignored) {
        } catch (InvalidKeyException ignored) {
        }
        Logger.print("String", "encryptHmacSHA1 signature   :  "+signature);
        return signature;
    }

}
