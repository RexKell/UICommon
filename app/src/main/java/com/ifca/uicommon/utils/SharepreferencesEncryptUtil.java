package com.ifca.uicommon.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by rex on 2017/4/27.
 * 功能：AES加密工具
 */

public class SharepreferencesEncryptUtil {
    private String key;
    private static SharepreferencesEncryptUtil instance;
    //加密key
    private final static String SIGN_KEY = "Rex@ifca2016";
    //SHA 加密 ALGORITHM
    private final static String SHA_ALGORITHM = "SHA-256";
    private final static String AES_ALGORITHM = "AES";
    private final static String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";

    public SharepreferencesEncryptUtil(Context context) {
        String serialNo = getDeviceSerialNumber(context);
        key = SHA(serialNo + SIGN_KEY).substring(0, 16);
    }

    public static SharepreferencesEncryptUtil getInstance(Context context) {
        if (instance == null) {
            instance = new SharepreferencesEncryptUtil(context.getApplicationContext());
        }
        return instance;
    }

    private static String getDeviceSerialNumber(Context context) {
        try {
            String deviceSerial = (String) Build.class.getField("SERIAL").get(null);
            if (TextUtils.isEmpty(deviceSerial)) {
                return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                return deviceSerial;
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    /**
     * SHA 加密 key
     *
     * @param strKey 加签key
     * @return
     */
    private String SHA(String strKey) {
        String strResult = null;
        if (strKey != null && strKey.length() > 0) {
            try {
                MessageDigest digest = MessageDigest.getInstance(SHA_ALGORITHM);
                digest.update(strKey.getBytes());
                byte byteBuffer[] = digest.digest();
                StringBuffer strHexString = new StringBuffer();
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }

    /**
     * AES128 加密
     *
     * @param strText 明文
     * @return
     */
    public String encrypt(String strText) {
        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(strText.getBytes());
            return Base64.encodeToString(encrypted, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES128 解密
     *
     * @param
     */
    public String decrypt(String cipherText) {
        try {
            byte[] encryptBytes=Base64.decode(cipherText,Base64.NO_WRAP);
            Cipher cipher=Cipher.getInstance(AES_TRANSFORMATION);
            SecretKeySpec keySpec=new SecretKeySpec(key.getBytes(),AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,keySpec);
            byte[] original=cipher.doFinal(encryptBytes);
            String originalString=new String(original);
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
