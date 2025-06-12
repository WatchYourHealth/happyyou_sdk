package com.wyh.happyyousdk.crypto;

import android.os.Build;


import com.wyh.happyyousdk.BuildConfig;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class RSAEncryption {

    public static String rsaEncrypt(String str) {
        String publicKey = BuildConfig.PUBLIC_KEY;
        try {
            byte[] keyBytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                keyBytes = Base64.getDecoder().decode(publicKey);
            }
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            return rsaEncrypt(str, pubKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
    public static String rsaEncrypt(String str, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(str.getBytes());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(encryptedBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String callDecryptionMethod(String base64EncodedMessage) {
        try {
            String privateKEY = BuildConfig.PRIVATE_KEY;

            byte[] keyBytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                keyBytes = Base64.getDecoder().decode(privateKEY);
            }
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            byte[] encryptedBytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                encryptedBytes = Base64.getDecoder().decode(base64EncodedMessage);
            }

            return rsaDecrypt(encryptedBytes, privateKey);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String rsaDecrypt(byte[] encryptedBytes, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
